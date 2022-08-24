package com.ljp.concurr.chapter07;

public class SynchronizedStaticMethod implements Runnable  {

    @Override
    public void run() {
        numberOff();
    }

    public synchronized static void numberOff(){
        for(int i=0; i<100; i++){
            System.out.println(Thread.currentThread().getName()
                    + ": 我来数数了，目前数到" + i);
        }
    }
}
