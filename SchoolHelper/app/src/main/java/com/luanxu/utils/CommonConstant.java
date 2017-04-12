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
    // 视频路径
    public static final String VIDEO_PATH = CACHE_PATH + "video" + File.separator;
    // 图片路径
    public static final String IMAGE_PATH = CACHE_PATH + "image" + File.separator;
    // 无网络的提示语
    public static final String NO_NETWORK_HINT = "当前没有网络，请检查网络连接";
    // 加载进度条的默认提示语
    public static final String LOADING_MSG = "数据加载中...";
    // Token 所在文件名
    public static final String TOKEN_FILE_NAME = "token_file_name";
    // Token码对应的Key
    public static final String TOKEN_CODE_KEY = "token_code_key";

    public static final int auto_focus = 1;
    public static final int decode = 2;
    public static final int decode_failed = 3;
    public static final int decode_succeeded = 4;

    public static final int launch_product_query = 7;
    public static final int quit = 8;
    public static final int restart_preview = 9;
    public static final int return_scan_result = 10;

    public static final String KEY_RESLUT = "key_reslut";
    // 图片拍照后图片地址字符串所在的文件名
    public static final String IMAGE_PATH_FILE_NAME = "image_path";
    // 图片拍照后图片地址字符串对应的Key
    public static final String IMAGE_PATH_KEY = "imagePath";

}
