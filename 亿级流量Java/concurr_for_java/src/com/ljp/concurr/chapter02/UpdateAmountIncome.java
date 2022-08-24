package com.ljp.concurr.chapter02;

public class UpdateAmountIncome {
    public static int amount = 0;
    public static void main(String[] args){
        ThreadPut1000Times canPut100RMB = new ThreadPut1000Times();

        Thread threadBoy01 = new Thread(canPut100RMB);
        Thread threadBoy02 = new Thread(canPut100RMB);
        Thread threadBoy03 = new Thread(canPut100RMB);
        Thread threadBoy04 = new Thread(canPut100RMB);
        Thread threadBoy05 = new Thread(canPut100RMB);

        threadBoy01.start();
        threadBoy02.start();
        threadBoy03.start();
        threadBoy04.start();
        threadBoy05.start();

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println(amount);
    }



}

class ThreadPut1000Times implements Runnable {

    @Override
    public void run() {
        for (int i=0; i<300; i++){
            synchronized (this){
                UpdateAmountIncome.amount = UpdateAmountIncome.amount + 1;
            }
        }
        System.out.println("我用完了300个硬币了。");
    }
}
