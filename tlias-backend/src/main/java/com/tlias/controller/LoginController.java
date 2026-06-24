package com.tlias.controller;

import com.tlias.mapper.EmpMapper;
import com.tlias.pojo.Result;
import com.tlias.pojo.entity.Emp;
import com.tlias.util.JwtUtil;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * 用户认证控制器
 * 处理登录、获取用户信息、退出登录
 * 认证方式：JWT Token（无状态，服务端不存储 Session）
 */
@RestController
public class LoginController {

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private EmpMapper empMapper;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    /**
     * 用户登录
     * 流程：校验用户名 → 校验密码 → 生成 JWT Token → 返回 Token 和用户名
     */
    @PostMapping("/login")
    public Result<Map<String, String>> login(@RequestBody LoginRequest request) {
        // 1. 根据用户名查询用户
        Emp emp = empMapper.selectByUsername(request.getUsername());
        if (emp == null) {
            return Result.error(401, "用户名或密码错误");
        }

        // 2. BCrypt 密码校验（比对加密后的哈希值）
        if (!passwordEncoder.matches(request.getPassword(), emp.getPassword())) {
            return Result.error(401, "用户名或密码错误");
        }

        // 3. 生成 JWT Token（携带用户ID和用户名，有效期2小时）
        Map<String, Object> claims = new HashMap<>();
        claims.put("id", emp.getId());
        claims.put("username", emp.getUsername());
        String token = jwtUtil.generateToken(claims);

        // 4. 返回 Token 和用户姓名
        Map<String, String> result = new HashMap<>();
        result.put("token", token);
        result.put("username", emp.getName());
        return Result.success(result);
    }

    /**
     * 获取当前登录用户信息
     * 前端在页面加载时调用，用于显示用户名和头像
     */
    @GetMapping("/user/info")
    public Result<Map<String, String>> getUserInfo(@RequestHeader("token") String token) {
        try {
            // 解析 Token 获取用户ID
            var claims = jwtUtil.parseToken(token);
            Emp emp = empMapper.selectById(Integer.parseInt(claims.get("id").toString()));
            if (emp == null) {
                return Result.error(404, "用户不存在");
            }
            // 返回用户基本信息（密码已通过 @JsonIgnore 过滤）
            Map<String, String> userInfo = new HashMap<>();
            userInfo.put("id", emp.getId().toString());
            userInfo.put("username", emp.getName());
            userInfo.put("image", emp.getImage() != null ? emp.getImage() : "");
            return Result.success(userInfo);
        } catch (Exception e) {
            return Result.error(401, "Token无效");
        }
    }

    /**
     * 登录请求 DTO（Data Transfer Object）
     */
    @Data
    public static class LoginRequest {
        private String username;
        private String password;
    }

    /**
     * 退出登录
     * JWT 是无状态的，服务端无需操作，前端删除本地 Token 即可
     */
    @PostMapping("/logout")
    public Result<Void> logout() {
        return Result.success();
    }
}
