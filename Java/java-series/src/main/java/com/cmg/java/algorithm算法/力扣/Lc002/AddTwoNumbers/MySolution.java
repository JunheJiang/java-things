package com.cmg.java.algorithm算法.力扣.Lc002.AddTwoNumbers;

import lombok.extern.slf4j.Slf4j;

/**
 * 给你两个 非空 的链表，表示两个非负的整数。它们每位数字都是按照 逆序 的方式存储的，并且每个节点只能存储 一位 数字。
 * <p>
 * 请你将两个数相加，并以相同形式返回一个表示和的链表。
 * <p>
 * 来源：力扣（LeetCode）
 * 链接：https://leetcode-cn.com/problems/add-two-numbers
 * 著作权归领扣网络所有。商业转载请联系官方授权，非商业转载请注明出处。
 */
@Slf4j
public class MySolution {

    public static ListNode addTwoNumbers(ListNode l1, ListNode l2) {
        //多地址引用便说明 current不断赋用 dummyHead逗留原处
        ListNode dummyHead = new ListNode(0);
        ListNode p = l1, q = l2, current = dummyHead;
        int carry = 0;
        while (p != null || q != null) {
            int oneBit = (p != null) ? p.val : 0;
            int anotherBit = (q != null) ? q.val : 0;
            int sum = carry + oneBit + anotherBit;
            //进位余数得到新的位数
            carry = sum / 10;
            //取余留位
            current.next = new ListNode(sum % 10);
            //赋用链移
            current = current.next;
            if (p != null) p = p.next;
            if (q != null) q = q.next;
        }
        if (carry > 0) {
            current.next = new ListNode(carry);
        }
        return dummyHead.next;
    }

    /**
     * test use time:1ms
     *
     * @param args
     */
    public static void main(String[] args) {
        ListNode firstNodeInList1 = new ListNode(2);
        ListNode secondNodeInList1 = new ListNode(4);
        ListNode thirdNodeInList1 = new ListNode(3);
        firstNodeInList1.setNext(secondNodeInList1);
        secondNodeInList1.setNext(thirdNodeInList1);

        ListNode firstNodeInList2 = new ListNode(5);
        ListNode secondNodeInList2 = new ListNode(6);
        ListNode thirdNodeInList2 = new ListNode(4);
        firstNodeInList2.setNext(secondNodeInList2);
        secondNodeInList2.setNext(thirdNodeInList2);
        long time = System.currentTimeMillis();
        log.info(MySolution.addTwoNumbers(firstNodeInList1, firstNodeInList2).toString());
        log.info("addTwoNumbers use time::" + (System.currentTimeMillis() - time));
    }
}
