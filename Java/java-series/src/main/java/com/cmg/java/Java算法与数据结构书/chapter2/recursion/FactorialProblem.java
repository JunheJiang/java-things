package com.cmg.java.Java算法与数据结构书.chapter2.recursion;

import java.math.BigInteger;

/**
 * how many 0 in result of 1000's factorial
 */
public class FactorialProblem {

    public static void main(String[] args) {

        BigInteger bigInteger = new BigInteger("1");
        for (int i = 1; i <= 1000; i++) {
            BigInteger bigInteger1 = new BigInteger(i + "");
            bigInteger = bigInteger1.multiply(bigInteger);
        }

        String str = bigInteger.toString();
        int count = 0;
        for (int i = 0; i < str.length(); i++) {
            if ('0' == str.charAt(i)) {
                count += 1;
            }
        }
        System.out.println(count);

        StringBuffer sb = new StringBuffer(bigInteger.toString());
        str = sb.reverse().toString();
        count = 0;
        for (int i = 0; i < str.length(); i++) {
            if ('0' == str.charAt(i)) {
                count += 1;
            } else {
                break;
            }
        }
        System.out.println(count);
    }

}
