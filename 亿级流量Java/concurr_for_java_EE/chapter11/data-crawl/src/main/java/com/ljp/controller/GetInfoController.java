package com.ljp.controller;

import com.ljp.datacrawl.GetWebPageRunnable;
import com.ljp.util.WriterUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/")
@CrossOrigin
public class GetInfoController {

    @Value("${base.university.info.url}")
    private String baseUniversityInfoURL;

    @GetMapping(value="/getUnivInfo")
    public void getUnivInfo(HttpServletRequest httpRequest){
        int startIndex = Integer.parseInt(
                httpRequest.getParameter("startIndex")
        );
        int endIndex = Integer.parseInt(httpRequest.getParameter("endIndex"));
        for (int i=startIndex; i<= endIndex; i++){
            GetWebPageRunnable getWebPageRunnable =
                    new GetWebPageRunnable(baseUniversityInfoURL + i,
                            "school_" + i);
            Thread getWebPageStarter = new Thread(getWebPageRunnable);
            getWebPageStarter.start();
            // 为避免一次发送请求过多，出现HTTP-429问题，所以加入一个短暂的时间间隔
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

}
