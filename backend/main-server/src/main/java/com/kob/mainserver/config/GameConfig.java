package com.kob.mainserver.config;

import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.kob.mainserver.model.po.User;

/**
 * @author tongfs@stu.pku.edu.cn
 * @date 2023/2/3
 */
@Configuration
public class GameConfig {

    @Bean
    public Set<User> matchPool() {
        return new CopyOnWriteArraySet<>();
    }
}
