package com.yzxie.java.demo.charter5.part_5_4;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.ConcurrentSkipListMap;

/**
 * @author xieyizun
 * @date 19/5/2019 00:42
 * @description:
 */
public class ConcurrentSkipListMapDemo {
    public static void main(String[] args) {
        // 打印TreeMap
        System.out.println("===TreeMap===");
        Map treeMap = buildNormalSortedMap();
        printMapSortedKeyValue(treeMap);
        // 打印ConcurrentSkipListMap
        System.out.println("===ConcurrentSkipListMap===");
        Map concurrentSortedMap = buildConcurrentSortedMap();
        printMapSortedKeyValue(concurrentSortedMap);
    }
    // 创建TreeMap
    public static TreeMap<String, Object> buildNormalSortedMap() {
        TreeMap<String, Object> map = new TreeMap<>();
        initMap(map);
        return map;
    }
    // 创建ConcurrentSkipListMap
    public static ConcurrentSkipListMap<String, Object> buildConcurrentSortedMap() {
        ConcurrentSkipListMap<String, Object> map = new ConcurrentSkipListMap();
        initMap(map);
        return map;
    }
    private static void initMap(Map<String, Object> map) {
        List<String> unSortedKeys = Arrays.asList("1key", "3key", "2key", "4key");
        for (String key : unSortedKeys) {
            map.put(key, key);
        }
    }
    // 打印map的key，value
    public static void printMapSortedKeyValue(Map<String, Object> map) {
        for (Map.Entry entry : map.entrySet()) {
            System.out.println(entry.getKey() + ":" + entry.getValue());
        }
    }
}
