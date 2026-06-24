package com.tlias.aspect;

import com.tlias.mapper.OperationLogMapper;
import com.tlias.pojo.entity.OperationLog;
import jakarta.servlet.http.HttpServletRequest;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

/**
 * 操作日志 AOP 切面
 * 使用 Spring AOP 自动记录所有写操作（POST/PUT/DELETE）
 * 记录内容：操作人、请求方法、请求路径、请求参数、IP地址
 * 设计原则：日志记录失败不影响业务执行（异常安全）
 */
@Aspect
@Component
public class OperationLogAspect {

    @Autowired
    private OperationLogMapper logMapper;

    /**
     * 环绕通知：拦截所有 Controller 的非 GET 请求
     * 排除：LoginController（登录无需记录）、StatisticsController（只读查询）
     */
    @Around("execution(* com.tlias.controller.*Controller.*(..)) && " +
            "!execution(* com.tlias.controller.LoginController.*(..)) && " +
            "!execution(* com.tlias.controller.StatisticsController.*(..))")
    public Object logOperation(ProceedingJoinPoint joinPoint) throws Throwable {
        // 获取当前请求对象
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attributes == null) {
            return joinPoint.proceed(); // 非 Web 请求，直接放行
        }

        HttpServletRequest request = attributes.getRequest();
        String method = request.getMethod();

        // 只记录写操作（POST/PUT/DELETE），GET 查询不记录
        if ("POST".equals(method) || "PUT".equals(method) || "DELETE".equals(method)) {
            try {
                OperationLog log = new OperationLog();
                log.setMethod(method);
                log.setUrl(request.getRequestURI());
                log.setIp(getClientIp(request));

                // 从 request 属性获取用户名（由 JwtInterceptor 在认证时设置）
                Object username = request.getAttribute("username");
                if (username != null) {
                    log.setUsername(username.toString());
                }

                // 记录请求参数（截取前 500 字符，密码自动脱敏）
                Object[] args = joinPoint.getArgs();
                if (args.length > 0) {
                    String params = args[0] != null ? args[0].toString() : "";
                    // 密码字段脱敏：password=xxx 替换为 password=******
                    if (params.contains("password")) {
                        params = params.replaceAll("password=[^,}\\s]+", "password=******");
                    }
                    log.setParams(params.length() > 500 ? params.substring(0, 500) : params);
                }

                logMapper.insert(log); // 异步写入数据库
            } catch (Exception e) {
                // 日志记录失败不应影响业务执行（异常安全设计）
                System.err.println("操作日志记录失败: " + e.getMessage());
            }
        }

        return joinPoint.proceed(); // 执行目标方法（业务逻辑）
    }

    /**
     * 获取客户端真实 IP 地址
     * 优先读取代理头（X-Forwarded-For），否则取直连 IP
     */
    private String getClientIp(HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For");
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("X-Real-IP");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        return ip;
    }
}
