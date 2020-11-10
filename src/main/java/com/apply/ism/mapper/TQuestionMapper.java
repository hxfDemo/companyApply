package com.apply.ism.mapper;

import com.apply.ism.entity.TQuestion;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.springframework.stereotype.Repository;

/**
 *
 * @author Leon
 * @date 2020-04-26
 */
@Repository
public interface TQuestionMapper extends BaseMapper<TQuestion> {
    Double sumScore(Long paperId,Long userId,Long knowtestId,String testType);

    Double sumMnScore(Long uid);
}
