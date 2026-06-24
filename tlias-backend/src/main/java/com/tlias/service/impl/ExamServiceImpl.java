package com.tlias.service.impl;
import com.tlias.mapper.ExamMapper;
import com.tlias.pojo.entity.Exam;
import com.tlias.service.ExamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

/**
 * 考试管理服务实现类
 * 实现ExamService接口，通过ExamMapper完成考试数据的增删改查操作
 */
@Service
public class ExamServiceImpl implements ExamService {

    @Autowired
    private ExamMapper examMapper;

    /**
     * 查询所有考试列表
     * @return 所有考试的列表
     */
    @Override
    public List<Exam> list() {
        return examMapper.selectAll();
    }

    /**
     * 根据ID查询考试详情
     * @param id 考试ID
     * @return 考试详情对象
     */
    @Override
    public Exam getById(Integer id) {
        return examMapper.selectById(id);
    }

    /**
     * 新增考试记录
     * @param exam 待新增的考试对象
     */
    @Override
    public void add(Exam exam) {
        examMapper.insert(exam);
    }

    /**
     * 更新考试信息
     * @param exam 包含更新信息的考试对象
     */
    @Override
    public void update(Exam exam) {
        examMapper.update(exam);
    }

    /**
     * 根据ID删除考试记录
     * @param id 待删除考试的ID
     */
    @Override
    public void delete(Integer id) {
        examMapper.deleteById(id);
    }
}
