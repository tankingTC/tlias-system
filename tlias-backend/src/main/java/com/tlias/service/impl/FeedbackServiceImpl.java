package com.tlias.service.impl;
import com.tlias.mapper.FeedbackMapper;
import com.tlias.pojo.entity.Feedback;
import com.tlias.service.FeedbackService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

/**
 * 反馈管理服务实现类
 * 实现FeedbackService接口，通过FeedbackMapper完成反馈数据的增删改查操作
 * 支持按反馈类型和处理状态进行条件查询
 */
@Service
public class FeedbackServiceImpl implements FeedbackService {

    @Autowired
    private FeedbackMapper feedbackMapper;

    /**
     * 查询反馈列表，按类型和处理状态条件筛选
     * @param type   反馈类型（可为null，不筛选）
     * @param status 处理状态（可为null，不筛选）
     * @return 符合条件的反馈列表
     */
    @Override
    public List<Feedback> list(Short type, Short status) {
        return feedbackMapper.selectByCondition(type, status);
    }

    /**
     * 根据ID查询反馈详情
     * @param id 反馈ID
     * @return 反馈详情对象
     */
    @Override
    public Feedback getById(Integer id) {
        return feedbackMapper.selectById(id);
    }

    /**
     * 新增反馈记录
     * @param feedback 待新增的反馈对象
     */
    @Override
    public void add(Feedback feedback) {
        feedbackMapper.insert(feedback);
    }

    /**
     * 更新反馈信息
     * @param feedback 包含更新信息的反馈对象
     */
    @Override
    public void update(Feedback feedback) {
        feedbackMapper.update(feedback);
    }

    /**
     * 根据ID删除反馈记录
     * @param id 待删除反馈的ID
     */
    @Override
    public void delete(Integer id) {
        feedbackMapper.deleteById(id);
    }
}
