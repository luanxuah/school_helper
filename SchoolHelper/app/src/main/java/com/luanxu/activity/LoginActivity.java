package com.luanxu.activity;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.accessibility.AccessibilityManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.luanxu.application.SchoolHelperApplication;
import com.luanxu.base.BaseActivity;
import com.luanxu.bean.Bean;
import com.luanxu.bean.LoginBean;
import com.luanxu.custom.RoundCornerImageView;
import com.luanxu.custom.percent.PercentLinearLayout;
import com.luanxu.schoolhelper.MainActivity;
import com.luanxu.schoolhelper.R;
import com.luanxu.utils.CommonUtils;
import com.luanxu.utils.ToastUtil;
import com.luanxu.utils.UrlConstant;
import com.luanxu.utils.okhttps.NetCallback;
import com.luanxu.utils.okhttps.OkHttpsUtil;

import java.util.HashMap;
import java.util.Map;

/**
 * @author: LuanXu
 * @createTime:2017/3/3 11:50
 * @className:  LoginActivity
 * @Description: 登录页面
 */

public class LoginActivity extends BaseActivity implements View.OnClickListener{
    //上下文对象
    private Activity context;

    //是否输入用户名，隐藏显示相关控件
    private boolean isInputUsername = true;

    //用户头像
    private RoundCornerImageView iv_head;
    //头像下方的用户名
    private TextView tv_name;
    //用户名输入框
    private EditText et_username;
    //用户名输入框的清空图标
    private PercentLinearLayout pll_phone_close;
    //密码输入框
    private EditText et_password;
    //密码输入框的清空图标
    private PercentLinearLayout pll_psd_close;
    //登录按钮
    private Button bt_login;

    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            finish();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_login);
        SchoolHelperApplication.getInstance().addActivity(this);
        context = this;

        init();
    }

    //初始化数据和控件
    private void init(){
        iv_head = (RoundCornerImageView) findViewById(R.id.iv_head);
        tv_name = (TextView) findViewById(R.id.tv_name);
        et_username = (EditText) findViewById(R.id.et_username);
        et_username.setCursorVisible(true);
        pll_phone_close = (PercentLinearLayout) findViewById(R.id.pll_phone_close);
        et_password = (EditText) findViewById(R.id.et_password);
        et_password.setCursorVisible(true);
        pll_psd_close = (PercentLinearLayout) findViewById(R.id.pll_psd_close);
        bt_login = (Button) findViewById(R.id.bt_login);
        bt_login.setOnClickListener(this);

        setListener();
    }

    private void setListener(){
        et_username.addTextChangedListener(new TextWatcher() {
            int beforeLen = 0;
            int afterLen = 0;

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // 消除文本
                CommonUtils.eliminateText(et_username,pll_phone_close);
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                beforeLen = s.length();
            }

            @Override
            public void afterTextChanged(Editable s) {
                String txt = et_username.getText().toString();
                afterLen = txt.length();
                if (afterLen > beforeLen) {
                    if (txt.length() == 4 || txt.length() == 9) {
                        et_username.setText(new StringBuffer(txt).insert(txt.length() - 1, " ").toString());
                        et_username.setSelection(et_username.getText().length());
                    }
                } else {
                    if (txt.startsWith(" ")) {
                        et_username.setText(new StringBuffer(txt).delete(afterLen - 1, afterLen).toString());
                        et_username.setSelection(et_username.getText().length());
                    }
                }
            }
        });

        et_password.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // 消除文本
                CommonUtils.eliminateText(et_password,pll_psd_close);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private void login(){
        String userName = et_username.getText().toString().trim();
        String passward = et_password.getText().toString().trim();
        if (TextUtils.isEmpty(userName)){
            ToastUtil.show(context, "请输入用户名", Toast.LENGTH_SHORT);
            return;
        }else if (TextUtils.isEmpty(passward)){
            ToastUtil.show(context, "请输入密码", Toast.LENGTH_SHORT);
            return;
        }
        Map<String, String> params = new HashMap<String, String>();
        params.put("userName", userName);
        params.put("passWord", passward);
        OkHttpsUtil.postWithoutToken(context, UrlConstant.LOGIN, "登陆中", params, Bean.class, new NetCallback<Bean>() {
            @Override
            public void onSuccess(Context ctx, Bean bean, Dialog dialog) {
                super.onSuccess(ctx, bean, dialog);
                if (bean!=null){
                    Intent intent = new Intent(context, MainActivity.class);
                    startActivity(intent);
                }
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            //登录
            case R.id.bt_login:
//                login();
                Intent intent = new Intent(context, MainActivity.class);
                startActivity(intent);
                break;
        }
    }
}
