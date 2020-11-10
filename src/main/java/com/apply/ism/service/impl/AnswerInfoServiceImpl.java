package com.apply.ism.service.impl;

import com.apply.ism.entity.AnswerInfo;
import com.apply.ism.mapper.AnswerInfoMapper;
import com.apply.ism.service.IAnswerInfoService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
@Slf4j
@RequiredArgsConstructor
public class AnswerInfoServiceImpl extends ServiceImpl<AnswerInfoMapper, AnswerInfo> implements IAnswerInfoService {
    @Autowired
    private AnswerInfoMapper answerInfoMapper;
    @Override
 public List<Map<String, Object>> wjdcScore(String companyid){
        return answerInfoMapper.wjdcScore(companyid);
    };
}
