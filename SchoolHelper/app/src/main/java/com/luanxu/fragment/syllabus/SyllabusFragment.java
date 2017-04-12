package com.luanxu.fragment.syllabus;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.luanxu.activity.syllabus.UpdateSyllabusActivity;
import com.luanxu.adapter.syllabus.FragSyllabusWeekAdapter;
import com.luanxu.base.BaseFragment;
import com.luanxu.bean.BottomMenuBean;
import com.luanxu.bean.syllabus.CourseBean;
import com.luanxu.custom.percent.PercentLinearLayout;
import com.luanxu.schoolhelper.R;
import com.luanxu.utils.CommonUtils;
import com.luanxu.utils.DateUtils;
import com.luanxu.utils.ResourceUtil;

import java.util.ArrayList;
import java.util.List;


/**
 * @author: LuanXu
 * @createTime:2016/12/1 20:43
 * @className:  SyllabusFragment
 * @Description: 课表
 */

public class SyllabusFragment extends BaseFragment implements View.OnClickListener {
    private Activity context;

    //一节课的高度
    private int itemHeight;
    //距离顶部的距离
    private int marTop;
    //距离左侧的距离
    private int marLeft;
    //课程的集合
    private List courseData[]=new ArrayList[7];
    //周的集合
    private List<BottomMenuBean> weeks;
    //当前周的位置
    private int newWeekPosition;
    //当前所选择的周的位置
    private int newSelectPosition;
    //选择的学年
    private String selectYear = "大二";
    //选择的学期
    private String selectTerm = "第一学期";
    private String firstDayTime = "2017-2-27";

    //课程控件的背景数组
    private final Integer[] lucency_colors = {R.drawable.shape_round_58aeef, R.drawable.shape_round_ed9fa0, R.drawable.shape_round_e3c372, R.drawable.shape_round_94d161, R.drawable.shape_round_8aabe9
            , R.drawable.shape_round_37bbac, R.drawable.shape_round_0d95b4, R.drawable.shape_round_f2ab8a, R.drawable.shape_round_e98af2, R.drawable.shape_round_b59fd0};
    private final Integer[] colors = {R.color.color_58aeef, R.color.color_ed9fa0, R.color.color_e3c372, R.color.color_94d161, R.color.color_8aabe9
            , R.color.color_37bbac, R.color.color_0d95b4, R.color.color_f2ab8a, R.color.color_e98af2, R.color.color_b59fd0};
    private final String[] weekDaysName = {"周一", "周二", "周三", "周四", "周五", "周六", "周日"};

    //课程表周数的适配器
    private FragSyllabusWeekAdapter adapter;
    // 更多弹窗
    private PopupWindow morePop;

    private View view;
    //每天课程外部包裹控件的数组
    private LinearLayout weekPanels[]=new LinearLayout[7];
    //顶部周一到周日控件的集合
    private TextView tv_weeks[] = new TextView[7];
    //日期控件的数组
    private TextView tv_days[] = new TextView[7];
    //星期和日期外部布局
    private LinearLayout ll_times[] = new LinearLayout[7];
    //最顶部当前周数的控件
    private TextView tv_week;
    //最顶部当前学期的控件
    private TextView tv_term;
    //当前月份
    private TextView tv_month;
    //周的列表外部布局
    private PercentLinearLayout pll_list_week;
    //周的列表
    private ListView lv_week;
    //课表顶部的时间
    private LinearLayout ll_time;
    //课程详情的外部布局
    private PercentLinearLayout pll_class_details;
    //课程详情的控件
    private TextView tv_class_details;
    //右上角更多按钮
    private ImageView tv_more;
    //蒙板
    private View view_gray;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (view == null) {
            view = LayoutInflater.from(getActivity()).inflate(R.layout.frag_syllabus, null);
        } else {
            ((ViewGroup) view.getParent()).removeView(view);
        }
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        context = getActivity();
        itemHeight=getResources().getDimensionPixelSize(R.dimen.yms_dimens_80_0_px);
        marTop=getResources().getDimensionPixelSize(R.dimen.yms_dimens_4_0_px);
        marLeft=getResources().getDimensionPixelSize(R.dimen.yms_dimens_4_0_px);
        //数据
        getData();

        init();
    }

    /**
     * 初始化数据和控件
     */
    private void init(){
        //获取本周日期集合
        String[] days = DateUtils.getNowWeekDays();
        //初始化顶部日期控件
        for (int i=0; i<tv_days.length; i++){
            tv_days[i] = (TextView) view.findViewById(R.id.day_1 + 3*i);
            tv_days[i].setText(days[i]);
        }
        //初始化顶部星期集合
        for (int i=0; i<tv_weeks.length; i++){
            tv_weeks[i] = (TextView) view.findViewById(R.id.week_1 + 3*i);
        }
        //初始化每天课程包裹布局控件
        for (int i = 0; i < weekPanels.length; i++) {
            weekPanels[i]=(LinearLayout) view.findViewById(R.id.weekPanel_1 + i);
            initWeekPanel(weekPanels[i], courseData[i]);
        }
        //初始化课表顶部星期，日期包裹布局控件
        for (int i=0; i< ll_times.length; i++){
            ll_times[i] = (LinearLayout) view.findViewById(R.id.ll_time_1 + 3*i);
        }
        //获取当前星期
        final String newWeekDayName = DateUtils.getNowWeekDay();
        for (int i=0; i< weekDaysName.length; i++){
            if (weekDaysName[i].equals(newWeekDayName)){
                ll_times[i].setBackgroundResource(R.color.color_blue);
                tv_days[i].setTextColor(ResourceUtil.getColor(context, R.color.color_white));
                tv_weeks[i].setTextColor(ResourceUtil.getColor(context, R.color.color_white));
                break;
            }
        }
        view_gray = view.findViewById(R.id.view);
        view_gray.setOnClickListener(this);
        tv_week = (TextView) view.findViewById(R.id.tv_week);
        tv_week.setText(weeks.get(newSelectPosition).content);
        tv_term = (TextView) view.findViewById(R.id.tv_term);
        tv_term.setText(selectYear+" "+selectTerm);

        tv_month = (TextView) view.findViewById(R.id.tv_month);
        tv_month.setText(DateUtils.getNowMonth());
        pll_list_week = (PercentLinearLayout) view.findViewById(R.id.pll_list_week);
        pll_list_week.setOnClickListener(this);
        lv_week = (ListView) view.findViewById(R.id.lv_week);
        adapter = new FragSyllabusWeekAdapter(context, weeks, newWeekPosition, newSelectPosition);
        lv_week.setAdapter(adapter);
        lv_week.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                //更改当前选择的位置
                newSelectPosition = i;
                adapter.setDate(newSelectPosition);
                //把列表隐藏
                pll_list_week.setVisibility(View.GONE);
                if (newSelectPosition == newWeekPosition){
                    tv_week.setText(weeks.get(newSelectPosition).content);
                    tv_week.setTextColor(ResourceUtil.getColor(context, R.color.color_blue));
                    tv_week.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.mipmap.ic_title_week_set_icon, 0);
                }else{
                    tv_week.setText(weeks.get(newSelectPosition).content+"（非本周）");
                    tv_week.setTextColor(ResourceUtil.getColor(context, R.color.color_f15209));
                    tv_week.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.mipmap.ic_title_week_set_orange_icon, 0);
                }
                setCurrentWeekDays();
            }
        });
        ll_time = (LinearLayout) view.findViewById(R.id.ll_time);
        ll_time.setOnClickListener(this);
        pll_class_details = (PercentLinearLayout) view.findViewById(R.id.pll_class_details);
        pll_class_details.setOnClickListener(this);
        tv_class_details = (TextView) view.findViewById(R.id.tv_class_details);
        tv_more = (ImageView) view.findViewById(R.id.tv_more);
        tv_more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popupWindow(view);
            }
        });
    }

    /**
     * @param v 控件
     * @Description: title更多弹框
     * @return: void
     */
    private void popupWindow(View v) {
        view_gray.setVisibility(View.VISIBLE);
        final View popRoot = LayoutInflater.from(getActivity()).inflate(R.layout.pop_reference, null);
        // 创建PopupWindow实例, 分别是宽度和高度
        morePop = new PopupWindow(popRoot, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);
        morePop.showAsDropDown(view.findViewById(R.id.ll_head));
        // 点击其他地方消失
        popRoot.setOnTouchListener(new View.OnTouchListener() {
            @SuppressLint("ClickableViewAccessibility")
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (morePop != null && morePop.isShowing()) {
                    morePop.dismiss();
                    morePop = null;
                }
                return false;
            }
        });
        popRoot.setFocusable(true);
        popRoot.setFocusableInTouchMode(true);
        popRoot.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    morePop.dismiss();
                    morePop = null;
                    return true;
                }
                return false;
            }
        });

        //刷新课表
        popRoot.findViewById(R.id.ll_refresh).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, UpdateSyllabusActivity.class);
                intent.putExtra("week", weeks.get(newSelectPosition).content);
                intent.putExtra("year", selectYear);
                intent.putExtra("term", selectTerm);
                intent.putExtra("tag", "refresh");
                startActivityForResult(intent, 1);
                morePop.dismiss();
                morePop = null;
            }
        });

        //  修改课表
        popRoot.findViewById(R.id.ll_update).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, UpdateSyllabusActivity.class);
                intent.putExtra("week", weeks.get(newSelectPosition).content);
                intent.putExtra("year", selectYear);
                intent.putExtra("term", selectTerm);
                intent.putExtra("tag", "update");
                startActivityForResult(intent, 2);
                morePop.dismiss();
                morePop = null;
            }
        });

        morePop.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                view_gray.setVisibility(View.GONE);
            }
        });

    }

    public void getData(){
        List<CourseBean>list1=new ArrayList<CourseBean>();
        CourseBean c1 =new CourseBean("软件工程","A402", 1, 3, "典韦", "1002", 0);
        list1.add(c1);
        list1.add(new CourseBean("C语言", "A101", 2, 4, "甘宁", "1001", 1));
        courseData[0]=list1;

        List<CourseBean>list2=new ArrayList<CourseBean>();
        list2.add(new CourseBean("计算机组成原理", "A106", 6, 3, "马超", "1001", 2));
        courseData[1]=list2;

        List<CourseBean>list3=new ArrayList<CourseBean>();
        list3.add(new CourseBean("数据库原理", "A105", 2, 3, "孙权", "1008", 3));
        list3.add(new CourseBean("计算机网络", "A405", 6, 2, "司马懿", "1009", 4));
        list3.add(new CourseBean("电影赏析", "A112", 9, 2, "诸葛亮", "1039", 5));
        courseData[2]=list3;

        List<CourseBean>list4=new ArrayList<CourseBean>();
        list4.add(new CourseBean("数据结构", "A223", 1, 3, "刘备", "1012", 6));
        list4.add(new CourseBean("操作系统", "A405", 6, 3, "曹操", "1014", 7));
        courseData[3]=list4;

        List<CourseBean>list5=new ArrayList<CourseBean>();
        list5.add(new CourseBean("Android开发","C120",1,4,"黄盖","1250", 8));
        list5.add(new CourseBean("游戏设计原理","C120",8,4,"陆逊","1251", 9));
        courseData[4]=list5;

        weeks = CommonUtils.getWeekList();
        //当前的日期
        String newDay = DateUtils.getNowDay(DateUtils.FORMAT_YYYY_LINE_MM_LINE_DD);
        //当前日期和第一周周一的时间间隔
        int intervalDay = DateUtils.getTwoDaysInterval(newDay, DateUtils.FORMAT_YYYY_LINE_MM_LINE_DD, firstDayTime, DateUtils.FORMAT_YYYY_LINE_MM_LINE_DD);
        newWeekPosition = intervalDay/7;
        newSelectPosition = newWeekPosition;
    }

    /**
     * 设置当前周的本地日期
     */
    public void setCurrentWeekDays(){
        String month = "";
        for (int i=0; i<7; i++){
            //选择的这一周的时间与第一周周一的时间的间隔天数
            int intervalDay = newSelectPosition*7+i;
            //时间yyyy-MM-dd
            String currentTime = DateUtils.getBeforeAfterDate(firstDayTime, DateUtils.FORMAT_YYYY_LINE_MM_LINE_DD, intervalDay).toString();
            if (i==0){
                //月
                month = DateUtils.getNewFormatDateString(currentTime, DateUtils.FORMAT_YYYY_LINE_MM_LINE_DD, DateUtils.FORMAT_MM);
            }
            //日
            String day = DateUtils.getNewFormatDateString(currentTime, DateUtils.FORMAT_YYYY_LINE_MM_LINE_DD, DateUtils.FORMAT_DD);
            //设置时间
            tv_days[i].setText(day);
        }
        tv_month.setText(month);

    }

    /**
     * 初始化课表面板
     * @param ll
     * @param data
     */
    public void initWeekPanel(LinearLayout ll,List<CourseBean>data){
        if(ll==null || data==null || data.size()<1)return;
        Log.i("Msg", "初始化面板");
        CourseBean pre=data.get(0);
        for (int i = 0; i < data.size(); i++) {
            final CourseBean c =data.get(i);
            TextView tv =new TextView(context);
            LinearLayout.LayoutParams lp =new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.FILL_PARENT ,
                    itemHeight*c.getStep()+marTop*(c.getStep()-1));
            if(i>0){
                lp.setMargins(marLeft, (c.getStart()-(pre.getStart()+pre.getStep()))*(itemHeight+marTop)+marTop, 0, 0);
            }else{
                lp.setMargins(marLeft, (c.getStart()-1)*(itemHeight+marTop)+marTop, 0, 0);
            }
            tv.setLayoutParams(lp);
            tv.setGravity(Gravity.CENTER);
            tv.setTextSize(12);
            tv.setEllipsize(TextUtils.TruncateAt.valueOf("END"));
            tv.setTextColor(getResources().getColor(R.color.color_white));
            tv.setText(c.getName()+"\n"+c.getRoom()+"\n"+c.getTeach());
            tv.setBackground(ResourceUtil.getDrawable(context, lucency_colors[c.getBackground()-(c.getBackground()/10)*10]));
            tv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    pll_class_details.setVisibility(View.VISIBLE);
                    tv_class_details.setText(c.getName()+"\n"+c.getRoom()+"\n"+c.getStart()+"~"+(c.getStart()+c.getStep())+"节"+"\n"+c.getTeach());
                    tv_class_details.setBackgroundResource(colors[c.getBackground()-(c.getBackground()/10)*10]);
                }
            });
            ll.addView(tv);
            pre=c;
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.ll_time:
                if (pll_list_week.getVisibility() == View.VISIBLE){
                    pll_list_week.setVisibility(View.GONE);
                }else{
                    pll_list_week.setVisibility(View.VISIBLE);
                }
                break;
            case R.id.pll_list_week:
                pll_list_week.setVisibility(View.GONE);
                break;
            case R.id.pll_class_details:
                pll_class_details.setVisibility(View.GONE);
                break;
            case R.id.view:
                view_gray.setVisibility(View.GONE);
        }
    }
}
