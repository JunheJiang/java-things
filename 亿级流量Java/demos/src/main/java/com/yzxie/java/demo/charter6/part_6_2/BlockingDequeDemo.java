package com.yzxie.java.demo.charter6.part_6_2;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author xieyizun
 * @date 19/5/2019 17:31
 * @description:
 */
public class BlockingDequeDemo {
    public static void main(String[] args) {
        // 生产者和消费者共享同一个队列
        BlockingDeque deque1 = new LinkedBlockingDeque();
        // 生产者线程
        Producer producer1 = new Producer("fasterConsumer", deque1, new AtomicInteger(0));

        // 生产者和消费者共享同一个队列
        BlockingDeque deque2 = new LinkedBlockingDeque();
        // 生产者线程
        Producer producer2 = new Producer("slowConsumer", deque2, new AtomicInteger(0));

        // 消费者线程，每隔2秒消费一次
        Consumer fastConsumer = new Consumer("fastConsumer", "slowConsumer", deque1, deque2, 2000);
        // 消费者线程，每隔4秒消费一次
        Consumer slowConsumer = new Consumer("slowConsumer", "fastConsumer",  deque2, deque1, 4000);

        new Thread(producer1).start();
        new Thread(producer2).start();

        // 等待以上生产者线程生产完毕
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // 生产者先生产好全部数据
        new Thread(fastConsumer).start();
        new Thread(slowConsumer).start();
    }

    /**
     * 生产者
     */
    static class Producer implements Runnable {
        // 消费者名称
        private final String consumerName;
        // 数据存储队列
        private final BlockingQueue queue;
        // 产品序列号
        private final AtomicInteger productSeq;

        Producer(String consumerName, BlockingQueue queue, AtomicInteger productSeq) {
            this.consumerName = consumerName;
            this.queue = queue;
            this.productSeq = productSeq;
        }

        @Override
        public void run() {
            try {
                // 总共生成20个产品
                for (int i = 0; i < 20; i++) {
                    // put：如果队列满了，则阻塞
                    queue.put(produce());
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        // 生产产品
        Object produce() {
            return "product-" + productSeq.incrementAndGet() + " for " + consumerName;
        }
    }

    /**
     * 消费者
     */
    static class Consumer implements Runnable {
        private final String name;
        private final String otherName;
        // 数据存储队列
        private final BlockingDeque myDeque;
        // 包含其他线程的队列引用，从而进行窃取
        private final BlockingDeque otherDeque;
        // 每隔intervalMs毫秒消费一个产品
        private int intervalMs;

        public Consumer(String name, String otherName, BlockingDeque myDeque, BlockingDeque otherDeque, int intervalMs) {
            this.name = name;
            this.otherName = otherName;
            this.myDeque = myDeque;
            this.otherDeque = otherDeque;
            this.intervalMs = intervalMs;
        }

        @Override
        public void run() {
            try {
                while (true) {
                    // 从队列头部读取数据
                    Object product = myDeque.pollFirst();
                    if (product != null) {
                        consume(product);

                        // 休眠intervalMs秒
                        Thread.sleep(intervalMs);
                        continue;
                    }

                    // 从队列尾部，窃取其他线程的数据
                    Object otherProduct = otherDeque.pollLast();
                    if (otherProduct != null) {
                        System.out.println(name + " steal data from " + otherName);
                        consume(otherProduct);
                    }
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        // 消费产品
        void consume(Object product) {
            System.out.println(name + " consume " + product);
        }
    }
}
