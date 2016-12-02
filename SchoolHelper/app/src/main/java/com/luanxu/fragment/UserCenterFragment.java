package com.luanxu.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.luanxu.activity.CreditActivity;
import com.luanxu.base.BaseFragment;
import com.luanxu.custom.CircleImageView;
import com.luanxu.custom.PullScrollView;
import com.luanxu.schoolhelper.R;


/**
 * @author: LuanXu
 * @createTime:2016/12/1 11:09
 * @className:  UserCenterFragment
 * @Description: 我的部分
 */

public class UserCenterFragment extends BaseFragment implements
		PullScrollView.OnTurnListener, View.OnClickListener{
	private Context context;
	//下方列表
	private PullScrollView mScrollView;
	//列表顶部
	private View mHeadView;
	//用户头像
	private CircleImageView civ_head;
	//用户名
	private TextView tv_username;
	//学院
	private TextView tv_academy;
	//班级
	private TextView tv_class;
	//学分查询
	private LinearLayout ll_circle;
	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		super.onCreateView(inflater, container, savedInstanceState);
		View view = inflater.inflate(R.layout.frag_account, container, false);
		initView(view);
		return view;
	}

	@Override
	public void onActivityCreated(@Nullable Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		context = getActivity();
	}

	/**
	 * 初始化数据
	 * @param view
     */
	private void initView(View view){
		mHeadView = view.findViewById(R.id.headview_acc);
		mScrollView = (PullScrollView) view.findViewById(R.id.scroll_view_acc);
		mScrollView.setHeader(mHeadView);
		mScrollView.setOnTurnListener(this);
		civ_head = (CircleImageView) view.findViewById(R.id.civ_head);
		tv_username = (TextView) view.findViewById(R.id.tv_username);
		tv_academy = (TextView) view.findViewById(R.id.tv_academy);
		tv_class = (TextView) view.findViewById(R.id.tv_class);
		ll_circle = (LinearLayout) view.findViewById(R.id.ll_circle);
		ll_circle.setOnClickListener(this);
	}

	@Override
	public void onTurn() {

	}

	@Override
	public void onClick(View view) {
		Intent intent = null;
		switch (view.getId()){
			//点击学分查询
			case R.id.ll_circle:
				intent = new Intent(context, CreditActivity.class);
				startActivity(intent);
				break;
		}
	}
}
