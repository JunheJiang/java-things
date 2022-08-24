package com.ljp.concurr.chapter07;

public class MemValCannotSawDemo {

    //共享变量
    public static String trafficLight = "green";

    public static void main(String[] args){
        int i = 0;

        //创建和启动3个信号输出对象，替代现实中的交通信号灯
        Thread outputThread01 = new Thread(new OutputSignalRunnable());
        Thread outputThread02 = new Thread(new OutputSignalRunnable());
        Thread outputThread03 = new Thread(new OutputSignalRunnable());

        outputThread01.start();
        outputThread02.start();
        outputThread03.start();


        //以下的循环实际上是模拟一定时间内，交通信号灯信号的转变。
        while (i<80000){
            System.out.println("交通信号灯的信号是：" + trafficLight);
            i++;
            if (i == 20000){
                Thread changeSignalThread = new Thread(new ChangeSignalRunnable());
                changeSignalThread.start();
            }

            if (i == 30000){
                Thread changeSignalThread = new Thread(new ChangeSignalRunnable());
                changeSignalThread.start();
            }

            if (i == 60000){
                Thread changeSignalThread = new Thread(new ChangeSignalRunnable());
                changeSignalThread.start();
            }

        }
    }


}

//定义信号转换线程
class ChangeSignalRunnable implements Runnable{

    @Override
    public void run() {
        if (MemValCannotSawDemo.trafficLight.equalsIgnoreCase("green")){
            MemValCannotSawDemo.trafficLight = "yellow";
            System.out.println("交通信号灯的信号是：" + MemValCannotSawDemo.trafficLight);
        } else if (MemValCannotSawDemo.trafficLight.equalsIgnoreCase("yellow")){
            MemValCannotSawDemo.trafficLight = "red";
            System.out.println("交通信号灯的信号是：" + MemValCannotSawDemo.trafficLight);
        } else if (MemValCannotSawDemo.trafficLight.equalsIgnoreCase("red")){
            MemValCannotSawDemo.trafficLight = "green";
            System.out.println("交通信号灯的信号是：" + MemValCannotSawDemo.trafficLight);
        }
    }
}


//定义信号输出线程
class OutputSignalRunnable implements Runnable{

    @Override
    public void run() {
        while (true){
            System.out.println("交通信号灯的信号是：" + MemValCannotSawDemo.trafficLight);
        }
    }
}