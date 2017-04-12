package com.luanxu.custom.album;

import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.media.MediaPlayer;
import android.net.Uri;
import android.provider.MediaStore.Images;
import android.provider.MediaStore.MediaColumns;
import android.provider.MediaStore.Video;

import java.io.File;

/**
 * @author TUNGDX
 */

/**
 * Utility for Media Picker module.
 */
public class MediaUtils {
    public static final String[] PROJECT_PHOTO = {MediaColumns._ID};
    public static final String[] PROJECT_VIDEO = {MediaColumns._ID};

    public static Uri getPhotoUri(Cursor cursor) {
        return getMediaUri(cursor, Images.Media.EXTERNAL_CONTENT_URI);
    }

    public static Uri getVideoUri(Cursor cursor) {
        return getMediaUri(cursor, Video.Media.EXTERNAL_CONTENT_URI);
    }

    public static Uri getMediaUri(Cursor cursor, Uri uri) {
        String id = cursor.getString(cursor.getColumnIndex(MediaColumns._ID));
        return Uri.withAppendedPath(uri, id);
    }

    public static String getFileExtension(File file) {
        String name = file.getName();
        int lastIndexOf = name.lastIndexOf(".");
        if (lastIndexOf == -1) {
            return ""; // empty extension
        }
        return name.substring(lastIndexOf);
    }

    public static boolean isImageExtension(String extension) {
        String[] valid = {".jpg", ".jpeg"};
        for (String ex : valid) {
            if (extension.equalsIgnoreCase(ex)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Get path of image from uri
     *
     * @param contentResolver
     * @param contentURI
     * @return path of image. Null if not found.
     */
    public static String getRealImagePathFromURI(ContentResolver contentResolver,
                                                 Uri contentURI) {
        Cursor cursor = contentResolver.query(contentURI, null, null, null,
                null);
        if (cursor == null)
            return contentURI.getPath();
        else {
            cursor.moveToFirst();
            int idx = cursor
                    .getColumnIndex(Images.ImageColumns.DATA);
            try {
                return cursor.getString(idx);
            } catch (Exception exception) {
                return null;
            }
        }
    }

    /**
     * Get path of video from uri
     *
     * @param contentResolver
     * @param contentURI
     * @return path of video. Null if not found.
     */
    public static String getRealVideoPathFromURI(ContentResolver contentResolver,
                                                 Uri contentURI) {
        Cursor cursor = contentResolver.query(contentURI, null, null, null,
                null);
        if (cursor == null)
            return contentURI.getPath();
        else {
            cursor.moveToFirst();
            int idx = cursor.getColumnIndex(Video.VideoColumns.DATA);
            try {
                return cursor.getString(idx);
            } catch (Exception exception) {
                return null;
            }
        }
    }

    /**
     * Add file photo to gallery after capture from camera or downloaded.
     *
     * @param context
     * @param file
     */
    public static void galleryAddPic(Context context, File file) {
        Intent mediaScanIntent = new Intent(
                Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        Uri contentUri = Uri.fromFile(file);
        mediaScanIntent.setData(contentUri);
        context.sendBroadcast(mediaScanIntent);
    }

    /**
     * Get video's duration without {@link ContentProvider}. Because not know
     * {@link Uri} of video.
     *
     * @param context
     * @param path    Path of video file.
     * @return Duration of video, in milliseconds. Return 0 if path is null.
     */
    public static long getDuration(Context context, String path) {
        MediaPlayer mMediaPlayer = null;
        long duration = 0;
        try {
            mMediaPlayer = new MediaPlayer();
            mMediaPlayer.setDataSource(context, Uri.parse(path));
            mMediaPlayer.prepare();
            duration = mMediaPlayer.getDuration();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (mMediaPlayer != null) {
                mMediaPlayer.reset();
                mMediaPlayer.release();
                mMediaPlayer = null;
            }
        }
        return duration;
    }

    /**
     * Get video's duration from {@link ContentProvider}
     *
     * @param context
     * @param uri     must has {@link Uri#getScheme()} equals
     *                {@link ContentResolver#SCHEME_CONTENT}
     * @return Duration of video, in milliseconds.
     */
    public static long getDuration(Context context, Uri uri) {
        long duration = 0L;
        Cursor cursor = Video.query(context.getContentResolver(),
                uri, new String[]{Video.VideoColumns.DURATION});
        if (cursor != null) {
            cursor.moveToFirst();
            duration = cursor.getLong(cursor
                    .getColumnIndex(Video.VideoColumns.DURATION));
            cursor.close();
        }
        return duration;
    }
}