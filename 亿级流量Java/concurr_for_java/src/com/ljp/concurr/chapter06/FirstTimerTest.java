package com.ljp.concurr.chapter06;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

public class FirstTimerTest {
    public static void main(String[] args){
        Timer timer001 = new Timer();
        SimpleDateFormat dFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        //延迟2000ms执行程序
        timer001.schedule(new TimerTask() {
            @Override
            public void run() {
                System.out.println("当前时间为：" + dFormat.format(new Date()));
            }}, 2000, 5000
        );
    }
}
