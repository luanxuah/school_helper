package com.luanxu.adapter.community;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.luanxu.adapter.ImageAdapter;
import com.luanxu.custom.CircleImageView;
import com.luanxu.schoolhelper.R;

/**
 * @author: LuanXu
 * @createTime:2017/3/6 14:47
 * @className:  SchoolmateCircleAdapter
 * @Description: 校友圈的适配器
 */

public class ConfessionWallAdapter extends BaseAdapter{
    //上下文对象
    private Activity context;

    //  头布局类型
    private static final int ITEM_HEAD = 0;
    //  消息布局类型
    private static final int ITEM_MSG= 1;

    private ViewHolder holder;
    private int images[] = {R.mipmap.test, R.mipmap.test, R.mipmap.test};

    public ConfessionWallAdapter(Activity context){
        this.context = context;
    }

    @Override
    public int getCount() {
        return 6;
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
    public int getViewTypeCount() {
        return 2;
    }

    @Override
    public int getItemViewType(int position) {
        return position==0 ? ITEM_HEAD:ITEM_MSG;
    }

    @Override
    public View getView(int position, View contentView, ViewGroup viewGroup) {
        int type = getItemViewType(position);
        if (type == ITEM_HEAD){
            contentView = LayoutInflater.from(context).inflate(R.layout.item_confess_wall_head, null);
            CircleImageView civ_gold = (CircleImageView) contentView.findViewById(R.id.civ_gold);
            CircleImageView civ_silver = (CircleImageView) contentView.findViewById(R.id.civ_silver);
            CircleImageView civ_bronze = (CircleImageView) contentView.findViewById(R.id.civ_bronze);
        }else if (type == ITEM_MSG){
            holder = null;
            if(contentView == null){
                holder = new ViewHolder();
                contentView = LayoutInflater.from(context).inflate(R.layout.item_schoolmate_circle, null);
                holder.iv_medal = (ImageView) contentView.findViewById(R.id.iv_medal);
                holder.iv_head = (ImageView) contentView.findViewById(R.id.iv_head);
                holder.tv_name = (TextView) contentView.findViewById(R.id.tv_name);
                holder.tv_time = (TextView) contentView.findViewById(R.id.tv_time);
                holder.tv_college = (TextView) contentView.findViewById(R.id.tv_college);
                holder.tv_content = (TextView) contentView.findViewById(R.id.tv_content);
                holder.grid_view = (GridView) contentView.findViewById(R.id.grid_view);
                holder.iv_collect = (ImageView) contentView.findViewById(R.id.iv_collect);
                holder.iv_flowers = (ImageView) contentView.findViewById(R.id.iv_flowers);
                holder.iv_praise = (ImageView) contentView.findViewById(R.id.iv_praise);
                holder.iv_comment = (ImageView) contentView.findViewById(R.id.iv_comment);
                contentView.setTag(holder);
            }else{
                holder = (ViewHolder) contentView.getTag();
            }
            if (position == 1){
                holder.iv_medal.setVisibility(View.VISIBLE);
                holder.iv_medal.setImageResource(R.mipmap.ic_gold_medal);
            }else if (position == 2){
                holder.iv_medal.setVisibility(View.VISIBLE);
                holder.iv_medal.setImageResource(R.mipmap.ic_silver_medal);
            }else if (position == 3){
                holder.iv_medal.setVisibility(View.VISIBLE);
                holder.iv_medal.setImageResource(R.mipmap.ic_bronze_medal);
            }else{
                holder.iv_medal.setVisibility(View.GONE);
            }
            holder.grid_view.setVisibility(View.VISIBLE);
            ImageAdapter adapter = new ImageAdapter(context, holder.grid_view, images, 0.94, 3, false);
            holder.grid_view.setAdapter(adapter);
        }

        return contentView;
    }

    static class ViewHolder{
        //头衔
        ImageView iv_medal;
        //头像
        ImageView iv_head;
        //姓名
        TextView tv_name;
        //时间
        TextView tv_time;
        //学院
        TextView tv_college;
        //内容
        TextView tv_content;
        //图片
        GridView grid_view;
        //收藏按钮
        ImageView iv_collect;
        //献鲜花按钮
        ImageView iv_flowers;
        //点赞按钮
        ImageView iv_praise;
        //评论按钮
        ImageView iv_comment;
    }
}
