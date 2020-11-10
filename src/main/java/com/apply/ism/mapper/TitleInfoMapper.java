package com.apply.ism.mapper;

import com.apply.ism.entity.TitleInfo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.springframework.stereotype.Repository;

/**
 *
 * @author Leon
 * @date 2020-04-24
 */
@Repository
public interface TitleInfoMapper extends BaseMapper<TitleInfo> {
    String totalScore(String answerIds);
}