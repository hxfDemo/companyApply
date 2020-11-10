package com.apply.ism.mapper;

import com.apply.ism.entity.MaterialInfo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 *
 * @author Leon
 * @date 2020-04-23
 */
@Repository
public interface MaterialInfoMapper extends BaseMapper<MaterialInfo> {
    List<Map<String,Object>> selectByMap(String nd,String projectName,Long ascription,String company,Long hierarchy,Long tontru,Long evaluationId);
    List<Map<String,Object>> selectFileScore(String nd, Long ascription, String company, Long hierarchy, Long tontru);
    List<Map<String,Object>> selectFileTotalScore(String nd, Long ascription, String company, Long hierarchy, Long tontru);

    MaterialInfo selectByFileId(Long fileId);
    MaterialInfo selectById(Long id);
    List<MaterialInfo> selectByEvaluationId(Long evaluationId);

    int updateScore(MaterialInfo materialInfo);

}
