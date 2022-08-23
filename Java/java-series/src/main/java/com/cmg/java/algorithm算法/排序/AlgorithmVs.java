package com.cmg.java.algorithm算法.排序;

public class AlgorithmVs {
    public static void main(String[] args) {

        /**
         * 使用场景
         */

        /**
         * 原理
         *
         */

        //插入排序
        // 插入
        // 第i个元素都要和前面n-1个元素比较 swap
        // 时间复杂度：最坏情况 O(n*n)
        // 空间复杂度：中间temp存储 O(1)
        //稳定性：稳定

        // 希尔 分组插入排序 缩减增量排序  增量即是组数 折半取整 直到增量为1
        // 时间复杂度:O(n)
        // 空间复杂度:O(1)
        //稳定性：跳跃式排序 不稳定

        //交换排序
        // 冒泡
        // 时间复杂度:O(n) O(n*n)
        // 空间复杂度:O(1)
        //稳定性：逐个比较 稳定

        // 快速 分治递归
        // 时间复杂度:O(n·log(n)) O(n*n)
        // 空间复杂度:O(1）
        //稳定性：比较移动 不稳定
        //适合场景：数据量大且无序

        //选择排序：
        // 选择 直接选择最小的摆位置
        // 时间复杂度: O(n*n)
        // 空间复杂度:O(1)
        //稳定性：不稳定

        // 堆 大往大堆放  小往小堆放 完全二叉树
        // 时间复杂度: O(log(n)*n) 分割趟数*比较移动次数
        // 空间复杂度:O(1)
        //稳定性：两两比较 稳定

        //归并排序 长度为1的子序列  两两进行归并


        //选择类型
        testHeap();
        testSelection();

        //交换类型
        testQuickSort();
        testBubble();

        //插入类型
        testShell();
        testMerge();
        testInsert();
    }

    public static void testQuickSort() {
        System.out.println("交换类型：testQuickSort");
        int[] list = new int[10000];
        for (int i = 0; i < 10000; i++) {
            list[i] = (int) (Math.random() * 100000);
        }
        long start = System.currentTimeMillis();
        QuickSort.quickSort(list, 0, list.length - 1);
        long end = System.currentTimeMillis();
        System.out.println("testQuickSort 耗时：" + (end - start));
        display(list);
    }

    public static void testBubble() {
        System.out.println("交换类型：testBubble");
        int[] list = new int[10000];
        for (int i = 0; i < 10000; i++) {
            list[i] = (int) (Math.random() * 100000);
        }

        long start = System.currentTimeMillis();
        BubbleSort.bubbleSort(list);
        long end = System.currentTimeMillis();
        System.out.println("testBubble 耗时：" + (end - start));
        display(list);
    }


    public static void testInsert() {
        System.out.println("插入类型：testInsert");
        int[] list = new int[10000];
        for (int i = 0; i < 10000; i++) {
            list[i] = (int) (Math.random() * 100000);
        }

        long start = System.currentTimeMillis();
        InsertSort.insertSort(list);
        long end = System.currentTimeMillis();
        System.out.println("ֱtestInsert 耗时：" + (end - start));
        display(list);
    }


    public static void testHeap() {
        System.out.println("选择类型：testHeap");
        int[] list = new int[10000];
        for (int i = 0; i < 10000; i++) {
            list[i] = (int) (Math.random() * 100000);
        }
        long start = System.currentTimeMillis();
        HeapSort.heapSort(list);
        long end = System.currentTimeMillis();
        System.out.println("testHeap 耗时：" + (end - start));
        display(list);
    }


    public static void testMerge() {
        System.out.println("插入类型：testMerge");
        int[] list = new int[10000];
        for (int i = 0; i < 10000; i++) {
            list[i] = (int) (Math.random() * 100000);
        }
        long start = System.currentTimeMillis();
        MergeSort.mergeSort(list, new int[list.length], 0, list.length - 1);
        long end = System.currentTimeMillis();
        System.out.println("testMerge 耗时：" + (end - start));
        display(list);
    }


    public static void testShell() {
        System.out.println("插入类型：testShell");
        int[] list = new int[10000];
        for (int i = 0; i < 10000; i++) {
            list[i] = (int) (Math.random() * 100000);
        }
        long start = System.currentTimeMillis();
        ShellSort.shellSort(list);
        long end = System.currentTimeMillis();
        System.out.println("testShell 耗时：" + (end - start));
        display(list);
    }

    public static void testSelection() {
        System.out.println("选择类型：testSelection");
        int[] list = new int[10000];
        for (int i = 0; i < 10000; i++) {
            list[i] = (int) (Math.random() * 100000);
        }
        long start = System.currentTimeMillis();
        SelectionSort.selectionSort(list);
        long end = System.currentTimeMillis();
        System.out.println("ֱtestSelection 耗时：" + (end - start));
        display(list);
    }


    public static void display(int[] list) {
        System.out.println("********display start********");
        if (list != null && list.length > 0) {
            for (int i = 0; i < 10; i++) {
                System.out.print(list[i] + " ");
            }
            System.out.println("");
        }
        System.out.println("********display end**********");
        System.out.println("");
    }

}
