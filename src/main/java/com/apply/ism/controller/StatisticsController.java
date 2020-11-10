package com.apply.ism.controller;

import com.apply.ism.common.Result;
import com.apply.ism.common.constants.RequestMappingConstant;
import com.apply.ism.common.utils.TokenUtil;
import com.apply.ism.entity.Company;
import com.apply.ism.entity.DeclareInfo;
import com.apply.ism.entity.FileMessage;
import com.apply.ism.entity.Users;
import com.apply.ism.service.*;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@RestController
@Log4j2
@Validated
@RequestMapping(value = RequestMappingConstant.SERVICE_STATISTICS)
public class StatisticsController {

    @Autowired
    private IUsersService iUsersService;

    @Autowired
    private ICompanyService iCompanyService;

    @Autowired
    private IDeclareInfoService iDeclareInfoService;

    @Autowired
    private IAnswerInfoService iAnswerInfoService;

    @Autowired
    private IFileMessageService iFileMessageService;

    /**
     * 成绩统计列表
     * @param year
     * @param company
     * @param djmc
     * @param sswmb
     * @param zctc
     * @param current
     * @param size
     * @return
     */
    @PostMapping("/statisticsPagerList")//分页单位统计
    public Result statisticsPagerList(String year, String company, Long djmc, Long sswmb, Long zctc, Integer current, Integer size){
        String role = iUsersService.getRole(TokenUtil.getUserId());
        if (role.isEmpty()){
            return Result.error(901,"获取权限失败！");
        }
        if (!role.equals("admin")){
            return Result.error(901,"无此权限！");
        }
        Map<String,Object> hs=new HashMap<>();
        PageHelper.startPage(current!=null?current:1,size!=null?size:10);
        List<Map<String,Object>> lists = iCompanyService.getStatisticsList(year,company,djmc,sswmb,zctc);
        PageInfo<Map<String, Object>> page = new PageInfo<Map<String, Object>>(lists);
        hs.put("page",lists);
        hs.put("total",page.getTotal());
        return Result.ok(hs);
    }
    @PostMapping("/statisticsList")//不分页单位统计
    public Result statisticsList(String year, String company, Long djmc, Long sswmb, Long zctc){
        String role = iUsersService.getRole(TokenUtil.getUserId());
        if (role.isEmpty()){
            return Result.error(901,"获取权限失败！");
        }
        if (!role.equals("admin")){
            return Result.error(901,"无此权限！");
        }
        HashMap<String,Object> hs=new HashMap<>();
        List<Map<String,Object>> lists = iCompanyService.getStatisticsList(year,company,djmc,sswmb,zctc);
        hs.put("info",lists);
        return Result.ok(hs);
    }
    /**
     * 单位统计

     * @param sswmb
     * @param zctc
     * @return
     */
    @PostMapping("/companyPagerStatisticsList")
    public Result companyPagerStatisticsList(String dwmc, Long sbcj, Long dqcj, Long sswmb, Integer zctc, Integer current, Integer size){
        String role = iUsersService.getRole(TokenUtil.getUserId());
        if (role.isEmpty()){
            return Result.error(901,"获取权限失败！");
        }
        if (!role.equals("admin")){
            return Result.error(901,"无此权限！");
        }
        HashMap<String,Object> hs=new HashMap<>();
        PageHelper.startPage(current!=null?current:1,size!=null?size:10);
        List<Map<String,Object>> lists = iCompanyService.getCompanyStatisticsList(dwmc,sbcj,dqcj,sswmb,zctc);
        PageInfo<Map<String, Object>> page = new PageInfo<Map<String, Object>>(lists);
        hs.put("page",page);
        return Result.ok(hs);
    }
    @PostMapping("/companyStatisticsList")
    public Result companyStatisticsList(String dwmc, Long sbcj, Long dqcj, Long sswmb, Integer zctc){
        String role = iUsersService.getRole(TokenUtil.getUserId());
        if (role.isEmpty()){
            return Result.error(901,"获取权限失败！");
        }
        if (!role.equals("admin")){
            return Result.error(901,"无此权限！");
        }
        HashMap<String,Object> hs=new HashMap<>();
        List<Map<String,Object>> lists = iCompanyService.getCompanyStatisticsList(dwmc,sbcj,dqcj,sswmb,zctc);
        hs.put("info",lists);
        return Result.ok(hs);
    }
    /**
     * 根据id获取信息
     * @param id
     * @return
     */
    @PostMapping("/companyInfo")
    public Result getCompanyInfo(@NotNull(message = "ID[companyid]不能为空") Long id){
        Long userId = TokenUtil.getUserId();
        String role = iUsersService.getRole(userId);
        if (role.isEmpty()){
            return Result.error(901,"获取权限失败！");
        }
        if (!role.equals("admin")){
            return Result.error(901,"无此权限！");
        }
        DeclareInfo declare = iDeclareInfoService.getById(id);
        if (declare == null){
            return Result.error(203,"未获取到信息！");
        }
        Map<String,Object> company = iCompanyService.getMap(new QueryWrapper<Company>().select("userid,ascription,company,nature,category,hierarchy,tontru,address,introduce,jgdmz,bzwj").eq("id",declare.getCompanyId()).eq("data_state","Normal"));
        if (company.isEmpty()){
            return Result.error(203,"获取单位信息失败！");
        }
        Users users = iUsersService.getById(userId);
        if (users == null){
            return Result.error(202,"获取用户信息失败！");
        }
        company.put("sbdj",declare.getApplytype()!=null?declare.getApplytype():"");
        company.put("name",users.getName()!=null?users.getName():"");
        company.put("email",users.getMailbox()!=null?users.getMailbox():"");
        company.put("tel",users.getTel()!=null?users.getTel():"");
        company.put("phone",users.getPhone()!=null?users.getPhone():"");
        if (company.get("jgdmz")!=null){
            FileMessage file= iFileMessageService.getById(Long.valueOf(company.get("jgdmz") + ""));
            company.put("jgdmz",file.getFilePath());
            company.put("jgdmzName",file.getFileName());
        }else {
            company.put("jgdmz","");
        }
        if (company.get("bzwj")!=null){
            FileMessage file= iFileMessageService.getById(Long.valueOf(company.get("bzwj") + ""));
            company.put("bzwj",file.getFilePath());
            company.put("bzwjName",file.getFileName());
        }else {
            company.put("bzwj","");
        }
        return Result.ok(company);
    }
}
