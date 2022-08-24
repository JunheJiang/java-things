package com.yzxie.java.demo.charter5.part_5_2;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * @author xieyizun
 * @date 18/5/2019 20:30
 * @description:
 */
public class SynchronizedMapDemo {
    public static void main(String[] args) {
        HashMap<String, String> hashMap = new HashMap<>();
        // safeMap为线程安全的
        final Map<String, String> safeMap = Collections.synchronizedMap(hashMap);

        // 线程thread1和线程thread2共享同一个safeMap对象
        Thread thread1 = new Thread(new Runnable() {
            public void run() {
                safeMap.put("key1", "val1");
            }
        });
        Thread thread2 = new Thread(new Runnable() {
            public void run() {
                System.out.println(safeMap.get("key1"));
            }
        });
        thread1.start();
        thread2.start();
    }
}
