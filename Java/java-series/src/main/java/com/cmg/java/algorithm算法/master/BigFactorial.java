package com.cmg.java.algorithm算法.master;

/**
 * 大数阶乘
 * 模拟乘法列式计算
 */
public class BigFactorial {
    //分解成 两两数字相乘

    //列式乘法 一个数字字符串与字符相乘

    //加法计算 分解

    //列式加法

    /**
     * 两字符串相乘
     * 123*53
     * 123*5+0 123*3
     *
     * @param a
     * @param b
     * @return
     */
    private String multiply(String a, String b) {
        String result = "";
        int lenB = b.length();
        //从乘数低位开始相乘
        for (int i = lenB - 1; i >= 0; i--) {
            String temp = multiplyByCol(a, b.charAt(i));
            for (int j = 0; j < lenB - 1 - i; j++) {
                //加0
                temp = temp + "0";
            }
            //列式相加
            result = add(result, temp);
        }
        return result;
    }

    /**
     * 列式乘法：被乘数乘以乘数一位数字
     *
     * @param a
     * @param b
     * @return
     */
    private String multiplyByCol(String a, char b) {
        String result = "";
        int extra = 0;
        int i = a.length() - 1;
        while (i >= 0) {
            result = String.valueOf(((a.charAt(i) - '0') * (b - '0') + extra) % 10) + result;
            extra = ((a.charAt(i) - '0') * (b - '0') + extra) / 10;
            i--;
        }
        if (extra != 0) {
            result = String.valueOf(extra) + result;
        }
        return result;
    }

    /**
     * 列式相加
     *
     * @param a
     * @param b
     * @return
     */
    private String add(String a, String b) {
        String result = "";
        int i = a.length() - 1;
        int j = b.length() - 1;
        int extra = 0;
        while (i >= 0 && j >= 0) {
            result = String.valueOf((a.charAt(i) - '0' + b.charAt(i) - '0' + extra) % 10) + result;
            extra = (a.charAt(i) - '0' + b.charAt(i) - '0' + extra) / 10;
            i--;
            j--;
        }
        while (i >= 0) {
            result = String.valueOf((a.charAt(i) - '0' + extra) % 10) + result;
            extra = (a.charAt(i) - '0' + extra) / 10;
            i--;
        }
        while (j >= 0) {
            result = String.valueOf((b.charAt(j) - '0' + extra) % 10) + result;
            extra = (b.charAt(j) - '0' + extra) / 10;
            j--;
        }
        if (extra != 0) {
            result = String.valueOf(extra) + result;
        }
        return result;
    }

    /**
     * 大数阶乘
     *
     * @param n
     * @return
     */
    String factorial(int n) {
        String result = "1";
        for (int i = 2; i <= n; i++) {
            result = multiply(result, String.valueOf(i));
        }
        return result;
    }

    public static void main(String[] args) {
        BigFactorial bigFactorial = new BigFactorial();
        System.out.println(bigFactorial.factorial(4));
    }
}

