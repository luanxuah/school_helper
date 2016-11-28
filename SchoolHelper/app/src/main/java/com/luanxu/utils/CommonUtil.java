package com.luanxu.utils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Point;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;

@SuppressLint("DefaultLocale")
public class CommonUtil {

	private static Point deviceSize = null;

	public static Point getDeviceSize(Context context) {
		if (deviceSize == null) {
			deviceSize = new Point(0, 0);
		}
		DisplayMetrics metric = new DisplayMetrics();
		((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(metric);
		deviceSize.x = metric.widthPixels;
		deviceSize.y = metric.heightPixels;
		return deviceSize;
	}

	/**
	 * 消除指定文本框的文本
	 * @param editText 指定文本框
	 * @param closeParent 关闭按钮fu'k
	 */
	public static void eliminateText(final EditText editText, LinearLayout closeParent) {
		if (editText != null && closeParent != null) {

			String tempStr = editText.getText().toString().trim();

			if (TextUtils.isEmpty(tempStr) || tempStr.length() <= 0) {
				closeParent.setVisibility(View.GONE);
				closeParent.setOnClickListener(null);
			}else {
				closeParent.setVisibility(View.VISIBLE);
				closeParent.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						editText.setText("");
					}
				});
			}
		}
	}
}
