package com.ljp.concurr.chapter03;

public class ThreadWaitNotifyDemo {
    public static void main(String[] args) throws Exception {
        Object lock = new Object();
        Thread waitThread = new Thread(() -> {
            ThreadWait threadWait = new ThreadWait(lock);
            threadWait.testWait();
        });
        Thread notifyThread = new Thread(() -> {
            ThreadNotify threadNotify = new ThreadNotify(lock);
            threadNotify.testNotify();
        });
        waitThread.start();
        /**
         * 保证waitThread一定会先开始启动
         */
        Thread.sleep(1000);
        notifyThread.start();
    }
}
