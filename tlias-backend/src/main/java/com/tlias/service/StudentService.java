package com.tlias.service;

import com.tlias.pojo.PageResult;
import com.tlias.pojo.entity.Student;
import java.util.List;

/**
 * 学生管理服务接口
 * 提供学生的分页查询、详情查询、增删改、批量导入、导出及违纪扣分功能
 */
public interface StudentService {

    /**
     * 分页查询学生列表，支持按姓名、班级、学历条件筛选
     * @param page      当前页码
     * @param pageSize  每页记录数
     * @param name      学生姓名（模糊匹配，可为null）
     * @param classId   班级ID（可为null）
     * @param education 学历（可为null）
     * @return 分页结果对象，包含总记录数和当前页数据
     */
    PageResult<Student> page(Integer page, Integer pageSize, String name, Integer classId, String education);

    /**
     * 根据ID查询学生详情
     * @param id 学生ID
     * @return 学生详情对象
     */
    Student getById(Integer id);

    /**
     * 新增单个学生
     * @param student 待新增的学生对象
     */
    void add(Student student);

    /**
     * 更新学生信息
     * @param student 包含更新信息的学生对象
     */
    void update(Student student);

    /**
     * 根据ID删除学生
     * @param id 待删除学生的ID
     */
    void delete(Integer id);

    /**
     * 批量导入学生数据（如通过Excel导入）
     * @param students 待导入的学生列表
     */
    void batchImport(List<Student> students);

    /**
     * 导出所有学生数据
     * @return 所有学生的列表
     */
    List<Student> exportAll();

    /**
     * 为学生添加违纪记录并扣分
     * @param id    学生ID
     * @param score 扣除的分数
     */
    void addViolation(Integer id, Integer score);
}
