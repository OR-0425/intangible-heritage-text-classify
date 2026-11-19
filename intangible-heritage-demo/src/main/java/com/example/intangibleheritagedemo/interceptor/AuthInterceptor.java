package com.example.intangibleheritagedemo.interceptor;

import com.example.intangibleheritagedemo.util.JwtUtil;
import io.jsonwebtoken.Claims;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class AuthInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 放行登录、注册接口
        String uri = request.getRequestURI();
        if(uri.contains("/api/user/login") || uri.contains("/api/user/register")){
            return true;
        }
        String token = request.getHeader("token");
        if(token == null || token.isEmpty()){
            response.setStatus(401);
            return false;
        }
        Claims claims;
        try{
            claims = JwtUtil.parseToken(token);
        }catch (Exception e){
            response.setStatus(401);
            return false;
        }
        request.setAttribute("userId", claims.get("userId"));
        request.setAttribute("role", claims.get("role"));
        return true;
    }
}
