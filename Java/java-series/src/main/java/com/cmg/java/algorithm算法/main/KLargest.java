package com.cmg.java.algorithm算法.main;

import java.util.PriorityQueue;
//703

/**
 * 返回数据流中第k个最大
 */
public class KLargest {
    final PriorityQueue<Integer> q;
    final int k;

    public KLargest(int k, int[] a) {
        this.k = k;
        q = new PriorityQueue<>(k);
        for (int n : a) {
            add(n);
        }
    }

    //mini heap
    //维护一个k大小的优先队列
    public int add(int n) {
        if (q.size() < k) {
            q.offer(n);
        } else if (q.peek() < n) {
            q.poll();
            q.offer(n);
        }
        return q.peek();
    }
}
