package com.yzxie.java.demo.charter4.part_4_7;

import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveTask;

/**
 * Author: xieyizun
 * Version: 1.0
 * Date: 2019-09-13
 * Description:
 **/
public class ForkJoinDemo2 {
    // 累加1到n的顺序数组：f(n) = 1+2+..+n
    private static class SumNums extends RecursiveTask<Integer> {
        private int n;
        private int result;
        public SumNums(int n) {
            this.n = n;
        }

        @Override
        protected Integer compute() {
            // 停止分解的条件
            if (n == 0) {
                return n;
            }
            SumNums s = new SumNums(n-1);

            // 递归分解为子任务
            s.fork();

            // 等待子任务执行完成
            return s.join() + n;
        }
    }

    public static void main(String[] args) {
        ForkJoinPool forkJoinPool = new ForkJoinPool();
        int n = 5;
        // 计算1到n的和
        SumNums task3 = new SumNums(n);

        // 启动任务执行
        int result3 = forkJoinPool.invoke(task3);
        System.out.println("f(n)=1+2+..+n：n = " + n + ", result = " +result3);

        // 关闭线程池，此处需要等待所有任务执行完成
        forkJoinPool.shutdown();
    }
}
