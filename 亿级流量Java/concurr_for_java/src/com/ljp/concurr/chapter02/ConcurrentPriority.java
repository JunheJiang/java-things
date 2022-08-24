package com.ljp.concurr.chapter02;

public class ConcurrentPriority {
    public static void main(String[] args){
        Thread thread01 = new Thread(new PriorityGoRun());
        Thread thread02 = new Thread(new PriorityGoRun());
        Thread thread03 = new Thread(new PriorityGoRun());
        Thread threadMax = new Thread(new PriorityGoRun());

        thread01.setPriority(8);
        thread01.setName("thead01");
        thread01.start();

        thread02.setPriority(3);
        thread02.setName("thead02");
        thread02.start();

        thread03.setPriority(1);
        thread03.setName("thead03");
        thread03.start();

        threadMax.setPriority(10);
        threadMax.setName("theadMax");
        threadMax.start();
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
