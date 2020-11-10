package com.apply.ism.controller;

import com.apply.ism.common.Result;
import com.apply.ism.common.constants.RequestMappingConstant;
import com.apply.ism.common.utils.TokenUtil;
import com.apply.ism.entity.Company;
import com.apply.ism.entity.DeclareInfo;
import com.apply.ism.entity.FileMessage;
import com.apply.ism.entity.Users;
import com.apply.ism.mapper.DeclareInfoMapper;
import com.apply.ism.service.ICompanyService;
import com.apply.ism.service.IDeclareInfoService;
import com.apply.ism.service.IFileMessageService;
import com.apply.ism.service.IUsersService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.api.R;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.*;

@RequiredArgsConstructor
@RestController
@Log4j2
@Validated
@RequestMapping(value = RequestMappingConstant.SERVICE_DECLARE)
public class DeclareController {

    @Value("${api.base.upload.path}")
    private String filePath;

    private String Catalog = "/officefile/";//文件夹目录

    @Autowired
    private IDeclareInfoService iDeclareInfoService;

    @Autowired
    private ICompanyService iCompanyService;

    @Autowired
    private IFileMessageService iFileMessageService;

    @Autowired
    private IUsersService iUsersService;

    /**
     * 申报记录
     * @param current
     * @param size
     * @return
     */
    @PostMapping("/declareRecord")
    public Result declareRecord(Integer current, Integer size){
        HashMap<String,Object> hs=new HashMap<>();
        Long userid = TokenUtil.getUserId();
        String role = iUsersService.getRole(userid);
        if (role.isEmpty()){
            return Result.error(901,"获取权限失败！");
        }
        if (!role.equals("user")){
            return Result.error(901,"无此权限！");
        }
        Users user = iUsersService.getById(userid);
        if (user == null || user.getCompanyid() == null){
            return Result.fail(203,"未获单位到信息！");
        }
        PageHelper.startPage(current!=null?current:1,size!=null?size:10);
        List<Map<String,Object>> lists = iDeclareInfoService.getBaseMapper().selectMaps(new QueryWrapper<DeclareInfo>().select("id,company,years,DATE_FORMAT(create_time,'%Y-%m-%d %H:%i:%s') create_time,audit_status,DATE_FORMAT(audit_time,'%Y-%m-%d %H:%i:%s') audit_time").eq("company_id",user.getCompanyid()).eq("data_state","Normal"));
        PageInfo<Map<String, Object>> page = new PageInfo<Map<String, Object>>(lists);
        hs.put("page",page);
        return Result.ok(hs);
    }

    /**
     * 初始化单位申报页面
     * @return
     */
    @PostMapping("/initialDeclareInfo")
    public Result initialCompanyInfo(Long id,Integer current, Integer size){
        HashMap<String,Object> hs=new HashMap<>();
        Long userid = TokenUtil.getUserId();
        String role = iUsersService.getRole(userid);
        if (role.isEmpty()){
            return Result.error(901,"获取权限失败！");
        }
        if (!role.equals("user")){
            return Result.error(901,"非单位用户，获取信息失败！");
        }
        Users user = iUsersService.getById(userid);
        if (user == null || user.getCompanyid() == null){
            return Result.fail(203,"未获单位到信息！");
        }
        Map<String, Object> map = new HashMap<>();
        DeclareInfo declare = null;
        Company one = iCompanyService.getOne(new QueryWrapper<Company>().eq("id", user.getCompanyid()).eq("data_state", "Normal"));
        if (one == null || one.getTontru() == null){
            log.error("获取主同创状态失败！");
        }
        if (id!=null){
            declare = iDeclareInfoService.getById(id);
            FileMessage byId = iFileMessageService.getById(declare.getFileId() != null ? declare.getFileId() : 0);
            declare.setSpare1(byId.getFilePath());
            declare.setSpare2(byId.getFileName());
        }else {
            declare = new DeclareInfo();
            declare.setLlyName(user.getName()!=null?user.getName():"");
            declare.setTel(user.getPhone()!=null?user.getPhone():"");
            declare.setCompany(one.getCompany()!=null?one.getCompany():"");
            declare.setDwxz(one.getNature()!=null?one.getNature():0);
            declare.setAddress(one.getAddress()!=null?one.getAddress():"");
            declare.setBzrs(one.getBzrs()!=null?one.getBzrs().longValue():null);
        }
        List<Map<String, Object>> list = new ArrayList<>();
        //去掉分页查询全部同创或者主创单位信息
//        PageHelper.startPage(current!=null?current:1,size!=null?size:5);
        if (one.getTontru()==1){//是否主创：1主创，2同创
            //获取同创单位列表
            list = iCompanyService.getTontruInfo(user.getCompanyid());
        }else if (one.getTontru() == 2){
            //获取主创单位信息
            list = iCompanyService.getZcInfos(one.getZcdw());
        }
        PageInfo<Map<String, Object>> page = new PageInfo<Map<String, Object>>(list);
        hs.put("dataList",page);
        hs.put("info",declare);
        return Result.ok(hs);
    }
    /**
     * 文明单位申报
     * @param declareInfo
     * @return
     */
    @PostMapping("/addDeclareInfo")
    public Result addDeclareInfo(@ModelAttribute DeclareInfo declareInfo,@NotBlank(message = "年度[year]不能为空值") String year,String tcid){
        if (declareInfo.getDeclareLevel() == null || declareInfo.getCompany() == null || declareInfo.getDwxz() == null
                || declareInfo.getAddress() ==null || declareInfo.getBzrs() == null ||declareInfo.getAdminName() == null
                || declareInfo.getLxfs() == null || declareInfo.getDepartment() == null || declareInfo.getLlyName() == null
                || declareInfo.getTel() == null || declareInfo.getZipCode() == null || declareInfo.getFax() == null
                || declareInfo.getBasicInfo() == null || declareInfo.getFileId() == null){
            System.out.println("传入参数："+declareInfo);
            return Result.fail(202,"必要参数未填写");
        }
        Date date = new Date();
        Long userid = TokenUtil.getUserId();
        if (userid==null || userid<=0){
            return Result.fail(203,"未获取到用户ID");
        }
        String role = iUsersService.getRole(userid);
        if (role.isEmpty()){
            return Result.error(901,"获取权限失败！");
        }
        if (!role.equals("user")){
            return Result.error(901,"无此权限！");
        }
        Users user = iUsersService.getById(userid);
        if (user == null || user.getCompanyid() == null){
            return Result.fail(203,"未获单位到信息！");
        }
        Company coId = iCompanyService.getById(user.getCompanyid());
        if (coId==null){
            return Result.fail(203,"未获单位到信息！");
        }
        Long tagid = declareInfo.getId();
        if (tagid==null){
            List<DeclareInfo> declareInfos = iDeclareInfoService.getBaseMapper().selectList(new QueryWrapper<DeclareInfo>().eq("data_state", "Normal").eq("years",year).eq("company_id",user.getCompanyid()));
            if (!declareInfos.isEmpty()&&declareInfos.size()>0){
                return Result.fail(203,"不能重复申报！");
            }
        }
        if (tcid!=null&&!tcid.equals("")){
            if(coId.getTontru()==2){
                return Result.fail(201, "同创单位无需勾选主创单位！");
            }

            List<Users> list = iUsersService.getUserlists(tcid);
            if(list!=null&&list.size()>0) {
                for (Users c : list) {
                    DeclareInfo byId1 = iDeclareInfoService.getOne(new QueryWrapper<DeclareInfo>().eq("company_id", c.getCompanyid()));
                    if (byId1 != null) {
                        if (byId1.getSpare3()==null){
                            return Result.fail(201, "您所选择的名称：" + byId1.getCompany() + " 单位已经提交申报，不能重复申报！");
                        }
                    }
                }
            }
            declareInfo.setYears(year);
            declareInfo.setUserid(userid);
            declareInfo.setAuditStatus(0);//设置默认未审核
            declareInfo.setCompanyId(user.getCompanyid());
            declareInfo.setTcids(tcid);
            boolean save = false;
            if (tagid!=null){
                declareInfo.setSpare1(null);//因跳转修改时暂用该字段，保存时需要清除
                declareInfo.setSpare2(null);//因跳转修改时暂用该字段，保存时需要清除
                declareInfo.setUpdateTime(date);
                save = iDeclareInfoService.updateById(declareInfo);
            }else {
                declareInfo.setCreateTime(date);
                save = iDeclareInfoService.save(declareInfo);
            }
            if (!save){
                return Result.fail(201,"保存失败！");
            }
            Long dwid = declareInfo.getId();
            List<DeclareInfo> spare3 = iDeclareInfoService.getBaseMapper().selectList(new QueryWrapper<DeclareInfo>().eq("spare3", tagid));
            if (spare3!=null&&spare3.size()>0){
                for (DeclareInfo dif:spare3) {
                    iDeclareInfoService.getBaseMapper().deleteById(dif);
                }
            }
            for (Users l:list){
                Company byId = iCompanyService.getById(l.getCompanyid());
                    DeclareInfo df = new DeclareInfo();
                    df = declareInfo;
                    df.setTcids(null);
                    df.setSpare3(dwid.toString());
                    df.setCreateTime(date);
                    df.setCompany(byId.getCompany());
                    df.setUserid(l.getId());
                    df.setCompanyId(byId.getId());
                    iDeclareInfoService.save(df);
            }
        }else {
            List<DeclareInfo> spare3 = iDeclareInfoService.getBaseMapper().selectList(new QueryWrapper<DeclareInfo>().eq("spare3", tagid));
            if (spare3!=null&&spare3.size()>0){
                for (DeclareInfo dif:spare3) {
                    iDeclareInfoService.getBaseMapper().deleteById(dif);
                }
            }
            declareInfo.setYears(year);
            declareInfo.setUserid(userid);
            declareInfo.setAuditStatus(0);//设置默认未审核
            declareInfo.setCompanyId(user.getCompanyid());
            declareInfo.setTcids(tcid);
            boolean save = false;
            if (tagid!=null){
                declareInfo.setSpare1(null);//因跳转修改时暂用该字段，保存时需要清除
                declareInfo.setSpare2(null);//因跳转修改时暂用该字段，保存时需要清除
                declareInfo.setUpdateTime(date);
                declareInfo.setSpare3(null);
                save = iDeclareInfoService.updateById(declareInfo);
            }else {
                declareInfo.setCreateTime(date);
                save = iDeclareInfoService.save(declareInfo);
            }
            if (!save){
                return Result.fail(201,"保存失败！");
            }
        }
        return Result.ok("保存成功！");
    }

    /**
     * 申报等级审核列表
     * @param dwmc
     * @param sswmb
     * @param shzt
     * @param dqcj
     * @param sbcj
     * @param current
     * @param size
     * @return
     */
    @PostMapping("/declareList")
    public Result declareList(String dwmc, Long sswmb, Long shzt, Long dqcj, Long sbcj,String sort_time, String sort_shzt, Integer current, Integer size){
        HashMap<String,Object> hs=new HashMap<>();
        PageHelper.startPage(current!=null?current:1,size!=null?size:10);
        List<Map<String,Object>> lists = iDeclareInfoService.declareList(dwmc.trim(),sswmb,shzt,dqcj,sbcj,sort_time==null||sort_time.equals("")?"DESC":sort_time,sort_shzt==null||sort_shzt.equals("")?"DESC":sort_shzt);
        PageInfo<Map<String, Object>> page = new PageInfo<Map<String, Object>>(lists);
        hs.put("page",page);
        return Result.ok(hs);
    }

    /**
     * 获取申报审核信息
     * @param id
     * @return
     */
    @PostMapping("/getDeclareAuditInfo")
    public Result getDeclareAuditInfo(@NotNull(message = "ID[id]不能为空") Long id, Integer current, Integer size){
        HashMap<String,Object> hs=new HashMap<>();
        Map<String, Object> declareAuditInfo = iDeclareInfoService.getDeclareAuditInfo(id);
        if (declareAuditInfo == null){
            return Result.error(203,"获取申报信息失败！");
        }
        FileMessage fileId = null;
        if (declareAuditInfo.get("file_id")!=null&&!declareAuditInfo.get("file_id").equals("")){
            fileId = iFileMessageService.getById(Long.valueOf(declareAuditInfo.get("file_id") + ""));
        }
        if (declareAuditInfo.get("tontru")==null || declareAuditInfo.get("tontru").equals("")){
            log.error("获取主同创状态失败！");
        }
        int tontru = Integer.valueOf(declareAuditInfo.get("tontru") + "");
        List<Map<String, Object>> list = new ArrayList<>();
        if (tontru==1){//是否主创：1主创，2同创
            //获取同创单位列表
            List<DeclareInfo> spare3 = iDeclareInfoService.getBaseMapper().selectList(new QueryWrapper<DeclareInfo>().eq("spare3", id));
            if(spare3!=null&&spare3.size()>0){
                String comid = "";
                for (DeclareInfo s:spare3){
                    comid+=s.getCompanyId()+",";
                }
                PageHelper.startPage(current!=null?current:1,size!=null?size:5);
                list = iCompanyService.getTontru(comid.substring(0,comid.length()-1));
            }
        }else if (tontru == 2){
            //获取主创单位信息
            PageHelper.startPage(current!=null?current:1,size!=null?size:5);
            list = iCompanyService.getZcInfos(Long.valueOf(declareAuditInfo.get("zcdw")+""));
        }
        PageInfo<Map<String, Object>> page = new PageInfo<Map<String, Object>>(list);
        declareAuditInfo.put("dataList",page);
        declareAuditInfo.put("file_id",fileId!=null?fileId.getFilePath():"");
        declareAuditInfo.put("file_Name",fileId!=null?fileId.getFileName():"");
        hs.put("info",declareAuditInfo);
        return Result.ok(hs);
    }

    /**
     * 申报审核
     * @param id
     * @param auditStatus
     * @return
     */
    @PostMapping("/declareAudit")
    public Result declareAudit(@NotNull(message = "申报ID[id]不能为空") Long id, @NotNull(message = "审核状态[auditStatus]不能为空") Integer auditStatus){
        DeclareInfo byId = iDeclareInfoService.getById(id);
        if (byId==null){
            return Result.error(203,"获取申报信息失败");
        }
        if (byId.getAuditStatus()==1){
            return Result.error(203,"审核已通过，无法重复操作！");
        }
        log.info("审核传入申报id:{}，传入审核状态：{}",id,auditStatus);
        Date date = new Date();
        byId.setAuditStatus(auditStatus);
        byId.setAuditTime(date);
        boolean b = iDeclareInfoService.updateById(byId);
        if (!b){
            return Result.error(203,"审核保存失败！");
        }
        List<DeclareInfo> spare3 = iDeclareInfoService.getBaseMapper().selectList(new QueryWrapper<DeclareInfo>().eq("spare3", byId.getId()));
        if(spare3!=null&&spare3.size()>0){
            for (DeclareInfo d:spare3){
                d.setAuditStatus(auditStatus);
                d.setAuditTime(date);
                iDeclareInfoService.updateById(d);
            }
        }
        return Result.ok("审核保存成功！");
    }

    /**
     * 删除申报
     * @param id
     * @return
     */
    @PostMapping("/declareDelete")
    public Result declareDelete(@NotNull(message = "申报ID[id]不能为空") Long id){
        DeclareInfo byId = iDeclareInfoService.getById(id);
        if (byId==null){
            return Result.error(203,"获取申报信息失败");
        }
        int b = iDeclareInfoService.getBaseMapper().deleteById(byId);
        if (b<=0){
            return Result.error(203,"删除保存失败！");
        }
        return Result.ok("删除保存成功！");
    }

    /**
     * 申报记录列表
     * @return
     *//*
    @PostMapping("/declareRecordList")
    public Result declareRecordList(){
        Long userId = TokenUtil.getUserId();
        List<Map<String, Object>> list = iDeclareInfoService.getBaseMapper().selectMaps(new QueryWrapper<DeclareInfo>());
        return Result.ok();
    }*/
}
