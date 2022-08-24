package com.ljp.concurr.chapter04;

public class FindAllThread {
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

        // 获得当前的main线程组，并且遍历出该线程组下所有的线程。
        ThreadGroup mainGroup = Thread.currentThread().getThreadGroup();

        Thread[] tmpTreadList = new Thread[10];
        mainGroup.enumerate(tmpTreadList);

        int i = 1;
        for (Thread tmpThread : tmpTreadList){
            if (tmpThread == null){
                continue;
            }
            System.out.println("main线程组中运行的线程有：(" + i + ")"
                    + tmpThread.getName());
            i++;
        }

        // 获得ThreadGroup-01线程组，并且遍历出该线程组下所有的线程。
        tmpTreadList = new Thread[10];
        threadGroup01.enumerate(tmpTreadList);

        i = 1;
        for (Thread tmpThread : tmpTreadList){
            if (tmpThread == null){
                continue;
            }
            System.out.println("ThreadGroup-01线程组中运行的线程有：(" + i + ")"
                    + tmpThread.getName());
            i++;
        }


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

