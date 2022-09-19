package com.cmg.java.Java算法与数据结构书.chapter3.list.questions;

import com.cmg.java.Java算法与数据结构书.common.ListNode;

/**
 * 找到链表的中间节点
 */
public class Q14_FindMiddle {

    //法一：暴力
    //O(n*n) O(1)

    //法二：n n/2

    //法三：散列表
    // 技术

    //法四：一次扫描
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

}
