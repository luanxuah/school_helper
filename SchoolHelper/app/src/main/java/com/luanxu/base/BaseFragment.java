package com.luanxu.base;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.luanxu.custom.TitleBar;

public class BaseFragment extends Fragment{

	private TitleBar titleBar = null;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {
		return super.onCreateView(inflater, container, savedInstanceState);
	}

	public TitleBar getTitleBar() {
		if (titleBar == null) {
			titleBar = new TitleBar(getActivity(), getView());
		}
		return titleBar;
	}
}
