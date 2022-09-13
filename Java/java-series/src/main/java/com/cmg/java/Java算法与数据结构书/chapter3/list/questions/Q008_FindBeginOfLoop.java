package com.cmg.java.Java算法与数据结构书.chapter3.list.questions;

import com.cmg.java.Java算法与数据结构书.common.ListNode;

/**
 * 有环找到环起点位置
 */
public class Q008_FindBeginOfLoop {

    /**
     * 时间复杂度 O(n)
     * * 空间复杂度 O(1)
     * * 稳定性
     *
     * @param head
     * @return
     */
    ListNode findBeginOfLoop(ListNode head) {
        ListNode slowPtr = head, fastPtr = head;
        boolean loopExists = false;
        if (head == null) return null;
        while (fastPtr.getNext() != null &&
                fastPtr.getNext().getNext() != null) {
            slowPtr = slowPtr.getNext();
            fastPtr = fastPtr.getNext().getNext();
            if (slowPtr == fastPtr) {
                loopExists = true;
                break;
            }
        }
        //有环 继续走 去找起点位置
        if (loopExists) {
            slowPtr = head;
            while (slowPtr != fastPtr) {
                fastPtr = fastPtr.getNext();
                slowPtr = slowPtr.getNext();
            }
            return slowPtr;
        }
        return null;
    }
}
