package com.cmg.java.algorithm算法.leetcodeStarted;

public class BinarySearchInsert {

    public int searchInsert(int[] nums, int target) {
        int low = 0;
        int high = nums.length - 1;
        int pos = nums.length;
        while (low <= high) {
           int mid = low + (high - low) / 2;
            if (target <= nums[mid]) {
                pos = mid;
                high = mid - 1;
            } else {
                low = mid + 1;
            }
        }
        //尾部为基准
        return pos;
    }
}
