package com.luanxu.custom.bottommenu;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import com.luanxu.bean.BottomMenuBean;
import com.luanxu.schoolhelper.R;

import java.util.List;

public class LinkageMainPicker extends LinearLayout {

	/** 滑动控件 */
	private ScrollerNumberPicker leftPicker;
	private ScrollerNumberPicker rightPicker;
	/** 选择监听 */
	private OnSelectingListener onSelectingListener;
	/** 刷新界面 */
	private static final int REFRESH_VIEW = 0x001;
	/** 临时日期 */
	private int tempProvinceIndex = -1;
	private BottomMenuUtil util;
    private List<BottomMenuBean> list1;
    private List<BottomMenuBean> list2;
    
    private boolean isOne = true;

	public LinkageMainPicker(Context context) {
		this(context,null);
	}

	public LinkageMainPicker(Context context, AttributeSet attrs) {
		this(context, attrs,0);
	}

	@SuppressLint("NewApi")
	public LinkageMainPicker(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}
	

	public void setData(List<BottomMenuBean> list1, List<BottomMenuBean> list2, String defaults1, String defaults2) {
		this.list1 = list1;
		this.list2 = list2;
		this.isOne = false;
		this.defaults1 = defaults1;
		this.defaults2 = defaults2;
		refresh();
	}
	public void setData(List<BottomMenuBean> list1, String defaults) {
		this.list1 = list1;
		this.isOne = true;
		this.defaults1 = defaults;
		refresh();
	}
	
	public BottomMenuBean getResultLeft() {
		return new BottomMenuBean(getLeftId(), getLeftContent());
	}
	public BottomMenuBean getResultRight() {
		return new BottomMenuBean(getRightId(), getRightContent());
	}
	
	Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case REFRESH_VIEW:
				if (onSelectingListener != null) {
					onSelectingListener.selected(true);
				}
				break;
			}
		}
	};
	
	String defaults1,defaults2;
	private void refresh(){
		View view = LayoutInflater.from(getContext()).inflate(R.layout.linkage_city_picker, this);
		util = BottomMenuUtil.getSingleton();
		leftPicker = (ScrollerNumberPicker) view.findViewById(R.id.left1);
		rightPicker = (ScrollerNumberPicker) view.findViewById(R.id.right1);
		leftPicker.setVisibility(View.GONE);
		rightPicker.setVisibility(View.GONE);
		if(list1!=null){
			leftPicker.setData(util.getBottomMenuBean(list1));
		}
		
		if (isOne) {
			leftPicker.setVisibility(View.VISIBLE);
			rightPicker.setVisibility(View.GONE);
		}else {
			leftPicker.setVisibility(View.VISIBLE);
			rightPicker.setVisibility(View.VISIBLE);
			
			if(list2!=null){
				rightPicker.setData(util.getBottomMenuBean(list2));
			}
			
			if (TextUtils.isEmpty(defaults2)) {//如果没有默认值，定位到0位置
				rightPicker.setDefault(0);
			}else {
				for (int i = 0; i < list2.size(); i++) {
					if (list2.get(i).content.equals(defaults2)) {
						rightPicker.setDefault(i);
					}
				}
			}
			if ("至今".equals(getLeftContent())) {
				rightPicker.setDefault(0);
			}
			
			rightPicker.setOnSelectListener(new ScrollerNumberPicker.OnSelectListener() {
				
				@Override
				public void endSelect(int id, String text) {
					if (tempProvinceIndex != id) {
						int lastDay = Integer.valueOf(rightPicker.getListSize());
						if (id > lastDay) {
							rightPicker.setDefault(lastDay - 1);
						}
					}
					if ("至今".equals(getLeftContent())) {//左边的如果是至今的，右边只能是至今
						rightPicker.setDefault(0);
					}
					
					if (!"至今".equals(getLeftContent()) && "至今".equals(getRightId())) {//左边的不是至今的，右边也不是
						rightPicker.setDefault(1);
					}
					tempProvinceIndex = id;
					Message message = new Message();
					message.what = REFRESH_VIEW;
					handler.sendMessage(message);
				}
				
				@Override
				public void selecting(int id, String text) {
				}
			});
		}
		 
		if (TextUtils.isEmpty(defaults1)) {//如果没有默认值，定位到0位置
			leftPicker.setDefault(0);
		}else {
			for (int i = 0; i < list1.size(); i++) {
				if (list1.get(i).content.equals(defaults1)) {
					leftPicker.setDefault(i);
				}
			}
		}
		
		leftPicker.setOnSelectListener(new ScrollerNumberPicker.OnSelectListener() {

			@Override
			public void endSelect(int id, String text) {
				if (text.equals("") || text == null)
					return;
				if (tempProvinceIndex != id) {
					int lastDay = Integer.valueOf(leftPicker.getListSize());
					if (id > lastDay) {
						leftPicker.setDefault(lastDay - 1);
					}
				}
				if (!isOne && "至今".equals(getLeftContent())) {//左边是至今，右边也是
					rightPicker.setDefault(0);
				}
				if (!isOne && "至今".equals(getRightId()) && !"至今".equals(getLeftContent())) {//左边不是至今，右边也不是
					rightPicker.setDefault(1);
				}
				tempProvinceIndex = id;
				Message message = new Message();
				message.what = REFRESH_VIEW;
				handler.sendMessage(message);
			}

			@Override
			public void selecting(int id, String text) {
			}
		});
	}

	public void setOnSelectingListener(OnSelectingListener onSelectingListener) {
		this.onSelectingListener = onSelectingListener;
	}
 
	public String getLeftId() {
		return list1.get(leftPicker.getSelected()).id;
	}
	
	public String getLeftContent() {
		return leftPicker.getSelectedText();
	}
	
	public String getRightId() {
		return list2.get(rightPicker.getSelected()).id;
	}

	public String getRightContent() {
		return rightPicker.getSelectedText();
	}

	public interface OnSelectingListener {
		public void selected(boolean selected);
	}
}
