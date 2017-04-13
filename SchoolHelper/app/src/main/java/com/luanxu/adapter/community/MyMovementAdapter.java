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
 * @createTime:2017/4/13 17:39
 * @className:  MyMovementAdapter
 * @Description: 我的消息列表
 */

public class MyMovementAdapter extends BaseAdapter{
    //上下文对象
    private Context context;

    public MyMovementAdapter(Context conext){
        this.context = conext;
    }

    @Override
    public int getCount() {
        return 7;
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
        ViewHolder viewHolder = null;
        if (contentView == null){
            viewHolder = new ViewHolder();
            contentView = LayoutInflater.from(context).inflate(R.layout.item_my_movement, null);
            viewHolder.iv = (ImageView) contentView.findViewById(R.id.iv);
            viewHolder.tv_title = (TextView) contentView.findViewById(R.id.tv_title);
            viewHolder.tv_time = (TextView) contentView.findViewById(R.id.tv_time);
            viewHolder.tv_credit = (TextView) contentView.findViewById(R.id.tv_credit);
            contentView.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder) contentView.getTag();
        }

        return contentView;
    }

    static class ViewHolder{
        //活动图片
        ImageView iv;
        //标题
        TextView tv_title;
        //时间
        TextView tv_time;
        //我的学分
        TextView tv_credit;
    }
}
