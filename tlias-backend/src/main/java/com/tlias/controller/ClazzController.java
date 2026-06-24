package com.tlias.controller;

import com.tlias.pojo.PageResult;
import com.tlias.pojo.Result;
import com.tlias.pojo.entity.Clazz;
import com.tlias.service.ClazzService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

/**
 * 班级管理控制器
 * 处理班级相关的CRUD操作，服务于班级管理模块
 */
@RestController
@RequestMapping("/classes")
public class ClazzController {

    @Autowired
    private ClazzService clazzService;

    /**
     * 分页查询班级列表，支持按名称和结课时间范围筛选
     * @param page 当前页码（默认1）
     * @param pageSize 每页记录数（默认10）
     * @param name 班级名称（可选，模糊查询）
     * @param endBegin 结课开始日期（可选，格式yyyy-MM-dd）
     * @param endEnd 结课结束日期（可选，格式yyyy-MM-dd）
     * @return 分页后的班级列表
     */
    @GetMapping
    public Result<PageResult<Clazz>> page(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer pageSize,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate endBegin,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate endEnd) {
        return Result.success(clazzService.page(page, pageSize, name, endBegin, endEnd));
    }

    /**
     * 新增班级
     * @param clazz 班级信息（从请求体获取）
     * @return 操作结果
     */
    @PostMapping
    public Result<Void> add(@RequestBody Clazz clazz) {
        clazzService.add(clazz);
        return Result.success();
    }

    /**
     * 根据ID查询班级详情
     * @param id 班级ID（从路径参数获取）
     * @return 班级详细信息
     */
    @GetMapping("/{id}")
    public Result<Clazz> getById(@PathVariable Integer id) {
        return Result.success(clazzService.getById(id));
    }

    /**
     * 更新班级信息
     * @param clazz 班级信息（从请求体获取，需包含id）
     * @return 操作结果
     */
    @PutMapping
    public Result<Void> update(@RequestBody Clazz clazz) {
        clazzService.update(clazz);
        return Result.success();
    }

    /**
     * 根据ID删除班级
     * @param id 班级ID（从路径参数获取）
     * @return 操作结果
     */
    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Integer id) {
        clazzService.delete(id);
        return Result.success();
    }
}
