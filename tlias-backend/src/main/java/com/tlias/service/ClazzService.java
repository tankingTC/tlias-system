package com.tlias.service;

import com.tlias.pojo.PageResult;
import com.tlias.pojo.entity.Clazz;
import java.time.LocalDate;

/**
 * 班级管理服务接口
 * 提供班级的分页查询、详情查询、增删改功能，支持按名称和结业日期范围筛选
 */
public interface ClazzService {

    /**
     * 分页查询班级列表，支持按班级名称和结业日期范围条件筛选
     * @param page      当前页码
     * @param pageSize  每页记录数
     * @param name      班级名称（模糊匹配，可为null）
     * @param endBegin  结业开始日期（可为null）
     * @param endEnd    结业结束日期（可为null）
     * @return 分页结果对象，包含总记录数和当前页数据
     */
    PageResult<Clazz> page(Integer page, Integer pageSize, String name, LocalDate endBegin, LocalDate endEnd);

    /**
     * 根据ID查询班级详情
     * @param id 班级ID
     * @return 班级详情对象
     */
    Clazz getById(Integer id);

    /**
     * 新增班级
     * @param clazz 待新增的班级对象
     */
    void add(Clazz clazz);

    /**
     * 更新班级信息
     * @param clazz 包含更新信息的班级对象
     */
    void update(Clazz clazz);

    /**
     * 根据ID删除班级
     * @param id 待删除班级的ID
     */
    void delete(Integer id);
}
