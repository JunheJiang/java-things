package com.ljp.util;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;

public class ReaderUtils {
    public static String readFile(String filePathAndName){
        String result = "";
        try {
            result = FileUtils.readFileToString(new File(filePathAndName));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    public static void main(String[] args){

        String filePath = "D:\\2019_projects\\data-crawl\\file_folder" +
                "\\2019-07-30\\";
        File file = new File(filePath);
        if (!file.isDirectory()) {
            System.out.println("文件");
            System.out.println("path=" + file.getPath());
            System.out.println("absolutepath=" + file.getAbsolutePath());
            System.out.println("name=" + file.getName());

        } else if (file.isDirectory()) {
            System.out.println("文件夹");
            int i = 1;
            for (File tmpFile : file.listFiles()){
                String result = ReaderUtils.readFile(filePath + "\\"
                        + tmpFile.getName());
                try{
                    String universityName = result.substring(result
                            .indexOf("<div class=\"bg_sez\">") + 48, result
                            .indexOf("class=\"college_com leftMargin10\">" +
                                    "高校对比</a>") - 60);
                    String universityLevel = result.substring(result
                            .indexOf("<li>高校类型： ") + 10, result
                            .indexOf("<li>高校隶属于") - 10);
                    ;
                    // 去除换行符
                    universityName = universityName.replace("/r", "")
                            .replace("/n", "").trim();
                    universityLevel = universityLevel.replace("/r", "")
                            .replace("/n", "").trim();

                    System.out.println(i + " : " + universityName + " -- "
                            + universityLevel.replace("</li>", ""));
                }catch(Exception e){
                    System.out.println("提取信息时发生错误，导致该大学信息无法提取，" +
                            "编号为：" + i);
                }
                i++;
            }

        }

    }
}


