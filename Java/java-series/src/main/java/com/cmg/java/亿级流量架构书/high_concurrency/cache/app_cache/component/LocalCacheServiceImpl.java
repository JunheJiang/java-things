package com.cmg.java.亿级流量架构书.high_concurrency.cache.app_cache.component;

import com.google.common.cache.Cache;
import org.springframework.stereotype.Service;

@Service
public class LocalCacheServiceImpl implements LocalCacheService{
    @Override
    public void addCache(String key, Cache<?, ?> cache) {

    }

    @Override
    public Cache getLocalCache(String key) {
        return null;
    }
}
