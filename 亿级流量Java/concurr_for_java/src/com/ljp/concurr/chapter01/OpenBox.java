package com.ljp.concurr.chapter01;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class OpenBox {
    public static void main(String[] args) {
        // 设置宝箱中可能包含的水果
        List<String> fruits = new ArrayList<String>();

        fruits.add("green apple");
        fruits.add("red apple");
        fruits.add("banana");
        fruits.add("cherry");
        fruits.add("watermelon");

        // 获取随机的下标，用于生成随机的水果，范围在：0 ~ 最大水果连表的下标
        Random randomUtil = new Random();
        int randomInt = randomUtil.nextInt(fruits.size());

        System.out.println("打开宝箱，得到了" + fruits.get(randomInt) + "!");

    }
}
