package com.yzxie.java.demo.charter5.part_5_3;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author xieyizun
 * @date 18/5/2019 20:54
 * @description:
 */
public class ConcurrentHashMapDemo {
    public static void main(String[] args) {
        //testSetValue();
        testInteger();
    }

    /**
     * 集合类型测试
     */
    static void testSetValue() {
        final AtomicInteger counter = new AtomicInteger();

        // 本地缓存定义，key为String类型，value为Set类型
        final ConcurrentHashMap<String, Set<String>> subscribeCache =
                new ConcurrentHashMap<>();

        // 订阅主题
        final String topic = "mytopic";

        // 线程producer负责填充缓存subscribeCache
        Thread producer = new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    Set<String> devices = subscribeCache.get(topic);
                    if (devices == null) {
                        // HashSet不是线程安全的
                        // devices = new HashSet<>();

                        // ConcurrentSkipListSet为线程安全的
                        devices = new ConcurrentSkipListSet<>();

                        subscribeCache.put(topic, devices);
                    }

                    // 模拟生成客户端设备ID
                    devices.add("device" + counter.incrementAndGet());

                    // 每隔2秒写入一次
                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        // 线程consumer负责读取缓存subscribeCache
        Thread consumer = new Thread(new Runnable() {
            @Override
            public void run() {
                while (subscribeCache.size() > 0) {

                    // 打印整个value的值，内部会使用Set接口实现类的迭代器
                    System.out.println(subscribeCache.get(topic));

                    // 每隔2秒读取一次
                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

// 启动线程
        producer.start();
        consumer.start();
    }

    /**
     * Integer类型测试
     */
    static void testInteger() {
        // 计数器
//        final Integer counter = new Integer(0);
//        ConcurrentHashMap<String, Integer> counterMap = new ConcurrentHashMap<>(1);

        // 线程安全版本
        final AtomicInteger counter = new AtomicInteger(0);
        ConcurrentHashMap<String, AtomicInteger> counterMap = new ConcurrentHashMap<>(1);
        counterMap.put("counter", counter);
        // 两个线程同时开始的开关
        CountDownLatch starter = new CountDownLatch(1);

        // 两个线程都是每隔2秒递增一次
        Thread thread1 = new Thread(new CounterTask(starter, counterMap));
        Thread thread2 = new Thread(new CounterTask(starter, counterMap));
        thread1.start();
        thread2.start();
         // 同时启动
        starter.countDown();

        // 等待以上两个线程执行完成
        try {
            thread1.join();
            thread2.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        // 打印最后结果
        System.out.println(counterMap.get("counter"));
    }

    // 计数任务定义
    private static class CounterTask implements Runnable  {
        private CountDownLatch starter;
        // private ConcurrentHashMap<String, Integer> counterMap;
        private ConcurrentHashMap<String, AtomicInteger> counterMap;

        CounterTask(CountDownLatch starter, ConcurrentHashMap<String, AtomicInteger> counterMap) {
            this.starter = starter;
            this.counterMap = counterMap;
        }

        @Override
        public void run() {
            try {
                starter.await();
                for (int i = 0; i < 10; i++) {
                    // Integer counter = counterMap.get("counter");
                    // counterMap.put("counter", ++counter);
                    AtomicInteger counter = counterMap.get("counter");
                    counter.incrementAndGet();
                    counterMap.put("counter", counter);
                    System.out.println(Thread.currentThread().getName() + ":" + counterMap.get("counter"));
                    // 每隔2秒递增一次
                    Thread.sleep(2000);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
