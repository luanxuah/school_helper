package com.luanxu.schoolhelper;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.LinearLayout;

import com.luanxu.application.SchoolHelperApplication;
import com.luanxu.base.BaseActivity;
import com.luanxu.custom.CommonDialog;
import com.luanxu.custom.NoScrollViewPager;

/**
 * Created by 栾煦 on 2016/11/28.
 */
public class MainActivity extends BaseActivity{
    // 主Activity的实例
    private static MainActivity instance;
    // 回收界面用于保存状态的Tag
    private int currentTag = 0;
    // 期刊和文章的Viewpager
    private NoScrollViewPager contentViewPager;
    // 回收界面用于保存状态的Tag
    private String currentTagStr;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.act_main);
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        instance = this;
        currentTagStr = getResources().getString(R.string.current_tag);

        initView();
        initData(savedInstanceState);
        initListener();
        changeTopIndicator(currentTag);

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putInt(currentTagStr, currentTag);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        if (savedInstanceState != null) {
            currentTag = savedInstanceState.getInt(currentTagStr);
        }
    }

    /**
     * 初始化界面
     */
    private void initView() {
        contentViewPager = (NoScrollViewPager) findViewById(R.id.viewpager);
        contentViewPager.setNoScroll(true);
        contentViewPager.setOffscreenPageLimit(4);
        contentViewPager.setAdapter(new MyFragmentAdapterByFactory(getSupportFragmentManager()));
        contentViewPager.setCurrentItem(0);
    }

    /**
     * 初始化相关数据
     * @param savedInstanceState 上一次退出时保存的有用的参数
     */
    public void initData(Bundle savedInstanceState) {
        // 由于此activity被系统回收，保存了一些必要数据，再次回到此activity时，用于界面恢复
        if (savedInstanceState != null) {
            currentTag = savedInstanceState.getInt(currentTagStr);
        }
    }

    /**
     * 注册监听
     */
    private void initListener() {
        // 给底部tab切换的按钮注册监听
        LinearLayout bottomTab = (LinearLayout) findViewById(R.id.ll_bottom_tab);
        int count = bottomTab.getChildCount();
        for (int i = 0; i < count; i++) {
            bottomTab.getChildAt(i).setOnClickListener(new TabChangedListner(i));
        }
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    /**
     * 切换底部按钮
     * @param position 切换到当前位置
     */
    private void changeTopIndicator(int position) {
        LinearLayout bottomTab = (LinearLayout) findViewById(R.id.ll_bottom_tab);
        int count = bottomTab.getChildCount();
        currentTag = position;
        for (int i = 0; i < count; i++) {
            if (i == position) {
                ((ViewGroup) bottomTab.getChildAt(i)).getChildAt(0).setSelected(true);
            } else {
                ((ViewGroup) bottomTab.getChildAt(i)).getChildAt(0).setSelected(false);
            }
        }
    }

    /**
     * 切换Fragment
     * @param position
     */
    public void CustomFragmentManager(int position) {
        currentTag = position;
        contentViewPager.setCurrentItem(position, false);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            exitDialog();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    /**
     * 退出应用
     */
    public void exitDialog() {
        CommonDialog commonDialog = new CommonDialog(instance);
        commonDialog.setTitle("提示");
        commonDialog.setMessage("是否退出医学圈?");
        commonDialog.setPositiveButton(new CommonDialog.BtnClickedListener() {

            @Override
            public void onBtnClicked() {
                SchoolHelperApplication.getInstance().exit();


            }

        }, "确定");
        commonDialog.setCancleButton(null, "取消");
        commonDialog.showDialog();
    }


    public static MainActivity getInstance() {
        return instance;
    }

    /**
     * 底部Tab切换的监听器
     */
    private class TabChangedListner implements View.OnClickListener {

        private int position = 0;

        public TabChangedListner(int position) {
            this.position = position;
        }

        @Override
        public void onClick(View arg0) {
            switch (position) {
                case 0:
                    changeTopIndicator(0);
                    CustomFragmentManager(0);
                    break;
                case 1:
                    changeTopIndicator(1);
                    CustomFragmentManager(1);
                    break;
                case 2:
                    changeTopIndicator(2);
                    CustomFragmentManager(2);
                    break;
                case 3:
                    changeTopIndicator(3);
                    CustomFragmentManager(3);
                    break;
            }
        }
    }
}
