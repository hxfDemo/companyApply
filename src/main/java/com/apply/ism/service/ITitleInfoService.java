package com.apply.ism.service;

import com.apply.ism.entity.TitleInfo;
import com.baomidou.mybatisplus.extension.service.IService;
import org.apache.poi.ss.usermodel.Sheet;

import java.util.List;
import java.util.Map;

public interface ITitleInfoService extends IService<TitleInfo> {
    List<Map<String,Object>> getTitleInfo(String year,int i);

    String totalScore(String answerIds);

    boolean addSurveyInfo(Sheet sheet);
}
