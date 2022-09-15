package com.cmg.java.Java算法与数据结构书.chapter3.list.questions;

import com.cmg.java.Java算法与数据结构书.common.ListNode;

/**
 * 环长度
 */
public class Q010_LoopLength {
    /**
     * 时间复杂度 O(n)
     * 空间复杂度 O(1)
     *
     * @param head
     * @return
     */
    int findLength(ListNode head) {
        ListNode slowPtr = head, fastPtr = head;
        boolean loopExits = false;
        int counter = 0;
        if (head == null) return 0;
        while (fastPtr.getNext() != null &&
                fastPtr.getNext().getNext() != null) {
            slowPtr = slowPtr.getNext();
            fastPtr = fastPtr.getNext();
            if (slowPtr == fastPtr) {
                loopExits = true;
                break;
            }
        }
        if (loopExits) {
            fastPtr = fastPtr.getNext();
            while (slowPtr != fastPtr) {
                fastPtr = fastPtr.getNext();
                counter++;
            }
            return counter;
        }
        return counter;
    }
}
