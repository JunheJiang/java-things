package com.cmg.java.亿级流量架构书;

import com.cmg.java.亿级流量架构书.high_availlable.isolation.HystrixMetricsStreamServlet;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;

@SpringBootApplication(scanBasePackages = {"com.cmg.java.亿级流量架构书"})
@Slf4j
public class App {

    public static void main(String[] args) {
        SpringApplication.run(App.class, args);
//        ConfigurableApplicationContext context = SpringApplication.run(App.class,args);
//        PrintApplicationInfo.print(context);
    }

    @Bean
    public ServletRegistrationBean servletRegistrationBean(){
        return  new ServletRegistrationBean(new HystrixMetricsStreamServlet(),
                "/hystrix.stream");
    }
}
