package com.cmg.java.Java算法与数据结构书.chapter2.recursion;

/**
 * 递归算法
 */
public class Recursion {

    //斐波那契数列 阶乘 归并排序 快速排序 二分查找 树的中前后序遍历
    //动态规划 分置算法 汉诺塔 回溯

    /**
     * Hanoi Tower
     * 3 pegs
     *
     * @param n         total plates
     * @param fromPeg   Peg A
     * @param topPeg    Peg B
     * @param assistPeg Peg C
     */
    void towerOfHanoi(int n, char fromPeg, char topPeg, char assistPeg) {
        if (n == 1) {
            System.out.println("one move,done");
        }
        // 把C作为辅助桩，A为原桩，B为目的桩，将A柱子最上面的圆盘移到B柱子上
        //Initially take Peg C as assistant one，B as destination one ，A as original
        towerOfHanoi(n - 1, fromPeg, assistPeg, topPeg);
        // remove lefts on Peg A to Peg C
        System.out.println("move from peg " + fromPeg + " to peg" + topPeg);
        //regard Peg A as assistant，remove last one on Peg B to Peg C，finished
        towerOfHanoi(n - 1, assistPeg, topPeg, fromPeg);
    }

    /**
     * whether the array is sorted or not
     *
     * @param arr
     * @param index
     */
    boolean isIntArraySorted(int[] arr, int index) {
        if (arr.length == 1) return true;
        return (arr[index - 1] <= arr[index - 2] ? false : isIntArraySorted(arr, index - 1));
    }

    /**
     * 阶乘 factorial
     *
     * @param n
     * @return
     */
    int factorial(int n) {
        int result = 1;
        for (int i = 2; i < n; i++) {
            result *= i;
        }
        return result;
    }


    //递归与迭代区别与优劣势
    //递归调用函数自身，递推加回归，每次递推就会产生一个函数副本，每一次回归
    // 迭代遍历环，更新环的状态，返回值是下一次循环到初始值
    //回溯是一种分治策略进行穷枚举搜索的方法

    //actually what happened  cases in business situation
    //业务实际使用场景

    /**
     * 长度为n的二进制串
     * 回溯产生所有的二进制串
     * back track for binary string
     * 大O 2的n次幂
     *
     * @param n
     */
    void binaryStr(int n) {
        int[] arr = new int[n];
        if (n < 1) {
            System.out.println(arr);
        } else {
            arr[n - 1] = 0;
            binaryStr(n - 1);
            arr[n - 1] = 1;
            binaryStr(n - 1);
        }
    }

    /**
     * 长度为n的k进制串
     * 大O k 的n次幂
     *
     * @param n
     * @param k
     */
    void kString(int n, int k) {
        int[] arr = new int[n];
        if (n < 1) {
            System.out.println(arr);
        } else {
            for (int j = 0; j < k; j++) {
                arr[n - 1] = j;
                kString(n - 1, k);


            }
        }
    }


}
