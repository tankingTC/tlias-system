package com.tlias.pojo.entity;

import lombok.Data;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 学生实体类，对应数据库 student 表
 * 用于管理学生的基本信息、学籍状态、违规记录及就业情况
 */
@Data
public class Student {
    /** 学生主键ID */
    private Integer id;
    /** 学号 */
    private String studentNo;
    /** 学生姓名 */
    private String name;
    /** 性别：0-女，1-男 */
    private Short gender;
    /** 手机号码 */
    private String phone;
    /** 身份证号码 */
    private String idCard;
    /** 是否为大专学历：0-否，1-是 */
    private Integer isCollege;
    /** 联系地址 */
    private String address;
    /** 所属班级ID，关联 class 表 */
    private Integer classId;
    /** 学历层次 */
    private String education;
    /** 毕业院校 */
    private String school;
    /** 所学专业 */
    private String major;
    /** 入学日期 */
    private LocalDate enrollDate;
    /** 在读状态：0-休学，1-在读，2-结业 */
    private Short studyStatus;
    /** 违规次数 */
    private Integer violationCount;
    /** 违规扣分累计值 */
    private Integer violationScore;
    /** 就业状态：0-未就业，1-已就业 */
    private Short jobStatus;
    /** 记录创建时间 */
    private LocalDateTime createTime;
    /** 记录最后更新时间 */
    private LocalDateTime updateTime;

    /** 关联查询字段：班级名称，通过 JOIN class 表获取，非数据库存储字段 */
    private String className;
}
