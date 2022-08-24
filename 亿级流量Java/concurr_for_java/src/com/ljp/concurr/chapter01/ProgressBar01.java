package com.ljp.concurr.chapter01;

public class ProgressBar01 extends Thread {
    // 进度条的目前进度值
    private int progressValue = 0;
    // 累加辅助器，初始值为0；
    private int accValue = 0;

    @Override
    public void run(){
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

    public static void main(String[] args) {
        ProgressBar01 countProgressBar = new ProgressBar01();
        System.out.println("数数线程刚创建，还没有调用start()方法，它这时的状态是："
                + countProgressBar.getState());
        countProgressBar.start();
        System.out.println("数数线程调用了start()方法，它这时的状态是："
                + countProgressBar.getState());
        try {
            Thread.sleep(2);
            System.out.println("主线程等待2毫秒，数数线程应在执行任务，它这时的状态是："
                    + countProgressBar.getState());
            Thread.sleep(2000);
            System.out.println("主线程等待2秒，数数线程应该已完成任务，它这时的状态是："
                    + countProgressBar.getState());
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

