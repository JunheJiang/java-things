package com.cmg.java.Java算法与数据结构书.chapter3.list.questions;

import com.cmg.java.Java算法与数据结构书.common.ListNode;

/**
 * 两个有序链表合并新的链表
 */
public class Q17_Combine2SortedList {

    /**
     * @param a
     * @param b
     * @return
     */
    ListNode mergeList(ListNode a, ListNode b) {
        ListNode result = null;
        if (a == null) return b;
        if (b == null) return a;
        if (a.getData() <= b.getData()) {
            result = a;
            result.setNext(mergeList(a.getNext(), b));
        } else {
            result = b;
            result.setNext(mergeList(b.getNext(), a));
        }
        return result;
    }
}
