package com.cmg.java.Java算法与数据结构书.chapter3.list.questions;

import com.cmg.java.Java算法与数据结构书.common.ListNode;

/**
 * 判断链表有环
 */
public class Q06_HasLoop {
    /**
     * 时间复杂度 O(n)
     * 空间复杂度 O(1)
     * 稳定性
     *
     * @param head
     * @return
     */
    boolean doesLinkedListContainsLoop(ListNode head) {
        if (head == null) return false;
        ListNode slowPtr = head, fastPtr = head;
        while (fastPtr.getNext() != null &&
                fastPtr.getNext().getNext() != null) {
            slowPtr = slowPtr.getNext();
            fastPtr = fastPtr.getNext().getNext();
            if (slowPtr == fastPtr) return true;
        }
        return false;
    }
}
