package com.luanxu.activity.user;

import android.os.Bundle;

import com.luanxu.base.BaseActivity;
import com.luanxu.custom.TitleBar;
import com.luanxu.schoolhelper.R;

/**
 * @author: LuanXu
 * @createTime:2017/3/3 11:51
 * @className:  PersonalDetailsActivity
 * @Description: 个人信息页面
 */

public class PersonalDetailsActivity extends BaseActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_personal_details);
        TitleBar bar = getTitleBar();
        bar.setTitle(getResources().getString(R.string.str_personal_msg), R.color.color_white);
        bar.setBack();
    }
}
