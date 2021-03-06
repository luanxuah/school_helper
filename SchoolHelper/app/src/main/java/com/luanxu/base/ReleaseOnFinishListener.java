package com.luanxu.base;

 
public interface ReleaseOnFinishListener {

    // 功能完成时结束中间界面广播
    public static final String BACK_RELEASE_ACTION = ".action.BACK_RELEASE";

    /**
     * @author: lihs
     * @Title: isBackable
     * @Description: 是否功能中间界面，可返回
     * @return
     * @date: 2014-3-10 下午5:27:57
     */
    public boolean isBackable();

    /**
     * @author: lihs
     * @Title: setReleaseOnFinish
     * @Description: 功能结束后是否结束中间界面
     * @param isRelease
     * @date: 2014-3-10 下午5:32:57
     */
    public void setReleaseOnFinish(boolean isRelease);

    public void releasePreActivity();

    /**
     * 刷新UI的
     * @param pushMsg
     */
    public void refreshUI(Object pushMsg);

    /**
     * 网络变化回调
     * @param netType 网络类型
     */
    public void callBackForNetWork(String netType);
}
