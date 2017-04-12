package com.luanxu.utils.okhttps;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.luanxu.schoolhelper.R;
import com.luanxu.utils.AndroidVersionUtil;
import com.luanxu.utils.LogUtil;

/**
 * @author: 范建海
 * @createTime: 2016/12/5 19:56
 * @className:  LoadingDialog
 * @description: 加载数据的等待框
 * @changed by:
 */
public class LoadingStyleOneDialog extends AlertDialog {

	private TextView tips_loading_msg;
	private ProgressBar progressBar;
	private int layoutResId;
	private String message;
	private Context context;
	/**
	 * 构造方法
	 * @param ctx 上下文
	 * @param layoutResId 等待框的布局
	 * @param alertMsg 显示字符串
	 */
	public LoadingStyleOneDialog(Context ctx, int layoutResId, String alertMsg) {
		super(ctx);
		this.context = ctx;
		this.layoutResId = layoutResId;
		try {
			message = alertMsg;
		} catch (Exception e) {
			LogUtil.e("LoadingDialog: " + e.toString());
		}
		show();

	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(layoutResId);
		tips_loading_msg = (TextView) findViewById(R.id.tips_loading_msg);
		if (!TextUtils.isEmpty(message)) {
			tips_loading_msg.setText(message);
		}
		progressBar = (ProgressBar) findViewById(R.id.progressBar);
		if (AndroidVersionUtil.isMarshmallow()) {//android 6.0替换clip的加载动画
			final Drawable drawable =  context.getApplicationContext().getResources().getDrawable(R.drawable.loading_two);
			progressBar.setIndeterminateDrawable(drawable);
		}
		setCancelable(true);
		setCanceledOnTouchOutside(false);
	}
}

