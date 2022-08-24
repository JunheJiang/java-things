package com.ljp.concurr.chapter04;

public class ThreadWatchMonitor {
    public static void main(String[] args) {
        Thread thread01 = new Thread(new LongTimeExistThread());
        Thread thread02 = new Thread(new LongTimeExistThread());

        thread01.setName("Thread-No10001");
        thread02.setName("Thread-No10002");

        thread01.start();
        thread02.start();
    }
}

class LongTimeExistThread implements Runnable {

    @Override
    public void run() {
        while (true){
            System.out.println(Thread.currentThread().getName() + ": Hi~~我还在哟。");
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
