package com.apply.ism.service.impl;

import com.apply.ism.entity.SurveyInfo;
import com.apply.ism.mapper.SurveyInfoMapper;
import com.apply.ism.service.ISurveyInfoService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.util.Base64;
import java.util.List;
import java.util.Map;

@Service
@Log4j2
@RequiredArgsConstructor
public class SurveyInfoServiceImpl extends ServiceImpl<SurveyInfoMapper, SurveyInfo> implements ISurveyInfoService {

    @Autowired
    private SurveyInfoMapper surveyInfoMapper;

    /**
     * 生成二维码
     * @param code
     * @param width
     * @param height
     * @return
     */
    @Override
    public String generateQRCodeImage(String code, int width, int height) {
        String image=null;
        try {
            QRCodeWriter qrCodeWriter = new QRCodeWriter();
            BitMatrix bitMatrix = qrCodeWriter.encode(code, BarcodeFormat.QR_CODE,width,height);
            ByteArrayOutputStream pngOutputStream = new ByteArrayOutputStream();
            MatrixToImageWriter.writeToStream(bitMatrix, "PNG", pngOutputStream);
            byte[] pngData = pngOutputStream.toByteArray();
            image= Base64.getEncoder().encodeToString(pngData);
        }catch (WriterException e){
            e.printStackTrace();
            return null;
        }catch (IOException e){
            e.printStackTrace();
            return null;
        }
        return image;
    }

    @Override
    public List<Map<String, Object>> getSurveyInfoList(String dwmc, Long id) {
        return surveyInfoMapper.getSurveyInfoList(dwmc,id);
    }

    @Override
    public Integer surveyInfoByQrcode(String nd, String yzCode) {
        return surveyInfoMapper.surveyInfoByQrcode(nd,yzCode);
    }
}
