package com.tlias.pojo.entity;

import lombok.Data;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 班级实体类，对应数据库 class 表
 * 用于管理培训班级信息，包括班级名称、教室、授课讲师、开课时间等
 */
@Data
public class Clazz {
    /** 班级主键ID */
    private Integer id;
    /** 班级名称 */
    private String name;
    /** 上课教室 */
    private String classroom;
    /** 授课讲师ID，关联 emp 表 */
    private Integer teacherId;
    /** 开课日期 */
    private LocalDate startDate;
    /** 结课日期 */
    private LocalDate endDate;
    /** 班级状态：0-未开班，1-已开班，2-已结班 */
    private Short status;
    /** 授课科目 */
    private String subject;
    /** 记录创建时间 */
    private LocalDateTime createTime;
    /** 记录最后更新时间 */
    private LocalDateTime updateTime;

    /** 关联查询字段：讲师姓名，通过 JOIN emp 表获取，非数据库存储字段 */
    private String teacherName;
}
