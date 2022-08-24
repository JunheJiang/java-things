package com.ljp.concurr.chapter07;

import java.util.HashMap;
import java.util.Map;

public class DeadLockTest {

    public static Map<String, Boolean> resourceInUseMap =
            new HashMap<String, Boolean>();

    public static void main(String[] args){
        Object monitorLock01 = new Object();
        Object monitorLock02 = new Object();
        // 设置锁未被占用
        resourceInUseMap.put("monitorLock01", false);
        resourceInUseMap.put("monitorLock02", false);

        //两个线程获取锁的顺序刚好相反
        Thread needLockThread01 = new Thread(
                new NeedLockRunnable(monitorLock01, monitorLock02));
        Thread needLockThread02 = new Thread(
                new NeedLockRunnable(monitorLock02, monitorLock01));

        needLockThread01.setName("Thread-01");
        needLockThread02.setName("Thread-02");

        needLockThread01.start();
        needLockThread02.start();

    }
}

class NeedLockRunnable implements Runnable {

    Object monitorLock01;
    Object monitorLock02;

    NeedLockRunnable(Object monitorLock01,
                                   Object monitorLock02) {
        this.monitorLock01 = monitorLock01;
        this.monitorLock02 = monitorLock02;
    }

    @Override
    public void run() {
        boolean finishFlag = false;
        while (finishFlag == false){
            if (DeadLockTest.resourceInUseMap.get("monitorLock01") != true){
                // 马上使用未占用的锁，并且快速标记，防止其他线程争抢。
                DeadLockTest.resourceInUseMap.put("monitorLock01", true);
                synchronized (monitorLock01) {
                    System.out.println(Thread.currentThread().getName()
                            + "得到了监控锁：" + monitorLock01
                            + "，准备获取第二把监控锁：" + monitorLock02);
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    if (DeadLockTest.resourceInUseMap.get("monitorLock02")
                            != true){
                        // 马上使用未占用的锁，并且快速标记，防止其他线程争抢。
                        DeadLockTest.resourceInUseMap
                                .put("monitorLock02", true);
                        synchronized (monitorLock02) {
                            System.out.println(Thread.currentThread()
                                    .getName() + "得到了监控锁：" + monitorLock02);
                            try {
                                Thread.sleep(1000);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }

                        }
                        // 这把锁用完了，可以释放资源了,改回未占用状态。
                        DeadLockTest.resourceInUseMap.put("monitorLock02", false);
                    } else {
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }

                }
                // 这把锁用完了，可以释放资源了,改回未占用状态。
                DeadLockTest.resourceInUseMap.put("monitorLock01", false);
                finishFlag = true;
            } else {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

        }

    }
}
