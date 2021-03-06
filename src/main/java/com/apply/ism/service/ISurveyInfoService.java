package com.apply.ism.service;

import com.apply.ism.entity.SurveyInfo;
import com.baomidou.mybatisplus.extension.service.IService;
import com.google.zxing.WriterException;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public interface ISurveyInfoService extends IService<SurveyInfo> {
    String generateQRCodeImage(String code, int width, int height);

    List<Map<String, Object>> getSurveyInfoList(String dwmc, Long id);
    Integer surveyInfoByQrcode(String nd,String yzCode);
}
