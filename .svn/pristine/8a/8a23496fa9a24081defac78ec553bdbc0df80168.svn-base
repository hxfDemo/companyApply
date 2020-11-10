package com.apply.ism.utils;

import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.Date;
import java.util.Random;

public class ToolUtil {
    //length用户要求产生字符串的长度
    public static String getRandomString(int length) {
//        String str="abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        String str = "0123456789";
        Random random = new Random();
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < length; i++) {
            int number = random.nextInt(str.length());
            sb.append(str.charAt(number));
        }
        return sb.toString();
    }

    //密码解码
    public static String decodepwd(String str) {
        Base64.Decoder decoder = Base64.getMimeDecoder();
        byte[] bs_1 = decoder.decode(str);
        String password1 = "";
        try {
            password1 = new String(bs_1, "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return password1;
    }

    //获取日期字符串
    public static String getCurrentTime(String def, Date date) {
        if (null == def) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            return sdf.format(date);

        } else {
            SimpleDateFormat sdf = new SimpleDateFormat(def);
            return sdf.format(date);
        }
    }
//获取分钟时间差
    public static Long getdiffDate(String createTime1) throws ParseException {
        //时间处理类
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        long currentTime = System.currentTimeMillis();
        Date date = new Date(currentTime);
        String format = df.format(date);
        System.out.println("currentTime"+format);
        System.out.println("createTime1"+createTime1);
        //从对象中拿到时间
        long createTime = df.parse(createTime1).getTime();
        long diff = (currentTime - createTime) / 1000 / 60;
//        System.out.println("当前系统时间为：" + currentTime + " 创建时间为：" + createTime + "两个时间差为：" + diff);
        return diff;
    }
//获取秒时间差
    public static Long getdiffTime(String createTime1) throws ParseException {
        //时间处理类
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        long currentTime = System.currentTimeMillis();
        Date date = new Date(currentTime);
        String format = df.format(date);
        System.out.println("currentTime"+format);
        System.out.println("createTime1"+createTime1);
        //从对象中拿到时间
        long createTime = df.parse(createTime1).getTime();
        long diff = (currentTime - createTime) / 1000;
//        System.out.println("当前系统时间为：" + currentTime + " 创建时间为：" + createTime + "两个时间差为：" + diff);
        return diff;
    }

    public static String getSdftime() {
        Date now = new Date();
        Date afterDate = new Date(now.getTime() + 6000000);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String format = sdf.format(afterDate);
        return format;
    }

}
