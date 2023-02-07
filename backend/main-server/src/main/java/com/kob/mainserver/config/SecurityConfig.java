package com.kob.mainserver.config;

import static com.kob.common.constant.Constants.GAME_NEXT_URI;
import static com.kob.common.constant.Constants.GAME_RECORD_URI;
import static com.kob.common.constant.Constants.GAME_START_URI;
import static com.kob.common.constant.Constants.LOCALHOST;
import static com.kob.common.constant.Constants.USER_LOGIN_URI;
import static com.kob.common.constant.Constants.USER_RANK_URI;
import static com.kob.common.constant.Constants.USER_REGISTER_URI;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.kob.mainserver.filter.JwtAuthenticationTokenFilter;

/**
 * @author tongfs@stu.pku.edu.cn
 * @date 2023/1/1
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private static final String[] PERMIT_OUT_URI = {
            USER_LOGIN_URI,
            USER_REGISTER_URI,
            USER_RANK_URI,
            GAME_RECORD_URI,
    };

    private static final String[] PERMIT_LOCAL_URI = {
            GAME_START_URI,
            GAME_NEXT_URI
    };

    @Autowired
    private JwtAuthenticationTokenFilter jwtAuthenticationTokenFilter;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeRequests()
                .antMatchers(PERMIT_OUT_URI).permitAll()
                .antMatchers(PERMIT_LOCAL_URI).hasIpAddress(LOCALHOST)
                .antMatchers(HttpMethod.OPTIONS).permitAll()
                .anyRequest().authenticated();

        http.addFilterBefore(jwtAuthenticationTokenFilter, UsernamePasswordAuthenticationFilter.class);
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers("/websocket/**");
    }
}
