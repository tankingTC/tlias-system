package com.tlias.mapper;

import com.tlias.pojo.entity.Clazz;
import org.apache.ibatis.annotations.*;
import java.time.LocalDate;
import java.util.List;

/**
 * 班级数据访问层接口，对应数据库 class 表
 * 提供班级的增删改查、条件查询及学生人数统计操作
 */
@Mapper
public interface ClazzMapper {

    /**
     * 多条件动态查询班级列表，支持按名称模糊搜索、按结课日期范围筛选
     * 关联查询讲师姓名
     * @param name     班级名称（模糊匹配）
     * @param endBegin 结课日期起始值
     * @param endEnd   结课日期截止值
     * @return 班级列表
     */
    @Select("<script>" +
            "SELECT c.*, e.name as teacherName FROM class c " +
            "LEFT JOIN emp e ON c.teacher_id = e.id " +
            "WHERE 1=1 " +
            "<if test='name != null and name != \"\"'>" +
            "AND c.name LIKE CONCAT('%', #{name}, '%') " +
            "</if>" +
            "<if test='endBegin != null'>" +
            "AND c.end_date &gt;= #{endBegin} " +
            "</if>" +
            "<if test='endEnd != null'>" +
            "AND c.end_date &lt;= #{endEnd} " +
            "</if>" +
            "ORDER BY c.update_time DESC" +
            "</script>")
    List<Clazz> selectByCondition(@Param("name") String name,
                                   @Param("endBegin") LocalDate endBegin,
                                   @Param("endEnd") LocalDate endEnd);

    /**
     * 根据ID查询班级详情，关联查询讲师姓名
     * @param id 班级ID
     * @return 班级实体对象
     */
    @Select("SELECT c.*, e.name as teacherName FROM class c " +
            "LEFT JOIN emp e ON c.teacher_id = e.id " +
            "WHERE c.id = #{id}")
    Clazz selectById(Integer id);

    /**
     * 新增班级记录，自动回填生成的主键ID
     * @param clazz 班级实体对象
     */
    @Insert("INSERT INTO class(name, classroom, teacher_id, start_date, end_date, status, subject, create_time, update_time) " +
            "VALUES(#{name}, #{classroom}, #{teacherId}, #{startDate}, #{endDate}, #{status}, #{subject}, NOW(), NOW())")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    void insert(Clazz clazz);

    /**
     * 根据ID更新班级全部信息，同时更新修改时间
     * @param clazz 班级实体对象（需包含id）
     */
    @Update("UPDATE class SET name = #{name}, classroom = #{classroom}, teacher_id = #{teacherId}, start_date = #{startDate}, " +
            "end_date = #{endDate}, status = #{status}, subject = #{subject}, update_time = NOW() WHERE id = #{id}")
    void update(Clazz clazz);

    /**
     * 根据主键ID删除班级记录
     * @param id 班级ID
     */
    @Delete("DELETE FROM class WHERE id = #{id}")
    void deleteById(Integer id);

    /**
     * 统计班级总数
     * @return 班级数量
     */
    @Select("SELECT COUNT(*) FROM class")
    int count();

    /**
     * 统计指定班级下的学生人数
     * @param classId 班级ID
     * @return 学生数量
     */
    @Select("SELECT COUNT(*) FROM student WHERE class_id = #{classId}")
    int countStudents(@Param("classId") Integer classId);
}
