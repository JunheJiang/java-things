package com.ljp.concurr.chapter03;

public class ThreadWait {

    private Object lock;

    public ThreadWait(Object lock) {
        this.lock = lock;
    }

    public void testWait() {
        try {
            synchronized (lock) {
                System.out.println("start wait........");
                lock.wait();
                System.out.println("end wait........");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
