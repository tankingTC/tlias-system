package com.tlias.config;

import io.minio.MinioClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * MinIO / Sealos OSS 配置类
 * 注册 MinIO 客户端 Bean，用于文件上传到 S3 兼容对象存储
 * Sealos Cloud 内置对象存储使用 S3 协议，MinIO SDK 完全兼容
 */
@Configuration
public class OssConfig {

    /** 对象存储访问地址（External Endpoint） */
    @Value("${minio.endpoint}")
    private String endpoint;

    /** 访问密钥 ID */
    @Value("${minio.access-key}")
    private String accessKey;

    /** 访问密钥 Secret */
    @Value("${minio.secret-key}")
    private String secretKey;

    /**
     * 创建 MinIO 客户端 Bean
     * 所有需要上传文件的地方通过 @Autowired 注入使用
     */
    @Bean
    public MinioClient minioClient() {
        return MinioClient.builder()
                .endpoint(endpoint)           // Sealos OSS Endpoint
                .credentials(accessKey, secretKey) // Sealos OSS 密钥
                .build();
    }
}
