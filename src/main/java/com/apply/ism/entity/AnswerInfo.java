package com.apply.ism.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 *
 * @author Leon
 * @date 2020-04-24
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("answer_info")
public class AnswerInfo extends BaseEntity implements Serializable {
    /**
     * 创建者Id
     */
    private Long createId;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 修改者Id
     */
    private Long updateId;

    /**
     * 修改时间
     */
    private Date updateTime;

    /**
     * 数据状态
     */
    private String dataState;

    /**
     * 排序
     */
    private Integer sort;

    /**
     * version字段
     */
    private Long versionNo;

    /**
     * 备注
     */
    private String remarks;

    /**
     * 备用字段1
     */
    private String spare1;

    /**
     * 备用字段2
     */
    private String spare2;

    /**
     * 备用字段3
     */
    private String spare3;

    /**
     * 年度
     */
    private String years;

    /**
     * 单位id
     */
    private Long companyid;

    /**
     * 调查选项id逗号分隔
     */
    private String answerIds;

    /**
     * 成绩
     */
    private String totalScore;

    private static final long serialVersionUID = 1L;
}