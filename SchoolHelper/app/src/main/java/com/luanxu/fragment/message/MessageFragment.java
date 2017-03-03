package com.luanxu.fragment.message;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.luanxu.adapter.message.MessagePageAdapter;
import com.luanxu.base.BaseFragment;
import com.luanxu.bean.message.MessageTitleBean;
import com.luanxu.custom.ReferencePagerSlidingTabStrip;
import com.luanxu.custom.TitleBar;
import com.luanxu.custom.percent.PercentLinearLayout;
import com.luanxu.schoolhelper.R;

import java.util.ArrayList;
import java.util.List;

/**
 * @author: LuanXu
 * @createTime:2017/3/2 15:49
 * @className:  MessageFragment
 * @Description: 资讯页面
 */

public class MessageFragment extends BaseFragment implements View.OnClickListener{
    //上下文对象
    private Context mActivity;

    public static int position = 0;

    //顶部栏目集合
    private List<MessageTitleBean> titles;

    //viewPage适配器
    private MessagePageAdapter pageAdapter;

    // 父类控件
    private View rootParent;
    // 滑动指示器控件
    private ReferencePagerSlidingTabStrip tabs;
    private ViewPager pager;
    // 频道的tab
    private PercentLinearLayout pll_tab;
    //添加更多
    private PercentLinearLayout btnMore;

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

        TitleBar bar = getTitleBar();
        bar.setTitle(getResources().getString(R.string.str_information), R.color.color_white);

        init();
    }

    /**
     * 初始化数据
     */
    public void init() {
        initDate();
        btnMore = (PercentLinearLayout) rootParent.findViewById(R.id.btnMore);
        btnMore.setOnClickListener(this);
        tabs = (ReferencePagerSlidingTabStrip) rootParent.findViewById(R.id.tabs);
        pager = (ViewPager) rootParent.findViewById(R.id.pager);
        pll_tab = (PercentLinearLayout) rootParent.findViewById(R.id.pll_tab);
        pageAdapter = new MessagePageAdapter(getChildFragmentManager(), titles);
        pager.setAdapter(pageAdapter);
        tabs.setViewPager(pager);
    }

    /**
     * 初始化数据
     */
    private void initDate(){
        titles = new ArrayList<MessageTitleBean>();
        MessageTitleBean bean1 = new MessageTitleBean("安徽师范大学", "11");
        MessageTitleBean bean2 = new MessageTitleBean("安徽师范大学学生会", "11");
        MessageTitleBean bean3 = new MessageTitleBean("安徽师范大学数计学院", "11");
        MessageTitleBean bean4 = new MessageTitleBean("安徽师范大学经管学院", "11");
        MessageTitleBean bean5 = new MessageTitleBean("安徽师范大学物电学院", "11");
        MessageTitleBean bean6 = new MessageTitleBean("安徽师范大学空乘学院", "11");
        titles.add(bean1);
        titles.add(bean2);
        titles.add(bean3);
        titles.add(bean4);
        titles.add(bean5);
        titles.add(bean6);
    }

    @Override
    public void onClick(View view) {
        //点击添加更多频道

    }
}
