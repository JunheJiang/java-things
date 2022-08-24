package com.yzxie.java.demo.charter4.part_4_7;

import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveTask;

/**
 * Author: xieyizun
 * Version: 1.0
 * Date: 2019-09-13
 * Description:
 **/
public class ForkJoinDemo1 {
    // 实现版本1
    private static class FibonacciTask extends RecursiveTask<Integer> {
        // n为斐波那契数列的第几项
        final int n;
        FibonacciTask(int n) { this.n = n; }
        protected Integer compute() {
            // 递归的退出条件
            if (n <= 1)
                return n;
            FibonacciTask f1 = new FibonacciTask(n - 1);

            // 调用fork方法，分解出子任务
            f1.fork();

            FibonacciTask f2 = new FibonacciTask(n - 2);
            // f2不分解
            return f2.compute() + f1.join();
        }
    }

    // 实现版本2
    private static class FibonacciTask2 extends RecursiveTask<Integer> {
        // n为斐波那契数列的第几项
        final int n;
        FibonacciTask2(int n) { this.n = n; }

        @Override
        protected Integer compute() {
            // 递归的退出条件
            if (n <= 1)
                return n;
            FibonacciTask2 f1 = new FibonacciTask2(n - 1);
            // 调用fork方法，分解出子任务
            f1.fork();

            FibonacciTask2 f2 = new FibonacciTask2(n - 2);
            // 调用fork方法，分解出子任务
            f2.fork();

            // 汇总
            return f2.join() + f1.join();
        }
    }

    public static void main(String[] args) {
        ForkJoinPool forkJoinPool = new ForkJoinPool();

        // 第8项为21，故最终打印都是21
        // 任务1
        FibonacciTask task = new FibonacciTask(8);
        // 任务2
        FibonacciTask2 task2 = new FibonacciTask2(8);
        int result1 = forkJoinPool.invoke(task);
        int result2 = forkJoinPool.invoke(task2);

        System.out.println("task1: " + result1);
        System.out.println("task2: " + result2);

        // 停止任务的继续提交
        forkJoinPool.shutdown();
    }
}
