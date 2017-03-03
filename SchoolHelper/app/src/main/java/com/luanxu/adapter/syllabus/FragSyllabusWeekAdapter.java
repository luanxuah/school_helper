package com.luanxu.adapter.syllabus;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.luanxu.bean.BottomMenuBean;
import com.luanxu.schoolhelper.R;
import com.luanxu.utils.ResourceUtil;

import java.util.List;

/**
 * @author: LuanXu
 * @createTime:2017/3/3 11:54
 * @className:  FragSyllabusWeekAdapter
 * @Description: 课程表周列表适配器
 */

public class FragSyllabusWeekAdapter extends BaseAdapter{
    //上下文对象
    private Context context;

    //周的集合
    private List<BottomMenuBean> weeks;
    //当前周的位置
    private int newWeekPosition;
    //当前选择的位置
    private int newSelectPostion;

    public FragSyllabusWeekAdapter(Context context, List<BottomMenuBean> weeks, int newWeekPosition, int newSelectPostion){
        this.context = context;
        this.weeks = weeks;
        this.newWeekPosition = newWeekPosition;
        this.newSelectPostion = newSelectPostion;
    }

    public void setDate(int newSelectPostion){
        this.newSelectPostion = newSelectPostion;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return weeks.size();
    }

    @Override
    public Object getItem(int position) {
        return weeks.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null){
            holder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.item_week, null);
            holder.tv_week = (TextView) convertView.findViewById(R.id.tv_week);
            convertView.setTag(holder);
        }else{
            holder = (ViewHolder) convertView.getTag();
        }
        //如果是本周的话，加上本周字样
        if (position == newWeekPosition){
            holder.tv_week.setText(weeks.get(position).content+"（本周）");
        }else{
            holder.tv_week.setText(weeks.get(position).content);
        }
        //如果是选择的周的话，设置为蓝色背景白色字体
        if (position == newSelectPostion){
            holder.tv_week.setBackgroundColor(ResourceUtil.getColor(context, R.color.color_blue));
            holder.tv_week.setTextColor(ResourceUtil.getColor(context, R.color.color_white));
        }else{
            holder.tv_week.setBackgroundColor(ResourceUtil.getColor(context, R.color.color_white));
            holder.tv_week.setTextColor(ResourceUtil.getColor(context, R.color.color_blue));
        }
        return convertView;
    }

    public static class ViewHolder{
        TextView tv_week;
    }
}
