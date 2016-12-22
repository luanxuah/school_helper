package com.luanxu.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.luanxu.application.SchoolHelperApplication;
import com.luanxu.base.BaseActivity;
import com.luanxu.bean.BottomMenuBean;
import com.luanxu.custom.TitleBar;
import com.luanxu.custom.bottommenu.LinkageCommontBottomMenu;
import com.luanxu.custom.percent.PercentLinearLayout;
import com.luanxu.schoolhelper.R;
import com.luanxu.utils.ToastUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * @author: LuanXu
 * @createTime:2016/12/13 20:16
 * @className:  SelectYearTermActivity
 * @Description: 选择学期学年
 */

public class SelectYearTermActivity extends BaseActivity implements View.OnClickListener{
    //上下文对象
    private Context context;

    //学年
    private String year;
    //学期
    private String term;
    //学年的集合
    private List<BottomMenuBean> yearBeanList = new ArrayList<BottomMenuBean>();
    //学期的集合
    private List<BottomMenuBean> termBeanList = new ArrayList<BottomMenuBean>();

    //学年的外部布局
    private PercentLinearLayout pll_year;
    //学年控件
    private TextView tv_year;
    //学期的外部布局
    private PercentLinearLayout pll_term;
    //学期控件
    private TextView tv_term;
    //完成按钮
    private Button tv_complete;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_select_year_term);
        SchoolHelperApplication.getInstance().addActivity(this);
        context = this;

        TitleBar bar = getTitleBar();
        bar.setBack();
        bar.setTitle(getResources().getString(R.string.str_select_year_term), R.color.color_white);

        init();
    }

    /**
     * 初始化数据和控件
     */
    private void init(){
        year = getIntent().getStringExtra("year");
        term = getIntent().getStringExtra("term");
        yearBeanList.add(new BottomMenuBean("0", "大一"));
        yearBeanList.add(new BottomMenuBean("1", "大二"));
        yearBeanList.add(new BottomMenuBean("2", "大三"));
        yearBeanList.add(new BottomMenuBean("3", "大四"));
        termBeanList.add(new BottomMenuBean("0", "第一学期"));
        termBeanList.add(new BottomMenuBean("1", "第二学期"));

        pll_year = (PercentLinearLayout) findViewById(R.id.pll_year);
        pll_year.setOnClickListener(this);
        tv_year = (TextView) findViewById(R.id.tv_year);
        tv_year.setText(year);
        pll_term = (PercentLinearLayout) findViewById(R.id.pll_term);
        pll_term.setOnClickListener(this);
        tv_term = (TextView) findViewById(R.id.tv_term);
        tv_term.setText(term);
        tv_complete = (Button) findViewById(R.id.tv_complete);
        tv_complete.setOnClickListener(this);
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
            case R.id.tv_complete:
                if (TextUtils.isEmpty(year)){
                    //如果学年为空提示
                    ToastUtil.show(context, R.string.str_year_is_null, Toast.LENGTH_SHORT);
                    return;
                }
                if (TextUtils.isEmpty(term)){
                    //如果学期为空提示
                    ToastUtil.show(context, R.string.str_term_is_null, Toast.LENGTH_SHORT);
                    return;
                }
                Intent intent = new Intent();
                intent.putExtra("year", year);
                intent.putExtra("term", term);
                setResult(Activity.RESULT_OK, intent);
                finish();
                break;
        }
    }
}
