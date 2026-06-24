package com.tlias.controller;

import com.tlias.pojo.Result;
import com.tlias.pojo.entity.Dept;
import com.tlias.service.DeptService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 部门管理控制器
 * 处理部门相关的CRUD操作，服务于部门管理模块
 */
@RestController
@RequestMapping("/depts")
public class DeptController {

    @Autowired
    private DeptService deptService;

    /**
     * 查询所有部门列表
     * @return 包含所有部门信息的Result对象
     */
    @GetMapping
    public Result<List<Dept>> list() {
        return Result.success(deptService.list());
    }

    /**
     * 新增部门
     * @param dept 部门信息（从请求体获取）
     * @return 操作结果
     */
    @PostMapping
    public Result<Void> add(@RequestBody Dept dept) {
        deptService.add(dept);
        return Result.success();
    }

    /**
     * 更新部门信息
     * @param dept 部门信息（从请求体获取，需包含id）
     * @return 操作结果
     */
    @PutMapping
    public Result<Void> update(@RequestBody Dept dept) {
        deptService.update(dept);
        return Result.success();
    }

    /**
     * 根据ID删除部门
     * @param id 部门ID（从路径参数获取）
     * @return 操作结果
     */
    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Integer id) {
        deptService.delete(id);
        return Result.success();
    }
}
