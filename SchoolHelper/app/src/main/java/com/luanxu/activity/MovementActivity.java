package com.luanxu.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;

import com.luanxu.adapter.MovementAdapter;
import com.luanxu.application.SchoolHelperApplication;
import com.luanxu.base.BaseActivity;
import com.luanxu.custom.PullToRefreshListView;
import com.luanxu.custom.TitleBar;
import com.luanxu.schoolhelper.R;

/**
 * @author: LuanXu
 * @createTime:2017/1/3 19:47
 * @className:  MovementActivity
 * @Description: 校内活动页面
 */

public class MovementActivity extends BaseActivity{
    //上下文对象
    private Context context;

    private MovementAdapter adapter;

    //列表
    private PullToRefreshListView list;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_movement);
        SchoolHelperApplication.getInstance().addActivity(this);
        context = this;

        TitleBar bar = getTitleBar();
        bar.setTitle(getResources().getString(R.string.str_movement), R.color.color_white);

        init();
    }

    /**
     * 初始化数据和控件
     */
    private void init(){
        list = (PullToRefreshListView) findViewById(R.id.list);
        adapter = new MovementAdapter(context);
        list.setAdapter(adapter);

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(context, MovementDetailActivity.class);
                startActivity(intent);
            }
        });
    }
}
