package com.pk.lease.web.admin.custom.interceptor;

import com.pk.lease.common.exception.LeaseException;
import com.pk.lease.common.result.ResultCodeEnum;
import com.pk.lease.common.utils.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class AuthenticationInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String token=request.getHeader("access_token");
        JwtUtil.parseToken(token);
        return true;
    }
}
