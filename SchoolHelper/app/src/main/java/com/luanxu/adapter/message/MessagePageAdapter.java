package com.luanxu.adapter.message;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.view.ViewGroup;

import com.luanxu.bean.message.MessageTitleBean;
import com.luanxu.fragment.message.MessageItemFragment;

import java.util.List;

/**
 * @author: LuanXu
 * @createTime:2017/3/2 15:03
 * @className:  MessagePageAdapter
 * @Description: 资讯viewPager适配器
 */

public class MessagePageAdapter extends FragmentStatePagerAdapter {
    //顶部栏目集合
    private List<MessageTitleBean> titles;
    public int totalPage = 0;

    public MessagePageAdapter(FragmentManager fm, List<MessageTitleBean> titles) {
        super(fm);
        this.titles = titles;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return titles.get(position).getTitle();
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        return super.instantiateItem(container, position);
    }

    @Override
    public int getCount() {
        totalPage = titles.size();
        return totalPage;
    }

    @Override
    public Fragment getItem(int position) {
        return MessageItemFragment.newInstance(position,0, titles);
    }

    @Override
    public int getItemPosition(Object object) {
        return PagerAdapter.POSITION_NONE;
    }
}
