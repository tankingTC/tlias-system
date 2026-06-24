package com.tlias.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 * 安全配置类
 * 注册 BCryptPasswordEncoder 作为 Spring Bean
 * 用于密码加密和校验（BCrypt 算法，自动加盐）
 * 所有需要加密密码的地方通过 @Autowired 注入使用，避免重复创建实例
 */
@Configuration
public class SecurityConfig {

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
