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

import com.luanxu.bean.BottomMenuBean;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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

	/**
	 * 获取年的集合
	 */
	public static List<BottomMenuBean> getYearList(){
		int year = Integer.valueOf(DateUtils.formatDate2String(new Date(),DateUtils.FORMAT_YYYY));
		List<BottomMenuBean> list = new ArrayList<BottomMenuBean>();
		for (int i = year+5; i > 1900; i--) {
			BottomMenuBean object = new BottomMenuBean();
			object.id="" + i;
			object.content = i+ "";
			list.add(object);
		}
		return list;
	}

	/**
	 * 获取周的集合
	 * @return
     */
	public static List<BottomMenuBean> getWeekList(){
		List<BottomMenuBean> weekList = new ArrayList<BottomMenuBean>();
		for (int i=1; i<15; i++){
			BottomMenuBean bean = new BottomMenuBean(i+"", "第"+i+"周");
			weekList.add(bean);
		}
		return weekList;
	}
}
