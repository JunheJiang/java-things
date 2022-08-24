package com.yzxie.java.demo.charter3.part_3_2;

/**
 * @author xieyizun
 * @date 16/6/2019 13:40
 * @description:
 */
public class ThreadStatusDemo {

    public static void main(String[] args) {
        // 死循环线程
        Thread deadLoopThread = new Thread(new Runnable() {
            @Override
            public void run() {
                // 死循环
                while (true) {

                }
            }
        }, "deadLoopThread");

        // 正常线程
        Thread normalThread = new Thread(new Runnable() {
            @Override
            public void run() {
                System.out.println("do something");
            }
        }, "normalThread");

        // 启动线程deadLoopThread
        deadLoopThread.start();

        try {
            // 等待线程执行完成
            deadLoopThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // 启动线程normalThread
        normalThread.start();
    }
}
