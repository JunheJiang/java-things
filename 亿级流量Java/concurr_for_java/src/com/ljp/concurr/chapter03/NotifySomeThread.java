package com.ljp.concurr.chapter03;

public class NotifySomeThread {
    public static void main(String[] args){
        //这是一个监控锁对象
        Object monitorLock01 = new Object();

        Thread waitingRunnableThread01 = new Thread(
                new NewWaitingRunnable(monitorLock01)
            );
        waitingRunnableThread01.setName("Thread-01");

        Thread waitingRunnableThread02 = new Thread(
                new NewWaitingRunnable(monitorLock01)
            );
        waitingRunnableThread02.setName("Thread-02");

        Thread waitingRunnableThread03 = new Thread(
                new NewWaitingRunnable(monitorLock01)
            );
        waitingRunnableThread03.setName("Thread-03");

        waitingRunnableThread01.start();
        waitingRunnableThread02.start();
        waitingRunnableThread03.start();

        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        //wait( )方法、notify( )方法、notifyAll( )方法等，都需要放入synchronized块中。
        //这里，我们只使用monitorLock01来试一试，看看能否唤醒其他锁的线程
        synchronized (monitorLock01){
//            monitorLock01.notify();
//            monitorLock01.notify();
//            monitorLock01.notify();
            monitorLock01.notifyAll();
        }
    }
}


//wait模拟线程
class NewWaitingRunnable implements Runnable {

    private Object monitorLock;

    public NewWaitingRunnable(Object monitorLock) {
        this.monitorLock = monitorLock;
    }

    @Override
    public void run() {
        //wait( )方法、notify( )方法、notifyAll( )方法等，都需要放入synchronized块中。
        synchronized (monitorLock){
            try {
                //Thread.sleep(1000);
                System.out.println("我们让" + Thread.currentThread().getName() + "休息等待一下。");
                monitorLock.wait();
                System.out.println(Thread.currentThread().getName() + "被唤醒了！");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

}