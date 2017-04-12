package com.luanxu.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.text.TextUtils;
import android.widget.ImageView;

import com.luanxu.activity.ActPreViewIcon;
import com.luanxu.bean.PreviewedImageInfo;
import com.luanxu.custom.album.SelectPhotoAlbumUtils;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import java.io.File;

/**
 * @author: 范建海
 * @createTime: 2016/10/8 11:29
 * @className:  LoadImageUtil
 * @description: 加载图片的工具类
 * @changed by:
 */
public class LoaderImageUtil {
    // 头像类缩略图类型
    public static final String TYPE_IMG_100PX_SIZE = "@!100px_db";
    // 期刊缩略图类型
    public static final String TYPE_IMG_160PX_SIZE = "@!160px_db";
    // 其他缩略(除了头像类型和期刊封面)图类型
    public static final String TYPE_IMG_250PX_SIZE = "@!250px_db";
    // 默认图片的资源ID
    private static int defaultImgID;
    // 开启预览界面标志
    public static final String TAG_START_PREVIEW = "picture_info";

    /**
     * 展示网络图片(缩略图)
     * @param url 图片路径
     * @param defaultImgID 默认图片的资源ID
     * @param iv 加载图片的控件
     * @param imgType 缩略图类型
     *                 {@link LoaderImageUtil#TYPE_IMG_100PX_SIZE}
     *                 {@link LoaderImageUtil#TYPE_IMG_160PX_SIZE}
     *                 {@link LoaderImageUtil#TYPE_IMG_250PX_SIZE}
     */
    public static void displayFromNet(String url, int defaultImgID, ImageView iv, String imgType) {
        if(!TextUtils.isEmpty(url) && !TextUtils.isEmpty(imgType) && iv != null) {
            innerDisplay(defaultImgID, url + imgType, iv,null);
        }
    }

    /**
     * 展示网络图片（原图）
     * @param url 图片路径
     * @param defaultImgID 默认图片资源ID
     * @param iv 加载图片的控件
     */
    public static void displayFromNet(String url, int defaultImgID, ImageView iv) {
        if(!TextUtils.isEmpty(url) && iv != null) {
            innerDisplay(defaultImgID, url, iv,null);
        }
    }

    /**
     * 展示网络图片（原图）
     * @param url 图片路径
     * @param defaultImgID 默认图片资源ID
     * @param iv 加载图片的控件
     */
    public static void display(String url, int defaultImgID, ImageView iv, ImageLoadingListener listener) {
        if(!TextUtils.isEmpty(url) && iv != null) {
            if (url.startsWith("http")){
                innerDisplay(defaultImgID, url, iv,listener);
            }else{
                innerDisplay(defaultImgID, "file://" + url, iv, listener);
            }
        }
    }

    /**
     * 加载SDcard中本地图片
     * @param uri
     * @param iv
     */
    public static void displayFromSDCard(String uri, int defaultImgID, ImageView iv) {
        innerDisplay(defaultImgID, "file://" + uri, iv,null);
    }

    /**
     * 从assets文件夹中异步加载图片
     * @param imageName 图片名称，带后缀的，例如：1.png
     * @param iv 图片控件
     */
    public static void dispalyFromAssets(String imageName, ImageView iv) {
        innerDisplay(0, "assets://" + imageName,iv,null);
    }

    /**
     * 从drawable中已不加载本地图片， (only images, non-9patch)
     * @param resId
     * @param iv
     */
    public static void displayFromDrawable(int resId, ImageView iv) {
        innerDisplay(0, "drawable://" + resId,iv,null);
    }

    /**
     * 从内容提提供者中抓取图片
     * @param uri  例如：content://media/external/audio/albumart/13;
     * @param iv 图片控件
     */
    public static void displayFromContent(String uri, ImageView iv) {
        innerDisplay(0, "content://" + uri,iv,null);
    }

    /**
     * 内部处理图片逻辑
     * @param defaultImgID 默认图片id
     * @param url 图片路径
     * @param iv 加载图片的控件
     * @param listener 加载图片时的回调
     */
    private static void innerDisplay(int defaultImgID, String url, ImageView iv,ImageLoadingListener listener) {
        DisplayImageOptions.Builder builder = new DisplayImageOptions.Builder();
        builder.showImageForEmptyUri(defaultImgID);
        builder.showImageOnFail(defaultImgID);
        builder.cacheInMemory(true);
        builder.cacheOnDisk(true);
        builder.considerExifParams(true);
        builder.imageScaleType(ImageScaleType.EXACTLY);
        builder.bitmapConfig(Bitmap.Config.RGB_565);
        DisplayImageOptions	options = builder.build();
        ImageLoader.getInstance().displayImage(url,iv,options,listener);
    }

    /**
     * 开启预览图片界面
     * @param previewedImageInfo 显示图片时的相关信息实体Bean
     * @param activity Activity页面上下文
     */
    public static void previewLargePic(PreviewedImageInfo previewedImageInfo, Activity activity) {
        if (previewedImageInfo != null && activity != null ) {

            Intent intent = new Intent(activity, ActPreViewIcon.class);
            intent.putExtra(TAG_START_PREVIEW,previewedImageInfo);
            activity.startActivity(intent);
        }
    }

    /**
     * 调用系统的视频播放器进行视频播放
     * @param videoPath 视频的本地地址
     * @param context 上下文对象
     */
    public static void previewVideo(String videoPath, Context context){
        Uri uri = Uri.parse(videoPath);
        Intent intent = new Intent(Intent.ACTION_VIEW);
        String style="";
        if(videoPath.toLowerCase().endsWith(".mp4")){
            style="mp4";
        }
        else if(videoPath.toLowerCase().endsWith(".3gp")){
            style="3gp";
        }
        else if(videoPath.toLowerCase().endsWith(".mov")){
            style="mov";
        }
        else if(videoPath.toLowerCase().endsWith(".wmv")){
            style="wmv";
        }
        intent.setDataAndType(uri, "video/"+style);
        context.startActivity(intent);
    }

    /**
     * 	裁剪图片
     * @param uri 被裁剪图片的 uri
     * @param path 被裁剪图片的路径
     * @param act 所在的activity
     * @return 裁剪好的图片路径
     */
    public static String cropPhoto(Uri uri,String path,Activity act) {
        if (uri != null && !TextUtils.isEmpty(path) && act != null) {
            Intent iintent = new Intent("com.android.camera.action.CROP");
            // 裁剪图的文件名
            String shortCutName = path.substring((path.lastIndexOf("/") + 1), path.lastIndexOf("."))+"_tailor"+path.substring(path.lastIndexOf("."));
            LogUtil.i("doctorlog     tailor="+shortCutName);
            File directory = new File(CommonConstant.IMAGE_PATH) ;

            if (!directory.exists()) {
                directory.mkdirs();
            }

            iintent.setDataAndType(uri, "image/*");
            iintent.putExtra("crop", "true");//显示裁剪区域
            iintent.putExtra("outputX", 560);
            iintent.putExtra("outputY", 560);
            iintent.putExtra("aspectX", 1);
            iintent.putExtra("aspectY", 1);
            iintent.putExtra("scale", true);
            iintent.putExtra("return-data", false);// true:不返回uri，false：返回uri
            iintent.putExtra("output", Uri.fromFile(new File(CommonConstant.IMAGE_PATH + shortCutName)));
            iintent.putExtra("outputFormat", "JPEG");//返回格式
            act.startActivityForResult(iintent, SelectPhotoAlbumUtils.CROP_PHOTO_REQUEST_CODE);

            return CommonConstant.IMAGE_PATH + shortCutName;

        }

        return "";
    }

}






























