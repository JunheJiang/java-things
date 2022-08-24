package com.ljp.concurr.chapter02;

public class DaemonRunnableThread implements Runnable {
    @Override
    public void run() {
        while(true){
            Thread.currentThread().setName("B002");
            System.out.println(Thread.currentThread().getName() + ": Hi~~，我是" +
                    "守护线程" + Thread.currentThread().getName() + "哟，作为守护线程，" +
                    "我不参与业务处理，但我每隔一秒就和大家打一声招呼。");
            try {
                Thread.sleep(1000L);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
