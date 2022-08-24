package com.ljp.concurr.chapter07;

public class ReorderTestDemo {

    //创建3个共享变量
    public static int a = 0;
    public static int b = 0;
    public static int c = 0;

    //循环100000次，看看能否出现重排序现象
    public static void main(String[] args){
        for (int i=0; i<1000000; i++){
            Thread setValueThread = new Thread(new SetValueRunnable());
            Thread reorderOpThread = new Thread(new ReorderOutputRunnable());
            setValueThread.start();
            reorderOpThread.start();
            try {
                setValueThread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            try {
                reorderOpThread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            a = 0;
            b = 0;
            c = 0;
        }
    }

}

//按a b c的顺序来设置共享变量的线程。
class SetValueRunnable implements Runnable {

    @Override
    public void run() {
        ReorderTestDemo.a = 1 - ReorderTestDemo.b - ReorderTestDemo.c;
        ReorderTestDemo.b = 1;
        ReorderTestDemo.c = 1;
    }
}

//当发现重排序现象出现的时候，会打印出信息的线程。
class ReorderOutputRunnable implements  Runnable {

    @Override
    public void run() {
        if (ReorderTestDemo.b > ReorderTestDemo.a){
            System.out.println("指令重排序出现了，b > a了");
        }

        if (ReorderTestDemo.c > ReorderTestDemo.b){
            System.out.println("指令重排序出现了，c > b了");
        }

        if (ReorderTestDemo.c > ReorderTestDemo.a){
            System.out.println("指令重排序出现了，c > a了");
        }
    }
}
