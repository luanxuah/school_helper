package com.luanxu.activity;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.luanxu.base.BaseActivity;
import com.luanxu.bean.CourseBean;
import com.luanxu.custom.TitleBar;
import com.luanxu.schoolhelper.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by luanxu on 2016/11/27.
 */
public class SyllabusActivity extends BaseActivity{
    LinearLayout weekPanels[]=new LinearLayout[7];
    List courseData[]=new ArrayList[7];
    int itemHeight;
    int marTop,marLeft;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_syllabus);
        itemHeight=getResources().getDimensionPixelSize(R.dimen.yms_dimens_80_0_px);
        marTop=getResources().getDimensionPixelSize(R.dimen.yms_dimens_4_0_px);
        marLeft=getResources().getDimensionPixelSize(R.dimen.yms_dimens_4_0_px);

        //数据
        getData();

        for (int i = 0; i < weekPanels.length; i++) {
            weekPanels[i]=(LinearLayout) findViewById(R.id.weekPanel_1+i);
            initWeekPanel(weekPanels[i], courseData[i]);
        }

    }
    public void getData(){
        List<CourseBean>list1=new ArrayList<CourseBean>();
        CourseBean c1 =new CourseBean("软件工程","A402", 1, 4, "典韦", "1002");
        list1.add(c1);
        list1.add(new CourseBean("C语言", "A101", 6, 3, "甘宁", "1001"));
        courseData[0]=list1;

        List<CourseBean>list2=new ArrayList<CourseBean>();
        list2.add(new CourseBean("计算机组成原理", "A106", 6, 3, "马超", "1001"));
        courseData[1]=list2;

        List<CourseBean>list3=new ArrayList<CourseBean>();
        list3.add(new CourseBean("数据库原理", "A105", 2, 3, "孙权", "1008"));
        list3.add(new CourseBean("计算机网络", "A405", 6, 2, "司马懿", "1009"));
        list3.add(new CourseBean("电影赏析", "A112", 9, 2, "诸葛亮", "1039"));
        courseData[2]=list3;

        List<CourseBean>list4=new ArrayList<CourseBean>();
        list4.add(new CourseBean("数据结构", "A223", 1, 3, "刘备", "1012"));
        list4.add(new CourseBean("操作系统", "A405", 6, 3, "曹操", "1014"));
        courseData[3]=list4;

        List<CourseBean>list5=new ArrayList<CourseBean>();
        list5.add(new CourseBean("Android开发","C120",1,4,"黄盖","1250"));
        list5.add(new CourseBean("游戏设计原理","C120",8,4,"陆逊","1251"));
        courseData[4]=list5;
    }

    public void initWeekPanel(LinearLayout ll,List<CourseBean>data){
        if(ll==null || data==null || data.size()<1)return;
        Log.i("Msg", "初始化面板");
        CourseBean pre=data.get(0);
        for (int i = 0; i < data.size(); i++) {
            CourseBean c =data.get(i);
            TextView tv =new TextView(this);
            LinearLayout.LayoutParams lp =new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.FILL_PARENT ,
                    itemHeight*c.getStep()+marTop*(c.getStep()-1));
            if(i>0){
                lp.setMargins(marLeft, (c.getStart()-(pre.getStart()+pre.getStep()))*(itemHeight+marTop)+marTop, 0, 0);
            }else{
                lp.setMargins(marLeft, (c.getStart()-1)*(itemHeight+marTop)+marTop, 0, 0);
            }
            tv.setLayoutParams(lp);
            tv.setGravity(Gravity.TOP);
            tv.setGravity(Gravity.CENTER_HORIZONTAL);
            tv.setTextSize(12);
            tv.setTextColor(getResources().getColor(R.color.color_ececec));
            tv.setText(c.getName()+"\n"+c.getRoom()+"\n"+c.getTeach());
            //tv.setBackgroundColor(getResources().getColor(R.color.classIndex));
            tv.setBackground(getResources().getDrawable(R.drawable.tvshape));
            ll.addView(tv);
            pre=c;
        }
    }
}
