package com.cmg.java.Java算法与数据结构书.chapter3.list.questions;

import com.cmg.java.Java算法与数据结构书.common.ListNode;

/**
 * 逆置k节点链表
 */
public class Q22_ReverseKNodeBlock {


    //k个节点逆置后，设置其最后一个节点指向第k+1节点

    //当前位置移动到k+1节点

    //

    ListNode getKPlusOneNode(int K, ListNode head) {
        if (head == null) return head;
        ListNode Kth;
        int i = 0;
        //检查当前链表剩余部分还有k个节点
        //有 获取k+1指针
        for (i = 0, Kth = head; Kth != null && (i < K); i++, Kth = Kth.getNext()) ;
        if (i == K && Kth != null) return Kth;
        return head.getNext();
    }

    boolean hasKNodes(ListNode head, int K) {
        int i;
        for (i = 0; head != null && (i < K); i++, head = head.getNext()) ;
        if (i == K)
            return true;
        return false;
    }

    ListNode reverseBlockOfKNodesInLinkedList(ListNode head, int K) {
        ListNode temp, next, cur = head, newHead;
        if (K == 0 || K == 1) {
            return head;
        }

        if (hasKNodes(cur, K - 1)) {
            //逆置前面的k个节点
            newHead = getKPlusOneNode(K, cur);
        } else {
            //表头节点
            newHead = head;
        }

        while (cur != null && hasKNodes(cur, K)) {
            temp = getKPlusOneNode(K, cur);
            int i = 0;
            while (i < K) {
                next = cur.getNext();
                cur.setNext(temp);
                temp = cur;
                cur = next;
                i++;
            }
        }
        return newHead;
    }
}
