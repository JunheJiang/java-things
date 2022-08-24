package com.ljp.concurr.chapter07;

public class RunWithoutSync {
    public static int count = 0;

    public static void main(String[] args){
        //创建四个线程，让它们一齐跑累计递增计算
        Thread increaseValueThread01 = new Thread(new IncreaseWithoutSyncRunnable());
        Thread increaseValueThread02 = new Thread(new IncreaseWithoutSyncRunnable());
        Thread increaseValueThread03 = new Thread(new IncreaseWithoutSyncRunnable());
        Thread increaseValueThread04 = new Thread(new IncreaseWithoutSyncRunnable());

        increaseValueThread01.start();
        increaseValueThread02.start();
        increaseValueThread03.start();
        increaseValueThread04.start();

        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println(RunWithoutSync.count);
    }
}
