package com.luanxu.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.luanxu.base.BaseFragment;
import com.luanxu.custom.ReferencePagerSlidingTabStrip;
import com.luanxu.custom.percent.PercentLinearLayout;
import com.luanxu.schoolhelper.R;

/**
 * Created by 栾煦 on 2016/12/25.
 */
public class MessageFragment extends BaseFragment{
    public static int position = 0;
    private Context mActivity;
    // 父类控件
    private View rootParent;
    // 滑动指示器控件
    private ReferencePagerSlidingTabStrip tabs;
    private ViewPager pager;
    // 频道的tab
    private PercentLinearLayout pll_tab;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (rootParent == null) {
            rootParent = LayoutInflater.from(getActivity()).inflate(R.layout.frag_message, null);
        } else {
            ((ViewGroup) rootParent.getParent()).removeView(rootParent);
        }
        return rootParent;
    }

    @Override
    public void onStart() {
        super.onStart();
        if (rootParent == null) {
            rootParent = LayoutInflater.from(getActivity()).inflate(R.layout.frag_message,  null);
        }
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mActivity = getActivity();

        initData();
    }

    /**
     * 初始化数据
     */
    public void initData() {
        tabs = (ReferencePagerSlidingTabStrip) rootParent.findViewById(R.id.tabs);
        pager = (ViewPager) rootParent.findViewById(R.id.pager);
        pll_tab = (PercentLinearLayout) rootParent.findViewById(R.id.pll_tab);
    }
}
