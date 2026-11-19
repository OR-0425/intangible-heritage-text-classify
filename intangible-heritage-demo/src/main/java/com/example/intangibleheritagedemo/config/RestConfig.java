package com.example.intangibleheritagedemo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

// @Configuration 代表这是配置类，Spring启动会自动扫描这里
@Configuration
public class RestConfig {

    // @Bean：把这个方法返回的对象交给Spring容器【注册管理】
    @Bean
    public RestTemplate restTemplate(){
        // 创建对象，交给Spring，之后我们直接@Resource拿来用
        return new RestTemplate();
    }
}
