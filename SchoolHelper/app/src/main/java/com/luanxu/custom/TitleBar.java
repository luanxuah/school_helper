package com.luanxu.custom;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.luanxu.custom.percent.PercentLinearLayout;
import com.luanxu.schoolhelper.R;

public class TitleBar {

	private Activity mActivity = null;
	public TextView backBtn = null;// 返回按钮
	private PercentLinearLayout leftView = null;
	private PercentLinearLayout rightView = null;
	private PercentLinearLayout rightView1 = null;
	private TextView rightBtn = null;
	private TextView rightBtn1 = null;
	private TextView titleTxt = null;// 标题
	private LinearLayout titleLine = null; // 标题

	private View titleView = null; // 标识是否有监听事件

	public TitleBar(Activity activity, View parent) {
		mActivity = activity;
		initWidget(parent);
	}

	public void setTitle(String title, int color) {
		if (!TextUtils.isEmpty(title)) {
			titleTxt.setBackgroundResource(0);
			titleTxt.setTextColor(mActivity.getResources().getColor(R.color.color_white));
			titleTxt.setText(title);
		}
		if (color > 0) {
			titleTxt.setTextColor(mActivity.getResources().getColor(R.color.color_white));
		}
	}

	public void setTitle(int title, int color) {
		if (title > 0) {
			titleTxt.setText(mActivity.getResources().getString(title));
		}
		if (color > 0) {
			titleTxt.setTextColor(mActivity.getResources().getColor(color));
		}
	}

	/**白色字体*/
	public void setTitle(String str) {
		titleTxt.setText(str);
		titleTxt.setTextColor(mActivity.getResources().getColor(R.color.color_white));
	}

	public void setBackground(int index) {
		if (titleView != null)
			titleView.setBackgroundColor(mActivity.getResources().getColor(index));
	}

	public void enableBack() {
		backBtn.setVisibility(View.VISIBLE);
		// 默认返回事件，关闭当前activity
		backBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				mActivity.finish();
			}
		});
	}

	public TextView getBackBtn() {
		return backBtn;
	}

	/**默认 没有文字 图标为返回的图标 默认监听事件为关闭当前界面*/
	public void setBack() {
		backBtn.setVisibility(View.VISIBLE);
		backBtn.setCompoundDrawablesWithIntrinsicBounds(R.mipmap.ic_back, 0, 0, 0);
		leftView.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				mActivity.finish();
			}
		});
	}

	/** setBack返回 */
	public void setBack(String backStr, OnClickListener clickListener, int icon) {
		backBtn.setVisibility(View.VISIBLE);
		if (!TextUtils.isEmpty(backStr)) { 
			backBtn.setText(backStr);
			backBtn.setTextColor(mActivity.getResources().getColor(R.color.color_white));
		}

		if (icon > 0) { // 设置图片在文字的左边
			backBtn.setCompoundDrawablesWithIntrinsicBounds(icon, 0, 0, 0);
		}

		if (clickListener != null) { // 用户自定义返回事�?
			leftView.setOnClickListener(clickListener);
		} else { // 默认返回事件，关闭当前activity
			leftView.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) { 
					mActivity.finish();
				}
			});
		}
	}

	public void setRightBtnText(String btnStr) {
		rightBtn.setVisibility(View.VISIBLE);
		if (btnStr != null) {
			rightBtn.setText(btnStr);
		}
	}

	public void enableRightBtn(String btnStr, int icon, View.OnClickListener clickListener) {

		rightBtn.setVisibility(View.VISIBLE);
		if (btnStr != null) {
			rightBtn.setText(btnStr);
		}
		if (icon > 0) {
			rightBtn.setCompoundDrawablesWithIntrinsicBounds(icon, 0, 0, 0);
		}
		if (clickListener != null) {
			rightView.setOnClickListener(clickListener);
		}
	}
	public void enableRightBtn1(String btnStr, int icon, View.OnClickListener clickListener) {
		 
		rightView1.setVisibility(View.VISIBLE);
//		if (!TextUtils.isEmpty(btnStr)) {
			rightBtn1.setText(btnStr);
//		}
		if (icon > 0) {
			rightBtn1.setCompoundDrawablesWithIntrinsicBounds(0, 0, icon, 0);
		}
		if (clickListener != null) {
			rightView1.setOnClickListener(clickListener);
		}else {
			rightView1.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
				}
			});
		}
	}

	public void enableRightBtn(int icon) {
		rightBtn.setVisibility(View.VISIBLE);
		if (icon > 0) {
			rightBtn.setCompoundDrawablesWithIntrinsicBounds(0, 0, icon, 0);
		}
	}

	public TextView getRightContent() {
		return rightBtn;
	}

	public void enableRightBtn(String icon) {
		rightBtn.setVisibility(View.VISIBLE);
		rightBtn.setText(icon);
	}

	@SuppressLint("NewApi")
	public void enableRightBtn(int drawableId, View.OnClickListener clickListener) {
		if (clickListener != null) { // 用户自定义事 
			rightBtn.setOnClickListener(clickListener);
		}
	}

	public void setRightBtnVisibility(int visibility) {
		rightBtn.setVisibility(visibility);
	}

	public void setRightBtn1Visibility(int visibility) {
		rightBtn1.setVisibility(visibility);
	}

	public void addCustomTitleView(View view) {
		if (titleLine != null) {
			titleLine.addView(view);
		}
	}

	public void addCustomRightView(View view) {
		if (rightView != null) {
			rightView.addView(view);
		}
	}

	public void addCustomLeftView(View view) {
		if (leftView != null) {
			leftView.removeAllViews();
			leftView.addView(view);
		}
	}

	public void removeCustomLeftView() {
		if (leftView != null) {
			leftView.removeAllViews();
		}
	}

	public boolean isShowing() {
		if (titleView.getVisibility() == View.VISIBLE) {
			return true;
		} else {
			return false;
		}
	}

	public void hide() {
		Animation mHiddenAction = new TranslateAnimation(
				Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF,
				0.0f, Animation.RELATIVE_TO_SELF, 0.0f,
				Animation.RELATIVE_TO_SELF, -1.0f);
		mHiddenAction.setDuration(200);
		titleView.startAnimation(mHiddenAction);
		titleView.setVisibility(View.GONE);
	}

	public void show() {
		Animation mShowAction = new TranslateAnimation(
				Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF,
				0.0f, Animation.RELATIVE_TO_SELF, -1.0f,
				Animation.RELATIVE_TO_SELF, 0.0f);
		mShowAction.setDuration(200);
		titleView.startAnimation(mShowAction);
		titleView.setVisibility(View.VISIBLE);
	}

	public void setTitleVisibility(int visibility) {
		titleView.setVisibility(visibility);
	}

	public void setTitleBg(int bgId) {
		if (bgId > 0) {
			titleView.setBackgroundColor(mActivity.getResources().getColor(bgId));
		}
	}

	private void initWidget(View parent) {
		titleView = parent.findViewById(R.id.title);
		backBtn = (TextView) parent.findViewById(R.id.back_btn);
		leftView = (PercentLinearLayout) parent.findViewById(R.id.title_left);
		rightView1 = (PercentLinearLayout) parent.findViewById(R.id.title_right1);
		rightBtn1 = (TextView) parent.findViewById(R.id.right_btn1);
		rightView = (PercentLinearLayout) parent.findViewById(R.id.title_right);
		rightBtn = (TextView) parent.findViewById(R.id.right_btn);
		titleTxt = (TextView) parent.findViewById(R.id.title_txt);
		titleLine = (LinearLayout) parent.findViewById(R.id.title_center);
	}
}
