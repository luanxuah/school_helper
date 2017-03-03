package com.luanxu.activity.community;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.res.AssetFileDescriptor;
import android.graphics.Bitmap;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.text.TextUtils;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.Result;
import com.luanxu.application.SchoolHelperApplication;
import com.luanxu.base.BaseActivity;
import com.luanxu.custom.CommonDialog;
import com.luanxu.custom.TitleBar;
import com.luanxu.custom.zxing.camera.CameraManager;
import com.luanxu.custom.zxing.decoding.CaptureActivityHandler;
import com.luanxu.custom.zxing.decoding.InactivityTimer;
import com.luanxu.custom.zxing.view.ViewfinderView;
import com.luanxu.schoolhelper.R;
import com.luanxu.utils.LogUtil;
import com.luanxu.utils.ResourceUtil;
import com.luanxu.utils.ToastUtil;
import java.io.IOException;
import java.util.Vector;

/**
 * @author: Guol
 * @ClassName: MipcaActivityCapture
 * @date: 2016-3-9 下午3:22:57
 * @Description: 二维码扫描
 */
public class MipcaActivityCapture extends BaseActivity implements Callback {
    private BaseActivity mContext;
    private CaptureActivityHandler handler;
    private ViewfinderView viewfinderView;
    private boolean hasSurface;
    private Vector<BarcodeFormat> decodeFormats;
    private String characterSet;
    private InactivityTimer inactivityTimer;
    private MediaPlayer mediaPlayer;
    private boolean playBeep;
    private static final float BEEP_VOLUME = 0.10f;
    private boolean vibrate;
    private String content;

    @SuppressLint("NewApi")
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_capture);
        mContext = this;
        SchoolHelperApplication.getInstance().addActivity(this);
        TitleBar bar = getTitleBar();
        bar.setBack();
        bar.setTitle("扫一扫", R.color.color_white);

        // 是否加的那句提示
        content = "正在扫描";
        Log.i("提示语为：  ", content);

        TextView tvContext = (TextView) findViewById(R.id.tv_context);
        if (!TextUtils.isEmpty(content)) {
            tvContext.setVisibility(View.VISIBLE);
            tvContext.setText(content);
        } else {
            tvContext.setVisibility(View.GONE);
        }

        CameraManager.init(getApplication());
        viewfinderView = (ViewfinderView) findViewById(R.id.viewfinder_view);
        hasSurface = false;
        inactivityTimer = new InactivityTimer(this);
    }

    @SuppressWarnings("deprecation")
    @Override
    protected void onResume() {
        super.onResume();
        SurfaceView surfaceView = (SurfaceView) findViewById(R.id.preview_view);
        SurfaceHolder surfaceHolder = surfaceView.getHolder();
        if (hasSurface) {
            initCamera(surfaceHolder);
        } else {
            surfaceHolder.addCallback(this);
            surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        }
        decodeFormats = null;
        characterSet = null;

        playBeep = true;
        AudioManager audioService = (AudioManager) getSystemService(AUDIO_SERVICE);
        if (audioService.getRingerMode() != AudioManager.RINGER_MODE_NORMAL) {
            playBeep = false;
        }
        initBeepSound();
        vibrate = true;
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (handler != null) {
            handler.quitSynchronously();
            handler = null;
        }
        CameraManager.get().closeDriver();
    }

    @Override
    protected void onDestroy() {
        inactivityTimer.shutdown();
        super.onDestroy();
    }

    public void handleDecode(Result result, Bitmap barcode) {
        inactivityTimer.onActivity();
        LogUtil.i("doctorlog   扫描结果为："+result.toString());
        ToastUtil.show(this, result.toString(), Toast.LENGTH_SHORT);

        playBeepSoundAndVibrate();
        String resultString = result.getText();
        if (resultString.equals("")) {
            continuePreview();
            Toast.makeText(MipcaActivityCapture.this, "扫描失败", Toast.LENGTH_SHORT).show();
        } else {

        }
    }

    // 扫描失败可以继续扫描
    private void continuePreview() {
        SurfaceView surfaceView = (SurfaceView) findViewById(R.id.preview_view);
        SurfaceHolder surfaceHolder = surfaceView.getHolder();
        initCamera(surfaceHolder);
        if (handler != null) {
            handler.postDelayed(new Runnable() {

                @Override
                public void run() {
                    handler.restartPreviewAndDecode();
                }
            }, 1000);// 延迟3秒 post
        }
    }

    private void initCamera(SurfaceHolder surfaceHolder) {
        try {
            CameraManager.get().openDriver(surfaceHolder);
        } catch (IOException ioe) {
            showPromptDialog();
            return;
        } catch (RuntimeException e) {
            showPromptDialog();
            return;
        }
        if (handler == null) {
            handler = new CaptureActivityHandler(this, decodeFormats,
                    characterSet);
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width,
                               int height) {
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        if (!hasSurface) {
            hasSurface = true;
            initCamera(holder);
        }
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        hasSurface = false;
    }

    public ViewfinderView getViewfinderView() {
        return viewfinderView;
    }

    public Handler getHandler() {
        return handler;
    }

    public void drawViewfinder() {
        viewfinderView.drawViewfinder();

    }

    private void initBeepSound() {
        if (playBeep && mediaPlayer == null) {
            // The volume on STREAM_SYSTEM is not adjustable, and users found it
            // too loud,
            // so we now play on the music stream.
            setVolumeControlStream(AudioManager.STREAM_MUSIC);
            mediaPlayer = new MediaPlayer();
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mediaPlayer.setOnCompletionListener(beepListener);

            AssetFileDescriptor file = getResources().openRawResourceFd(
                    R.raw.beep);
            try {
                mediaPlayer.setDataSource(file.getFileDescriptor(),
                        file.getStartOffset(), file.getLength());
                file.close();
                mediaPlayer.setVolume(BEEP_VOLUME, BEEP_VOLUME);
                mediaPlayer.prepare();
            } catch (IOException e) {
                mediaPlayer = null;
            }
        }
    }

    private static final long VIBRATE_DURATION = 200L;

    private void playBeepSoundAndVibrate() {
        if (playBeep && mediaPlayer != null) {
            mediaPlayer.start();
        }
        if (vibrate) {
            Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
            vibrator.vibrate(VIBRATE_DURATION);
        }
    }

    /**
     * When the beep has finished playing, rewind to queue up another one.
     */
    private final OnCompletionListener beepListener = new OnCompletionListener() {
        public void onCompletion(MediaPlayer mediaPlayer) {
            mediaPlayer.seekTo(0);
        }
    };

    /**
     * 当相机权限未打开时显示提示注意框
     */
    public void showPromptDialog() {
        SurfaceView surfaceView = (SurfaceView) findViewById(R.id.preview_view);
        surfaceView.setBackgroundResource(R.color.color_black);
        final CommonDialog dialog = new CommonDialog(mContext, CommonDialog.WEIXIN_STYLE);
        dialog.setTitle(ResourceUtil.getString(mContext, R.string.str_open_camera_power));
        dialog.setMessage(ResourceUtil.getString(mContext, R.string.str_open_camera_power_primpt));
        dialog.setPositiveButton(new CommonDialog.BtnClickedListener() {
            @Override
            public void onBtnClicked() {
                dialog.dismiss();
                mContext.setResult(Activity.RESULT_CANCELED);
                mContext.finish();
            }
        }, "知道了");
        dialog.showDialog();
    }

}