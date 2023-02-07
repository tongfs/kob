package com.kob.mainserver.filter;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.kob.mainserver.mapper.UserMapper;
import com.kob.mainserver.model.UserDetailsImpl;
import com.kob.mainserver.model.po.User;
import com.kob.mainserver.util.AuthenticationUtils;

/**
 * @author tongfs@stu.pku.edu.cn
 * @date 2023/1/1
 */
@Component
public class JwtAuthenticationTokenFilter extends OncePerRequestFilter {

    private static final String TOKEN_HEADER = "Authorization";

    @Autowired
    private UserMapper userMapper;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String token = request.getHeader(TOKEN_HEADER);

        // 如果没有携带token，则直接登录
        if (StringUtils.isBlank(token) || !StringUtils.startsWith(token, AuthenticationUtils.TOKEN_PREFIX)) {
            filterChain.doFilter(request, response);
            return;
        }

        token = StringUtils.substring(token, AuthenticationUtils.TOKEN_PREFIX.length());
        long userId = AuthenticationUtils.getUserId(token);

        User user = userMapper.selectById(userId);
        if (user == null) {
            throw new RuntimeException("身份验证失败，请重新登录");
        }

        UserDetailsImpl userDetails = new UserDetailsImpl(user);
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(userDetails, null, null);
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);

        filterChain.doFilter(request, response);
    }
}
