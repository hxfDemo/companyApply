package com.apply.ism.service;

import com.apply.ism.entity.AnswerInfo;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;
import java.util.Map;

public interface IAnswerInfoService extends IService<AnswerInfo> {
    List<Map<String, Object>> wjdcScore(String companyid);

}
