package com.ljp.concurr.chapter03;

public class SetHighPriorityForThread {

    public static void main(String[] args){
        Thread thread01 = new Thread(new PriorityGoRun());
        Thread thread02 = new Thread(new PriorityGoRun());
        Thread thread03 = new Thread(new PriorityGoRun());
        Thread thread04 = new Thread(new PriorityGoRun());
        Thread thread05 = new Thread(new PriorityGoRun());

        thread01.setPriority(1);
        thread01.setName("thead01");
        thread01.start();

        thread02.setPriority(1);
        thread02.setName("thead02");
        thread02.start();

        thread03.setPriority(1);
        thread03.setName("thead03");
        thread03.start();

        thread04.setPriority(1);
        thread04.setName("thead04");
        thread04.start();

        thread05.setPriority(1);
        thread05.setName("thead05");
        thread05.start();

        // 全部线程运行后，立即设大线程05的优先级为满级，看看是否能发生插队现象。
        thread05.setPriority(10);


    }


}

class PriorityGoRun implements Runnable {
    @Override
    public void run() {
        int calcValue = 0;
        for(int i=0; i< 1000; i++){
            calcValue = calcValue + i;
        }
        System.out.println("[" + Thread.currentThread().getName() +"] I finish the job , the value is: " + calcValue);
    }
}
