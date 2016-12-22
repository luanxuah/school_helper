package com.luanxu.custom.bottommenu;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
import android.widget.Toast;

import com.luanxu.bean.BottomMenuBean;
import com.luanxu.schoolhelper.R;
import com.luanxu.utils.DateUtils;
import com.luanxu.utils.ToastUtil;

import java.util.Date;
import java.util.List;

/** 底部按钮菜单 */
public class LinkageCommontBottomMenu extends BottomMenuWindow {
	private Context context;
	private TextView ok;
	private LinkageMainPicker left;
	private LinkageMainPicker right;
	private BottomMenuBean bean1;
	private BottomMenuBean bean2;
	private BottomMenuBean bean3;
	private BottomMenuBean bean4;
	private BottomMenuBean bean5;

	public LinkageCommontBottomMenu(Activity activity) {
		super(activity);
		setContentView(R.layout.commont_bottom_menu);
		context = activity;
		ok = (TextView) getContentView().findViewById(R.id.ok);
		left = (LinkageMainPicker) getContentView().findViewById(R.id.left);
		right = (LinkageMainPicker) getContentView().findViewById(R.id.right);
		left.setVisibility(View.VISIBLE);
		right.setVisibility(View.VISIBLE);

		getContentView().findViewById(R.id.cancel).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				dismiss();
			}
		});

	}

	/**
	 * 只有一个list，比如  年
	 */
	public void setData1(List<BottomMenuBean> year, String defaults){
		right.setVisibility(View.GONE);
		ok.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				dismiss();
				if (listener1 != null) {
					listener1.result1(left.getResultLeft());
				}
			}
		});
		left.setData(year,defaults);
	}

	/**
	 * 左边一个，比如  年 月
	 */
	public void setData2(List<BottomMenuBean> year, List<BottomMenuBean> month, String defaults1, String defaults2){
		right.setVisibility(View.VISIBLE);
		ok.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				dismiss();
				if (listener2 != null) {
					listener2.result2(left.getResultLeft(),left.getResultLeft());
				}
			}
		});
		left.setData(year,month,defaults1,defaults2);
	}

	/**
	 * 左右两个   两列，比如2015-2015
	 */
	public void setData3(List<BottomMenuBean> year1, List<BottomMenuBean> year2, String defaults1, String defaults2){
		right.setVisibility(View.VISIBLE);
		ok.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (left.getResultLeft().content.length() > 2 && right.getResultLeft().content.length() > 2 && (Integer.valueOf(left.getResultLeft().content) > Integer.valueOf(right.getResultLeft().content))) {
					ToastUtil.show(context, "请选择正确的时间", Toast.LENGTH_SHORT);
				}else {
					dismiss();
					if (listener3 != null) {
						listener3.result3(left.getResultLeft(),right.getResultLeft());
					}
				}
			}
		});
		left.setData(year1,defaults1);
		right.setData(year2,defaults2);
	}

	/**
	 * 4个，比如2015.01-2015.08
	 */
	public void setData4(List<BottomMenuBean> year1, List<BottomMenuBean> month1, List<BottomMenuBean> year2, List<BottomMenuBean> month2, String default1, String default2, String default3, String default4){
		right.setVisibility(View.VISIBLE);
		ok.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (listener4 != null && check()) {
					dismiss();
					listener4.result4(bean1,bean2,bean3,bean4);
				}
			}
		});
		left.setData(year1,month1,default1,default2);
		right.setData(year2,month2,default3,default4);
	}

	/**
	 * 左右两个   两列，任意类型
	 */
	public void setData5(List<BottomMenuBean> year1, List<BottomMenuBean> year2, String defaults1, String defaults2){
		right.setVisibility(View.VISIBLE);
		ok.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				dismiss();
				if (listener5 != null) {
					listener5.result5(left.getResultLeft(),right.getResultLeft());
				}
			}
		});
		left.setData(year1,defaults1);
		right.setData(year2,defaults2);
	}

	/**
	 * 结果处理
	 * */
	private boolean check(){
		bean1 = left.getResultLeft();
		bean2 = left.getResultRight();

		if ("至今".equals(right.getResultLeft().content)) {
			int year = Integer.valueOf(DateUtils.formatDate2String(new Date(),DateUtils.FORMAT_YYYY));
			int month = Integer.valueOf(DateUtils.formatDate2String(new Date(),DateUtils.FORMAT_MM));
			bean3 = new BottomMenuBean(year+"", year+"");
			bean4 = new BottomMenuBean(month + "", month + "");
		} else {
			bean3 = right.getResultLeft();
			bean4 = right.getResultRight();
		}

		if (Integer.parseInt(bean1.id) > Integer.parseInt(bean3.id) || (Integer.parseInt(bean1.id) == Integer.parseInt(bean3.id) && Integer.parseInt(bean2.id) > Integer.parseInt(bean4.id))) {
			ToastUtil.show(context, "请选择正确的日期", Toast.LENGTH_SHORT);
			return false;
		}
		return true;
	}

	private LinkageCommontBottomMenuListener1 listener1;
	private LinkageCommontBottomMenuListener2 listener2;
	private LinkageCommontBottomMenuListener3 listener3;
	private LinkageCommontBottomMenuListener4 listener4;
	private LinkageCommontBottomMenuListener5 listener5;

	public void setListener1(LinkageCommontBottomMenuListener1 listener) {
		this.listener1 = listener;
	}
	public void setListener2(LinkageCommontBottomMenuListener2 listener) {
		this.listener2 = listener;
	}
	public void setListener3(LinkageCommontBottomMenuListener3 listener) {
		this.listener3 = listener;
	}
	public void setListener4(LinkageCommontBottomMenuListener4 listener) {
		this.listener4 = listener;
	}
	public void setListener5(LinkageCommontBottomMenuListener5 listener) {
		this.listener5 = listener;
	}

	public interface LinkageCommontBottomMenuListener1 {
		public void result1(BottomMenuBean bean1);
	}
	public interface LinkageCommontBottomMenuListener2 {
		public void result2(BottomMenuBean bean1, BottomMenuBean bean2);
	}
	public interface LinkageCommontBottomMenuListener3 {
		public void result3(BottomMenuBean bean1, BottomMenuBean bean2);
	}
	public interface LinkageCommontBottomMenuListener4 {
		public void result4(BottomMenuBean bean1, BottomMenuBean bean2, BottomMenuBean bean3, BottomMenuBean bean4);
	}
	public interface LinkageCommontBottomMenuListener5 {
		public void result5(BottomMenuBean bean1, BottomMenuBean bean2);
	}
}
