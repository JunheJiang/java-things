package com.cmg.java.algorithm算法.leetcodeStarted;

import lombok.extern.slf4j.Slf4j;

import java.util.Random;

/**
 * 二分查找
 */
@Slf4j
public class BinarySearch {
    static final Random random = new Random();

    public static void main(String[] args) {
        int[] num = new int[100];
        for (int i = 0; i < 100; i++) {
            num[i] = random.nextInt(100);
            if (i == 65) {
                num[i] = 65;
            }
        }
        search(num, 65);
    }

    public static int search(int[] nums, int target) {
        int low = 0;
        int high = nums.length - 1;
        while (low <= high) {
            //相同数值情况
            int mid = low + (high - low) / 2;
            int num = nums[mid];
            if (num == target) {
                return mid;
            } else if (num < target) {
                low = mid + 1;
            } else {
                high = mid - 1;
            }
        }
        return -1;
    }

}
