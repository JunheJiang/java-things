package com.ljp.concurr.chapter03;

import java.text.SimpleDateFormat;
import java.util.Date;

public class InSleepDemo {
    public static void main(String[] args){
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        System.out.println(dateFormat.format(new Date()) +
                " -- 嗯，这个时刻，先输出第一行吧。");
        try {
            //睡眠休息3秒
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println(dateFormat.format(new Date()) +
                " -- OK，休息3秒了，这个时刻，再输出第二行吧。");

    }
}
