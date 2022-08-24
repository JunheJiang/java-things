package com.ljp.concurr.chapter03;

public class ToYieldDemo {

    public static void main(String[] args){
        Thread yiledThread01 = new Thread(new YieldRunnable());
        Thread yiledThread02 = new Thread(new YieldRunnable());
        yiledThread01.start();
        yiledThread02.start();
    }

}

class YieldRunnable implements Runnable{

    @Override
    public void run() {
        for (int i = 1; i <= 20; i++) {
            System.out.println(Thread.currentThread().getName() + "-----" + i);
            //当运行到大概一半的时候，线程进行让步操作。
            if (i == 10) {
                Thread.yield();
            }
        }
    }
}