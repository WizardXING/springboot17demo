package com.example.springboot17demo.config;

import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RocketMQConfig {

    @Bean
    @ConditionalOnProperty(name = "rocketmq.enabled", havingValue = "true", matchIfMissing = false)
    public RocketMQTemplate rocketMQTemplate() {
        return new RocketMQTemplate();
    }
}