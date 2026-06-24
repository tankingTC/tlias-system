package com.tlias.service;
import com.tlias.pojo.entity.Feedback;
import java.util.List;

/**
 * 反馈管理服务接口
 * 提供反馈信息的增删改查功能，支持按反馈类型和处理状态筛选
 */
public interface FeedbackService {

    /**
     * 查询反馈列表，支持按类型和处理状态筛选
     * @param type   反馈类型（可为null，表示不筛选）
     * @param status 处理状态（可为null，表示不筛选）
     * @return 符合条件的反馈列表
     */
    List<Feedback> list(Short type, Short status);

    /**
     * 根据ID查询反馈详情
     * @param id 反馈ID
     * @return 反馈详情对象
     */
    Feedback getById(Integer id);

    /**
     * 新增反馈记录
     * @param feedback 待新增的反馈对象
     */
    void add(Feedback feedback);

    /**
     * 更新反馈信息
     * @param feedback 包含更新信息的反馈对象
     */
    void update(Feedback feedback);

    /**
     * 根据ID删除反馈记录
     * @param id 待删除反馈的ID
     */
    void delete(Integer id);
}
