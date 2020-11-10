package com.apply.ism.common.utils;

import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Date;

public class TimeUtils {
    /**
     * 日期格式化
     * @param time 时间
     * @param dateFrom 日期格式
     * @return
     */
    public static Date TimeFormat(String time,String dateFrom){
        Date date = null;
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(dateFrom);
            date = sdf.parse(time);
            System.out.println(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    /**
     * 日期转字符串
     * @param date
     * @param dateFrom
     * @return
     */
    public static String dateToString(Date date,String dateFrom){
        String time = null;
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(dateFrom);
            time = sdf.format(date);
        }catch (Exception e){
            e.printStackTrace();
        }
        return time;
    }

    /**
     * 获取当前时间返回指定类型
     * @return
     */
    public static String getStringDate(String dateFrom) {
        String dateString = null;
        try {
            Date currentTime = new Date();
            SimpleDateFormat formatter = new SimpleDateFormat(dateFrom);
            dateString = formatter.format(currentTime);
        }catch (Exception e){
            e.printStackTrace();
        }

        return dateString;
     }

}
