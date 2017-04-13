package com.luanxu.activity.community;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupWindow;
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
    //更多按钮
    private ImageView iv_more;
    private NoScrollViewPager viewPager;
    //蒙版
    private View view;

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
        iv_more = (ImageView) findViewById(R.id.iv_more);
        iv_more.setOnClickListener(this);
        viewPager = (NoScrollViewPager) findViewById(R.id.viewPager);
        viewPager.setNoScroll(true);
        view = findViewById(R.id.view);
        view.setOnClickListener(this);
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

    // 更多弹窗
    private PopupWindow morePop;

    /**
     * @param v 控件
     * @Description: title更多弹框
     * @return: void
     */
    private void popupWindow(View v) {
        view.setVisibility(View.VISIBLE);
        final View popRoot = LayoutInflater.from(context).inflate(R.layout.pop_lost_found, null);
        // 创建PopupWindow实例, 分别是宽度和高度
        morePop = new PopupWindow(popRoot, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);
        morePop.showAsDropDown(findViewById(R.id.ll_head));
        // 点击其他地方消失
        popRoot.setOnTouchListener(new View.OnTouchListener() {
            @SuppressLint("ClickableViewAccessibility")
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (morePop != null && morePop.isShowing()) {
                    morePop.dismiss();
                    morePop = null;
                }
                return false;
            }
        });
        popRoot.setFocusable(true);
        popRoot.setFocusableInTouchMode(true);
        popRoot.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    morePop.dismiss();
                    morePop = null;
                    return true;
                }
                return false;
            }
        });

        //发布
        popRoot.findViewById(R.id.ll_send).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                morePop.dismiss();
                Intent intent = new Intent(context, LostAndFoundSendActivity.class);
                startActivity(intent);
            }
        });

        //消息
        popRoot.findViewById(R.id.ll_message).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                morePop.dismiss();
            }
        });

        morePop.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                view.setVisibility(View.GONE);
            }
        });

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
            case R.id.iv_more:
                //点击更多按钮
                popupWindow(iv_more);
                break;
            case R.id.view:
                //点击蒙版
                view.setVisibility(View.GONE);
                break;
        }
    }
}
