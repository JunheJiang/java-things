package com.cmg.java.algorithm算法.排序;

/**
 * 选择排序
 *
 * 选择类型排序
 */
public class SelectionSort {

	public static void main(String[] args) {
		int[] list = { 27, 76, 47, 23, 7, 32, 19, 86 };
		display(list);
		selectionSort(list);
		display(list);
	}

	public static void selectionSort(int[] list) {
		int len = list.length;
		for (int i = 0; i < len - 1; i++) {
			int min = i;
			for (int j = i + 1; j <= len - 1; j++) {
				if (list[j] < list[min]) {
					min = j;
				}
			}
			if (min != i) {
				swap(list, min, i);
			}
		}
	}


	public static void swap(int[] list, int min, int i) {
		int temp = list[min];
		list[min] = list[i];
		list[i] = temp;
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
