###微服务

####组成与特性
- 服务框架
  - RPC 
  - REST
- 运行时基础支撑服务
  - 服务发现治理
    - nacos
  - 服务配置中心
    - nacos
    - apollo
  - 服务网关
    - gateway
    - kong
  - 服务接入层
    - nginx
  - 负载均衡
    - nginx
    - ribbon
  
- 服务安全
  - Oauth授权
  - jwt认证
  - IAM
  
- 后台服务  
  - 消息系统
  - 分布式数据访问层
  - 任务调度
  - 缓存管理
  
- 服务容错
  - 超时
  - 熔断
  - 隔离
  - 限流
    - Sentinel
    - Hystrix
  - 降级
  

- 服务监控
  - 日志监控
  - 调用链监控
    - Dapper google 
      https://research.google/pubs/archive/36356.pdf
      - trace 一次分布式调用链路踪迹
      - span 方法调用踪迹
      - annotation 标注
      - sampling 采样率
    - 历史
      - 2002 ebay cal （参考1）
      - 2010 Dapper google （参考2）
      - 2011 点评 cat 丰富报表
      - 2012 zipkin（参考Dapper）
      - 2012 cTrace（参考 zipkin ）
      - 2012 pinpoint 参考 zipkin ） 非侵入式
      - 2014 京东 Hydra 阿里 eagleye
      - openTracing SkyWalking 借鉴pinpoint非侵入 开源综合
      - jaeger go语言 zipkin升级版
    - CAT
      - 报表丰富 
        - 报错大盘
        - 业务大盘
    - Sleuth
    - Zipkin
  - Metrics监控
    - Prometheus
    - KairosDB
  - 告警通知
    - zalando zMon
  - 健康检查
  - 物理监控
    - 系统层 物理 虚拟机 zabbix
    - 基础设施
  

- 服务部署平台
  - 发布机制
    - 蓝绿
    - 金丝雀
    - 灰度
  - 资源/容器调度机制
    - 集群资源 galaxy+mesos
    - 资源治理 atlas
  - 发布系统
    - stargate
  - 镜像治理
    - dockyard
    - harbor
  - 租户资源管理
  - 发布流水线
