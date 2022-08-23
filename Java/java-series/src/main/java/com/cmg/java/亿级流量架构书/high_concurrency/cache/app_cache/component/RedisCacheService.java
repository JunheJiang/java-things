package com.cmg.java.亿级流量架构书.high_concurrency.cache.app_cache.component;

import java.util.List;
import java.util.Map;

public interface RedisCacheService {
    void set(String key, String s, int remoteCacheExpiresInSeconds);

    Map<String, String> mGet(List<String> partitionKeys);
}
