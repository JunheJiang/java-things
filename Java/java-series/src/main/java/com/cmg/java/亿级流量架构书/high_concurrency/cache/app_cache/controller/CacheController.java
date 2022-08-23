package com.cmg.java.亿级流量架构书.high_concurrency.cache.app_cache.controller;

import com.cmg.java.亿级流量架构书.high_concurrency.cache.app_cache.service.AppCacheService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
public class CacheController {
    @Autowired
    AppCacheService appCacheService;

    @GetMapping("/guava/heapCache")
    public void guavaHeapCache() {
        String str = appCacheService.guavaHeapCache();
        log.info("guavaHeapCache::" + str);
    }


    @GetMapping("/ehCache/heapCache")
    public void ehcacheHeapCache() {
        String str = appCacheService.ehcacheHeapCache();
        log.info("ehcacheHeapCache::" + str);
    }

    @GetMapping("/mapDB/heapCache")
    public void mapDBHeapCache() {
        String str = appCacheService.mapDBHeapCache();
        log.info("mapDBHeapCache::" + str);
    }

    @GetMapping("/ehCahce/outside/heapCache")
    public void ehcacheHeapOutsideCache() {
        String str = appCacheService.ehcacheHeapOutsideCache();
        log.info("ehcacheHeapOutsideCache::" + str);
    }

    @GetMapping("/mapDB/outside/heapCache")
    public void mapDBHeapOutsideCache() {
        String str = appCacheService.mapDBHeapOutsideCache();
        log.info("mapDBHeapOutsideCache::" + str);
    }

    @GetMapping("/ehCahce/diskCache")
    public void ehcacheDiskCache() {
        String str = appCacheService.ehcacheDiskCache();
        log.info("ehcacheDiskCache::" + str);
    }

    @GetMapping("/mapDB/diskCache")
    public void mapDBDiskCache() {
        String str = appCacheService.mapDBDiskCache();
        log.info("mapDBDiskCache::" + str);
    }
}
