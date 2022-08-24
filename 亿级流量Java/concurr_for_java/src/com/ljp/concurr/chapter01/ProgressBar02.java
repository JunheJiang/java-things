package com.ljp.concurr.chapter01;

public class ProgressBar02 implements Runnable {
    // 进度条的目前进度值
    private int progressValue = 0;
    // 累加辅助器，初始值为0；
    private int accValue = 0;
    @Override
    public void run() {
        for (int i = 0; i <= 300; i++){
            System.out.println("我已经数到了第" + i + "个数字了哟， 目前进度："
                    + progressValue + "%");
            accValue++;
            if (accValue ==3){
                progressValue++;
                accValue = 0;
            }
        }
    }

    public static void main(String[] args){
        ProgressBar02 countProgressBar = new ProgressBar02();
        Thread countProgressBarThread = new Thread(countProgressBar);
        countProgressBarThread.start();
    }
}

