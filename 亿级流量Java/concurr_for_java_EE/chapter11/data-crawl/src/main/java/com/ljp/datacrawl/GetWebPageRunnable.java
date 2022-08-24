package com.ljp.datacrawl;

import com.ljp.util.WriterUtils;
import org.springframework.web.client.RestTemplate;

import java.io.UnsupportedEncodingException;

public class GetWebPageRunnable implements Runnable {
    // 使用RestTemplate进行Http访问
    RestTemplate restTemplate = new RestTemplate();

    private String url = "";

    private String saveFileName = "";

    public GetWebPageRunnable(String url, String fileName){
        this.url = url;
        this.saveFileName = fileName;
    }

    @Override
    public void run() {
        String result = restTemplate.getForObject(url, String.class);
        try {
            // 进行utf-8字符集转换
            result = new String(result.getBytes("utf-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        if (result != null) {
            // 将获取的数据写入文件
            WriterUtils.writeFile(saveFileName, ".txt", result);
        }
    }
}
