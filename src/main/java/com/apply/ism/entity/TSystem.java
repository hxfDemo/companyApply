package com.apply.ism.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
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
@TableName("t_system")
public class TSystem extends BaseEntity implements Serializable {
    private static final long serialVersionUID = -5362696582893222401L;
    private Long createId;

    private Date createTime;

    private Long updateId;

    private Date updateTime;

    private String dataState;

    private Integer sort;

    private Long versionNo;

    private String sysName;

    /**
     * 年度
     */
    private String nd;

    private String category;

    private String projectName;

    private String standard;

    private String contents;

    private String scoreStandard;

    private String fraction;

    private Long parentId;

    private String grade;

    private String remarks;

    private String spare1;

    private String spare2;

    private String spare3;


}
