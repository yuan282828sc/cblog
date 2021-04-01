package com.zcw.cblog.article;


import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;


@EnableRedisHttpSession
@MapperScan("com.zcw.cblog.article.dao")
@EnableFeignClients(basePackages ="com.zcw.cblog.article.feign")
@EnableDiscoveryClient
@SpringBootApplication
public class CblogArticleApplication {

    public static void main(String[] args) {
        SpringApplication.run(CblogArticleApplication.class, args);
    }

}
