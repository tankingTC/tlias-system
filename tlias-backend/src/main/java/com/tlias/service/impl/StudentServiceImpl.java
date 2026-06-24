package com.tlias.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.tlias.mapper.StudentMapper;
import com.tlias.pojo.PageResult;
import com.tlias.pojo.entity.Student;
import com.tlias.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 学生管理服务实现类
 * 实现StudentService接口，提供学生的分页查询、增删改、批量导入、导出及违纪扣分功能
 */
@Service
public class StudentServiceImpl implements StudentService {

    @Autowired
    private StudentMapper studentMapper;

    /**
     * 分页查询学生列表
     * 使用PageHelper进行物理分页，支持按姓名、班级、学历条件筛选
     * @param page      当前页码
     * @param pageSize  每页记录数
     * @param name      学生姓名
     * @param classId   班级ID
     * @param education 学历
     * @return 分页结果对象
     */
    @Override
    public PageResult<Student> page(Integer page, Integer pageSize, String name, Integer classId, String education) {
        // 设置分页参数
        PageHelper.startPage(page, pageSize);
        // 按条件查询学生列表
        List<Student> list = studentMapper.selectByCondition(name, classId, education);
        // 转换为Page对象获取分页信息
        Page<Student> p = (Page<Student>) list;
        return new PageResult<>(p.getTotal(), p.getResult());
    }

    /**
     * 根据ID查询学生详情
     * @param id 学生ID
     * @return 学生详情对象
     */
    @Override
    public Student getById(Integer id) {
        return studentMapper.selectById(id);
    }

    /**
     * 新增单个学生
     * @param student 待新增的学生对象
     */
    @Override
    public void add(Student student) {
        studentMapper.insert(student);
    }

    /**
     * 更新学生信息
     * @param student 包含更新信息的学生对象
     */
    @Override
    public void update(Student student) {
        studentMapper.update(student);
    }

    /**
     * 根据ID删除学生
     * @param id 待删除学生的ID
     */
    @Override
    public void delete(Integer id) {
        studentMapper.deleteById(id);
    }

    /**
     * 批量导入学生数据
     * 通常用于Excel导入场景，一次性插入多条学生记录
     * @param students 待导入的学生列表
     */
    @Override
    public void batchImport(List<Student> students) {
        studentMapper.batchInsert(students);
    }

    /**
     * 导出所有学生数据，用于Excel导出等场景
     * @return 所有学生的列表
     */
    @Override
    public List<Student> exportAll() {
        return studentMapper.selectAll();
    }

    /**
     * 为学生添加违纪记录并扣分
     * @param id    学生ID
     * @param score 扣除的分数
     */
    @Override
    public void addViolation(Integer id, Integer score) {
        studentMapper.addViolation(id, score);
    }
}
