package com.tlias.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.tlias.exception.BusinessException;
import com.tlias.mapper.ClazzMapper;
import com.tlias.pojo.PageResult;
import com.tlias.pojo.entity.Clazz;
import com.tlias.service.ClazzService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

/**
 * 班级管理服务实现类
 * 实现ClazzService接口，提供班级的分页查询、增删改功能
 * 删除班级时会检查是否有关联学生，防止误删
 */
@Service
public class ClazzServiceImpl implements ClazzService {

    @Autowired
    private ClazzMapper clazzMapper;

    /**
     * 分页查询班级列表
     * 使用PageHelper进行物理分页，先设置分页参数再执行查询
     * @param page      当前页码
     * @param pageSize  每页记录数
     * @param name      班级名称
     * @param endBegin  结业开始日期
     * @param endEnd    结业结束日期
     * @return 分页结果对象
     */
    @Override
    public PageResult<Clazz> page(Integer page, Integer pageSize, String name, LocalDate endBegin, LocalDate endEnd) {
        // 设置分页参数，PageHelper会拦截下一次SQL查询进行分页
        PageHelper.startPage(page, pageSize);
        // 按条件查询班级列表
        List<Clazz> list = clazzMapper.selectByCondition(name, endBegin, endEnd);
        // 将查询结果转换为Page对象以获取分页元数据（总记录数等）
        Page<Clazz> p = (Page<Clazz>) list;
        return new PageResult<>(p.getTotal(), p.getResult());
    }

    /**
     * 根据ID查询班级详情
     * @param id 班级ID
     * @return 班级详情对象
     */
    @Override
    public Clazz getById(Integer id) {
        return clazzMapper.selectById(id);
    }

    /**
     * 新增班级
     * @param clazz 待新增的班级对象
     */
    @Override
    public void add(Clazz clazz) {
        clazzMapper.insert(clazz);
    }

    /**
     * 更新班级信息
     * @param clazz 包含更新信息的班级对象
     */
    @Override
    public void update(Clazz clazz) {
        clazzMapper.update(clazz);
    }

    /**
     * 根据ID删除班级
     * 删除前检查该班级下是否有学生，若有学生则抛出业务异常，禁止删除
     * @param id 待删除班级的ID
     * @throws BusinessException 当班级下存在学生时抛出
     */
    @Override
    public void delete(Integer id) {
        // 查询该班级下的学生数量
        int count = clazzMapper.countStudents(id);
        // 如果有学生关联，则不允许删除，抛出业务异常
        if (count > 0) {
            throw new BusinessException("该班级下有学生，不能直接删除");
        }
        // 无关联学生，执行删除操作
        clazzMapper.deleteById(id);
    }
}
