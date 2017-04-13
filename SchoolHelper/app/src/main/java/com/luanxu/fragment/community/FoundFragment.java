package com.luanxu.fragment.community;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.luanxu.adapter.community.LostAdapter;
import com.luanxu.base.BaseFragment;
import com.luanxu.custom.PullToRefreshListView;
import com.luanxu.schoolhelper.R;

/**
 * @author: LuanXu
 * @createTime:2017/3/29 18:45
 * @className:  FoundFragment
 * @Description: 招领页面
 */

public class FoundFragment extends BaseFragment{
    //上下文对象
    private Activity context;

    //失物适配器
    private LostAdapter adapter;
    //列表
    private PullToRefreshListView list;

    private View view;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.from(context).inflate(R.layout.layout_listview, null);

        init();

        return view;
    }

    /**
     * 初始化数据和控件
     */
    private void init(){
        list = (PullToRefreshListView) view.findViewById(R.id.list);
        adapter = new LostAdapter(context, true);
        list.setAdapter(adapter);
    }

}
