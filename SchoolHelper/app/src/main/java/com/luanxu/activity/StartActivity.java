package com.luanxu.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import com.luanxu.application.SchoolHelperApplication;
import com.luanxu.base.BaseActivity;
import com.luanxu.schoolhelper.R;

public class StartActivity extends BaseActivity {
    private static int LOGIN = 1;

    private Activity context;

    @SuppressLint("HandlerLeak")
    public  Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == LOGIN){
                Intent intent = new Intent(context, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_start);
        SchoolHelperApplication.getInstance().addActivity(this);
        context = this;
        handler.sendEmptyMessageDelayed(LOGIN, 2000);
    }
}
