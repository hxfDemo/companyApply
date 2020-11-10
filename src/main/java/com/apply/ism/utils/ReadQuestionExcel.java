package com.apply.ism.utils;

import com.apply.ism.entity.TQuestion;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.InputStream;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ReadQuestionExcel {
    public static Map<String, List<TQuestion>> addReportByExcel(InputStream inputStream, String fileName){
        boolean isE2007 = false;    //判断是否是excel2007格式
        if(fileName.endsWith("xlsx")){
            isE2007 = true;
        }
        try {
            InputStream input = inputStream;  //建立输入流
            Workbook wb  = null;
            //根据文件格式(2003或者2007)来初始化
            if(isE2007){
                wb = new XSSFWorkbook(input);
            }else{
                wb = new HSSFWorkbook(input);
            }
            Sheet sheet = wb.getSheetAt(0);    //获得第一个表单
            int count = sheet.getLastRowNum()+1;//总行数
            Map<String,List<TQuestion>> datas = new HashMap<>();
            List<TQuestion> data = new ArrayList<>();
            for(int i = 1; i < count;i++){
                Row row = sheet.getRow(i);
                TQuestion question = new TQuestion();
                String score = null;
                String title = ReadExcelUtil.getCellValue(row.getCell(0));
                if(StringUtils.isEmpty(title)){
                    return null;
                }
                String optionA = ReadExcelUtil.getCellValue(row.getCell(1));
                if(StringUtils.isEmpty(optionA)){
                    return null;
                }
                String optionB = ReadExcelUtil.getCellValue(row.getCell(2));
                String optionC = ReadExcelUtil.getCellValue(row.getCell(3));
                String optionD = ReadExcelUtil.getCellValue(row.getCell(4));
                String optionE = ReadExcelUtil.getCellValue(row.getCell(5));
                String optionF = ReadExcelUtil.getCellValue(row.getCell(6));
                String optionG = ReadExcelUtil.getCellValue(row.getCell(7));
                String rightAnswer = ReadExcelUtil.getCellValue(row.getCell(8));
                score = ReadExcelUtil.getCellValue(row.getCell(9));
                String type = ReadExcelUtil.getCellValue(row.getCell(10));
                String nd = ReadExcelUtil.getCellValue(row.getCell(11));
                question.setTitle(title);
                question.setOptiona(optionA);
                question.setOptionb(optionB);
                question.setOptionc(optionC);
                question.setOptiond(optionD);
                question.setSpare1(optionE);
                question.setSpare2(optionF);
                question.setSpare3(optionG);
                question.setRightAnswer(rightAnswer);
                question.setScore(Double.valueOf(score));
                question.setType(type);
                question.setNd(nd);
                data.add(question);
            }
            datas.put("objs",data);
            return datas;
        } catch (Exception ex) {
            return null;
        }
    }
}
