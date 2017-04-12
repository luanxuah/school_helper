package com.luanxu.utils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Point;
import android.net.Uri;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.luanxu.bean.BottomMenuBean;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@SuppressLint("DefaultLocale")
public class CommonUtils {

	private static Point deviceSize = null;
	private static long lastClickTime = -1;
	/**
	 * 将2进制转化为16进制
	 *
	 * @param buf 2进制数组
	 * @return String
	 */
	public static String parseByte2HexStr(byte buf[]) {
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < buf.length; i++) {
			String hex = Integer.toHexString(buf[i] & 0xFF);
			if (hex.length() == 1) {
				hex = '0' + hex;
			}
			sb.append(hex.toUpperCase());
		}
		return sb.toString();
	}

	/**
	 * 将16进制转化为2进制
	 *
	 * @param hexStr 字符串
	 * @return 二进制
	 */
	public static byte[] parseHexStr2Byte(String hexStr) {
		if (hexStr.length() < 1)
			return null;
		byte[] result = new byte[hexStr.length() / 2];
		for (int i = 0; i < hexStr.length() / 2; i++) {
			int high = Integer.parseInt(hexStr.substring(i * 2, i * 2 + 1), 16);
			int low = Integer.parseInt(hexStr.substring(i * 2 + 1, i * 2 + 2), 16);
			result[i] = (byte) (high * 16 + low);
		}
		return result;
	}

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

	public static void scanFileAsync(Context ctx, String filePath) {
		LogUtil.d("CommonUtil scanFileAsync,Intent.ACTION_MEDIA_SCANNER_SCAN_FILE发送广播:" + filePath);
		Intent scanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
		scanIntent.setData(Uri.fromFile(new File(filePath)));
		ctx.sendBroadcast(scanIntent);
	}

	public static boolean isFastDoubleClick() {
		long time = System.currentTimeMillis();
		long timeD = time - lastClickTime;
		if (0 < timeD && timeD < 200) {
			return true;
		}
		lastClickTime = time;
		return false;
	}

	/**
	 * 判断外置存储是否可用
	 * @param context
	 * @param showTip
	 * @return
	 */
	public static boolean checkExternalStorage(Context context, boolean showTip) {
		if (ExternalStorageUtil.isExternalStorageWritable()) {

			if (ExternalStorageUtil.getSDcardAvailableSize() > 20) {
				return true;
			} else {
				// 小于20M可用空间，提示用户清理
				if (showTip) {
					ToastUtil.show(context,"存储空间不足，请及时清理",0);
				}
			}
		} else {
			if (showTip) {
				ToastUtil.show(context,"SD卡暂不可用，请检查SD卡",0);
			}
		}
		return false;
	}
}
