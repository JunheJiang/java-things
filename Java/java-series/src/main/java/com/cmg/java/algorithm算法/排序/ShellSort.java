package com.cmg.java.algorithm算法.排序;

/**
 * 希尔排序
 * 插入类型排序
 */
public class ShellSort {
    public static void main(String[] args) {
        int[] list = {27, 76, 47, 23, 7, 32, 19, 86};
        display(list);
        shellSort(list);
        display(list);
    }

    public static void shellSort(int[] list) {
        int len = list.length;
        int gap = len / 2;
        while (gap >= 1) {
            for (int i = gap; i < len; i++) {
                int temp = list[i];
                int j;
                for (j = i - gap; j >= 0 && list[j] > temp; j = j - gap) {
                    list[j + gap] = list[j];
                }
                list[j + gap] = temp;
            }
            gap = gap / 2;
        }
    }

    public static void display(int[] list) {
        if (list != null && list.length > 0) {
            for (int num : list) {
                System.out.print(num + " ");
            }
            System.out.println("");
        }
    }
}
