package com.luanxu.adapter.community;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.luanxu.schoolhelper.R;
import com.luanxu.utils.CommonUtils;
import com.luanxu.utils.LoaderImageUtil;

import java.util.List;

/**
 * @author: LuanXu
 * @createTime:2017/3/16 18:35 
 * @className:  SchoolmateCircleSendAdapter
 * @Description: 发布校友圈的图片加载
 */

public class SchoolmateCircleSendAdapter extends BaseAdapter{
    //上下文对象
    private Context context;

    //分享文件本地和网络路径
    private List<String> mImgPathLists;
    // 相片的最大数目
    private static final int MAX_IMG_COUNTS = 9;
    // Gridview当中最多显示的列数
    private static final int GRID_COLUMN_COUNT = 4;

    public SchoolmateCircleSendAdapter(Context context, List<String> mImgPathLists){
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
    public View getView(int position, View convertView, ViewGroup parent) {
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
