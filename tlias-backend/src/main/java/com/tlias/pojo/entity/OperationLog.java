package com.tlias.pojo.entity;

import lombok.Data;
import java.time.LocalDateTime;

/**
 * 操作日志实体类，对应数据库 operation_log 表
 * 用于记录系统用户的操作行为，便于审计追踪和问题排查
 */
@Data
public class OperationLog {
    /** 日志主键ID */
    private Integer id;
    /** 操作用户的账号名 */
    private String username;
    /** 请求方法（GET/POST/PUT/DELETE） */
    private String method;
    /** 请求的URL地址 */
    private String url;
    /** 请求参数（JSON格式） */
    private String params;
    /** 操作用户的IP地址 */
    private String ip;
    /** 日志记录创建时间 */
    private LocalDateTime createTime;
}
