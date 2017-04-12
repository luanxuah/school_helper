package com.luanxu.application;

import android.app.Activity;
import android.app.Application;

import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.WeakMemoryCache;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

/**
 * Created by 栾煦 on 2016/11/29.
 */
public class SchoolHelperApplication extends Application{
    /** 用户缓存Activity的集合 **/
    private List<Activity> mActCollections;
    /** 应用实例 **/
    private static SchoolHelperApplication instance;
    //线程池 定长 支持定时及周期性任务  减轻application的初始化，只在使用时获初始化线程池
    private ScheduledExecutorService scheduledThreadPool;
    //最大线程数
    private final static int MAX_SIZE = 3;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        mActCollections = new ArrayList<Activity>();
        initImageLoader();
    }

    /**
     * 初始化ImageLoader组件
     */
    public static void initImageLoader() {
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
                instance).threadPriority(Thread.NORM_PRIORITY - 2)
                .denyCacheImageMultipleSizesInMemory()
                .diskCacheFileNameGenerator(new Md5FileNameGenerator())
                .diskCacheSize(50 * 1024 * 1024)
                // 50 Mb
                .tasksProcessingOrder(QueueProcessingType.LIFO)
                .memoryCache(new WeakMemoryCache())// 采用软引用
                .writeDebugLogs() // Remove for release app
                .build();
        ImageLoader.getInstance().init(config);
    }

    /**
     * 退出应用，释放内存
     */
    public void exit() {
        for (Activity activity : mActCollections) {
            // 此处要判空处理，否则时间一长，退出就会出现奔溃的现象
            if (activity != null && !activity.isFinishing()) {
                activity.finish();
            }
        }
        System.exit(0);
    }

    /** 获取应用实例 **/
    public static SchoolHelperApplication getInstance() {
        return instance;
    }

    /**
     * 创建Activity时，需要把该Activity添加到集合中
     * @param activity
     */
    public void addActivity(Activity activity) {
        mActCollections.add(activity);
    }

    /**
     * 减轻application的初始化，只在使用时获初始化线程池
     */
    public ScheduledExecutorService getThreadPool(){
        if (null == scheduledThreadPool){
            scheduledThreadPool = Executors.newScheduledThreadPool(MAX_SIZE);
        }
        return scheduledThreadPool;
    }
}
