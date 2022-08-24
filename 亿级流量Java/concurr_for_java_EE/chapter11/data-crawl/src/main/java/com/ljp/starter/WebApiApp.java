package com.ljp.starter;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.web.servlet.MultipartConfigFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;

import javax.servlet.MultipartConfigElement;


@SpringBootApplication
@EntityScan(basePackages = {"com.ljp.*.entity"})
@ComponentScan(basePackages = {"com.ljp.*"})
@EnableAutoConfiguration(exclude={DataSourceAutoConfiguration.class})
public class WebApiApp {
	public static void main(String[] args) {
		ApplicationContext ctx = SpringApplication.run(WebApiApp.class, args);
	}
}