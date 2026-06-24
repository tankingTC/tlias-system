package com.tlias.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 统一响应结果封装类
 * 所有 API 接口统一返回此格式，前端拦截器据此判断成功/失败
 * @param T 响应数据类型（泛型）
 *
 * 成功响应：{ "code": 200, "message": "操作成功", "data": {...} }
 * 错误响应：{ "code": 400, "message": "错误描述", "data": null }
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Result<T> {
    private Integer code;    // 业务状态码（200=成功，400=参数错误，500=系统异常）
    private String message;  // 提示信息
    private T data;          // 响应数据（泛型）

    /** 成功响应（无数据） */
    public static <T> Result<T> success() {
        return new Result<>(200, "操作成功", null);
    }

    /** 成功响应（带数据） */
    public static <T> Result<T> success(T data) {
        return new Result<>(200, "操作成功", data);
    }

    /** 错误响应（默认 500） */
    public static <T> Result<T> error(String message) {
        return new Result<>(500, message, null);
    }

    /** 错误响应（自定义状态码） */
    public static <T> Result<T> error(Integer code, String message) {
        return new Result<>(code, message, null);
    }
}
