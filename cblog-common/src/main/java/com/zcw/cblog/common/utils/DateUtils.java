package com.zcw.cblog.common.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @Description TODO:时间字符串转换工具类
 */
public class DateUtils {


    /**
     *日期转字符串
     * @param date Tue Jan 14 04:58:16 CST 2020
     * @param pattern 时间格式：如yyyy-MM-dd HH:mm:ss
     * @return
     */
    public static String date2String(Date date,String pattern){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
        String format = simpleDateFormat.format(date);
        return format;
    }

    /**
     * 字符串转日期
     * @param time
     * @param pattern
     * @return
     */
    public static Date string2Date(String time,String pattern) throws ParseException {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
        Date date = simpleDateFormat.parse(time);
        return date;
    }

    /**
     * 距离第二天的时间
     * @return
     */
    public static Long countDown() throws ParseException {
        //获取当前时间戳
        long startTime = new Date().getTime();
        //第二天00:00时间戳
        //先获取当前天的00:00时间戳。
        String time = date2String(new Date(), "yyyy-MM-dd");
        Date date = string2Date(time, "yyyy-MM-dd");
        //加一天时间
        long endTime = date.getTime() + 24*60*60*1000;
        return (endTime-startTime)/1000;
    }
}
