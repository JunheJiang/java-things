package com.cmg.java.Java算法与数据结构书.chapter2.recursion;

/**
 * classic problem
 */
public class FibonacciSequence {

    public static void main(String[] args) {
        System.out.println(getNumber(5));
    }

    static int count = 0;

    /**
     * recursion method
     *
     * @param n
     * @return
     */
//    public static int getNumber(int n) {
//        count++;
//        if (n == 1 || n == 2) {
//            return n;
//        } else {
//            return getNumber(n - 1) + getNumber(n - 2);
//        }
//    }

    //reduce repeated method times
    //memo algorithm
    //top to bottom
//    static HashMap<Integer, Integer> map = new HashMap<>();
//
//    public static int getNumber(int n) {
//        count++;
//        if (n == 1 || n == 2) {
//            return n;
//        }
//        if (map.containsKey(n)) {
//            return map.get(n);
//        } else {
//            int value = getNumber(n - 1) + getNumber(n - 2);
//            map.put(n, value);
//            return value;
//        }
//    }

    //dynamic programming：spilt up to similar small problem till solve the whole problem
    //bottom to top
    public static int getNumber(int n) {
        count++;
        if (n == 1 || n == 2) {
            return n;
        } else {
            int a = 1;
            int b = 1;
            int temp = 0;
            for (int x = 3; x < n; x++) {
                temp = a + b;
                a = b;
                b = temp;
            }
            return temp;
        }
    }
}
