package com.example.springboot17demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/thread-pool")
public class ThreadPoolController {

    @Autowired
    private ThreadPoolTaskExecutor messageProcessThreadPool;

    @GetMapping("/status")
    public Map<String, Object> getThreadPoolStatus() {
        Map<String, Object> status = new HashMap<>();
        status.put("activeCount", messageProcessThreadPool.getActiveCount());
        status.put("corePoolSize", messageProcessThreadPool.getCorePoolSize());
        status.put("maxPoolSize", messageProcessThreadPool.getMaxPoolSize());
        status.put("poolSize", messageProcessThreadPool.getPoolSize());
        status.put("queueSize", messageProcessThreadPool.getQueueSize());
        return status;
    }
}