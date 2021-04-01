package com.zcw.cblog.gateway.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.CorsWebFilter;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;

/**
 * @author Chrisz
 * @date 2020/10/21 - 21:50
 */
@Configuration
public class CblogCorsConfiguration {

    @Bean
    public CorsWebFilter corsWebFilter(){

        UrlBasedCorsConfigurationSource urlBasedCorsConfigurationSource = new UrlBasedCorsConfigurationSource();

        CorsConfiguration configuration = new CorsConfiguration();
        configuration.addAllowedHeader("*");
        configuration.addAllowedMethod("*");
        configuration.addAllowedOrigin("*");
        //coocike
        configuration.setAllowCredentials(true);

        urlBasedCorsConfigurationSource.registerCorsConfiguration("/**",configuration);
        return new CorsWebFilter(urlBasedCorsConfigurationSource);
    }
}
