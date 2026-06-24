package com.tlias.pojo.entity;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 就业记录实体类，对应数据库 job 表
 * 用于记录学生的就业去向信息，包括就业单位、职位、薪资、城市等
 */
@Data
public class Job {
    /** 就业记录主键ID */
    private Integer id;
    /** 关联学生ID，对应 student 表 */
    private Integer studentId;
    /** 就业单位/公司名称 */
    private String company;
    /** 担任职位 */
    private String position;
    /** 月薪金额 */
    private BigDecimal salary;
    /** 入职日期 */
    private LocalDate jobDate;
    /** 就业城市 */
    private String city;
    /** 记录创建时间 */
    private LocalDateTime createTime;
    /** 记录最后更新时间 */
    private LocalDateTime updateTime;

    /** 关联查询字段：学生姓名，通过 JOIN student 表获取，非数据库存储字段 */
    private String studentName;
}
