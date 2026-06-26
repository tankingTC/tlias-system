package com.tlias.interceptor;

import com.tlias.util.JwtUtil;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

/**
 * JWT Token 认证拦截器
 * 在每个请求到达 Controller 之前执行：
 * 1. 放行 OPTIONS 预检请求和登录接口
 * 2. 从请求头获取 Token 并校验
 * 3. 校验通过后将用户信息存入 request 属性，供后续使用
 * 4. 校验失败返回 HTTP 401
 */
@Component
public class JwtInterceptor implements HandlerInterceptor {

    @Autowired
    private JwtUtil jwtUtil;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 1. 放行 OPTIONS 预检请求（CORS 跨域时浏览器会先发 OPTIONS）
        if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
            return true;
        }

        // 2. 获取请求头中的 Token
        String token = request.getHeader("token");
        if (token == null || token.isEmpty()) {
            response.setStatus(401); // 未携带 Token
            return false;
        }

        // 4. 校验 Token 有效性（签名验证 + 过期检查）
        try {
            Claims claims = jwtUtil.parseToken(token);
            // 将用户信息存入 request 属性，供 Controller 和 AOP 切面使用
            request.setAttribute("userId", claims.get("id"));
            request.setAttribute("username", claims.get("username"));
            return true; // 校验通过，放行请求
        } catch (Exception e) {
            response.setStatus(401); // Token 无效或已过期
            return false;
        }
    }
}
