package com.luanxu.activity;

import android.os.Bundle;

import com.luanxu.base.BaseActivity;
import com.luanxu.custom.TitleBar;
import com.luanxu.schoolhelper.R;

/**
 * Created by 栾煦 on 2016/12/4.
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
