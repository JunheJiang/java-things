package com.ljp.concurr.chapter02;

public class ThreadInterrupt {
    public static void main(String[] args){
        Thread doInterruptThread = new Thread(new DoInterrupt());
        doInterruptThread.start();
        System.out.println("--------------state:" + doInterruptThread.getState());
        try {
            Thread.sleep(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        doInterruptThread.interrupt();
        System.out.println("--------------state:" + doInterruptThread.getState());

    }

}

class DoInterrupt implements Runnable{
    @Override
    public void run() {
        int acc = 0;
        for (int i=0; i< 10000; i++){
            System.out.println(i);
            acc = acc + i;
            if (Thread.currentThread().isInterrupted()){
                System.out.println("我是线程，但我被中断了啊");
                System.out.println("--------------state:" +Thread.currentThread().getState());
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    System.out.println("--------------state:" +Thread.currentThread().getState());
                    Thread.currentThread().interrupt();
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e1) {
                        e1.printStackTrace();
                    }

                }
            }
        }
        //休息一下，处理1001~2000的数据
//        try {
//            Thread.sleep(1000);
//        } catch (InterruptedException e) {
//            System.out.println("我是线程，但我被中断了啊");
//
//        }

        for (int i=1001; i< 2000; i++){
            System.out.println(i);
            acc = acc + i;
        }
    }
}


