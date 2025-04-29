package com.planus.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import com.planus.common.annotations.LoginRequired;

import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class AuthInterceptor implements HandlerInterceptor {

    @Autowired
    private JwtUtil jwtUtil;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {

        if (handler instanceof HandlerMethod) {
            HandlerMethod handlerMethod = (HandlerMethod) handler;
            LoginRequired loginRequired = handlerMethod.getMethodAnnotation(LoginRequired.class);

            if (loginRequired == null) {
                return true;
            }

            String bearerToken = request.getHeader("Authorization");
            if (bearerToken == null || !bearerToken.startsWith("Bearer ")) {
                response.setStatus(HttpStatus.UNAUTHORIZED.value());
                return false;
            }

            String token = bearerToken.substring(7);

            try {
                Claims claims = jwtUtil.getClaims(token);
                String userId = claims.getSubject();

                UserContext.setUserId(Long.parseLong(userId));
            } catch (Exception e) {
                response.setStatus(HttpStatus.UNAUTHORIZED.value());
                return false;
            }
        }

        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
            throws Exception {
        UserContext.clearUserId();
    }
}
