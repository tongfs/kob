package com.kob.matchserver.config;

import static com.kob.common.constant.Constants.LOCALHOST;
import static com.kob.common.constant.Constants.MATCH_ADD_URI;
import static com.kob.common.constant.Constants.MATCH_REMOVE_URI;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;

/**
 * @author tongfs@stu.pku.edu.cn
 * @date 2023/1/1
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private static final String[] PERMIT_LOCAL_URI = {
            MATCH_ADD_URI,
            MATCH_REMOVE_URI
    };

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeRequests()
                .antMatchers(PERMIT_LOCAL_URI).hasIpAddress(LOCALHOST)
                .antMatchers(HttpMethod.OPTIONS).permitAll()
                .anyRequest().authenticated();
    }
}
