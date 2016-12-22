package com.luanxu.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.luanxu.schoolhelper.R;

/**
 * @author: LuanXu
 * @createTime:2016/12/20 20:47
 * @className:  EmptyClassroomAdapter
 * @Description: 空教室的适配器
 */

public class EmptyClassroomAdapter extends BaseAdapter {
    private Context context;

    public EmptyClassroomAdapter(Context context){
        this.context = context;
    }
    @Override
    public int getCount() {
        return 10;
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder holder = null;
        if (view==null){
            holder = new ViewHolder();
            view = LayoutInflater.from(context).inflate(R.layout.item_empty_classroom, null);
            holder.tv_classroom = (TextView) view.findViewById(R.id.tv_classroom);
            holder.tv_sort = (TextView) view.findViewById(R.id.tv_sort);
            holder.tv_num = (TextView) view.findViewById(R.id.tv_num);
            view.setTag(holder);
        }else{
            holder = (ViewHolder) view.getTag();
        }
        return view;
    }

    public static class ViewHolder{
        //教室
        TextView tv_classroom;
        //教室类别
        TextView tv_sort;
        //数目
        TextView tv_num;
    }

}
