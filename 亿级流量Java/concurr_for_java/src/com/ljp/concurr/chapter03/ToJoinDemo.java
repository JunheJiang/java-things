package com.ljp.concurr.chapter03;

public class ToJoinDemo {

    public static void main(String[] args){

        Thread threadA001 = new Thread(new IwillJoinOtherRunnable());

        //main线程开始数数，1~19
        for (int i=0; i< 20; i++){
            System.out.println("main:" + i);
            if (i == 8){
                threadA001.start();
                try {
                    threadA001.join();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

        }
    }

}

class IwillJoinOtherRunnable implements Runnable {

    @Override
    public void run() {
        for (int i=1; i<=10; i++){
            System.out.println("我要插队数数了，目前数到：" + i);
        }
    }
}
