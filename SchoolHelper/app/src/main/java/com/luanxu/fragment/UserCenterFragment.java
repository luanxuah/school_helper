package com.luanxu.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.luanxu.base.BaseFragment;
import com.luanxu.custom.PullScrollView;
import com.luanxu.schoolhelper.R;


/**
 * @author: LuanXu
 * @createTime:2016/12/1 11:09
 * @className:  UserCenterFragment
 * @Description: 我的部分
 */

public class UserCenterFragment extends BaseFragment implements
		PullScrollView.OnTurnListener{

	//下方列表
	private PullScrollView mScrollView;
	//列表顶部
	private View mHeadView;

	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		super.onCreateView(inflater, container, savedInstanceState);
		View view = inflater.inflate(R.layout.frag_account, container, false);
		init(view);
		return view;
	}

	private void init(View view){
		mHeadView = view.findViewById(R.id.headview_acc);
		mScrollView = (PullScrollView) view.findViewById(R.id.scroll_view_acc);
		mScrollView.setHeader(mHeadView);
		mScrollView.setOnTurnListener(this);
	}

	@Override
	public void onTurn() {

	}
}
