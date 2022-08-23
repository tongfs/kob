package com.kob.backend.util;

import com.kob.backend.pojo.User;
import com.kob.backend.service.util.UserDetailsImpl;
import io.jsonwebtoken.Claims;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 * @author tfs
 * @date 2022-08-21
 * @description
 */
public class AuthenticationUtil {
    public static User getLoginUser() {
        UsernamePasswordAuthenticationToken authenticationToken =
                (UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl userDetails = (UserDetailsImpl) authenticationToken.getPrincipal();
        return userDetails.getUser();
    }

    public static long getUserId(String token) {
        long userId;
        try {
            Claims claims = JwtUtil.parseJWT(token);
            userId = new Long(claims.getSubject());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return userId;
    }
}
