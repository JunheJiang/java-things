package com.ljp.concurr.chapter04;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class CreateThreadPool01 {
    public static void main(String[] args) {
        ThreadPoolExecutor threadPool01 = new ThreadPoolExecutor(3, 5, 6000,
                TimeUnit.MILLISECONDS, new ArrayBlockingQueue<Runnable>(5));

        for (int i=0; i<10; i++) {
            DoWork doWork = new DoWork(i);
            threadPool01.execute(doWork);
            System.out.println("Hi，我是线程池，目前池内线程总数量为：" +
                    threadPool01.getPoolSize() + "，BlockingQueue中等待执行的任务为：" +
                    threadPool01.getQueue().size());
        }



        for (int i=0; i<8; i++){
            System.out.println("Hi，我是01号线程池，目前池内线程总数量为：" +
                    threadPool01.getPoolSize() + "，BlockingQueue中等待执行的任务为：" +
                    threadPool01.getQueue().size());
            try {
                Thread.currentThread().sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

//        DoWork doWork10000 = new DoWork(10000);
//        DoWork doWork9999 = new DoWork(9999);
//
//        threadPool01.execute(doWork10000);
//        threadPool01.execute(doWork10000);
//        threadPool01.execute(doWork10000);
//        threadPool01.execute(doWork10000);
//        threadPool01.execute(doWork10000);
//        threadPool01.execute(doWork10000);
//        threadPool01.execute(doWork10000);
//
//        threadPool01.execute(doWork9999);
//        threadPool01.execute(doWork9999);
//        threadPool01.execute(doWork9999);
//
//        threadPool01.execute(doWork10000);
//        threadPool01.execute(doWork10000);
//        threadPool01.execute(doWork10000);
//        threadPool01.execute(doWork10000);
//        threadPool01.execute(doWork10000);
//        threadPool01.execute(doWork10000);
//        threadPool01.execute(doWork10000);
//
//
//
//        threadPool01.shutdown();
    }
}

class DoWork implements Runnable {
    private int sequence;

    public DoWork(int num) {
        this.sequence = num;
    }

    @Override
    public void run() {
        System.out.println("Hi~~，我是" + sequence + "号任务[" + Thread.currentThread().getName() + "]");
        try {
            Thread.currentThread().sleep(600);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}


//class DoWork02 implements Runnable {
//    private int sequence;
//
//    public DoWork02(int num) {
//        this.sequence = num;
//    }
//
//    @Override
//    public void run() {
//        System.out.println("Hello~~，我是" + sequence + "号任务[" + Thread.currentThread().getName() + "]");
//        try {
//            Thread.currentThread().sleep(100);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//    }
//}
