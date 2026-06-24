package com.tlias.pojo.entity;

import lombok.Data;
import java.time.LocalDateTime;

/**
 * 考试实体类，对应数据库 exam 表
 * 用于管理班级考试安排，包括考试时间、满分值和考试状态
 */
@Data
public class Exam {
    /** 考试主键ID */
    private Integer id;
    /** 考试标题/名称 */
    private String title;
    /** 关联班级ID，对应 class 表 */
    private Integer classId;
    /** 考试开始时间 */
    private LocalDateTime startTime;
    /** 考试结束时间 */
    private LocalDateTime endTime;
    /** 考试满分分值 */
    private Integer totalScore;
    /** 考试状态：0-未开始，1-进行中，2-已结束 */
    private Short status;
    /** 记录创建时间 */
    private LocalDateTime createTime;
    /** 记录最后更新时间 */
    private LocalDateTime updateTime;

    /** 关联查询字段：班级名称，通过 JOIN class 表获取，非数据库存储字段 */
    private String className;
}
