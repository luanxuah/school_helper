package com.luanxu.activity.user;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Display;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.zxing.WriterException;
import com.luanxu.base.BaseActivity;
import com.luanxu.custom.TitleBar;
import com.luanxu.custom.zxing.encoding.EncodingHandler;
import com.luanxu.schoolhelper.R;
import com.luanxu.utils.BitmapManager;
import com.luanxu.utils.LoaderImageUtil;
import com.luanxu.utils.ResourceUtil;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * @author: 范建海
 * @createTime: 2016/8/13 15:10
 * @className:  ActMyQr
 * @Description:  生成二维码
 */
public class ActMyQr extends BaseActivity {
	private int height = 0;
	private int heightPadd = 0;

	// 当前登录用户
	private Bitmap qrCodeBitmap;

	// 我的二维码样式
	private ImageView myQr;

	@SuppressWarnings("deprecation")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.pop_my_qr);

		TitleBar bar = getTitleBar();
		bar.setBack();
		bar.setTitle(ResourceUtil.getString(this, R.string.my_qr_code), R.color.color_white);

		Display display = getWindowManager().getDefaultDisplay(); // 为获取屏幕宽、高
		heightPadd = getResources().getDimensionPixelSize(R.dimen.yms_dimens_80_0_px);
		height = (int) (display.getHeight() * 0.60);
		init();
	}
  
	private void init() {
		// 用户名设置
		((TextView)findViewById(R.id.tv_name)).setText("栾煦");
		// 单位设置
		TextView tvTitle = (TextView) findViewById(R.id.tv_title);
		if (!TextUtils.isEmpty("计算机科学与技术")) {
			tvTitle.setText("计算机科学与技术");
		}
		// 生成我的二维码
		myQr = (ImageView) findViewById(R.id.ivMyQr);
		JSONObject item = new JSONObject();
		try {
			item.put("name", "栾煦");
			item.put("partyId", "111111");
			item.put("phone", "18801116048");
		} catch (JSONException e1) {
			e1.printStackTrace();
		}

		String contentString = item.toString();

		try {
			qrCodeBitmap = EncodingHandler.createQRCode(contentString, height - heightPadd);
			// 头像处理
			ImageView myHead = (ImageView) findViewById(R.id.iv_head);
			final Bitmap bitmap = Bitmap.createBitmap(qrCodeBitmap.getWidth() - 20, qrCodeBitmap.getHeight() - 20, qrCodeBitmap.getConfig());
			if (!TextUtils.isEmpty("www.baidu.com")){
				//有用户头像的时候
				LoaderImageUtil.display("www.baidu.com",R.mipmap.ic_heads_doc, myHead, new ImageLoadingListener() {
					@Override
					public void onLoadingStarted(String s, View view) {

					}

					@Override
					public void onLoadingFailed(String s, View view, FailReason failReason) {
						Bitmap logoBmp = BitmapManager.zoomImg(BitmapFactory.decodeResource(getResources(),R.mipmap.ic_heads_doc), 80, 80);

						loadDefaultImg(logoBmp,bitmap);
					}

					@Override
					public void onLoadingComplete(String s, View view, Bitmap logoBmp) {
						if (logoBmp != null) {
							logoBmp = BitmapManager .zoomImg(logoBmp, 80, 80);
						}else {
							logoBmp = BitmapManager.zoomImg(BitmapFactory.decodeResource(getResources(),R.mipmap.ic_heads_doc), 80, 80);
						}
						loadDefaultImg(logoBmp,bitmap);

					}

					@Override
					public void onLoadingCancelled(String s, View view) {

					}
				});
			}else{
				//没有用户头衔的时候，显示默认头像
				Bitmap logoBmp= BitmapFactory.decodeResource(getResources(), R.mipmap.ic_heads_doc);
				loadDefaultImg(logoBmp, bitmap);
			}


		} catch (WriterException e) {
			e.printStackTrace();
		}
	}


	/**
	 * 载入图片
	 * @param logomap 中间目标小图
	 * @param bitmap 外围大图
     */
	public void loadDefaultImg(Bitmap logomap, Bitmap bitmap) {
		Canvas canvas = new Canvas(bitmap);
		canvas.drawBitmap(qrCodeBitmap, 0, 0, null);
		canvas.drawBitmap(logomap,
				qrCodeBitmap.getWidth() / 2 - logomap.getWidth() / 2,
				qrCodeBitmap.getHeight() / 2 - logomap.getHeight() / 2, null);
		myQr.setImageBitmap(bitmap);
	}

}
