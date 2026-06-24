package com.tlias.controller;

import com.tlias.mapper.OperationLogMapper;
import com.tlias.pojo.Result;
import com.tlias.pojo.entity.OperationLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

/**
 * 操作日志控制器
 * 提供操作日志查询功能，服务于系统监控和审计模块
 */
@RestController
@RequestMapping("/logs")
public class OperationLogController {

    @Autowired
    private OperationLogMapper logMapper;

    /**
     * 查询操作日志列表，支持按操作员用户名筛选
     * @param username 操作员用户名（可选，模糊查询）
     * @return 符合条件的操作日志列表
     */
    @GetMapping
    public Result<List<OperationLog>> list(@RequestParam(required = false) String username) {
        return Result.success(logMapper.selectByCondition(username));
    }
}
