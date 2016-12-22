package com.luanxu.utils;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.luanxu.schoolhelper.R;

/**
 * @author: 范建海
 * @createTime: 2016/10/31 16:52
 * @className:  ToastUtil
 * @description: 显示通知的工具类
 * @changed by:
 */
public class ToastUtil {
    private static Handler handler = new Handler(Looper.getMainLooper());
    private static Toast toast = null;
    private static Object synObj = new Object();

    private static Toast makeText(Context act, String msg, int len, int image) {
        Toast result = new Toast(act);
        View tipsView = LayoutInflater.from(act).inflate(
                R.layout.toast_default, null);
        TextView tips = (TextView) tipsView.findViewById(R.id.toast_content);
        ImageView iv = (ImageView) tipsView.findViewById(R.id.iv);
        if (image != -1){
            iv.setImageResource(image);
            iv.setVisibility(View.VISIBLE);
            result.setGravity(Gravity.CENTER, 0, 0);
        }else{
            iv.setVisibility(View.GONE);
            int distance = CommonUtil.getDeviceSize(act).y * 30/ 100;
            result.setGravity(Gravity.NO_GRAVITY, 0, distance);
        }
        tips.setText(msg);
        result.setDuration(len);
        result.setView(tipsView);

        return result;
    }


    private static void reMakeText(Context context, String msg, int len, int image) {
        View tipsView = LayoutInflater.from(context).inflate(
                R.layout.toast_default, null);
        TextView tips = (TextView) tipsView.findViewById(R.id.toast_content);
        ImageView iv = (ImageView) tipsView.findViewById(R.id.iv);
        if (image != -1){
            iv.setImageResource(image);
            iv.setVisibility(View.VISIBLE);
            toast.setGravity(Gravity.CENTER, 0, 0);
        }else{
            iv.setVisibility(View.GONE);
            int distance = CommonUtil.getDeviceSize(context).y * 30/ 100;
            toast.setGravity(Gravity.NO_GRAVITY, 0, distance);
        }
        tips.setText(msg);
        toast.setDuration(len);
        toast.setView(tipsView);
    }

    /**
     * 显示通知
     * @param context 上下文
     * @param messageId 字符串对应的资源id
     * @param length 显示时间
     */
    public static void show(final Context context, final int messageId,
                            final int length) {
        show(context, context.getResources().getString(messageId), length);
    }

    /**
     * 显示通知
     * @param context 上下文
     * @param message 显示的字符串
     * @param length 显示时间
     */
    public static void show(final Context context, final String message,
                            final int length) {
        int strSDKVersion = android.os.Build.VERSION.SDK_INT;
        if (strSDKVersion >= 14) {
            if (toast != null) {
                reMakeText(context, message, length, -1);
            } else {
                toast = makeText(context, message, length, -1);
            }
            toast.show();
        } else {
            new Thread(new Runnable() {
                public void run() {
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            synchronized (synObj) {
                                if (toast != null) {
                                    toast.cancel();
                                    reMakeText(context, message, length, -1);
                                } else {
                                    toast = makeText(context, message, length, -1);
                                }
                                toast.show();
                            }
                        }
                    });
                }
            }).start();
        }
    }

    /**
     * 显示通知，带图标
     * @param context 上下文对象
     * @param message 要显示的消息
     * @param length 提示显示的时长
     * @param image 提示上的图标
     */
    public static void show(final Context context, final String message,
                            final int length, final int image){
        int strSDKVersion = android.os.Build.VERSION.SDK_INT;
        if (strSDKVersion >= 14) {
            if (toast != null) {
                reMakeText(context, message, length, image);
            } else {
                toast = makeText(context, message, length, image);
            }
            toast.show();
        } else {
            new Thread(new Runnable() {
                public void run() {
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            synchronized (synObj) {
                                if (toast != null) {
                                    toast.cancel();
                                    reMakeText(context, message, length, image);
                                } else {
                                    toast = makeText(context, message, length, image);
                                }
                                toast.show();
                            }
                        }
                    });
                }
            }).start();
        }
    }
}
