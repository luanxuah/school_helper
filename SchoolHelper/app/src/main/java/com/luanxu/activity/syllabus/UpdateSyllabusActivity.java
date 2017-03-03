package com.luanxu.activity.syllabus;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.luanxu.application.SchoolHelperApplication;
import com.luanxu.base.BaseActivity;
import com.luanxu.bean.BottomMenuBean;
import com.luanxu.custom.TitleBar;
import com.luanxu.custom.bottommenu.LinkageCommontBottomMenu;
import com.luanxu.custom.percent.PercentLinearLayout;
import com.luanxu.schoolhelper.R;
import com.luanxu.utils.CommonUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @author: LuanXu
 * @createTime:2016/12/11 14:03
 * @className:  UpdateSyllabusActivity
 * @Description:  修改课表
 */
public class UpdateSyllabusActivity extends BaseActivity implements View.OnClickListener{
    //上下文对象
    private Context context;

    //标记，刷新课表：refresh，修改课表：update
    private String tag;
    //选择的学年
    private String year;
    //选择的学期
    private String term;
    //选择的当前周
    private String week;
    //学年的集合
    private List<BottomMenuBean> yearBeanList = new ArrayList<BottomMenuBean>();
    //学期的集合
    private List<BottomMenuBean> termBeanList = new ArrayList<BottomMenuBean>();


    //学年外部布局
    private PercentLinearLayout pll_year;
    //学年
    private TextView tv_year;
    //学年的小箭头
    private ImageView iv_year;
    //学期外部布局
    private PercentLinearLayout pll_term;
    //学期
    private TextView tv_term;
    //学期的小箭头
    private ImageView iv_term;
    //当前周外部布局
    private PercentLinearLayout pll_week;
    //当期周
    private TextView tv_week;
    //当前周的小箭头
    private ImageView iv_week;
    //底部按钮
    private Button btn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_update_syllabus);
        SchoolHelperApplication.getInstance().addActivity(this);
        context = this;
        Intent intent = getIntent();
        tag = intent.getStringExtra("tag");
        year = intent.getStringExtra("year");
        term = intent.getStringExtra("term");
        week = intent.getStringExtra("week");
        TitleBar bar = getTitleBar();
        if (tag.equals("update")){
            bar.setTitle(getResources().getString(R.string.str_update_syllabus), R.color.color_white);
        }else{
            bar.setTitle(getResources().getString(R.string.str_refresh_syllabus), R.color.color_white);
        }
        bar.setBack();

        init();
    }

    private void init(){

        yearBeanList.add(new BottomMenuBean("0", "大一"));
        yearBeanList.add(new BottomMenuBean("1", "大二"));
        yearBeanList.add(new BottomMenuBean("2", "大三"));
        yearBeanList.add(new BottomMenuBean("3", "大四"));
        termBeanList.add(new BottomMenuBean("0", "第一学期"));
        termBeanList.add(new BottomMenuBean("1", "第二学期"));

        pll_year = (PercentLinearLayout) findViewById(R.id.pll_year);
        iv_year = (ImageView) findViewById(R.id.iv_year);
        tv_year = (TextView) findViewById(R.id.tv_year);
        tv_year.setText(year);
        pll_term = (PercentLinearLayout) findViewById(R.id.pll_term);
        iv_term = (ImageView) findViewById(R.id.iv_term);
        tv_term = (TextView) findViewById(R.id.tv_term);
        tv_term.setText(term);
        pll_week = (PercentLinearLayout) findViewById(R.id.pll_week);
        iv_week = (ImageView) findViewById(R.id.iv_week);
        tv_week = (TextView) findViewById(R.id.tv_week);
        tv_week.setText(week);
        btn = (Button) findViewById(R.id.btn);
        btn.setOnClickListener(this);
        if (tag.equals("update")){
            iv_year.setVisibility(View.VISIBLE);
            iv_term.setVisibility(View.VISIBLE);
            iv_week.setVisibility(View.VISIBLE);
            pll_year.setOnClickListener(this);
            pll_term.setOnClickListener(this);
            pll_week.setOnClickListener(this);
        }
    }

    @Override
    public void onClick(View view) {
        LinkageCommontBottomMenu buttonMenu = null;
        switch (view.getId()){
            case R.id.pll_year:
                //点击学年
                buttonMenu = new LinkageCommontBottomMenu(this);
                buttonMenu.setData1(yearBeanList, year);
                buttonMenu.setListener1(new LinkageCommontBottomMenu.LinkageCommontBottomMenuListener1() {

                    @Override
                    public void result1(BottomMenuBean bean1) {
                        year = bean1.content;
                        tv_year.setText(year);
                    }
                });

                buttonMenu.show();
                break;
            case R.id.pll_term:
                //点击学期
                buttonMenu = new LinkageCommontBottomMenu(this);
                buttonMenu.setData1(termBeanList, term);
                buttonMenu.setListener1(new LinkageCommontBottomMenu.LinkageCommontBottomMenuListener1() {

                    @Override
                    public void result1(BottomMenuBean bean1) {
                        term = bean1.content;
                        tv_term.setText(term);
                    }
                });
                buttonMenu.show();
                break;
            case R.id.pll_week:
                //点击当期周
                buttonMenu = new LinkageCommontBottomMenu(this);
                buttonMenu.setData1(CommonUtils.getWeekList(), week);
                buttonMenu.setListener1(new LinkageCommontBottomMenu.LinkageCommontBottomMenuListener1() {

                    @Override
                    public void result1(BottomMenuBean bean1) {
                        week = bean1.content;
                        tv_week.setText(week);
                    }
                });
                buttonMenu.show();
                break;

            case R.id.btn:
                //点击底部按钮

                break;
        }
    }
}
