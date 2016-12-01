package com.luanxu.utils;

import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

/**
 * Created by 栾煦 on 2016/11/29.
 */
public class DateUtils {

    /**
     * 获取当前星期的日期集合
     * @return 0-7   星期一到星期日
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

}
