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
 * @createTime:2016/12/20 20:48
 * @className:  ScoreImquiryAdapter
 * @Description: 成绩查询的适配器
 */

public class ScoreImquiryAdapter extends BaseAdapter{
    //上下文对象
    private Context context;

    public ScoreImquiryAdapter(Context context){
        this.context = context;
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
        ViewHolder holder = null;
        if (contentView == null){
            holder = new ViewHolder();
            contentView = LayoutInflater.from(context).inflate(R.layout.item_score_inquiry, null);
            holder.tv_name = (TextView) contentView.findViewById(R.id.tv_name);
            holder.tv_type = (TextView) contentView.findViewById(R.id.tv_type);
            holder.tv_credit = (TextView) contentView.findViewById(R.id.tv_credit);
            holder.tv_text_score = (TextView) contentView.findViewById(R.id.tv_text_score);
            holder.tv_score = (TextView) contentView.findViewById(R.id.tv_score);
            holder.tv_credit_num = (TextView) contentView.findViewById(R.id.tv_credit_num);
            holder.tv_exam_again = (TextView) contentView.findViewById(R.id.tv_exam_again);
            contentView.setTag(holder);
        }else{
            holder = (ViewHolder) contentView.getTag();
        }

        return contentView;
    }

    static class ViewHolder{
        //课程名称
        public TextView tv_name;
        //课程类型
        public TextView tv_type;
        //课程的学分
        public TextView tv_credit;
        //期末的成绩
        public TextView tv_text_score;
        //成绩
        public TextView tv_score;
        //学分绩点
        public TextView tv_credit_num;
        //补考
        public TextView tv_exam_again;
    }
}
