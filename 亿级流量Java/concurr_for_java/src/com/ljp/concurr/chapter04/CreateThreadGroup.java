package com.ljp.concurr.chapter04;

public class CreateThreadGroup {
    public static void main(String[] args){
        ThreadGroup threadGroup01 = new ThreadGroup("ThreadGroup-01");

        Thread thread01 = new Thread(new SayHiThread());
        thread01.setName("Thread-01");
        Thread thread02 = new Thread(new SayHiThread());
        thread02.setName("Thread-02");

        Thread thread03 = new Thread(threadGroup01, new SayHiThread(),
                "Thread-03");
        Thread thread04 = new Thread(threadGroup01, new SayHiThread(),
                "Thread-04");

        System.out.println(Thread.currentThread().getName() + ": Hi~~!，" +
                "我在[" + Thread.currentThread().getThreadGroup().getName() + "]" +
                "线程组哟，我的父级线程组是："+ Thread.currentThread().getThreadGroup()
                .getParent().getName());

        thread01.start();
        thread02.start();
        thread03.start();
        thread04.start();

        try {
            Thread.sleep(100L);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println(threadGroup01.getName() + ": Hi~~!，" +
                "我是一个线程组，我属于线程组["+ threadGroup01
                .getParent().getName() + "]");

    }




}

class SayHiThread implements Runnable{
    @Override
    public void run() {
        System.out.println(Thread.currentThread().getName() + ": Hi~~!，" +
                "我在[" + Thread.currentThread().getThreadGroup().getName() + "]" +
                "线程组哟，我的父级线程组是："+ Thread.currentThread().getThreadGroup()
                .getParent().getName());
    }
}