package com.tlias.controller;

import com.tlias.pojo.Result;
import com.tlias.pojo.entity.Job;
import com.tlias.service.JobService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

/**
 * 岗位管理控制器
 * 处理岗位信息的CRUD操作，服务于岗位管理模块
 */
@RestController
@RequestMapping("/jobs")
public class JobController {
    @Autowired private JobService jobService;

    /**
     * 查询所有岗位列表
     * @return 包含所有岗位信息的Result对象
     */
    @GetMapping public Result<List<Job>> list() { return Result.success(jobService.list()); }

    /**
     * 新增岗位
     * @param job 岗位信息（从请求体获取）
     * @return 操作结果
     */
    @PostMapping public Result<Void> add(@RequestBody Job job) { jobService.add(job); return Result.success(); }

    /**
     * 根据ID查询岗位详情
     * @param id 岗位ID（从路径参数获取）
     * @return 岗位详细信息
     */
    @GetMapping("/{id}") public Result<Job> getById(@PathVariable Integer id) { return Result.success(jobService.getById(id)); }

    /**
     * 更新岗位信息
     * @param job 岗位信息（从请求体获取，需包含id）
     * @return 操作结果
     */
    @PutMapping public Result<Void> update(@RequestBody Job job) { jobService.update(job); return Result.success(); }

    /**
     * 根据ID删除岗位
     * @param id 岗位ID（从路径参数获取）
     * @return 操作结果
     */
    @DeleteMapping("/{id}") public Result<Void> delete(@PathVariable Integer id) { jobService.delete(id); return Result.success(); }
}
