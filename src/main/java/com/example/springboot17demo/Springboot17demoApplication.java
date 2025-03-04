package com.example.springboot17demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import lombok.extern.slf4j.Slf4j;

@SpringBootApplication
@Slf4j
public class Springboot17demoApplication {

    public static void main(String[] args) {
        SpringApplication.run(Springboot17demoApplication.class, args);
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            log.info("应用程序已停止运行");
        }));
    }

}