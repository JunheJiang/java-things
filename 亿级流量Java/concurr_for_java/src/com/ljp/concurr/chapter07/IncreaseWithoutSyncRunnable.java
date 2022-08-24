package com.ljp.concurr.chapter07;

//用于值累加递增的线程
public class IncreaseWithoutSyncRunnable implements Runnable {

    @Override
    public void run() {
        for(int i=0; i<10000; i++){
            RunWithoutSync.count++;
        }
    }
}
