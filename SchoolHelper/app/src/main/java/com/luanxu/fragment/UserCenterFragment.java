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
import com.luanxu.activity.PersonalDetailsActivity;
import com.luanxu.application.SchoolHelperApplication;
import com.luanxu.base.BaseFragment;
import com.luanxu.custom.CircleImageView;
import com.luanxu.custom.CommonDialog;
import com.luanxu.custom.PullScrollView;
import com.luanxu.schoolhelper.R;
import com.luanxu.utils.ResourceUtil;


/**
 * @author: LuanXu
 * @createTime:2016/12/1 11:09
 * @className:  UserCenterFragment
 * @Description: 我的部分
 */

public class UserCenterFragment extends BaseFragment implements
		PullScrollView.OnTurnListener, View.OnClickListener{
	//上下文对象
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
	private LinearLayout ll_credit;
	//退出按钮
	private TextView btn_logout;

	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		super.onCreateView(inflater, container, savedInstanceState);
		View view = inflater.inflate(R.layout.frag_user_center, container, false);
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
		mHeadView = view.findViewById(R.id.rl_head);
		mScrollView = (PullScrollView) view.findViewById(R.id.psv);
		mScrollView.setHeader(mHeadView);
		mScrollView.setOnTurnListener(this);
		civ_head = (CircleImageView) view.findViewById(R.id.civ_head);
		civ_head.setOnClickListener(this);
		tv_username = (TextView) view.findViewById(R.id.tv_name);
		tv_academy = (TextView) view.findViewById(R.id.tv_academy);
		tv_class = (TextView) view.findViewById(R.id.tv_class);
		ll_credit = (LinearLayout) view.findViewById(R.id.ll_credit);
		ll_credit.setOnClickListener(this);
		btn_logout = (TextView) view.findViewById(R.id.btn_logout);
		btn_logout.setOnClickListener(this);
	}

	@Override
	public void onTurn() {

	}

	@Override
	public void onClick(View view) {
		Intent intent = null;
		switch (view.getId()){
			case R.id.ll_credit:
				//点击学分查询
				intent = new Intent(context, CreditActivity.class);
				startActivity(intent);
				break;
			case R.id.civ_head:
				//点击头像进入个人信息页面
				intent = new Intent(context, PersonalDetailsActivity.class);
				startActivity(intent);
				break;
			case R.id.btn_logout:
				//退出应用
				CommonDialog commonDialog = new CommonDialog(context);
				commonDialog.setTitle(ResourceUtil.getString(context, R.string.str_prompt));
				commonDialog.setMessage(ResourceUtil.getString(context, R.string.str_out_of_application));
				commonDialog.setPositiveButton(new CommonDialog.BtnClickedListener() {

					@Override
					public void onBtnClicked() {
						SchoolHelperApplication.getInstance().exit();
					}
				}, ResourceUtil.getString(context, R.string.str_ok));
				commonDialog.setCancleButton(null, ResourceUtil.getString(context, R.string.str_cancle));
				commonDialog.showDialog();
				break;
		}
	}
}
