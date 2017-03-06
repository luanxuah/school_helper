package com.luanxu.adapter.message;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.luanxu.bean.message.MessageTitleBean;
import com.luanxu.activity.message.ChooseMessageActivity;
import com.luanxu.schoolhelper.R;
import com.luanxu.utils.ResourceUtil;

import java.util.List;

/**
 * @author: LuanXu
 * @createTime:2017/3/3 15:21
 * @className:  ChooseMessageAdapter
 * @Description: 选择频道适配器
 */

public class ChooseMessageAdapter extends BaseAdapter{
    //上下文对象
    private ChooseMessageActivity context;
    //列表数据
    private List<MessageTitleBean> list;

    private ViewHolder viewHoldr;

    public ChooseMessageAdapter(ChooseMessageActivity context){
        this.context = context;
    }

    /**
     * 设置数据
     * @param list
     */
    public void setDate(List<MessageTitleBean> list){
        this.list = list;
    }

    @Override
    public int getCount() {
        return list==null ? 0:list.size();
    }

    @Override
    public Object getItem(int position) {
        return list==null ? null:list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View contentView, ViewGroup viewGroup) {
        viewHoldr = null;
        if (contentView == null){
            viewHoldr = new ViewHolder();
            contentView = LayoutInflater.from(context).inflate(R.layout.item_choose_message, null);
            viewHoldr.tv_name = (TextView) contentView.findViewById(R.id.tv_name);
            viewHoldr.btn_select = (Button) contentView.findViewById(R.id.btn_select);
            contentView.setTag(viewHoldr);
        }else{
            viewHoldr = (ViewHolder) contentView.getTag();
        }

        final MessageTitleBean bean = list.get(position);
        viewHoldr.tv_name.setText(bean.getTitle());
        if (bean.getSelect().equals("1")){
            //已选择
            viewHoldr.btn_select.setText(ResourceUtil.getString(context, R.string.str_choosed));
            viewHoldr.btn_select.setSelected(true);
        }else{
            //未选择
            viewHoldr.btn_select.setText(ResourceUtil.getString(context, R.string.str_choose));
            viewHoldr.btn_select.setSelected(false);
        }

        /**
         * 点击按钮
         */
        viewHoldr.btn_select.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (view.isSelected()){
                    list.get(position).setSelect("0");
                    notifyDataSetChanged();
                }else{
                    list.get(position).setSelect("1");
                    notifyDataSetChanged();
                }
            }
        });

        return contentView;
    }

    static class ViewHolder{
        //频道的名称
        TextView tv_name;
        //选择按钮
        Button btn_select;
    }
}
