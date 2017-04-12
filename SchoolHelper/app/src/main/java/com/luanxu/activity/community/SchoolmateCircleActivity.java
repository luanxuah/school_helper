package com.luanxu.activity.community;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.luanxu.adapter.community.SchoolmateCircleAdapter;
import com.luanxu.application.SchoolHelperApplication;
import com.luanxu.base.BaseActivity;
import com.luanxu.custom.PullToRefreshListView;
import com.luanxu.custom.TitleBar;
import com.luanxu.schoolhelper.R;
import com.luanxu.utils.ResourceUtil;

/**
 * @author: LuanXu
 * @createTime:2017/3/6 14:36
 * @className:  SchoolmateCircleActivity
 * @Description: 校友圈
 */

public class SchoolmateCircleActivity extends BaseActivity{
    //上下文对象
    private Activity context;
    //列表
    private PullToRefreshListView list;
    //列表适配器
    private SchoolmateCircleAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_schoolmate_circle);
        SchoolHelperApplication.getInstance().addActivity(this);
        context = this;
        TitleBar bar = getTitleBar();
        bar.setTitle(getResources().getString(R.string.str_schoolmate_circle), R.color.color_white);
        bar.setBack();
        bar.enableRightBtn(ResourceUtil.getString(context, R.string.str_release), -1, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, SchoolmateCircleSendActivity.class);
                startActivity(intent);
            }
        });
        init();
    }

    /**
     * 初始化数据和控件
     */
    private void init(){
        list = (PullToRefreshListView) findViewById(R.id.list);
        adapter = new SchoolmateCircleAdapter(context);
        list.setAdapter(adapter);
    }

}
