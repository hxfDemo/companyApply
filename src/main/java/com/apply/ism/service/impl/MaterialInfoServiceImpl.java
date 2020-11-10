package com.apply.ism.service.impl;

import com.apply.ism.entity.Company;
import com.apply.ism.entity.FileMessage;
import com.apply.ism.entity.MaterialInfo;
import com.apply.ism.entity.Users;
import com.apply.ism.mapper.CompanyMapper;
import com.apply.ism.mapper.FileMessageMapper;
import com.apply.ism.mapper.MaterialInfoMapper;
import com.apply.ism.service.MaterialInfoService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
@Log4j2
@RequiredArgsConstructor
public class MaterialInfoServiceImpl extends ServiceImpl<MaterialInfoMapper, MaterialInfo> implements MaterialInfoService {

    @Autowired
    MaterialInfoMapper materialInfoMapper;

    @Autowired
    FileMessageMapper fileMessageMapper;

    @Autowired
    CompanyMapper companyMapper;

    @Override
    public int insert(MaterialInfo materialInfo) {
        return materialInfoMapper.insert(materialInfo);
    }

    @Override
    public int pdfInsert(Long evaluationId, String fileTitle, Users user, String filename, String filepath) {
       try{
           FileMessage fileMessage = new FileMessage();
           fileMessage.setCreateTime(new Date());
           fileMessage.setFileName(filename);
           fileMessage.setFilePath(filepath);
           //把文件相关信息存放到附件表
           int insert = fileMessageMapper.insert(fileMessage);
           if (insert<=0){
               log.error("保存文件信息到附件表失败！");
               return 0;
           }
           List<MaterialInfo> list = this.list(new QueryWrapper<MaterialInfo>().select("id,create_time,user_id,company_id,evaluation_id,file_id,auditstate,title").eq("user_id", user.getId()).eq("data_state", "Normal").eq("evaluation_id", evaluationId));
           if (!list.isEmpty()&&list.size()>0){
               for (MaterialInfo m :list){
                   FileMessage files = fileMessageMapper.selectById(m.getFileId());
                   if (files!=null){
                       files.setDataState("Delete");
                       fileMessageMapper.updateById(files);
                   }
                   m.setDataState("Delete");
                   int b = this.materialInfoMapper.updateById(m);
                   if (b<=0){
                       log.error("删除已存在的材料信息失败！");
                       return 0;
                   }
               }
           }
           MaterialInfo m = new MaterialInfo();
           m.setCreateTime(new Date());
           m.setUserId(user.getId());//用户id
           m.setCompanyId(user.getCompanyid());//根据用户id查询单位id
           m.setEvaluationId(evaluationId);
           m.setFileId(fileMessage.getId());
           m.setAuditstate("0");
           m.setTitle(fileTitle);
           //把上传的资料信息存放到资料表中
           int row = materialInfoMapper.insert(m);
           if (row<=0){
               log.error("保存资料信息失败！");
               return 0;
           }
           return 1;
       }catch (Exception e){
           e.printStackTrace();
           return 0;
       }
    }

    @Override
    public List<Map<String,Object>> selectByMap(String nd, String projectName, Long ascription, String company, Long hierarchy, Long tontru,Long evaluationId) {
        return materialInfoMapper.selectByMap(nd,projectName,ascription,company,hierarchy,tontru,evaluationId);
    }

    @Override
    public List<Map<String, Object>> selectFileScore(String nd, Long ascription, String company, Long hierarchy, Long tontru) {
        return materialInfoMapper.selectFileScore(nd,ascription,company,hierarchy,tontru);
    }

    @Override
    public List<Map<String, Object>> selectFileTotalScore(String nd, Long ascription, String company, Long hierarchy, Long tontru) {
        return materialInfoMapper.selectFileTotalScore(nd,ascription,company,hierarchy,tontru);
    }

    @Override
    public MaterialInfo selectByFileId(Long fileId) {
        return materialInfoMapper.selectByFileId(fileId);
    }

    @Override
    public MaterialInfo selectById(Long id) {
        return materialInfoMapper.selectById(id);
    }

    @Override
    public List<MaterialInfo> selectByEvaluationId(Long evaluationId) {
        return materialInfoMapper.selectByEvaluationId(evaluationId);
    }

    @Override
    public int updateScore(MaterialInfo materialInfo) {
        return materialInfoMapper.updateScore(materialInfo);
    }


}
