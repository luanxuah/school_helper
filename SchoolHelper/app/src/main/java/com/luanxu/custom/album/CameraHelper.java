package com.luanxu.custom.album;

import android.app.Activity;
import android.hardware.Camera;
import android.hardware.Camera.CameraInfo;
import android.os.Build;
import android.view.Surface;

import com.luanxu.utils.LogUtil;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class CameraHelper {

	private static final String TAG = "CameraHelper";
	/** 应用支持的标准的视频预览宽度 */
	public static final int STANDARD_PREVIEW_WIDTH = 352;
	/** 应用支持的标准的视频预览高度 */
	public static final int STANDARD_PREVIEW_HEIGHT = 288;
	/** camera可用 */
	public static final int CAMERA_ENABLED_MASK = 0x0011;
	/** 前置camera可用 */
	public static final int CAMERA_FRONT_ENABLED = 0x0001;
	/** 后置camera可用 */
	public static final int CAMERA_BACK_ENABLED = 0x0010;
	/** camera不可用 */
	public static final int CAMERA_DISABLED = 0x0;

	/**
	 * 部分手机摄像头所支持的previewsize虽然包含352*288，但设为352*288却不能正常预览和发送视频信息，
	 * 因此需要特别列出来，以让此类手机的特定摄像头不能使用
	 */
	public static final Map<String, Integer> UNSUPPORTED_PHONE_MAP = new HashMap<String, Integer>();
	static {
		// 三星GT-I9508，前置摄像头正常支持，后置摄像头视频图像显示彩条
		UNSUPPORTED_PHONE_MAP.put("GT-I9508", CAMERA_BACK_ENABLED);
	}

	/**
	 * 部分手机摄像头所支持的previewsize虽然不包含352*288，但设为352*288却能正常预览和发送视频信息，
	 * 因此需要特别列出来，以让此类手机的特定摄像头能使用
	 */
	public static final Map<String, Integer> SUPPORTED_PHONE_MAP = new HashMap<String, Integer>();
	static { // 三星GT-I9508，前置摄像头正常支持，后置摄像头视频图像显示彩条
		SUPPORTED_PHONE_MAP.put("GT-I9508", CAMERA_FRONT_ENABLED);
	}

	/**
	 * 前置摄像头竖屏拍摄时，MediaRecorder需要setOrientationHint为270度，
	 * 部分手机比较特殊，只需要设置90度，否则图像就倒过来了， 因此需要特别列出来，以便做排除
	 */
	public static final Map<String, Integer> FRONT_CAMERA_HINT_DEGREE_MAP = new HashMap<String, Integer>();
	static { // MX2
		FRONT_CAMERA_HINT_DEGREE_MAP.put("M040", 90);
	}

	/** 初始化摄像头信息 */
	public static int initCameraInfo() {
		int cameraResult = CAMERA_DISABLED;
		Camera camera = null;
		CameraInfo cameraInfo = new CameraInfo();
		// 特定手机不支持前置或后置摄像头
		Integer cameraSupport = UNSUPPORTED_PHONE_MAP.get(Build.MODEL.toUpperCase(Locale.ENGLISH));
		boolean isSupportFront = true;
		boolean isSupportBack = true;
		if (cameraSupport != null) {
			if ((cameraSupport & CAMERA_FRONT_ENABLED) == CAMERA_FRONT_ENABLED) {
				isSupportFront = false;
			}
			if ((cameraSupport & CAMERA_BACK_ENABLED) == CAMERA_BACK_ENABLED) {
				isSupportBack = false;
			}
		}

		// 特定手机强制支持前置或后置摄像头
		cameraSupport = SUPPORTED_PHONE_MAP.get(Build.MODEL.toUpperCase(Locale.ENGLISH));
		if (cameraSupport != null) {
			if (isSupportFront && (cameraSupport & CAMERA_FRONT_ENABLED) == CAMERA_FRONT_ENABLED) {
				isSupportFront = false;
				cameraResult = cameraResult | CAMERA_FRONT_ENABLED;
			}
			if (isSupportBack && (cameraSupport & CAMERA_BACK_ENABLED) == CAMERA_BACK_ENABLED) {
				isSupportBack = false;
				cameraResult = cameraResult | CAMERA_BACK_ENABLED;
			}
		}

		try { // 得到摄像头的个数
			int cameraCount = Camera.getNumberOfCameras();

			for (int i = 0; i < cameraCount; i++) {
				Camera.getCameraInfo(i, cameraInfo);
				if (isSupportFront && cameraInfo.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
					// 前置摄像头
					if (isCameraEnabled(camera, i)) { // 前置摄像头支持标准分辨率
						cameraResult = cameraResult | CAMERA_FRONT_ENABLED;
					}
				} else if (isSupportBack && cameraInfo.facing == CameraInfo.CAMERA_FACING_BACK) {
					// 后置摄像头
					if (isCameraEnabled(camera, i)) {
						// 后置摄像头支持标准分辨率
						cameraResult = cameraResult | CAMERA_BACK_ENABLED;
					}
				}
			}
		} catch (Exception e) {
			LogUtil.e(e);
		} finally {
			if (camera != null) {
				camera.release();
				camera = null;
			}
		}

		return cameraResult;
	}

	/** 指定摄像头是否可用，即摄像头previewsize是否支持标准分辨率 */
	private static boolean isCameraEnabled(Camera camera, int cameraIdx) throws InterruptedException {
		boolean enabled = false;

		int retryCnt = 3;
		while (retryCnt > 0) {
			try {
				camera = Camera.open(cameraIdx);
				break;
			} catch (Exception e) {
				LogUtil.e(TAG + " isCameraEnabled 打开摄像头失败，可能由于摄像头打开过于频繁，隔4s再次尝试", e);
				camera = null;
				retryCnt--;
				if (retryCnt > 0) { // 隔4s再次尝试
					Thread.sleep(4000);
					continue;
				} else {
					break;
				}
			}
		}

		if (camera == null) {
			return enabled;
		}

		try {
			Camera.Parameters parameters = camera.getParameters();
			parameters.setPreviewSize(STANDARD_PREVIEW_WIDTH, STANDARD_PREVIEW_HEIGHT);
			camera.setParameters(parameters);

			// 判断设置的previewsize是否有效
			parameters = camera.getParameters();
			Camera.Size size = parameters.getPreviewSize();
			if (size.width == STANDARD_PREVIEW_WIDTH && size.height == STANDARD_PREVIEW_HEIGHT) {
				// 设置成功，表示摄像头支持标准分辨率
				enabled = true;
			}
		} catch (Exception e) {
			LogUtil.e(e);
			enabled = false;
		}

		if (camera != null) {
			camera.release();
			camera = null;
		}

		return enabled;
	}

	/***
	 * 获得可使用（可支持352*288视频或SUPPORTED_PHONE_MAP中明确支持的）的摄像头数目
	 *
	 * @param mask
	 *            保存在本地的已知的摄像头
	 * @return 0：不支持视频通话，1：不可切换前后摄像头，2：可切换前后摄像头
	 */
	public static int getNumberOfEnableCameras(String mask) {

		int value = 0;
		try {
			value = Integer.parseInt(mask);
			if (CAMERA_ENABLED_MASK == value) {
				return 2;
			}
			if (CAMERA_FRONT_ENABLED == value) {
				return 1;
			}
			if (CAMERA_BACK_ENABLED == value) {
				return 1;
			}
		} catch (NumberFormatException e) {
			e.printStackTrace();
		}
		return 0;
	}

	/** activity中，camera显示需要旋转的角度 */
	public static void setCameraDisplayOrientation(Activity activity, int cameraId, Camera camera) {
		Camera.CameraInfo info = new Camera.CameraInfo();
		Camera.getCameraInfo(cameraId, info);
		int degrees = getDisplayRotation(activity);
		int result;
		if (info.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
			result = (info.orientation + degrees) % 360;
			result = (360 - result) % 360; // compensate the mirror
		} else { // back-facing
			result = (info.orientation - degrees + 360) % 360;
		}
		LogUtil.d(TAG, "setCameraDisplayOrientation:" + result);
		camera.setDisplayOrientation(result);
	}

	/** activity显示的角度 */
	public static int getDisplayRotation(Activity activity) {
		int rotation = activity.getWindowManager().getDefaultDisplay().getRotation();
		switch (rotation) {
		case Surface.ROTATION_0:
			return 0;
		case Surface.ROTATION_90:
			return 90;
		case Surface.ROTATION_180:
			return 180;
		case Surface.ROTATION_270:
			return 270;
		}
		return 0;
	}
}
