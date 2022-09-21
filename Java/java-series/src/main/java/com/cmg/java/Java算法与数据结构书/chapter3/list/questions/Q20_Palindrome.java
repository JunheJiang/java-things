package com.cmg.java.Java算法与数据结构书.chapter3.list.questions;

import com.cmg.java.Java算法与数据结构书.common.ListNode;

/**
 * 判断链表是否是回文结构
 */
public class Q20_Palindrome {

    //获取链表的中间节点

    //把链表的后半部分逆置

    //比较链表的前半部分和后半部分

    void isPalindrome(ListNode head) {
        ListNode middleNode = findMiddle(head);
        if (middleNode != null) {

        }
    }

    int isListLengthEven(ListNode head) {
        while (head != null && head.getNext() != null) {
            head = head.getNext().getNext();
        }
        //偶数
        if (head == null) return 0;
        //奇数
        return 1;
    }

    //两倍移动速度
    ListNode findMiddle(ListNode head) {
        ListNode ptr1, ptr2;
        ptr1 = ptr2 = head;
        int i = 0;

        while (ptr1.getNext() != null) {
            //只后移第一个指针
            if (i == 0) {
                ptr1 = ptr1.getNext();
                i = 1;
            }
            //两个都后移动
            else if (i == 1) {
                ptr1 = ptr1.getNext();
                ptr2 = ptr2.getNext();
                i = 0;
            }

        }
        return ptr2;
    }

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
