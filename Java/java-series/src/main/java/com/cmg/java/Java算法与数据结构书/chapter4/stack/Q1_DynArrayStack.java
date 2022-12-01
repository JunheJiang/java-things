package com.cmg.java.Java算法与数据结构书.chapter4.stack;

/**
 * 基于动态数组实现栈
 * <p>
 * 栈满 复制消耗
 * <p>
 * 重复倍增、提高性能
 * <p>
 * push 一次平摊 O(1)、 push n 次 logN次倍增
 * <p>
 * 倍增太多可能导致内存溢出
 */
public class Q1_DynArrayStack {

    private int top;
    private int capacity;
    private int[] array;

    public Q1_DynArrayStack() {
        top = -1;
        capacity = 1;
        array = new int[capacity];
    }

    public boolean isEmpty() {
        return top == -1;
    }

    public boolean isStackFull() {
        return top == capacity - 1;
    }

    public void doubleStack() {
        int newArray[] = new int[capacity * 2];
        System.arraycopy(array, 0, newArray, 0, capacity);
        capacity = capacity * 2;
        array = newArray;
    }

    public int pop() {
        if (isEmpty()) return 0;
        else return array[top--];
    }

    public void deleteStack() {
        top = -1;
    }


}
