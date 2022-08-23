package com.cmg.java.亿级流量架构书.high_concurrency.cache.app_cache.component;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class RedisCacheServiceImpl implements RedisCacheService {

    @Override
    public void set(String key, String jsonStr, int remoteCacheExpiresInSeconds) {

    }

    @Override
    public Map<String, String> mGet(List<String> partitionKeys) {
        return null;
    }
}
