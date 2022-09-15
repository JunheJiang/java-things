package com.cmg.java.Java算法与数据结构书.chapter3.list.questions;

import com.cmg.java.Java算法与数据结构书.common.ListNode;

/**
 * list m
 * list n
 * 找到链表合并点
 */
public class Q13_TwoListMeetingPoint {

    //法一：暴力
    // 栈 比较元素
    //时间复杂度O(m+n) 空间复杂度O(m+n)

    //法二：数组解 保存两个链表所有节点的后继节点
    //在数组中查找第一个重复元素
    //时间复杂度O(m+n) 空间复杂度O(m+n)

    //法三：排序和搜索技术
//    ：数组保存第一个list节点
    //排序
    //对第二个list在排序数组中搜索

    /**
     * 法四
     * 获取两个链表对长度： ListNode O(max(m,n))
     * 计算两个列表对长度差：d
     * 从较长对列表表头开始、移动d步
     * 两个链表同时开始移动 直到出现两个后继指针值相等对情况
     * 时间复杂度：O(max(m,n))
     * 空间复杂度：O(1)
     *
     * @param list1
     * @param list2
     * @return
     */
    ListNode findIntersectingNode(ListNode list1, ListNode list2) {
        int l1 = 0,
                l2 = 0,
                diff = 0;
        ListNode head1 = list1, head2 = list2;

        while (head1 != null) {
            l1++;
            head1 = head1.getNext();
        }
        while (head2 != null) {
            l2++;
            head2 = head2.getNext();
        }

        if (l1 < l2) {
            head1 = list2;
            head2 = list1;
            diff = l2 - l1;
        } else {
            head1 = list1;
            head2 = list2;
            diff = l1 - l2;
        }

        for (int i = 0; i < diff; i++) {
            head1 = head1.getNext();
        }

        while (head1 != null && head2 != null) {
            if (head1 == head2) {
                return head1;
            }
            head1 = head1.getNext();
            head2 = head2.getNext();
        }

        return null;

    }

}
