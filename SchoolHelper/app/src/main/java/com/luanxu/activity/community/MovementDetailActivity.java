package com.luanxu.activity.community;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.PopupWindow;

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
    //上下文对象
    private Context context;

    // 更多弹窗
    private PopupWindow morePop;

    //蒙版
    private View view_gray;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_movement_detail);
        SchoolHelperApplication.getInstance().addActivity(this);
        context = this;

        TitleBar bar = getTitleBar();
        bar.setTitle(getResources().getString(R.string.str_movement_detail), R.color.color_white);
        bar.setBack();
        bar.enableRightBtn(null, R.mipmap.img_title_more, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            }
        });

        init();
    }

    private void init(){
        view_gray = findViewById(R.id.view_gray);
    }
}
