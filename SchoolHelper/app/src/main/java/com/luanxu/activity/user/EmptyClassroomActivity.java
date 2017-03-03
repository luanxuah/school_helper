package com.luanxu.activity.user;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.luanxu.adapter.user.EmptyClassroomAdapter;
import com.luanxu.application.SchoolHelperApplication;
import com.luanxu.base.BaseActivity;
import com.luanxu.bean.BottomMenuBean;
import com.luanxu.custom.TitleBar;
import com.luanxu.custom.bottommenu.LinkageCommontBottomMenu;
import com.luanxu.custom.percent.PercentLinearLayout;
import com.luanxu.schoolhelper.R;
import com.luanxu.utils.DataUtils;

import java.util.List;

/**
 * @author: LuanXu
 * @createTime:2016/12/15 20:14
 * @className:  EmptyClassroomActivity
 * @Description: 空教室
 */

public class EmptyClassroomActivity extends BaseActivity implements View.OnClickListener{
    //上下文对象
    private Context context;

    //选择的校区校区
    private BottomMenuBean campusBean;
    //校区集合
    private List<BottomMenuBean> campusList;
    //选择的教室类别
    private BottomMenuBean sortBean;
    //教室类别的集合
    private List<BottomMenuBean> sortList;
    //选择的日期
    private BottomMenuBean dayBean;
    //日期的集合
    private List<BottomMenuBean> dayList;
    //选择的使用时间
    private BottomMenuBean timeBean;
    //使用时间的集合
    private List<BottomMenuBean> timeList;

    private EmptyClassroomAdapter adapter;

    //校区外部布局
    private PercentLinearLayout pll_campus;
    //选择教室类别
    private PercentLinearLayout pll_sort;
    //使用时间外部布局
    private PercentLinearLayout pll_time;
    //日期外部布局
    private PercentLinearLayout pll_day;
    //校区
    private TextView tv_campus;
    //教室类别
    private TextView tv_sort;
    //使用时间
    private TextView tv_time;
    //日期
    private TextView tv_day;
    //底部列表
    private ListView lv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_empty_classroom);
        SchoolHelperApplication.getInstance().addActivity(this);
        context = this;
        TitleBar bar = getTitleBar();
        bar.setBack();
        bar.setTitle(getResources().getString(R.string.str_empty_classroom), R.color.color_white);
        bar.enableRightBtn(getResources().getString(R.string.str_search), -1, new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        init();
    }

    /**
     * 初始化数据和控件
     */
    private void init(){
        campusList = DataUtils.getCampus();
        campusBean = campusList.get(0);
        sortList = DataUtils.getSort();
        sortBean = sortList.get(0);
        dayList = DataUtils.getDay();
        dayBean = dayList.get(0);
        timeList = DataUtils.getTime();
        timeBean = timeList.get(0);

        pll_campus = (PercentLinearLayout) findViewById(R.id.pll_campus);
        pll_campus.setOnClickListener(this);
        pll_sort = (PercentLinearLayout) findViewById(R.id.pll_sort);
        pll_sort.setOnClickListener(this);
        pll_time = (PercentLinearLayout) findViewById(R.id.pll_time);
        pll_time.setOnClickListener(this);
        pll_day = (PercentLinearLayout) findViewById(R.id.pll_day);
        pll_day.setOnClickListener(this);
        tv_campus = (TextView) findViewById(R.id.tv_campus);
        tv_campus.setText(campusBean.content);
        tv_sort = (TextView) findViewById(R.id.tv_sort);
        tv_sort.setText(sortBean.content);
        tv_time = (TextView) findViewById(R.id.tv_time);
        tv_time.setText(timeBean.content);
        tv_day = (TextView) findViewById(R.id.tv_day);
        tv_day.setText(dayBean.content);
        lv = (ListView) findViewById(R.id.lv);
        adapter = new EmptyClassroomAdapter(context);
        lv.setAdapter(adapter);
    }

    @Override
    public void onClick(View view) {
        LinkageCommontBottomMenu buttonMenu = null;
        switch (view.getId()){
            case R.id.pll_campus:
                //选择校区
                buttonMenu = new LinkageCommontBottomMenu(this);
                buttonMenu.setData1(campusList, campusBean.content);
                buttonMenu.setListener1(new LinkageCommontBottomMenu.LinkageCommontBottomMenuListener1() {

                    @Override
                    public void result1(BottomMenuBean bean1) {
                        tv_campus.setText(bean1.content);
                    }
                });
                buttonMenu.show();
                break;
            case R.id.pll_sort:
                //选择教室类别
                buttonMenu = new LinkageCommontBottomMenu(this);
                buttonMenu.setData1(sortList, sortBean.content);
                buttonMenu.setListener1(new LinkageCommontBottomMenu.LinkageCommontBottomMenuListener1() {

                    @Override
                    public void result1(BottomMenuBean bean1) {
                        tv_sort.setText(bean1.content);
                    }
                });
                buttonMenu.show();
                break;
            case R.id.pll_day:
                //日期
                buttonMenu = new LinkageCommontBottomMenu(this);
                buttonMenu.setData1(dayList, dayBean.content);
                buttonMenu.setListener1(new LinkageCommontBottomMenu.LinkageCommontBottomMenuListener1() {

                    @Override
                    public void result1(BottomMenuBean bean1) {
                        tv_day.setText(bean1.content);
                    }
                });
                buttonMenu.show();
                break;
            case R.id.pll_time:
                //选择使用时间
                buttonMenu = new LinkageCommontBottomMenu(this);
                buttonMenu.setData1(timeList, timeBean.content);
                buttonMenu.setListener1(new LinkageCommontBottomMenu.LinkageCommontBottomMenuListener1() {

                    @Override
                    public void result1(BottomMenuBean bean1) {
                        tv_time.setText(bean1.content);
                    }
                });
                buttonMenu.show();
                break;
        }
    }
}
