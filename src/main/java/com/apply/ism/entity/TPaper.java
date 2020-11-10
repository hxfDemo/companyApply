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
 * @date 2020-04-27
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("t_paper")
public class TPaper extends BaseEntity implements Serializable {
    private Long createId;

    private Date createTime;

    private Long updateId;

    private Date updateTime;

    private String dataState;

    private Integer sort;

    private Long versionNo;

    /**
     * 分数
     */
    private Double score;

    /**
     * 试卷编号
     */
    private String paperNum;

    /**
     * 试卷密码
     */
    private String paperPwd;

    private Long userId;

    private Long knowledgeTestId;

    private String remarks;

    private String spare1;

    private String spare2;

    private String spare3;

    private static final long serialVersionUID = 1L;
}
