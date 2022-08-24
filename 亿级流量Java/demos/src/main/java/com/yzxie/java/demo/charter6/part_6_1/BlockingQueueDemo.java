package com.yzxie.java.demo.charter6.part_6_1;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author xieyizun
 * @date 19/5/2019 16:57
 * @description:
 */
public class BlockingQueueDemo {
    public static void main(String[] args) {
        // 生产者和消费者共享同一个队列
        BlockingQueue q = new LinkedBlockingQueue<>();
        // 一个生产者线程
        Producer p = new Producer(q);
        // 两个消费者线程
        Consumer c1 = new Consumer(q);
        Consumer c2 = new Consumer(q);

        new Thread(p).start();
        new Thread(c1).start();
        new Thread(c2).start();
    }

    // 产品序列号
    private static AtomicInteger seq = new AtomicInteger(0);

    /**
     * 生产者
     */
    static class Producer implements Runnable {
        private final BlockingQueue queue;

        Producer(BlockingQueue q) { queue = q; }

        @Override
        public void run() {
            try {
                while (true) {
                    // put：如果队列满了，则阻塞
                    queue.put(produce());

                    Thread.sleep(2000);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        // 生产产品
        Object produce() {
            return "product-" + seq.incrementAndGet();
        }
    }

    /**
     * 消费者
     */
    static class Consumer implements Runnable {
        private final BlockingQueue queue;

        Consumer(BlockingQueue q) { queue = q; }

        @Override
        public void run() {
            try {
                while (true) {
                    // take：如果队列空，则阻塞
                    consume(queue.take());
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        // 消费产品
        void consume(Object product) {
            System.out.println(Thread.currentThread().getName() + " consume " + product);
        }
    }
}
