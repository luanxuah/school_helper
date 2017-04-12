package com.luanxu.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.TextUtils;
import android.view.ContextMenu;
import android.view.View;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager.LayoutParams;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.luanxu.base.BaseActivity;
import com.luanxu.bean.PreviewedImageInfo;
import com.luanxu.custom.CircleDot;
import com.luanxu.custom.PreViewPager;
import com.luanxu.schoolhelper.R;
import com.luanxu.utils.ExternalStorageUtil;
import com.luanxu.utils.LoaderImageUtil;
import com.luanxu.utils.LogUtil;
import com.luanxu.utils.NetWorkUtil;
import com.luanxu.utils.ToastUtil;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author: 范建海
 * @createTime: 2016/8/18 14:10
 * @className:  ActPreViewIcon
 * @Description: 图片预览
 */
public class ActPreViewIcon extends BaseActivity {
	/**预览图片 类型*/
	public static int IMAGE_PREVIEW =100101;
	/** 当前预览图片的相关信息 **/
	private PreviewedImageInfo previewedImageInfo;
	/** 自定义ViewPager **/
	private PreViewPager mViewPager;
	/** 小圆点 **/
	private CircleDot cirDot;
	/** viewPager适配器 **/
	private SamplePagerAdapter mAdapter;
	/** 上下文 **/
	private Activity activity;
	/** 加载大图的进度条 **/
	private ProgressBar pbLoding;

	private Map<Integer, String> localImagePath;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.act_preview_icon);

		Window window = getWindow();
		LayoutParams windowLayoutParams = window.getAttributes(); // 获取对话框当前的参数值
		// dimAmount在0.0f和1.0f之间，0.0f完全不暗，1.0f全暗
		windowLayoutParams.dimAmount = 1.0f;
		window.setAttributes(windowLayoutParams);

		pbLoding = (ProgressBar) findViewById(R.id.loading);
		activity = ActPreViewIcon.this;
		previewedImageInfo = (PreviewedImageInfo)getIntent().getSerializableExtra(LoaderImageUtil.TAG_START_PREVIEW);

		if (previewedImageInfo != null) {
			initViewPager();
		}
	}


	private void initViewPager() {
		localImagePath = new HashMap<Integer, String>();
		cirDot = new CircleDot(activity, (LinearLayout)findViewById(R.id.ltAddDot),previewedImageInfo.getImgUrls().size(),previewedImageInfo.getPosition());
		cirDot.selected(previewedImageInfo.getPosition());
		mViewPager = (PreViewPager) findViewById(R.id.vpIcon);
		mAdapter = new SamplePagerAdapter();
		mAdapter.setItems(previewedImageInfo.getImgUrls());
		mViewPager.setAdapter(mAdapter);
		mViewPager.setCurrentItem(previewedImageInfo.getPosition(), true);

		mViewPager.setOnPageChangeListener(new OnPageChangeListener() {

			@Override
			public void onPageSelected(int arg0) {
				if (cirDot != null) {
					cirDot.selected(arg0);
				}
				mViewPager.setCurrentItem(arg0, true);
			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {}

			@Override
			public void onPageScrollStateChanged(int arg0) {}

		});
	}



	public class SamplePagerAdapter extends PagerAdapter {
		/** 图片路径 **/
		private List<String> imgPaths;
		/** 自定义ImageView **/
		private ImageView photoView;

		public void setItems(List<String> paths) {
			imgPaths = paths;
		}
		@Override
		public int getCount() {
			return (imgPaths == null) ? 0 : imgPaths.size();
		}

		@Override
		public View instantiateItem(ViewGroup container, final int position) {

			photoView = new ImageView(container.getContext());

			if (imgPaths != null && imgPaths.size() > 0) {
				if (!TextUtils.isEmpty(imgPaths.get(position)) && !imgPaths.get(position).startsWith(ExternalStorageUtil.getExternalStoragePath())) {
					if (NetWorkUtil.isNetworkConnected(activity)) {
						downLoadMessage(previewedImageInfo.getImgUrls().get(position), position);
						// 加载原图
						LoaderImageUtil.display(previewedImageInfo.getImgUrls().get(position), previewedImageInfo.getDefaultImgRes(),photoView, new ImageLoadingListener() {
							@Override
							public void onLoadingStarted(String s, View view) {
								pbLoding.setVisibility(View.VISIBLE);
							}

							@Override
							public void onLoadingFailed(String s, View view, FailReason failReason) {
								pbLoding.setVisibility(View.GONE);
							}

							@Override
							public void onLoadingComplete(String s, View view, Bitmap bitmap) {
								pbLoding.setVisibility(View.GONE);
								// 给控件设置标志，用于缓存文件用
								photoView.setTag(previewedImageInfo.getImgUrls().get(position).substring(previewedImageInfo.getImgUrls().get(position).lastIndexOf("/") + 1));
							}

							@Override
							public void onLoadingCancelled(String s, View view) {
								pbLoding.setVisibility(View.GONE);

							}
						});
					}else {
						ToastUtil.show(activity,"请查看网络连接", Toast.LENGTH_SHORT);
					}

				}else {
					// 展示本地原图
					LoaderImageUtil.displayFromSDCard(previewedImageInfo.getImgUrls().get(position),previewedImageInfo.getDefaultImgRes(), photoView);
					localImagePath.put(position, previewedImageInfo.getImgUrls().get(position));
				}

				container.addView(photoView, 0, new LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT));

				// 长按转发
				photoView.setOnLongClickListener(new OnLongClickListener() {
					@Override
					public boolean onLongClick(View v) {
						if (!TextUtils.isEmpty(previewedImageInfo.getImgUrls().get(position)) && !previewedImageInfo.isSourceForPhoto()){
							Intent intent = new Intent(activity, ContextMenu.class);
							intent.putExtra("type",IMAGE_PREVIEW );
							intent.putExtra("path", previewedImageInfo.getImgUrls().get(position));
							intent.putExtra("localPath", localImagePath.get(position));
							LogUtil.i("doctorlog   previewImag="+previewedImageInfo.getImgUrls().get(position));
							if (!TextUtils.isEmpty(previewedImageInfo.getCoverPartyId())){
								intent.putExtra("coverPartyId", previewedImageInfo.getCoverPartyId());
							}
							startActivity(intent);
						}
						return true;
					}
				});
			}

			return photoView;
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			container.removeView((View) object);
		}

		@Override
		public boolean isViewFromObject(View view, Object object) {
			return view == object;
		}
	}

	/**
	 * 下载图片至本地
	 * @param path 图片地址
	 * @param position 图片所在位置
     */
	public void downLoadMessage(String path, final int position){
//		String partDirectory = path.substring(0, path.lastIndexOf("/")+1);
//		OssUtil.asyncGetObjectRequest(null, activity, partDirectory, path, new OssCallback() {
//			@Override
//			public void success(ProgressDialog progressDialog, String objectKey, String filePath) {
//				localImagePath.put(position, filePath);
//			}
//
//		});
	}
}
