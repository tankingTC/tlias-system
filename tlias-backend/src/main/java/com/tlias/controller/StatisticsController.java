package com.tlias.controller;

import com.tlias.mapper.*;
import com.tlias.pojo.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * 数据统计控制器
 * 提供系统数据统计功能，服务于数据看板模块
 */
@RestController
@RequestMapping("/statistics")
public class StatisticsController {

    @Autowired
    private DeptMapper deptMapper;
    @Autowired
    private EmpMapper empMapper;
    @Autowired
    private ClazzMapper clazzMapper;
    @Autowired
    private StudentMapper studentMapper;
    @Autowired
    private JobMapper jobMapper;

    /**
     * 获取仪表盘统计数据
     * 包括部门数、员工数、班级数、学员数、岗位数以及就业率
     * @return 包含各项统计数据的Map对象
     */
    @GetMapping("/dashboard")
    public Result<Map<String, Object>> dashboard() {
        Map<String, Object> data = new HashMap<>();
        // 查询学员总数
        int studentCount = studentMapper.count();
        // 统计各模块数量
        data.put("deptCount", deptMapper.count());
        data.put("empCount", empMapper.count());
        data.put("classCount", clazzMapper.count());
        data.put("studentCount", studentCount);
        data.put("jobCount", jobMapper.count());

        // 计算就业率：已就业学员数 / 学员总数 * 100%
        int employed = studentMapper.countEmployed();
        // 避免除零错误，当学员总数为0时就业率为0
        double rate = studentCount == 0 ? 0 : (double) employed / studentCount * 100;
        // 格式化为保留一位小数的百分比字符串
        data.put("employmentRate", String.format("%.1f%%", rate));

        return Result.success(data);
    }
}
