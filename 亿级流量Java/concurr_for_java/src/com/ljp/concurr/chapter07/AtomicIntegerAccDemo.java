package com.ljp.concurr.chapter07;

import java.util.concurrent.atomic.AtomicInteger;

//原子整型多线程累加例子
public class AtomicIntegerAccDemo {
    public static AtomicInteger atomicInt = new AtomicInteger(0);

    public static void main(String[] args){
        Thread thread01 = new Thread(new AccAtomicInt());
        Thread thread02 = new Thread(new AccAtomicInt());
        Thread thread03 = new Thread(new AccAtomicInt());

        thread01.start();
        thread02.start();
        thread03.start();

        try {
            Thread.sleep(1000L);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println(AtomicIntegerAccDemo.atomicInt);
    }

}

//使用了原子整型的线程累加器
class AccAtomicInt implements Runnable {
    @Override
    public void run() {
        for (int i=0; i<1000000; i++){
            AtomicIntegerAccDemo.atomicInt.getAndIncrement();
        }

    }
}
