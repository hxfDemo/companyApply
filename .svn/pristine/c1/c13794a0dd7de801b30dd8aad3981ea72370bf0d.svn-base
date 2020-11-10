package com.apply.ism.utils;

import com.apply.ism.entity.TSystem;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.InputStream;
import java.util.*;

/**
 * @ClassName ReadExcel
 * @Description TODO
 * @Author shuai
 * @Date 2018/8/9 17:43
 * @Version 1.0
 **/
public class ReadExcel {

	public static Map<String,List<TSystem>> addReportByExcel(InputStream inputStream,String fileName)
			throws Exception{
		String message = "Import success";

		boolean isE2007 = false;    //判断是否是excel2007格式
		if(fileName.endsWith("xlsx")){
			isE2007 = true;
		}

		int rowIndex = 0;
		int columnIndex = 0;
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

			List<CellRangeAddress> cras = ReadExcelUtil.getCombineCell(sheet);
			int count = sheet.getLastRowNum()+1;//总行数

			List<TSystem> irs = new ArrayList<>();
			List<TSystem> items = new ArrayList<>();
			List<TSystem> objs = new ArrayList<>();
			Map<String,List<TSystem>> datas = new HashMap<>();
			String sysName = ReadExcelUtil.getCellValue(sheet.getRow(0).getCell(0));
			String category = ReadExcelUtil.getCellValue(sheet.getRow(1).getCell(0));
			TSystem obj = new TSystem();
			obj.setSysName(sysName);
			obj.setCategory(category);
			objs.add(obj);
			for(int i = 3; i < count;i++){
				rowIndex = i;
				Row row = sheet.getRow(i);
				TSystem ir = new TSystem();
				String projectName = ReadExcelUtil.getCellValue(row.getCell(0)).replaceAll("\n","");
				ir.setProjectName(projectName);
				ir.setSysName(sysName);
				ir.setCategory(category);

				if(ReadExcelUtil.isMergedRegion(sheet,i,0)){
					int lastRow = ReadExcelUtil.getRowNum(cras,sheet.getRow(i).getCell(0),sheet);

					for(;i<=lastRow;i++){
						row = sheet.getRow(i);
						TSystem item = new TSystem();
						item.setProjectName(projectName);
						item.setSysName(sysName);
						item.setCategory(category);
						item.setStandard(ReadExcelUtil.getCellValue(row.getCell(1)).replaceAll("\n",""));
						item.setContents(ReadExcelUtil.getCellValue(row.getCell(2)).replaceAll("\n",""));
						item.setScoreStandard(ReadExcelUtil.getCellValue(row.getCell(3)).replaceAll("\n",""));
						item.setFraction(ReadExcelUtil.getCellValue(row.getCell(4)));
						items.add(item);
					}
					i--;
				}else{
					row = sheet.getRow(i);
					TSystem item = new TSystem();
					item.setProjectName(projectName);
					item.setSysName(sysName);
					item.setCategory(category);
					item.setStandard(ReadExcelUtil.getCellValue(row.getCell(1)).replaceAll("\n",""));
					item.setContents(ReadExcelUtil.getCellValue(row.getCell(2)).replaceAll("\n",""));
					item.setScoreStandard(ReadExcelUtil.getCellValue(row.getCell(3)).replaceAll("\n",""));
					item.setFraction(ReadExcelUtil.getCellValue(row.getCell(4)));
					items.add(item);
				}
				irs.add(ir);
			}
			datas.put("project",irs);
			datas.put("item",items);
			datas.put("objs",objs);
			return datas;

		} catch (Exception ex) {
			return null;
		}
	}

	/**
	 * 获取execl的信息
	 * @param inputStream
	 * @param fileName
	 * @return
	 */
	public static Sheet addReportByExcelNew(InputStream inputStream, String fileName) {
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

			return sheet;

		} catch (Exception ex) {
			return null;
		}
	}
}
