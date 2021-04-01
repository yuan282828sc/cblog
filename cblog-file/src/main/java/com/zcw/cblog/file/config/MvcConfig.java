package com.zcw.cblog.file.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @Description TODO:配置mvc
 */
@Configuration
public class MvcConfig implements WebMvcConfigurer {
    /**
     * SpringMVC提供的页面视图跳转 视图映射
     */
    @Override
    public void addViewControllers(ViewControllerRegistry registry){
        //写了这个映射不能实现自定义的业务逻辑，所以得看情况
        registry.addViewController("/").setViewName("index");
        registry.addViewController("/index").setViewName("index");
        registry.addViewController("/blog/detail").setViewName("blog/detail");
        registry.addViewController("/blog/index").setViewName("blog/index");
        registry.addViewController("/user/login").setViewName("login");
        registry.addViewController("/user/reg").setViewName("user/reg");
        registry.addViewController("/user/forget").setViewName("user/forget");

    }
    //添加资源处理器  便于网页访问本地资源
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/img/**").addResourceLocations("file:D:/project/communityFile/");
    }
}
