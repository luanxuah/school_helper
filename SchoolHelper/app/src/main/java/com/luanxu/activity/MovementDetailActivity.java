package com.luanxu.activity;

import android.content.Context;
import android.os.Bundle;

import com.luanxu.application.SchoolHelperApplication;
import com.luanxu.base.BaseActivity;
import com.luanxu.custom.TitleBar;
import com.luanxu.schoolhelper.R;

/**
 * @author: LuanXu
 * @createTime:2017/1/3 20:18
 * @className:  MovementDetailActivity
 * @Description: 活动详情页面
 */

public class MovementDetailActivity extends BaseActivity{
    private Context context;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_movement_detail);
        SchoolHelperApplication.getInstance().addActivity(this);
        context = this;

        TitleBar bar = getTitleBar();
        bar.setTitle(getResources().getString(R.string.str_movement_detail), R.color.color_white);
    }
}
