package com.example.springboot17demo.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/rocketmq")
@Slf4j
public class RocketMQController {

    private final ApplicationContext applicationContext;

    public RocketMQController(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    @Value("${rocketmq.enabled:false}")
    private boolean rocketmqEnabled;

    // TODO: 手动初始化RocketMQ时需要考虑以下几点：
    // 1. 检查相关依赖的Bean是否需要手动初始化（如RocketMQProducerService和RocketMQConsumerService）
    // 2. 确保Bean的初始化顺序正确
    // 3. 处理权限不足的问题，可能需要添加相应的权限检查和错误处理
    @PostMapping("/init")
    public ResponseEntity<String> initializeRocketMQ() {
        if (rocketmqEnabled) {
            return ResponseEntity.badRequest().body("RocketMQ已经初始化");
        }

        try {
            // 设置系统属性，使RocketMQTemplate条件注解生效
            System.setProperty("rocketmq.enabled", "true");
            log.info("RocketMQ初始化开始");
            
            // 从ApplicationContext获取RocketMQConfig Bean
            org.springframework.beans.factory.config.ConfigurableListableBeanFactory beanFactory = 
                ((org.springframework.context.ConfigurableApplicationContext) applicationContext).getBeanFactory();
            
            // 获取RocketMQConfig类
            Class<?> rocketMQConfigClass = Class.forName("com.example.springboot17demo.config.RocketMQConfig");
            
            // 检查RocketMQConfig Bean是否已存在
            if (applicationContext.containsBean("rocketMQConfig")) {
                log.info("RocketMQConfig Bean已存在，将被覆盖");
                // 在Spring中，我们不直接销毁Bean，而是通过重新注册来覆盖它
            }
            
            // 重新注册RocketMQTemplate Bean
            Object rocketMQConfig = rocketMQConfigClass.getDeclaredConstructor().newInstance();
            beanFactory.registerSingleton("rocketMQConfig", rocketMQConfig);
            
            // 调用RocketMQConfig的rocketMQTemplate方法创建RocketMQTemplate
            java.lang.reflect.Method rocketMQTemplateMethod = rocketMQConfigClass.getDeclaredMethod("rocketMQTemplate");
            Object rocketMQTemplate = rocketMQTemplateMethod.invoke(rocketMQConfig);
            
            // 注册RocketMQTemplate Bean
            beanFactory.registerSingleton("rocketMQTemplate", rocketMQTemplate);
            
            log.info("RocketMQ组件初始化成功");
            return ResponseEntity.ok("RocketMQ初始化成功");
        } catch (Exception e) {
            log.error("RocketMQ初始化失败", e);
            return ResponseEntity.internalServerError().body("RocketMQ初始化失败: " + e.getMessage());
        }
    }

    // TODO: RocketMQ状态查询功能优化点：
    // 1. 添加更详细的状态信息（如Producer和Consumer的运行状态）
    // 2. 添加连接状态检查
    // 3. 添加消息积压情况的监控
    // 4. 考虑添加健康检查接口
    @GetMapping("/status")
    public ResponseEntity<String> getRocketMQStatus() {
        StringBuilder status = new StringBuilder();
        status.append("RocketMQ状态:\n");
        status.append("初始化状态: ").append(rocketmqEnabled ? "已初始化" : "未初始化").append("\n");
        
        // 检查相关Bean的存在状态
        status.append("RocketMQConfig Bean: ").append(applicationContext.containsBean("rocketMQConfig") ? "存在" : "不存在").append("\n");
        status.append("RocketMQTemplate Bean: ").append(applicationContext.containsBean("rocketMQTemplate") ? "存在" : "不存在");
        
        return ResponseEntity.ok(status.toString());
    }

}