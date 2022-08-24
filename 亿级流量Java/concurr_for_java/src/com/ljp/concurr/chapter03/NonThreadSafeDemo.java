package com.ljp.concurr.chapter03;

public class NonThreadSafeDemo {

    public static int count;

    public static void main(String[] args){
        Thread increaseCount01 = new Thread(new IncreaseValueRunnable());
        Thread increaseCount02 = new Thread(new IncreaseValueRunnable());
        Thread increaseCount03 = new Thread(new IncreaseValueRunnable());
        Thread increaseCount04 = new Thread(new IncreaseValueRunnable());

        increaseCount01.start();
        increaseCount02.start();
        increaseCount03.start();
        increaseCount04.start();

        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println(NonThreadSafeDemo.count);

    }
}

class IncreaseValueRunnable implements Runnable {

    @Override
    public void run() {
        for (int i=0; i< 10000; i++){
            NonThreadSafeDemo.count++;
        }
    }
}
