//package com.cmg.java.architecture.high_availlable.dowmGrading.component;
//
//import com.google.common.net.HostAndPort;
//import com.orbitz.consul.Consul;
//import com.orbitz.consul.ConsulException;
//import com.orbitz.consul.KeyValueClient;
//import com.orbitz.consul.model.kv.Value;
//import com.orbitz.consul.option.QueryOptions;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.core.io.ClassPathResource;
//import org.springframework.core.io.Resource;
//import org.springframework.core.io.support.PropertiesLoaderUtils;
//import org.springframework.stereotype.Component;
//
//import java.math.BigInteger;
//import java.nio.file.*;
//import java.util.List;
//import java.util.Objects;
//import java.util.Properties;
//import java.util.concurrent.atomic.AtomicBoolean;
//
//@Component
//@Slf4j
//public class DownGradingComponent {
//    static Properties properties;
//    static WatchService watchService;
//    static String fileName;
//    static Resource resource;
//
//    static {
//        try {
//            fileName = "application.properties";
//            resource = new ClassPathResource(fileName);
//            watchService = FileSystems.getDefault().newWatchService();
//            Paths.get(resource.getFile().getParent())
//                    .register(watchService, StandardWatchEventKinds.ENTRY_MODIFY,
//                            StandardWatchEventKinds.ENTRY_DELETE);
//            properties = PropertiesLoaderUtils.loadProperties(resource);
//        } catch (Exception e) {
//            log.error(e.getMessage());
//        }
//
//    }
//
//    /**
//     * 开关降级
//     * 开关设置
//     */
//    public void switchOfProperties() {
//        Thread watchThread = new Thread(() -> {
//            while (true) {
//                try {
//                    WatchKey watchKey =
//                            watchService.take();
//                    for (WatchEvent<?> event : watchKey.pollEvents()) {
//                        if (Objects.equals(event.context().toString(), fileName)) {
//                            properties = PropertiesLoaderUtils.loadProperties(resource);
//                            break;
//                        }
//                    }
//                    watchKey.reset();
//                } catch (Exception e) {
//                    log.error(e.getMessage());
//                }
//            }
//        });
//        watchThread.setDaemon(true);
//        watchThread.start();
//        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
//            try {
//                watchService.close();
//            } catch (Exception e) {
//                log.error(e.getMessage());
//            }
//        }
//        ));
//    }
//
//    private static transient Properties consulProperties = null;
//    private static transient String system = "content-tomcat";
//
//    static {
//        Consul consul = Consul.builder().
//                withHostAndPort(HostAndPort.fromString(""))
//                .withConnectTimeoutMillis(1000)
//                .withReadTimeoutMillis(30 * 1000)
//                .withWriteTimeoutMillis(5000).build();
//        final KeyValueClient keyValueClient = consul.keyValueClient();
//        final AtomicBoolean needBreak = new AtomicBoolean(true);
//        Thread thread = new Thread(() -> {
//            BigInteger index = BigInteger.ZERO;
//            while (true) {
//                Properties temp = new Properties();
//                try {
//                    List<Value> values = keyValueClient.getValues(system,
//                            QueryOptions.blockSeconds(30, index).build());
//                    for (Value value : values) {
//                        temp.put(value.getKey().substring(system.length() + 1),
//                                value.getValueAsString());
//                        index = index.max(BigInteger.valueOf(value.getCreateIndex()));
//                    }
//                    properties = temp;
//                } catch (ConsulException e) {
//                    log.error(e.getMessage());
//                    if (e.getCode() == 404) {
//                        try {
//                            Thread.sleep(5000l);
//                        } catch (Exception e1) {
//                            log.error(e1.getMessage());
//                        }
//                    }
//                }
//                if (needBreak.get() == true) {
//                    break;
//                }
//            }
//        });
//        thread.run();
//        needBreak.set(false);
//        thread.setDaemon(true);
//        thread.start();
//    }
//
//
//    /**
//     * 引入配置中心
//     */
//    public void consulSwitch() {
//
//    }
//
//}
