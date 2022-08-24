package com.ljp.concurr.chapter07;

public class IncreaseWithSyncRunnable implements Runnable {

    private Object monitorLock;

    public IncreaseWithSyncRunnable(Object monitorLock) {
        this.monitorLock = monitorLock;
    }


    @Override
    public void run() {
        for(int i=0; i<10000; i++){
            //同步关键字synchronized进行累加的同步控制
            synchronized (monitorLock){
                RunWithSync.count++;
            }

        }
    }
}
