package com.luanxu.custom.album;

import android.content.Context;
import android.net.Uri;
import android.widget.ImageView;

import com.luanxu.schoolhelper.R;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.imageaware.ImageAware;
import com.nostra13.universalimageloader.core.imageaware.ImageViewAware;
/**
 * @author TUNGDX
 */

public class MediaImageLoaderImpl implements MediaImageLoader {

    public MediaImageLoaderImpl(Context context) {
        ImageLoaderConfiguration imageLoaderConfig = new ImageLoaderConfiguration.Builder(context)
                .threadPriority(Thread.NORM_PRIORITY - 2)
                .denyCacheImageMultipleSizesInMemory()
                .diskCacheFileNameGenerator(new Md5FileNameGenerator())
                .memoryCacheSizePercentage(30)
                .tasksProcessingOrder(QueueProcessingType.FIFO)
                .writeDebugLogs().threadPoolSize(3)
                .build();

        ImageLoader.getInstance().init(imageLoaderConfig);
    }

    @Override
    public void displayImage(Uri uri, ImageView imageView) {
        DisplayImageOptions displayImageOptions = new DisplayImageOptions.Builder()
                .cacheInMemory(true).cacheOnDisk(true)
                .showImageOnLoading(R.color.color_picker_imageloading)
                .cacheOnDisk(false)
                .considerExifParams(true).resetViewBeforeLoading(true).build();

        ImageAware imageAware = new ImageViewAware(imageView, false);
        ImageLoader.getInstance().displayImage(uri.toString(), imageAware, displayImageOptions);
    }
}