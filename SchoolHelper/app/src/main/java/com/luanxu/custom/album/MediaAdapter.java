package com.luanxu.custom.album;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.support.v4.widget.CursorAdapter;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.AbsListView.RecyclerListener;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.luanxu.bean.PreviewedImageInfo;
import com.luanxu.schoolhelper.R;
import com.luanxu.utils.LoaderImageUtil;
import com.luanxu.utils.ToastUtil;

import java.util.ArrayList;
import java.util.List;
/**
 * @author TUNGDX
 */

/**
 * Adapter for display media item list.
 */
public class MediaAdapter extends CursorAdapter implements RecyclerListener {
    private String TAG = MediaAdapter.class.getSimpleName();
    private int maxSelectNum;
    private int mMediaType;
    private MediaImageLoader mMediaImageLoader;
    private List<MediaItem> mMediaListSelected = new ArrayList<MediaItem>();
    private MediaOptions mMediaOptions;
    private int mItemHeight = 0;
    private int mNumColumns = 0;
    private RelativeLayout.LayoutParams mImageViewLayoutParams;
    private MediaSelectedListener mMediaSelectedListener;
    private List<PickerImageView> mPickerImageViewSelected = new ArrayList<PickerImageView>();
    private List<CheckBox> mCheckBoxSelected = new ArrayList<CheckBox>();
    private int selectNum = 0;
    private Activity activity;
    private int itemViewType;
    private final int TYPE_CAMERA = 0;
    private final int TYPE_ALBUM = 1;

    public MediaAdapter(Context context, Cursor c, int flags,
                        MediaImageLoader mediaImageLoader, int mediaType, MediaOptions mediaOptions, MediaSelectedListener mMediaSelectedListener, int maxSelectNum) {
        this(context, c, flags, null, mediaImageLoader, mediaType, mediaOptions, mMediaSelectedListener, maxSelectNum);
    }

    public MediaAdapter(Context context, Cursor c, int flags,
                        List<MediaItem> mediaListSelected, MediaImageLoader mediaImageLoader,
                        int mediaType, MediaOptions mediaOptions, MediaSelectedListener mMediaSelectedListener, int maxSelectNum) {
        super(context, c, flags);
        activity = (Activity) context;
        if (mediaListSelected != null)
            mMediaListSelected = mediaListSelected;
        mMediaImageLoader = mediaImageLoader;
        mMediaType = mediaType;
        mMediaOptions = mediaOptions;
        this.mMediaSelectedListener = mMediaSelectedListener;
        this.maxSelectNum = maxSelectNum;
        mImageViewLayoutParams = new RelativeLayout.LayoutParams(
                LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
    }

    @Override
    public int getCount() {
        if (mDataValid && mCursor != null) {
            return mCursor.getCount() + 1;
        } else {
            return 1;
        }
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        itemViewType = getItemViewType(position);
        View v;
        if (position != 0) {
            if (!mDataValid) {
                throw new IllegalStateException("this should only be called when the cursor is valid");
            }
            if (!mCursor.moveToPosition(position - 1)) {
                throw new IllegalStateException("couldn't move cursor to position " + (position - 1));
            }
        } else {
            if (!mDataValid) {
                throw new IllegalStateException("this should only be called when the cursor is valid");
            }
            if (!mCursor.moveToPosition(position)) {
                throw new IllegalStateException("couldn't move cursor to position " + (position));
            }
        }
        if (convertView == null) {
            v = newView(mContext, mCursor, parent);
        } else {
            v = convertView;
        }
        bindView(v, mContext, mCursor);
        return v;
    }

    @Override
    public void bindView(View view, final Context context, final Cursor cursor) {
        if (itemViewType == TYPE_CAMERA) {
            ViewHolderCamera holderCamera = (ViewHolderCamera) view.getTag();
            holderCamera.llCamera.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mMediaType == MediaItem.PHOTO) {
                        MediaPickerActivity.isCamera = true;
                        SelectPhotoAlbumUtils.openCamera(activity, SelectPhotoAlbumUtils.ACTION_SHARE_FROM_CAMERA);
                    } else {
                        Intent i = new Intent(activity, RecordingVideoActivity.class);
                        activity.startActivityForResult(i, MediaPickerActivity.REQUEST_TAKE_VIDEO);
                    }

                }
            });
        } else {
            final ViewHolder holder = (ViewHolder) view.getTag();
            holder.checkBox.setChecked(false);
            final Uri uri;
            if (mMediaType == MediaItem.PHOTO) {
                uri = MediaUtils.getPhotoUri(cursor);
                holder.thumbnail.setVisibility(View.GONE);
            } else {
                uri = MediaUtils.getVideoUri(cursor);
                holder.thumbnail.setVisibility(View.VISIBLE);
            }

            holder.layout_check.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (holder.checkBox.isChecked()) {
                        holder.viewMask.setVisibility(View.GONE);
                        holder.checkBox.setChecked(false);
                    } else {
                        if (selectNum < maxSelectNum) {
                            holder.viewMask.setVisibility(View.VISIBLE);
                            holder.checkBox.setChecked(true);
                        } else {
                            ToastUtil.show(mContext, "最多选择" + maxSelectNum + "个文件", Toast.LENGTH_SHORT);
                            holder.checkBox.setChecked(false);
                            return;
                        }
                    }
                    MediaItem mediaItem = new MediaItem(mMediaType, uri);
                    updateMediaSelected(mediaItem, holder.imageView, holder.checkBox);
                    mMediaListSelected = getMediaSelectedList();
                    if (hasSelected()) {
                        mMediaSelectedListener.onHasSelected(getMediaSelectedList());
                    } else {
                        mMediaSelectedListener.onHasNoSelected();
                    }
                    TextView tv_complete = (TextView) activity.findViewById(R.id.right_btn);
                    TextView tv_preview = (TextView) activity.findViewById(R.id.tv_preview_num);
                    if (selectNum == 0) {
                        tv_preview.setText("预览");
                    }else {
                        tv_preview.setText("预览("+selectNum+")");
                    }
                    tv_complete.setText("完成(" + selectNum + "/" + maxSelectNum + ")");

                }
            });

            holder.imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mMediaType == MediaItem.PHOTO) {
                        //图片
                        PreviewedImageInfo previewedImageInfo = new PreviewedImageInfo();
                        ArrayList<String> imgUrls = new ArrayList<String>();
                        if (!holder.imageView.isSelected()) {
                            MediaItem mediaItem = new MediaItem(mMediaType, uri);
                            imgUrls.add(mediaItem.getPathOrigin(context));
                        }
                        for (int i = 0; i < mMediaListSelected.size(); i++) {
                            imgUrls.add(mMediaListSelected.get(i).getPathOrigin(context));
                        }
                        previewedImageInfo.setImgUrls(imgUrls);
//                        previewedImageInfo.setCoverPartyId(LoginPreference.getUserInfo().getPartyId());
                        previewedImageInfo.setSourceForPhoto(true);
                        LoaderImageUtil.previewLargePic(previewedImageInfo, (Activity) context);
                    } else {//视频

                        MediaItem mediaItem = new MediaItem(mMediaType, uri);
                        String videoPath = mediaItem.getPathOrigin(context);
                        Log.d(TAG, "onClick: =====" + videoPath);
                        LoaderImageUtil.previewVideo(videoPath, mContext);
                    }
                }
            });

            boolean isSelected = isSelected(uri);
            holder.imageView.setSelected(isSelected);
            holder.checkBox.setChecked(isSelected);
            if (isSelected) {
                mPickerImageViewSelected.add(holder.imageView);
                mCheckBoxSelected.add(holder.checkBox);
            }
            mMediaImageLoader.displayImage(uri, holder.imageView);
        }
    }

    @Override
    public int getViewTypeCount() {
        return 2;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return TYPE_CAMERA;
        }
        return TYPE_ALBUM;
    }

    @Override
    public View newView(Context context, final Cursor cursor, ViewGroup viewGroup) {
        View root = null;

        if (itemViewType == TYPE_CAMERA) {
            ViewHolderCamera holderCamera = null;
            if (root == null) {
                holderCamera = new ViewHolderCamera();
                root = View
                        .inflate(mContext, R.layout.list_item_mediapicker_camera, null);
                root.setTag(holderCamera);
            } else {
                holderCamera = (ViewHolderCamera) root.getTag();
            }
            holderCamera.tv_type = (TextView) root.findViewById(R.id.tv_media_piker_type);
            holderCamera.llCamera = (LinearLayout) root.findViewById(R.id.thumbnail_camera);
            if (mMediaType == MediaItem.PHOTO) {
                holderCamera.tv_type.setText("拍摄照片");
                //底部导航栏
                activity.findViewById(R.id.rl_meida_picker_bottom).setVisibility(View.VISIBLE);
            } else {
                holderCamera.tv_type.setText("拍摄视频");
                activity.findViewById(R.id.rl_meida_picker_bottom).setVisibility(View.GONE);
            }
        } else {
            final ViewHolder holder = new ViewHolder();
            root = View
                    .inflate(mContext, R.layout.list_item_mediapicker, null);
            holder.imageView = (PickerImageView) root.findViewById(R.id.thumbnail);
            holder.thumbnail = root.findViewById(R.id.overlay);
            holder.checkBox = (CheckBox) root.findViewById(R.id.checkbox);
            holder.viewMask = root.findViewById(R.id.view_media_adapter_mask);
            holder.viewMask.setVisibility(View.GONE);

            holder.layout_check = (LinearLayout) root.findViewById(R.id.layout_check);
            holder.imageView.setLayoutParams(mImageViewLayoutParams);
            // Check the height matches our calculated column width
            if (holder.imageView.getLayoutParams().height != mItemHeight) {
                holder.imageView.setLayoutParams(mImageViewLayoutParams);
            }
            root.setTag(holder);
        }

        return root;
    }

    private class ViewHolder {
        PickerImageView imageView;
        View thumbnail;
        CheckBox checkBox;
        View viewMask;
        LinearLayout layout_check;
    }

    private class ViewHolderCamera {
        LinearLayout llCamera;
        TextView tv_type;
    }

    public boolean hasSelected() {
        return mMediaListSelected.size() > 0;
    }

    /**
     * Check media uri is selected or not.
     *
     * @param uri Uri of media item (photo, video)
     * @return true if selected, false otherwise.
     */
    public boolean isSelected(Uri uri) {
        if (uri == null)
            return false;
        for (MediaItem item : mMediaListSelected) {
            if (item.getUriOrigin().equals(uri))
                return true;
        }
        return false;
    }

    /**
     * Check {@link MediaItem} is selected or not.
     *
     * @param item {@link MediaItem} to check.
     * @return true if selected, false otherwise.
     */
    public boolean isSelected(MediaItem item) {
        return mMediaListSelected.contains(item);
    }

    /**
     * Set {@link MediaItem} selected.
     *
     * @param item {@link MediaItem} to selected.
     */
    public void setMediaSelected(MediaItem item) {
        syncMediaSelectedAsOptions();
        if (!mMediaListSelected.contains(item))
            mMediaListSelected.add(item);
    }

    /**
     * If item selected then change to unselected and unselected to selected.
     *
     * @param item Item to update.
     */
    public void updateMediaSelected(MediaItem item,
                                    PickerImageView pickerImageView, CheckBox checkBox) {
        if (mMediaListSelected.contains(item)) {
            mMediaListSelected.remove(item);
            pickerImageView.setSelected(false);
            checkBox.setChecked(false);
            this.mPickerImageViewSelected.remove(pickerImageView);
            this.mCheckBoxSelected.remove(checkBox);
            selectNum--;
        } else {
            boolean value = syncMediaSelectedAsOptions();
            if (value) {
                for (PickerImageView picker : this.mPickerImageViewSelected) {
                    picker.setSelected(false);
                }
                for (CheckBox cb : this.mCheckBoxSelected) {
                    cb.setChecked(false);
                }
                this.mPickerImageViewSelected.clear();
                this.mCheckBoxSelected.clear();
            }
            checkBox.setChecked(true);
            mMediaListSelected.add(item);
            pickerImageView.setSelected(true);
            this.mPickerImageViewSelected.add(pickerImageView);
            this.mCheckBoxSelected.add(checkBox);
            selectNum++;
        }
    }

    /**
     * @return List of {@link MediaItem} selected.
     */
    public List<MediaItem> getMediaSelectedList() {
        return mMediaListSelected;
    }

    /**
     * Set list of {@link MediaItem} selected.
     *
     * @param list
     */
    public void setMediaSelectedList(List<MediaItem> list) {
        mMediaListSelected = list;
    }

    /**
     * Whether clear or not media selected as options.
     *
     * @return true if clear, false otherwise.
     */
    private boolean syncMediaSelectedAsOptions() {
        switch (mMediaType) {
            case MediaItem.PHOTO:
                if (!mMediaOptions.canSelectMultiPhoto()) {
                    mMediaListSelected.clear();
                    return true;
                }
                break;
            case MediaItem.VIDEO:
                if (!mMediaOptions.canSelectMultiVideo()) {
                    mMediaListSelected.clear();
                    return true;
                }

                break;
            default:
                break;
        }
        return false;
    }

    /**
     * {@link MediaItem#VIDEO} or {@link MediaItem#PHOTO}
     *
     * @param mediaType
     */
    public void setMediaType(int mediaType) {
        mMediaType = mediaType;
    }

    // set numcols
    public void setNumColumns(int numColumns) {
        mNumColumns = numColumns;
    }

    public int getNumColumns() {
        return mNumColumns;
    }

    // set photo item height
    public void setItemHeight(int height) {
        if (height == mItemHeight) {
            return;
        }
        mItemHeight = height;
        mImageViewLayoutParams.height = height;
        mImageViewLayoutParams.width = height;
        notifyDataSetChanged();
    }

    @Override
    public void onMovedToScrapHeap(View view) {
        PickerImageView imageView = (PickerImageView) view
                .findViewById(R.id.thumbnail);
        CheckBox cb = (CheckBox) view.findViewById(R.id.checkbox);
        mPickerImageViewSelected.remove(imageView);
        mCheckBoxSelected.remove(cb);
    }

    public void onDestroyView() {
        mPickerImageViewSelected.clear();
        mCheckBoxSelected.clear();
    }
}