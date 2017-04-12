package com.luanxu.utils.okhttps;

import android.app.Dialog;
import android.content.Context;

import com.luanxu.bean.Bean;
import com.luanxu.utils.CommonConstant;
import com.luanxu.utils.NetWorkUtil;
import com.luanxu.utils.ToastUtil;

/**
 * @author: 范建海
 * @createTime: 2017/1/16 17:41
 * @className:  NetCallback
 * @description: 网络回调接口
 * @changed by:
 */
public abstract class NetCallback <T extends Bean> {

    public void onSuccess(Context ctx,T t, Dialog dialog) {
        if (dialog != null) {
            dialog.dismiss();
        }
    }


    public void onNoData(Context ctx,Dialog dialog){
        if (dialog != null) {
            dialog.dismiss();
        }
    }

    public void onNoData(Context ctx,String msg,Dialog dialog) {
        if (dialog != null) {
            dialog.dismiss();
        }
    }

    public void onError(Context ctx,Dialog dialog) {
        if (dialog != null) {
            try {
                dialog.dismiss();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        if(ctx != null) {
            if (!NetWorkUtil.isNetworkConnected(ctx)) {
                ToastUtil.show(ctx, CommonConstant.NO_NETWORK_HINT,0);
            }
        }
    }
}
