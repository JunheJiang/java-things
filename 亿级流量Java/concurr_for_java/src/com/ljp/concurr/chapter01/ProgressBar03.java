package com.ljp.concurr.chapter01;

import java.util.concurrent.Callable;
import java.util.concurrent.FutureTask;

import java.util.concurrent.Callable;
import java.util.concurrent.FutureTask;

public class ProgressBar03 implements Callable {
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
        ProgressBar03 countProgressBar = new ProgressBar03();
        FutureTask<String> futureTaskResult = new FutureTask<String>(countProgressBar);
        Thread countProgressBarThread = new Thread(futureTaskResult);
        countProgressBarThread.start();
        try {
            System.out.println(futureTaskResult.get());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
