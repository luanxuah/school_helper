package com.luanxu.adapter.community;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.luanxu.adapter.ImageAdapter;
import com.luanxu.custom.MyGridView;
import com.luanxu.schoolhelper.R;

/**
 * @author: LuanXu
 * @createTime:2017/3/31 14:29
 * @className:  LostAdapter
 * @Description: 失物的适配器
 */

public class LostAdapter extends BaseAdapter{
    //上下文对象
    private Activity context;

    private boolean isLost;
    private int images[] = {R.mipmap.test, R.mipmap.test, R.mipmap.test};

    private ViewHolder viewHolder;

    public LostAdapter(Activity context, boolean isLost){
        this.context = context;
        this.isLost = isLost;
    }

    @Override
    public int getCount() {
        return 5;
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
        viewHolder = null;
        if (contentView == null){
            viewHolder = new ViewHolder();
            contentView = LayoutInflater.from(context).inflate(R.layout.item_lost, null);
            //头像
            viewHolder.iv_head = (ImageView) contentView.findViewById(R.id.iv_head);
            //姓名
            viewHolder.tv_name = (TextView) contentView.findViewById(R.id.tv_name);
            //发送时间
            viewHolder.tv_send_time = (TextView) contentView.findViewById(R.id.tv_send_time);
            //学院
            viewHolder.tv_college = (TextView) contentView.findViewById(R.id.tv_college);
            //地点
            viewHolder.tv_place = (TextView) contentView.findViewById(R.id.tv_place);
            //地点的头部
            viewHolder.tv_place_title = (TextView) contentView.findViewById(R.id.tv_place_title);
            //时间
            viewHolder.tv_time = (TextView) contentView.findViewById(R.id.tv_time);
            //时间的头部
            viewHolder.tv_time_title = (TextView) contentView.findViewById(R.id.tv_time_title);
            //物品
            viewHolder.tv_article = (TextView) contentView.findViewById(R.id.tv_article);
            //物品的头部
            viewHolder.tv_article_title = (TextView) contentView.findViewById(R.id.tv_article_title);
            //图片
            viewHolder.grid_view = (MyGridView) contentView.findViewById(R.id.grid_view);
            //消息外部布局
            viewHolder.ll_message = (LinearLayout) contentView.findViewById(R.id.ll_message);
            //消息数目
            viewHolder.iv_message = (TextView) contentView.findViewById(R.id.iv_message);
            //隔线
            viewHolder.view = contentView.findViewById(R.id.view);
            //关闭外部布局
            viewHolder.ll_close = (LinearLayout) contentView.findViewById(R.id.ll_close);
            //关闭数目
            viewHolder.iv_close = (TextView) contentView.findViewById(R.id.iv_close);
            contentView.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder) contentView.getTag();
        }

        if (isLost){
            viewHolder.tv_place.setText("拾取地点：花津校区2030104");
            viewHolder.tv_place_title.setText("拾取地点：");
            viewHolder.tv_time.setText("拾取时间：2017年3月30");
            viewHolder.tv_time_title.setText("拾取时间：");
            viewHolder.tv_article.setText("拾取物品：钥匙");
            viewHolder.tv_article_title.setText("拾取物品：");
            viewHolder.ll_message.setVisibility(View.VISIBLE);
            viewHolder.view.setVisibility(View.GONE);
            viewHolder.ll_close.setVisibility(View.GONE);
        }else{
            viewHolder.tv_place.setText("丢失地点：花津校区2030104");
            viewHolder.tv_place_title.setText("丢失地点：");
            viewHolder.tv_time.setText("丢失时间：2017年3月30");
            viewHolder.tv_time_title.setText("丢失时间：");
            viewHolder.tv_article.setText("丢失物品：钥匙");
            viewHolder.tv_article_title.setText("丢失物品：");
            viewHolder.ll_message.setVisibility(View.VISIBLE);
            viewHolder.view.setVisibility(View.VISIBLE);
            viewHolder.ll_close.setVisibility(View.VISIBLE);
        }

        viewHolder.grid_view.setVisibility(View.VISIBLE);
        ImageAdapter adapter = new ImageAdapter(context, viewHolder.grid_view, images, 0.94, 3, false);
        viewHolder.grid_view.setAdapter(adapter);

        return contentView;
    }

    static class ViewHolder{
        //头像
        ImageView iv_head;
        //姓名
        TextView tv_name;
        //发送时间
        TextView tv_send_time;
        //学院
        TextView tv_college;
        //地点
        TextView tv_place;
        //地点的头部
        TextView tv_place_title;
        //时间
        TextView tv_time;
        //时间的头部
        TextView tv_time_title;
        //物品
        TextView tv_article;
        //物品的头部
        TextView tv_article_title;
        //图片
        MyGridView grid_view;
        //消息外部布局
        LinearLayout ll_message;
        //消息数目
        TextView iv_message;
        //隔线
        View view;
        //关闭外部布局
        LinearLayout ll_close;
        //关闭数目
        TextView iv_close;
    }
}
