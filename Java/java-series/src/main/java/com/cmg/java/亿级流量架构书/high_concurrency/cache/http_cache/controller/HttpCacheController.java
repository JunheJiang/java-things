package com.cmg.java.亿级流量架构书.high_concurrency.cache.http_cache.controller;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

@RestController
public class HttpCacheController {

    @GetMapping("http/cache")
    public ResponseEntity<String> modifiedCache(
            @RequestHeader(value = "If-Modified-Since", required = false)
                    Date ifModifiedSince) throws Exception {

        DateFormat gmtDateFormat = new SimpleDateFormat(
                "EEE,d MMM yyyy HH:mm:ss 'GMT'", Locale.US
        );
        //文档最后修改到时间
        //去掉毫秒值
        long lastModifiedMills = getLastModified() / 1000 * 1000;
        long now = System.currentTimeMillis() / 1000 * 1000;

        long maxAge = 20;

        if (ifModifiedSince != null &&
                ifModifiedSince.getTime() == lastModifiedMills) {
            MultiValueMap<String, String> headers =
                    new HttpHeaders();
            //当前时间
            headers.add("Date", gmtDateFormat.format(new Date(now)));
            //过期时间 http1.0支持
            headers.add("Expires",
                    gmtDateFormat.format(new Date((now + maxAge) * 1000)));
            //文档生存时间 http1.1支持
            headers.add("Cache-Control",
                    "max-age=" + maxAge);
            return new ResponseEntity<String>(headers,
                    HttpStatus.NOT_MODIFIED);
        }

        String body = "<a href=''>https://www.baidu.com</a>";
        MultiValueMap<String, String> headers =
                new HttpHeaders();
        //当前时间
        headers.add("Date", gmtDateFormat.format(new Date(now)));
        //过期时间 http1.0支持
        headers.add("Expires",
                gmtDateFormat.format(new Date((now + maxAge) * 1000)));
        //文档生存时间 http1.1支持
        headers.add("Cache-Control",
                "max-age=" + maxAge);
        //修改时间
        headers.add("Last-Modified", gmtDateFormat.format(new Date(lastModifiedMills)));
        return new ResponseEntity<String>(body, headers,
                HttpStatus.OK);
    }

    Cache<String, Long> lastModifiedCache =
            CacheBuilder.newBuilder()
                    .expireAfterWrite(10, TimeUnit.SECONDS)
                    .build();

    private long getLastModified() throws ExecutionException {
        return lastModifiedCache.get("lastModified", () -> {
            return System.currentTimeMillis();
        });
    }

    //内容变更验证
    @GetMapping("http/etag/cache")
    public ResponseEntity<String> eTagCache(
            @RequestHeader(value = "If-None-Match", required = false)
                    String ifNoneMatch) {
        long now = System.currentTimeMillis();
        long maxAge = 10;
        String body = "<a href=''>https://www.baidu.com</a>";
        //md5 body
        String etag = "W/\"" + (body) + "\"";
        if (StringUtils.equals(etag, ifNoneMatch)) {
            return new ResponseEntity<String>(HttpStatus.NOT_MODIFIED);
        }
        DateFormat gmtDateFormat = new SimpleDateFormat(
                "EEE,d MMM yyyy HH:mm:ss 'GMT'", Locale.US
        );
        MultiValueMap<String, String> headers =
                new HttpHeaders();
        headers.add("ETag", etag);
        //当前时间
        headers.add("Date", gmtDateFormat.format(new Date(now)));
        //文档生存时间 http1.1支持
        headers.add("Cache-Control",
                "max-age=" + maxAge);
        return new ResponseEntity<String>(body, headers,
                HttpStatus.OK);
    }
}
