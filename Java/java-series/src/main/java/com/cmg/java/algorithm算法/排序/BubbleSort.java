package com.cmg.java.algorithm算法.排序;

import lombok.extern.slf4j.Slf4j;

/**
 * 冒泡排序
 * <p>
 * 交换类型排序
 */
@Slf4j
public class BubbleSort {

    public static void main(String[] args) {
        int[] list = {27, 76, 47, 23, 7, 32, 19, 86};
        display(list);
        long time = System.currentTimeMillis();
        bubbleSort(list);
        log.info((System.currentTimeMillis() - time) + "");
        display(list);
    }


    public static void display(int[] list) {
        if (list != null && list.length > 0) {
            for (int num : list) {
                System.out.print(num + " ");
            }
            System.out.println("");
        }
    }

    /**
     * 逐个比较 大数下沉
     * @param list
     */
    public static void bubbleSort(int[] list) {
        int len = list.length;
        for (int i = 0; i < len - 1; i++) {
            for (int j = 0; j < len - 1 - i; j++) {
                if (list[j] > list[j + 1]) {
                    int temp = list[j];
                    list[j] = list[j + 1];
                    list[j + 1] = temp;
                }
            }
        }
    }

}
