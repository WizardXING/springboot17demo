package com.example.springboot17demo.service;

import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RocketMQMessageListener(
    topic = "${rocketmq.topic}",
    consumerGroup = "${rocketmq.consumer.group}"
)
public class RocketMQConsumerService implements RocketMQListener<String> {

    @Autowired
    private ThreadPoolTaskExecutor messageProcessThreadPool;

    /**
     * 消息消费处理
     * @param message 接收到的消息内容
     */
    @Override
    public void onMessage(String message) {
        messageProcessThreadPool.execute(() -> {
            try {
                // 在这里添加实际的消息处理逻辑
                log.info("收到消息: {}", message);
                // TODO: 根据业务需求处理消息
                
                log.info("消息处理成功: {}", message);
            } catch (Exception e) {
                log.error("消息处理失败: {}, 异常信息: {}", message, e.getMessage());
            }
        });
    }
}