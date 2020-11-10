package com.apply.ism.mapper;

import com.apply.ism.entity.SurveyInfo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 *
 * @author Leon
 * @date 2020-04-24
 */
@Repository
public interface SurveyInfoMapper extends BaseMapper<SurveyInfo> {
    List<Map<String, Object>> getSurveyInfoList(String dwmc, Long id);
    Integer surveyInfoByQrcode(String nd,String yzCode);
}