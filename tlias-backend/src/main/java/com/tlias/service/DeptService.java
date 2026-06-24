package com.tlias.service;

import com.tlias.pojo.entity.Dept;
import java.util.List;

/**
 * 部门管理服务接口
 * 提供部门的增删改查功能，用于管理组织架构中的部门信息
 */
public interface DeptService {

    /**
     * 查询所有部门列表
     * @return 所有部门的列表
     */
    List<Dept> list();

    /**
     * 新增部门
     * @param dept 待新增的部门对象
     */
    void add(Dept dept);

    /**
     * 更新部门信息
     * @param dept 包含更新信息的部门对象
     */
    void update(Dept dept);

    /**
     * 根据ID删除部门
     * @param id 待删除部门的ID
     */
    void delete(Integer id);
}
