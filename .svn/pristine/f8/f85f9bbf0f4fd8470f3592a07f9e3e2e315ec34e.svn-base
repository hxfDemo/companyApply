package com.apply.ism.controller;

import com.apply.ism.common.Result;
import com.apply.ism.common.constants.RequestMappingConstant;
import com.apply.ism.common.constants.TimeConstants;
import com.apply.ism.common.utils.TimeUtils;
import com.apply.ism.common.utils.TokenUtil;
import com.apply.ism.entity.*;
import com.apply.ism.service.*;
import com.apply.ism.utils.ReadExcel;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.*;

@RequiredArgsConstructor
@RestController
@Log4j2
@Validated
@RequestMapping(value = RequestMappingConstant.SERVICE_QUESTIONNAIRE)
public class QuestionnaireController {

    private static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

    @Autowired
    private ITitleInfoService iTitleInfoService;

    @Autowired
    private ICompanyService iCompanyService;

    @Autowired
    private ISurveyInfoService iSurveyInfoService;

    @Autowired
    private IAnswerInfoService iAnswerInfoService;

    @Autowired
    private IUsersService iUsersService;

    @Value("${api.base.qrcode.url}")
    private String qrcodePath;

    @Value("${api.base.upload.path}")
    private String filePath;

    private String Catalog = "/survey/";//文件夹目录

    /**
     * 上传问卷调查execl
     * @param file
     * @return
     */
    @PostMapping("/uploadSurvey")
    public Result uploadSurvey(@NotNull(message = "上传文件[file]不能为空")MultipartFile file,String nd){
        if(file.isEmpty()){
            return Result.error(903,"上传文件为空");
        }
        try {
            String role = iUsersService.getRole(TokenUtil.getUserId());
            if (role.isEmpty()){
                return Result.error(901,"获取权限失败！");
            }
            if (!role.equals("admin")){
                return Result.error(901,"无此权限！");
            }
            File dir = new File(filePath+Catalog);
            if(!dir.exists()){
                dir.mkdir();
            }
            String filename = file.getOriginalFilename();
            File dest = new File(filePath+Catalog+filename);
            try {
                if(dest.exists()){
                    dest.delete();
                }
                file.transferTo(dest);
                InputStream is = new FileInputStream(dest);
                Sheet sheet = ReadExcel.addReportByExcelNew(is, filename);
                boolean b=iTitleInfoService.addSurveyInfo(sheet);
                if (!b){
                    return Result.error(902,"上传失败");
                }
                return Result.ok("上传成功");
            }catch (Exception e){
                return Result.error(901,e.getMessage());
            }
        }catch (Exception e){
            return Result.error(901,e.getMessage());
        }
    }


    /**
     * 根据年度获取问卷调查题目信息列表
     * @param year
     * @return
     */
    @PostMapping("/surveyInfoList")
    public Result surveyInfoList(@NotBlank(message = "年度[year]不能为空值")String year){
        String role = iUsersService.getRole(TokenUtil.getUserId());
        if (role.isEmpty()){
            return Result.error(901,"获取权限失败！");
        }
        if (!role.equals("admin")){
            return Result.error(901,"无此权限！");
        }
        List<Map<String,Object>> titleInfo=iTitleInfoService.getTitleInfo(year,1);
        if (titleInfo.isEmpty()){
            return Result.error(203,"未获取到["+year+"年]问卷调查信息列表");
        }
        return Result.ok(titleInfo);
    }

    /**
     * 生成单位的调查问卷
     * @param companyid 公司ID   type o 1 模拟  正式
     *
     * @return
     */
    @PostMapping("/createSurvey")
    public Result createSurvey(@NotNull(message = "单位ID[companyid]不能为空") String companyid,@NotBlank(message = "开始时间[startTime]不能为空值") String startTime,
                               @NotBlank(message = "结束时间[endTime]不能为空值")String endTime, @NotBlank(message = "年度[year]不能为空值") String year,@NotNull(message = "类型[type]不能为空值")Integer type){
        if (companyid == null ||companyid.length() <=0){
            return Result.error(203,"单位ID必须大于0");
        }
        String role = iUsersService.getRole(TokenUtil.getUserId());
        if (role.isEmpty()){
            return Result.error(901,"获取权限失败！");
        }
        if (!role.equals("admin")){
            return Result.error(901,"无此权限！");
        }
        String[] split = companyid.split(",");
        for (String cid : split) {

            Company company = iCompanyService.getById(cid);
            if (company == null){
                return Result.error(203,"未获取到单位信息");
            }

//        String year = String.valueOf(Calendar.getInstance().get(Calendar.YEAR));
            TitleInfo surveyNameInfo = iTitleInfoService.getOne(new QueryWrapper<TitleInfo>().eq("years", year).eq("data_state", "Normal").isNotNull("survey_name"));
            if (surveyNameInfo==null){
                return Result.error(203,"未获取到调查问卷信息");
            }
            SurveyInfo surveyInfo = new SurveyInfo();
            surveyInfo.setCreateTime(new Date());
            surveyInfo.setCompanyid(Long.valueOf(cid));
            String code=""+((int) ((Math.random() * 9 + 1) * 100000));

            surveyInfo.setYzCode(code);
            surveyInfo.setYears(year);
            surveyInfo.setSurveyName(surveyNameInfo.getSurveyName());
            surveyInfo.setStartTime(TimeUtils.TimeFormat(startTime.trim(), TimeConstants.PATTERN_YYYY_MM_DD_HH_MM_SS));
            surveyInfo.setEndTime(TimeUtils.TimeFormat(endTime.trim(), TimeConstants.PATTERN_YYYY_MM_DD_HH_MM_SS));
            surveyInfo.setSurveyType(type);//设置默认调查类型1正式
            String qrCode=iSurveyInfoService.generateQRCodeImage(qrcodePath+"?zy_code="+code,350,350);
            surveyInfo.setQrCode(qrCode);
            boolean b = iSurveyInfoService.save(surveyInfo);
            if (!b){
                return Result.error(203,"生成单位的调查问卷失败！");
            }
        }
        return Result.ok("生成单位的调查问卷成功！");
    }

    /**
     * 调查问卷列表
     * @param dwmc
     * @param current
     * @param size
     * @return
     */
    @PostMapping("/surveyList")
    public Result surveyList(String dwmc, Integer current, Integer size){
        Long userid = TokenUtil.getUserId();
        List<Map<String,Object>> lists=null;
        HashMap<String,Object> hs=new HashMap<>();
        if (userid==null){
            return Result.error(203,"获取用户信息失败！");
        };
        Users byId = iUsersService.getById(userid);
        if (byId == null){
            return Result.error(203,"获取用户信息失败！");
        }
        PageHelper.startPage(current!=null?current:1,size!=null?size:10);
        if (byId.getRole().equals("user")){
            lists=iSurveyInfoService.getSurveyInfoList(null,byId.getCompanyid());
        }else if (byId.getRole().equals("admin")){
            if (StringUtils.isNotBlank(dwmc)){
                lists = iSurveyInfoService.getSurveyInfoList(dwmc.trim(), null);
            }else {
                lists = iSurveyInfoService.getSurveyInfoList("", null);
            }
        }
        PageInfo<Map<String, Object>> page = new PageInfo<Map<String, Object>>(lists);
        hs.put("page",page);
        return Result.ok(hs);
    }

    /**
     * 获取问卷调查信息
     * @return
     */
    @PostMapping("/info/questionnaire")
    public Result Questionnaire(@NotBlank(message = "验证码[yzmCode]不能为空值") String yzmCode){
        SurveyInfo surveyInfo = iSurveyInfoService.getOne(new QueryWrapper<SurveyInfo>().eq("yz_code", yzmCode).eq("data_state","Normal"));
        if (surveyInfo == null){
            return Result.error(203,"验证码不正确获取问卷调查失败！");
        }
        Date date = new Date();
        if (surveyInfo.getStartTime().getTime()>date.getTime()){
            return Result.error(203,"问卷调查暂未开始");
        }
        if (surveyInfo.getEndTime().getTime()<date.getTime()){
            return Result.error(203,"问卷调查已结束");
        }
        Map<String, Object> titleInfoMap = new HashMap<>();
        List<Map<String,Object>> titleInfo=iTitleInfoService.getTitleInfo(surveyInfo.getYears(),0);
        if (titleInfo.size()<=0 || titleInfo.isEmpty()){
            return Result.error(203,"未获取到问卷信息");
        }
        titleInfoMap.put("titleList",titleInfo);
        titleInfoMap.put("yzmCode",yzmCode);
        return Result.ok(titleInfoMap);
    }

    /**
     * 提交问卷调查
     * @param yzmCode
     * @return
     */
    @PostMapping("/info/submitSurvey")
    public Result submitSurvey(@NotBlank(message = "验证码[yzmCode]不能为空值") String yzmCode,
                               @NotBlank(message = "选项ids不能为空[answerIds]不能为空值") String answerIds){
        SurveyInfo surveyInfo = iSurveyInfoService.getOne(new QueryWrapper<SurveyInfo>().eq("yz_code", yzmCode).eq("data_state","Normal"));
        if (surveyInfo == null){
            return Result.error(203,"验证码不正确");
        }

        Date date = new Date();
        if (surveyInfo.getStartTime().getTime()>date.getTime()){
            return Result.error(203,"提交失败，问卷调查未开始");
        }
        if (surveyInfo.getEndTime().getTime()<date.getTime()){
            return Result.error(203,"提交失败，超出提交时间");
        }

        Integer surveyType = surveyInfo.getSurveyType();
        if (surveyType==1){
            Integer integer = iSurveyInfoService.surveyInfoByQrcode(surveyInfo.getYears(), yzmCode);
            if(null!=integer&&integer>0) {
                if (integer > 25) {
                    return Result.error(203, "该项问卷调查次数已用完");
                }
            }
        String[] s=answerIds.split(",");
        System.out.println("问卷答题传入id的个数："+s.length);
        String score=iTitleInfoService.totalScore(answerIds);
        AnswerInfo answerInfo = new AnswerInfo();
        answerInfo.setYears(surveyInfo.getYears());
        answerInfo.setCreateTime(date);
        answerInfo.setCompanyid(surveyInfo.getCompanyid());
        answerInfo.setAnswerIds(answerIds);
        answerInfo.setTotalScore(score);
        boolean b = iAnswerInfoService.save(answerInfo);
        if (!b){
            return Result.error(203,"提交失败");
        }
        }
        return Result.ok("提交完成！");
    }

    /**
     * 获取申报审核通过的单位
     * @return
     */
    @PostMapping("/companyInfo")
    public Result companyInfo(){
        List<Map<String,Object>> companyInfo = iCompanyService.getCompanyInfo();
        return Result.ok(companyInfo);
    }

}
