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
        // 华为云ECS服务器
//        String ACCESS_IP = "124.70.48.240";
        // 华为云Flexus服务器
        String ACCESS_IP = "120.46.139.40";
        registry.addMapping("/**")
//                .allowedOrigins("http://" + ACCESS_IP) // 设置允许的前端URL
                .allowedOrigins("http://localhost:8080")
//                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                .allowedHeaders("*")
                .allowCredentials(true)
                .maxAge(9600);
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/image/**").addResourceLocations("file:D:/files/");
//        registry.addResourceHandler("/image/**") .addResourceLocations("file:/home/server/XiangCuHui/java/files/");
    }
}

