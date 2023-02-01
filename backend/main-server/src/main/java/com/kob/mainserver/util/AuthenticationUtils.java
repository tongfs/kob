package com.kob.mainserver.util;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;

import com.kob.mainserver.model.UserDetailsImpl;
import com.kob.mainserver.model.po.User;

import io.jsonwebtoken.Claims;

/**
 * @author tongfs@stu.pku.edu.cn
 * @date 2023/1/1
 */
public class AuthenticationUtils {
    public static final String TOKEN_PREFIX = "Bearer ";

    public static User getUser() {
        UsernamePasswordAuthenticationToken authenticationToken =
                (UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl userDetails = (UserDetailsImpl) authenticationToken.getPrincipal();

        return userDetails.getUser();
    }

    public static long getUserId(String token) {
        long userId;
        try {
            Claims claims = JwtUtils.parseJWT(token);
            userId = new Integer(claims.getSubject());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return userId;
    }

    public static long getUserId() {
        return getUser().getId();
    }
}
