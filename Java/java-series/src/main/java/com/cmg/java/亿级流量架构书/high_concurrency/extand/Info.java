package com.cmg.java.亿级流量架构书.high_concurrency.extand;

public class Info {

    //cdn
    //多机房多活

    //分表分库
    //取模：编号哈希取模
    //分区：时间分区
    //路由表

    //跨库join
    //全剧表
    //es搜索等异构数据机制实现

    //sharding-jdbc
    //分库分表 弱事务 跨库join/分页/排序 柔性事务

    //数据异构：按照不同查询纬度建立表结构

    //分布式定时任务

    //cpu伪共享

    //disruptor+redis
    //mq队列+其他推送
    //等待队列  本地处理队列 失败队列
    //备份队列

    //event queue


    //disruptor
    //快的原因
    //无锁：更好的线程间交换信息的方式 cas cpu级别不牵涉操作系统
    //cas缺点 比单线程无锁使用时间多
    //单线程策略：没有竞争
    //多线程策略

    //缓存行填充 cpu伪共享优化是一个话题，缓存行塞满避免伪共享造成的冲突
    //内存屏障优化：



}
