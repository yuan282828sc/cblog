package com.zcw.cblog.file;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;


@EnableRedisHttpSession
@MapperScan("com.zcw.cblog.file.dao")
@EnableFeignClients(basePackages = "com.zcw.cblog.file.feign")
@EnableDiscoveryClient
@SpringBootApplication
public class CblogFileApplication {

    public static void main(String[] args) {
        SpringApplication.run(CblogFileApplication.class, args);
    }

}
