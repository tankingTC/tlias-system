package com.tlias.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.Map;

/**
 * JWT (JSON Web Token) 工具类
 * 签名算法：HS384 (HMAC-SHA384)
 * Token 结构：Header.Payload.Signature
 * Payload 包含：用户ID、用户名、签发时间、过期时间
 */
@Component
public class JwtUtil {

    @Value("${jwt.secret}") // 从 application.yml 读取签名密钥
    private String secret;

    @Value("${jwt.ttl}") // Token 有效期（毫秒），默认2小时
    private Long ttl;

    /**
     * 生成 JWT Token
     * @param claims 载荷数据（如用户ID、用户名）
     * @return 编码后的 Token 字符串
     */
    public String generateToken(Map<String, Object> claims) {
        Date now = new Date();
        Date expiration = new Date(now.getTime() + ttl);
        // 使用 HMAC-SHA384 算法签名
        SecretKey key = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
        return Jwts.builder()
                .claims(claims)      // 载荷数据
                .issuedAt(now)       // 签发时间
                .expiration(expiration) // 过期时间
                .signWith(key)       // 签名
                .compact();          // 编码为字符串
    }

    /**
     * 解析 JWT Token
     * @param token Token 字符串
     * @return 载荷数据（Claims）
     * @throws Exception Token 无效或已过期时抛出异常
     */
    public Claims parseToken(String token) {
        SecretKey key = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
        return Jwts.parser()
                .verifyWith(key)     // 设置签名密钥
                .build()
                .parseSignedClaims(token) // 验证签名并解析
                .getPayload();
    }
}
