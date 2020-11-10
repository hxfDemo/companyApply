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
 * @date 2020-04-28
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("t_user_test")
public class TUserTest extends BaseEntity implements Serializable {
    private Long createId;

    private Date createTime;

    private Long updateId;

    private Date updateTime;

    private String dataState;

    private Integer sort;

    private Long versionNo;

    private Long userId;

    private Long knowledgeTestId;

    private Long paperId;

    private Integer testCount;

    private String testType;

    private String remarks;

    private String spare1;

    private String spare2;

    private String spare3;

    private static final long serialVersionUID = 1L;
}
