package com.cmg.java.Java算法与数据结构书.chapter3.list.questions;

import com.cmg.java.Java算法与数据结构书.common.ListNode;

/**
 * 切割双向链表
 * floyd算法寻找并存储链表的中间节点和最后一个节点指针
 * 把后半部分变成环
 * 把前半部分变成环
 * 设置两个链表的表头指针
 */
public class Q19_SplitToEqualLengthList {

    void SlitList(ListNode head, ListNode head1, ListNode head2) {
        ListNode slowPtr = head, fastPtr = head;
        if (head == null) {
            return;
        }
        /**
         * 奇数个节点、 fastPtr.getNext()指向头节点head
         * 偶数个节点、fastPtr.getNext().getNext()指向头节点head
         */
        while (fastPtr.getNext() != head && fastPtr.getNext().getNext() != head) {
            fastPtr = fastPtr.getNext().getNext();
            slowPtr = slowPtr.getNext();
        }

        //偶数个元素 再向后移动一次指针
        if (fastPtr.getNext().getNext() == head) {
            fastPtr = fastPtr.getNext().getNext();
        }

        head1 = head;

        if (head.getNext() != head) {
            head2 = slowPtr.getNext();
        }

        fastPtr.setNext(slowPtr.getNext());

        slowPtr.setNext(head);
    }
}
