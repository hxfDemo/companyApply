package com.apply.ism.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.apply.ism.entity.TUserAnswer;
import com.apply.ism.entity.TUserTest;
import com.apply.ism.mapper.TQuestionMapper;
import com.apply.ism.mapper.TUserAnswerMapper;
import com.apply.ism.mapper.TUserTestMapper;
import com.apply.ism.service.TUserAnswerService;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@Log4j2
@RequiredArgsConstructor
public class TUserAnswerServiceImpl extends ServiceImpl<TUserAnswerMapper, TUserAnswer> implements TUserAnswerService {

    @Autowired
    private TUserAnswerMapper userAnswerMapper;

    @Autowired
    private TUserTestMapper userTestMapper;

    @Autowired
    private TQuestionMapper questionMapper;

    @Override
    public Map<String, Object> insertOne(List<TUserAnswer> userAnswers, TUserTest userTest,Long uid) {
        Map<String,Object> map = new HashMap<>();
        UpdateWrapper<TUserAnswer> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("user_id",uid);
        userAnswerMapper.delete(updateWrapper);
        for (TUserAnswer un:userAnswers) {
            String uesrAnswer = un.getUesrAnswer();
            String regEx="[\n`~!@#$%^&*()+=|{}':;'\\[\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。,， 、？]";
            if(uesrAnswer.contains("A")||uesrAnswer.contains("B")||uesrAnswer.contains("C")||uesrAnswer.contains("D")||uesrAnswer.contains("E")||uesrAnswer.contains("F")||uesrAnswer.contains("G")){
                String str = uesrAnswer;
                if (str.length()>1){
                    String[] split = str.split(",");
                    StringBuilder stringBuilder = new StringBuilder();
                    for (String s : split) {
                        stringBuilder.append(s);
                    }
                    String aa = "";
                    str = stringBuilder.toString().replaceAll(regEx,aa);;//不想保留原来的字符串可以直接写成 “str = str.replaceAll(regEX,aa);”
//            System.out.println(str);
                    un.setUesrAnswer(str);
                }
            }
            if (uesrAnswer.contains(" ")){//筛选填空题
                String aa = "";
                uesrAnswer = uesrAnswer.replaceAll(regEx,aa);
                un.setUesrAnswer(uesrAnswer);
            }
            un.setCreateTime(new Date());
            un.setCreateId(uid);
            un.setUserId(uid);
            int insert1 = userAnswerMapper.insert(un);
            if(insert1<=0){
                map.put("data",0);
                log.error("保存用户答案失败");
                map.put("msg","交卷失败");
                return map;
            }
        }
        int insert = userTestMapper.insert(userTest);
        if(insert<=0){
            map.put("data",0);
            log.error("保存用户和测试关联关系失败");
            map.put("msg","交卷失败");
            return map;
        }
        Double aDouble = questionMapper.sumScore(userTest.getPaperId(), uid, userTest.getKnowledgeTestId(), userTest.getTestType());
        String score = String.valueOf(aDouble);
        userTest.setRemarks(score);
        int i = userTestMapper.updateById(userTest);
//        TUserAnswer tUserAnswer = new TUserAnswer();
//        tUserAnswer.setUesrAnswer("");
//        UpdateWrapper<TUserAnswer> updateWrapper = new UpdateWrapper<>();
//        updateWrapper.eq("user_id",uid);
//        userAnswerMapper.update(tUserAnswer,updateWrapper);
        if(i<=0){
            map.put("data",0);
            log.error("保存用户考试得分失败");
            map.put("msg","交卷失败");
            return map;
        }
        map.put("data",1);
        map.put("msg","交卷成功");
        return map;
    }
    @Override
    public Map<String, Object> insertMnOne(List<TUserAnswer> userAnswers, TUserTest userTest,Long uid) {
        Map<String,Object> map = new HashMap<>();
        UpdateWrapper<TUserAnswer> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("user_id",uid);
        int delete = userAnswerMapper.delete(updateWrapper);
        System.out.println("delete"+delete);
        for (TUserAnswer un:userAnswers) {//筛选选择题
            String uesrAnswer = un.getUesrAnswer();
            String regEx="[\n`~!@#$%^&*()+=|{}':;'\\[\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’ 。,，、？]";
            if(uesrAnswer.contains("A")||uesrAnswer.contains("B")||uesrAnswer.contains("C")||uesrAnswer.contains("D")||uesrAnswer.contains("E")||uesrAnswer.contains("F")||uesrAnswer.contains("G")){
            String str = uesrAnswer;
            if (str.length()>1){
            String[] split = str.split(",");
            StringBuilder stringBuilder = new StringBuilder();
            for (String s : split) {
                stringBuilder.append(s);
            }
            String aa = "";
            str = stringBuilder.toString().replaceAll(regEx,aa);;//不想保留原来的字符串可以直接写成 “str = str.replaceAll(regEX,aa);”
            System.out.println(str);
            un.setUesrAnswer(str);
            }
            }

            if (uesrAnswer.contains(" ")){//筛选填空题
                String aa = "";
                uesrAnswer = uesrAnswer.replaceAll(regEx,aa);
                un.setUesrAnswer(uesrAnswer);
            }
            un.setCreateTime(new Date());
            un.setCreateId(uid);
            un.setUserId(uid);
            int insert1 = userAnswerMapper.insert(un);
            if(insert1<=0){
                map.put("data",0);
                log.error("保存用户答案失败");
                map.put("msg","交卷失败");
                return map;
            }
        }
        int insert = userTestMapper.insert(userTest);
        if(insert<=0){
            map.put("data",0);
            log.error("保存用户和测试关联关系失败");
            map.put("msg","交卷失败");
            return map;
        }
        Double aDouble = questionMapper.sumMnScore(uid);
        String score = String.valueOf(aDouble);
        userTest.setRemarks(score);
        int i = userTestMapper.updateById(userTest);
        TUserAnswer tUserAnswer = new TUserAnswer();
//        tUserAnswer.setUesrAnswer("");
//        UpdateWrapper<TUserAnswer> updateWrapper = new UpdateWrapper<>();
//        updateWrapper.eq("user_id",uid);
//        userAnswerMapper.update(tUserAnswer,updateWrapper);
        if(i<=0){
            map.put("data",0);
            log.error("保存用户考试得分失败");
            map.put("msg","交卷失败");
            return map;
        }
        map.put("data",1);
        map.put("msg","交卷成功");
        return map;
    }
}
