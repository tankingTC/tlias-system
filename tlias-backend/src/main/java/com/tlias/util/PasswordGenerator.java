package com.tlias.util;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 * 运行此类生成 BCrypt 密码哈希
 * 使用命令: mvn exec:java "-Dexec.mainClass=com.tlias.util.PasswordGenerator"
 */
public class PasswordGenerator {
    public static void main(String[] args) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String password = "123456";
        String hash = encoder.encode(password);
        System.out.println("=== Generated BCrypt Hash ===");
        System.out.println("Password: " + password);
        System.out.println("Hash: " + hash);
        System.out.println("Matches: " + encoder.matches(password, hash));
        System.out.println("===========================");
    }
}
