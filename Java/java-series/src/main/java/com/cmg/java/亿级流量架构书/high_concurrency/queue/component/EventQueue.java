package com.cmg.java.亿级流量架构书.high_concurrency.queue.component;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import lombok.Data;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

@Data
public class EventQueue {
    //redis队列实例
    RedisTemplate queueRedis;
    Integer processingErrorRetryCount = 2;
    //redis等待队列名称
    String queueName = "product";
    String processingQueueName;
    //等待队列拉一份会备份一份
    long maxBakSize = 200000000;
    long awaitMillis;
    ReentrantLock lock = new ReentrantLock();
    Condition notEmpty;

    /**
     * 获取任务id
     *
     * @return
     */
    public String next() {
        while (true) {
            //暂停消费 queueName 等待队列
            String id = null;
            try {
                //等待队列push到本地队列
                id = (String) queueRedis.opsForList().rightPopAndLeftPush(queueName, processingQueueName);
            } catch (Exception e) {
                //发生网络异常
                //将本地队列长时间未消费的任务推送回等待队列
                continue;
            }
            if (id != null) {
                awaitMillis = 300L;
                return id;
            }
            lock.lock();
            try {
                //如果没有任务则休息一下稍后处理防止死循环耗死cpu
                if (awaitMillis < 1000) {
                    awaitMillis = awaitMillis + awaitMillis;
                }
                notEmpty.await(awaitMillis, TimeUnit.MILLISECONDS);
            } catch (Exception e) {
                //ignore
            } finally {
                lock.unlock();
            }
            return "";
        }
    }

    /**
     * 成功后从本地任务队列删除该任务
     *
     * @param id
     */
    public void success(String id) {
        queueRedis.opsForList().remove(processingQueueName, 0, id);
    }

    LoadingCache<String, AtomicInteger> failedCache =
            CacheBuilder.newBuilder()
                    .softValues()
                    .maximumSize(10000)
                    .build(new CacheLoader<String, AtomicInteger>() {
                        @Override
                        public AtomicInteger load(String s) throws Exception {
                            return new AtomicInteger(0);
                        }
                    });

    public void fail(String id) {
        final int failedCount = failedCache.getUnchecked(id).incrementAndGet();
        if (failedCount < processingErrorRetryCount) {
            //添加到等待队列对未
        } else {
            //加入失败队列
        }

    }
}
