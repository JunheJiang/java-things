package com.yzxie.java.demo.charter7.part_7_3;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Author: xieyizun
 * Version: 1.0
 * Date: 2019-09-22
 * Description:
 **/
public class CountDownLatchDemo {

    private static final int N = 10;

    // 主方法
    public static void main(String[] args) {

        // 大任务拆成 N 个小任务
        CountDownLatch doneSignal = new CountDownLatch(N);

         // 线程池
        ExecutorService threadPool = Executors.newCachedThreadPool();
        // 创建 N 个线程来执行这 N 个小任务
        for (int i = 0; i < N; ++i) {
            threadPool.execute(new WorkerRunnable(doneSignal, i));
        }

        // 在主线程等待这些子线程执行完成，即等待 CountDownLatch 的数值为 0 doneSignal.await();
        // 执行汇总操作
        try {
            doneSignal.await();
            System.out.println("all done.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 子任务定义
    private static class WorkerRunnable implements Runnable {
        private final CountDownLatch doneSignal;
        private final int i;

        WorkerRunnable(CountDownLatch doneSignal, int i) {
            this.doneSignal = doneSignal;
            this.i = i;
        }
        public void run() {
            try {
                // 子任务工作
                doWork(i);

                // 子任务工作完成，调用 CountDownLatch 的 countDown 方法来递减计时器的数值
                doneSignal.countDown();
            } catch (Exception e) {
                e.printStackTrace();
            } // return;

        }

        void doWork(int taskId) {
            System.out.println("process task: " + taskId);
        }
    }
}
