package com.tlias.service;
import com.tlias.pojo.entity.Job;
import java.util.List;

/**
 * 岗位管理服务接口
 * 提供岗位信息的增删改查功能，用于管理就业岗位信息
 */
public interface JobService {

    /**
     * 查询所有岗位列表
     * @return 所有岗位的列表
     */
    List<Job> list();

    /**
     * 根据ID查询岗位详情
     * @param id 岗位ID
     * @return 岗位详情对象
     */
    Job getById(Integer id);

    /**
     * 新增岗位
     * @param job 待新增的岗位对象
     */
    void add(Job job);

    /**
     * 更新岗位信息
     * @param job 包含更新信息的岗位对象
     */
    void update(Job job);

    /**
     * 根据ID删除岗位
     * @param id 待删除岗位的ID
     */
    void delete(Integer id);
}
