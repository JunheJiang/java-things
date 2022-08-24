package com.yzxie.java.demo.charter3.part_3_1;

import java.util.Arrays;
import java.util.List;

/**
 * Author: xieyizun
 * Version: 1.0
 * Date: 2019-09-07
 * Description:
 **/
public class ThreadJoinDemo {
    static class SumThread extends Thread {
        private List<Integer> originData;
        private long sum;
        public SumThread(List<Integer> originData) {
            this.originData = originData;
        }
        public Long getSum() {
            return sum;
        }
        @Override
        public void run() {
            // 累加操作
            for (Integer item : originData) {
                sum += item;
            }
        }
    }

    public static void main(String[] args) {
        try {
            // 线程1
            List<Integer> data1 = Arrays.asList(1,2,3);
            SumThread thread1 = new SumThread(data1);
            // 线程2
            List<Integer> data2 = Arrays.asList(4,5,6);
            SumThread thread2 = new SumThread(data2);

            // 子线程并行计算
            thread1.start();
            thread2.start();

            // 主线程等待子线程执行完成
            thread1.join();
            thread2.join();

            // 主线程计算差异
            System.out.println(thread1.getSum() - thread2.getSum());
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
