package com.luanxu.activity;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.luanxu.application.SchoolHelperApplication;
import com.luanxu.base.BaseActivity;
import com.luanxu.custom.TitleBar;
import com.luanxu.schoolhelper.R;

/**
 * @author: LuanXu
 * @createTime:2016/12/23 15:51
 * @className:  ChangePasswardActivity
 * @Description: 修改密码页面
 */

public class ChangePasswardActivity extends BaseActivity{
    //上下文对象
    private Context context;

    //原始密码
    private EditText et_last_password;
    //新密码
    private EditText et_password;
    //再次输入新密码
    private EditText et_password_two;
    //确认按钮
    private Button btn_confirm;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_change_password);
        SchoolHelperApplication.getInstance().addActivity(this);
        context = this;

        TitleBar bar = getTitleBar();
        bar.setTitle(getResources().getString(R.string.str_update_passward), R.color.color_white);
        bar.setBack();

        init();
    }

    /**
     * 初始化数据和控件
     */
    private void init(){
        et_last_password = (EditText) findViewById(R.id.et_last_password);
        et_password = (EditText) findViewById(R.id.et_password);
        et_password_two = (EditText) findViewById(R.id.et_password_two);
        btn_confirm = (Button) findViewById(R.id.btn_confirm);
        btn_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }
}
