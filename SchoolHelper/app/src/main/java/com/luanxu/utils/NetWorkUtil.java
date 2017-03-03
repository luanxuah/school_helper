package com.luanxu.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * @author: 范建海
 * @createTime: 2017/1/18 16:14
 * @className:  NetWorkUtil
 * @description: 网络工具类
 * @changed by:
 */
public class NetWorkUtil {

    /**
     * 是否连接上网络
     * @param ctx 上下文
     * @return 连接上true，未连接上false
     * @date: 2017-1-16 下午17:52
     */
    public static boolean isNetworkConnected(Context ctx) {
        // 网络连接的状态
        boolean isConnected = false;
        ConnectivityManager connectivityManager = (ConnectivityManager) ctx.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager!=null){
            // 建立网络数组
            NetworkInfo[] net_info = connectivityManager.getAllNetworkInfo();
            if (net_info != null) {
                for (int i = 0; i < net_info.length; i++) {
                    // 判断获得的网络状态是否是处于连接状态
                    if (net_info[i].getState() == NetworkInfo.State.CONNECTED) {
                        isConnected=true;
                    }
                }
            }
        }
        return isConnected;

    }

    /**
     * 判断当前网络是否为wifi状态
     * @param ctx 上下文
     * @return wifi状态true，非wifi状态false
     * @date: 2017-1-16 下午17:52
     */
    public static boolean isWifi(Context ctx) {
        // 判断是否为wifi的状态
        boolean isWifi = false;
        if(isNetworkConnected(ctx)) {
            ConnectivityManager connectivityManager = (ConnectivityManager) ctx.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo activeNetInfo = connectivityManager.getActiveNetworkInfo();

            if (activeNetInfo != null && activeNetInfo.getType() == ConnectivityManager.TYPE_WIFI) {

                isWifi=true;
            }
        }
        return isWifi;
    }

}
