package com.yzxie.java.demo.charter3.part_3_3;

/**
 * @author xieyizun
 * @date 17/6/2019 23:06
 * @description:
 */
public class VolatileDemo {

    // 控制开关默认为关闭的
    private static boolean onCtrl; // 不使用volatile修饰
    //private static volatile boolean onCtrl; // 使用volatile修饰

    public void turnOn() {
        onCtrl = true;
    }

    public void doWork() {
        System.out.println("doing work now.");
    }

    public static void main(String[] args) {
        VolatileDemo demo = new VolatileDemo();
        // 工作线程
        Thread workThread = new Thread(new Runnable() {
            @Override
            public void run() {
                // 无限等待直到开关打开
                while (!onCtrl) {}

                // 开关打开后开始工作
                demo.doWork();
            }
        });
        workThread.start();

        // 在主线程休眠2秒后，打开开关
        try {
            Thread.sleep(2000);
            demo.turnOn();
            System.out.println("onCtrl is on.");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
