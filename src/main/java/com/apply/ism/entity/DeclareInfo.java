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
 * @date 2020-04-22
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("declare_info")
public class DeclareInfo extends BaseEntity implements Serializable {
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
     * 用户Id
     */
    private Long userid;

    /**
     * 单位id
     */
    private Long companyId;

    /**
     * 申报等级
     */
    private Long declareLevel;

    /**
     * 单位名称
     */
    private String company;

    /**
     * 单位性质
     */
    private Long dwxz;

    /**
     * 详细地址
     */
    private String address;

    /**
     * 编制人数
     */
    private Long bzrs;

    /**
     * 分管领导姓名
     */
    private String adminName;

    /**
     * 联系方式
     */
    private String lxfs;

    /**
     * 具体部门
     */
    private String department;

    /**
     * 负责人姓名、电话
     */
    private String namePhone;

    /**
     * 联络员姓名
     */
    private String llyName;

    /**
     *联络人的联系方式
     */
    private String tel;

    /**
     * 邮政编码
     */
    private Integer zipCode;

    /**
     * 传真电话、邮箱
     */
    private String fax;

    /**
     * 评审年度
     */
    private String years;

    /**
     * 在届等级
     */
    private Long grade;

    /**
     * 申报类型
     */
    private Long applytype;

    /**
     * 附件id
     */
    private Long fileId;

    /**
     * 获得市级以上荣誉、奖励
     */
    private String rewardInfo;

    /**
     * 单位基本信息介绍
     */
    private String basicInfo;

    /**
     * 审核状态
     */
    private Integer auditStatus;

    /**
     * 审核时间
     */
    private Date auditTime;

    /**
     *同床单位id
     */
    private String tcids;

    private static final long serialVersionUID = 1L;
}