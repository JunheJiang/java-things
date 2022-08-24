package com.ljp.concurr.chapter05;

public class CommonThreadException {

    public static void main(String[] args){
        Thread thread01 = new Thread(new ThrowsExceptionRunnable());

        try{
            thread01.start();
        } catch(Exception e){
            System.out.println("成功捕获到线程的异常！");
        }

    }
}

class ThrowsExceptionRunnable implements  Runnable {

    @Override
    public void run() {
        //被除数初定为30
        int dividend = 30;
        //除数初定为3
        int divisor = 3;

        int step = 1;

        while(divisor >= 0){
            try {
                System.out.println(dividend + "与" + divisor + "相除结果为:"
                        + dividend/divisor);
            } catch (Exception e){
                System.out.println("成功在run( )方法中捕获到线程的异常！");
            }

            divisor--;
        }
        System.out.println("结束运算");
    }
}


