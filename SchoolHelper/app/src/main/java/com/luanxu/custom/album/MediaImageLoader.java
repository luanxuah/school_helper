package com.luanxu.custom.album;

import android.net.Uri;
import android.widget.ImageView;

/**
 * @author TUNGDX
 */
public interface MediaImageLoader {
    void displayImage(Uri uri, ImageView imageView);
}
