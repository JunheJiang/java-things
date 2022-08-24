package com.yzxie.java.demo.charter6.part_6_4;

import java.util.concurrent.CopyOnWriteArrayList;

/**
 * @author xieyizun
 * @date 20/5/2019 22:35
 * @description:
 */
public class CopyOnWriteArrayListDemo {
    public static void main(String[] args) {
        // 初始化缓存
        CopyOnWriteArrayList<String> cache = new CopyOnWriteArrayList<>(new String[] {"item-0"});
        // 两个读线程，一个后台定时更新线程
        new Thread(new CacheReadTask(cache)).start();
        new Thread(new CacheReadTask(cache)).start();

        new Thread(new CacheUpdateTask(cache)).start();
    }
    // 读线程
    static class CacheReadTask implements Runnable {
        private CopyOnWriteArrayList<String> cache;

        public CacheReadTask(CopyOnWriteArrayList<String> cache) {
            this.cache = cache;
        }

        @Override
        public void run() {
            while (true) {
                // 打印第一个元素
                if (cache.size() > 0) {
                    System.out.println(Thread.currentThread().getName() + " read " + cache.get(0));
                }
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    // 后台定时更新缓存线程
    static class CacheUpdateTask implements Runnable {
        private CopyOnWriteArrayList<String> cache;

        public CacheUpdateTask(CopyOnWriteArrayList<String> cache) {
            this.cache = cache;
        }

        @Override
        public void run() {
            int i = 0;
            while (true) {
                // 每隔2秒，更新缓存
                cache.set(0, "item-" + i++);
                System.out.println(Thread.currentThread().getName() + " update cache");
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
