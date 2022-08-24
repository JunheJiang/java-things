package com.yzxie.java.demo.charter5.part_5_1;

import java.util.Hashtable;

/**
 * @author xieyizun
 * @date 18/5/2019 20:02
 * @description:
 */
public class HashTableDemo {
    public static void main(String[] args) {
        final Hashtable<String, String> hashTable = new Hashtable<String, String>();

        Thread thread1 = new Thread(new Runnable() {
            public void run() {
                hashTable.put("key1", "val1");
            }
        });

        Thread thread2 = new Thread(new Runnable() {
            public void run() {
                System.out.println(hashTable.get("key1"));
            }
        });

        thread1.start();
        thread2.start();

    }
}
