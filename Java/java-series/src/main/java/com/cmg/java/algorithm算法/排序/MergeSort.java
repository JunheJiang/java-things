package com.cmg.java.algorithm算法.排序;

/**
 * 归并排序
 */
public class MergeSort {

	public static void main(String[] args) {
		int[] list = { 50, 10, 90, 30, 70 };
		display(list);
		mergeSort(list, new int[list.length], 0, list.length - 1);
		display(list);
	}

	public static void mergeSort(int[] list, int[] tempList, int head, int rear) {
		if (head < rear) {
			int middle = (head + rear) / 2;
			mergeSort(list, tempList, head, middle);
			mergeSort(list, tempList, middle + 1, rear);
			merge(list, tempList, head, middle + 1, rear);
		}
	}

	public static void merge(int[] list, int[] tempList, int head, int middle, int rear) {
		int headEnd = middle - 1;
		int rearStart = middle;
		int tempIndex = head;
		int tempLength = rear - head + 1;
		while ((headEnd >= head) && (rearStart <= rear)) {
			if (list[head] < list[rearStart]) {
				tempList[tempIndex++] = list[head++];
			} else {
				tempList[tempIndex++] = list[rearStart++];
			}
		}

		while (head <= headEnd) {
			tempList[tempIndex++] = list[head++];
		}

		while (rearStart <= rear) {
			tempList[tempIndex++] = list[rearStart++];
		}

		for (int i = 0; i < tempLength; i++) {
			list[rear] = tempList[rear];
			rear--;
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
