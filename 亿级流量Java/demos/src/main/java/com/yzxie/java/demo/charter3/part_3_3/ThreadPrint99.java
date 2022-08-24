package com.yzxie.java.demo.charter3.part_3_3;

/**
 * Author: xieyizun
 * Version: 1.0
 * Date: 2019-09-21
 * Description: 打印1到99
 **/
public class ThreadPrint99 {
    private static volatile int totalNum = 0;

    public static void main(String[] args) {
        Object lock = new Object();

        for (int i = 1; i <= 99; i++) {

            // 每个线程一个编号
            int num = i;
            Thread thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        synchronized (lock) {
                            // 等待轮到自己打印
                            while (totalNum != num) {
                                lock.wait();
                            }
                            System.out.println(Thread.currentThread().getName() + ": " + num);

                            totalNum = totalNum + 1;

                            // 通知所有等待线程，只有满足条件的可以从以上的while返回
                            lock.notifyAll();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }, "thread-"+num);
            thread.start();
        }

        // 主线程启动，其中totalNum是volatile修饰，线程可见性
        synchronized (lock) {
            totalNum = totalNum + 1;
            lock.notifyAll();
        }
    }
}
