package com.jy.bigevent.interceptors;

import com.jy.bigevent.pojo.Result;
import com.jy.bigevent.utils.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.Map;

@Component
public class LoginInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //令牌验证
        String token = request.getHeader("Authorization");

        // 验证token
        try {
            Map<String, Object> claims = JwtUtil.parseToken(token);
            //放行
            return true;
        } catch (Exception e) {
            //http状态码401，未授权
            response.setStatus(401);
            //不放行
            return false;
        }
    }
}
