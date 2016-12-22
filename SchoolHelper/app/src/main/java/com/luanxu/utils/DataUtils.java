package com.luanxu.utils;

import com.luanxu.bean.BottomMenuBean;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by 栾煦 on 2016/12/20.
 */
public class DataUtils {

    /**
     * 获取校区的集合
     * @return
     */
    public static List<BottomMenuBean> getCampus(){
        List<BottomMenuBean> campusList = new ArrayList<BottomMenuBean>();
        campusList.add(new BottomMenuBean("0", "花津校区"));
        campusList.add(new BottomMenuBean("1", "赭山校区"));
        return campusList;
    }

    /**
     * 获取教室
     * @return
     */
    public static List<BottomMenuBean> getSort(){
        List<BottomMenuBean> sortList = new ArrayList<BottomMenuBean>();
        sortList.add(new BottomMenuBean("0", "全部"));
        sortList.add(new BottomMenuBean("1", "传媒学院机房"));
        sortList.add(new BottomMenuBean("2", "传媒学院专用"));
        sortList.add(new BottomMenuBean("3", "大学外语专用"));
        sortList.add(new BottomMenuBean("4", "电声实验室"));
        sortList.add(new BottomMenuBean("5", "电声实验室"));
        sortList.add(new BottomMenuBean("6", "电子技术实验室"));
        sortList.add(new BottomMenuBean("7", "电子政务实验室"));
        sortList.add(new BottomMenuBean("7", "多功能报告厅"));
        return sortList;
    }


    /**
     * 获取日期
     * @return
     */
    public static List<BottomMenuBean> getDay(){
        List<BottomMenuBean> sortList = new ArrayList<BottomMenuBean>();
        for (int i=0; i<7; i++){
            BottomMenuBean bean = new BottomMenuBean();
            bean.id = i+"";
            //当前日期i天后的日期
            if (i==0){
                bean.content = "今天";
            }else{
                bean.content = DateUtils.getBeforeAfterNowDate(DateUtils.FORMAT_YYYY_LINE_MM_LINE_DD, i).toString();
            }
            sortList.add(bean);
        }
        return sortList;
    }

    /**
     * 获取使用时间
     * @return
     */
    public static List<BottomMenuBean> getTime(){
        List<BottomMenuBean> sortList = new ArrayList<BottomMenuBean>();
        sortList.add(new BottomMenuBean("0", "上午"));
        sortList.add(new BottomMenuBean("1", "下午"));
        sortList.add(new BottomMenuBean("2", "晚上"));
        return sortList;
    }


}
