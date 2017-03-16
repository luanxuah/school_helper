package com.luanxu.custom;

import android.view.MotionEvent;
import android.widget.GridView;

// 不滚动的gridView
public class MyGridView extends GridView {

	public MyGridView(android.content.Context context, android.util.AttributeSet attrs) {
		super(context, attrs);
	}

	/** 设置不滚动 */
	public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2, MeasureSpec.AT_MOST);
		super.onMeasure(widthMeasureSpec, expandSpec);
	}

	@Override
	public boolean dispatchTouchEvent(MotionEvent ev) {
		if (ev.getAction() == MotionEvent.ACTION_MOVE) {
			return true;// 禁止Gridview进行滑动
		}
		return super.dispatchTouchEvent(ev);
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		return false;
	}
}