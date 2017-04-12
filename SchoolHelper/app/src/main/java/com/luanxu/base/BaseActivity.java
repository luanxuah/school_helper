package com.luanxu.base;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.ViewGroup;
import android.view.WindowManager;

import com.luanxu.custom.CommonDialog;
import com.luanxu.custom.TitleBar;
import com.luanxu.schoolhelper.R;

public class BaseActivity extends FragmentActivity{
	private Activity context;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN); // 显示标题栏
		overridePendingTransition(R.anim.activity_ani_enter, 0);
		context=this;

	}

	private TitleBar titleBar = null;

	public TitleBar getTitleBar() {
		if (titleBar == null) {
			titleBar = new TitleBar(this, ((ViewGroup) findViewById(android.R.id.content)).getChildAt(0));
		}
		return titleBar;
	}

	public void onSaveInstance(Bundle outState) {
		super.onSaveInstanceState(outState);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		if (dialog != null && dialog.isShowing()) {
			dialog.dismiss();
			dialog = null;
		}
	}



	/**
	 * 退出登录
	 */

	private CommonDialog dialog;

	@Override
	protected void onResume() {
		super.onResume();
	}

}
