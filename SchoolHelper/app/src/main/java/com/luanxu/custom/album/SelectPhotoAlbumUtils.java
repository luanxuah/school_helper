package com.luanxu.custom.album;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.hardware.Camera;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;

import com.luanxu.application.SchoolHelperApplication;
import com.luanxu.base.BaseActivity;
import com.luanxu.custom.CommonDialog;
import com.luanxu.schoolhelper.R;
import com.luanxu.utils.BitmapManager;
import com.luanxu.utils.CommonConstant;
import com.luanxu.utils.LogUtil;
import com.luanxu.utils.ResourceUtil;
import com.luanxu.utils.SharedPreferencesUtil;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author: LuanXu
 * @createTime:2016/9/8 16:50
 * @className:  SelectPhotoAlbumUtils
 * @Description: 图库的工具类
 */

public class SelectPhotoAlbumUtils {
    // 拍照，从相机选择请求码
    public static final int ACTION_SHARE_FROM_CAMERA = 1000;
    // 从相册中选择图片请求码
    public static final int ACTION_SHARE_FROM_ALBUM = 1001;
    // 裁剪图片请求码
    public static final int CROP_PHOTO_REQUEST_CODE = 1007;
    //该图片是否旋转（适配三星手机拍照后图片倾斜的问题），0是未旋转
    public static int degree = 0;

    /**
     * 从图库中选择图片
     * @param activity 上下文对象
     * @param requestCode 请求码
     * @param maxSelect 最多选择图片的数目
     */
    public static void selectPhoto(Activity activity, int requestCode, int maxSelect){
        MediaOptions.Builder builder = new MediaOptions.Builder();
        MediaOptions options;
        options = builder.canSelectMultiPhoto(true).build();
        if (options != null) {
            MediaPickerActivity.open(activity, requestCode, options, maxSelect);
        }
    }

    /**
     * 从图库中选择视频
     * @param activity 上下文对象
     * @param requestCode 请求码
     * @param maxSelect 最大选择视频的数目
     */
    public static void selectVideo(Activity activity, int requestCode, int maxSelect){
        MediaOptions.Builder builder = new MediaOptions.Builder();
        MediaOptions options;
        options = builder.selectVideo().canSelectMultiVideo(true).build();
        if (options != null) {
            MediaPickerActivity.open(activity, requestCode, options, maxSelect);
        }
    }

    /**
     * 从图库选择视频并限制最大时长
     * @param activity 上下文对象
     * @param requestCode 请求码
     * @param duration 视频的最大时长
     */
    public static void setectVideoLimit(Activity activity, int requestCode, int duration){
        MediaOptions.Builder builder = new MediaOptions.Builder();
        MediaOptions options;
        options = builder.selectVideo().setMaxVideoDuration(duration * 1000)
                .build();
        if (options != null) {
            MediaPickerActivity.open(activity, requestCode, options, 1);
        }
    }

    /**
     * 从图库中选择图片或视频
     * @param activity 上下文对象
     * @param maxSelect 最大选择视频或图片的数目
     */
    public static void selectPhotoAndVideo(Activity activity, int requestCode, int maxSelect){
        MediaOptions.Builder builder = new MediaOptions.Builder();
        MediaOptions options;
        options = builder.canSelectBothPhotoVideo()
                .canSelectMultiPhoto(true).canSelectMultiVideo(true)
                .build();
        if (options != null) {
            MediaPickerActivity.open(activity, requestCode, options, maxSelect);
        }
    }

    /**
     * 从相册中选择图片或视频，视频限制时长
     * @param activity 上下文对象
     * @param requestCode 请求码
     * @param maxSelect 最多选择数
     * @param duration 视频的最大时长
     */
    public static void selectPhotoAndVideoLimit(Activity activity, int requestCode, int maxSelect, int duration){
        MediaOptions.Builder builder = new MediaOptions.Builder();
        MediaOptions options;
        options = builder.canSelectBothPhotoVideo()
                .canSelectMultiPhoto(true).canSelectMultiVideo(true).setMaxVideoDuration(duration * 1000)
                .build();
        if (options != null) {
            MediaPickerActivity.open(activity, requestCode, options, maxSelect);
        }
    }

    /**
     * 打开相机进行拍照
     * @param activity 上下文对象
     * @param requestCode 请求码
     */
    public static void openCamera(final Activity activity, int requestCode){
        try {
            //验证拍照权限是否打开
            Camera camera = Camera.open();
            camera.release();
        } catch (Exception e) {
            //没有打开显示提示对话框
            final CommonDialog dialog = new CommonDialog(activity,CommonDialog.WEIXIN_STYLE);
            dialog.setTitle(ResourceUtil.getString(activity, R.string.str_open_camera_power));
            dialog.setMessage(ResourceUtil.getString(activity, R.string.str_open_camera_power_primpt));
            dialog.setPositiveButton(new CommonDialog.BtnClickedListener() {
                @Override
                public void onBtnClicked() {
                    dialog.dismiss();
                }
            }, "知道了");
            dialog.showDialog();
            return;
        }
        Intent openCameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        Uri imageUri = getOutputMediaFileUri();
        openCameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        activity.startActivityForResult(openCameraIntent, requestCode);
        ((BaseActivity)activity).onSaveInstance(new Bundle());
    }

    /**
     * 用于拍照时获取输出的Uri
     * @return
     */
    public static Uri getOutputMediaFileUri() {
        File mediaStorageDir = new File(CommonConstant.IMAGE_PATH);
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                LogUtil.i("doctorlog   返回");
                return null;
            }
        }
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        File mediaFile = new File(mediaStorageDir.getAbsolutePath() + File.separator + "IMG_" + timeStamp + ".jpg");
        SharedPreferencesUtil.set(SchoolHelperApplication.getInstance() , CommonConstant.IMAGE_PATH_FILE_NAME, CommonConstant.IMAGE_PATH_KEY, mediaFile.getAbsolutePath());
        String photoResultPath = SharedPreferencesUtil.get(SchoolHelperApplication.getInstance(), CommonConstant.IMAGE_PATH_FILE_NAME, CommonConstant.IMAGE_PATH_KEY);
        LogUtil.i("doctorlog    path===="+photoResultPath);
        return Uri.fromFile(mediaFile);
    }

    /**
     * 获取拍照后返回的原图地址，如果图片被旋转，纠正图片方向（适配三星手机）（该方法放在onActivity方法中），用于展示
     * @return 拍照的图片地址
     */
    public static String getCameraPhotoPath(){
        final String photoResultPath = SharedPreferencesUtil.get(SchoolHelperApplication.getInstance() , CommonConstant.IMAGE_PATH_FILE_NAME, CommonConstant.IMAGE_PATH_KEY);
        LogUtil.i("doctorlog    path===="+photoResultPath);
        if (!TextUtils.isEmpty(photoResultPath)){
            degree = BitmapManager.readPictureDegree(photoResultPath);
        }else{
            degree = 0;
        }
        LogUtil.i("doctorlog   degree="+degree);
        if (degree!=0){
            //图片被旋转，开启子线程纠正图片方向
            new Thread(new Runnable() {
                @Override
                public void run() {
                    Bitmap diskBitmap = BitmapManager.getDiskBitmap(photoResultPath);
                    Bitmap bitmap = BitmapManager.rotateBitmapByDegree(diskBitmap, degree);
                    //旋转后图片的文件地址
                    String roatephotoUrl = photoResultPath.substring(0, photoResultPath.lastIndexOf(".jpg"))+"_rotate.jpg";
                    //压缩并且生成旋转后的图片
                    BitmapManager.saveBitmap(bitmap, roatephotoUrl);
                }
            }).start();
        }
        return photoResultPath;
    }

    /**
     * 获取纠正后的图片地址，该方法放到getCameraPhotoPath方法之后，用于上传
     * 注意：由于矫正图片的方向是放到子线程中，生成矫正后的图片大概需要0.5s，故如果需要立马上传文件,如关系链对话传输图片不适用
     * @return
     */
    public static String getRotatePhotoUrl(){
        final String photoResultPath = SharedPreferencesUtil.get(SchoolHelperApplication.getInstance() , CommonConstant.IMAGE_PATH_FILE_NAME, CommonConstant.IMAGE_PATH_KEY);
        LogUtil.i("doctorlog    Ropath===="+photoResultPath);
        if (degree!=0 && !TextUtils.isEmpty(photoResultPath)){
            String rotatephotoUrl = photoResultPath.substring(0, photoResultPath.lastIndexOf(".jpg"))+"_rotate.jpg";
            return rotatephotoUrl;
        }else{
            return photoResultPath;
        }
    }

    /**
     * 获取纠正后的图片地址，该方法是放到主线程中，会有0.5s延迟，适用于获取完地址后需要立马使用的情况，如关系链聊天图片
     * @return 纠正后的图片地址
     */
    public static String getRotatePhotoUrlForMain(){
        final String photoResultPath = SharedPreferencesUtil.get(SchoolHelperApplication.getInstance() , CommonConstant.IMAGE_PATH_FILE_NAME, CommonConstant.IMAGE_PATH_KEY);
        LogUtil.i("doctorlog    Ro1111path===="+photoResultPath);
        if (!TextUtils.isEmpty(photoResultPath)){
            degree = BitmapManager.readPictureDegree(photoResultPath);
        }else{
            degree = 0;
        }

        LogUtil.i("doctorlog  degree="+degree);
        String rotateUrl = photoResultPath;
        if (degree!=0){
            //图片被旋转，开启子线程纠正图片方向
            Bitmap diskBitmap = BitmapManager.getDiskBitmap(photoResultPath);
            Bitmap bitmap = BitmapManager.rotateBitmapByDegree(diskBitmap, degree);
            //旋转后图片的文件地址
            String roatephotoUrl = photoResultPath.substring(0, photoResultPath.lastIndexOf(".jpg"))+"_rotate.jpg";
            //压缩并且生成旋转后的图片
            rotateUrl = BitmapManager.saveBitmap(bitmap, roatephotoUrl);
        }
        return rotateUrl;
    }

    /**
     * 获取从相册中选择的文件地址（该方法放在onActivityResult方法中）
     * @param activity 上下文对象
     * @param data 返回的intent
     * @return 图片的本地地址的集合
     */
    public static List<String> getSelectPath(Activity activity, Intent data){
        List<String> selectPathList = new ArrayList<String>();
        List<MediaItem> mediaSelectedList = MediaPickerActivity
                .getMediaItemSelected(data);
        if (mediaSelectedList != null){
            for (int i = 0; i<mediaSelectedList.size(); i++){
                    selectPathList.add(mediaSelectedList.get(i).getPathOrigin(activity).toString());
            }
        }
        return selectPathList;
    }
}
