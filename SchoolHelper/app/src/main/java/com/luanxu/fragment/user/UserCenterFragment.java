package com.luanxu.fragment.user;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.luanxu.activity.user.CreditActivity;
import com.luanxu.activity.user.EmptyClassroomActivity;
import com.luanxu.activity.user.PersonalDetailsActivity;
import com.luanxu.activity.user.ScoreInquiryActivity;
import com.luanxu.activity.user.SetActivity;
import com.luanxu.application.SchoolHelperApplication;
import com.luanxu.base.BaseFragment;
import com.luanxu.custom.CircleImageView;
import com.luanxu.custom.CommonDialog;
import com.luanxu.custom.TitleBar;
import com.luanxu.custom.percent.PercentLinearLayout;
import com.luanxu.schoolhelper.R;
import com.luanxu.utils.ResourceUtil;


/**
 * @author: LuanXu
 * @createTime:2016/12/1 11:09
 * @className:  UserCenterFragment
 * @Description: 我的部分
 */

public class UserCenterFragment extends BaseFragment implements View.OnClickListener{
	//上下文对象
	private Context context;

	//个人信息
	private PercentLinearLayout pll_head;
	//头像
	private CircleImageView raiv_head;
	//姓名
	private TextView tv_name;
	//学院
	private TextView tv_college;
	//学分查询
	private PercentLinearLayout pll_credit;
	//成绩查询
	private PercentLinearLayout pll_score;
	//空教室查询
	private PercentLinearLayout pll_empty_classroom;
	//收藏
	private PercentLinearLayout pll_collect;
	//设置
	private PercentLinearLayout pll_set;
	//登录按钮
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
		TitleBar bar = getTitleBar();
		bar.setTitle(getResources().getString(R.string.str_me), R.color.color_white);
	}

	/**
	 * 初始化数据
	 * @param view
     */
	private void initView(View view){
		pll_head = (PercentLinearLayout) view.findViewById(R.id.pll_head);
		pll_head.setOnClickListener(this);
		raiv_head = (CircleImageView) view.findViewById(R.id.raiv_head);
		tv_name = (TextView) view.findViewById(R.id.tv_name);
		tv_college = (TextView) view.findViewById(R.id.tv_college);
		pll_credit = (PercentLinearLayout) view.findViewById(R.id.pll_credit);
		pll_credit.setOnClickListener(this);
		pll_score = (PercentLinearLayout) view.findViewById(R.id.pll_score);
		pll_score.setOnClickListener(this);
		pll_empty_classroom = (PercentLinearLayout) view.findViewById(R.id.pll_empty_classroom);
		pll_empty_classroom.setOnClickListener(this);
		pll_collect = (PercentLinearLayout) view.findViewById(R.id.pll_collect);
		pll_collect.setOnClickListener(this);
		pll_set = (PercentLinearLayout) view.findViewById(R.id.pll_set);
		pll_set.setOnClickListener(this);
		btn_logout = (TextView) view.findViewById(R.id.btn_logout);
		btn_logout.setOnClickListener(this);
	}

	@Override
	public void onClick(View view) {
		Intent intent = null;
		switch (view.getId()){
			case R.id.pll_credit:
				//点击学分查询
				intent = new Intent(context, CreditActivity.class);
				startActivity(intent);
				break;
			case R.id.pll_head:
				//点击头像进入个人信息页面
				intent = new Intent(context, PersonalDetailsActivity.class);
				startActivity(intent);
				break;
			case R.id.pll_score:
				//点击成绩查询
				intent = new Intent(context, ScoreInquiryActivity.class);
				startActivity(intent);
				break;
			case R.id.pll_empty_classroom:
				//空教室查询
				intent = new Intent(context, EmptyClassroomActivity.class);
				startActivity(intent);
				break;
			case R.id.pll_collect:

				break;
			case R.id.pll_set:
				//设置
				intent = new Intent(context, SetActivity.class);
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
