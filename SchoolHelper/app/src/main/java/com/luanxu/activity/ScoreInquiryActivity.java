package com.luanxu.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.luanxu.adapter.ScoreImquiryAdapter;
import com.luanxu.application.SchoolHelperApplication;
import com.luanxu.base.BaseActivity;
import com.luanxu.custom.TitleBar;
import com.luanxu.schoolhelper.R;

/**
 * @author: LuanXu
 * @createTime:2016/12/20 20:44
 * @className:  ScoreInquiryActivity
 * @Description: 成绩查询
 */

public class ScoreInquiryActivity extends BaseActivity implements View.OnClickListener{
    //上下文对象
    private Context context;

    //学年
    private String year = "大二";
    //学期
    private String term = "第二学期";

    private ScoreImquiryAdapter adapter;

    //选择的时间
    private TextView tv_time;
    //成绩列表
    private ListView lv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_score_inquiry);
        SchoolHelperApplication.getInstance().addActivity(this);
        context = this;

        TitleBar bar = getTitleBar();
        bar.setBack();
        bar.setTitle(getResources().getString(R.string.str_score_query), R.color.color_white);

        init();
    }

    public void init(){
        tv_time = (TextView) findViewById(R.id.tv_time);
        tv_time.setText(year+term);
        tv_time.setOnClickListener(this);
        lv = (ListView) findViewById(R.id.lv);
        adapter = new ScoreImquiryAdapter(context);
        lv.setAdapter(adapter);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.tv_time:
                Intent intent = new Intent(context, SelectYearTermActivity.class);
                intent.putExtra("year", year);
                intent.putExtra("term", term);
                startActivityForResult(intent, 1);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK && requestCode == 1){
            //从选择学期学年页面返回
            if (data!=null){
                year = data.getStringExtra("year");
                term = data.getStringExtra("term");
                tv_time.setText(year+term);
            }
        }
    }
}
