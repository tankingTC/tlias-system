package com.tlias.service.impl;

import com.tlias.mapper.DeptMapper;
import com.tlias.pojo.entity.Dept;
import com.tlias.service.DeptService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 部门管理服务实现类
 * 实现DeptService接口，通过DeptMapper完成部门数据的持久化操作
 */
@Service
public class DeptServiceImpl implements DeptService {

    @Autowired
    private DeptMapper deptMapper;

    /**
     * 查询所有部门列表，直接调用Mapper层查询全部部门数据
     * @return 所有部门的列表
     */
    @Override
    public List<Dept> list() {
        return deptMapper.selectAll();
    }

    /**
     * 新增部门，将部门信息持久化到数据库
     * @param dept 待新增的部门对象
     */
    @Override
    public void add(Dept dept) {
        deptMapper.insert(dept);
    }

    /**
     * 更新部门信息，根据部门ID更新对应的数据库记录
     * @param dept 包含更新信息的部门对象（需包含ID）
     */
    @Override
    public void update(Dept dept) {
        deptMapper.update(dept);
    }

    /**
     * 根据ID删除部门，从数据库中移除对应记录
     * @param id 待删除部门的ID
     */
    @Override
    public void delete(Integer id) {
        deptMapper.deleteById(id);
    }
}
