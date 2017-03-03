package com.luanxu.adapter.message;

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
 * @createTime:2017/3/2 15:53
 * @className:  MessageItemAdapter
 * @Description: 资讯页面viewPager的item列表适配器
 */

public class MessageItemAdapter extends BaseAdapter{
    //上下文对象
    private Context context;

    private int TYPE_ONE = 0;
    private int TYPE_TWO = 1;
    //大图片页面格式
    private ViewHolderOne viewHolderOne;
    //小图片页面格式
    private ViewHolderTwo viewHolderTwo;

    public MessageItemAdapter(Context context){
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
    public int getItemViewType(int position) {
        return position%3==0 ? TYPE_ONE:TYPE_TWO;
    }

    @Override
    public int getViewTypeCount() {
        return 2;
    }

    @Override
    public View getView(int position, View contentView, ViewGroup viewGroup) {
        int type = getItemViewType(position);
        if (type == TYPE_ONE){
            //大图片页面效果
            viewHolderOne = null;
            if (contentView == null){
                viewHolderOne = new ViewHolderOne();
                contentView = LayoutInflater.from(context).inflate(R.layout.item_message_one, null);
                viewHolderOne.tv_title = (TextView) contentView.findViewById(R.id.tv_title);
                viewHolderOne.tv_time = (TextView) contentView.findViewById(R.id.tv_time);
                viewHolderOne.tv_read_num = (TextView) contentView.findViewById(R.id.tv_read_num);
                viewHolderOne.image = (ImageView) contentView.findViewById(R.id.image);
                viewHolderOne.tv_content = (TextView) contentView.findViewById(R.id.tv_content);
                contentView.setTag(viewHolderOne);
            }else{
                viewHolderOne = (ViewHolderOne) contentView.getTag();
            }
        }else if (type == TYPE_TWO){
            //小图片页面效果
            viewHolderTwo = null;
            if (contentView == null){
                viewHolderTwo = new ViewHolderTwo();
                contentView = LayoutInflater.from(context).inflate(R.layout.item_message_two, null);
                viewHolderTwo.tv_title = (TextView) contentView.findViewById(R.id.tv_title);
                viewHolderTwo.tv_time = (TextView) contentView.findViewById(R.id.tv_time);
                viewHolderTwo.tv_read_num = (TextView) contentView.findViewById(R.id.tv_read_num);
                viewHolderTwo.image = (ImageView) contentView.findViewById(R.id.image);
                contentView.setTag(viewHolderTwo);
            }else{
                viewHolderTwo = (ViewHolderTwo) contentView.getTag();
            }
        }
        return contentView;
    }

    /**
     * 第一种效果的viewHolder
     */
    static class ViewHolderOne{
        //标题
        TextView tv_title;
        //发布时间
        TextView tv_time;
        //阅读量
        TextView tv_read_num;
        //图片
        ImageView image;
        //内容
        TextView tv_content;
    }

    /**
     * 第二种效果的viewHolder
     */
    static class ViewHolderTwo{
        //标题
        TextView tv_title;
        //发布时间
        TextView tv_time;
        //阅读量
        TextView tv_read_num;
        //图片
        ImageView image;
    }
}
