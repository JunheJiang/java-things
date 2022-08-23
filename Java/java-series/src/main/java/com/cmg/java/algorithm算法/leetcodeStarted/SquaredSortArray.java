package com.cmg.java.algorithm算法.leetcodeStarted;

/**
 * 平方后生序数组
 */
public class SquaredSortArray {
    /**
     * //暴力解法
     * //平方
     * //排序
     *O(nlogn)
     * @param nums
     * @return
     */
//    public int[] sortedSquares(int[] nums) {
//        for (int i = 0; i < nums.length; i++) {
    //n
//            nums[i] = nums[i] * nums[i];
//        }
    //logn
//        Arrays.sort(nums);
//        return nums;
//    }

    /**
     * 双指针
     *
     * @param nums
     * @return
     */
//    public int[] sortedSquares(int[] nums) {
//        int n = nums.length;
//        int negative = -1;
//        //找到第一个负数
//        for (int i = 0; i < n; ++i) {
//            if (nums[i] < 0) {
//                negative = i;
//            } else {
//                break;
//            }
//        }
//         //负数一组  非负数一组
//        int[] ans = new int[n];
//        int index = 0, i = negative, j = negative + 1;
//        while (i >= 0 || j < n) {
//            if (i < 0) {
//                ans[index] = nums[j] * nums[j];
//                ++j;
//            } else if (j == n) {
//                ans[index] = nums[i] * nums[i];
//                --i;
//            } else if (nums[i] * nums[i] < nums[j] * nums[j]) {
//                ans[index] = nums[i] * nums[i];
//                --i;
//            } else {
//                ans[index] = nums[j] * nums[j];
//                ++j;
//            }
//            ++index;
//        }
//        return ans;
//    }

    /**
     * 双向指针 一头一尾
     *
     * @param nums
     * @return
     */
    public int[] sortedSquares(int[] nums) {
        int n = nums.length;
        int[] ans = new int[n];
        for (int i = 0, j = n - 1, pos = n - 1; i <= j; ) {
            if (nums[i] * nums[i] > nums[j] * nums[j]) {
                ans[pos] = nums[i] * nums[i];
                ++i;
            } else {
                ans[pos] = nums[j] * nums[j];
                --j;
            }
            --pos;
        }
        return ans;
    }

}
