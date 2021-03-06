package com.luanxu.schoolhelper;

import android.annotation.SuppressLint;
import android.support.v4.app.Fragment;

import com.luanxu.fragment.community.CommunityFragment;
import com.luanxu.fragment.message.MessageFragment;
import com.luanxu.fragment.syllabus.SyllabusFragment;
import com.luanxu.fragment.user.UserCenterFragment;

import java.util.HashMap;

/**
 * 
 * @author Stephen 获取四个主界面的Fragment的工厂类
 *
 */
public class FragmentFactory {
	@SuppressLint("UseSparseArrays")
	private static HashMap<Integer, Fragment> hashMap = new HashMap<Integer, Fragment>();

	public static Fragment createFragment(int position) {
		Fragment baseFragment = null;
		if (hashMap.containsKey(position)) {
			if (hashMap.get(position) != null) {
				baseFragment =hashMap.get(position);
			}
		} else {
			switch (position) {
			case 0:
				baseFragment = new SyllabusFragment();
				break;
			case 1:
				baseFragment = new MessageFragment();
				break;
			case 2:
				baseFragment = new CommunityFragment();
				break;
			case 3:
				baseFragment = new UserCenterFragment();
				break;
			}
			hashMap.put(position, baseFragment);
		}
		return baseFragment;
	}
}
