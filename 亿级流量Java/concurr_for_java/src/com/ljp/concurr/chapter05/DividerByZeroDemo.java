package com.ljp.concurr.chapter05;

public class DividerByZeroDemo {

    public static void main(String[] args){
        //被除数初定为30
        int dividend = 30;
        //除数初定为3
        int divisor = 3;

        int step = 1;

        while(divisor >= 0){
            try{
                System.out.println(dividend + "与" + divisor + "相除结果为:"
                        + dividend/divisor);
            }catch(ArithmeticException exception){
                System.out.println("除数为0了，不符合逻辑。");
            }finally{
                System.out.println("第" + step + "次除法计算结束");
                step++;
            }

            divisor--;
        }
        System.out.println("结束运算");

    }
}
