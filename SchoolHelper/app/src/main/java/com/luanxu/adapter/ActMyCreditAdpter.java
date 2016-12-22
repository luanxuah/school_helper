package com.luanxu.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.luanxu.bean.MagnificentChartItem;
import com.luanxu.custom.MagnificentChart;
import com.luanxu.schoolhelper.R;

import java.util.ArrayList;
import java.util.List;

/**
 * @author: LuanXu
 * @createTime:2016/11/30 20:57
 * @className:  ActMyCreditAdpter
 * @Description: 学分页面的适配器
 */

public class ActMyCreditAdpter extends BaseAdapter {
	//上下文对象
	private Context context;
	private ViewHolder holder;

	//当前获得的学分
	private int creditPoints = 7;
	//总的学分
	private int totalPoints=10;
	//满足毕业要求的学分
	private int passPoints = 6;
	//头部学分栏标识
	private static final int ITEM_TOP = 0;
	//底部学分列表标识
	static final int ITEM_DATA = 1;

	//学分展示控件
	public MagnificentChart magnificentChart;
	

	public ActMyCreditAdpter(Context context) {
		this.context=context;
	}
	@Override
	public int getCount() {
		return 15;
	}

	@Override
	public Object getItem(int position) {
		return null;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}
	@Override
	public int getItemViewType(int position) {
		if (position == 0) {
			return ITEM_TOP;
		}
		return ITEM_DATA;
	}

	@Override
	public int getViewTypeCount() {
		return 2;
	}
	@Override
	public View getView(final int position, View view, ViewGroup parent) {
		int type = getItemViewType(position);
//		if (view != null) {
//			Object tag = view.getTag();
//			if (tag instanceof ViewHolder) {
//				holder = (ViewHolder) tag;
//			} else if (tag instanceof Integer) {
//				int tagType = (Integer) tag;
//				switch (tagType) {
//				case ITEM_TOP:
//					initTop(view);
//					break;
//				}
//			}
//		}else{
//			if (ITEM_TOP == type) {
//				view = LayoutInflater.from(context).inflate(R.layout.item_mycredit_top, null);
//				view.setTag(ITEM_TOP);
//				initTop(view);
//			} else if (type == ITEM_DATA){
//				holder = new ViewHolder();
//				view = LayoutInflater.from(context).inflate(R.layout.item_mycredit, null);
//				holder.time = (TextView) view.findViewById(R.id.tv_time);//时间
//				holder.content = (TextView) view.findViewById(R.id.tv_content);
//				holder.credit = (TextView) view.findViewById(R.id.tv_credit);//时间
//				view.setTag(holder);
//			}
//		}
//		if (type == ITEM_DATA && holder != null && holder instanceof ViewHolder) {
//
//		}

		if (type == ITEM_TOP){
			//顶部扇形区域
			view = LayoutInflater.from(context).inflate(R.layout.item_mycredit_top, null);
			initTop(view);
		}else if (type == ITEM_DATA){
			if (view == null){
				holder = new ViewHolder();
				view = LayoutInflater.from(context).inflate(R.layout.item_mycredit, null);
				holder.time = (TextView) view.findViewById(R.id.tv_time);//时间
				holder.content = (TextView) view.findViewById(R.id.tv_content);
				holder.credit = (TextView) view.findViewById(R.id.tv_credit);//时间
				view.setTag(holder);
			}else{
				holder = (ViewHolder) view.getTag();
			}
		}
		return view;
	}
	private void initTop(View view) {
		magnificentChart = (MagnificentChart) view.findViewById(R.id.magnificentChart);
		if (creditPoints > passPoints){
			((TextView)view.findViewById(R.id.tv_date)).setText(context.getResources().getString(R.string.str_credit_satisfy));
		}
		setShow();

	}
	private void setShow() {
		List<MagnificentChartItem> chartItemsList = new ArrayList<MagnificentChartItem>();
		if (creditPoints < passPoints){
			MagnificentChartItem firstItem = new  MagnificentChartItem("first", (float)creditPoints, Color.parseColor("#30d7fb"));
			MagnificentChartItem secondItem = new  MagnificentChartItem("first", (float)(passPoints-creditPoints), Color.parseColor("#f6f6f6"));
			MagnificentChartItem thirdItem = new  MagnificentChartItem("first", (float)(0.01), Color.parseColor("#FF0000"));
			MagnificentChartItem fourthItem = new  MagnificentChartItem("first", (float)(totalPoints-passPoints-0.01), Color.parseColor("#f6f6f6"));
			chartItemsList.add(firstItem);
			chartItemsList.add(secondItem);
			chartItemsList.add(thirdItem);
			chartItemsList.add(fourthItem);
		}else{
			MagnificentChartItem firstItem = new  MagnificentChartItem("first", (float)passPoints, Color.parseColor("#30d7fb"));
			MagnificentChartItem secondItem = new  MagnificentChartItem("first", (float)(0.01), Color.parseColor("#FF0000"));
			MagnificentChartItem thirdItem = new  MagnificentChartItem("first", (float)(creditPoints-passPoints), Color.parseColor("#30d7fb"));
			MagnificentChartItem fourthItem = new  MagnificentChartItem("first", (float)(totalPoints-creditPoints-0.01), Color.parseColor("#f6f6f6"));
			chartItemsList.add(firstItem);
			chartItemsList.add(secondItem);
			chartItemsList.add(thirdItem);
			chartItemsList.add(fourthItem);
		}

		magnificentChart.setChartItemsList(chartItemsList);
		magnificentChart.setMaxValue((float)totalPoints);
		MagnificentChart.namber=creditPoints+"";
		magnificentChart.setAnimationState(true);
		magnificentChart.setRound(false);
		magnificentChart.setShadowShowingState(false);
		magnificentChart.setAnimationSpeed(MagnificentChart.ANIMATION_SPEED_FAST);
	}
	// 实体
	public class ViewHolder {
		TextView time;//时间
		TextView content;//内容
		TextView credit;//分数
	}
}