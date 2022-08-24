package com.ljp.concurr.chapter04;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class CreateThreadPool02 {
    public static void main(String[] args){
        ExecutorService fixedThreadPool = Executors.newFixedThreadPool(10);
        for (int i=0; i<10; i++) {
            DoWork doWork = new DoWork(i);
            fixedThreadPool.execute(doWork);
        }

    }
}
