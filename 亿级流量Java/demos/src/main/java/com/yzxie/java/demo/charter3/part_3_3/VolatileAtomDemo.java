package com.yzxie.java.demo.charter3.part_3_3;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author xieyizun
 * @date 17/6/2019 23:37
 * @description:
 */
public class VolatileAtomDemo {
    // 累加和
    //private static volatile int sum = 0;
    private static AtomicInteger sum = new AtomicInteger(0);

    public static void main(String[] args) {
        try {
            // 当20个线程都递增完成，则在主线程打印累加和sum
            int times = 20;
            CountDownLatch countDownLatch = new CountDownLatch(times);
            for (int i = 0; i < times; i++) {
                Thread thread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        // 每个线程累加100000次
                        for (int i = 0; i < 100000; i++) {
                            // ++操作是复合操作
                            //sum++;
                            sum.incrementAndGet();

                        }
                        countDownLatch.countDown();
                    }
                });
                thread.start();
            }

            countDownLatch.await();
            System.out.println("sum is " + sum);

        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
