package com.ljp.concurr.chapter02;

public class ThreadPriority {
    public static void main(String[] args){
        Thread thread01 = new Thread(new PriorityTestRun());
        thread01.setPriority(8);
        thread01.start();
    }


}

class PriorityTestRun implements Runnable {
    @Override
    public void run() {
        System.out.println("I am running");
    }
}
