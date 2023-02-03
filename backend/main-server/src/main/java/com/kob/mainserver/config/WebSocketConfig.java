package com.kob.mainserver.config;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.server.standard.ServerEndpointExporter;

import com.kob.mainserver.model.bean.UserConnection;

/**
 * @author tongfs@stu.pku.edu.cn
 * @date 2023/2/2
 */
@Configuration
public class WebSocketConfig {

    @Bean
    public ServerEndpointExporter serverEndpointExporter() {
        return new ServerEndpointExporter();
    }

    /**
     * 建立user和socket的映射
     */
    @Bean
    public Map<Long, UserConnection> users() {
        return new ConcurrentHashMap<>();
    }
}
