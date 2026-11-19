package com.example.intangibleheritagedemo;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.example.intangibleheritagedemo.mapper")
public class IntangibleHeritageDemoApplication {
    public static void main(String[] args) {
        SpringApplication.run(IntangibleHeritageDemoApplication.class, args);
    }
}
