package com.luanxu.custom;

import android.content.Context;
import android.support.v4.view.ViewConfigurationCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.TranslateAnimation;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

/**
 * ListView subclass that mediates drag and drop resorting of items.
 * 
 * 
 * @author heycosmo
 * 
 */
public class AnimationRemoveListView extends ListView {
	
    private SlideListViewTouchListener touchListener = null;
    
    public AnimationRemoveListView(Context context) {
        super(context);
    }

    public AnimationRemoveListView(Context context, AttributeSet attrs,
                                   int defStyle) {
        super(context, attrs, defStyle);
    }

    public AnimationRemoveListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void initSlideListViewTouchListener() {
        final ViewConfiguration configuration = ViewConfiguration.get(getContext());
        final int touchSlop = ViewConfigurationCompat.getScaledPagingTouchSlop(configuration);
        touchListener = new SlideListViewTouchListener(this, touchSlop);
        setOnTouchListener(touchListener);
    }
    
    public void setShowDelBtnRes(int res){
        if(touchListener != null){
            touchListener.setShowDelBtnRes(res);
        }
    }
    
    public void setShowDelOpBtnRes(int res){
        if(touchListener != null){
            touchListener.setShowDelOpBtnRes(res);
        }
    }

    public void setItemRes(int showRes, int hideRes){
    	if(touchListener != null){
    		touchListener.setItemRes(showRes, hideRes);
    	}
    }

    private List<Animation> animationList = new ArrayList<Animation>();
    private int maxWidth = 0;

    public void setMaxWidth(int maxWidth) {
        this.maxWidth = maxWidth;
    }

    /**
     * 删除item，并播放动画,该方法仅用于删除多个View时调�?     * 
     * @param rowView
     *            播放动画的view
     * @param position
     *            要删除的item位置
     */
    public void removeListItem(View rowView, final int position) {
        Log.d("removeListItem", "removeListItem:" + position);
        // 多动画组合有问题，调用cancel方法的时候会调用onAnimationEnd，按现有逻辑会导致死循环
        // AnimationSet animationSet = new AnimationSet(true);
        if (maxWidth == 0) {
            maxWidth = 800;
        }
        Log.d("removeListItem", "maxWidth == " + maxWidth);
        // AlphaAnimation alpha = new AlphaAnimation(1.0f, 0.0f);
        final MyAnimation translate = new MyAnimation(0, maxWidth, 0, 0);
        // final TranslateAnimation translate = new TranslateAnimation(0,
        // maxWidth, 0, 0);
        translate.setFillAfter(true);
        // animationSet.addAnimation(alpha);
        // animationSet.addAnimation(translate);
        translate.setDuration(400);
        // animationSet.setFillAfter(true);

        translate.setAnimationListener(new AnimationListener() {

            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                if (translate.isCancel) {
                    return;
                }
                if (itemRemoveCallBack != null) {
                    
                    itemRemoveCallBack.itemRemoveCallBack(position);
                } else {
                }
            }
        });
        if (rowView != null) {
            rowView.startAnimation(translate);
            animationList.add(translate);
        }
    }

    /**
     * @Title: allAnimationCancel
     * @Description: 取消全部动画，删除多个View完成后调用该方法，否则会导致部分view不能显示
     *               原因是view重用，动画设置了view停留在动画结束时位置(屏幕�?
     * @return: void
     */
    public void allAnimationCancel() {
        if (animationList != null) {
            int size = animationList.size();
            for (int count = 0; count < size; count++) {
                Log.d("allAnimationCancel", "cancel: " + count);
                animationList.get(count).cancel();
            }
            animationList.clear();
        }
    }

    private ItemRemoveCallBack itemRemoveCallBack;

    public void setItemRemoveCallBack(ItemRemoveCallBack callBack) {
        this.itemRemoveCallBack = callBack;
    }
    
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
    	getParent().requestDisallowInterceptTouchEvent(true);  
        return super.dispatchTouchEvent(ev);    
    }
    
    public interface ItemRemoveCallBack {
        public void itemRemoveCallBack(int position);
    }

    private class MyAnimation extends TranslateAnimation {

        private boolean isCancel = false;

        public MyAnimation(float fromXDelta, float toXDelta, float fromYDelta,
                float toYDelta) {
            super(fromXDelta, toXDelta, fromYDelta, toYDelta);
        }

        @Override
        public void cancel() {
            isCancel = true;
            super.cancel();
        }
    }
}
