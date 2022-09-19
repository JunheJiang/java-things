package com.cmg.java.Java算法与数据结构书.chapter3.list.questions;

import com.cmg.java.Java算法与数据结构书.common.ListNode;

/**
 * 判断链表长度奇数还是偶数
 */
public class Q15_IsLengthEven {

    /*时间复杂度：O(n)
     * 空间复杂度：O(1)
     * @param head
     * @return
     */
    int isListLengthEven(ListNode head) {
        while (head != null && head.getNext() != null) {
            head = head.getNext().getNext();
        }
        if (head == null) return 0;
        return 1;
    }
}
