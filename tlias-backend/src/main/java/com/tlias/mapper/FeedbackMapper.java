package com.tlias.mapper;

import com.tlias.pojo.entity.Feedback;
import org.apache.ibatis.annotations.*;
import java.util.List;

/**
 * 反馈意见数据访问层接口，对应数据�?feedback �? * 提供反馈信息的增删改查及条件筛选操�? */
public interface FeedbackMapper {

    /**
     * 多条件动态查询反馈列表，支持按类型和状态筛�?     * @param type   反馈类型
     * @param status 处理状�?     * @return 反馈列表
     */
    @Select("<script>" +
            "SELECT * FROM feedback WHERE 1=1 " +
            "<if test='type != null'> AND type = #{type} </if>" +
            "<if test='status != null'> AND status = #{status} </if>" +
            "ORDER BY id DESC" +
            "</script>")
    List<Feedback> selectByCondition(@Param("type") Short type, @Param("status") Short status);

    /**
     * 根据ID查询反馈详情
     * @param id 反馈ID
     * @return 反馈实体对象
     */
    @Select("SELECT * FROM feedback WHERE id = #{id}")
    Feedback selectById(Integer id);

    /**
     * 新增反馈记录，自动回填生成的主键ID
     * @param feedback 反馈实体对象
     */
    @Insert("INSERT INTO feedback(type, title, content, contact, phone, status, create_time, update_time) " +
            "VALUES(#{type}, #{title}, #{content}, #{contact}, #{phone}, #{status}, NOW(), NOW())")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    void insert(Feedback feedback);

    /**
     * 根据ID更新反馈全部信息，同时更新修改时�?     * @param feedback 反馈实体对象（需包含id�?     */
    @Update("UPDATE feedback SET type = #{type}, title = #{title}, content = #{content}, " +
            "contact = #{contact}, phone = #{phone}, status = #{status}, update_time = NOW() " +
            "WHERE id = #{id}")
    void update(Feedback feedback);

    /**
     * 根据主键ID删除反馈记录
     * @param id 反馈ID
     */
    @Delete("DELETE FROM feedback WHERE id = #{id}")
    void deleteById(Integer id);
}
