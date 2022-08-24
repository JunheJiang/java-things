package com.ljp.concurr.chapter07;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.Semaphore;

public class CanteenSemaphoreDemo {
    //这里通过信号量，模拟今日的饭堂只开了3个打饭菜的窗口
    public static Semaphore auntWindowsCount = new Semaphore(3);

    public static void main(String[] args){
        //某个时刻来了9位同学到饭堂打饭菜
        Thread student001 = new Thread(new StudentRunnable());
        Thread student002 = new Thread(new StudentRunnable());
        Thread student003 = new Thread(new StudentRunnable());
        Thread student004 = new Thread(new StudentRunnable());
        Thread student005 = new Thread(new StudentRunnable());
        Thread student006 = new Thread(new StudentRunnable());
        Thread student007 = new Thread(new StudentRunnable());
        Thread student008 = new Thread(new StudentRunnable());
        Thread student009 = new Thread(new StudentRunnable());

        student001.start();
        student002.start();
        student003.start();
        student004.start();
        student005.start();
        student006.start();
        student007.start();
        student008.start();
        student009.start();

    }


}


//学生到窗口，饭堂阿姨帮忙打饭菜的这一过程的线程
class StudentRunnable implements Runnable{
    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @Override
    public void run() {
        try {
            CanteenSemaphoreDemo.auntWindowsCount.acquire();
            System.out.println(dateFormat.format(new Date())
                    + ": 饭堂的窗口的阿姨正在为同学"
                    + Thread.currentThread().getName() + "打饭菜...");
            Thread.sleep(5000);
            CanteenSemaphoreDemo.auntWindowsCount.release();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}