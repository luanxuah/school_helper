package com.luanxu.utils;

import android.text.TextUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;

/**
 * Created by 栾煦 on 2016/11/29.
 */
public class DateUtils {

    // 12(月)
    public static final String FORMAT_MM = "MM";
    // 19(日)
    public static final String FORMAT_DD = "dd";
    // 0930  (09月30日)
    public static final String FORMAT_MMDD = "MMdd";
    // 2016(年)
    public static final String FORMAT_YYYY = "yyyy";
    // 20160927194515
    public static final String FORMAT_YYYYMMDDHHMMSS = "yyyyMMddHHmmss";
    // 20160927194515558
    public static final String FORMAT_YYYYMMDDHHMMSSSSS = "yyyyMMddHHmmssSSS";
    // 20160927
    public static final String FORMAT_YYYYMMDD = "yyyyMMdd";
    // 12/12
    public static final String FORMAT_MM_SLASH_DD = "MM/dd";
    // 2016/09/27
    public static final String FORMAT_YYYY_SLASH_MM_SLASH_DD = "yyyy/MM/dd";
    // 2016/09/27 05:11
    public static final String FORMAT_YYYY_SLASH_MM_SLASH_DD_HH_MM = "yyyy/MM/dd HH:mm";
    // 09/27 05:11
    public static final String FORMAT_MM_SLASH_DD_HH_MM = "MM/dd HH:mm";
    // 09-27 05:11
    public static final String FORMAT_MM_LINE_DD_HH_MM = "MM-dd HH:mm";
    // 15:03:34
    public static final String FORMAT_HH_MM_SS = "HH:mm:ss";
    // 15:03
    public static final String FORMAT_HH_MM = "HH:mm";
    // 03:34
    public static final String FORMAT_MM_SS = "mm:ss";
    // 2016-09-27 15:03:34
    public static final String FORMAT_YYYY_LINE_MM_LINE_DD_HH_MM_SS = "yyyy-MM-dd HH:mm:ss";
    // 2016-09-27 15:03:34.876
    public static final String FORMAT_YYYY_LINE_MM_LINE_DD_HH_MM_SS_SSS = "yyyy-MM-dd HH:mm:ss.SSS";
    // 12-12
    public static final String FORMAT_MM_LINE_DD = "MM-dd";
    // 2016-09
    public static final String FORMAT_YYYY_LINE_MM = "yyyy-MM";
    // 2016-09-27
    public static final String FORMAT_YYYY_LINE_MM_LINE_DD = "yyyy-MM-dd";
    // 12月12日
    public static final String FORMAT_MM_DD = "MM月dd日";
    // 2016年09月
    public static final String FORMAT_YYYY_MM = "yyyy年MM月";
    // 2016年09月27日
    public static final String FORMAT_YYYY_MM_DD = "yyyy年MM月dd日";
    // 2016-09-27 12时19分
    public static final String FORMAT_YYYY_LINE_MM_LINE_DD_HH_MM = "yyyy-MM-dd HH时mm分";
    // yyyy年MM月dd日 HH时mm分ss秒
    public static final String FORMAT_YYYY_MM_DDHH_MM_SS_WORD_ZH = "yyyy年MM月dd日HH时mm分ss秒";
    // yyyy年MM月dd日 HH时mm分ss秒
    public static final String FORMAT_YYYY_MM_DD_HH_MM_SS_WORD_ZH = "yyyy年MM月dd日 HH时mm分ss秒";
    // 2016.09.28
    public static final String FORMAT_YYYY_DOT_MM_DOT_DD = "yyyy.MM.dd";
    // 09.28
    public static final String FORMAT_MM_DOT_DD = "MM.dd";

    /**
     * 获取本周的日期集合
     * @return 本周的日期集合（从周一到周日）
     */
    public static String[]  getNowWeekDays(){
        String[] days = new String[7];
        Calendar c = Calendar.getInstance();
        c.setTimeZone(TimeZone.getTimeZone("GMT+8:00"));
        String mWay = String.valueOf(c.get(Calendar.DAY_OF_WEEK));
        int way = Integer.parseInt(mWay)-1;
        if (way ==0){
            way = 7;
        }
        int position = way;
        for (int i=0; i<=7-way; i++){
            Calendar caclender = Calendar.getInstance();
            caclender.setTime(new Date());
            caclender.set(Calendar.DATE, caclender.get(Calendar.DATE) + i);
            String day = String.valueOf(caclender.get(Calendar.DAY_OF_MONTH));
            days[position-1] = day;
            position++;
        }
        position = way;
        for (int i=0; i< way; i++){
            Calendar caclender = Calendar.getInstance();
            caclender.setTime(new Date());
            caclender.set(Calendar.DATE, caclender.get(Calendar.DATE) - i);
            String day = String.valueOf(caclender.get(Calendar.DAY_OF_MONTH));
            days[position-1] = day;
            position--;
        }
        return days;
    }

    /**
     * 获取当前月份
     * @return
     */
    public static String getNowMonth(){
        Calendar c = Calendar.getInstance();
        c.setTimeZone(TimeZone.getTimeZone("GMT+8:00"));
        String mMonth = String.valueOf(c.get(Calendar.MONTH) +1);// 获取当前月份
        return mMonth;
    }

    /**
     *  获取制定字符串n天前或之后的日期
     * @param datestr 日期字符串
     * @param format 日期字符串格式
     * @param day  相对天数，为正数表示之后，为负数表示之前
     * @return 指定日期字符串n天之前或者之后的日期
     */
    public static java.sql.Date getBeforeAfterDate(String datestr, String format, int day) {
        SimpleDateFormat df = new SimpleDateFormat(format);
        java.sql.Date olddate = null;
        try {
            df.setLenient(false);
            olddate = new java.sql.Date(df.parse(datestr).getTime());
        } catch (ParseException e) {
            throw new RuntimeException("日期转换错误");
        }
        Calendar cal = new GregorianCalendar();
        cal.setTime(olddate);

        int Year = cal.get(Calendar.YEAR);
        int Month = cal.get(Calendar.MONTH);
        int Day = cal.get(Calendar.DAY_OF_MONTH);

        int NewDay = Day + day;

        cal.set(Calendar.YEAR, Year);
        cal.set(Calendar.MONTH, Month);
        cal.set(Calendar.DAY_OF_MONTH, NewDay);

        return new java.sql.Date(cal.getTimeInMillis());
    }

    /**
     *  获取当前日期n天前或之后的日期
     * @param format 日期字符串格式
     * @param day  相对天数，为正数表示之后，为负数表示之前
     * @return 指定日期字符串n天之前或者之后的日期
     */
    public static java.sql.Date getBeforeAfterNowDate(String format, int day) {
        SimpleDateFormat df = new SimpleDateFormat(format);
        df.setLenient(false);
        java.sql.Date olddate = new java.sql.Date(new Date().getTime());

        Calendar cal = new GregorianCalendar();
        cal.setTime(olddate);

        int Year = cal.get(Calendar.YEAR);
        int Month = cal.get(Calendar.MONTH);
        int Day = cal.get(Calendar.DAY_OF_MONTH);

        int NewDay = Day + day;

        cal.set(Calendar.YEAR, Year);
        cal.set(Calendar.MONTH, Month);
        cal.set(Calendar.DAY_OF_MONTH, NewDay);

        return new java.sql.Date(cal.getTimeInMillis());
    }

    /**
     * 获取当前时间为指定字符串
     * @param format 指定字符串
     * @return
     */
    public static String getNowDay(String format){
        SimpleDateFormat formatter = new SimpleDateFormat(format);
        return  formatter.format(new Date());
    }

    /**
     * 获取指定格式时间字符串对应的星期
     * @param timeStr 时间字符串
     * @param format 时间字符串格式
     * @return  返回值的结果可能{"周日", "周一", "周二", "周三", "周四", "周五", "周六"} ,null
     */
    public static String getWeekOfDate(String timeStr,String format) {
        // 存储结果指定星期的字符串
        String weekDay = null;

        if (!TextUtils.isEmpty(timeStr) && !TextUtils.isEmpty(format)) {
            String[] weekDaysName = {"周日", "周一", "周二", "周三", "周四", "周五", "周六"};
            Date date = formatString2Date(timeStr,format);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            weekDay = weekDaysName[calendar.get(Calendar.DAY_OF_WEEK) - 1];
        }

        return weekDay;
    }

    /**
     * 获取当前的星期
     * @return 返回当前的星期
     */
    public static String getNowWeekDay(){
        String timeStr = getNowDay(FORMAT_YYYY_LINE_MM_LINE_DD);
        return getWeekOfDate(timeStr, FORMAT_YYYY_LINE_MM_LINE_DD);
    }

    /**
     * 指定格式的时间串转换成Date对象
     * @param dateStr 日期字符串
     * @param format 日期格式
     * @return 日期对象或者null
     */
    public static Date formatString2Date(String dateStr, String format) {
        // 接收待返回的时间日期对象
        Date resultDate = null;

        if (!TextUtils.isEmpty(dateStr) && !TextUtils.isEmpty(format)) {
            try {
                SimpleDateFormat sdf = new SimpleDateFormat(format);
                resultDate = sdf.parse(dateStr);
            } catch (Exception e) {}
        }

        return resultDate;
    }

    /**
     * 获取两个指定格式时间间隔
     * @param dateStr1
     * @param format1
     * @param dateStr2
     * @param format2
     * @return
     */
    public static int getTwoDaysInterval(String dateStr1, String format1, String dateStr2,  String format2){
        long intervalDay = 0;
        try {
            Date date1 = new SimpleDateFormat(format1).parse(dateStr1);
            Date date2 = new SimpleDateFormat(format2).parse(dateStr2);
            //获取相减后天数
            intervalDay = (date1.getTime()-date2.getTime())/(24*60*60*1000);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return (int)intervalDay;
    }

    /**
     * 将指定的时间对象转换成指定格式的时间字符串
     * @param dateStr 原日期字符串
     * @param fromFormat 原日期时间格式
     * @param toFormat 转换后的时间格式
     * @return 转换后的时间字符串、null
     */
    public static String getNewFormatDateString(String dateStr, String fromFormat, String toFormat) {
        // 接收待返回的时间字符串
        String resultTimeStr = null;

        if (!TextUtils.isEmpty(dateStr) && !TextUtils.isEmpty(fromFormat) && !TextUtils.isEmpty(toFormat)) {
            // 1、将原始日期字符串转换成Date对象
            Date date = formatString2Date(dateStr, fromFormat);
            // 2、将Date对象转换成目标样式字符串
            resultTimeStr = formatDate2String(date, toFormat);
        }

        return resultTimeStr;
    }

    /**
     * 将日期转换成指定格式的字符串
     * @param date 待转换的日期
     * @param format 时间格式
     * @return 返回时间字符串或者null
     */
    public static String formatDate2String(Date date, String format) {
        // 接收待返回的时间字符串
        String resultTimeStr = null;

        if(date != null && !TextUtils.isEmpty(format)) {
            try {
                SimpleDateFormat formatPattern = new SimpleDateFormat(format);
                resultTimeStr = formatPattern.format(date);
            } catch (Exception e) {}
        }

        return resultTimeStr;
    }
}
