package com.yzxie.java.demo.charter7.part_7_3;

import java.util.concurrent.CountDownLatch;

/**
 * Author: xieyizun
 * Version: 1.0
 * Date: 2019-09-22
 * Description:
 **/
public class CountDownLatchDemo2 {
    private static int N = 10;

    // 主方法
    public static void main(String[] args) {
        // 启动控制开关，计时器的数值为1
        CountDownLatch startSignal = new CountDownLatch(1);

        // N个线程都准备就绪开关
        CountDownLatch doneSignal = new CountDownLatch(N);

        for (int i = 0; i < N; ++i) {
            new Thread(new Worker(startSignal, doneSignal, i)).start();
        }

        // 打开控制开关，即递减计时器的数值，由于初始值为1，故递减一次就是0
        startSignal.countDown();

        try {
            // 等待N个线程都完成
            doneSignal.await();
            System.out.println("all done.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static class Worker implements Runnable {
        // 控制开关
        private final CountDownLatch startSignal;
        // 完成通知
        private final CountDownLatch doneSignal;

        private final int taskId;

        Worker(CountDownLatch startSignal, CountDownLatch doneSignal, int taskId) {
            this.startSignal = startSignal;
            this.doneSignal = doneSignal;
            this.taskId = taskId;
        }
        public void run() {
            try {
                // 等待主线程打开控制开关
                startSignal.await();
                // 工作
                doWork(taskId);
                // 该子线程完成了自身工作，调用countDown方法通知主线程
                doneSignal.countDown();
            } catch (InterruptedException ex) {}
        }

        void doWork(int taskId) {
            System.out.println("process task: " + taskId + " at " + System.currentTimeMillis());
        }
    }
}
