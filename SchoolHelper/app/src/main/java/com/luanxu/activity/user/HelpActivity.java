package com.luanxu.activity.user;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.luanxu.application.SchoolHelperApplication;
import com.luanxu.base.BaseActivity;
import com.luanxu.custom.TitleBar;
import com.luanxu.custom.percent.PercentLinearLayout;
import com.luanxu.schoolhelper.R;

/**
 * @author: LuanXu
 * @createTime:2016/12/22 20:59
 * @className:  HelpActivity
 * @Description: 帮助和反馈页面
 */

public class HelpActivity extends BaseActivity implements View.OnClickListener{
    //上下文对象
    private Context context;
    //第一个电话的外部布局
    private PercentLinearLayout pll_phone_one;
    //第一个电话
    private TextView tv_phone_one;
    //第二个电话的外部布局
    private PercentLinearLayout pll_phone_two;
    //第二个电话
    private TextView tv_phone_two;
    //第一个qq的外部布局
    private PercentLinearLayout pll_qq_one;
    //第一个qq
    private TextView tv_qq_one;
    //第二个qq的外部布局
    private PercentLinearLayout pll_qq_two;
    //第二个qq
    private TextView tv_qq_two;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_help);
        SchoolHelperApplication.getInstance().addActivity(this);
        context = this;

        TitleBar bar = getTitleBar();
        bar.setTitle(getResources().getString(R.string.str_help), R.color.color_white);
        bar.setBack();

        init();
    }

    private void init(){
        pll_phone_one = (PercentLinearLayout) findViewById(R.id.pll_phone_one);
        pll_phone_one.setOnClickListener(this);
        tv_phone_one = (TextView) findViewById(R.id.tv_phone_one);
        pll_phone_two = (PercentLinearLayout) findViewById(R.id.pll_phone_two);
        pll_phone_two.setOnClickListener(this);
        tv_phone_two = (TextView) findViewById(R.id.tv_phone_two);
        pll_qq_one = (PercentLinearLayout) findViewById(R.id.pll_qq_one);
        pll_qq_one.setOnClickListener(this);
        tv_qq_one = (TextView) findViewById(R.id.tv_qq_one);
        pll_qq_two = (PercentLinearLayout) findViewById(R.id.pll_qq_two);
        pll_qq_two.setOnClickListener(this);
        tv_qq_two = (TextView) findViewById(R.id.tv_qq_two);
    }

    @Override
    public void onClick(View view) {
        Intent intent = null;
        switch (view.getId()){
            case R.id.pll_phone_one:
                //点击第一个电话
                intent = new Intent(Intent.ACTION_CALL,Uri.parse("tel:"+tv_phone_one.getText().toString()));
                break;
            case R.id.pll_phone_two:
                //点击第二个电话
                intent = new Intent(Intent.ACTION_CALL,Uri.parse("tel:"+tv_phone_two.getText().toString()));
                break;
            case R.id.pll_qq_one:
                //第一个qq
                String url="mqqwpa://im/chat?chat_type=wpa&uin="+tv_qq_one.getText().toString();
                intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                break;
            case R.id.pll_qq_two:
                //第二个qq
                String url2="mqqwpa://im/chat?chat_type=wpa&uin="+tv_qq_two.getText().toString();
                intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url2));
                break;
        }
        startActivity(intent);
    }
}
