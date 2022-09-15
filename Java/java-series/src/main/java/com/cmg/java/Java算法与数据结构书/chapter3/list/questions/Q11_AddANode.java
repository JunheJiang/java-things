package com.cmg.java.Java算法与数据结构书.chapter3.list.questions;

import com.cmg.java.Java算法与数据结构书.common.ListNode;

/**
 * 有序链表末尾添加一个节点
 */
public class Q11_AddANode {
//    时间复杂度O(n)
//     空降复杂度O(1)

    /**
     * 时间复杂度O(n)
     * 空降复杂度O(1)
     *
     * @param head
     * @param newNode
     * @return
     */
    ListNode insertInSortedList(ListNode head, ListNode newNode) {
        ListNode current = head, temp = head;
        if (head == null) return newNode;
        //有序
        while (current != null && current.getData() < newNode.getData()) {
            temp = current;
            current = current.getNext();
        }
        //插入新节点
        newNode.setNext(current);
        temp.setNext(newNode);
        return head;
    }

}
