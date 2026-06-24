package com.tlias.controller;

import com.tlias.exception.BusinessException;
import com.tlias.pojo.PageResult;
import com.tlias.pojo.Result;
import com.tlias.pojo.entity.Emp;
import com.tlias.pojo.entity.WorkExperience;
import com.tlias.service.EmpService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

/**
 * 员工管理控制器
 * 提供员工 CRUD、分页查询、工作经历管理
 * 支持按姓名/性别/入职时间范围筛选
 */
@RestController
@RequestMapping("/emps")
public class EmpController {

    @Autowired
    private EmpService empService;

    /**
     * 分页查询员工列表
     * 支持多条件筛选：姓名模糊搜索、性别精确匹配、入职时间范围
     * 按 update_time 倒序排列
     */
    @GetMapping
    public Result<PageResult<Emp>> page(
            @RequestParam(defaultValue = "1") Integer page,        // 页码，默认第1页
            @RequestParam(defaultValue = "10") Integer pageSize,   // 每页条数，默认10条
            @RequestParam(required = false) String name,           // 姓名（模糊搜索）
            @RequestParam(required = false) Short gender,          // 性别（0-女 1-男）
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate begin,  // 入职开始日期
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate end) {  // 入职结束日期
        return Result.success(empService.page(page, pageSize, name, gender, begin, end));
    }

    /**
     * 新增员工
     * 参数校验：用户名(必填/2-20位/仅字母/唯一)、姓名(必填/2-10位)、手机号(11位/唯一)
     * 新增员工默认密码 123456
     */
    @PostMapping
    public Result<Void> add(@RequestBody Emp emp) {
        // 用户名校验
        if (emp.getUsername() == null || emp.getUsername().isEmpty()) {
            return Result.error("用户名不能为空");
        }
        if (emp.getUsername().length() < 2 || emp.getUsername().length() > 20) {
            return Result.error("用户名长度为2-20位");
        }
        if (!emp.getUsername().matches("^[a-zA-Z]+$")) {
            return Result.error("用户名只能包含字母");
        }
        // 姓名校验
        if (emp.getName() == null || emp.getName().isEmpty()) {
            return Result.error("姓名不能为空");
        }
        if (emp.getName().length() < 2 || emp.getName().length() > 10) {
            return Result.error("姓名长度为2-10位");
        }
        // 手机号格式校验（11位数字，1开头）
        if (emp.getPhone() != null && !emp.getPhone().isEmpty()) {
            if (!emp.getPhone().matches("^1\\d{10}$")) {
                return Result.error("手机号格式不正确");
            }
        }
        empService.add(emp); // Service 层处理密码加密和唯一性校验
        return Result.success();
    }

    /**
     * 查询员工详情（含工作经历）
     * 返回 Map: {emp: 员工信息, workExperience: 工作经历列表}
     */
    @GetMapping("/{id}")
    public Result<Map<String, Object>> getById(@PathVariable Integer id) {
        Emp emp = empService.getById(id);
        List<WorkExperience> workExp = empService.getWorkExperience(id);
        return Result.success(Map.of("emp", emp, "workExperience", workExp));
    }

    /**
     * 修改员工信息
     * 注意：username 和 password 字段会被忽略，不允许修改
     */
    @PutMapping
    public Result<Void> update(@RequestBody Emp emp) {
        empService.update(emp);
        return Result.success();
    }

    /**
     * 删除员工
     */
    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Integer id) {
        empService.delete(id);
        return Result.success();
    }

    /**
     * 查询员工工作经历列表
     */
    @GetMapping("/{id}/work-experience")
    public Result<List<WorkExperience>> getWorkExperience(@PathVariable Integer id) {
        return Result.success(empService.getWorkExperience(id));
    }

    /**
     * 保存员工工作经历（先删后增策略）
     * 传入完整的工作经历列表，替换该员工原有记录
     */
    @PostMapping("/{id}/work-experience")
    public Result<Void> saveWorkExperience(@PathVariable Integer id, @RequestBody List<WorkExperience> list) {
        empService.saveWorkExperience(id, list);
        return Result.success();
    }
}
