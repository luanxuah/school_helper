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

}
