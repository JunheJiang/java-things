package com.ljp.util;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.Date;

public class WriterUtils {
    public static boolean writeFile(String fileName, String expandedName, String content) {

        // 文件的保存文件夹
        String fileDir = "file_folder";
        File dir = new File(fileDir);
        // 若文件夹不存在则创建
        if (!dir.exists()) {
            dir.mkdirs();
        }

        String dateStr = DateUtils
                .formatDateToString(new Date(), DateUtils.DATE_FORMAT_YMD);
        String fileDateDir = fileDir + File.separator + dateStr;
        File dateDir = new File(fileDateDir);
        if (!dateDir.exists()) {
            dateDir.mkdirs();
        }

        String dateStr1 = DateUtils
                .formatDateToString(new Date(), "yyyyMMddHHmmssSSS");
        String name = fileDateDir + File.separator + fileName + "_" + dateStr1
                + expandedName;

        try {
            // 按utf-8编码写入文件
            FileUtils.writeStringToFile(new File(name), content, "utf-8");
            return true;
        } catch (IOException e) {
            return false;
        }

    }
}
