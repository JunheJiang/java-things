package com.ljp.concurr.chapter03;

public class StringBuilderTest {

    public static void main(String[] args){
        StringBuilder stringBuilder = new StringBuilder();

        stringBuilder.append("apple");
        stringBuilder.append("boy");
        stringBuilder.append("cat");
        stringBuilder.append("duck");

        System.out.println(stringBuilder);
    }
}
