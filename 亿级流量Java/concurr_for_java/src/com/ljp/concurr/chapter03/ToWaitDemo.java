package com.ljp.concurr.chapter03;


public class ToWaitDemo {
    public static void main(String[] args){
        //这是一个能让一个线程等待，并且具有自动唤醒功能的线程实例
        Thread letYouWaitingThread = new Thread(new WaitingRunnable());

        //wait( )方法、notify( )方法、notifyAll( )方法等，都需要放入synchronized块中。
        synchronized (letYouWaitingThread){
            letYouWaitingThread.start();
            try {
                System.out.println("我们让main线程休息等待一下。");
                letYouWaitingThread.wait(200);
                System.out.println("目测main休息完毕。");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }
}

//wait模拟线程，其实例能使用wait( )方法，且具备自动唤醒功能
class WaitingRunnable implements Runnable {

    @Override
    public void run() {
        //wait( )方法、notify( )方法、notifyAll( )方法等，都需要放入synchronized块中。
        synchronized (this){
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("唤醒等待中的一个实例");
            this.notify();
        }
    }
}
