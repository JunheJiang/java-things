package com.ljp.concurr.chapter02;

public class WithDaemonFriendThread extends Thread {
    @Override
    public void run(){
        Thread.currentThread().setName("A002");
        System.out.println(Thread.currentThread().getName() + ": Hi,我是做完一件事" +
                "就走人的线程喔，后面的事情就交给我的朋友线程B002了");
        Thread myFriendThread = new Thread(new DaemonRunnableThread());
        myFriendThread.setDaemon(true);//将它的朋友，即线程B002设置为守护线程。
        myFriendThread.start();
        try {
            Thread.sleep(6000L);//睡眠6秒后再终止
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
