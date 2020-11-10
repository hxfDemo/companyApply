package com.apply.ism.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.apply.ism.common.Result;
import com.apply.ism.common.constants.TimeConstants;
import com.apply.ism.common.utils.TimeUtils;
import com.apply.ism.common.utils.TokenUtil;
import com.apply.ism.entity.*;
import com.apply.ism.mapper.*;
import com.apply.ism.service.IUsersService;
import com.apply.ism.utils.ToolUtil;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.*;

@RequiredArgsConstructor
@RestController
@Log4j2
@Validated
@RequestMapping("/knowtest")
public class TKnowledgeTestController {
    @Autowired
    private TKnowledgeTestMapper knowledgeTestMapper;

    @Autowired
    private TPaperMapper paperMapper;


    @Autowired
    private CompanyMapper companyMapper;

    @Autowired
    private TPaperController tPaperController;

    @Autowired
    private UsersMapper usersMapper;
    @Autowired
    private IUsersService iUsersService;

    @Autowired
    private TUserTestMapper userTestMapper;

    /**
     * 测验列表、考试列表
     *
     * @param nd       年份
     * @param testName 测验名称
     *        datetime  查询时间
     * @return
     */
    @PostMapping("/getList")
    public Result getList(String nd, String testName ,String datetime) {
        try {
            Map<String, Object> colmnMap = new HashMap<>();
            List<Map<String, Object>> data = new ArrayList<>();
            if (StringUtils.isNotBlank(nd)) {
                colmnMap.put("nd", nd);
            }
            if (StringUtils.isNotBlank(testName)) {
                colmnMap.put("test_name", testName);
            }
            Users users = usersMapper.selectOne(new QueryWrapper<Users>().eq("data_state", "Normal").eq("id", TokenUtil.getUserId()));
            if (null == users) {
                return Result.error(902, "当前账号已失效，请重新登陆或联系管理员");
            }
            //如果用户角色是考生或者单位，那么用户只能看到单位对应的考试信息
            if ("student".equals(users.getRole()) || "user".equals(users.getRole())) {
                colmnMap.put("company_id", users.getCompanyid());
            }
//            data = knowledgeTestMapper.testLits(colmnMap);

            data = knowledgeTestMapper.testList(nd, testName, users.getCompanyid());
            for (Map<String, Object> datum : data) {
                String mnkstime = (String) datum.get("mnkstime");
                String zskstime = (String) datum.get("zskstime");
                Long companyId = (Long) datum.get("companyId");

                if (null!= zskstime&&zskstime!=""){
                    if (ToolUtil.getdiffDate(zskstime) >20){
                        TKnowledgeTest tKnowledgeTest = new TKnowledgeTest();
                        Company company = new Company();
                        String testType = (String) datum.get("testType");
                        if ("正式".equals(testType)){
                            tKnowledgeTest.setId((Long) datum.get("qid"));
                            tKnowledgeTest.setZsks(0);
//                            tKnowledgeTest.setSpare3("");
                            company.setZsks(0);
                            company.setId(Long.valueOf(companyId));
//                            company.setSpare3("");
                            companyMapper.updateById(company);
                            knowledgeTestMapper.updateById(tKnowledgeTest);
                        }
                    }
                }

                if (null!=mnkstime&&mnkstime!=""){
                    if (ToolUtil.getdiffDate(mnkstime) >20){
                        TKnowledgeTest tKnowledgeTest = new TKnowledgeTest();
                        Company company = new Company();
                        String testType = (String) datum.get("testType");
                        if ("模拟".equals(testType)){
                            tKnowledgeTest.setId((Long)  datum.get("qid"));
                            tKnowledgeTest.setMnks(0);
//                            tKnowledgeTest.setSpare2("");
                            company.setMnks(0);
                            company.setId(Long.valueOf(companyId));
//                            company.setSpare3("");
                            companyMapper.updateById(company);
                            knowledgeTestMapper.updateById(tKnowledgeTest);
                        }
                    }
                }
            }
            JSONObject jsonObject = new JSONObject();

            if (null == data && data.size() == 0) {
                return Result.error(902, "获取信息失败！");
            }
            jsonObject.put("data", data);
            return Result.ok(jsonObject);
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error(901, "获取信息失败！");
        }
    }

    @PostMapping("/getPagerList")
    public Result getPagerList(String nd, String testName, Integer current, Integer size) {
        try {
            Map<String, Object> colmnMap = new HashMap<>();
            List<Map<String, Object>> data = new ArrayList<>();
            if (StringUtils.isNotBlank(nd)) {
                colmnMap.put("nd", nd);
            }
            if (StringUtils.isNotBlank(testName)) {
                colmnMap.put("test_name", testName);
            }
            Users users = usersMapper.selectOne(new QueryWrapper<Users>().eq("data_state", "Normal").eq("id", TokenUtil.getUserId()));
            if (null == users) {
                return Result.error(902, "当前账号已失效，请重新登陆或联系管理员");
            }
            //如果用户角色是考生或者单位，那么用户只能看到单位对应的考试信息
            if ("student".equals(users.getRole()) || "user".equals(users.getRole())) {
                colmnMap.put("company_id", users.getCompanyid());
            }
//            data = knowledgeTestMapper.testLits(colmnMap);
            PageHelper.startPage(current != null ? current : 1, size != null ? size : 10);
            data = knowledgeTestMapper.testList(nd, testName, users.getCompanyid());
            PageInfo<Map<String, Object>> page = new PageInfo<Map<String, Object>>(data);
            if (null == data && data.size() == 0) {
                return Result.error(902, "获取信息失败！");
            }
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("data", data);
            jsonObject.put("total", page.getTotal());
            return Result.ok(jsonObject);
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error(901, "获取信息失败！");
        }
    }

    /**
     * 新增测验
     *
     * @param testName
     * @param nd
     * @param testNode
     * @param companyId
     * @param manNum
     * @param startTime
     * @param endTime
     * @param testTime  test_count  模拟次数
     *                  pare1      生成试卷密码
     *                  type 正式 模拟
     * @return
     */

    @PostMapping("/insert")
    public Result insert(@NotBlank(message = "测验名字不能为空") String testName, @NotBlank(message = "年份不能为空") String nd, String testNode,
                         String companyId, @NotNull Integer manNum, String startTime,
                         String endTime, @NotBlank String testTime, String test_count, String spare1, String type) {
        try {
            Users users = usersMapper.selectOne(new QueryWrapper<Users>().eq("data_state", "Normal").eq("id", TokenUtil.getUserId()));
            if (null == users) {
                return Result.error(902, "当前账号已失效，请重新登陆或联系管理员");
            }
            String[] params = companyId.split(",");
//            List<String> params = JSONArray.parseArray(companyId,String.class);
            for (String com : params) {
                Calendar c1 = Calendar.getInstance();   //当前日期
                Calendar c2 = Calendar.getInstance();
                c1.setTime(TimeUtils.TimeFormat(startTime, TimeConstants.PATTERN_YYYY_MM_DD_HH_MM_SS));
                c2.setTime(TimeUtils.TimeFormat(endTime, TimeConstants.PATTERN_YYYY_MM_DD_HH_MM_SS));
                Long times = Integer.valueOf(testTime) * 60 * 1000l;
                if ((c2.getTimeInMillis() - c1.getTimeInMillis()) < times) {
                    return Result.error(903, "设置的考试开始时间和考试结束时间的时间差小于考试总时长");
                }
                TKnowledgeTest knowledgeTest = new TKnowledgeTest();
                String time = TimeUtils.dateToString(new Date(), TimeConstants.PATTERN_YYYYMMDDHHMMSS);
                String testnum = "EM" + time + (int) ((Math.random() * 9 + 1) * 100000);
                knowledgeTest.setTestName(testName);
                knowledgeTest.setNd(nd);
                knowledgeTest.setCompanyId(Long.valueOf(com));
                knowledgeTest.setManNum(manNum);
                knowledgeTest.setStartTime(TimeUtils.TimeFormat(startTime, TimeConstants.PATTERN_YYYY_MM_DD_HH_MM_SS));
                knowledgeTest.setEndTime(TimeUtils.TimeFormat(endTime, TimeConstants.PATTERN_YYYY_MM_DD_HH_MM_SS));
                knowledgeTest.setTestTime(testTime);
                if (StringUtils.isNotBlank(test_count)) {//设置模拟次数
                    knowledgeTest.setTestCount(Integer.parseInt(test_count));
                    knowledgeTest.setRemarks(test_count);//设置不变的模拟次数
                }

                if (StringUtils.isNotBlank(spare1)) {//设置密码
                    knowledgeTest.setSpare1(spare1);
                }
                knowledgeTest.setTestNum(testnum);
                if (StringUtils.isNotBlank(testNode)) {
                    knowledgeTest.setTestNode(testNode);
                }
                knowledgeTest.setTestType(type);
                knowledgeTest.setCreateId(TokenUtil.getUserId());
                knowledgeTest.setCreateTime(new Date());
                int insert = knowledgeTestMapper.insert(knowledgeTest);
//                System.out.println("knowledgeTest"+knowledgeTest.getId());
                if ("正式".equals(type)) {
                    tPaperController.createPaper(knowledgeTest.getId() + "");
                }
                if (insert <= 0) {
                    return Result.error(902, "添加测验失败");
                }
            }
            return Result.ok("添加测验成功");
        } catch (Exception e) {
            return Result.error(901, e.getMessage());
        }
    }

    /**
     * 新增或编辑测验信息
     *
     * @param id        qid
     * @param testName  生成测试的名字
     * @param nd        年度
     * @param testNode  说明
     * @param companyId 公司id
     * @param manNum    考生人数 考生数
     * @param startTime 考生开始试卷
     * @param endTime   结束时间
     * @param testTime  考试总时长
     *                  test_count  模拟次数
     *                  spare1      生成试卷密码
     *                  type 正式 模拟
     * @return
     */
    @PostMapping("/insertOrupdate")
    public Result insertOrupdate(Long id, @NotBlank(message = "测验名字不能为空") String testName, @NotBlank(message = "年份不能为空") String nd, String testNode,
                                 @NotNull Long companyId, @NotNull Integer manNum,
                                 String startTime,
                                 String endTime, @NotBlank String testTime, String test_count, String spare1, String type) {
        try {
            Users users = usersMapper.selectOne(new QueryWrapper<Users>().eq("data_state", "Normal").eq("id", TokenUtil.getUserId()));
            if (null == users) {
                return Result.error(902, "当前账号已失效，请重新登陆或联系管理员");
            }
            Calendar c1 = Calendar.getInstance();   //当前日期
            Calendar c2 = Calendar.getInstance();
            c1.setTime(TimeUtils.TimeFormat(startTime, TimeConstants.PATTERN_YYYY_MM_DD_HH_MM_SS));
            c2.setTime(TimeUtils.TimeFormat(endTime, TimeConstants.PATTERN_YYYY_MM_DD_HH_MM_SS));
            Long times = Integer.valueOf(testTime) * 60 * 1000l;
            if ((c2.getTimeInMillis() - c1.getTimeInMillis()) < times) {
                return Result.error(903, "设置的考试开始时间和考试结束时间的时间差小于考试总时长");
            }
            TKnowledgeTest knowledgeTest = new TKnowledgeTest();
            String time = TimeUtils.dateToString(new Date(), TimeConstants.PATTERN_YYYYMMDDHHMMSS);
            String testnum = "EM" + time + (int) ((Math.random() * 9 + 1) * 100000);
            knowledgeTest.setTestName(testName);
            knowledgeTest.setNd(nd);
            knowledgeTest.setCompanyId(companyId);
            knowledgeTest.setManNum(manNum);
            knowledgeTest.setStartTime(TimeUtils.TimeFormat(startTime, TimeConstants.PATTERN_YYYY_MM_DD_HH_MM_SS));
            knowledgeTest.setEndTime(TimeUtils.TimeFormat(endTime, TimeConstants.PATTERN_YYYY_MM_DD_HH_MM_SS));
            if (StringUtils.isNotBlank(test_count)) {//设置模拟次数
                knowledgeTest.setTestCount(Integer.parseInt(test_count));
            }
            if (StringUtils.isNotBlank(spare1)) {//生成试卷密码
                knowledgeTest.setSpare1(spare1);
            }
            knowledgeTest.setTestNum(testnum);
            if (StringUtils.isNotBlank(testNode)) {
                knowledgeTest.setTestNode(testNode);
            }
            knowledgeTest.setTestType(type);
            knowledgeTest.setCreateId(TokenUtil.getUserId());
            knowledgeTest.setCreateTime(new Date());
//                System.out.println("knowledgeTest"+knowledgeTest.getId());
            if ("正式".equals(type)) {
                tPaperController.createPaper(knowledgeTest.getId() + "");
            }
            if (null == id) {
                TKnowledgeTest tKnowledgeTests = knowledgeTestMapper.selectOne(new QueryWrapper<TKnowledgeTest>().eq("data_state", "Normal").eq("id", TokenUtil.getUserId()).eq("test_name", testName));
                if (null != tKnowledgeTests) {
                    return Result.error(902, "测验名称已存在");
                }
                knowledgeTest.setCreateId(TokenUtil.getUserId());
                knowledgeTest.setCreateTime(new Date());
                int insert = knowledgeTestMapper.insert(knowledgeTest);
                if (insert <= 0) {
                    return Result.error(902, "添加测验失败");
                }
            } else {
                knowledgeTest.setId(id);
                knowledgeTest.setUpdateId(TokenUtil.getUserId());
                knowledgeTest.setUpdateTime(new Date());
                int update = knowledgeTestMapper.updateById(knowledgeTest);
                if (update <= 0) {
                    return Result.error(902, "编辑测验失败");
                }
            }
            return Result.ok("添加或编辑测验成功");
        } catch (Exception e) {
            return Result.error(901, e.getMessage());
        }
    }



    /**
     * 删除测验
     *
     * @param id
     * @return
     */
    @PostMapping("/deleteOne")
    public Result deleteOne(@NotNull Long id) {
        try {
            Map<String, Object> colmn1Map = new HashMap<>();
            Map<String, Object> colmn2Map = new HashMap<>();
            colmn1Map.put("data_state", "Normal");
            colmn1Map.put("knowledge_test_id", id);
            List<TPaper> tPapers = paperMapper.selectByMap(colmn1Map);
            if (tPapers.size() > 0 || !tPapers.isEmpty()) {
                return Result.error(902, "请先删除测验对应的试卷");
            }
            colmn2Map.put("data_state", "Normal");
            colmn2Map.put("id", id);
            int delete = knowledgeTestMapper.deleteByMap(colmn2Map);
            if (delete <= 0) {
                return Result.error(903, "删除测验失败");
            }
            return Result.ok("删除测验成功");
        } catch (Exception e) {
            return Result.error(901, e.getMessage());
        }
    }

    /*  testName 测试名字   唯一
    type 考试类型     0 ，1   模拟   ：    正式
    删除考试数据
    */
    @PostMapping("/deleteTestData")
    public Result deleteTestAll(@NotNull String testName, Integer type) {
        try {
            if (type == 0) {
                int delete = knowledgeTestMapper.delete(new UpdateWrapper<TKnowledgeTest>().eq("test_type", "模拟").eq("test_name", testName));
                if (delete<=0){
                    return Result.error(903, "未找到模拟考试删除项");
                }
                return Result.ok("模拟考试删除成功");
            }
            int delete = knowledgeTestMapper.deleteCsData(testName);
            if (delete <= 0) {
                return Result.error(903, "删除测验失败或为找到试卷");
            }
            return Result.ok("删除测验成功");
        } catch (Exception e) {
            return Result.error(901, e.getMessage());
        }
    }

    //获取测验成绩信息
    @GetMapping("/getUserTestScore")
    public Result getUserTest() {
        try {
            Long userId = TokenUtil.getUserId();
            TUserTest userTest = userTestMapper.getUserTest(userId);
            if (userTest == null) {
                return Result.error(901, "此考生还未模拟考试");
            }
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("data", userTest);
            return Result.ok(jsonObject);
        } catch (Exception e) {
            return Result.error(901, e.getMessage());
        }

    }

    //获取生成检验数据
    //testName 测试名称
    @PostMapping("/getTestByName")
    public Result getTestByName(String testName, Integer current, Integer size) {
        try {
            Map<String, Object> map = new HashMap<>();
            PageHelper.startPage(current != null ? current : 1, size != null ? size : 10);
            List<Map<String, Object>> testByName = knowledgeTestMapper.getTestByName(testName);
            PageInfo<Map<String, Object>> page = new PageInfo<Map<String, Object>>(testByName);
            map.put("data", testByName);
            map.put("total", page.getTotal());
            if (null == testByName || testByName.size() == 0) {
                return Result.error(901, "没有单位测试数据");
            }
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("data", map);
            return Result.ok(jsonObject);
        } catch (Exception e) {
            return Result.error(901, e.getMessage());
        }

    }
    @PostMapping("/updatePwd")
    public Result updatePwd(@NotNull String testName, @NotNull String pwd) {
        try {
            Users users = usersMapper.selectOne(new QueryWrapper<Users>().eq("data_state", "Normal").eq("id", TokenUtil.getUserId()));
            if (null == users) {
                return Result.error(902, "当前账号已失效，请重新登陆或联系管理员");
            }
            if (!"admin".equals(users.getRole())){
                return Result.error(902, "无修改权限");
            }
            TKnowledgeTest tKnowledgeTest = new TKnowledgeTest();
            tKnowledgeTest.setSpare1(pwd);
            int test_name = knowledgeTestMapper.update(tKnowledgeTest, new UpdateWrapper<TKnowledgeTest>().eq("test_name", testName));
            if (test_name>0){
                return Result.ok("修改成功");
            }
            return Result.error(901, "无修改信息，请检查后再试");
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error(901, e.getMessage());

        }
    }
}
