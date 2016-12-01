package com.luanxu.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.luanxu.adapter.ActMyCreditAdpter;
import com.luanxu.base.BaseFragment;
import com.luanxu.custom.TitleBar;
import com.luanxu.schoolhelper.R;

/**
 * Created by 栾煦 on 2016/11/29.
 */
public class CreditFragment extends BaseFragment{
    private double[] yData = new double[] { 0, 0, 0, 0,0 };

    private ListView pullToRefreshListView;

    private ActMyCreditAdpter adpter;

    private LinearLayout noData;
    @SuppressWarnings("unused")
    private LinearLayout loadFail;
    //上下文
    private Context mContext;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.act_my_credit, null);
        mContext=getActivity();

        init(view);

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        TitleBar bar = getTitleBar();
        bar.setTitle(getResources().getString(R.string.credit), R.color.white);
    }

    private void init(View view) {

        pullToRefreshListView = (ListView) view.findViewById(R.id.pullToRefreshListView);
        adpter=new ActMyCreditAdpter(mContext);
        pullToRefreshListView.setAdapter(adpter);
    }

    public String crediteString(String s){
        if (null == s || s.equals("") || TextUtils.isEmpty(s) || s.equals("null") ) {
            s = "0";
        }
        return s;
    }
}
