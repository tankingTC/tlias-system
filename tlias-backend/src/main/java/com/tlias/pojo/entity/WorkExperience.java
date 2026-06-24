package com.tlias.pojo.entity;

import lombok.Data;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 工作经历实体类，对应数据库 work_experience 表
 * 用于记录员工的历史工作经历信息，包括就职公司、职位、起止时间等
 */
@Data
public class WorkExperience {
    /** 工作经历主键ID */
    private Integer id;
    /** 关联员工ID，对应 emp 表 */
    private Integer empId;
    /** 入职日期 */
    private LocalDate startDate;
    /** 离职日期 */
    private LocalDate endDate;
    /** 就职公司名称 */
    private String company;
    /** 担任职位 */
    private String position;
    /** 记录创建时间 */
    private LocalDateTime createTime;
    /** 记录最后更新时间 */
    private LocalDateTime updateTime;
}
