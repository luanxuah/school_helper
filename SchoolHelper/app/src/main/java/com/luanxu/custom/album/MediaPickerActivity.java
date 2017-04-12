package com.luanxu.custom.album;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.FileObserver;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.luanxu.base.BaseActivity;
import com.luanxu.bean.PreviewedImageInfo;
import com.luanxu.custom.CommonDialog;
import com.luanxu.custom.percent.PercentLinearLayout;
import com.luanxu.schoolhelper.R;
import com.luanxu.utils.CommonConstant;
import com.luanxu.utils.LoaderImageUtil;
import com.luanxu.utils.LogUtil;
import com.luanxu.utils.ResourceUtil;
import com.luanxu.utils.ToastUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * @author: LuanXu
 * @createTime:2016/12/9 16:00
 * @className:  MediaPickerActivity
 * @Description: 相册
 */
public class MediaPickerActivity extends BaseActivity implements
        MediaSelectedListener, CropListener, FragmentManager.OnBackStackChangedListener, FragmentHost, View.OnClickListener{
    private static final String TAG = "MediaPickerActivity";
    //拍摄视频请求码
    public static final int REQUEST_TAKE_VIDEO = 10;

    public static final String EXTRA_MEDIA_OPTIONS = "extra_media_options";
    /**
     * Intent extra included when return back data in
     * {@link Activity#onActivityResult(int, int, Intent)} of activity or fragment
     * that open media picker. Always return {@link ArrayList} of
     * {@link MediaItem}. You must always check null and size of this list
     * before handle your logic.
     */
    public static final String EXTRA_MEDIA_SELECTED = "extra_media_selected";
    private static final int REQUEST_PHOTO_CAPTURE = 100;
    private static final int REQUEST_VIDEO_CAPTURE = 200;

    private static final String KEY_PHOTOFILE_CAPTURE = "key_photofile_capture";

    private MediaOptions mMediaOptions;

    private File mPhotoFileCapture;
    private List<File> mFilesCreatedWhileCapturePhoto;
    private RecursiveFileObserver mFileObserver;
    private FileObserverTask mFileObserverTask;
    /**最多选择的文件数**/
    public static int maxSelectNum;
    //判断是不是在拍照
    public static boolean isCamera;

    /**
     * Start {@link MediaPickerActivity} in {@link Activity} to pick photo or
     * video that depends on {@link MediaOptions} passed.
     *
     * @param activity
     * @param requestCode
     * @param options
     */
    public static void open(Activity activity, int requestCode,
                            MediaOptions options, int max) {
        maxSelectNum = max;
        Intent intent = new Intent(activity, MediaPickerActivity.class);
        intent.putExtra(EXTRA_MEDIA_OPTIONS, options);
        activity.startActivityForResult(intent, requestCode);
    }

    /**
     * Start {@link MediaPickerActivity} in {@link Activity} with default media
     * option: {@link MediaOptions#createDefault()}
     *
     * @param activity
     * @param requestCode
     */
    public static void open(Activity activity, int requestCode, int max) {
        open(activity, requestCode, MediaOptions.createDefault(), max);
    }

    /**
     * Start {@link MediaPickerActivity} in {@link Fragment} to pick photo or
     * video that depends on {@link MediaOptions} passed.
     *
     * @param fragment
     * @param requestCode
     * @param options
     */
    public static void open(Fragment fragment, int requestCode,
                            MediaOptions options) {
        Intent intent = new Intent(fragment.getActivity(),
                MediaPickerActivity.class);
        intent.putExtra(EXTRA_MEDIA_OPTIONS, options);
        fragment.startActivityForResult(intent, requestCode);
    }

    /**
     * Start {@link MediaPickerActivity} in {@link Fragment} with default media
     * option: {@link MediaOptions#createDefault()}
     *
     * @param fragment
     * @param requestCode
     */
    public static void open(Fragment fragment, int requestCode) {
        open(fragment, requestCode, MediaOptions.createDefault());
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: not support change orientation right now (because out of
        // memory when crop image and change orientation, must check third party
        // to crop image again).
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_mediapicker);

        File dest = new File(CommonConstant.APP_LOCAL_PATH_ROOT);
        if (!dest.exists()) {
            if (!dest.mkdirs()) {
                showPromptDialog();
                return;
            }
        }

        if (savedInstanceState != null) {
            mMediaOptions = savedInstanceState
                    .getParcelable(EXTRA_MEDIA_OPTIONS);
            mPhotoFileCapture = (File) savedInstanceState
                    .getSerializable(KEY_PHOTOFILE_CAPTURE);
            //是不是正在拍照的时候崩溃的
            boolean isCamera = savedInstanceState.getBoolean("isCamera", false);
            if (isCamera){
                //从相册拍照返回
                String photoPath=SelectPhotoAlbumUtils.getCameraPhotoPath();
                LogUtil.i("doctorlog   photoPath= "+photoPath);
                returnPath(photoPath);
            }
        } else {
            mMediaOptions = getIntent().getParcelableExtra(EXTRA_MEDIA_OPTIONS);
            if (mMediaOptions == null) {
                throw new IllegalArgumentException(
                        "MediaOptions must be not null, you should use MediaPickerActivity.open(Activity activity, int requestCode,MediaOptions options) method instead.");
            }
        }
        if (getActivePage() == null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.container,
                            MediaPickerFragment.newInstance(mMediaOptions))
                    .commit();
        }
        getSupportFragmentManager().addOnBackStackChangedListener(this);
    }

    /**
     * 显示提示注意框
     */
    public void showPromptDialog(){
        final CommonDialog dialog = new CommonDialog(MediaPickerActivity.this,CommonDialog.WEIXIN_STYLE);
        dialog.setTitle(ResourceUtil.getString(MediaPickerActivity.this, R.string.str_open_store_power));
        dialog.setMessage(ResourceUtil.getString(MediaPickerActivity.this, R.string.str_open_store_power_primpt));
        dialog.setPositiveButton(new CommonDialog.BtnClickedListener() {
            @Override
            public void onBtnClicked() {
                dialog.dismiss();
                MediaPickerActivity.this.setResult(Activity.RESULT_CANCELED);
                MediaPickerActivity.this.finish();
            }
        }, "知道了");
        dialog.showDialog();
    }

    private PercentLinearLayout iv_back;
    private TextView tv_title;
    private TextView iv_change;
    private TextView tv_complete;
    private LinearLayout ll_preview;

    @Override
    protected void onResume() {
        super.onResume();
        init();
    }

    private void init(){
        iv_back = (PercentLinearLayout) findViewById(R.id.title_left);
        iv_back.setOnClickListener(this);
        tv_title = (TextView) findViewById(R.id.title_txt);
        tv_title.setOnClickListener(this);
        iv_change = (TextView) findViewById(R.id.right_btn1);
        iv_change.setOnClickListener(this);
        tv_complete = (TextView) findViewById(R.id.right_btn);
        ll_preview= (LinearLayout) findViewById(R.id.tv_mediapicker_preview);
        ll_preview.setOnClickListener(this);
        tv_complete.setOnClickListener(this);
        syncActionbar();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        getSupportFragmentManager().removeOnBackStackChangedListener(this);
        cancelFileObserverTask();
        stopWatchingFile();
        mFilesCreatedWhileCapturePhoto = null;
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(EXTRA_MEDIA_OPTIONS, mMediaOptions);
        outState.putSerializable(KEY_PHOTOFILE_CAPTURE, mPhotoFileCapture);
        outState.putBoolean("isCamera", isCamera);
    }

    @Override
    public MediaImageLoader getImageLoader() {
        return new MediaImageLoaderImpl(getApplicationContext());
    }

    @Override
    public void onHasNoSelected() {
        tv_complete.setVisibility(View.GONE);
        syncActionbar();
    }

    @Override
    public void onHasSelected(List<MediaItem> mediaSelectedList) {
        showDone();
    }

    private void showDone() {
        tv_complete.setVisibility(View.VISIBLE);
        iv_change.setVisibility(View.GONE);
    }

    private void syncMediaOptions() {
        // handle media options
        if (mMediaOptions.canSelectPhotoAndVideo()) {
            iv_change.setVisibility(View.VISIBLE);
        } else {
            iv_change.setVisibility(View.GONE);
        }
    }

    private void syncIconMenu(int mediaType) {
        View root= LayoutInflater.from(this).inflate(R.layout.list_item_mediapicker_camera,null);
        TextView tvType= (TextView) root.findViewById(R.id.tv_media_piker_type);
        switch (mediaType) {
            case MediaItem.PHOTO:
                tv_title.setText("图片");
                tvType.setText("拍摄照片");
                iv_change.setCompoundDrawablesWithIntrinsicBounds(0,0,R.mipmap.ab_picker_video_2,0);
                break;
            case MediaItem.VIDEO:
                tv_title.setText("视频");
                tvType.setText("拍摄视频");
                iv_change.setCompoundDrawablesWithIntrinsicBounds(0,0,R.mipmap.ab_picker_camera2,0);
                break;
            default:
                break;
        }
    }

    private void returnBackData(List<MediaItem> mediaSelectedList) {
        Intent data = new Intent();
        data.putParcelableArrayListExtra(EXTRA_MEDIA_SELECTED,
                (ArrayList<MediaItem>) mediaSelectedList);
        setResult(Activity.RESULT_OK, data);
        finish();
    }
    public static String getPath(Intent data){
        if (data==null){
            return null;
        }
        String path= data.getStringExtra("path");
        Log.d(TAG, "getPath: "+path);
        return  path;
    }
    private void returnPath(String path){
        Intent intent=new Intent();
        intent.putExtra("path",path);
        setResult(Activity.RESULT_OK, intent);
        finish();
    }

    private RecursiveFileObserver.OnFileCreatedListener mOnFileCreatedListener = new RecursiveFileObserver.OnFileCreatedListener() {

        @Override
        public void onFileCreate(File file) {
            if (mFilesCreatedWhileCapturePhoto == null)
                mFilesCreatedWhileCapturePhoto = new ArrayList<File>();
            mFilesCreatedWhileCapturePhoto.add(file);
        }
    };

    /**
     * In some HTC devices (maybe others), duplicate image when captured with
     * extra_output. This method will try delete duplicate image. It's prefer
     * default image by camera than extra output.
     */
    private void tryCorrectPhotoFileCaptured() {
        if (mPhotoFileCapture == null || mFilesCreatedWhileCapturePhoto == null
                || mFilesCreatedWhileCapturePhoto.size() <= 0)
            return;
        long captureSize = mPhotoFileCapture.length();
        for (File file : mFilesCreatedWhileCapturePhoto) {
            if (MediaUtils
                    .isImageExtension(MediaUtils.getFileExtension(file))
                    && file.length() >= captureSize
                    && !file.equals(mPhotoFileCapture)) {
                boolean value = mPhotoFileCapture.delete();
                mPhotoFileCapture = file;
                Log.i(TAG,
                        String.format(
                                "Try correct photo file: Delete duplicate photos in [%s] [%s]",
                                mPhotoFileCapture, value));
                return;
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        isCamera = false;
        cancelFileObserverTask();
        stopWatchingFile();
        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case REQUEST_PHOTO_CAPTURE:
                    //从相册选取照片返回
                    tryCorrectPhotoFileCaptured();
                    if (mPhotoFileCapture != null) {
                        MediaUtils.galleryAddPic(getApplicationContext(),
                                mPhotoFileCapture);
                        if (mMediaOptions.isCropped()) {

                        } else {
                            MediaItem item = new MediaItem(MediaItem.PHOTO,
                                    Uri.fromFile(mPhotoFileCapture));
                            ArrayList<MediaItem> list = new ArrayList<MediaItem>();
                            list.add(item);
                            returnBackData(list);
                        }
                    }
                    break;
                case SelectPhotoAlbumUtils.ACTION_SHARE_FROM_CAMERA:
                    //从相册拍照返回
                    String photoPath=SelectPhotoAlbumUtils.getCameraPhotoPath();
                    returnPath(photoPath);
                    break;
                case REQUEST_VIDEO_CAPTURE:
                    //从相册选取视频返回
                    returnVideo(data.getData());
                    break;
                case REQUEST_TAKE_VIDEO:
                    //从相册拍摄视频返回
                    String videoPath=data.getStringExtra(RecordingVideoActivity.KEY_VIDEO_FILE_PATH);
                    returnPath(videoPath);
                    break;
                default:
                    break;
            }
        }
    }

    @Override
    public void onSuccess(MediaItem mediaItem) {
        List<MediaItem> list = new ArrayList<MediaItem>();
        list.add(mediaItem);
        returnBackData(list);
    }

    @Override
    public void onBackStackChanged() {
        syncActionbar();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        syncActionbar();
    }

    public void syncActionbar() {
        Fragment fragment = getActivePage();
        if (fragment instanceof MediaPickerFragment) {
            Log.i("doctorlog","是的");
            syncMediaOptions();
            MediaPickerFragment pickerFragment = (MediaPickerFragment) fragment;
            syncIconMenu(pickerFragment.getMediaType());
            if (pickerFragment.hasMediaSelected()) {
                showDone();
            } else {
                tv_complete.setVisibility(View.GONE);
            }
        }else{
            Log.i("doctorlog","不是");
        }
    }

    private Fragment getActivePage() {
        return getSupportFragmentManager().findFragmentById(R.id.container);
    }

    private void hideAllOptionsMenu() {
        if (tv_complete != null){
            tv_complete.setVisibility(View.GONE);
        }
        if (iv_change != null){
            iv_change.setVisibility(View.GONE);
        }
    }

    /**
     * Check video duration valid or not with options.
     *
     * @param videoUri
     * @return 1 if valid, otherwise is invalid. -2: not found, 0 larger than
     * accepted, -1 smaller than accepted.
     */
    private int checkValidVideo(Uri videoUri) {
        if (videoUri == null)
            return -2;
        // try get duration using MediaPlayer. (Should get duration using
        // MediaPlayer before use Uri because some devices can get duration by
        // Uri or not exactly. Ex: Asus Memo Pad8)
        long duration = MediaUtils.getDuration(getApplicationContext(),
                MediaUtils.getRealVideoPathFromURI(getContentResolver(), videoUri));
        if (duration == 0) {
            // try get duration one more, by uri of video. Note: Some time can
            // not get duration by Uri after record video.(It's usually happen
            // in HTC
            // devices 2.3, maybe others)
            duration = MediaUtils
                    .getDuration(getApplicationContext(), videoUri);
        }
        // accept delta about < 1000 milliseconds. (ex: 10769 is still accepted
        // if limit is 10000)
        if (mMediaOptions.getMaxVideoDuration() != Integer.MAX_VALUE
                && duration >= mMediaOptions.getMaxVideoDuration() + 1000) {
            return 0;
        } else if (duration == 0
                || duration < mMediaOptions.getMinVideoDuration()) {
            return -1;
        }
        return 1;
    }

    private void returnVideo(Uri videoUri) {
        final int code = checkValidVideo(videoUri);
        switch (code) {
            // not found. should never happen. Do nothing when happen.
            case -2:

                break;
            // smaller than min
            case -1:
                // in seconds
                int duration = mMediaOptions.getMinVideoDuration() / 1000;
                String msg = MessageUtils.getInvalidMessageMinVideoDuration(
                        getApplicationContext(), duration);
                showVideoInvalid(msg);
                break;

            // larger than max
            case 0:
                // in seconds.
                duration = mMediaOptions.getMaxVideoDuration() / 1000;
                msg = MessageUtils.getInvalidMessageMaxVideoDuration(
                        getApplicationContext(), duration);
                showVideoInvalid(msg);
                break;
            // ok
            case 1:
                MediaItem item = new MediaItem(MediaItem.VIDEO, videoUri);
                ArrayList<MediaItem> list = new ArrayList<MediaItem>();
                list.add(item);
                returnBackData(list);
                break;

            default:
                break;
        }
    }

    private void showVideoInvalid(String msg) {
        MediaPickerErrorDialog errorDialog = MediaPickerErrorDialog
                .newInstance(msg);
        errorDialog.show(getSupportFragmentManager(), null);
    }

    /**
     * Get media item list selected from intent extra included in
     * {@link Activity#onActivityResult(int, int, Intent)} of activity or fragment
     * that open media picker.
     *
     * @param intent In {@link Activity#onActivityResult(int, int, Intent)} method of
     *               activity or fragment that open media picker.
     * @return Always return {@link ArrayList} of {@link MediaItem}. You must
     * always check null and size of this list before handle your logic.
     */
    public static ArrayList<MediaItem> getMediaItemSelected(Intent intent) {
        if (intent == null)
            return null;
        ArrayList<MediaItem> mediaItemList = intent
                .getParcelableArrayListExtra(MediaPickerActivity.EXTRA_MEDIA_SELECTED);
        return mediaItemList;
    }

    @Override
    public void onClick(View view) {
        int i = view.getId();
        if (i == R.id.title_left) {
            finish();
        } else if (i == R.id.right_btn1) {
            Fragment activePage = getActivePage();
            if (mMediaOptions.canSelectPhotoAndVideo()
                    && activePage instanceof MediaPickerFragment) {
                MediaPickerFragment mediaPickerFragment = ((MediaPickerFragment) activePage);
                mediaPickerFragment.switchMediaSelector();
                syncIconMenu(mediaPickerFragment.getMediaType());
            }
        } else if (i == R.id.right_btn) {
            //完成按钮
            Fragment activePage;
            activePage = getActivePage();
            boolean isPhoto = ((MediaPickerFragment) activePage)
                    .getMediaType() == MediaItem.PHOTO;
            if (isPhoto) {
                if (mMediaOptions.isCropped()
                        && !mMediaOptions.canSelectMultiPhoto()) {
                    // get first item in list (pos=0) because can only crop 1 image at same time.
                } else {
                    returnBackData(((MediaPickerFragment) activePage)
                            .getMediaSelectedList());
                }
            } else {
                if (mMediaOptions.canSelectMultiVideo()) {
                    returnBackData(((MediaPickerFragment) activePage)
                            .getMediaSelectedList());
                } else {
                    // only get 1st item regardless of have many.
                    returnVideo(((MediaPickerFragment) activePage)
                            .getMediaSelectedList().get(0).getUriOrigin());
                }
            }
        }else if (i==R.id.tv_mediapicker_preview){
            //预览按钮
            Fragment activePage;
            activePage = getActivePage();
            ArrayList<String> selectPathList = new ArrayList<String>();
            List<MediaItem> mediaItemList=((MediaPickerFragment) activePage)
                    .getMediaSelectedList();
            if (mediaItemList != null){
                for (int j= 0; j<mediaItemList.size(); j++){
                    selectPathList.add(mediaItemList.get(j).getPathOrigin(this).toString());
                    Log.i("doctorlog", "地址="+mediaItemList.get(j).getPathOrigin(this).toString());
                }
            }
            if(selectPathList==null||selectPathList.size()<1){
                ToastUtil.show(this,"你还没勾选图片", Toast.LENGTH_SHORT);
                return;
            }
            PreviewedImageInfo previewedImageInfo = new PreviewedImageInfo();
            previewedImageInfo.setImgUrls(selectPathList);
            // 设置点击图片所在集合中的位置
            previewedImageInfo.setPosition(0);
            // 默认显示的图片类型
            previewedImageInfo.setDefaultImgRes(R.mipmap.empty_photo);
            // 开启预览界面
            LoaderImageUtil.previewLargePic(previewedImageInfo,this);
        }
    }

    private class FileObserverTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {
            if (isCancelled()) return null;
            if (mFileObserver == null) {
                mFileObserver = new RecursiveFileObserver(Environment
                        .getExternalStorageDirectory().getAbsolutePath(),
                        FileObserver.CREATE);
                mFileObserver
                        .setFileCreatedListener(mOnFileCreatedListener);
            }
            mFileObserver.startWatching();
            return null;
        }
    }

    private void cancelFileObserverTask() {
        if (mFileObserverTask != null) {
            mFileObserverTask.cancel(true);
            mFileObserver = null;
        }
    }

    private void stopWatchingFile() {
        if (mFileObserver != null) {
            mFileObserver.stopWatching();
            mFileObserver = null;
        }
    }
}