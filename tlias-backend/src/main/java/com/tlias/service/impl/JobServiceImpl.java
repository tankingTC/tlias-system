package com.tlias.service.impl;
import com.tlias.mapper.JobMapper;
import com.tlias.pojo.entity.Job;
import com.tlias.service.JobService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

/**
 * 岗位管理服务实现类
 * 实现JobService接口，通过JobMapper完成岗位数据的增删改查操作
 */
@Service
public class JobServiceImpl implements JobService {

    @Autowired
    private JobMapper jobMapper;

    /**
     * 查询所有岗位列表
     * @return 所有岗位的列表
     */
    @Override
    public List<Job> list() {
        return jobMapper.selectAll();
    }

    /**
     * 根据ID查询岗位详情
     * @param id 岗位ID
     * @return 岗位详情对象
     */
    @Override
    public Job getById(Integer id) {
        return jobMapper.selectById(id);
    }

    /**
     * 新增岗位
     * @param job 待新增的岗位对象
     */
    @Override
    public void add(Job job) {
        jobMapper.insert(job);
    }

    /**
     * 更新岗位信息
     * @param job 包含更新信息的岗位对象
     */
    @Override
    public void update(Job job) {
        jobMapper.update(job);
    }

    /**
     * 根据ID删除岗位
     * @param id 待删除岗位的ID
     */
    @Override
    public void delete(Integer id) {
        jobMapper.deleteById(id);
    }
}
