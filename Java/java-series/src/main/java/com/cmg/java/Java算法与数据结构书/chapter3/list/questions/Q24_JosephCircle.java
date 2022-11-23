package com.cmg.java.Java算法与数据结构书.chapter3.list.questions;

import com.cmg.java.Java算法与数据结构书.common.ListNode;
import lombok.extern.slf4j.Slf4j;

/**
 * 佛洛依德判断链表是否有环
 * <p>
 * 约瑟夫环
 * N节点成环 排除第M个人 重新数到M再剔除 最后留在环中的人
 */
@Slf4j
public class Q24_JosephCircle<p> {

    void getJosephusPosition(int N, int M) {
        //建立一个包含所有人的链表
        ListNode p = new ListNode(1);
        ListNode q = p;

        for (int i = 2; i <= N; i++) {
            p = p.getNext();
            p.setData(i);
        }
        //构建环
        p.setNext(q);

        for (int count = N; count > 1; --count) {
            for (int i = 0; i < M - 1; ++i)
                p = p.getNext();
            //删除淘汰选手
            p.setNext(p.getNext().getNext());
        }

        log.info("Last left player standing position::" + p.getData());
    }


}
