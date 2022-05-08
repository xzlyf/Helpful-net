package com.xz.helpful;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@EnableTransactionManagement
@EnableRedisHttpSession
@SpringBootApplication
@MapperScan(basePackages = "com.xz.helpful.dao")
public class HelpfulApplication {
    public static void main(String[] args) {
        SpringApplication.run(HelpfulApplication.class, args);
    }

}
