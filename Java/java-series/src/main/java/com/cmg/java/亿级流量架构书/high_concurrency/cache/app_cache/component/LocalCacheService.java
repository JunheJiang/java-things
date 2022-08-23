package com.cmg.java.亿级流量架构书.high_concurrency.cache.app_cache.component;

import com.google.common.cache.Cache;

public interface LocalCacheService {

    void addCache(String key, Cache<?, ?> cache);

    Cache getLocalCache(String key);
}
