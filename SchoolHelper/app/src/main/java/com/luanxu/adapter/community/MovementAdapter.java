package com.luanxu.adapter.community;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.luanxu.schoolhelper.R;

/**
 * @author: LuanXu
 * @createTime:2017/1/3 19:48
 * @className:  MovementAdapter
 * @Description: 校内活动的适配器
 */

public class MovementAdapter extends BaseAdapter{

    private Context context;

    public MovementAdapter(Context context){
        this.context = context;
    }

    @Override
    public int getCount() {
        return 10;
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
    public View getView(int position, View contentView, ViewGroup viewGroup) {
        ViewHolder holder = null;
        if (contentView == null){
            holder = new ViewHolder();
            contentView = LayoutInflater.from(context).inflate(R.layout.item_movement, null);
            holder.tv_address = (TextView) contentView.findViewById(R.id.tv_address);
            holder.iv = (ImageView) contentView.findViewById(R.id.iv);
            holder.tv_title = (TextView) contentView.findViewById(R.id.tv_title);
            holder.tv_starttime = (TextView) contentView.findViewById(R.id.tv_starttime);
            holder.tv_endtime = (TextView) contentView.findViewById(R.id.tv_endtime);
            holder.tv_type = (TextView) contentView.findViewById(R.id.tv_type);
            contentView.setTag(holder);
        }else{
            holder = (ViewHolder) contentView.getTag();
        }

        return contentView;
    }

    static class ViewHolder{
        //活动地址
        public TextView tv_address;
        //活动图片
        public ImageView iv;
        //活动标题
        public TextView tv_title;
        //活动开始时间
        public TextView tv_starttime;
        //活动结束时间
        public TextView tv_endtime;
        //学分类型
        public TextView tv_type;
    }
}
