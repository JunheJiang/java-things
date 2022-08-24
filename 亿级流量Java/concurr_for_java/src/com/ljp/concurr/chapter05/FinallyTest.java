package com.ljp.concurr.chapter05;

public class FinallyTest {
    public static void main(String[] args) {

        int r = test();
        System.out.println(r);

    }
    public static int test()
    {
        try {
            System.out.println("try");
            //return 1/0;
            return 0;
        } catch (Exception e) {
            System.out.println("exception");
            return 100;
        }finally{
            System.out.println("finally");
            return 1000;

        }

    }
}
