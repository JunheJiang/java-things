package com.cmg.java.亿级流量架构书.high_availlable.limiting.component;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.common.collect.Lists;
import com.google.common.io.Files;
import com.google.common.util.concurrent.RateLimiter;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;
import rx.Observable;
import rx.Subscriber;
import rx.schedulers.Schedulers;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

@Component
@Slf4j
public class LimitingComponent {

    //限流算法

    //令牌桶算法：固定容量令牌桶、固定速率添加令牌
    //固定速率往桶里添加令牌、
    //桶满时、丢弃或拒绝加入令牌
    //桶中令牌富足时：请求到达时、删除令牌
    //桶中令牌不足时：丢弃拒绝或缓冲请求
    //拿令牌处理请求、允许突发流量

    //漏桶算法：固定容量漏桶、常量固定速率流出水滴
    //桶空时：不流出水滴
    //可以任意速率流入漏桶
    //超出漏桶 丢弃
    //拿常量流出水滴处理对应数目请求、达到流量整形和平滑请求流入速率的目的


    Semaphore semaphore = new Semaphore(100);
    AtomicLong atomicLong = new AtomicLong(0);

    /**
     * 限制接口的总并发和请求数
     */
    public void limitInterfaceTotalReq() {
        boolean canAccess = semaphore.tryAcquire();
        if (canAccess) {
            try {
                //todo
                log.info("limitInterfaceTotalReq handled it..");
            } finally {
                semaphore.release();
            }
        } else {
            //throw exception
        }
        try {
            if (atomicLong.incrementAndGet() > 100) {
                //refuse
                log.info("limitInterfaceTotalReq refuse it..");
            }
            //todo
            log.info("limitInterfaceTotalReq handled it..");
        } finally {

        }
    }

    /**
     * 限制接口的时间窗口内请求数
     * 对每秒每分钟的调用进行限速
     * 以避免服务被打卦
     */
    public void limitTimeWindowReq() throws ExecutionException {
        LoadingCache<Long, AtomicLong>
                counter = CacheBuilder.newBuilder()
                .expireAfterWrite(2, TimeUnit.SECONDS)
                .build(new CacheLoader<Long, AtomicLong>() {
                    @Override
                    public AtomicLong load(Long aLong) throws Exception {
                        return new AtomicLong(0);
                    }
                });
        long limit = 1000;
        while (true) {
            long currentSeconds = System.currentTimeMillis() / 1000;
            if (counter.get(currentSeconds).incrementAndGet() > limit) {
                log.error("限流了");
                continue;
            }
            //todo sth
            log.info("limitTimeWindowReq handled it..");
        }
    }

    /**
     * 平滑突发限流
     */
    public void smoothBursty() {
        RateLimiter limiter = RateLimiter.create(10);
        if (limiter.tryAcquire()) {
            log.info("smoothBursty handle it...");
        } else {
            log.info("smoothBursty refuse it...");
        }
    }

    public static void smoothBurstyCase1() {
        //令牌桶算法
        //每秒钟5个令牌 流入速率每200毫秒一个
        //桶容量为5 每秒新增5个令牌
        RateLimiter limiter = RateLimiter.create(5);
        System.out.println(limiter.acquire());
        System.out.println(limiter.acquire());
        System.out.println(limiter.acquire());
        System.out.println(limiter.acquire());
        System.out.println(limiter.acquire());
//        0.0
//        0.193791
//        0.191054
//        0.198847
//        0.199209
    }

    /**
     * 允许一定程度的突发
     */
    public static void smoothBurstyCase2() {
        RateLimiter limiter = RateLimiter.create(5);
        System.out.println(limiter.acquire(5));
        System.out.println(limiter.acquire());
        System.out.println(limiter.acquire());
        System.out.println(limiter.acquire());
//        0.0
//        0.996192
//        0.19063
//        0.195939
    }

    /**
     * 允许消费未来令牌
     */
    public static void smoothBurstyCase3() {
        RateLimiter limiter = RateLimiter.create(5);
        System.out.println(limiter.acquire(10));
        System.out.println(limiter.acquire());
        System.out.println(limiter.acquire());
        System.out.println(limiter.acquire());
//        1.998093
//        0.194684
//        0.19734
    }


    public static void main(String[] args) throws InterruptedException {
//        LimitingComponent.smoothBurstyCase1();
//        LimitingComponent.smoothBurstyCase2();
//        LimitingComponent.smoothBurstyCase3();
        LimitingComponent.smoothWarmingUpCase1();
    }


    /**
     * 平滑预热限流
     */
    public void smoothWarmingUp() {
        RateLimiter limiter = RateLimiter.create(5,
                1000, TimeUnit.MICROSECONDS);
        if (limiter.tryAcquire()) {
            log.info("smoothWarmingUp handle it...");
        } else {
            log.info("smoothWarmingUp refuse it...");
        }
    }

    /**
     * 冷启动到平均速率
     * 开始小一点
     * 后慢慢趋于平均速率
     * 梯形上升速率
     *
     * @throws InterruptedException
     */
    public static void smoothWarmingUpCase1() throws InterruptedException {
        RateLimiter limiter = RateLimiter.create(5,
                1000, TimeUnit.MICROSECONDS);
        for (int i = 1; i < 5; i++) {
            System.out.println(limiter.acquire());
        }
        Thread.sleep(1000l);
        for (int i = 1; i < 5; i++) {
            System.out.println(limiter.acquire());
        }
//        0.0
//        0.19824
//        0.191788
//        0.196459
//        0.0
//        0.200278
//        0.197376
//        0.194709
    }

    /**
     * 分布式限流
     */
    public static boolean distributedLimiting() throws IOException {
        //redis 操作数
        String luaScript = Files.toString(new File("limit.lua"),
                Charset.defaultCharset());
        String key = "ip:" + System.currentTimeMillis() / 1000;
        //限流大小
        String limit = "3";
        Jedis jedis = new Jedis("", 6379);
        //参数一 KEYS[1] key
        //参数二 ARGV[1] 限流大小
        return (Long) jedis.eval(luaScript,
                Lists.newArrayList(key), Lists.newArrayList(limit)) == 1;
    }

    //接入层限流

    /**
     * 节流
     * first
     * last
     * withTimeout debounce 去抖打字搜索框 搜索下拉框等控件
     */
    public void throttle() {
        Observable.create(new Observable.OnSubscribe<Integer>() {
            @SneakyThrows
            @Override
            public void call(Subscriber<? super Integer> subscriber) {
                next(subscriber, 1, 0);
                next(subscriber, 1, 50);
                next(subscriber, 1, 50);
                next(subscriber, 1, 30);
                next(subscriber, 1, 40);
                next(subscriber, 1, 130);//300ms
                subscriber.onCompleted();
            }
        }).subscribeOn(Schedulers.newThread())
                .throttleWithTimeout(100, TimeUnit.MICROSECONDS)
                .subscribe(new Subscriber<Integer>() {
                    @Override
                    public void onCompleted() {

                    }
                    //小于最小间隔时间或未等到最新事件来
                    // 处理后一个事件

                    @Override
                    public void onError(Throwable throwable) {

                    }

                    @Override
                    public void onNext(Integer integer) {
                        log.info("====" + integer);
                    }
                });
    }

    public void next(Subscriber subscriber, int i, long millis) throws InterruptedException {
        Thread.sleep(millis);
        subscriber.onNext(i);

    }
}
