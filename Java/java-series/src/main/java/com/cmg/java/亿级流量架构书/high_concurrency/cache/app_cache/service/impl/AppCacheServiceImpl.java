package com.cmg.java.亿级流量架构书.high_concurrency.cache.app_cache.service.impl;

import com.cmg.java.亿级流量架构书.high_concurrency.cache.app_cache.service.AppCacheService;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import lombok.extern.slf4j.Slf4j;
import org.ehcache.CacheManager;
import org.ehcache.config.builders.*;
import org.ehcache.config.units.EntryUnit;
import org.ehcache.config.units.MemoryUnit;
import org.ehcache.impl.config.persistence.CacheManagerPersistenceConfiguration;
import org.mapdb.DB;
import org.mapdb.DBMaker;
import org.mapdb.HTreeMap;
import org.springframework.stereotype.Service;

import java.io.File;
import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.concurrent.TimeUnit;

@Service
@Slf4j
public class AppCacheServiceImpl implements AppCacheService {

    // 软引用 堆内存不足时回收这些对象
    // 弱引用 回收被发现立刻回收 更短的生命周期

    //堆缓存：没有序列和反序列化 最快 GC暂停时间会变长

    //堆外缓存 有序列化和反序列化问题 GC扫描和移动的对象变少了 减少GC暂停时间
    //可以支持更大的缓存空间
    public String guavaHeapCache() {
        //不会在缓存数据失效时立即触发回收操作 put时会主动进行一次缓存清理
        Cache<String, String> cache = CacheBuilder.newBuilder().
                concurrencyLevel(4).
                //定期回收缓存数据 没有写就过期了
                        expireAfterWrite(10, TimeUnit.SECONDS)
                .maximumSize(10000)
                .build();
        cache.put("guavaHeapCache", "guavaHeapCache");
        return cache.getIfPresent("guavaHeapCache");
    }

    //guava cache 只提供堆缓存
    //ehcache 堆缓存 堆外缓存 磁盘缓存 分布式缓存
    //mapdb java数据类型 支持acid事务 增量备份 堆 堆外 磁盘

    //TTL 存活期 到点必死
    //TTI 空闲期 有人碰则活

    @Override
    public String ehcacheHeapCache() {
        CacheManager cacheManager = CacheManagerBuilder.newCacheManagerBuilder().build(true);
        CacheConfigurationBuilder<String, String> cacheConfig =
                CacheConfigurationBuilder.newCacheConfigurationBuilder(
                        String.class, String.class, ResourcePoolsBuilder.newResourcePoolsBuilder()
                                .heap(100, EntryUnit.ENTRIES))
                        .withDispatcherConcurrency(4)
                        .withExpiry(ExpiryPolicyBuilder.timeToLiveExpiration(
                                Duration.of(10, ChronoUnit.SECONDS)));
        org.ehcache.Cache<String, String> ehCache = cacheManager.createCache("ehcacheHeapCache", cacheConfig);
        ehCache.put("ehcacheHeapCache", "ehcacheHeapCache");
        return ehCache.get("ehcacheHeapCache");
    }

    @Override
    public String mapDBHeapCache() {
        HTreeMap cache = DBMaker.heapDB().
                concurrencyScale(16)
                .make().hashMap("mapDBHeapCache")
                .expireMaxSize(10000)
                .expireAfterCreate(10, TimeUnit.SECONDS)
                .expireAfterUpdate(10, TimeUnit.SECONDS)
                .expireAfterGet(10, TimeUnit.SECONDS)
                .create();
        //支持线程池定期清理缓存
        //触发失效
        //memoryDB
        cache.put("mapDBHeapCache", "mapDBHeapCache");
        return (String) cache.get("mapDBHeapCache");
    }

    @Override
    public String ehcacheHeapOutsideCache() {
        //堆外缓存不支持key容量的缓存过期策略
        CacheManager cacheManager = CacheManagerBuilder.newCacheManagerBuilder().build(true);
        CacheConfigurationBuilder<String, String> cacheConfig =
                CacheConfigurationBuilder.newCacheConfigurationBuilder(
                        String.class, String.class, ResourcePoolsBuilder.newResourcePoolsBuilder()
                                .offheap(100, MemoryUnit.MB))
                        .withDispatcherConcurrency(4)
                        .withExpiry(ExpiryPolicyBuilder.timeToLiveExpiration(
                                Duration.of(10, ChronoUnit.SECONDS)))
                        //
                        .withSizeOfMaxObjectGraph(3)
                        //
                        .withSizeOfMaxObjectSize(1, MemoryUnit.KB);
        org.ehcache.Cache<String, String> ehCache = cacheManager.createCache("ehcacheHeapOutsideCache", cacheConfig);
        ehCache.put("ehcacheHeapOutsideCache", "ehcacheHeapOutsideCache");
        return ehCache.get("ehcacheHeapOutsideCache");
    }

    @Override
    public String mapDBHeapOutsideCache() {
        //记得添加启动参数 -XX:MaxDirectMemorySize=10G
        HTreeMap cache = DBMaker.memoryDirectDB()
                .concurrencyScale(16)
                .make().hashMap("mapDBHeapOutsideCache")
                .expireStoreSize(64 * 1024 * 1024)
                .expireMaxSize(10000)
                .expireAfterCreate(10, TimeUnit.SECONDS)
                .expireAfterUpdate(10, TimeUnit.SECONDS)
                .expireAfterGet(10, TimeUnit.SECONDS)
                .create();
        //支持线程池定期清理缓存
        //触发失效
        //memoryDB
        cache.put("mapDBHeapOutsideCache", "mapDBHeapOutsideCache");
        return (String) cache.get("mapDBHeapOutsideCache");
    }

    @Override
    public String ehcacheDiskCache() {
        CacheManager cacheManager = CacheManagerBuilder.newCacheManagerBuilder()
                .using(PooledExecutionServiceConfigurationBuilder
                        .newPooledExecutionServiceConfigurationBuilder()
                        .defaultPool("default", 1, 10).build())
                .with(new CacheManagerPersistenceConfiguration(new File("/Users/jiangjunhe/Downloads/bak")))
                .build(true);
        CacheConfigurationBuilder<String, String> cacheConfig =
                CacheConfigurationBuilder.newCacheConfigurationBuilder(
                        String.class, String.class, ResourcePoolsBuilder.newResourcePoolsBuilder()
                                .disk(100, MemoryUnit.MB, true))
                        .withDiskStoreThreadPool("default", 5)
                        .withExpiry(ExpiryPolicyBuilder.timeToLiveExpiration(
                                Duration.of(50, ChronoUnit.SECONDS)))
                        .withSizeOfMaxObjectGraph(3)
                        .withSizeOfMaxObjectSize(1, MemoryUnit.KB);
        org.ehcache.Cache<String, String> ehCache = cacheManager.createCache("ehcacheDiskCache", cacheConfig);
        ehCache.put("ehcacheDiskCache", "ehcacheDiskCache");
        return ehCache.get("ehcacheDiskCache");
    }

    @Override
    public String mapDBDiskCache() {
        DB db = DBMaker.fileDB("/Users/jiangjunhe/Downloads/mapDB.data")
                .fileMmapEnable()
                .fileMmapEnableIfSupported()
                .fileMmapPreclearDisable()
                .cleanerHackEnable()
                .transactionEnable()
                .closeOnJvmShutdown()
                .concurrencyScale(16).make();

        HTreeMap cache = db.hashMap("mapDBDiskCache")
//                .expireStoreSize(64 * 1024 * 1024)
                .expireMaxSize(10000)
                .expireAfterCreate(10, TimeUnit.SECONDS)
                .expireAfterUpdate(10, TimeUnit.SECONDS)
                .expireAfterGet(10, TimeUnit.SECONDS)
                .createOrOpen();
        //支持线程池定期清理缓存
        //触发失效
        //memoryDB
        cache.put("mapDBDiskCache", "mapDBDiskCache");
        return (String) cache.get("mapDBDiskCache");
    }

    public String mpDB2LevelCache() {
        DB db = DBMaker.fileDB("/Users/jiangjunhe/Downloads/mapDB.data")
                .fileMmapEnable()
                .fileMmapEnableIfSupported()
                .fileMmapPreclearDisable()
                .cleanerHackEnable()
                .transactionEnable()
                .closeOnJvmShutdown()
                .concurrencyScale(16).make();

        HTreeMap mapDBDiskCache = db.hashMap("mapDBDiskCache")
//                .expireStoreSize(64 * 1024 * 1024)
                .expireMaxSize(10000)
                .expireAfterCreate(10, TimeUnit.SECONDS)
                .expireAfterUpdate(10, TimeUnit.SECONDS)
                .expireAfterGet(10, TimeUnit.SECONDS)
                .createOrOpen();

        HTreeMap mapDBHeapCache = DBMaker.heapDB()
                .concurrencyScale(16)
                .make().hashMap("mapDBHeapCache")
                .expireStoreSize(64 * 1024 * 1024)
                .expireMaxSize(10000)
                .expireAfterCreate(10, TimeUnit.SECONDS)
                .expireAfterUpdate(10, TimeUnit.SECONDS)
                .expireAfterGet(10, TimeUnit.SECONDS)
                //溢出转存disk
                .expireOverflow(mapDBDiskCache)
                .createOrOpen();
        mapDBHeapCache.put("mpDB2LevelCache", "mpDB2LevelCache");
        return (String) mapDBHeapCache.get("mpDB2LevelCache");
    }
    //Cache—Aside

    //Cache-As-Sor
}
