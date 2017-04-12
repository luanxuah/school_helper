package com.luanxu.custom.album;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;

import com.luanxu.base.BaseFragment;

/**
 * Created by TungDX
 */
public class BaseFragmentCamera extends BaseFragment {
    protected Context mContext;
    protected MediaImageLoader mMediaImageLoader;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        FragmentHost host = (FragmentHost) activity;
        mMediaImageLoader = host.getImageLoader();
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mContext = getActivity();
    }
}
