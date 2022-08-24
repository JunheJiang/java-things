package com.ljp.concurr.chapter06;

import org.apache.commons.lang3.concurrent.BasicThreadFactory;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimerTask;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class SchedulerExecutorServiceTest {
    static int decreaseInteger = 8;
    public static void main(String[] args) {
        SimpleDateFormat dFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        ScheduledExecutorService executorService =
                new ScheduledThreadPoolExecutor(10,
                        new BasicThreadFactory.Builder()
                                .namingPattern("example-schedule-pool-%d")
                                .daemon(true).build());

        executorService.scheduleAtFixedRate(new TimerTask(){
            @Override
            public void run() {
                System.out.println("Hi，我是第一个TimerTask线程，当前时间为："
                        + dFormat.format(new Date()));
            }
        }, 2000, 5000, TimeUnit.MILLISECONDS);

        executorService.scheduleAtFixedRate(new TimerTask(){
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


        while (true){
            //让main( )线程保持活性。
        }

    }
}
