package com.tlias.controller;

import com.tlias.pojo.Result;
import com.tlias.pojo.entity.Exam;
import com.tlias.service.ExamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

/**
 * 考试管理控制器
 * 处理考试信息的CRUD操作，服务于考试管理模块
 */
@RestController
@RequestMapping("/exams")
public class ExamController {
    @Autowired private ExamService examService;

    /**
     * 查询所有考试列表
     * @return 包含所有考试信息的Result对象
     */
    @GetMapping public Result<List<Exam>> list() { return Result.success(examService.list()); }

    /**
     * 新增考试
     * @param exam 考试信息（从请求体获取）
     * @return 操作结果
     */
    @PostMapping public Result<Void> add(@RequestBody Exam exam) { examService.add(exam); return Result.success(); }

    /**
     * 根据ID查询考试详情
     * @param id 考试ID（从路径参数获取）
     * @return 考试详细信息
     */
    @GetMapping("/{id}") public Result<Exam> getById(@PathVariable Integer id) { return Result.success(examService.getById(id)); }

    /**
     * 更新考试信息
     * @param exam 考试信息（从请求体获取，需包含id）
     * @return 操作结果
     */
    @PutMapping public Result<Void> update(@RequestBody Exam exam) { examService.update(exam); return Result.success(); }

    /**
     * 根据ID删除考试
     * @param id 考试ID（从路径参数获取）
     * @return 操作结果
     */
    @DeleteMapping("/{id}") public Result<Void> delete(@PathVariable Integer id) { examService.delete(id); return Result.success(); }
}
