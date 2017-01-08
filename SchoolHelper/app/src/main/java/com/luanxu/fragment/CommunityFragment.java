package com.luanxu.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.luanxu.activity.MovementActivity;
import com.luanxu.base.BaseFragment;
import com.luanxu.custom.TitleBar;
import com.luanxu.custom.percent.PercentFrameLayout;
import com.luanxu.schoolhelper.R;

/**
 * @author: LuanXu
 * @createTime:2016/12/28 19:34
 * @className:  CommunityFragment
 * @Description: 社区
 */

public class CommunityFragment extends BaseFragment implements View.OnClickListener{
    //上下文对象
    private Context context;

    private View view;
    //校友圈
    private PercentFrameLayout pfl_schoolmate_circle;
    //失物招领
    private PercentFrameLayout pfl_help;
    //表白墙
    private PercentFrameLayout pfl_love;
    //校内活动
    private PercentFrameLayout pfl_activity;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (view == null) {
            view = LayoutInflater.from(getActivity()).inflate(R.layout.frag_community, null);
        } else {
            ((ViewGroup) view.getParent()).removeView(view);
        }
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        context = getActivity();
        TitleBar bar = getTitleBar();
        bar.setTitle(getResources().getString(R.string.str_community), R.color.color_white);

        init();
    }

    /**
     * 初始化数据和控件
     */
    private void init(){
        pfl_schoolmate_circle = (PercentFrameLayout) view.findViewById(R.id.pfl_schoolmate_circle);
        pfl_schoolmate_circle.setOnClickListener(this);
        pfl_help = (PercentFrameLayout) view.findViewById(R.id.pfl_help);
        pfl_help.setOnClickListener(this);
        pfl_love = (PercentFrameLayout) view.findViewById(R.id.pfl_love);
        pfl_love.setOnClickListener(this);
        pfl_activity = (PercentFrameLayout) view.findViewById(R.id.pfl_activity);
        pfl_activity.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        Intent intent = null;
        switch (view.getId()){
            case R.id.pfl_schoolmate_circle:
                //校友圈
                break;
            case R.id.pfl_help:
                //失物招领
                break;
            case R.id.pfl_love:
                //表白墙
                break;
            case R.id.pfl_activity:
                //校内活动
                intent = new Intent(context, MovementActivity.class);
                startActivity(intent);
                break;
        }
    }
}
