package com.example.springboot17demo.service;

import org.apache.rocketmq.client.producer.SendCallback;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@ConditionalOnProperty(name = "rocketmq.enabled", havingValue = "true", matchIfMissing = false)
public class RocketMQProducerService {

    @Autowired
    private RocketMQTemplate rocketMQTemplate;

    @Autowired
    private ThreadPoolTaskExecutor messageProcessThreadPool;

    @Value("${rocketmq.topic}")
    private String topic;

    /**
     * 异步发送消息
     * @param message 消息内容
     */
    private void logThreadPoolStatus() {
        int activeCount = messageProcessThreadPool.getActiveCount();
        int poolSize = messageProcessThreadPool.getPoolSize();
        int corePoolSize = messageProcessThreadPool.getCorePoolSize();
        int maxPoolSize = messageProcessThreadPool.getMaxPoolSize();
        int queueSize = messageProcessThreadPool.getThreadPoolExecutor().getQueue().size();
        int remainingCapacity = messageProcessThreadPool.getThreadPoolExecutor().getQueue().remainingCapacity();
        
        // 只在线程池负载较高时才输出警告日志
        if (activeCount >= poolSize * 0.8 || queueSize >= remainingCapacity * 0.8) {
            log.warn("线程池负载较高 - 活跃线程数: {}, 当前线程池大小: {}, 核心线程数: {}, 最大线程数: {}, 队列大小: {}, 队列剩余容量: {}",
                    activeCount, poolSize, corePoolSize, maxPoolSize, queueSize, remainingCapacity);
        }
    }

    public void sendAsyncMessage(String message) {
        logThreadPoolStatus();
        messageProcessThreadPool.execute(() -> {
            try {
                rocketMQTemplate.asyncSend(topic, MessageBuilder.withPayload(message).build(), new SendCallback() {
                    @Override
                    public void onSuccess(SendResult sendResult) {
                        log.info("消息发送成功 - 消息ID: {}, 消息内容: {}", sendResult.getMsgId(), message);
                    }

                    @Override
                    public void onException(Throwable throwable) {
                        log.error("消息发送失败 - 消息内容: {}, 异常信息: {}", message, throwable.getMessage());
                    }
                });
            } catch (Exception e) {
                log.error("消息发送异常 - 消息内容: {}, 异常信息: {}", message, e.getMessage());
            }
        });
    }

    /**
     * 同步发送消息
     * @param message 消息内容
     * @return SendResult 发送结果
     */
    public SendResult sendSyncMessage(String message) {
        logThreadPoolStatus();
        try {
            SendResult sendResult = rocketMQTemplate.syncSend(topic, MessageBuilder.withPayload(message).build());
            log.info("同步消息发送成功 - 消息ID: {}, 消息内容: {}", sendResult.getMsgId(), message);
            return sendResult;
        } catch (Exception e) {
            log.error("同步消息发送失败 - 消息内容: {}, 异常信息: {}", message, e.getMessage());
            throw e;
        }
    }

    /**
     * 发送延迟消息
     * @param message 消息内容
     * @param delayLevel 延迟级别
     */
    public void sendDelayMessage(String message, int delayLevel) {
        logThreadPoolStatus();
        messageProcessThreadPool.execute(() -> {
            try {
                rocketMQTemplate.asyncSend(topic, MessageBuilder.withPayload(message).build(), new SendCallback() {
                    @Override
                    public void onSuccess(SendResult sendResult) {
                        log.info("延迟消息发送成功 - 消息ID: {}, 消息内容: {}, 延迟级别: {}", 
                                sendResult.getMsgId(), message, delayLevel);
                    }

                    @Override
                    public void onException(Throwable throwable) {
                        log.error("延迟消息发送失败 - 消息内容: {}, 延迟级别: {}, 异常信息: {}", 
                                message, delayLevel, throwable.getMessage());
                    }
                }, 3000, delayLevel);
            } catch (Exception e) {
                log.error("延迟消息发送异常 - 消息内容: {}, 延迟级别: {}, 异常信息: {}", 
                        message, delayLevel, e.getMessage());
            }
        });
    }
}