package com.tlias.mapper;

import com.tlias.pojo.entity.Student;
import org.apache.ibatis.annotations.*;
import java.util.List;

/**
 * 学生数据访问层接口，对应数据库 student 表
 * 提供学生的增删改查、条件查询、批量导入、违规记录更新及统计操作
 */
@Mapper
public interface StudentMapper {

    /**
     * 多条件动态查询学生列表，支持按姓名模糊搜索、按班级和学历精确筛选
     * 关联查询班级名称
     * @param name      学生姓名（模糊匹配）
     * @param classId   班级ID
     * @param education 学历层次
     * @return 学生列表
     */
    @Select("<script>" +
            "SELECT s.*, c.name as className FROM student s " +
            "LEFT JOIN class c ON s.class_id = c.id " +
            "WHERE 1=1 " +
            "<if test='name != null and name != \"\"'>" +
            "AND s.name LIKE CONCAT('%', #{name}, '%') " +
            "</if>" +
            "<if test='classId != null'>" +
            "AND s.class_id = #{classId} " +
            "</if>" +
            "<if test='education != null and education != \"\"'>" +
            "AND s.education = #{education} " +
            "</if>" +
            "ORDER BY s.update_time DESC" +
            "</script>")
    List<Student> selectByCondition(@Param("name") String name,
                                     @Param("classId") Integer classId,
                                     @Param("education") String education);

    /**
     * 根据ID查询学生详情，关联查询班级名称
     * @param id 学生ID
     * @return 学生实体对象
     */
    @Select("SELECT s.*, c.name as className FROM student s " +
            "LEFT JOIN class c ON s.class_id = c.id " +
            "WHERE s.id = #{id}")
    Student selectById(Integer id);

    /**
     * 新增学生记录，自动回填生成的主键ID
     * @param student 学生实体对象
     */
    @Insert("INSERT INTO student(student_no, name, gender, phone, id_card, is_college, address, class_id, education, school, major, " +
            "enroll_date, study_status, violation_count, violation_score, job_status, create_time, update_time) " +
            "VALUES(#{studentNo}, #{name}, #{gender}, #{phone}, #{idCard}, #{isCollege}, #{address}, #{classId}, #{education}, #{school}, #{major}, " +
            "#{enrollDate}, #{studyStatus}, #{violationCount}, #{violationScore}, #{jobStatus}, NOW(), NOW())")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    void insert(Student student);

    /**
     * 根据ID更新学生全部信息，同时更新修改时间
     * @param student 学生实体对象（需包含id）
     */
    @Update("UPDATE student SET student_no = #{studentNo}, name = #{name}, gender = #{gender}, phone = #{phone}, id_card = #{idCard}, " +
            "is_college = #{isCollege}, address = #{address}, class_id = #{classId}, education = #{education}, school = #{school}, major = #{major}, " +
            "enroll_date = #{enrollDate}, study_status = #{studyStatus}, violation_count = #{violationCount}, " +
            "violation_score = #{violationScore}, job_status = #{jobStatus}, " +
            "update_time = NOW() WHERE id = #{id}")
    void update(Student student);

    /**
     * 根据主键ID删除学生记录
     * @param id 学生ID
     */
    @Delete("DELETE FROM student WHERE id = #{id}")
    void deleteById(Integer id);

    /**
     * 查询所有学生记录
     * @return 学生列表
     */
    @Select("SELECT * FROM student")
    List<Student> selectAll();

    /**
     * 根据学号查询学生
     * @param studentNo 学号
     * @return 学生实体对象，不存在则返回null
     */
    @Select("SELECT * FROM student WHERE student_no = #{studentNo}")
    Student selectByStudentNo(String studentNo);

    /**
     * 根据手机号查询学生
     * @param phone 手机号
     * @return 学生实体对象，不存在则返回null
     */
    @Select("SELECT * FROM student WHERE phone = #{phone}")
    Student selectByPhone(String phone);

    /**
     * 为学生增加违规记录：违规次数+1，违规扣分累加指定分数
     * @param id    学生ID
     * @param score 本次违规扣分值
     */
    @Update("UPDATE student SET violation_count = violation_count + 1, " +
            "violation_score = violation_score + #{score}, update_time = NOW() WHERE id = #{id}")
    void addViolation(@Param("id") Integer id, @Param("score") Integer score);

    /**
     * 批量导入学生数据，使用 foreach 拼接多条 INSERT VALUES
     * @param students 学生列表
     */
    @Insert("<script>" +
            "INSERT INTO student(student_no, name, gender, phone, id_card, is_college, address, class_id, education, school, major, " +
            "enroll_date, study_status, violation_count, violation_score, job_status, create_time, update_time) VALUES " +
            "<foreach collection='list' item='s' separator=','>" +
            "(#{s.studentNo}, #{s.name}, #{s.gender}, #{s.phone}, #{s.idCard}, #{s.isCollege}, #{s.address}, #{s.classId}, #{s.education}, #{s.school}, " +
            "#{s.major}, #{s.enrollDate}, #{s.studyStatus}, #{s.violationCount}, #{s.violationScore}, #{s.jobStatus}, NOW(), NOW())" +
            "</foreach>" +
            "</script>")
    void batchInsert(@Param("list") List<Student> students);

    /**
     * 统计学生总数
     * @return 学生数量
     */
    @Select("SELECT COUNT(*) FROM student")
    int count();

    /**
     * 统计已就业学生人数（job_status=1）
     * @return 已就业学生数量
     */
    @Select("SELECT COUNT(*) FROM student WHERE job_status = 1")
    int countEmployed();
}
