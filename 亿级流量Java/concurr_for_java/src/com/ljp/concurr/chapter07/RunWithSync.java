package com.ljp.concurr.chapter07;

public class RunWithSync {
    public static int count = 0;

    public static void main(String[] args){
        //创建一个监控锁
        Object monitorLock = new Object();

        //创建四个线程，让它们一齐跑累计递增计算,并且加入相同的监控锁，作为同步锁
        Thread increaseValueThread01 = new Thread(
                new IncreaseWithSyncRunnable(monitorLock));
        Thread increaseValueThread02 = new Thread(
                new IncreaseWithSyncRunnable(monitorLock));
        Thread increaseValueThread03 = new Thread(
                new IncreaseWithSyncRunnable(monitorLock));
        Thread increaseValueThread04 = new Thread(
                new IncreaseWithSyncRunnable(monitorLock));

        increaseValueThread01.start();
        increaseValueThread02.start();
        increaseValueThread03.start();
        increaseValueThread04.start();

        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println(RunWithSync.count);
    }
}

