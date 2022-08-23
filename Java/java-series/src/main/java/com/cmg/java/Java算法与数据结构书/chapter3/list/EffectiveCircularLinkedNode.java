package com.cmg.java.Java算法与数据结构书.chapter3.list;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class EffectiveCircularLinkedNode<T> implements Serializable {
    private T data;
    //current's diff = pre xor next
    private EffectiveCircularLinkedNode diff;

    public EffectiveCircularLinkedNode(T data) {
        this.data = data;
    }

    //based on point difference
    //指针的差通过异或运算得到

    //x xor x = 0
    //x xor 0 = x
    //x xor y = y xor x  (symmetry)
    //(x xor y) xor z = x xor (y xor z) (transitivity)
}
