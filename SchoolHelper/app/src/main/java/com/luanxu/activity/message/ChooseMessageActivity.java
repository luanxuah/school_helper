package com.luanxu.activity.message;

import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import com.luanxu.adapter.message.ChooseMessageAdapter;
import com.luanxu.application.SchoolHelperApplication;
import com.luanxu.base.BaseActivity;
import com.luanxu.bean.message.MessageTitleBean;
import com.luanxu.custom.TitleBar;
import com.luanxu.schoolhelper.R;

import java.util.ArrayList;
import java.util.List;

/**
 * @author: LuanXu
 * @createTime:2017/3/3 14:28
 * @className:  ChooseMessageActivity
 * @Description: 选择资讯栏目的页面
 */

public class ChooseMessageActivity extends BaseActivity{
    //上下文对象
    private ChooseMessageActivity context;

    //所有栏目的名称
    private List<MessageTitleBean> list;

    //列表适配器
    private ChooseMessageAdapter adapter;

    //列表
    private ListView lv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_choose_message);
        SchoolHelperApplication.getInstance().addActivity(this);
        context = this;
        TitleBar bar = getTitleBar();
        bar.setBack();
        bar.setTitle(getResources().getString(R.string.str_choose_message), R.color.color_white);
        bar.enableRightBtn("完成", -1, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        init();
    }

    /**
     * 初始化数据和控件
     */
    private void init(){
        initDate();

        lv = (ListView) findViewById(R.id.lv);
        adapter = new ChooseMessageAdapter(context);
        adapter.setDate(list);
        lv.setAdapter(adapter);
    }

    /**
     * 初始化数据
     */
    private void initDate(){
        list = new ArrayList<MessageTitleBean>();
        MessageTitleBean bean1 = new MessageTitleBean("安徽师范大学", "11", "1");
        MessageTitleBean bean2 = new MessageTitleBean("安徽师范大学学生会", "11", "1");
        MessageTitleBean bean3 = new MessageTitleBean("安徽师范大学数计学院", "11", "1");
        MessageTitleBean bean4 = new MessageTitleBean("安徽师范大学经管学院", "11", "1");
        MessageTitleBean bean5 = new MessageTitleBean("安徽师范大学物电学院", "11", "1");
        MessageTitleBean bean6 = new MessageTitleBean("安徽师范大学空乘学院", "11", "1");
        MessageTitleBean bean7 = new MessageTitleBean("安徽师范大学生科学院", "11", "0");
        MessageTitleBean bean8 = new MessageTitleBean("安徽师范大学音乐学院", "11", "0");
        MessageTitleBean bean9 = new MessageTitleBean("安徽师范大学体育学院", "11", "0");
        MessageTitleBean bean10 = new MessageTitleBean("安徽师范大学教科学院", "11", "0");
        MessageTitleBean bean11 = new MessageTitleBean("安徽师范大学历史学院", "11", "0");
        MessageTitleBean bean12 = new MessageTitleBean("安徽师范大学空乘学院", "11", "0");
        MessageTitleBean bean13 = new MessageTitleBean("安徽师范大学法学院", "11", "0");
        MessageTitleBean bean14 = new MessageTitleBean("安徽师范大学皖江学院", "11", "0");

        list.add(bean1);
        list.add(bean2);
        list.add(bean3);
        list.add(bean4);
        list.add(bean5);
        list.add(bean6);
        list.add(bean7);
        list.add(bean8);
        list.add(bean9);
        list.add(bean10);
        list.add(bean11);
        list.add(bean12);
        list.add(bean13);
        list.add(bean14);
    }
}

























