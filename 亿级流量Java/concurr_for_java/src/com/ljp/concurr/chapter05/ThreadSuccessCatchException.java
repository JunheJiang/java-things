package com.ljp.concurr.chapter05;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ThreadSuccessCatchException {
    public static void main(String[] args){
        ExecutorService execService = Executors.newCachedThreadPool(new SuccessCatchThreadExceptionFactory());
        execService.execute(new DividerByZeroRunnable());

    }
}

class DividerByZeroRunnable implements Runnable {

    @Override
    public void run() {
        //被除数初定为30
        int dividend = 30;
        //除数初定为3
        int divisor = 3;
        int step = 1;

        while(divisor >= 0){
            System.out.print(dividend + "与" + divisor + "相除结果为:");
            System.out.println( dividend/divisor);
            divisor--;
        }
        System.out.println("结束运算");
    }
}
