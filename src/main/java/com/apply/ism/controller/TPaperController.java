package com.apply.ism.controller;

import com.alibaba.fastjson.JSONArray;
import com.apply.ism.common.Result;
import com.apply.ism.common.constants.TimeConstants;
import com.apply.ism.common.utils.TimeUtils;
import com.apply.ism.common.utils.TokenUtil;
import com.apply.ism.entity.*;
import com.apply.ism.mapper.*;
import com.apply.ism.service.TPaperService;
import com.apply.ism.service.TUserAnswerService;
import com.apply.ism.utils.ToolUtil;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.apache.catalina.User;
import org.apache.poi.ss.formula.functions.T;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.awt.print.Paper;
import java.text.ParseException;
import java.util.*;

import static com.apply.ism.utils.ToolUtil.getCurrentTime;

@RequiredArgsConstructor
@RestController
@Log4j2
@Validated
@RequestMapping("/paper")
public class TPaperController {

    @Autowired
    TQuestionMapper questionMapper;

    @Autowired
    private TPaperService paperService;

    @Autowired
    private TKnowledgeTestMapper knowledgeTestMapper;

    @Autowired
    private CompanyMapper companyMapper;

    @Autowired
    private TUserTestMapper userTestMapper;

    @Autowired
    private TPaperQuestionMapper paperQuestionMapper;

    @Autowired
    private UsersMapper usersMapper;

    @Autowired
    private TUserAnswerService userAnswerService;

    /**
     * 试卷管理列表
     *
     * @param current
     * @param size
     * @return
     */
    @PostMapping("/paperList")
    public Result paperList(String dwmc, Integer current, Integer size) {
        try {
            List<Map<String, Object>> data = new ArrayList<>();
            Map<String, Object> colmnMap = new HashMap<>();
            HashMap<String, Object> hs = new HashMap<>();
            PageHelper.startPage(current != null ? current : 1, size != null ? size : 10);
            List<Map<String, Object>> lists = paperService.getPaperList(dwmc);
            PageInfo<Map<String, Object>> page = new PageInfo<Map<String, Object>>(lists);
            hs.put("page", page);
            return Result.ok(hs);
        } catch (Exception e) {
            e.printStackTrace();
            log.error("获取试卷列表：" + e.getMessage());
            return Result.error(901, "获取试卷列表失败！");
        }
    }

    /**
     * 生成试卷
     *
     * @param knowledgeTestId 测验id
     * @return
     */
    @PostMapping("/createPaper")
    public Result createPaper(String knowledgeTestId) {
        try {
            String[] params = knowledgeTestId.split(",");
//            List<String> params = JSONArray.parseArray(knowledgeTestId,String.class);
            for (String test : params) {
                TPaper tPaper = new TPaper();
                Map<String, Object> singlecolumnMap = new HashMap<>();
                singlecolumnMap.put("data_state", "Normal");
                //获取所有的单选题
                singlecolumnMap.put("type", "单选题");
                List<TQuestion> singleList = questionMapper.selectByMap(singlecolumnMap);
                if (singleList.size() < 20) {
                    return Result.error(903, "题库中的单选题不足20道，请添加单选题到数据库");
                }
                //获取多选题
                singlecolumnMap.put("type", "多选题");
                List<TQuestion> moreleList = questionMapper.selectByMap(singlecolumnMap);
                if (moreleList.size() < 10) {
                    return Result.error(903, "题库中的多选题不足10道，请添加多选题到数据库");
                }
                //获取判断题
                singlecolumnMap.put("type", "判断题");
                List<TQuestion> judgeList = questionMapper.selectByMap(singlecolumnMap);
                if (judgeList.size() < 5) {
                    return Result.error(903, "题库中的判断题不足5道，请添加判断题到数据库");
                }
                //获取填空题
                singlecolumnMap.put("type", "填空题");
                List<TQuestion> tkList = questionMapper.selectByMap(singlecolumnMap);
                if (tkList.size() < 5) {
                    return Result.error(903, "题库中的填空题不足5道，请添加填空题到数据库");
                }


                TKnowledgeTest tKnowledgeTest = null;
                try {
                    tKnowledgeTest = knowledgeTestMapper.selectOne(new QueryWrapper<TKnowledgeTest>().eq("data_state", "Normal").eq("id", Long.valueOf(test)));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if (tKnowledgeTest != null) {
                    for (int i = 0; i < 20; i++) {
                        //抽取单选题     填空5道，单选20，多选10，判断5
                        List<TQuestion> singleQuestions = getRandom(singleList, 20);
                        //抽取多选题
                        List<TQuestion> moreQuestions = getRandom(moreleList, 10);
                        //抽取判断题
                        List<TQuestion> judgeQuestions = getRandom(judgeList, 5);
                        //抽取填空题
                        List<TQuestion> tkQuestions = getRandom(tkList, 5);
                        String time = TimeUtils.dateToString(new Date(), TimeConstants.PATTERN_YYYYMMDDHHMMSS);
                        String num = "EM" + time + (int) ((Math.random() * 9 + 1) * 100000);
                        String randomString = ToolUtil.getRandomString(6);
                        tPaper.setCreateId(TokenUtil.getUserId());
                        tPaper.setCreateTime(new Date());
                        tPaper.setPaperNum(num);
                        tPaper.setPaperPwd(randomString);
                        tPaper.setKnowledgeTestId(Long.valueOf(test));
                        Map<String, Object> paper = paperService.createPaper(tPaper, singleQuestions, moreQuestions, judgeQuestions, tkQuestions);
                        if (Integer.parseInt(String.valueOf(paper.get("data"))) <= 0) {
                            return Result.error(902, "生成试卷失败");
                        }
                    }
                }
            }
            return Result.ok("生成试卷成功");
        } catch (Exception e) {
            e.printStackTrace();
            log.error("生成试卷异常：" + e.getMessage());
            return Result.error(902, "生成试卷失败");
        }
    }

    @PostMapping("/deleteOne")
    public Result deleteOne(@NotNull Long id) {
        try {
            Map<String, Object> deleteOne = paperService.deleteOne(id);
            if (Integer.parseInt(String.valueOf(deleteOne.get("data"))) <= 0) {
                return Result.ok("删除失败");
            }
            return Result.ok("删除成功");
        } catch (Exception e) {
            return Result.error(901, e.getMessage());
        }
    }

    //抽取试题
    private List<TQuestion> getRandom(List<TQuestion> questionList, int n) {
        List backList = null;
        backList = new ArrayList<TQuestion>();
        Random random = new Random();
        int backSum = 0;
        if (questionList.size() >= n) {
            backSum = n;
        } else {
            backSum = questionList.size();
        }
        for (int i = 0; i < backSum; i++) {
//			随机数的范围为0-list.size()-1
            int target = random.nextInt(questionList.size());
            if (!backList.contains(questionList.get(target))){
            backList.add(questionList.get(target));
            }else {
                int target1 = random.nextInt(questionList.size());
                if (!backList.contains(questionList.get(target1))){
                    backList.add(questionList.get(target1));
                } else {
                    int target2 = random.nextInt(questionList.size());
                    if (!backList.contains(questionList.get(target2))){
                        backList.add(questionList.get(target2));
                    }else {
                        int target3 = random.nextInt(questionList.size());
                        if (!backList.contains(questionList.get(target3))){
                            backList.add(questionList.get(target3));
                        }
                    }
                }
            }
//            questionList.remove(target);
        }
        return backList;
    }

    //抽取试卷
    private TPaper getRandomPaper(List<TPaper> tPaperList, int n) {
        TPaper paper = null;
        paper = new TPaper();
        Random random = new Random();
        int backSum = 0;
        if (tPaperList.size() >= n) {
            backSum = n;
        } else {
            backSum = tPaperList.size();
        }
        for (int i = 0; i < backSum; i++) {
//			随机数的范围为0-list.size()-1
            int target = random.nextInt(tPaperList.size());
            paper.setKnowledgeTestId(tPaperList.get(target).getKnowledgeTestId());
            paper.setPaperNum(tPaperList.get(target).getPaperNum());
            paper.setId(tPaperList.get(target).getId());
            tPaperList.remove(target);
        }
        return paper;
    }

    //测验id
    @PostMapping("/getMnTotal")
    public Result getMnTotal(@NotNull Long id) {
        try {
//            int total = userTestMapper.selectCount(new QueryWrapper<TUserTest>().eq("data_state", "Normal").eq("user_id", TokenUtil.getUserId())
//                    .eq("knowledge_test_id", id).eq("test_type", "模拟"));
            TKnowledgeTest tKnowledgeTest = knowledgeTestMapper.selectById(id);
            return Result.ok(tKnowledgeTest.getTestCount()+"");
//            int testCount = tKnowledgeTest.getTestCount();
//            if (testCount >= 1) {
//                Integer i = testCount - 1;
//                tKnowledgeTest.setTestCount(i);
//                int i1 = knowledgeTestMapper.updateById(tKnowledgeTest);
//                return Result.ok(i+ "");
//            } else {
//                return Result.ok(0 + "");
//            }
        } catch (Exception e) {
            e.printStackTrace();
            log.error("模拟考试次数：" + e.getMessage());
            return Result.error(901, "获取考试次数失败!");
        }
    }

    /**
     * 模拟考试
     *
     * @param id 测验id
     * @return
     */
    @PostMapping("/addMNTest")
    public Result addMNTest(@NotNull Long id) throws ParseException {
        List<Map<String, Object>> data = new ArrayList<>();
        try {
            int total = userTestMapper.selectCount(new QueryWrapper<TUserTest>().eq("data_state", "Normal").eq("user_id", TokenUtil.getUserId())
                    .eq("knowledge_test_id", id).eq("test_type", "模拟"));

            TKnowledgeTest tKnowledgeTest = knowledgeTestMapper.selectById(id);
            String spare2 = tKnowledgeTest.getSpare2();//模拟考试开始时间
            Long aLongtime = ToolUtil.getdiffDate(spare2);
            if (aLongtime!=null) {
                if (aLongtime > 20) {
                    return Result.error(904, "当前账号考试时间已过期！");
                }
            }
            if (tKnowledgeTest.getStartTime().getTime() > System.currentTimeMillis()) {
                return Result.error(902, "该项测验未开始");
            }
            if (tKnowledgeTest.getEndTime().getTime() < System.currentTimeMillis()) {
                return Result.error(902, "该项测验已结束");
            }
//            int testCount = tKnowledgeTest.getTestCount();
            String remarks = tKnowledgeTest.getRemarks();//获取模拟次数
            if (remarks!=null&&remarks.trim().length()>0){
                if (total >Integer.parseInt(remarks) ) {
                    return Result.error(904, "当前账号模拟考试次数已用完！");
                }
            }
//            if (total > testCount) {
//                return Result.error(904, "当前账号模拟考试次数已用完！");
//            }
            Map<String, Object> singlecolumnMap = new HashMap<>();
            singlecolumnMap.put("data_state", "Normal");
            //获取所有的单选题
            singlecolumnMap.put("type", "单选题");
            List<TQuestion> singleList = questionMapper.selectByMap(singlecolumnMap);
            if (singleList.size() < 20) {
                return Result.error(903, "题库中的单选题不足20道，请添加单选题到数据库");
            }
            //获取多选题
            singlecolumnMap.put("type", "多选题");
            List<TQuestion> moreleList = questionMapper.selectByMap(singlecolumnMap);
            if (moreleList.size() < 10) {
                return Result.error(903, "题库中的多选题不足10道，请添加多选题到数据库");
            }
            //获取判断题
            singlecolumnMap.put("type", "判断题");
            List<TQuestion> judgeList = questionMapper.selectByMap(singlecolumnMap);
            if (judgeList.size() < 5) {
                return Result.error(903, "题库中的判断题不足5道，请添加判断题到数据库");
            }
            //获取填空题
            singlecolumnMap.put("type", "填空题");
            List<TQuestion> tkList = questionMapper.selectByMap(singlecolumnMap);
            if (tkList.size() < 5) {
                return Result.error(903, "题库中的填空题不足5道，请添加填空题到数据库");
            }

            //抽取单选题     填空5道，单选20，多选10，判断5
            List<TQuestion> singleQuestions = getRandom(singleList, 20);
            //抽取多选题
            List<TQuestion> moreQuestions = getRandom(moreleList, 10);
            //抽取判断题
            List<TQuestion> judgeQuestions = getRandom(judgeList, 5);
            //抽取填空题
            List<TQuestion> tkQuestions = getRandom(tkList, 5);
            singleQuestions.addAll(moreQuestions);
            singleQuestions.addAll(judgeQuestions);
            singleQuestions.addAll(tkQuestions);
            List<String> list = new ArrayList<>();
            for (TQuestion question : singleQuestions) {
                Map<String, Object> map = new HashMap<>();
                List<Map<String, Object>> param = new ArrayList<>();
                Map<String, Object> aMap = new HashMap<>();
                Map<String, Object> bMap = new HashMap<>();
                Map<String, Object> cMap = new HashMap<>();
                Map<String, Object> dMap = new HashMap<>();
                Map<String, Object> eMap = new HashMap<>();
                Map<String, Object> fMap = new HashMap<>();
                Map<String, Object> gMap = new HashMap<>();

//               TQuestion question = questionMapper.selectOne(new QueryWrapper<TQuestion>().eq("data_state", "Normal").eq("id", pq.getQuestionId()));
                map.put("no", question.getId());
                map.put("subject", question.getTitle());
                map.put("totalScore", question.getScore());
                map.put("type", question.getType());
                if (question.getType().equals("单选题")) {
                    map.put("type", 1);
                } else if (question.getType().equals("多选题")) {
                    map.put("type", 2);
                } else if (question.getType().equals("判断题")) {
                    map.put("type", 3);
                } else if (question.getType().equals("填空题")) {
                    map.put("type", 4);
                }
                if (question.getType().equals("填空题")) {
//                    param.add(aMap);
                } else if (question.getType().equals("判断题")) {
                    aMap.put("no", "A");
                    aMap.put("answer", question.getOptiona());
                    bMap.put("no", "B");
                    bMap.put("answer", question.getOptionb());
                    param.add(aMap);
                    param.add(bMap);
                } else {
                    if (question.getOptiona() != null && !question.getOptiona().equals("")) {
                        aMap.put("no", "A");
                        aMap.put("answer", question.getOptiona());
                        param.add(aMap);
                    }
                    if (question.getOptionb() != null && !question.getOptionb().equals("")) {
                        bMap.put("no", "B");
                        bMap.put("answer", question.getOptionb());
                        param.add(bMap);
                    }
                    if (question.getOptionc() != null && !question.getOptionc().equals("")) {
                        cMap.put("no", "C");
                        cMap.put("answer", question.getOptionc());
                        param.add(cMap);
                    }
                    if (question.getOptiond() != null && !question.getOptiond().equals("")) {
                        dMap.put("no", "D");
                        dMap.put("answer", question.getOptiond());
                        param.add(dMap);
                    }
                    if (question.getSpare1() != null && !question.getSpare1().equals("")) {
                        eMap.put("no", "E");
                        eMap.put("answer", question.getSpare1());
                        param.add(eMap);
                    }
                    if (question.getSpare2() != null && !question.getSpare2().equals("")) {
                        fMap.put("no", "F");
                        fMap.put("answer", question.getSpare2());
                        param.add(fMap);
                    }
                    if (question.getSpare3() != null && !question.getSpare3().equals("")) {
                        gMap.put("no", "G");
                        gMap.put("answer", question.getSpare3());
                        param.add(gMap);
                    }
                }
                map.put("answers", param);
                map.put("examineAnswer", list);
                map.put("correctAnswer", "");
                map.put("answerAnalysis", "");
                map.put("paperId", 0);
                Users users = usersMapper.selectById(TokenUtil.getUserId());
                String mntime = users.getMntime();
                if (null==mntime||mntime.length()==0){
                    map.put("testTime", tKnowledgeTest.getTestTime());
                    map.put("fztime", Long.valueOf(tKnowledgeTest.getTestTime()).intValue()*60);
                    users.setId(TokenUtil.getUserId());
                    users.setMntime(ToolUtil.getCurrentTime(null,new Date()));
                    usersMapper.updateById(users);
                } else {
                   Long aLong = ToolUtil.getdiffTime(mntime);//获取秒
                    String testTime = tKnowledgeTest.getTestTime();
                    Long aLong1 = Long.valueOf(testTime);
                    int fztime = aLong1.intValue()*60 - aLong.intValue();
                    map.put("testTime", tKnowledgeTest.getTestTime());
                    map.put("fztime", fztime);
                }
//                map.put("testTime", Long.valueOf(tKnowledgeTest.getTestTime()).intValue()*60);
//                map.put("testTime", Long.valueOf(100));
                map.put("endTime", ToolUtil.getSdftime());
                data.add(map);
            }
            return Result.ok(data);
        } catch (Exception e) {
            e.printStackTrace();
            log.error("模拟考试获取试卷信息：" + e.getMessage());
            return Result.error(901, "获取试卷信息失败!");
        }
    }

    /**
     * 密码校验
     *
     * @param id      测验id
     * @param testPwd 试卷密码
     * @return
     */
    @PostMapping("/pwdTest")
    public Result pwdTest(@NotNull Long id, @NotBlank String testPwd) {
        try {
            TKnowledgeTest tKnowledgeTest = knowledgeTestMapper.selectOne(new QueryWrapper<TKnowledgeTest>().eq("data_state", "Normal").eq("spare1", testPwd).eq("id", id));
            if (null == tKnowledgeTest) {
                return Result.error(902, "密码不正确");
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.error("试卷密码错误：" + e.getMessage());
            return Result.error(901, "系统维护中!");
        }
        return Result.ok("密码校验通过！");
    }

    /**
     * 正式考试
     *
     * @param id 测验id
     * @return
     */
    @PostMapping("/addZSTest")
    public Result addZSTest(@NotNull Long id) {
        List<Map<String, Object>> data = new ArrayList<>();
        try {
            Users users = usersMapper.selectOne(new QueryWrapper<Users>().eq("data_state", "Normal").eq("id", TokenUtil.getUserId()));
            if (null == users) {
                return Result.error(902, "当前账号已失效，请重新登陆或联系管理员");
            }
            List<TPaper> paperList = paperService.list(new QueryWrapper<TPaper>().eq("data_state", "Normal").eq("knowledge_test_id", id));
            if (null == paperList || paperList.isEmpty() || paperList.size() <= 0) {
                return Result.error(904, "该测验没有对应的试卷");
            }

            String spare2 = users.getSpare2();//考试科目
            String spare3 = users.getSpare3();//考试开始时间

            if (null != paperList && !paperList.isEmpty() && paperList.size() > 0) {
                if (spare2 != null && spare2.trim().length()>0&&spare3!=null&&spare3.trim().length()>0) {
                    data = zsTestData(id + "", spare2, spare3);
                    return Result.ok(data);
                }
            }
            TKnowledgeTest tKnowledgeTest = knowledgeTestMapper.selectById(id);
            String kstime = tKnowledgeTest.getSpare3();//模拟考试开始时间
            Long aLongtime = ToolUtil.getdiffDate(kstime);
            if (aLongtime!=null){
                if (aLongtime > 20) {
                    return Result.error(904, "当前账号考试时间已过期！");
                }
            }
            TKnowledgeTest knowledgeTest = knowledgeTestMapper.selectOne(new QueryWrapper<TKnowledgeTest>().eq("data_state", "Normal").eq("id", id));
            if (users.getCompanyid().intValue() != knowledgeTest.getCompanyId().intValue()) {
                return Result.error(902, "用户单位和测验对应的单位不匹配，当前账号不具有参加该项测验的资质");
            }

            int total = userTestMapper.selectCount(new QueryWrapper<TUserTest>().eq("data_state", "Normal").eq("user_id", TokenUtil.getUserId())
                    .eq("knowledge_test_id", id).eq("test_type", "正式"));
            if (total > 0) {
                return Result.error(902, "当前账号已参加过该项测验");
            }
            if (knowledgeTest.getStartTime().getTime() > System.currentTimeMillis()) {
                return Result.error(902, "该项测验未开始");
            }
            if (knowledgeTest.getEndTime().getTime() < System.currentTimeMillis()) {
                return Result.error(902, "该项测验已结束");
            }

            TPaper randomPaper = getRandomPaper(paperList, 1);
            data = zsTestData(id + "", randomPaper.getId() + "", null);
            users.setSpare2(randomPaper.getId() + "");//试卷id
            users.setSpare3(ToolUtil.getCurrentTime(null, new Date()));//保存生成试卷时间
            usersMapper.updateById(users);
            if (null == data) {
                return Result.error(901, "获取试卷信息为空!");
            }
            return Result.ok(data);
        } catch (Exception e) {
            e.printStackTrace();
            log.error("正式考试获取试卷信息：" + e.getMessage());
            return Result.error(901, "获取试卷信息失败!");
        }
    }

    private List<Map<String, Object>> zsTestData(String cyId, String pagerId, String kskssj) {
        try {
            List<Map<String, Object>> data = new ArrayList<>();
            List<TPaperQuestion> tPaperQuestions = paperQuestionMapper.selectList(new QueryWrapper<TPaperQuestion>().eq("data_state", "Normal").eq("paper_id", pagerId));
            TKnowledgeTest knowledgeTest = knowledgeTestMapper.selectOne(new QueryWrapper<TKnowledgeTest>().eq("data_state", "Normal").eq("id", cyId));
            List<String> list = new ArrayList<>();
            for (TPaperQuestion pq : tPaperQuestions) {
                Map<String, Object> map = new HashMap<>();
                List<Map<String, Object>> param = new ArrayList<>();
                Map<String, Object> aMap = new HashMap<>();
                Map<String, Object> bMap = new HashMap<>();
                Map<String, Object> cMap = new HashMap<>();
                Map<String, Object> dMap = new HashMap<>();
                Map<String, Object> eMap = new HashMap<>();
                Map<String, Object> fMap = new HashMap<>();
                Map<String, Object> gMap = new HashMap<>();
                TQuestion question = questionMapper.selectOne(new QueryWrapper<TQuestion>().eq("data_state", "Normal").eq("id", pq.getQuestionId()));
                map.put("no", question.getId());
                map.put("subject", question.getTitle());
                map.put("totalScore", question.getScore());
                map.put("type", question.getType());
                if (question.getType().equals("单选题")) {
                    map.put("type", 1);
                } else if (question.getType().equals("多选题")) {
                    map.put("type", 2);
                } else if (question.getType().equals("判断题")) {
                    map.put("type", 3);
                } else if (question.getType().equals("填空题")) {
                    map.put("type", 4);
                }
                if (question.getType().equals("填空题")) {
//                    param.add(aMap);
                } else if (question.getType().equals("判断题")) {
                    aMap.put("no", "A");
                    aMap.put("answer", question.getOptiona());
                    bMap.put("no", "B");
                    bMap.put("answer", question.getOptionb());
                    param.add(aMap);
                    param.add(bMap);
                } else {
                    if (question.getOptiona() != null && !question.getOptiona().equals("")) {
                        aMap.put("no", "A");
                        aMap.put("answer", question.getOptiona());
                        param.add(aMap);
                    }
                    if (question.getOptionb() != null && !question.getOptionb().equals("")) {
                        bMap.put("no", "B");
                        bMap.put("answer", question.getOptionb());
                        param.add(bMap);
                    }
                    if (question.getOptionc() != null && !question.getOptionc().equals("")) {
                        cMap.put("no", "C");
                        cMap.put("answer", question.getOptionc());
                        param.add(cMap);
                    }
                    if (question.getOptiond() != null && !question.getOptiond().equals("")) {
                        dMap.put("no", "D");
                        dMap.put("answer", question.getOptiond());
                        param.add(dMap);
                    }
                    if (question.getSpare1() != null && !question.getSpare1().equals("")) {
                        eMap.put("no", "E");
                        eMap.put("answer", question.getSpare1());
                        param.add(eMap);
                    }
                    if (question.getSpare2() != null && !question.getSpare2().equals("")) {
                        fMap.put("no", "F");
                        fMap.put("answer", question.getSpare2());
                        param.add(fMap);
                    }
                    if (question.getSpare3() != null && !question.getSpare3().equals("")) {
                        gMap.put("no", "G");
                        gMap.put("answer", question.getSpare3());
                        param.add(gMap);
                    }
                }
                map.put("answers", param);
                map.put("examineAnswer", list);
                map.put("correctAnswer", "");
                map.put("answerAnalysis", "");
                map.put("paperId", pagerId);
                if (null == kskssj) {
                    map.put("testTime", knowledgeTest.getTestTime());
                    map.put("fztime", Long.valueOf(knowledgeTest.getTestTime()).intValue()*60);
                } else {
                    Long aLong = ToolUtil.getdiffTime(kskssj);//获取秒
                    String testTime = knowledgeTest.getTestTime();

                    Long aLong1 = Long.valueOf(testTime);
                    int fztime = aLong1.intValue()*60 - aLong.intValue();
                    map.put("testTime", knowledgeTest.getTestTime());
                    map.put("fztime", fztime);
                }
                map.put("endTime", TimeUtils.dateToString(knowledgeTest.getEndTime(), TimeConstants.PATTERN_YYYY_MM_DD_HH_MM_SS));
                data.add(map);
            }
            return data;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 提交答案
     *
     * @param
     * @return
     */
    @PostMapping("/submitTest")
    public Result submitTest(@NotBlank String params, @NotBlank String testType, @NotNull Long knowtestId, @NotNull Long paperId) {
        try {
            List<TUserAnswer> userAnswers = JSONArray.parseArray(params, TUserAnswer.class);
            Long uid = TokenUtil.getUserId();
            TUserTest userTest = new TUserTest();
            userTest.setTestType(testType);
            userTest.setKnowledgeTestId(knowtestId);
            userTest.setPaperId(paperId);
            userTest.setUserId(TokenUtil.getUserId());
            userTest.setCreateId(TokenUtil.getUserId());
            userTest.setCreateTime(new Date());
            Map<String, Object> map = null;
            if ("正式".equals(testType)) {
                map = userAnswerService.insertOne(userAnswers, userTest, uid);
                Users users = new Users();
                users.setId(uid);
                users.setSpare2("");
                users.setSpare3("");
                usersMapper.updateById(users);
            } else {
                map = userAnswerService.insertMnOne(userAnswers, userTest, uid);
                Users users = new Users();
                users.setId(uid);
                users.setUpdateTime(new Date());
                users.setMntime("");
                usersMapper.updateById(users);
            }
            return Result.ok(map);
        } catch (Exception e) {
            e.printStackTrace();
            log.error("考试提交：" + e.getMessage());
            return Result.error(901, "提交答案失败！");
        }
    }

    /**
     * 交卷后得分以及错误提示
     *
     * @param testId
     * @return
     */
    @PostMapping("/afterTest")
    public Result afterTest(String testId, String paperId) {
        Map<String, Object> map = new HashMap<>();
        try {
            Users users = usersMapper.selectOne(new QueryWrapper<Users>().eq("data_state", "Normal").eq("id", TokenUtil.getUserId()));
            if (null == users) {
                return Result.error(902, "当前账号已失效，请重新登陆或联系管理员");
            }
            TUserTest userTest = userTestMapper.selectOne(new QueryWrapper<TUserTest>().eq("knowledge_test_id", testId)
                    .eq("paper_id", paperId).eq("user_id", TokenUtil.getUserId()).orderByDesc("create_time").last("limit 0,1"));
            map.put("score", userTest.getRemarks());
            return Result.ok(map);
        } catch (Exception e) {
            e.printStackTrace();
            log.error("考试得分：" + e.getMessage());
            return Result.error(901, "交卷后获取考试得分失败失败！");
        }
    }

    //获取正式成绩和单位信息
    @PostMapping("/testScore")
    public Result TestScore(String dwmc, Integer current, Integer size) {
        try {
            Map<String, Object> map = new HashMap<>();
            Users users = usersMapper.selectOne(new QueryWrapper<Users>().eq("data_state", "Normal").eq("id", TokenUtil.getUserId()));
            if (null == users) {
                return Result.error(902, "当前账号已失效，请重新登陆或联系管理员");
            }
            PageHelper.startPage(current != null ? current : 1, size != null ? size : 10);
            List<Map<String, Object>> list = paperService.testListScore(dwmc);
            PageInfo<Map<String, Object>> page = new PageInfo<Map<String, Object>>(list);
            if (null == list || list.size() == 0) {
                return Result.error(901, "统计成绩为空！");
            }
            map.put("data", list);
            map.put("total", page.getTotal());
            return Result.ok(map);
        } catch (Exception e) {
            e.printStackTrace();
            log.error("统计成绩：" + e.getMessage());
            return Result.error(901, "统计成绩失败！");
        }
    }
}
