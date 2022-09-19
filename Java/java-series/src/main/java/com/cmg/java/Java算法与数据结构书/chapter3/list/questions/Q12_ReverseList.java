package com.cmg.java.Java算法与数据结构书.chapter3.list.questions;

import com.cmg.java.Java算法与数据结构书.common.ListNode;

/**
 * 逆转链表
 */
public class Q12_ReverseList {

    /**
     * 时间复杂度O(n)
     * 空降复杂度O(1)
     * 迭代版本
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

    /**
     * 递归遍历至表尾、当返回时、输出元素
     * <p>
     * 时间复杂度O(n)
     * 空降复杂度O(n)
     *
     * @param head
     */
    void printListFromEnd(ListNode head) {
        if (head == null) return;
        printListFromEnd(head.getNext());
        System.out.println(head.getData());
    }
}
