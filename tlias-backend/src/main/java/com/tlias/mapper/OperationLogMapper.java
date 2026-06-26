package com.tlias.mapper;

import com.tlias.pojo.entity.OperationLog;
import org.apache.ibatis.annotations.*;
import java.util.List;

/**
 * 操作日志数据访问层接口，对应数据�?operation_log �? * 提供操作日志的记录入库和条件查询功能
 */
public interface OperationLogMapper {

    /**
     * 新增操作日志记录，自动回填生成的主键ID
     * @param log 操作日志实体对象
     */
    @Insert("INSERT INTO operation_log(username, method, url, params, ip, create_time) " +
            "VALUES(#{username}, #{method}, #{url}, #{params}, #{ip}, NOW())")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    void insert(OperationLog log);

    /**
     * 按用户名模糊查询操作日志，按创建时间倒序排列
     * @param username 操作用户名（模糊匹配�?     * @return 操作日志列表
     */
    @Select("<script>" +
            "SELECT * FROM operation_log WHERE 1=1 " +
            "<if test='username != null and username != \"\"'>" +
            "AND username LIKE CONCAT('%', #{username}, '%') " +
            "</if>" +
            "ORDER BY create_time DESC" +
            "</script>")
    List<OperationLog> selectByCondition(@Param("username") String username);
}
