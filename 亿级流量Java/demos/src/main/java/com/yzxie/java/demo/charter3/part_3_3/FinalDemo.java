package com.yzxie.java.demo.charter3.part_3_3;

/**
 * @author xieyizun
 * @date 16/6/2019 18:07
 * @description:
 */
public class FinalDemo {
    public static void main(String[] args) {
        String str = "hello";
        str = "world";

        final String str2 = "cann't change";
        // 编译报错
        //str2 = "change";
    }
}
