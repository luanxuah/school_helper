package com.luanxu.utils;


import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author: 范建海
 * @createTime: 2016/10/30 13:51
 * @className: ManifestUtil
 * @description: 用于读取AndroidManifest.xml文件中的值
 * @changed by:
 */
public class ManifestUtil {
    /**
     * 获得包名
     *
     * @param context 上下文
     * @return 包名
     */
    public static String getPackageName(Context context) {
        try {
            return context.getPackageManager().getPackageInfo(context.getPackageName(), 0).packageName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获得版本名
     *
     * @param context 上下文
     * @return 版本名
     */
    public static String getVersionName(Context context) {
        try {
            return context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获得版本号
     *
     * @param context 上下文
     * @return 版本号
     */
    public static int getVersionCode(Context context) {
        try {
            return context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * 获取application层级的metadata
     *
     * @param context 上下文
     * @param key     key
     * @return value
     */
    public static String getApplicationMetaData(Context context, String key) {
        try {
            Bundle metaData = context.getPackageManager().getApplicationInfo(context.getPackageName(), PackageManager.GET_META_DATA).metaData;
            return metaData.get(key).toString();
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 判断应用是否申明某个权限
     *
     * @param context    上下文
     * @param permission Manifest.permission里的值
     * @return 是否申明某个权限
     */
    public static boolean hasPermission(Context context, String permission) {
        return (PackageManager.PERMISSION_GRANTED) == (context.getPackageManager().checkPermission(permission, context.getPackageName()));
    }

    /**
     * 获得应用申明的所有权限列表
     *
     * @param context 上下文
     * @return 获得应用申明的所有权限列表
     */
    public static List<String> getPermissions(Context context) {
        List<String> permissions = new ArrayList<String>();
        try {
            PackageInfo packageInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), PackageManager.GET_PERMISSIONS);
            permissions.addAll(Arrays.asList(packageInfo.requestedPermissions));

        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return permissions;
    }


    /**
     * 判断当前应用程序处于后台运行
     *
     * @param context
     * @return true  在后台运行
     */
    public static boolean isApplicationBroughtToBackground(final Context context) {
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> tasks = am.getRunningTasks(1);
        if (!tasks.isEmpty()) {
            ComponentName topActivity = tasks.get(0).topActivity;
            if (!topActivity.getPackageName().equals(context.getPackageName())) {
                return true;
            }
        }
        return false;
    }

    /**
     * 判定应用是否在前台运行(以是否在前台可见为标准).
     *
     * @param context
     * @return true  在前台; false 在后台或被杀死
     */
    public static boolean isTheForeground(Context context) {

        ActivityManager activityManager = (ActivityManager) context
                .getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> appProcesses = activityManager
                .getRunningAppProcesses();

        for (ActivityManager.RunningAppProcessInfo appProcess : appProcesses) {
            if (appProcess.processName.equals(context.getPackageName())) {
                LogUtil.i("yms appProcess.importance", appProcess.importance + "");
                if (appProcess.importance <= ActivityManager.RunningAppProcessInfo.IMPORTANCE_VISIBLE) {
                    // 某些 Android 5.0之前的手机需要在此处再作判断
                    if (isApplicationBroughtToBackground(context)) {
                        LogUtil.i("yms Background App:", appProcess.processName);
                        return false;
                    }
                    LogUtil.i("yms Foreground App:", appProcess.processName);
                    return true;
                } else {
                    LogUtil.i("yms Background App:", appProcess.processName);
                    return false;
                }
            }
        }
        return false;
    }

}
