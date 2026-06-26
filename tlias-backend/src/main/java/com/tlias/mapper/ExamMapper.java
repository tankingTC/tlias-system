package com.tlias.mapper;

import com.tlias.pojo.entity.Exam;
import org.apache.ibatis.annotations.*;
import java.util.List;

/**
 * 考试数据访问层接口，对应数据�?exam �? * 提供考试信息的增删改查操�? */
public interface ExamMapper {

    /**
     * 查询所有考试记录，关联查询班级名称，按ID倒序排列
     * @return 考试列表
     */
    @Select("SELECT e.*, c.name as className FROM exam e " +
            "LEFT JOIN class c ON e.class_id = c.id " +
            "ORDER BY e.id DESC")
    List<Exam> selectAll();

    /**
     * 根据ID查询考试详情，关联查询班级名�?     * @param id 考试ID
     * @return 考试实体对象
     */
    @Select("SELECT e.*, c.name as className FROM exam e " +
            "LEFT JOIN class c ON e.class_id = c.id " +
            "WHERE e.id = #{id}")
    Exam selectById(Integer id);

    /**
     * 新增考试记录，自动回填生成的主键ID
     * @param exam 考试实体对象
     */
    @Insert("INSERT INTO exam(title, class_id, start_time, end_time, total_score, status, create_time, update_time) " +
            "VALUES(#{title}, #{classId}, #{startTime}, #{endTime}, #{totalScore}, #{status}, NOW(), NOW())")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    void insert(Exam exam);

    /**
     * 根据ID更新考试全部信息，同时更新修改时�?     * @param exam 考试实体对象（需包含id�?     */
    @Update("UPDATE exam SET title = #{title}, class_id = #{classId}, start_time = #{startTime}, " +
            "end_time = #{endTime}, total_score = #{totalScore}, status = #{status}, update_time = NOW() " +
            "WHERE id = #{id}")
    void update(Exam exam);

    /**
     * 根据主键ID删除考试记录
     * @param id 考试ID
     */
    @Delete("DELETE FROM exam WHERE id = #{id}")
    void deleteById(Integer id);
}
