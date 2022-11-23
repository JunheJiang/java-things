package com.cmg.java.Java算法与数据结构书.chapter3.list.questions;

import com.cmg.java.Java算法与数据结构书.common.ListNode;

/**
 * 寻找模具节点
 */
public class Q26_FindNode {

    //从表头找
    //编号 i
    //n k
    //最后一个整除数

    ListNode modularNodes(ListNode head, int k) {
        ListNode modularNode = null;
        int i = 1;
        if (k < 0) {
            return null;
        }

        for (; head != null; head = head.getNext()) {
            if (i % k == 0)
                modularNode = head;
            i++;
        }

        return modularNode;
    }

    //从表尾找
    ListNode modularNodeFromTail(ListNode head, int k) {
        ListNode modularNode = head;
        int i = 0;
        if (k < 0) {
            return null;
        }

        for (i = 0; i < k; i++) {
            if (head != null)
                head = head.getNext();
            else
                return null;
        }

        while (head != null) {
            modularNode = modularNode.getNext();
            head = head.getNext();
        }
        return modularNode;
    }

    //分数节点 n/k
    ListNode fractionalNode(ListNode head, int k) {
        ListNode fractionalNode = null;
        int i = 0;
        if (k < 0) {
            return null;
        }

        for (; head != null; head = head.getNext()) {
            if (i % k == 0) {
                if (fractionalNode != null) {
                    fractionalNode = head;
                }
                else {
                     fractionalNode = fractionalNode.getNext();
                }
            }
            i++;
        }


        return fractionalNode;
    }
}
