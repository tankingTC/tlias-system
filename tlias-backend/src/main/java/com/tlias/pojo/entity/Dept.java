package com.tlias.pojo.entity;

import lombok.Data;
import java.time.LocalDateTime;

/**
 * 部门实体类，对应数据库 dept 表
 * 用于管理企业内部组织架构中的部门信息
 */
@Data
public class Dept {
    /** 部门主键ID */
    private Integer id;
    /** 部门名称 */
    private String name;
    /** 记录创建时间 */
    private LocalDateTime createTime;
    /** 记录最后更新时间 */
    private LocalDateTime updateTime;
}
