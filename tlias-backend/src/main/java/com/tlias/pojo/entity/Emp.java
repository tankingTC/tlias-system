package com.tlias.pojo.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 员工实体类，对应数据库 emp 表
 * 用于管理企业员工的基本信息，包括账号、薪资、所属部门等
 */
@Data
public class Emp {
    /** 员工主键ID */
    private Integer id;
    /** 登录账号名 */
    private String username;
    /** 登录密码，使用 @JsonIgnore 防止序列化时返回给前端，保障密码安全 */
    @JsonIgnore
    private String password;
    /** 员工真实姓名 */
    private String name;
    /** 性别：0-女，1-男 */
    private Short gender;
    /** 手机号码 */
    private String phone;
    /** 职位名称 */
    private String job;
    /** 薪资金额 */
    private java.math.BigDecimal salary;
    /** 所属部门ID，关联 dept 表 */
    private Integer deptId;
    /** 员工头像图片URL */
    private String image;
    /** 入职日期 */
    private LocalDate entryDate;
    /** 记录创建时间 */
    private LocalDateTime createTime;
    /** 记录最后更新时间 */
    private LocalDateTime updateTime;

    /** 关联查询字段：部门名称，通过 JOIN dept 表获取，非数据库存储字段 */
    private String deptName;
}
