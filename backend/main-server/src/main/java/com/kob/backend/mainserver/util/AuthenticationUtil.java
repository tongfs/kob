package com.kob.backend.mainserver.util;

import com.kob.backend.mainserver.pojo.User;
import com.kob.backend.mainserver.service.util.UserDetailsImpl;
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

    public static int getUserId(String token) {
        int userId;
        try {
            Claims claims = JwtUtil.parseJWT(token);
            userId = new Integer(claims.getSubject());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return userId;
    }
}
