package com.cmg.java.Java算法与数据结构书.chapter2.recursion;

import java.util.HashMap;
import java.util.Random;

/**
 * tell age group by age in more than 100 million people
 * <p>
 * that means million concurrency access
 */
public class LargeScaleDataTell {

    //space for time
    // 1-6 child
    // 6-18 teenager
    // 19-35 young
    // 36-50 middle age
    // 51-80 old

    final static HashMap<Integer, String> map = new HashMap();
    final static Random random = new Random();
    static int children = 0;
    static int teenager = 0;
    static int young = 0;
    static int middleAge = 0;
    static int old = 0;
    static int count = 0;

    public static void main(String[] args) {

        int[] arr = new int[100000000];
        for (int i = 0; i < arr.length; i++) {
            arr[i] = random.nextInt(81);
        }

        for (int n = 0; n < 1000000; n++) {
            int i = random.nextInt(81);
            int j = random.nextInt(81);
            int temp = arr[i];
            arr[i] = arr[j];
            arr[j] = temp;
        }

        for (int i = 1; i <= 80; i++) {
            if (0 < i && i < 7) {
                map.put(i, "children");
            } else if (6 < i && i < 19) {
                map.put(i, "teenager");
            } else if (18 < i && i < 36) {
                map.put(i, "young");
            } else if (35 < i && i < 51) {
                map.put(i, "middleAge");
            } else if (50 < i && i < 81) {
                map.put(i, "old");
            }
        }

        long start = System.nanoTime();
        //50345 22744 22400 22818 25588 22918
//        tellAge(arr);
        //27467 33142 29150 29348 78450 30174 32797
        distinguishAge(arr);
        //169524631
//        mehtod3(arr);

//        System.out.println("children:" + children);
//        System.out.println("teenager:" + teenager);
//        System.out.println("young:" + young);
//        System.out.println("middleAge:" + middleAge);
//        System.out.println("old:" + old);

//
        System.out.println("time:" + String.valueOf(System.nanoTime() - start));

    }

    static String tellAge(int[] arr) {
        for (int i = 0; i < arr.length; i++) {
            return ageGroup(arr[i]);
        }
        return "";
    }

    static String distinguishAge(int[] arr) {
        for (int i = 0; i < arr.length; i++) {
            if (map.containsKey(arr[i])) {
                return map.get(arr[i]);
            }
        }
        return "";
    }

    static String mehtod3(int[] arr) {
        count++;
        int[] newArr = new int[arr.length];
        for (int i = 0; i < arr.length; i++) {
            newArr[arr[i]]++;
            if (newArr[arr[i]] >= 2) {
                return ageGroup(arr[i]);
            }
        }
        return "";
    }

    static String ageGroup(int age) {
        if (0 < age && age < 7) {
            return ("children");
//                children ++;
        } else if (6 < age && age < 19) {
            return ("teenager");
//                teenager++;
        } else if (18 < age && age < 36) {
//                young++;
            return ("young");
        } else if (35 < age && age < 51) {
//                middleAge++;
            return ("middleAge");
        } else if (50 < age && age < 81) {
//                old++;
            return ("old");
        }
        return "";
    }

}
