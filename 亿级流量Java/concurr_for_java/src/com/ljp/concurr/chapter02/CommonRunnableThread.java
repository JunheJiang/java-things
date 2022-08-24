package com.ljp.concurr.chapter02;

public class CommonRunnableThread implements Runnable {
    @Override
    public void run() {
        while(true){
            Thread.currentThread().setName("B001");
            System.out.println(Thread.currentThread().getName() + ": Hi~~，我是" +
                    "普通的用户线程" + Thread.currentThread().getName() + "哟，" +
                    "我每隔一秒就和大家打一声招呼。");
            try {
                Thread.sleep(1000L);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
