package com.luanxu.custom.bottommenu;

import com.luanxu.bean.BottomMenuBean;

import java.util.ArrayList;
import java.util.List;

/**
 * *****************************************
 * @author  sing
 * @文件名称 : BottomMenuUtil.java
 * @创建时间 : 2015年11月17日 下午5:00:26
 * @文件描述 : BottomMenuUtil
 * *****************************************
 */
public class BottomMenuUtil {

	private ArrayList<String> list = new ArrayList<String>();
	public ArrayList<String> listCode = new ArrayList<String>();
	/** 单例 */
	public static BottomMenuUtil model;

	public ArrayList<String> getListCode() {
		return listCode;
	}

	public void setListCode(ArrayList<String> listCode) {
		this.listCode = listCode;
	}

	/** 获取单例 */
	public static BottomMenuUtil getSingleton() {
		if (null == model) {
			model = new BottomMenuUtil();
		}
		return model;
	}

	public ArrayList<String> getBottomMenuBean(List<BottomMenuBean> lists) {
		if (listCode.size() > 0) {
			listCode.clear();
		}
		if (list.size() > 0) {
			list.clear();
		}
		for (int i = 0; i < lists.size(); i++) {
			list.add(lists.get(i).content);
			listCode.add(lists.get(i).id);
		}
		return list;
	}
}
