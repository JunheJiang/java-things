package com.cmg.java.Java算法与数据结构书.chapter3.list.questions;


import com.cmg.java.Java算法与数据结构书.common.ListNode;

/**
 * 找到链表中倒数第n个节点
 */
public class Q02To5FindLastNNode {
    //法-：蛮力法
    //统计当前节点后面的节点个数 n-1
    //小于n-1
    //大于n-1 往下移动
    //等于n-1 找到
    //n平方 哦1

    //法二：散列表
    //<节点位置,节点地址>
    //遍历链表获得长度M
    //转换为找散列表主键为M-N+1位置的数
    //哦N 哦N

    //法三：两次遍历
    //一次得到长度M
    //二次到M-N+1
    //哦N 哦1

    //法四：一次扫描找到
    //两个指针
    //第一个指针先行n
    //后两个指针同时移动、第一个到达末尾时，第二个到倒数N处
    //哦N 哦1
    ListNode nthNodeFromEnd(ListNode head, int n) {
        ListNode pTemp = head, pNthNode = null;
        for (int count = 1; count < n; count++) {
            if (null != pTemp) {
                pTemp = pTemp.getNext();
            }
        }
        while (null != pTemp) {
            if (null == pNthNode)
                pNthNode = head;
            else pNthNode = pNthNode.getNext();
            pTemp = pTemp.getNext();
        }
        if (null != pNthNode)
            return pNthNode;
        return null;
    }

}
