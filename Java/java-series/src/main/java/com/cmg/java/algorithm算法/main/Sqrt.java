package com.cmg.java.algorithm算法.main;

/**
 * 69题
 * 平方根
 */
public class Sqrt {
    int sqrt(int x) {
        if (x == 0 || x == 1) return x;
        int l = 1, r = x, res = 0;
        while (l <= r) {
            int m = (l + r) / 2;
            //避免越界
            if (m == x / m) {
                return m;
            } else if (m > x / m) {
                r = m - 1;
            } else {
                l = m + 1;
                res = m;
            }
        }
        return res;
    }

}
