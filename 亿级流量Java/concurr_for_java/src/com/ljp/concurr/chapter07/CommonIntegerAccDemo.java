package com.ljp.concurr.chapter07;

//普通整型的多线程累加例子
public class CommonIntegerAccDemo {
    public static int commonInt = 0;

    public static void main(String[] args){
        Thread thread01 = new Thread(new AccCommonInt());
        Thread thread02 = new Thread(new AccCommonInt());
        Thread thread03 = new Thread(new AccCommonInt());

        thread01.start();
        thread02.start();
        thread03.start();

        try {
            Thread.sleep(1000L);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println(CommonIntegerAccDemo.commonInt);

    }
}

//普通线程累加器
class AccCommonInt implements Runnable {
    @Override
    public void run() {
        for (int i=0; i<1000000; i++){
            CommonIntegerAccDemo.commonInt++;
        }

    }
}
