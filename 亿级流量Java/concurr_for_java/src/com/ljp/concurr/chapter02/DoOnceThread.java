package com.ljp.concurr.chapter02;

public class DoOnceThread extends Thread {
    @Override
    public void run(){
        Thread.currentThread().setName("A001");
        System.out.println(Thread.currentThread().getName() + ": Hi,我是做完一件事" +
                "就走人的线程喔，后面的事情就交给我的朋友线程B001了");
        Thread myFriendThread = new Thread(new CommonRunnableThread());
        myFriendThread.start();
    }
}