package com.ljp.concurr.chapter07;

import java.util.concurrent.Exchanger;

public class RunExchangerThread {

    public static void main(String[] args){
        // 实例化一个线程变量值交换工具
        Exchanger<String> exchangerUtil = new Exchanger<String>();

        // 创建两个线程，代表两个工作人员。其中一个在工作，另外一个则休息
        Thread workMan01 = new Thread(new ShiftRunnable("warking", exchangerUtil));
        Thread workMan02 = new Thread(new ShiftRunnable("take a break", exchangerUtil));

        workMan01.setName("workMan01");
        workMan02.setName("workMan02");

        // 工作人员01先工作
        workMan01.start();
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        // 工作人员02也起来了
        workMan02.start();

    }
}

class ShiftRunnable implements Runnable{

    private String workState;

    private Exchanger<String> exchangerUtil;

    ShiftRunnable(String workState, Exchanger<String> exchangeUtil){
        this.workState = workState;
        this.exchangerUtil = exchangeUtil;
    }

    @Override
    public void run() {
        System.out.println(Thread.currentThread().getName()
                + ": 我初始的工作状态是：" + workState);
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        try {
            // 开始进行线程间的数据交换
            String newWorkState = exchangerUtil.exchange(workState);
            System.out.println(Thread.currentThread().getName()
                    + ": 我的新工作状态是：" + newWorkState);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }
}

