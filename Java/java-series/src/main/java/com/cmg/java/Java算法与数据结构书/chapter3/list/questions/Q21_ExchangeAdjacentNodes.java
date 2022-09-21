package com.cmg.java.Java算法与数据结构书.chapter3.list.questions;

import com.cmg.java.Java算法与数据结构书.common.ListNode;

/**
 * 交互相邻节点
 */
public class Q21_ExchangeAdjacentNodes {


    void exchangeAdjacentNodes(ListNode head) {
        ListNode curNode, temp, nextNode;
        curNode = head;
        if (curNode == null || curNode.getNext() == null) {
            return;
        }
        
        head = curNode.getNext();

        while (curNode != null && curNode.getNext() != null) {
            nextNode = curNode.getNext();
            curNode.setNext(nextNode.getNext());
            temp = curNode.getNext();
            nextNode.setNext(curNode);
            if (temp != null && temp.getNext() != null) {
                curNode.setNext(curNode.getNext().getNext());
            }
            curNode = temp;
        }
    }

}
