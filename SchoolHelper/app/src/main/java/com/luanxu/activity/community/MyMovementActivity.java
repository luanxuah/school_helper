package com.luanxu.activity.community;

import android.content.Context;
import android.os.Bundle;

import com.luanxu.adapter.community.MyMovementAdapter;
import com.luanxu.application.SchoolHelperApplication;
import com.luanxu.base.BaseActivity;
import com.luanxu.custom.PullToRefreshListView;
import com.luanxu.custom.SearchEditText;
import com.luanxu.custom.TitleBar;
import com.luanxu.schoolhelper.R;

/**
 * @author: LuanXu
 * @createTime:2017/4/13 17:10
 * @className:  MyMovementActivity
 * @Description: 我的活动页面
 */

public class MyMovementActivity extends BaseActivity{
    //上下文对象
    private Context context;

    private MyMovementAdapter adapter;

    //搜索
    private SearchEditText et_search;
    //列表
    private PullToRefreshListView pullToRefreshListView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_my_movement);
        SchoolHelperApplication.getInstance().addActivity(this);
        context = this;

        TitleBar bar = getTitleBar();
        bar.setTitle(R.string.str_my_movement, R.color.color_white);
        bar.setBack();

        init();
    }

    /**
     * 初始化数据和控件
     */
    private void init(){
        et_search = (SearchEditText) findViewById(R.id.et_search);
        pullToRefreshListView = (PullToRefreshListView) findViewById(R.id.pullToRefreshListView);
        adapter = new MyMovementAdapter(context);
        pullToRefreshListView.setAdapter(adapter);
        et_search.setOnSearchListener(new SearchEditText.OnSearchListener() {
            @Override
            public void OnSearch() {

            }
        });
    }
}
