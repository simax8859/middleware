package com.ling.redisweb;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class RediswebApplication {

    public static void main(String[] args) {
        SpringApplication.run(RediswebApplication.class, args);
    }

}
