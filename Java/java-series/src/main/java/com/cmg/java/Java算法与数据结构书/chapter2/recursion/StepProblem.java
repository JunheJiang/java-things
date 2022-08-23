package com.cmg.java.Java算法与数据结构书.chapter2.recursion;

/**
 * how many methods to climb a 10 layers step by the way of 1 or 2 steps per step
 * <p>
 * equals fibonacci sequence
 */
public class StepProblem {

    public static void main(String[] args) {
        System.out.println(getWays(10));
    }

    static int getWays(int layers) {
        //dynamic plan
        if (layers == 0 || layers == 1 || layers == 2) {
            return layers;
        } else {
            int a = 1;
            int b = 1;
            int temp = 0;
            for (int x = 3; x < layers; x++) {
                temp = a + b;
                a = b;
                b = temp;
            }
            return temp;
        }
    }

}
