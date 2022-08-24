package com.ljp.concurr.chapter05;

public class CloseThread {
    public static void main(String[] args){
        Thread waitToCloseThread = new Thread(new waitToCloseRunnable());
        waitToCloseThread.start();
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        // 终止线程，设置线程的中断状态为true
        waitToCloseThread.interrupt();

    }
}


class waitToCloseRunnable implements Runnable {

    @Override
    public void run() {
        // 判断线程的状态是否为中断
        while (Thread.currentThread().isInterrupted() == false){
            try {
                System.out.println(Thread.currentThread()
                        .getName() + "线程正在运行");
                // 每次循环睡眠200毫秒
                Thread.sleep(200);
            } catch (InterruptedException e) {
                // 这里可以做一些线程中断前的准备工作，譬如释放资源等等。
                System.out.println("正在释放资源......");
                // 释放完资源后，可以关闭线程了
                System.out.println("立即中断线程，线程关闭中...");
                // 再次运行interrupt( )方法，真正的关闭线程
                Thread.currentThread().interrupt();
            }
        }
    }
}