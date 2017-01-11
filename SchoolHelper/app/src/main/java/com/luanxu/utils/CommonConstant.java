package com.luanxu.utils;

import java.io.File;

/**
 * @author: LuanXu
 * @createTime:2016/12/4 14:31
 * @className:  CommonConstant
 * @Description: 常量
 */

public class CommonConstant {

    //项目文件保存总路径
    public static final String PROJECT_NAME = "schoolHelper" + File.separator;
    // 应用外部存储的根路径
    public static final String APP_LOCAL_PATH_ROOT = ExternalStorageUtil.getExternalStoragePath() + File.separator +  PROJECT_NAME;
    // 缓存信息路径
    public static final String CACHE_PATH = APP_LOCAL_PATH_ROOT + "cache" + File.separator;


    public static final int auto_focus = 1;
    public static final int decode = 2;
    public static final int decode_failed = 3;
    public static final int decode_succeeded = 4;

    public static final int launch_product_query = 7;
    public static final int quit = 8;
    public static final int restart_preview = 9;
    public static final int return_scan_result = 10;

}
