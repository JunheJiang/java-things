package com.cmg.java.algorithm算法.main;

import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;

/**
 * test wasted time:2ms
 */
@Slf4j
public class TwoNums {
    public static int[] twoSum(int[] nums, int target) {
        long time = System.currentTimeMillis();
        Map<Integer, Integer> store = new HashMap<>();
        for (int i = 0; i < nums.length; i++) {
            int anotherNum = target - nums[i];
            if (store.containsKey(anotherNum)) {
                log.info("twoSum use::" + (System.currentTimeMillis() - time));
                return new int[]{store.get(anotherNum), i};
            }
            store.put(nums[i], i);
        }
        return null;
    }

    /**
     * 给定一个整数数组 nums 和一个整数目标值 target，请你在该数组中找出 和为目标值 target  的那 两个 整数，并返回它们的数组下标。
     */
    public static void main(String[] args) {
        log.info("" + com.cmg.java.algorithm算法.力扣.Lc001.TowSum.MySolution.twoSum(new int[]{0, 4, 3, 0}, 0));
    }
}
