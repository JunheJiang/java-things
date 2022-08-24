package com.yzxie.java.demo.charter3.part_3_1;

/**
 * @author xieyizun
 * @date 14/6/2019 10:09
 * @description:
 */
public class ThreadDemo {
    public static void main(String[] args) {
        //testSleep();
        testYield();
    }

    public static void testSleep() {
        while (true) {
            long start = System.currentTimeMillis();

            System.out.println("check something");

            try {
                // 暂停1秒，让出CPU资源给其他线程竞争
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            System.out.println("const time in ms: " + (System.currentTimeMillis() - start));
        }
    }

    public static void testYield() {
        while (true) {
            long start = System.currentTimeMillis();

            System.out.println("check something");
            // 暂停，让同等优先级的线程竞争获取CPU，自身也参与竞争
            Thread.yield();

            System.out.println("const time in ms: " + (System.currentTimeMillis() - start));
        }
    }
}
