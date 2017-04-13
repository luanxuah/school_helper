package com.luanxu.adapter.community;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.luanxu.activity.community.LostAndFoundSendActivity;
import com.luanxu.bean.PreviewedImageInfo;
import com.luanxu.custom.album.SelectPhotoAlbumUtils;
import com.luanxu.schoolhelper.R;
import com.luanxu.utils.CommonUtils;
import com.luanxu.utils.LoaderImageUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * @author: LuanXu
 * @createTime:2017/3/16 18:35 
 * @className:  SchoolmateCircleSendAdapter
 * @Description: 发布失物招领的图片加载
 */

public class LostAndFoundSendAdapter extends BaseAdapter{
    //上下文对象
    private LostAndFoundSendActivity context;

    //分享文件本地和网络路径
    private List<String> mImgPathLists;
    // 相片的最大数目
    private static final int MAX_IMG_COUNTS = 9;
    // Gridview当中最多显示的列数
    private static final int GRID_COLUMN_COUNT = 4;

    public LostAndFoundSendAdapter(LostAndFoundSendActivity context, List<String> mImgPathLists){
        this.context = context;
        this.mImgPathLists = mImgPathLists;
    }

    public void setDate(List<String> mImgPathLists){
        this.mImgPathLists = mImgPathLists;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return (mImgPathLists.size() > MAX_IMG_COUNTS ) ? MAX_IMG_COUNTS : mImgPathLists.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ImgsGridHolder holder;
        if (convertView == null) {
            holder = new ImgsGridHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.item_normal_image, null);
            holder.iv_share_img = (ImageView) convertView.findViewById(R.id.iv);
            convertView.setTag(holder);
        }else {
            holder = (ImgsGridHolder) convertView.getTag();
            holder.iv_share_img.setImageResource(R.color.color_white);
        }
        //　动态计算每一个item的大小
        dynamicItemSize(holder.iv_share_img);

        if(mImgPathLists.size() > MAX_IMG_COUNTS) {
            // 显示本地照相或相册的图片
            LoaderImageUtil.displayFromSDCard(mImgPathLists.get(position),R.mipmap.empty_photo,holder.iv_share_img);
        }else {
            if (position != mImgPathLists.size() - 1) {
                //显示我添加的图片
                LoaderImageUtil.displayFromSDCard(mImgPathLists.get(position),R.mipmap.empty_photo,holder.iv_share_img);
                holder.iv_share_img.setBackgroundResource(R.color.color_divider);
            }else {
                //显示加号
                LoaderImageUtil.displayFromDrawable(R.mipmap.ic_tianj,holder.iv_share_img);
                holder.iv_share_img.setBackgroundResource(R.mipmap.ic_tianj);
            }
        }

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 用来展示图片用的集合
                ArrayList<String> imgShowPaths = new ArrayList<String>();
                //进入图片预览所需的实体
                PreviewedImageInfo previewedImageInfo = new PreviewedImageInfo();
                previewedImageInfo.setImgUrls(imgShowPaths);
                previewedImageInfo.setPosition(position);
                previewedImageInfo.setDefaultImgRes(R.mipmap.empty_photo);
//                previewedImageInfo.setCoverPartyId(LoginPreference.getUserInfo().partyId);
                //不显示长按弹窗
                previewedImageInfo.setSourceForPhoto(true);
                if (mImgPathLists.size() > MAX_IMG_COUNTS) {
                    // 此时分享的图片已经有9张预览图片 去除多余的图片
                    mImgPathLists.remove(MAX_IMG_COUNTS);
                    for(int i = 0;i < MAX_IMG_COUNTS;i ++) {
                        imgShowPaths.add(mImgPathLists.get(i));
                    }
                    LoaderImageUtil.previewLargePic(previewedImageInfo,context);
                    // 恢复原来集合的数据
                    mImgPathLists.add(context.mPlusFlag);
                }else {
                    // 含有“+”添加按钮
                    if (position != mImgPathLists.size() - 1) {
                        // 预览图片
                        for(int i = 0;i < mImgPathLists.size() - 1;i ++) {
                            imgShowPaths.add(mImgPathLists.get(i));
                        }
                        LoaderImageUtil.previewLargePic(previewedImageInfo,context);
                    }else {
//                        showCasePopWindow();
                        //选择相册
                        SelectPhotoAlbumUtils.selectPhoto(context, SelectPhotoAlbumUtils.ACTION_SHARE_FROM_ALBUM,MAX_IMG_COUNTS + 1 - mImgPathLists.size());
                    }
                }
            }
        });

        return convertView;
    }

    /**
     * 图片GridView Holder
     */
    public static class ImgsGridHolder {
        public ImageView iv_share_img;
    }

    /**
     * @Description: 根据屏幕宽度重新计算每个item的大小
     * @return: void
     */
    public void dynamicItemSize(ImageView ivView) {
        //屏幕的宽度
        int screenWidth = CommonUtils.getDeviceSize(context).x;
        int itemSpacing = context.getResources().getDimensionPixelSize(R.dimen.yms_dimens_20_0_px);
        int itemSize = (int) ((screenWidth * 0.93 - (GRID_COLUMN_COUNT - 1) * itemSpacing) / GRID_COLUMN_COUNT);

        LinearLayout.LayoutParams rlParams = (LinearLayout.LayoutParams) ivView.getLayoutParams();
        rlParams.width = itemSize;
        rlParams.height = itemSize;
        ivView.setLayoutParams(rlParams);
    }
}
