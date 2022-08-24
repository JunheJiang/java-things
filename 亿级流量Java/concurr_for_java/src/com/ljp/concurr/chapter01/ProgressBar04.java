package com.ljp.concurr.chapter01;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;

public class ProgressBar04 implements Callable {
    // 进度条的目前进度值
    private int progressValue = 0;
    // 累加辅助器，初始值为0
    private int accValue = 0;

    @Override
    public String call() throws Exception {
        for (int i = 0; i <= 300; i++){
            System.out.println("我已经数到了第" + i + "个数字了哟， 目前进度："
                    + progressValue + "%");
            accValue++;
            if (accValue ==3){
                progressValue++;
                accValue = 0;
            }
        }
        return "complete";
    }

    public static void main(String[] args){
        ProgressBar04 countProgressBar = new ProgressBar04();
        FutureTask<String> futureTaskResult = new FutureTask<String>(countProgressBar);
        ExecutorService threadPool =  Executors.newFixedThreadPool(10);
        threadPool.submit(futureTaskResult);
        try {
            System.out.println(futureTaskResult.get());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

