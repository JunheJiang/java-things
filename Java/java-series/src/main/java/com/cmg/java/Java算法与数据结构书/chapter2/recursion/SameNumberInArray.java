package com.cmg.java.Java算法与数据结构书.chapter2.recursion;

import java.util.Random;

/**
 * find the repeated num in array
 */
public class SameNumberInArray {
    public static void main(String[] args) {
        Random random = new Random();
        int[] arr = new int[101];
        for (int i = 0; i < arr.length; i++) {
            arr[i] = i;
        }

        arr[100] = 88;
        for (int n = 0; n < 1000; n++) {
            int i = random.nextInt(101);
            int j = random.nextInt(101);
            int temp = arr[i];
            arr[i] = arr[j];
            arr[j] = temp;
        }
        printArray(arr);
        System.out.println(" ");
        System.out.println("------------ --  -- - ---- -- -");

        long startTime = System.nanoTime();

//        lable:
//        for (int x = 0; x < arr.length; x++) {
//            for (int y = x + 1; y < arr.length; y++) {
//                if (arr[x] == arr[y]) {
//                    System.out.println("重复数字是：" + arr[x]);
//                    break lable;
//                }
//            }
//        }

//
//        int sum = 0;
//        for (int x = 0; x < arr.length; x++) {
//            sum += arr[x];
//        }
//        for (int x = 0; x < 100; x++) {
//            sum -= x;
//        }
//        System.out.println("重复数字是：" + sum);

        //main method to optimize algorithm
        int[] newArr = new int[100];
        for (int x = 0; x < arr.length; x++) {
            newArr[arr[x]]++;
            if (newArr[arr[x]] == 2) {
                System.out.println("重复数字是：" + arr[x]);
            }
        }
        System.out.println(System.nanoTime() - startTime);
    }

    private static void printArray(int[] arr) {
        for (int i = 0; i < arr.length; ) {
            System.out.print(arr[i] + "  ");
            if (++i % 10 == 0)
                System.out.println();
        }
    }


    //策略：时间换空间 空间换时间
    //异或运算 异或抵消


}
