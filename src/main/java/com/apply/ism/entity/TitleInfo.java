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
@TableName("title_info")
public class TitleInfo extends BaseEntity implements Serializable {
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
     * 题目编号
     */
    private Integer number;

    /**
     * 问卷调查名称
     */
    private String surveyName;

    /**
     * 题目内容
     */
    private String title;

    /**
     * 题目Id
     */
    private Long parentId;

    /**
     * 选项内容
     */
    private String optionInfo;

    /**
     * 分数
     */
    private double score;

    /**
     * 年份
     */
    private String years;

    private static final long serialVersionUID = 1L;
}