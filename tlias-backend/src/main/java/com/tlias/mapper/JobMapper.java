package com.tlias.mapper;

import com.tlias.pojo.entity.Job;
import org.apache.ibatis.annotations.*;
import java.util.List;

/**
 * 就业记录数据访问层接口，对应数据库 job 表
 * 提供就业信息的增删改查及统计操作
 */
@Mapper
public interface JobMapper {

    /**
     * 查询所有就业记录，关联查询学生姓名，按ID倒序排列
     * @return 就业记录列表
     */
    @Select("SELECT j.*, s.name as studentName FROM job j " +
            "LEFT JOIN student s ON j.student_id = s.id " +
            "ORDER BY j.id DESC")
    List<Job> selectAll();

    /**
     * 根据ID查询就业记录详情，关联查询学生姓名
     * @param id 就业记录ID
     * @return 就业记录实体对象
     */
    @Select("SELECT j.*, s.name as studentName FROM job j " +
            "LEFT JOIN student s ON j.student_id = s.id " +
            "WHERE j.id = #{id}")
    Job selectById(Integer id);

    /**
     * 新增就业记录，自动回填生成的主键ID
     * @param job 就业记录实体对象
     */
    @Insert("INSERT INTO job(student_id, company, position, salary, job_date, city, create_time, update_time) " +
            "VALUES(#{studentId}, #{company}, #{position}, #{salary}, #{jobDate}, #{city}, NOW(), NOW())")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    void insert(Job job);

    /**
     * 根据ID更新就业记录全部信息，同时更新修改时间
     * @param job 就业记录实体对象（需包含id）
     */
    @Update("UPDATE job SET student_id = #{studentId}, company = #{company}, position = #{position}, " +
            "salary = #{salary}, job_date = #{jobDate}, city = #{city}, update_time = NOW() " +
            "WHERE id = #{id}")
    void update(Job job);

    /**
     * 根据主键ID删除就业记录
     * @param id 就业记录ID
     */
    @Delete("DELETE FROM job WHERE id = #{id}")
    void deleteById(Integer id);

    /**
     * 统计就业记录总数
     * @return 就业记录数量
     */
    @Select("SELECT COUNT(*) FROM job")
    int count();
}
