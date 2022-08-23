package com.cmg.java.algorithm算法.排序;

/**
 * 插入排序
 * 少量数据的排序
 * 增量式做法
 * 扑克为例
 * 插入类型排序
 */
public class InsertSort {

    public static void main(String[] args) {
        int[] list = {27, 76, 47, 23, 7, 32, 19, 86};
        display(list);
        insertSort(list);
        display(list);
    }

    public static void insertSort(int[] list) {
        int len = list.length;
        for (int i = 1; i < len; i++) {
            int temp = list[i];
            int j;
            for (j = i - 1; j >= 0 && list[j] > temp; j--) {
                list[j + 1] = list[j];
            }
            list[j + 1] = temp;
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
