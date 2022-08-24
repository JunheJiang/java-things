package com.yzxie.java.demo.charter3.part_3_3;

/**
 * @author xieyizun
 * @date 18/6/2019 11:59
 * @description:
 */
public class ThreadLocalDemo2 {
    // 每个线程的第几次操作
    private static ThreadLocal<Integer> nextWorkId = new ThreadLocal<Integer>() {
        @Override
        protected Integer initialValue() {
            return 1;
        }
    };


    public static void main(String[] args) {
        Thread thread1 = new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < 10; i++) {
                    // 打印并递增工作id
                    System.out.println("thread1 work id: " + nextWorkId.get());
                    Integer nextId = nextWorkId.get();
                    nextWorkId.set(++nextId);
                    // 每隔一秒
                    try {
                        Thread.sleep(1000);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        Thread thread2 = new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < 10; i++) {
                    System.out.println("thread2 work id: " + nextWorkId.get());
                    Integer nextId = nextWorkId.get();
                    nextWorkId.set(++nextId);

                    try {
                        Thread.sleep(1000);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        thread1.start();
        thread2.start();
    }
}
