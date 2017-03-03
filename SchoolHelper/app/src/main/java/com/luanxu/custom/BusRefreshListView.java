
package com.luanxu.custom;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.luanxu.schoolhelper.R;
import com.luanxu.utils.DateUtils;
import com.luanxu.utils.LogUtil;

import java.util.Date;

/**
 * @author: BaiCQ
 * @createTime: 2017/1/20 13:59
 * @className:  BusRefreshListView
 * @Description:  处理参考嵌套滚动封装的特殊业务的listview
 */
public class BusRefreshListView extends ListView implements OnScrollListener {
    private static final String TAG = "BusRefreshListView";
    public final static int RELEASE_To_REFRESH = 0;//松开立即刷新
    public final static int PULL_To_REFRESH = 1;//下拉刷新
    public final static int REFRESHING = 2;//正在刷新
    public final static int DONE = 3;//默认
    public final static int LOADING = 4;//加载更多的状态
    public final static int SHOW_BUS = 5;//显示业务view

    private final static int RATIO = 2;

    private LayoutInflater inflater;

    private LinearLayout headView;

    private TextView tipsTextview;
    private TextView lastUpdatedTextView;
    private ImageView arrowImageView;
    private ProgressBar progressBar;
    private RelativeLayout contentView;
    private View busView;

    private ImageView view;

    private RotateAnimation animation;
    private RotateAnimation reverseAnimation;

    // 用于保证startY的值在一个完整的touch事件中只被记录一次
    private boolean isRecored;

    private int headContentWidth;
    //头布局的高度
    private int headContentHeight;
    //刷新动画的高度
    private int contentViewH;
    //业务控件的高度
    private int busViewH = 0;

    private int startY;
    //起始状态 默认
    private int startState = 3;
    //current 状态 默认
    public int state = 3;

    private boolean isBack;

    private PullToRefreshListView.OnRefreshListener refreshListener;
    //是否可以刷新
    private boolean isRefreshable;

    public BusRefreshListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public void setView(ImageView view) {
        this.view = view;
    }

    private void init(Context context) {
        setCacheColorHint(context.getResources().getColor(android.R.color.transparent));
        inflater = LayoutInflater.from(context);
        // 给listview 添加上啦加载更多的功能
        footer = inflater.inflate(R.layout.listview_footer, null);
        loadFull = (TextView) footer.findViewById(R.id.loadFull);
        noData = (TextView) footer.findViewById(R.id.noData);
        more = (TextView) footer.findViewById(R.id.more);
        loading = (ProgressBar) footer.findViewById(R.id.loading);
        // 默认隐藏上啦刷新
        footer.setVisibility(View.GONE);
        //头布局
        headView = (LinearLayout) inflater.inflate(R.layout.bus_refresh_header, null);
        arrowImageView = (ImageView) headView.findViewById(R.id.head_arrowImageView);
        contentView= (RelativeLayout) headView.findViewById(R.id.head_contentLayout);
        //业务View
        busView = (LinearLayout) headView.findViewById(R.id.ll_magazine);
        arrowImageView.setMinimumWidth(70);
        arrowImageView.setMinimumHeight(50);
        progressBar = (ProgressBar) headView.findViewById(R.id.head_progressBar);
        tipsTextview = (TextView) headView.findViewById(R.id.head_tipsTextView);
        lastUpdatedTextView = (TextView) headView.findViewById(R.id.head_lastUpdatedTextView);

        measureView(headView);
        headContentHeight = headView.getMeasuredHeight();
        contentViewH = headContentHeight;
        headContentWidth = headView.getMeasuredWidth();

        // 隐藏 headview 的高度
        headView.setPadding(0, -1 * headContentHeight, 0, 0);
        LogUtil.d(TAG, "init headContentHeight = " + headContentHeight);
        headView.invalidate();
        addHeaderView(headView, null, false);
        addFooterView(footer);
        setOnScrollListener(this);
        animation = new RotateAnimation(0, -180,
                RotateAnimation.RELATIVE_TO_SELF, 0.5f,
                RotateAnimation.RELATIVE_TO_SELF, 0.5f);
        animation.setInterpolator(new LinearInterpolator());
        animation.setDuration(300);
        animation.setFillAfter(true);

        reverseAnimation = new RotateAnimation(-180, 0,
                RotateAnimation.RELATIVE_TO_SELF, 0.5f,
                RotateAnimation.RELATIVE_TO_SELF, 0.5f);
        reverseAnimation.setInterpolator(new LinearInterpolator());
        reverseAnimation.setDuration(300);
        reverseAnimation.setFillAfter(true);

        state = DONE;
        isRefreshable = false;
    }

    public void onScroll(AbsListView arg0, int firstVisiableItem, int arg2, int arg3) {
        this.firstItemIndex = firstVisiableItem;
    }

    public boolean onTouchEvent(MotionEvent event) {
        firstItemIndex = getFirstVisiblePosition();
        if (isRefreshable) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    if (firstItemIndex == 0 && !isRecored) {
                        isRecored = true;
                        startY = (int) event.getY();
                        startState = state;
                    }
                    break;
                case MotionEvent.ACTION_UP:
                    LogUtil.d(TAG, "ACTION_UP startState = " + startState);
                    LogUtil.d(TAG, "ACTION_UP state = " + state);
                    if (state != REFRESHING && state != LOADING) {
                        int tempY = (int) event.getY();
                        int dy = tempY - startY;
                        //ACTION_UP 判断 SHOW_BUS 和 DONE 的状态
                        LogUtil.d(TAG, "ACTION_UP dy = " + dy);
                        LogUtil.d(TAG, "ACTION_UP busViewH *0.6 = " + busViewH *0.6);
                        if (startState == DONE) {//起始是默认状态
                            if (state == DONE){
                                if (dy > 0 && dy / RATIO >= busViewH * 0.6) {//下
                                    state = SHOW_BUS;
                                }
                            }else  if (state == SHOW_BUS){
                                if (dy > 0 && dy / RATIO <= busViewH * 0.6) {//下
                                    state = DONE;
                                }
                            }
                        } else if (startState == SHOW_BUS) {//起始是SHOW_BUS
                            if (state == DONE) {//默认
                                if (dy < 0 &&  - dy / RATIO >= busViewH *0.6) {//上
                                    state = SHOW_BUS;
                                }
                            } else if (state == SHOW_BUS) {//显示busView
                                if (dy < 0 && -dy / RATIO >= busViewH * 0.6) {//上
                                     state = DONE;
                                }
                            }
                        }
                        if (state == PULL_To_REFRESH) {
                            state = SHOW_BUS;
                        }

                        //从此处赋值startState 只能有两个值： DONE 和 SHOW_BUS
                        if (state == DONE || state == SHOW_BUS ) {
                            startState = state;
                        }

                        if (state == RELEASE_To_REFRESH) {
                            state = REFRESHING;
                            startState = SHOW_BUS;
                        }
                        LogUtil.d(TAG, "ACTION_UP state = " + state);
                        changeHeaderViewByState();
                        LogUtil.d(TAG, "ACTION_UP startState = " + startState);
                        if (state == REFRESHING) {
                            onRefresh();
                        }
                    }
                    isRecored = false;
                    isBack = false;
                    break;
                case MotionEvent.ACTION_MOVE:
                    int tempY = (int) event.getY();
                    if (!isRecored && firstItemIndex == 0) {
                        isRecored = true;
                        startY = tempY;
                    }
                    int dy = tempY - startY;
                    if (state != REFRESHING && isRecored && state != LOADING) {
                        Log.d(TAG, "old state = " + state);
                        Log.d(TAG, "ACTION_MOVE dy = " + dy);
                        setSelection(0);
                        if (startState == DONE) {//起始是默认状态
                            Log.d(TAG, "ACTION_MOVE 起始是默认状态");
                            if (dy > 0) {//下
                               /* if (dy / RATIO >= busViewH - 5 && dy / RATIO <= busViewH + 5) {
                                    state = SHOW_BUS;
                                } else*/ if (dy / RATIO > busViewH + 5 && dy / RATIO < headContentHeight -5) {
                                    if (state != PULL_To_REFRESH){
                                        state = PULL_To_REFRESH;
                                        LogUtil.d(TAG, "ACTION_MOVE new state： PULL_To_REFRESH");
                                        changeHeaderViewByState();
                                    }
                                } else if (dy / RATIO >= headContentHeight + 5) {
                                    if (state != RELEASE_To_REFRESH) {
                                        state = RELEASE_To_REFRESH;
                                        isBack = true;
                                        LogUtil.d(TAG, "ACTION_MOVE new state： RELEASE_To_REFRESH");
                                        changeHeaderViewByState();
                                    }
                                }
                            }
                        } else if (startState == SHOW_BUS) {//起始是SHOW_BUS
                            Log.d(TAG, "ACTION_MOVE 起始是起始是SHOW_BUS");
                          /*  if (dy < -5) {//上
                                if (-dy / RATIO >= busViewH) {
                                    state = DONE;
                                }
                            } else */ if (dy > 5){//下
                                if (dy / RATIO <= contentViewH - 5) {
                                    if (state != PULL_To_REFRESH) {
                                        state = PULL_To_REFRESH;
                                        LogUtil.d(TAG, "ACTION_MOVE new state： PULL_To_REFRESH");
                                        changeHeaderViewByState();
                                    }
                                } else if (dy / RATIO >= contentViewH + 5) {
                                    if (state != RELEASE_To_REFRESH) {
                                        state = RELEASE_To_REFRESH;
                                        isBack = true;
                                        LogUtil.d(TAG, "ACTION_MOVE new state： RELEASE_To_REFRESH");
                                        changeHeaderViewByState();
                                    }
                                }
                            }
                        }
                        int padding = -1 * headContentHeight;
                        if (startState == DONE){
                            padding = -1 * headContentHeight + dy / RATIO;
                        }else if (startState == SHOW_BUS){
                            padding =  -1 * contentViewH + dy / RATIO;
                        }
                        headView.setPadding(0, padding, 0, 0);
                    }
                    break;
            }
        }
        return super.onTouchEvent(event);
    }

    //通知父层ViewGroup，你不能截获
    public boolean dispatchTouchEvent(MotionEvent ev) {
        getParent().requestDisallowInterceptTouchEvent(false);
        return super.dispatchTouchEvent(ev);
    }


    // 当状态改变时候，调用该方法，以更新界面
    public void changeHeaderViewByState() {
        LogUtil.d(TAG, "changeHeaderViewByState isBack = "+isBack);
        switch (state) {
            case RELEASE_To_REFRESH:
                arrowImageView.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.GONE);
                tipsTextview.setVisibility(View.VISIBLE);
                lastUpdatedTextView.setVisibility(View.VISIBLE);
                arrowImageView.clearAnimation();
                arrowImageView.startAnimation(animation);
                tipsTextview.setText("松开立即刷新");
                break;
            case PULL_To_REFRESH:
                progressBar.setVisibility(View.GONE);
                tipsTextview.setVisibility(View.VISIBLE);
                lastUpdatedTextView.setVisibility(View.VISIBLE);
                arrowImageView.clearAnimation();
                arrowImageView.setVisibility(View.VISIBLE);
                // 是由RELEASE_To_REFRESH状态转变来的
                if (isBack) {
                    isBack = false;
                    arrowImageView.clearAnimation();
                    arrowImageView.startAnimation(reverseAnimation);
                    tipsTextview.setText("下拉刷新");
                } else {
                    tipsTextview.setText("下拉刷新");
                }
                Log.v(TAG, "当前状态，下拉刷新");
                break;

            case REFRESHING:
                headView.setPadding(0, 0, 0, 0);
                progressBar.setVisibility(View.VISIBLE);
                arrowImageView.clearAnimation();
                arrowImageView.setVisibility(View.GONE);
                tipsTextview.setText("正在刷新...");
                lastUpdatedTextView.setVisibility(View.VISIBLE);
                break;
            case DONE:
                headView.setPadding(0, -1 * headContentHeight, 0, 0);
                progressBar.setVisibility(View.GONE);
                arrowImageView.clearAnimation();
                arrowImageView.setImageResource(R.mipmap.pull_arrow_icon);
                tipsTextview.setText("下拉刷新");
                lastUpdatedTextView.setVisibility(View.VISIBLE);
                break;
            case SHOW_BUS:
                headView.setPadding(0, -1 * contentViewH, 0, 0);
                progressBar.setVisibility(View.GONE);
                arrowImageView.clearAnimation();
                arrowImageView.setImageResource(R.mipmap.pull_arrow_icon);
                tipsTextview.setText("下拉刷新");
                lastUpdatedTextView.setVisibility(View.VISIBLE);
                break;
        }
    }

    public void setOnRefreshListener(PullToRefreshListView.OnRefreshListener refreshListener) {
        this.refreshListener = refreshListener;
        isRefreshable = true;

    }

    public void onRefreshComplete() {
        // 删除底部的footer 重新加载
        hideFooter();
//        state = DONE;
        state = SHOW_BUS;
        lastUpdatedTextView.setText("最近更新:" + DateUtils.formatDate2String(new Date(),DateUtils.FORMAT_HH_MM_SS));
        changeHeaderViewByState();
    }


    private void hideFooter() {
        footer.setVisibility(View.GONE);

    }

    private void showFooter() {
        footer.setVisibility(View.VISIBLE);
        loadFull.setVisibility(View.GONE);
        loading.setVisibility(View.VISIBLE);
        more.setVisibility(View.VISIBLE);
        noData.setVisibility(View.GONE);
    }

    private void onRefresh() {
        if (refreshListener != null) {
            refreshListener.onRefresh();
            if (removeFirst) {
                addFooterView(footer);
                removeFirst = false;
            }
        }
    }

    private void measureView(View child) {
        ViewGroup.LayoutParams p = child.getLayoutParams();
        if (p == null) {
            p = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
        }
        int childWidthSpec = ViewGroup.getChildMeasureSpec(0, 0 + 0, p.width);
        int lpHeight = p.height;
        int childHeightSpec;
        if (lpHeight > 0) {
            childHeightSpec = MeasureSpec.makeMeasureSpec(lpHeight,
                    MeasureSpec.EXACTLY);
        } else {
            childHeightSpec = MeasureSpec.makeMeasureSpec(0,
                    MeasureSpec.UNSPECIFIED);
        }
        child.measure(childWidthSpec, childHeightSpec);
    }

    public void setAdapter(BaseAdapter adapter) {
        lastUpdatedTextView.setText("最近更新:"+ DateUtils.formatDate2String(new Date(),DateUtils.FORMAT_HH_MM_SS));
        super.setAdapter(adapter);
    }

    // 区分当前操作是刷新还是加载
    public static final int REFRESH = 0;
    public static final int LOAD = 1;
    // 只有在listview第一个item显示的时候（listview滑到了顶部）才进行下拉刷新， 否则此时的下拉只是滑动listview
    private boolean isLoading;// 判断是否正在加载
    private boolean loadEnable = false;// 开启或者关闭加载更多功能，默认是开启上啦加载更多
    private boolean isLoadFull;// 加载全部
    private int pageSize = 10;
    private PullToRefreshListView.OnLoadListener onLoadListener;
    private View footer;

    private int firstItemIndex;
    private int scrollStates;

    private TextView noData;
    private TextView loadFull;
    private TextView more;
    private ProgressBar loading;

    @Override
    public void onScrollStateChanged(AbsListView view1, int scrollState) {

        this.scrollStates = scrollState;
        LogUtil.d(TAG, "滚动状态" + scrollStates);
            if (view != null) {
            switch (scrollState) {
                case OnScrollListener.SCROLL_STATE_IDLE:// 是当屏幕停止滚动时
                    view.setVisibility(View.VISIBLE);
                    break;
                case OnScrollListener.SCROLL_STATE_TOUCH_SCROLL:// 滚动时
                    view.setVisibility(View.GONE);
                    break;
                case OnScrollListener.SCROLL_STATE_FLING:// 是当用户由于之前划动屏幕并抬起手指，屏幕产生惯性滑动时
                    view.setVisibility(View.GONE);
                    if (getFirstVisiblePosition() > 0){
                        state = DONE;
                    }
                    break;
            }
        }
        ifNeedLoad(view1, scrollStates);
    }

    // 加载更多监听
    public void setOnLoadListener(PullToRefreshListView.OnLoadListener onLoadListener) {
        this.loadEnable = true;
        this.isLoading = false;
        this.isLoadFull = false;
        this.onLoadListener = onLoadListener;
    }

    public boolean isLoadEnable() {
        return loadEnable;
    }

    // 这里的开启或者关闭加载更多，并不支持动态调整
    public void setLoadEnable(boolean loadEnable) {
        this.loadEnable = loadEnable;
        this.removeFooterView(footer);
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public void onLoad() {
        if (onLoadListener != null) {
            loading.startAnimation(reverseAnimation);
            onLoadListener.onLoad();
//            currentPage++;
        }
    }

    private boolean removeFirst = false;

    public void setLoadFull(boolean isLoadFull) {
        if (loadEnable) {

            this.isLoadFull = isLoadFull;
            if (isLoadFull) {
                removeFooterView(footer);
                removeFirst = true;
            }else {
                if (removeFirst){
                    if (null != footer){
                        this.addFooterView(footer);
                    }
                }
            }
        }
    }

    // 用于加载更多结束后的回调
    public void onLoadComplete() {
        loading.clearAnimation();
        loadEnable = true;
        isLoading = false;
        // 加载更多完成的时候就隐藏 footer
        footer.setVisibility(View.GONE);
    }

    // 根据listview滑动的状态判断是否需要加载更多
    public void ifNeedLoad(AbsListView view, int scrollState) {

        if (!loadEnable) {
            // 控制是否需要 上拉加载更多的显示
            return;
        }
        try {

            if (scrollState == OnScrollListener.SCROLL_STATE_IDLE
                    && !isLoading
                    && view.getLastVisiblePosition() == view.getPositionForView(footer)
                    && getFirstVisiblePosition() > 0) {
//                    && getFirstVisiblePosition() != 0) {
                if (isLoadFull) {
                    return;
                }
                showFooter();
                onLoad();
                isLoading = true;
            }
        } catch (Exception e) {}
    }

    public View getBusView(){
        return busView;
    }

    public void setBusViewH(int busViewH){
        this.busViewH = busViewH;
        if (headContentHeight == contentViewH){//避免重复叠加
            headContentHeight = headContentHeight + busViewH;
        }
    }

    public void resetBusView(int busViewH){
        this.busViewH = busViewH;
        if (headContentHeight == contentViewH){//避免重复叠加
            headContentHeight = headContentHeight + busViewH;
        }
        headView.setPadding(0, -1 * headContentHeight, 0, 0);
        busView.setVisibility(VISIBLE);
        changeHeaderViewByState();
    }

    public void setState(int state) {
        this.state = state;
        changeHeaderViewByState();
    }
}
