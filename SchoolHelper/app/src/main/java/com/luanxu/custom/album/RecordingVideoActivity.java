package com.luanxu.custom.album;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.ImageFormat;
import android.hardware.Camera;
import android.hardware.Camera.Size;
import android.media.CamcorderProfile;
import android.media.MediaRecorder;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.luanxu.base.BaseActivity;
import com.luanxu.schoolhelper.R;
import com.luanxu.utils.CommonConstant;
import com.luanxu.utils.CommonUtils;
import com.luanxu.utils.DateUtils;
import com.luanxu.utils.ScreenUtil;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * @author: BaiCQ
 * @ClassName: RecordingVideoActivity
 * @date: 2016年3月10日 上午9:45:06
 * @Description: 录制视频
 * @Beautify: BaiCQ 代码格式化,消除警告
 */
public class RecordingVideoActivity extends BaseActivity implements
        OnClickListener, SurfaceHolder.Callback {
    //视频文件路径返回码
    public static final String KEY_VIDEO_FILE_PATH = "key_video_file_path";
    //视频文件时长返回码
    public static final String KEY_VIDEO_FILE_DURATION = "key_video_file_duration";
    // 默认视频宽度
    private static final int DEFAULT_VIDEO_WIDTH = 1080;
    // 默认视频高度
    private static final int DEFAULT_VIDEO_HEIGHT = 1920;
    // 视频可录制最大时长：秒
    private static final int VIDEO_MAX_DURATION = 180;
    // 录制倒计时：秒
    private static final int REC_COUNTDOWN = 10;
    // 摄像头类型：参考Camera.CameraInfo.CAMERA_FACING_BACK，Camera.CameraInfo.CAMERA_FACING_FRONT
    private int cameraType = -1;
    // 视频宽度
    private int videoWidth = DEFAULT_VIDEO_WIDTH;
    // 视频高度
    private int videoHeight = DEFAULT_VIDEO_HEIGHT;
    //
    private boolean isRecording = false;
    // record time handler
    private Handler recordTimeHandler;
    // 已录制时长
    private int recorderedDuration = -1;

    // 录制视频的类
    private MediaRecorder mRecorder;
    // Camera
    private Camera mCamera;
    // 录制的视频文件
    private File recordedFile = null;

    // 已录制时长
    private TextView recordedTimeTv;
    // 剩余录制时长
    private TextView restTimeTv;
    // 转换摄像头按钮
    private Button changeCameraBtn;
    // 拍摄按钮
    private Button recordBtn;
    //拍摄时间外部的布局
    private LinearLayout ll_recorded_time;
    // 显示视频的控件
    private SurfaceView surfaceView;

    @SuppressWarnings("deprecation")
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 拍摄视频时，窗口一直是高亮显示
        this.getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON | WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED);

        setContentView(R.layout.recording_video);

        init();
    }

    /**
     *初始化数据和控件
     */
    private void init(){
        recordTimeHandler = new Handler();
        recordBtn = (Button) findViewById(R.id.record_btn);
        recordBtn.setOnClickListener(this);
        changeCameraBtn = (Button) findViewById(R.id.change_camera);
        changeCameraBtn.setOnClickListener(this);
        recordedTimeTv = (TextView) findViewById(R.id.recorded_time);
        restTimeTv = (TextView) findViewById(R.id.rest_time);
        ll_recorded_time = (LinearLayout) findViewById(R.id.ll_recorded_time);
        surfaceView = (SurfaceView) this.findViewById(R.id.surface_view);
        // 设置该组件让屏幕不会自动关闭
        surfaceView.getHolder().setKeepScreenOn(true);
        surfaceView.getHolder().setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        surfaceView.getHolder().addCallback(this);
    }

    @Override
    protected void onPause() {
        completeRecording(-1);
        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        if (mRecorder != null) {
            mRecorder.release();
            mRecorder = null;
        }
        if (mCamera != null) {
            mCamera.stopPreview();
            mCamera.release();
            mCamera = null;
            cameraType = -1;
        }
        super.onDestroy();
    }

    private void completeRecording(int onSuccess) {
        if (isRecording) {
            mRecorder.stop();
            mRecorder.release();
            recordTimeHandler.removeCallbacks(timeRun);
            isRecording = false;

            CommonUtils.scanFileAsync(RecordingVideoActivity.this,
                    recordedFile.getAbsolutePath());

            if (onSuccess == 0) {
                // 跳转到拍摄结果界面
                Intent i = new Intent();
                i.putExtra(KEY_VIDEO_FILE_PATH,recordedFile.getAbsolutePath());
                i.putExtra(KEY_VIDEO_FILE_DURATION, recorderedDuration);
                setResult(Activity.RESULT_OK, i);
                finish();
            } else if (onSuccess == -1) {
                // 结束录制，可再次开始录制
                recordedTimeTv.setVisibility(View.GONE);
                ll_recorded_time.setVisibility(View.GONE);
                restTimeTv.setVisibility(View.GONE);
                changeCameraBtn.setVisibility(View.VISIBLE);
                recordBtn.setBackgroundResource(R.mipmap.rec_start);
            } else if (onSuccess == -2) {
                // 录制失败，结束录制
                Toast.makeText(RecordingVideoActivity.this, "拍摄视频失败",
                        Toast.LENGTH_SHORT).show();
                finish();
            }
            recorderedDuration = -1;
        }
    }
    
    /**
     * 录制过程中,时间变化
     */
    private Runnable timeRun = new Runnable() {

        @Override
        public void run() {
            recorderedDuration++;
            String time = DateUtils.formatDate2String(new Date(recorderedDuration*1000), DateUtils.FORMAT_MM_SS);
            recordedTimeTv.setText(time + "");

            if (recorderedDuration > VIDEO_MAX_DURATION - REC_COUNTDOWN) {
                restTimeTv.setVisibility(View.VISIBLE);
                restTimeTv.setText("还可以拍摄 "
                        + (VIDEO_MAX_DURATION - recorderedDuration) + " 秒");
            }
            if (recorderedDuration >= VIDEO_MAX_DURATION) {
                // 结束录制
                completeRecording(0);

                if (mCamera != null) {
                    mCamera.lock();
                    mCamera.stopPreview();
                    mCamera.release();
                    mCamera = null;
                    cameraType = -1;
                }
            } else {
                recordTimeHandler.postDelayed(timeRun, 1000);
            }
        }
    };

    @SuppressLint("DefaultLocale")
	@Override
    public void onClick(View v) {
        if (CommonUtils.isFastDoubleClick()) {
            return;
        }
        switch (v.getId()) {
        case R.id.record_btn:
            // 录制视频
            if (!isRecording) {
                try {
                    if (!CommonUtils.checkExternalStorage(
                            RecordingVideoActivity.this, true)) {
                        return;
                    }
                    if (mCamera == null) {
                        return;
                    }
                    File videoFolder = new File(CommonConstant.VIDEO_PATH);
                    if (!videoFolder.exists()) {
                        boolean success = videoFolder.mkdirs();
                        if (!success) {
                            Toast.makeText(RecordingVideoActivity.this,
                                    "视频文件目录创建失败", Toast.LENGTH_SHORT).show();
                            return;
                        }
                    }

                    changeCameraBtn.setVisibility(View.GONE);
                    recordBtn.setBackgroundResource(R.mipmap.rec_stop);
                    // 关闭预览
                    mCamera.stopPreview();
                    // 获取当前时间,作为视频文件的文件名
                    String nowTime = DateUtils.formatDate2String(new Date(),DateUtils.FORMAT_YYYYMMDDHHMMSSSSS);
                    // 声明视频文件对象
                    recordedFile = new File(videoFolder, nowTime + ".mp4");
                    // 创建此视频文件
                    recordedFile.createNewFile();
                    mRecorder = new MediaRecorder();
                    Camera.Parameters parameters = mCamera.getParameters();
                    parameters.set("orientation", "portrait");
                    if (cameraType == Camera.CameraInfo.CAMERA_FACING_BACK) {
                        // 设置保存的视频文件竖向播放
                        mRecorder.setOrientationHint(90);
                    } else if (cameraType == Camera.CameraInfo.CAMERA_FACING_FRONT) {
                        // 设置保存的视频文件竖向播放
                        Integer degree = CameraHelper.FRONT_CAMERA_HINT_DEGREE_MAP.get(Build.MODEL.toUpperCase(Locale.ENGLISH));
                        if (degree != null) {
                            mRecorder.setOrientationHint(degree);
                        } else {
                            mRecorder.setOrientationHint(270);
                        }
                    }
                    mCamera.setParameters(parameters);
                    mCamera.unlock();
                    mRecorder.setCamera(mCamera);
//                    // 视频源
//                    mRecorder.setVideoSource(MediaRecorder.VideoSource.CAMERA);
//                    // 录音源为麦克风
//                    mRecorder.setAudioSource(MediaRecorder.AudioSource.CAMCORDER);
//                    // 输出格式为mp4
//                    mRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
//                    // 视频编码
//                    /**
//                     * 1、H264的编码OPENCORE和最新的stagefright都是支持的，不过都是BASE PROFILE的，
//                     *  因为更高的profile编码是要专利费的，所以也就没有实现了
//                     *
//                     * 2、由于264的算法更加复杂，程序实现烦琐，运行它需要更多的处理器和内存资源。
//                     * 在相同的系统下，可能可以跑起四路MPEG4，两路263，却不一定跑得起一路264（当然跟这个程序的效率有关）。
//                     * 因此，运行264对系统要求是比较高的。
//                     * 其次，由于264的实现更加灵活，它把一些实现留给了厂商自己去实现，
//                     * 虽然这样给实现带来了很多好处，但是不同产品之间互通成了很大的问题，
//                     * 造成了通过A公司的编码器编出的数据，必须通过A公司的解码器去解这样尴尬的事情
//                     *
//                     * MX2使用H263编码，导致录制的视频花屏，故此处使用264
//                     */
//                    mRecorder.setVideoEncoder(MediaRecorder.VideoEncoder.H264);
//                    // 视频尺寸
//                    mRecorder.setVideoSize(videoWidth, videoHeight);
//                    // 视频帧频率
//                    mRecorder.setVideoFrameRate(30);
//                    // 视频编码的比特率，值越大，视频越清晰，不设置的话，视频会很模糊
//                    mRecorder.setVideoEncodingBitRate(500000);
//                    // 音频编码
//                    mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);
//                    // 设置profile了，就不需要设置setOutputFormat，setVideoSize，setVideoEncoder，setVideoEncoder等
//                    // 但由于此处需要指定videosize，故不能使用setProfile
////                  mRecorder.setProfile(CamcorderProfile.get(CamcorderProfile.QUALITY_CIF));
//                    mRecorder.setMaxDuration(30000);
//                    mRecorder.setOutputFile(recordedFile.getAbsolutePath());
//                    // 预览
//                    mRecorder.setPreviewDisplay(surfaceView.getHolder().getSurface());

                    mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
                    mRecorder.setVideoSource(MediaRecorder.VideoSource.CAMERA);
                    mRecorder.setProfile(CamcorderProfile.get(CamcorderProfile.QUALITY_720P));
                    mRecorder.setPreviewDisplay(surfaceView.getHolder().getSurface());
                    mRecorder.setOutputFile(recordedFile.getAbsolutePath());
                    // 准备录像
                    mRecorder.prepare();
                    // 开始录像
                    mRecorder.start();
                    // 设置文本框可见
                    recordedTimeTv.setVisibility(View.VISIBLE);
                    ll_recorded_time.setVisibility(View.VISIBLE);
                    // 调用Runable
                    recordTimeHandler.post(timeRun);
                    // 改变录制状态为正在录制
                    isRecording = true;
                } catch (IOException e) {
                    recordFailure();
                } catch (IllegalStateException e) {
                    recordFailure();
                } catch (Exception e) {
                    recordFailure();
                }
            } else {
                // 停止录制视频
                completeRecording(0);

                if (mCamera != null) {
                    mCamera.lock();
                    mCamera.stopPreview();
                    mCamera.release();
                    mCamera = null;
                    cameraType = -1;
                }
            }
            break;
        case R.id.change_camera:
            // 切换摄像头
            if (mCamera == null || cameraType == -1) {
                return;
            } else {
                mCamera.stopPreview();
                mCamera.release();
                mCamera = null;
            }
            try {
                // 开启相机
                Camera.CameraInfo cameraInfo = new Camera.CameraInfo();
                int cameraCount = Camera.getNumberOfCameras();
                for (int i = 0; i < cameraCount; i++) {
                    Camera.getCameraInfo(i, cameraInfo);
                    if (cameraType == Camera.CameraInfo.CAMERA_FACING_BACK
                            && cameraInfo.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
                        // 由后置摄像头转为前置摄像头
                        mCamera = Camera.open(i);
                        if (mCamera != null) {
                            cameraType = Camera.CameraInfo.CAMERA_FACING_FRONT;
                        }
                        break;
                    } else if (cameraType == Camera.CameraInfo.CAMERA_FACING_FRONT
                            && cameraInfo.facing == Camera.CameraInfo.CAMERA_FACING_BACK) {
                        // 由前置摄像头转为后置摄像头
                        mCamera = Camera.open(i);
                        if (mCamera != null) {
                            cameraType = Camera.CameraInfo.CAMERA_FACING_BACK;
                        }
                        break;
                    }
                }
                if (mCamera == null) {
                    mCamera = Camera.open();
                    if (mCamera != null) {
                        cameraType = Camera.CameraInfo.CAMERA_FACING_BACK;
                    }
                }

                startCameraPreview(surfaceView.getHolder());
            } catch (Exception e) {
                if (mCamera != null) {
                    mCamera.release();
                    mCamera = null;
                    cameraType = -1;
                }
                Toast.makeText(RecordingVideoActivity.this, "切换摄像头失败",
                        Toast.LENGTH_SHORT).show();
            }
            break;
        default:
            break;
        }
    }

    private void recordFailure() {
        // 录制失败
        completeRecording(-2);

        if (mCamera != null) {
            mCamera.lock();
            mCamera.stopPreview();
            mCamera.release();
            mCamera = null;
            cameraType = -1;
        }
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        try {
            // 开启相机
            Camera.CameraInfo cameraInfo = new Camera.CameraInfo();
            int cameraCount = Camera.getNumberOfCameras();
            if (cameraCount >= 2) {
                // 2个及以上摄像头时，可切换摄像头
                changeCameraBtn.setVisibility(View.VISIBLE);
            } else {
                changeCameraBtn.setVisibility(View.GONE);
            }
            for (int i = 0; i < cameraCount; i++) {
                Camera.getCameraInfo(i, cameraInfo);
                // 默认打开后置摄像头
                if (cameraInfo.facing == Camera.CameraInfo.CAMERA_FACING_BACK) {
                    mCamera = Camera.open(i);
                    if (mCamera != null) {
                        cameraType = Camera.CameraInfo.CAMERA_FACING_BACK;
                    }
                    break;
                }
            }

            if (mCamera == null) {
                mCamera = Camera.open();
                if (mCamera != null) {
                    cameraType = Camera.CameraInfo.CAMERA_FACING_BACK;
                }
            }

            startCameraPreview(holder);
        } catch (Exception e) {
            if (mCamera != null) {
                mCamera.release();
                mCamera = null;
                cameraType = -1;
            }
            Toast.makeText(RecordingVideoActivity.this, "打开摄像头失败",
                    Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width,
                               int height) {
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        // 关闭预览并释放资源
        if (mCamera != null) {
            mCamera.stopPreview();
            mCamera.release();
            mCamera = null;
            cameraType = -1;
        }
    }

	private void startCameraPreview(SurfaceHolder holder) throws IOException {
        if (mCamera != null) {
            // 由于设置显示方向为竖屏，此处需要设置显示角度
            // 某些手机竖屏下CameraInfo.orientation为0不是90（ZTE-N880E），
            // 因此CameraHelper.setCameraDisplayOrientation方法不会进行旋转90度。
            // 但实际上摄像头确实需要旋转90度，故此处暂固定旋转90度
            mCamera.setDisplayOrientation(90);

            Camera.Parameters parameters = mCamera.getParameters();

            videoWidth = DEFAULT_VIDEO_WIDTH;
            videoHeight = DEFAULT_VIDEO_HEIGHT;

            // 得到一个最接近默认尺寸的尺寸
            Size approachSize = getOptimalPreviewSize(
                    parameters.getSupportedPreviewSizes(), videoWidth,
                    videoHeight);
            if (approachSize.width != videoWidth
                    || approachSize.height != videoHeight) {
                videoWidth = approachSize.width;
                videoHeight = approachSize.height;
            }

            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) surfaceView
                    .getLayoutParams();

            int screenWidth = ScreenUtil.getScreenWidth(RecordingVideoActivity.this);
            int screenHeight = ScreenUtil.getScreenHeight(RecordingVideoActivity.this);

            params.width = screenWidth;
            params.height = screenHeight;
            // 屏幕为竖屏，显示角度旋转了90度，因此此处需要将宽高对调设置
            surfaceView.setLayoutParams(params);

            parameters.setPreviewSize(videoWidth, videoHeight);

            List<String> supportedFocusModes = parameters.getSupportedFocusModes();
            if (supportedFocusModes.contains(Camera.Parameters.FOCUS_MODE_CONTINUOUS_VIDEO)) {
                // 视频拍摄，设置连续自动对焦
                parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_VIDEO);
            } else if (supportedFocusModes.contains(Camera.Parameters.FOCUS_MODE_AUTO)) {
                // 视频拍摄，设置自动对焦
                parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_AUTO);
            }
            supportedFocusModes = null;

            parameters.setPreviewFormat(ImageFormat.NV21);
            parameters.setPreviewFrameRate(30);

            mCamera.setParameters(parameters);
            mCamera.setPreviewDisplay(holder);
            mCamera.startPreview();
        } else {
            Toast.makeText(RecordingVideoActivity.this, "无法使用摄像头",
                    Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * @author: zhaguitao
     * @Title: getOptimalPreviewSize
     * @Description: 获取一个与期望宽高相近的尺寸
     * @param sizes
     * @param w
     * @param h
     * @return
     * @date: 2014-3-25 下午5:31:30
     */
    private Size getOptimalPreviewSize(List<Size> sizes, int w, int h) {
        if (sizes == null)
            return null;

        Size optimalSize = null;
        double minDiff = Double.MAX_VALUE;

        // 由于摄像头旋转了90度，因此此处需要将目标宽高跟支持宽高对调比较
        int targetHeight = w;
        int targetWidth = h;

        // Cannot find the one match the aspect ratio, ignore the requirement
        if (optimalSize == null) {
            minDiff = Double.MAX_VALUE;
            for (Size size : sizes) {
                if (size.width == targetWidth && size.height == targetHeight) {
                    return size;
                } else if (Math.abs(size.width - targetWidth) < minDiff) {
                    optimalSize = size;
                    minDiff = Math.abs(size.width - targetWidth);
                }
            }
        }
        return optimalSize;
    }
}
