package com.cmg.java.亿级流量架构书.high_concurrency.cache.app_cache.component;

import com.alibaba.fastjson.JSONObject;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.task.AsyncTaskExecutor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

@Component
@Slf4j
public class CacheNoun_MultiLevelCache implements InitializingBean {
    @Autowired
    LocalCacheService localCacheService;
    @Autowired
    RedisCacheService redisCacheService;

    @Override
    public void afterPropertiesSet() throws Exception {
        //本地缓存初始化
        Cache<String, String> cache = CacheBuilder.newBuilder()
                .softValues()
                .maximumSize(1000000)
                .expireAfterWrite(10000, TimeUnit.SECONDS)
                .build();
        addCache("ct_cache", cache);
    }

    private void addCache(String key, Cache<?, ?> cache) {
        localCacheService.addCache(key, cache);
    }

    //写缓存
    //先写本地缓存 异步写分布式缓存

    boolean writeToLocalCache;
    boolean writeToRemoteCache;
    AsyncTaskExecutor asyncTaskExecutor;

    public void writeCache(String key, final Object value, final int remoteCacheExpiresInSeconds)
            throws RuntimeException {
        if (value == null) {
            return;
        }
        //复制对象问题
        //本地缓存是引用 分布式缓存需要序列化
        final Object finalValue = copy(value);
        if (writeToLocalCache) {
            Cache localCache = localCacheService.getLocalCache(key);
            if (localCache != null) {
                localCache.put(key, value);
            }
        }
        if (!writeToRemoteCache) {
            return;
        }
        asyncTaskExecutor.execute(() -> {
            try {
                redisCacheService.set(key, JSONObject.toJSONString(value), remoteCacheExpiresInSeconds);
            } catch (Exception e) {
                log.error(e.getMessage());
            }
        });
    }

    private Object copy(Object value) {
        return null;
    }

    boolean readLocalCache;
    boolean readRemoteCache;

    //读缓存
    //先读本地缓存 分区批量查询
    private Map innerMGet(List<String> keys, List<Class> types) throws Exception {
        Map<String, Object> result = Maps.newHashMap();
        List<String> missKeys = Lists.newArrayList();
        List<Class> missTypes = Lists.newArrayList();
        if (readLocalCache) {
            for (int i = 0; i < keys.size(); i++) {
                String key = keys.get(i);
                Class type = types.get(i);
                Cache localCache = localCacheService.getLocalCache(key);
                if (localCache != null) {
                    Object value = localCache.getIfPresent(key);
                    result.put(key, value);
                    if (value == null) {
                        missKeys.add(key);
                        missTypes.add(type);
                    }
                } else {
                    missKeys.add(key);
                    missTypes.add(type);
                }
            }
        }
        if (!readRemoteCache) {
            return result;
        }

        final Map<String, String> missResult = Maps.newHashMap();
        final List<List<String>> keysPage = Lists.partition(missKeys, 10);
        List<Future<Map<String, String>>> futuresPage = Lists.newArrayList();

        try {
            for (final List<String> partitionKeys : keysPage) {
                futuresPage.add(asyncTaskExecutor.submit(
                        () -> redisCacheService.mGet(partitionKeys)
                ));
            }
            for (Future<Map<String, String>> future : futuresPage) {
                missResult.putAll(future.get(300, TimeUnit.MICROSECONDS));
            }
        } catch (Exception e) {
            futuresPage.forEach(future -> future.cancel(true));
            log.error(e.getMessage());
            throw e;
        }
        //合并result和missResult
        result.putAll(missResult);
        return result;
    }

    //强制获取最新数据

    //null与""
    //读库为null时 写入空字符到缓存
    //读去缓存为空字符串时 返回null

    boolean failed;
    boolean succeed;

    //失败统计
    private void failureStatics(String key) {
        LoadingCache<String, AtomicInteger> failedCache =
                CacheBuilder.newBuilder()
                        .softValues()
                        .maximumSize(10000)
                        .build(new CacheLoader<String, AtomicInteger>() {
                            @Override
                            public AtomicInteger load(String s) throws Exception {
                                return new AtomicInteger(0);
                            }
                        });
        if (failed) {
            failedCache.getUnchecked(key).incrementAndGet();
        }
        if (succeed) {
            failedCache.invalidate(key);
        }
    }

    RedisTemplate redisTemplate;

    //延迟告警
    private void delayAlarm(String key) throws ExecutionException {
        LoadingCache<String, Integer> alarmCache =
                CacheBuilder.newBuilder()
                        .softValues()
                        .maximumSize(10000)
                        .expireAfterAccess(1, TimeUnit.HOURS)
                        .build(new CacheLoader<String, Integer>() {
                            @Override
                            public Integer load(String s) throws Exception {
                                return 0;
                            }
                        });
        //失败次数 延迟告警
        Integer count = 0;
        if (redisTemplate != null) {
            String countStr = (String) redisTemplate.opsForValue().get(key);
            count = Integer.valueOf(countStr);
        } else {
            count = alarmCache.get(key);
        }
        if (count % 5 == 0) {
            //5次告警一次
        }
        if (redisTemplate != null) {
            redisTemplate.opsForValue().set(key, String.valueOf(count),
                    1, TimeUnit.HOURS);
        } else {
            alarmCache.put(key, count);
        }
    }

    //缓存使用模式
    //cache-aside
    //业务代码围绕缓存写 业务直接维护缓存
    //读场景：未命中 回源sor读取数据 写入缓存
    //写场景：先写入sor 成功后同步写入缓存
    //可能存在并发更新情况

    //cache-as-sor
    //把缓存当作主库操作 在委托sor进行真实到读写操作

    //read-through 未命中直接回源sor
    //guava LoadingCache CacheLoader
    //dog-pile effect
    //get(key callable)
    //ehCache CacheLoaderWriter

    //write-through
    //cacheWriter组件操作 loaderWriter

    //write-behind
    //回写模式
    //异步写
    //批量写


}
