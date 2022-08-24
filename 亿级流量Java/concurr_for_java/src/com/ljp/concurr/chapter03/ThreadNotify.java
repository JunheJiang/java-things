package com.ljp.concurr.chapter03;

public class ThreadNotify {

    private Object lock;

    public ThreadNotify(Object lock) {
        this.lock = lock;
    }

    public void testNotify() {
        try {
            synchronized (lock) {
                System.out.println("start notify........");
                lock.notify();
                System.out.println("end notify........");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
