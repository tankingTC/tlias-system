package com.tlias.config;

import com.tlias.mapper.EmpMapper;
import com.tlias.pojo.entity.Emp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

/**
 * 数据初始化器
 * 实现 CommandLineRunner 接口，Spring Boot 启动完成后自动执行
 * 功能：首次启动时检查并创建默认管理员账号（admin/123456）
 * 设计：幂等操作，已存在 admin 用户则跳过
 */
@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired(required = false) // required=false：数据库未连接时不报错
    private EmpMapper empMapper;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        if (empMapper == null) {
            System.out.println("=== 数据库未连接，跳过数据初始化 ===");
            return;
        }

        try {
            // 检查是否已存在 admin 用户（幂等设计）
            Emp admin = empMapper.selectByUsername("admin");
            if (admin == null) {
                Emp newAdmin = new Emp();
                newAdmin.setUsername("admin");
                newAdmin.setPassword(passwordEncoder.encode("123456")); // BCrypt 加密
                newAdmin.setName("系统管理员");
                newAdmin.setGender((short) 1);
                newAdmin.setPhone("13800138000");
                newAdmin.setJob("系统管理员");
                newAdmin.setDeptId(3); // 技术部
                empMapper.insert(newAdmin);
                System.out.println("=== 默认管理员账号已创建 ===");
                System.out.println("用户名: admin");
                System.out.println("密码: 123456");
                System.out.println("===========================");
            }
        } catch (Exception e) {
            // 数据库可能未就绪（如 Docker 启动顺序），不影响应用启动
            System.out.println("=== 数据初始化失败（数据库可能未就绪）: " + e.getMessage() + " ===");
        }
    }
}
