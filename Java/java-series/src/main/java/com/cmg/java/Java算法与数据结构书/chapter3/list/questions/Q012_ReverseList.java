package com.cmg.java.Java算法与数据结构书.chapter3.list.questions;

import com.cmg.java.Java算法与数据结构书.common.ListNode;

/**
 * 逆转链表
 */
public class Q012_ReverseList {
    /**
     * 时间复杂度O(n)
     * 空降复杂度O(1)
     *
     * @param head
     * @return
     */
    ListNode reverseList(ListNode head) {
        ListNode temp = null, nextNode = null;
        while (head != null) {
            nextNode = head.getNext();
            head.setNext(temp);
            temp = head;
            head = nextNode;
        }
        return temp;
    }
}
