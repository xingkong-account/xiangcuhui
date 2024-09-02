package com.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    // 跨域请求的配置
    public void addCorsMappings(CorsRegistry registry) {
        String ACCESS_PORT = "8080";
        String ACCESS_IP = "localhost";
        registry.addMapping("/**")
                .allowedOrigins("http://" + ACCESS_IP + ":" + ACCESS_PORT) // 设置允许的前端URL
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                .allowedHeaders("*")
                .allowCredentials(true)
                .maxAge(9600);
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/image/**").addResourceLocations("file:D:/files/");
    }
}

