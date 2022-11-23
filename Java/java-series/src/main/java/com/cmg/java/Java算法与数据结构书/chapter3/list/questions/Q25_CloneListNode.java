package com.cmg.java.Java算法与数据结构书.chapter3.list.questions;

import com.cmg.java.Java算法与数据结构书.common.ListNode;

import java.util.HashMap;
import java.util.Map;

/**
 * copy list node
 */
public class Q25_CloneListNode {

    ListNode clone(ListNode head) {
        ListNode X = head, Y;
        Map<ListNode, ListNode> HT = new HashMap<>();

        while (X != null) {
            Y = new ListNode();
            Y.setData(X.getData());
            Y.setNext(null);
            Y.setRandom(null);
            HT.put(X, Y);
            X = X.getNext();
        }

        X = head;

        //扫描原列表
        while (X != null) {
            //从散列表获得Y对应的节点Y
            Y = HT.get(X);
            Y.setNext(HT.get(X.getNext()));
            Y.setRandom(HT.get(X.getRandom()));
            X = X.getNext();
        }

        //新链表表头
        return HT.get(head);
    }

}
