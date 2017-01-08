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
import com.luanxu.utils.CommonConstant;

import java.io.File;

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

        createDirectory();
    }

    /**
     * 判断缓存路径是否存在，不存在的话创建
     */
    private void createDirectory(){
        File file = new File(CommonConstant.CACHE_PATH);
        if (!file.exists()){
            file.mkdirs();
        }
    }
}
