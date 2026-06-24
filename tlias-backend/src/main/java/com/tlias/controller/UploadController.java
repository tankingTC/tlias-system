package com.tlias.controller;

import com.tlias.exception.BusinessException;
import com.tlias.pojo.Result;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

/**
 * 文件上传控制器
 * 使用 MinIO SDK 上传文件到 Sealos OSS（S3兼容对象存储）
 * 支持图片格式：jpg、jpeg、png、gif、bmp、webp，最大 5MB
 */
@RestController
@RequestMapping("/upload")
public class UploadController {

    /** 允许的图片文件扩展名列表 */
    private static final List<String> ALLOWED_EXTENSIONS = Arrays.asList(
            ".jpg", ".jpeg", ".png", ".gif", ".bmp", ".webp"
    );

    /** 最大文件大小限制：5MB */
    private static final long MAX_FILE_SIZE = 5 * 1024 * 1024;

    /** 允许的文件扩展名与 Content-Type 的映射 */
    private static final java.util.Map<String, String> CONTENT_TYPE_MAP = java.util.Map.of(
            ".jpg", "image/jpeg",
            ".jpeg", "image/jpeg",
            ".png", "image/png",
            ".gif", "image/gif",
            ".bmp", "image/bmp",
            ".webp", "image/webp"
    );

    @Autowired
    private MinioClient minioClient;

    /** 存储桶名称 */
    @Value("${minio.bucket}")
    private String bucketName;

    /** 对象存储访问地址 */
    @Value("${minio.endpoint}")
    private String endpoint;

    /**
     * 上传文件到 Sealos OSS
     * 流程：校验文件 → 生成唯一文件名 → MinIO 上传 → 返回访问 URL
     *
     * @param file 上传的文件（multipart/form-data 格式）
     * @return 文件访问 URL
     */
    @PostMapping
    public Result<String> upload(@RequestParam("file") MultipartFile file) {
        // 1. 校验文件是否为空
        if (file.isEmpty()) {
            throw new BusinessException("上传文件不能为空");
        }

        // 2. 校验文件大小是否超过限制（5MB）
        if (file.getSize() > MAX_FILE_SIZE) {
            throw new BusinessException("文件大小不能超过5MB");
        }

        // 3. 校验文件类型是否在允许列表中
        String originalFilename = file.getOriginalFilename();
        String ext = originalFilename != null ?
                originalFilename.substring(originalFilename.lastIndexOf(".")).toLowerCase() : "";
        if (!ALLOWED_EXTENSIONS.contains(ext)) {
            throw new BusinessException("仅支持图片格式: " + String.join(", ", ALLOWED_EXTENSIONS));
        }

        try {
            // 4. 使用 UUID 生成唯一文件名，避免文件名冲突
            String fileName = UUID.randomUUID() + ext;

            // 5. 获取文件 Content-Type（用于浏览器正确显示图片）
            String contentType = CONTENT_TYPE_MAP.getOrDefault(ext, "application/octet-stream");

            // 6. 上传文件到 MinIO / Sealos OSS
            minioClient.putObject(
                    PutObjectArgs.builder()
                            .bucket(bucketName)       // 存储桶名称
                            .object(fileName)          // 对象名称（文件名）
                            .stream(file.getInputStream(), file.getSize(), -1) // 文件流
                            .contentType(contentType)  // 内容类型
                            .build()
            );

            // 7. 拼接并返回文件的访问 URL
            // 格式：https://{endpoint}/{bucket}/{fileName}
            String url = endpoint + "/" + bucketName + "/" + fileName;
            return Result.success(url);

        } catch (Exception e) {
            throw new BusinessException("文件上传失败: " + e.getMessage());
        }
    }
}
