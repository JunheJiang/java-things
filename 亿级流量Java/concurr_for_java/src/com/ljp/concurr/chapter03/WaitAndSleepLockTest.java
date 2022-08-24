package com.ljp.concurr.chapter03;

public class WaitAndSleepLockTest {
    public static void main(String[] args){
        // 定义一个监控锁
        Object monitorLock = new Object();

        Thread waitThread = new Thread(new WaitUseLockRunnable(monitorLock));
        Thread sleepThread = new Thread(new SleepUseLockRunnable(monitorLock));

        waitThread.start();
        sleepThread.start();

    }
}


class WaitUseLockRunnable implements Runnable {

    private Object monitorLock;

    WaitUseLockRunnable(Object lock){
        this.monitorLock = lock;
    }

    @Override
    public void run() {
        synchronized (monitorLock){
            System.out.println("我是等待线程，我要等待一下...");
            try {
                monitorLock.wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("我等待完毕了。");
        }
    }
}

class SleepUseLockRunnable implements Runnable {
    private Object monitorLock;

    SleepUseLockRunnable(Object lock){
        this.monitorLock = lock;
    }

    @Override
    public void run() {
        synchronized (monitorLock){
            System.out.println("我是睡觉线程，我要睡觉了！！ ZZzz...ZZzz...");
            // 随便通知一下等待线程
            monitorLock.notify();
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("我睡醒了，真香。");
        }
    }
}