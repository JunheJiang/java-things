package com.cmg.java.algorithm算法.排序;

/**
 * 快速排序
 *
 * 交换类型排序
 */
public class QuickSort {

	public static void main(String[] args) {
		int[] list = { 6, 1, 2, 7, 9, 3, 4, 5, 10, 8 };
		display(list);
		quickSort(list, 0, list.length - 1);
		display(list);
	}

	public static void quickSort(int[] list, int left, int right) {
		if (left < right) {
			int point = partition(list, left, right);
			quickSort(list, left, point - 1);
			quickSort(list, point + 1, right);
		}
	}

	public static int partition(int[] list, int left, int right) {
		int first = list[left];
		while (left < right) {
			while (left < right && list[right] >= first) {
				right--;
			}
			swap(list, left, right);
			while (left < right && list[left] <= first) {
				left++;
			}
			swap(list, left, right);
		}
		return left;
	}


	public static void swap(int[] list, int left, int right) {
		int temp;
		if (list != null && list.length > 0) {
			temp = list[left];
			list[left] = list[right];
			list[right] = temp;
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
