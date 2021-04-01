package com.zcw.cblog.search;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;


@EnableRedisHttpSession
@MapperScan("com.zcw.cblog.search.dao")
@EnableFeignClients(basePackages = "com.zcw.cblog.search.feign")
@EnableDiscoveryClient
@SpringBootApplication
public class CblogSearchApplication {

    public static void main(String[] args) {
        SpringApplication.run(CblogSearchApplication.class, args);
    }

}
