package com.luanxu.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.PopupWindow;

import com.luanxu.adapter.MovementAdapter;
import com.luanxu.application.SchoolHelperApplication;
import com.luanxu.base.BaseActivity;
import com.luanxu.custom.PullToRefreshListView;
import com.luanxu.custom.TitleBar;
import com.luanxu.schoolhelper.R;

/**
 * @author: LuanXu
 * @createTime:2017/1/3 19:47
 * @className:  MovementActivity
 * @Description: 校内活动页面
 */

public class MovementActivity extends BaseActivity{
    //上下文对象
    private Context context;

    private MovementAdapter adapter;
    // 更多弹窗
    private PopupWindow morePop;

    //列表
    private PullToRefreshListView list;
    //蒙版
    private View view_gray;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_movement);
        SchoolHelperApplication.getInstance().addActivity(this);
        context = this;

        TitleBar bar = getTitleBar();
        bar.setTitle(getResources().getString(R.string.str_movement), R.color.color_white);
        bar.enableRightBtn(null, R.mipmap.img_title_more, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popupWindow(view);
            }
        });

        init();
    }

    /**
     * 初始化数据和控件
     */
    private void init(){
        view_gray = findViewById(R.id.view_gray);
        list = (PullToRefreshListView) findViewById(R.id.list);
        adapter = new MovementAdapter(context);
        list.setAdapter(adapter);

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(context, MovementDetailActivity.class);
                startActivity(intent);
            }
        });
    }

    /**
     * @param v 控件
     * @Description: title更多弹框
     * @return: void
     */
    private void popupWindow(View v) {
        view_gray.setVisibility(View.VISIBLE);
        final View popRoot = LayoutInflater.from(context).inflate(R.layout.pop_movement_detail, null);
        // 创建PopupWindow实例, 分别是宽度和高度
        morePop = new PopupWindow(popRoot, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);
        int[] location = new int[2];
        v.getLocationOnScreen(location);// v是title的右部，撑满整个title的高度，是一样高的
        morePop.showAtLocation(findViewById(R.id.title), Gravity.NO_GRAVITY, location[0], location[1] + findViewById(R.id.title).getHeight()-20);
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

        //发布活动
        popRoot.findViewById(R.id.ll_release).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, MipcaActivityCapture.class);
                startActivity(intent);
            }
        });

        //消息
        popRoot.findViewById(R.id.ll_message).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        morePop.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                view_gray.setVisibility(View.GONE);
            }
        });

    }
}
