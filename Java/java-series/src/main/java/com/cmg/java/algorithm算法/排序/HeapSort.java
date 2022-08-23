package com.cmg.java.algorithm算法.排序;

/**
 * 堆排序
 * <p>
 * 选择类型排序
 */
public class HeapSort {

    public static void main(String[] args) {
        int[] list = {27, 76, 47, 23, 7, 32, 19, 86};
        display(list);
        heapSort(list);
        display(list);
    }

    /**
     * 大数往大堆放，小数往小堆放
     * 分割次数与移动次数
     *
     * @param list
     */
    public static void heapSort(int[] list) {
        for (int i = list.length / 2 - 1; i >= 0; i--) {
            headAdjust(list, i, list.length);
        }

        for (int i = list.length - 1; i > 0; i--) {
            swap(list, 0, i);
            headAdjust(list, 0, i);
        }
    }


    public static void headAdjust(int[] list, int parent, int length) {
        int temp = list[parent];
        int leftChild = 2 * parent + 1;

        while (leftChild < length) {
            if (leftChild + 1 < length && list[leftChild] < list[leftChild + 1]) {
                leftChild++;
            }
            if (temp >= list[leftChild]) {
                break;
            }
            list[parent] = list[leftChild];
            parent = leftChild;
            leftChild = 2 * parent + 1;
        }
        list[parent] = temp;
    }


    public static void swap(int[] list, int top, int last) {
        int temp = list[top];
        list[top] = list[last];
        list[last] = temp;
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
