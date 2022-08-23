package com.cmg.java.亿级流量架构书.high_availlable.loadbalance;

import com.google.common.net.HostAndPort;
import com.orbitz.consul.AgentClient;
import com.orbitz.consul.Consul;
import com.orbitz.consul.model.agent.ImmutableRegistration;
import org.springframework.boot.SpringApplication;

/**
 * 动态负载均衡
 * 向nginx注册上游服务
 */
public class LoadBalanceRegisterToNginx {

    public static void main(String[] args) {
        SpringApplication.run(LoadBalanceRegisterToNginx.class, args);
        Consul consul = Consul.builder().withHostAndPort(HostAndPort.fromString("")).build();
        final AgentClient agentClient = consul.agentClient();
        String service = "";
        String address = "";
        String tag = "dev";
        int port = 9080;
        String serviceId = address + ":" + port;
        ImmutableRegistration.Builder builder = ImmutableRegistration.builder();
        builder.id(serviceId).name(service).address(address).port(port).addTags(tag);
        agentClient.register(builder.build());
        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() {
                agentClient.deregister(serviceId);
            }
        });

    }
}
