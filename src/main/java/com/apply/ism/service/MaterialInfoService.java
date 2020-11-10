package com.apply.ism.service;

import com.apply.ism.entity.MaterialInfo;
import com.apply.ism.entity.Users;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

public interface MaterialInfoService extends IService<MaterialInfo> {
    int insert(MaterialInfo materialInfo);
    @Transactional
    int pdfInsert(Long evaluationId, String fileTitle, Users user, String filename, String filepath);
    List<Map<String,Object>> selectByMap(String nd, String projectName, Long ascription, String company, Long hierarchy, Long tontru,Long evaluationId);
    List<Map<String,Object>> selectFileScore(String nd, Long ascription, String company, Long hierarchy, Long tontru);
    List<Map<String,Object>> selectFileTotalScore(String nd, Long ascription, String company, Long hierarchy, Long tontru);
    MaterialInfo selectByFileId(Long fileId);
    MaterialInfo selectById(Long id);

    List<MaterialInfo> selectByEvaluationId(Long evaluationId);

    int updateScore(MaterialInfo materialInfo);
}
