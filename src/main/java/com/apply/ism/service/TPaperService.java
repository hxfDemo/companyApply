package com.apply.ism.service;

import com.apply.ism.entity.TPaper;
import com.apply.ism.entity.TQuestion;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

public interface TPaperService extends IService<TPaper> {
    @Transactional
    Map<String,Object> createPaper(TPaper paper, List<TQuestion> singleQuestions, List<TQuestion> moreQuestions, List<TQuestion> judgeQuestions, List<TQuestion> tkQuestions);
    @Transactional
    Map<String,Object> deleteOne(Long id);

    List<Map<String, Object>> getPaperList(String dwmc);

    List<Map<String, Object>> testListScore(String dwmc);
}
