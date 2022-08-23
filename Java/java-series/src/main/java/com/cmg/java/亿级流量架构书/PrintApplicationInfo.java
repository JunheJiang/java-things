//package com.cmg.java.architecture;
//
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.context.ConfigurableApplicationContext;
//import org.springframework.core.env.ConfigurableEnvironment;
//import org.springframework.core.io.ClassPathResource;
//import org.springframework.util.FileCopyUtils;
//
//import java.io.IOException;
//import java.nio.charset.StandardCharsets;
//
//
//@Slf4j
//public class PrintApplicationInfo {
//
//    public PrintApplicationInfo() {
//    }
//
//    public static void printTip(ConfigurableApplicationContext context) {
//        ConfigurableEnvironment environment = context.getEnvironment();
//        String profileActive = environment.getProperty("spring.profiles.active");
//        StringBuffer tip = new StringBuffer();
//        tip.append("===========================================================================================\n");
//        tip.append("                                                                                  \n");
//        tip.append("===========================================================================================\n");
//        if ("dev".equals(profileActive)) {
//            log.info(tip.toString());
//        }
//
//    }
//
//    public static void print(ConfigurableApplicationContext context) {
//        ConfigurableEnvironment environment = context.getEnvironment();
//        String projectFinalName = environment.getProperty("info.project-finalName");
//        String projectVersion = environment.getProperty("info.project-version");
//        String profileActive = environment.getProperty("spring.profiles.active");
//        String contextPath = environment.getProperty("server.servlet.context-path");
//        String serverIp = environment.getProperty("fuse-boot.server-ip");
//        String port = environment.getProperty("server.port");
//        String springBootAdminServerUrl = environment.getProperty("spring.boot.admin.client.url");
//        log.info("projectFinalName : {}", projectFinalName);
//        log.info("projectVersion : {}", projectVersion);
//        log.info("profileActive : {}", profileActive);
//        log.info("contextPath : {}", contextPath);
//        log.info("serverIp : {}", serverIp);
//        log.info("port : {}", port);
//        String startSuccess = "Start Success";
//
//        try {
//            ClassPathResource cpr = new ClassPathResource("config/success.txt");
//            byte[] bytes = FileCopyUtils.copyToByteArray(cpr.getInputStream());
//            startSuccess = new String(bytes, StandardCharsets.UTF_8);
//        } catch (IOException var12) {
//            log.error(var12.getMessage());
//        }
//
//        String knife4jUrl = "http://" + serverIp + ":" + port + contextPath + "/doc.html";
//        log.info("\n{}", startSuccess);
//        log.info("api doc: {}", knife4jUrl);
//    }
//}
