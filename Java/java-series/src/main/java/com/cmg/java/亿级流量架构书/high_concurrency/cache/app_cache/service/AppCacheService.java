package com.cmg.java.亿级流量架构书.high_concurrency.cache.app_cache.service;

public interface AppCacheService {

    String guavaHeapCache();
    String ehcacheHeapCache();
    String mapDBHeapCache();
    String ehcacheHeapOutsideCache();
    String mapDBHeapOutsideCache();
    String ehcacheDiskCache();
    String mapDBDiskCache();
}
