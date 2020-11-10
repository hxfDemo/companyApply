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
@TableName("survey_info")
public class SurveyInfo extends BaseEntity implements Serializable {
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
     * 验证码
     */
    private String yzCode;

    /**
     * 单位id
     */
    private Long companyid;

    /**
     * 调查问卷名称
     */
    private String surveyName;

    /**
     * 调查类型
     */
    private Integer surveyType;

    /**
     * 二维码
     */
    private String qrCode;

    /**
     * 开始时间
     */
    private Date startTime;

    /**
     * 结束时间
     */
    private Date endTime;

    /**
     * 年度
     */
    private String years;

    private static final long serialVersionUID = 1L;
}