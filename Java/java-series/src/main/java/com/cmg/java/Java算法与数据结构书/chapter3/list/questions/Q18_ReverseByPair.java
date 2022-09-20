package com.cmg.java.Java算法与数据结构书.chapter3.list.questions;

import com.cmg.java.Java算法与数据结构书.common.ListNode;

/**
 * 逐对逆转链表
 */
public class Q18_ReverseByPair {

    /**
     * 递归版本
     *
     * @param head
     * @return
     */
    ListNode reverseByPairRecursively(ListNode head) {
        ListNode temp;
        if (head == null || head.getNext() == null) {
            return null;
        } else {
            temp = head.next;
            head.next = temp.next;
            temp.next = head;
            head = temp;
            head.next.next = reverseByPairRecursively(head.next.next);
            return head;
        }
    }

    /**
     * 迭代版本
     *
     * @param head
     * @return
     */
    ListNode reverseByPairIteratively(ListNode head) {
        ListNode temp1 = null;
        ListNode temp2 = null;
        while (head != null && head.next != null) {

            if (temp1 != null) {
                temp1.next.next = head.next;
            }

            temp1 = head.next;
            head.next = head.next.next;
            temp1.next = head;

            if (temp2 != null) {
                temp2 = temp1;
            }

            head = head.next;
        }
        return temp2;
    }
}
