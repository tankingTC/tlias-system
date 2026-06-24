package com.tlias.service;
import com.tlias.pojo.entity.Exam;
import java.util.List;

/**
 * 考试管理服务接口
 * 提供考试信息的增删改查功能，用于管理课程考试安排
 */
public interface ExamService {

    /**
     * 查询所有考试列表
     * @return 所有考试的列表
     */
    List<Exam> list();

    /**
     * 根据ID查询考试详情
     * @param id 考试ID
     * @return 考试详情对象
     */
    Exam getById(Integer id);

    /**
     * 新增考试记录
     * @param exam 待新增的考试对象
     */
    void add(Exam exam);

    /**
     * 更新考试信息
     * @param exam 包含更新信息的考试对象
     */
    void update(Exam exam);

    /**
     * 根据ID删除考试记录
     * @param id 待删除考试的ID
     */
    void delete(Integer id);
}
