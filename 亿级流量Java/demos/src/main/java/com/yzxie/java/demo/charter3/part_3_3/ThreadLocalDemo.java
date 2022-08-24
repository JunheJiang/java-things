package com.yzxie.java.demo.charter3.part_3_3;

/**
 * @author xieyizun
 * @date 18/6/2019 00:19
 * @description:
 */
public class ThreadLocalDemo {
    // 线程操作计数器
    private static ThreadLocal<Integer> counter = new ThreadLocal<Integer>() {
        @Override
        protected Integer initialValue() {
            // 初始值为0
            return 0;
        }
    };

    public static void main(String[] args) {
        Thread thread1 = new Thread(new Runnable() {
            @Override
            public void run() {
                // 递增100万次
                for (int i = 0; i < 1000000; i++) {
                    Integer counterVaule = counter.get();
                    counterVaule++;
                    counter.set(counterVaule);
                }
                System.out.println("thread1 counter = " + counter.get());
            }
        });
        Thread thread2 = new Thread(new Runnable() {
            @Override
            public void run() {
                // 递增200万次
                for (int i = 0; i < 2000000; i++) {
                    Integer counterVaule = counter.get();
                    counterVaule++;
                    counter.set(counterVaule);
                }
                System.out.println("thread2 counter = " + counter.get());
            }
        });
        thread1.start();
        thread2.start();
    }
}
