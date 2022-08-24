package com.yzxie.java.demo.charter7.part_7_4;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

/**
 * Author: xieyizun
 * Version: 1.0
 * Date: 2019-09-22
 * Description:
 **/
public class CyclicBarrierDemo {

    public static void main(String[] args) {
        float[][] data = {{1,2,3}, {4,5,6}};
        CyclicBarrierDemo cyclicBarrierDemo = new CyclicBarrierDemo(data);
        System.out.println("第一次执行");
        cyclicBarrierDemo.startWork();
        // 执行第二次
        System.out.println("第二次执行");
        cyclicBarrierDemo.reset();
        cyclicBarrierDemo.startWork();

    }

    // 矩阵数据
    private final int N;
    private final float[][] data;
    private final CyclicBarrier barrier;

    // 矩阵计算入口类，matrix两维数组为矩阵数据
    CyclicBarrierDemo(float[][] matrix) {
        data = matrix;
        N = matrix.length;
        // 等栅栏打开，自动执行该任务
        Runnable barrierAction = new Runnable() {
            public void run() {
                // mergeRows合并
                mergeRows();
            }
        };

        // 新建栅栏
        // 指定线程集合的大小N
        // 指定栅栏打开时需要执行的任务barrierAction，合并矩阵各行的计算结果
        barrier = new CyclicBarrier(N, barrierAction);
    }

    public void startWork() {
        List<Thread> threads = new ArrayList<>(N);
        for (int i = 0; i < N; i++) {
            Thread thread = new Thread(new Worker(i, barrier));
            threads.add(thread);
            thread.start();
        }

        // 等待所有子线程完成
        for (Thread thread : threads) {
            try {
                thread.join();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void reset() {
        // 重置，重新执行一次
        barrier.reset();
    }

    // 合并所有行的处理结果
    private void mergeRows() {
        System.out.println("all done.");
    }

    // 计算矩阵某行的任务定义
    private class Worker implements Runnable {
        private int myRow;
        private CyclicBarrier barrier;
        private boolean done;

        Worker(int row, CyclicBarrier barrier) {
            this.myRow = row;
            this.barrier = barrier;
        }

        public void run() {

            // 检查是否完成计算
            while (!done) {
                // 执行计算
                processRow();

                try {
                    // 等待其他线程处理完毕则返回，
                    // 即N个线程都调用了barrier.await
                    barrier.await();
                } catch (InterruptedException ex) {
                    return;
                } catch (BrokenBarrierException ex) {
                    return;
                }
            }
        }

        private void processRow() {
            // 省略处理行的代码
            float[] rowData = data[myRow];
            System.out.print("Thread " + Thread.currentThread().getId() + " process row: " + myRow + ", data=");
            for (int i = 0; i < rowData.length; i++) {
                System.out.print(rowData[i]+", ");
            }
            System.out.println();
            // 完成
            done = true;
        }
    }
}
