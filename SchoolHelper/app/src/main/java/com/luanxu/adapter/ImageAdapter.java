package com.luanxu.adapter;

import android.app.Activity;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.luanxu.schoolhelper.R;
import com.luanxu.utils.CommonUtils;
import com.luanxu.utils.LoaderImageUtil;

/**
 * @author: LuanXu
 * @createTime:2017/3/16 16:39
 * @className:  ImageAdapter
 * @Description: 图片适配器
 */

public class ImageAdapter extends BaseAdapter {
    Activity context;
    int channelWidth = 0;
    int mImageThumbSpacing;
    int pic[];

    boolean isRound;

    /**
     * @param context 上下文
     * @param gv 列表控件
     * @param pic 图片的地址的数组
     * @param percent gridView所占屏幕宽度的百分比
     */
    public ImageAdapter(Activity context, GridView gv, int pic[], double percent, int widthNum, boolean isRound) {
        super();
        this.context = context;
        this.pic = pic;
        this.isRound = isRound;

        mImageThumbSpacing = (int) context.getResources().getDimensionPixelSize(R.dimen.yms_dimens_10_0_px);
        channelWidth = ((int)(CommonUtils.getDeviceSize(context).x * percent) - mImageThumbSpacing * (widthNum-1)) / widthNum;
        gv.setNumColumns(widthNum);
        LinearLayout.LayoutParams imgGridLp = (LinearLayout.LayoutParams) gv.getLayoutParams();
        gv.setColumnWidth(channelWidth);
        imgGridLp.width = (int) (CommonUtils.getDeviceSize(context).x * percent);
        imgGridLp.gravity = Gravity.CENTER_VERTICAL | Gravity.START;
        gv.setLayoutParams(imgGridLp);
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return pic == null ? 0 : pic.length;
    }

    @Override
    public Object getItem(int position) {
        return pic == null ? 0 : pic[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View view, ViewGroup parent) {

        ViewHolder holder = null;
        if (view == null) {
            holder = new ViewHolder();
            if (isRound){
                view = LayoutInflater.from(context).inflate(R.layout.item_round_image, parent, false);
                holder.tvChannel = (ImageView) view.findViewById(R.id.round_img);
            }else{
                view = LayoutInflater.from(context).inflate(R.layout.item_normal_image, parent, false);
                holder.tvChannel = (ImageView) view.findViewById(R.id.iv);
            }
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        LinearLayout.LayoutParams magazine = (LinearLayout.LayoutParams) holder.tvChannel.getLayoutParams();
        magazine.width = channelWidth;
        magazine.height = channelWidth;
        holder.tvChannel.setLayoutParams(magazine);
        final ImageView iv = holder.tvChannel;
        int url = pic[position];
        holder.tvChannel.setImageResource(R.mipmap.empty_photo);
//        LoaderImageUtil.displayFromNet(url,R.mipmap.empty_photo,iv);
        LoaderImageUtil.displayFromDrawable(url,iv);
        if (!isRound){
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // 头像集合
//                    ArrayList<String> imgs = new ArrayList<String>();
//                    imgs.add(pic[position]);
//                    // 开启预览界面
//                    LoaderImage.previewLargePic(new PreviewedImageInfo(imgs,0,R.drawable.empty_photo,LoaderImage.TYPE_IMG_250PX_SIZE, LoginPreference.getUserInfo().partyId), context);
                }
            });
        }
        return view;
    }

    class ViewHolder {
        ImageView tvChannel;
    }
}
