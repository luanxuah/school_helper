package com.luanxu.activity.community;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.luanxu.adapter.MyFragmentPagerAdapter;
import com.luanxu.application.SchoolHelperApplication;
import com.luanxu.base.BaseActivity;
import com.luanxu.custom.NoScrollViewPager;
import com.luanxu.fragment.community.FoundFragment;
import com.luanxu.fragment.community.LostFragment;
import com.luanxu.schoolhelper.R;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

/**
 * @author: LuanXu
 * @createTime:2017/3/29 15:54
 * @className:  LostAndFoundActivity
 * @Description: 失物招领页面
 */

public class LostAndFoundActivity extends BaseActivity implements View.OnClickListener{
    //上下文对象
    private Activity context;

    private ArrayList<Fragment> fragmentList;
    private int currentPos = 0;

    //失物的页面
    private Fragment lostFragment;
    //招领的页面
    private Fragment foundFragment;

    //返回按钮
    private ImageView iv_back;
    //失物按钮
    private TextView tv_lost;
    //招领按钮
    private TextView tv_found;
    //发送按钮
    private ImageView iv_send;
    private NoScrollViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_lost_and_found);
        SchoolHelperApplication.getInstance().addActivity(this);
        context = this;

        init();
    }

    /**
     * 初始化
     */
    private void init(){
        iv_back = (ImageView) findViewById(R.id.iv_back);
        iv_back.setOnClickListener(this);
        tv_lost = (TextView) findViewById(R.id.tv_lost);
        tv_lost.setOnClickListener(this);
        tv_found = (TextView) findViewById(R.id.tv_found);
        tv_found.setOnClickListener(this);
        iv_send = (ImageView) findViewById(R.id.iv_send);
        iv_send.setOnClickListener(this);
        viewPager = (NoScrollViewPager) findViewById(R.id.viewPager);
        viewPager.setNoScroll(true);

        initDate();
    }

    /**
     * 初始化数据
     */
    private void initDate(){
        lostFragment = new LostFragment();
        fragmentList = new ArrayList<Fragment>();
        fragmentList.add(lostFragment);

        //设置适配器
        FragmentManager fragmentManager = getSupportFragmentManager();
        final MyFragmentPagerAdapter pagerAdapter = new MyFragmentPagerAdapter(fragmentManager, fragmentList);
        viewPager.setAdapter(pagerAdapter);

        //把当前页面设置为失物页面
        tv_lost.setTextColor(context.getResources().getColor(R.color.color_black));
        tv_found.setTextColor(context.getResources().getColor(R.color.color_white));
        tv_lost.setSelected(true);
        tv_found.setSelected(false);
        viewPager.setCurrentItem(currentPos);

        //延时用于优化失物招领页面启动时界面卡顿
        SchoolHelperApplication.getInstance().getThreadPool().schedule(new Runnable() {
            @Override
            public void run() {
                if (context.isFinishing()){
                    return;
                }
                context.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        foundFragment = new FoundFragment();
                        fragmentList.add(foundFragment);
                        pagerAdapter.notifyDataSetChanged();
                    }
                });
            }
        },500, TimeUnit.MILLISECONDS);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.tv_lost:
                //点击失物按钮
                currentPos = 0;
                tv_lost.setTextColor(context.getResources().getColor(R.color.color_black));
                tv_found.setTextColor(context.getResources().getColor(R.color.color_white));
                tv_lost.setSelected(true);
                tv_found.setSelected(false);
                viewPager.setCurrentItem(currentPos);
                break;
            case R.id.tv_found:
                //点击招领按钮
                currentPos = 1;
                tv_lost.setTextColor(context.getResources().getColor(R.color.color_white));
                tv_found.setTextColor(context.getResources().getColor(R.color.color_black));
                tv_lost.setSelected(false);
                tv_found.setSelected(true);
                viewPager.setCurrentItem(currentPos);
                break;
            case R.id.iv_back:
                finish();
                break;
            case R.id.iv_send:

                break;
        }
    }
}
