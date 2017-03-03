package com.luanxu.utils.okhttps;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import com.luanxu.schoolhelper.R;

/**
 * @author: 范建海
 * @createTime: 2017/1/21 10:38
 * @className:  LoadingCustomDialog
 * @description: 请求服务器默认样式等待加载框(后续对该类优化)
 * @changed by:
 */
public class LoadingStyleDefaultDialog extends Dialog {


    private View aniView;
    private ImageView btn;
    private RotateAnimation mRotateAnimation;
    private TextView tvShowContent;

    public LoadingStyleDefaultDialog(Context ctx, String customMsg) {
        this(ctx, R.style.transparentFrameWindowStyle,customMsg);
    }

    private LoadingStyleDefaultDialog(Context ctx, int theme, String customMsg) {
        super(ctx, theme);
        aniView = LayoutInflater.from(ctx).inflate(R.layout.wait_dialog_layout, null);
        btn = (ImageView) aniView.findViewById(R.id.btn_animation);
        tvShowContent = (TextView) aniView.findViewById(R.id.tv_animation);
        tvShowContent.setText(customMsg);
        mRotateAnimation = new RotateAnimation(0.0F, 360.0F, 1, 0.5F, 1, 0.5F);
        mRotateAnimation.setFillAfter(false);
        mRotateAnimation.setRepeatCount(Animation.INFINITE);
        mRotateAnimation.setDuration(800);
        mRotateAnimation.setInterpolator(new LinearInterpolator());
        // 允许点返回键取消
        setCancelable(true);
        // 触碰其他地方不消失
        setCanceledOnTouchOutside(false);
        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        addContentView(aniView, params);
        show();
        btn.startAnimation(mRotateAnimation);
    }

    @Override
    public void dismiss() {
        if (btn != null) {
            btn.clearAnimation();
        }
        super.dismiss();
    }
}
