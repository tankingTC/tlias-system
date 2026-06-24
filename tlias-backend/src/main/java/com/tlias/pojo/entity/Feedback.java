package com.tlias.pojo.entity;

import lombok.Data;
import java.time.LocalDateTime;

/**
 * 反馈意见实体类，对应数据库 feedback 表
 * 用于收集和管理用户提交的反馈、建议、投诉等信息
 */
@Data
public class Feedback {
    /** 反馈记录主键ID */
    private Integer id;
    /** 反馈类型：1-建议，2-投诉，3-咨询 */
    private Short type;
    /** 反馈标题 */
    private String title;
    /** 反馈详细内容 */
    private String content;
    /** 联系人姓名 */
    private String contact;
    /** 联系电话 */
    private String phone;
    /** 处理状态：0-未处理，1-已处理 */
    private Short status;
    /** 记录创建时间 */
    private LocalDateTime createTime;
    /** 记录最后更新时间 */
    private LocalDateTime updateTime;
}
