package com.luanxu.schoolhelper;


import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class MyFragmentAdapterByFactory extends FragmentPagerAdapter {

	public MyFragmentAdapterByFactory(FragmentManager fm) {
		super(fm);
	}

	@Override
	public int getCount() {
		return 4;
	}

	@Override
	public Fragment getItem(int position) {
		return FragmentFactory.createFragment(position);
	}

	@Override
	public int getItemPosition(Object object) {
		return super.getItemPosition(object);
	}
}
