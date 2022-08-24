package com.ljp.concurr.chapter06;

import org.apache.commons.lang3.concurrent.BasicThreadFactory;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class SecondTimerTest {
    static int decreaseInteger = 8;
    public static void main(String[] args) {
        Timer timer002 = new Timer();
        SimpleDateFormat dFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

//        //延迟2000ms执行程序
//        timer002.schedule(new TimerTask() {
//            @Override
//            public void run() {
//                System.out.println("Hi，我是第一个TimerTask线程，当前时间为："
//                        + dFormat.format(new Date()));
//            }
//          }, 2000, 5000
//        );
//
//        //延迟3001ms执行程序
//        timer002.schedule(new TimerTask() {
//            @Override
//            public void run() {
//                System.out.println("Hi，我是第二个TimerTask线程，当前时间为："
//                          + dFormat.format(new Date()));
//                //随着decreaseInteger的值不断递减，当到达0的时候，
//                //就会出现成除以0的逻辑错误，
//                //并且程序会抛出java.lang.ArithmeticException: / by zero异常。
//                SecondTimerTest.decreaseInteger--;
//                int i = (100/SecondTimerTest.decreaseInteger);
//
//            }
//          }, 3001, 1000
//        );

        ScheduledExecutorService executorService =
           new ScheduledThreadPoolExecutor(10,
               new BasicThreadFactory.Builder()
                   .namingPattern("example-schedule-pool-%d")
                       .daemon(true).build());

        executorService.scheduleAtFixedRate(new Runnable(){
            @Override
            public void run() {
                System.out.println("Hi，我是第一个TimerTask线程，当前时间为："
                        + dFormat.format(new Date()));
            }
        }, 2000, 5000, TimeUnit.MILLISECONDS);


        executorService.scheduleAtFixedRate(new Runnable(){
            @Override
            public void run() {
                System.out.println("Hi，我是第二个TimerTask线程，当前时间为："
                        + dFormat.format(new Date()));
                //随着decreaseInteger的值不断递减，当到达0的时候，
                //就会出现成除以0的逻辑错误，
                //并且程序会抛出java.lang.ArithmeticException: / by zero异常。
                SecondTimerTest.decreaseInteger--;
                int i = (100/SecondTimerTest.decreaseInteger);
            }
        }, 3001, 1000, TimeUnit.MILLISECONDS);


    }
}
