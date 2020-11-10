package com.apply.ism.service.impl;

import com.apply.ism.entity.TitleInfo;
import com.apply.ism.mapper.TitleInfoMapper;
import com.apply.ism.service.ITitleInfoService;
import com.apply.ism.utils.ReadExcelUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@Log4j2
@RequiredArgsConstructor
public class TitleInfoServiceImpl extends ServiceImpl<TitleInfoMapper, TitleInfo> implements ITitleInfoService {

    private List<TitleInfo> survey_name;

    @Autowired
    private TitleInfoMapper titleInfoMapper;

    private int mark;
    /**
     * 获取问卷调查信息
     * @return
     */
    @Override
    public List<Map<String,Object>> getTitleInfo(String year,int i) {
        mark=i;
        List<TitleInfo> surveyNameInfo = titleInfoMapper.selectList(new QueryWrapper<TitleInfo>().eq("years", year).eq("data_state", "Normal"));
        List<Map<String,Object>> list = new ArrayList<>();
        if (surveyNameInfo!=null&&surveyNameInfo.size()>0){
            this.survey_name = surveyNameInfo;
            for (TitleInfo t : surveyNameInfo){
                if (t.getParentId()!=null){
                    continue;
                }
                Map<String,Object> map = new LinkedHashMap<String, Object>();
                map.put("id",t.getId());
                map.put("surveyName",t.getSurveyName());
                List<Map<String,Object>> titleList = titleInfo(t.getId());
                //对题目编号进行排序
                Collections.sort(titleList, new Comparator<Map<String, Object>>() {
                    @Override
                    public int compare(Map<String, Object> o1, Map<String, Object> o2) {
                        return Integer.valueOf(o1.get("number")+"").compareTo(Integer.valueOf(o2.get("number")+""));
                    }
                });
                map.put("titleList",titleList);
                list.add(map);
            }
        }
        return list;
    }

    /**
     * 获取问卷调查题目信息
     * @param titleid
     * @return
     */
    private List<Map<String,Object>> titleInfo(Long titleid) {
        List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();
        for (TitleInfo title : this.survey_name){
            Map<String,Object> titleMap = new LinkedHashMap<String, Object>();
            if (title.getParentId() == null){
                continue;
            }
            if (title.getParentId().longValue()!=titleid.longValue()){
                continue;
            }
            titleMap.put("titleid",title.getId());
            titleMap.put("number",title.getNumber());
            titleMap.put("title",title.getTitle());
            titleMap.put("option",optInfo(title.getId()));
            list.add(titleMap);
        }
        return list;
    }

    /**
     * 计算问卷调查当前得分
     * @param answerIds
     * @return
     */
    @Override
    public String totalScore(String answerIds) {
        return titleInfoMapper.totalScore(answerIds);
    }

    /**
     * 解析execl的问卷调查内容存入数据库
     * @param sheet
     * @return
     */
    @Override
    public boolean addSurveyInfo(Sheet sheet) {
        if (sheet == null){
            return false;
        }
        String surveyName = ReadExcelUtil.getCellValue(sheet.getRow(1).getCell(0));
        String y = ReadExcelUtil.getCellValue(sheet.getRow(1).getCell(7));
        String years=y.substring(0,y.indexOf("."));
        List<TitleInfo> yearsList = titleInfoMapper.selectList(new QueryWrapper<TitleInfo>().eq("years", years));
        if (!yearsList.isEmpty() || yearsList.size()>0){//数据库中同年度的数据是否存在
            int d = titleInfoMapper.delete(new QueryWrapper<TitleInfo>().eq("years", years));
            if (d<=0){
                return false;
            }
        }
        TitleInfo surveyInfo = new TitleInfo();
        Date date = new Date();
        surveyInfo.setCreateTime(date);
        surveyInfo.setSurveyName(surveyName);
        surveyInfo.setYears(years);
        //保存问卷调查名内容等数据
        if(titleInfoMapper.insert(surveyInfo)<=0){
            return false;
        };
        for (int i = 3; i < sheet.getLastRowNum()+1;i++){
            Row row = sheet.getRow(i);
            TitleInfo titleInfo = new TitleInfo();
            titleInfo.setCreateTime(date);
            String Number = ReadExcelUtil.getCellValue(row.getCell(0)).replaceAll("\n", "");
            titleInfo.setNumber(Integer.valueOf(Number.substring(0,Number.indexOf("."))));
            titleInfo.setTitle(ReadExcelUtil.getCellValue(row.getCell(1)).replaceAll("\n",""));
            titleInfo.setParentId(surveyInfo.getId());
            titleInfo.setYears(years);
            //保存问卷题目等数据
            if(titleInfoMapper.insert(titleInfo)<=0){
                return false;
            };
            for (int j = 1;j<=3;j++){
                TitleInfo optionInfo = new TitleInfo();
                optionInfo.setCreateTime(date);
                optionInfo.setParentId(titleInfo.getId());
                optionInfo.setOptionInfo(ReadExcelUtil.getCellValue(row.getCell(j+j)).replaceAll("\n",""));
                optionInfo.setScore(Double.valueOf(ReadExcelUtil.getCellValue(row.getCell(j+j+1)).replaceAll("\n","")));
                optionInfo.setYears(years);
                //保存问卷选项等数据
                if(titleInfoMapper.insert(optionInfo)<=0){
                    return false;
                };
            }
        }
        return true;
    }


    /**
     * 获取问卷调查题目对应选项信息
     * @param titleid 题目id
     * @return
     */
    private List<Map<String,Object>> optInfo(Long titleid) {
        List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();
        for (TitleInfo op : this.survey_name){
            Map<String,Object> opMap = new LinkedHashMap<String, Object>();
            if (op.getParentId() == null){
                continue;
            }
            if (op.getParentId().longValue()!=titleid.longValue()){
                continue;
            }
            opMap.put("parentid",op.getId());
            opMap.put("option",op.getOptionInfo());
            if (mark==1){
                opMap.put("score",op.getScore());
            }
            list.add(opMap);
        }
        return list;
    }
}
