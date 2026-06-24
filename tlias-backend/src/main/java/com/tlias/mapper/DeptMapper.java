package com.tlias.mapper;

import com.tlias.pojo.entity.Dept;
import org.apache.ibatis.annotations.*;
import java.util.List;

/**
 * 部门数据访问层接口，对应数据库 dept 表
 * 提供部门的增删改查及统计操作
 */
@Mapper
public interface DeptMapper {

    /**
     * 查询所有部门，按更新时间倒序排列
     * @return 部门列表
     */
    @Select("SELECT * FROM dept ORDER BY update_time DESC")
    List<Dept> selectAll();

    /**
     * 新增部门记录，自动回填生成的主键ID
     * @param dept 部门实体对象
     */
    @Insert("INSERT INTO dept(name, create_time, update_time) VALUES(#{name}, NOW(), NOW())")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    void insert(Dept dept);

    /**
     * 根据ID更新部门名称，同时更新修改时间
     * @param dept 部门实体对象（需包含id和name）
     */
    @Update("UPDATE dept SET name = #{name}, update_time = NOW() WHERE id = #{id}")
    void update(Dept dept);

    /**
     * 根据主键ID删除部门记录
     * @param id 部门ID
     */
    @Delete("DELETE FROM dept WHERE id = #{id}")
    void deleteById(Integer id);

    /**
     * 根据ID查询单个部门
     * @param id 部门ID
     * @return 部门对象
     */
    @Select("SELECT * FROM dept WHERE id = #{id}")
    Dept selectById(Integer id);

    /**
     * 统计部门总数
     * @return 部门数量
     */
    @Select("SELECT COUNT(*) FROM dept")
    int count();
}
