package com.luanxu.fragment.message;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.luanxu.adapter.message.MessageItemAdapter;
import com.luanxu.base.BaseFragment;
import com.luanxu.bean.message.MessageTitleBean;
import com.luanxu.custom.BusRefreshListView;
import com.luanxu.schoolhelper.R;

import java.io.Serializable;
import java.util.List;

/**
 * @author: LuanXu
 * @createTime:2017/3/2 15:50
 * @className:  MessageItemFragment
 * @Description: 资讯页面viewPager的item
 */

public class MessageItemFragment extends BaseFragment {
    //上下文对象
    private Context context;
    private View view;

    private MessageItemAdapter adapter;

    //列表
    private BusRefreshListView pullToRefreshListView;

    public static MessageItemFragment newInstance(int position, int pushCount, List<MessageTitleBean> titles) {
        MessageItemFragment f = new MessageItemFragment();
        Bundle b = new Bundle();
        b.putInt("position", position);
        b.putInt("pushCount", pushCount);
        b.putSerializable("_titles", (Serializable) titles);
        f.setArguments(b);
        return f;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = LayoutInflater.from(getActivity()).inflate(R.layout.frag_message_item, null);

        init();

        return view;
    }

    /**
     * 初始化数据和控件
     */
    private void init(){
        pullToRefreshListView = (BusRefreshListView) view.findViewById(R.id.pullToRefreshListView);
        adapter = new MessageItemAdapter(context);
        pullToRefreshListView.setAdapter(adapter);
    }
}
