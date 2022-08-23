package com.cmg.java.亿级流量架构书.test;

import com.cmg.java.亿级流量架构书.App;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@Slf4j
@SpringBootTest(classes = App.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class CacheTest extends BaseTest {

    @Override
    public Object mockOne() {
        Object contentPush = new Object();
        return contentPush;
    }

    @Test
    @DisplayName("guava堆缓存")
    public void guavaHeapCache() throws Exception {
        get("/guava/heapCache");
    }

    @Test
    @DisplayName("ehCache堆缓存")
    public void ehcacheHeapCache() throws Exception {
        get("/ehCache/heapCache");
    }

    @Test
    @DisplayName("ehcache堆外缓存")
    public void ehcacheHeapOutsideCache() throws Exception {
        get("/ehCahce/outside/heapCache");
    }

    @Test
    @DisplayName("mapDB堆外缓存")
    public void mapDBHeapOutsideCache() throws Exception {
        get("/mapDB/outside/heapCache");
    }

    @Test
    @DisplayName("ehcache磁盘缓存")
    public void ehcacheDiskCache() throws Exception {
        get("/ehCahce/diskCache");
    }

    @Test
    @DisplayName("mapDB磁盘缓存")
    public void mapDBDiskCache() throws Exception {
        get("/mapDB/diskCache");
    }
}
