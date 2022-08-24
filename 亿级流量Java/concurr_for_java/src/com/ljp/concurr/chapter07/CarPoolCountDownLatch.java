package com.ljp.concurr.chapter07;

import java.util.concurrent.CountDownLatch;

public class CarPoolCountDownLatch {

    // 拼单乘客最大数值设置为3
    public static CountDownLatch seatCountDownLatch = new CountDownLatch(3);

    public static void main(String[] args){
        // 创建司机，并开始发顺风单，拼单满3人即走
        Thread driverThread = new Thread(new DriverMan());
        driverThread.start();

        Thread passengerThread01 = new Thread(new Passenger());
        Thread passengerThread02 = new Thread(new Passenger());
        Thread passengerThread03 = new Thread(new Passenger());

        passengerThread01.start();
        passengerThread02.start();
        passengerThread03.start();
    }

}

// 顺风车司机线程
class DriverMan implements Runnable{

    @Override
    public void run() {
        System.out.println("我是顺风车司机，满3个乘车拼车的话，我们就出发！");
        try {
            // 使用CountDownLatch工具，等待顺风车拼单的乘客。
            CarPoolCountDownLatch.seatCountDownLatch.await();
            System.out.println("嗯，乘客都齐了，我们出发！");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }
}

// 乘客线程
class Passenger implements Runnable {

    @Override
    public void run() {
        System.out.println("Hi~~! 我是乘客" + Thread.currentThread().getName()
                + "，我确定搭乘你的顺风车");
        CarPoolCountDownLatch.seatCountDownLatch.countDown();
    }
}
