package com.luanxu.activity.user;

import android.content.Context;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.ListView;

import com.luanxu.adapter.user.ActMyCreditAdpter;
import com.luanxu.application.SchoolHelperApplication;
import com.luanxu.base.BaseActivity;
import com.luanxu.custom.TitleBar;
import com.luanxu.schoolhelper.R;

/**
 * @author: LuanXu
 * @createTime:2016/12/1 20:46
 * @className:  CreditActivity
 * @Description: 学分页面
 */

public class CreditActivity extends BaseActivity{
    //上下文
    private Context mContext;

    //学分的适配器
    private ActMyCreditAdpter adpter;

    //学分列表
    private ListView pullToRefreshListView;

    private EditText et_search;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_my_credit);
        SchoolHelperApplication.getInstance().addActivity(this);
        mContext = this;

        TitleBar bar = getTitleBar();
        bar.setTitle(getResources().getString(R.string.str_credit), R.color.color_white);

        init();
    }

    /**
     * 初始化数据和控件
     */
    private void init() {
        pullToRefreshListView = (ListView) findViewById(R.id.pullToRefreshListView);
        adpter=new ActMyCreditAdpter(mContext);
        pullToRefreshListView.setAdapter(adpter);
        et_search = (EditText) findViewById(R.id.et_search);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }
}
