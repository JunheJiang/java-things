package com.yzxie.java.demo.charter3.part_3_3;

import java.util.ArrayDeque;
import java.util.Date;
import java.util.Queue;

/**
 * @author xieyizun
 * @date 16/6/2019 15:19
 * @description:
 */
public class SynchronizedProConDemo {

    // 监视器对象，用于对生产者和消费者进行同步
    private Object monitor;

    // 存放产品的队列的容量大小
    private int capacity;

    // 存储产品的队列的大小为1，故只能存放一个产品
    private Queue<String> products;

    public SynchronizedProConDemo(Object monitor, int capacity) {
        this.monitor = monitor;
        this.capacity = capacity;
        this.products = new ArrayDeque<>(capacity);
    }

    // 消费者消费产品方法定义
    public void consume() {
        while (true) {
            synchronized (monitor) {
                // 当队列不存在任何产品时则消费者等待
                while (products.size() == 0) {
                    try {
                        System.out.println("consumer wait at time: " + new Date());
                        monitor.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                // 从队列头部取出产品
                String product = products.poll();
                // 通知生产者可以继续生产产品
                monitor.notifyAll();
                System.out.println("Consumer get " + product + " at time: " + new Date());
            }
        }
    }

    // 生产者生产产品
    public void produce() {
        while (true) {
            // 生成产品
            synchronized (monitor) {
                // 队列不存在空间可以继续存放产品则生产者等待
                while (products.size() == capacity) {
                    try {
                        System.out.println("producer wait at time: " + new Date());
                        monitor.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

                // 模拟生产者产品填充到队列
                System.out.println("producer put product at time: " + new Date());
                products.offer("product");

                // 通知消费者消费，使用notifyAll而不是notify，避免通知到生产者自身，导致"假死"现象
                monitor.notifyAll();
            }

            // 暂停5秒，模拟生产者每隔5秒生产一个产品
            try {
                Thread.sleep(5000);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        Object monitor = new Object();
        SynchronizedProConDemo producerConsumer = new SynchronizedProConDemo(monitor, 1);

        // 生产者
        Thread producerThread = new Thread(new Runnable() {
            @Override
            public void run() {
                producerConsumer.produce();
            }
        });

        // 消费者
        Thread consumerThread = new Thread(new Runnable() {
            @Override
            public void run() {
                producerConsumer.consume();
            }
        });

        consumerThread.start();
        producerThread.start();
    }
}
