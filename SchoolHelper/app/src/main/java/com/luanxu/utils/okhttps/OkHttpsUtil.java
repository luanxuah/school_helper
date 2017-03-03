package com.luanxu.utils.okhttps;

import android.app.Dialog;
import android.content.Context;
import android.text.TextUtils;

import com.luanxu.bean.AccessTokenBean;
import com.luanxu.bean.Bean;
import com.luanxu.bean.PageBean;
import com.luanxu.custom.PullToRefreshListView;
import com.luanxu.utils.CommonConstant;
import com.luanxu.utils.GsonUtil;
import com.luanxu.utils.LogUtil;
import com.luanxu.utils.NetWorkUtil;
import com.luanxu.utils.SharedPreferencesUtil;
import com.luanxu.utils.ToastUtil;
import com.luanxu.utils.UrlConstant;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.builder.PostFormBuilder;
import com.zhy.http.okhttp.callback.Callback;
import com.zhy.http.okhttp.request.RequestCall;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Response;

/**
 * @author: 范建海
 * @createTime: 2017/1/16 14:48
 * @className:  OkHttpsUtil
 * @description: https网路框架
 * @changed by:
 */
public class OkHttpsUtil {

    /**
     *  思路：
     *      1.是否需要带Token的控制(不向外面暴露接口，方法名区分)
     *      2.进度条的有无控制
     *      3.进度条自定义提示语的控制
     *      4.有无请求参数的控制
     *      5.下拉刷新，上拉加载的控制
     *
     *      postWithToken方法列表：
     *      1.没有进度条                       && 没有请求参数
     *      2.没有进度条                       && 有请求参数
     *      3.没有自定义加载框（从外面传进来的） && 没有请求参数
     *      4.没有自定义加载框（从外面传进来的） && 有请求参数
     *      5.有自定义加载框                   && 没有请求参数       && 加载提示语默认
     *      6.有自定义加载框                   && 有请求参数         && 加载提示语默认
     *      7.有自定义加载框                   && 没有请求参数       && 自定义加载提示语
     *      8.有自定义加载框                   && 有请求参数　       && 自定义加载提示语
     *      9.下拉刷新、上拉是否有进度条加载框   && 有请求参数         && 加载提示语默认
     *
     *      postWithoutToken方法列表：
     *      1.没有自定义加载框（从外面传进来的） && 没有请求参数
     *      2.没有自定义加载框（从外面传进来的） && 有请求参数
     *      3.有自定义加载框                   && 没有请求参数       && 加载提示语默认
     *      4.有自定义加载框                   && 有请求参数         && 加载提示语默认
     *      5.有自定义加载框                   && 没有请求参数       && 自定义加载提示语
     *      6.有自定义加载框                   && 有请求参数　       && 自定义加载提示语
     *
     */

    // 请求成功有数据
    public static final String REQUEST_SUCCESS = "1";
    // 请求成功无数据
    public static final String REQUEST_NO_DATA = "2";
    // 请求Token失效
    public static final String REQUEST_TOKEN_INVALID = "3";
    // 默认失效的Token
    static final String DEFAULT_TOKEN = "123";
    // 当前的状态值，用来区分请求接口返回的状态值
    private static String CURRENT_STATUS = "";
    // 当前第几页
    private static int CURRENT_PAGE = 1;
    // 总页数
    private static int TOTAL_PAGE = 1;

    /**
     * 1.POST带Token方式请求
     *      a.没有进度条
     *      b.没有请求参数
     * @param ctx               上下文
     * @param url               请求地址
     * @param cls               相应的实体Bean的字节码类型
     * @param flag              重载标记，无意义
     * @param netCallback       网络回调
     * @param <T>               泛型
     */
    public static <T extends Bean> void postWithToken(Context ctx, String url, Class<T> cls, boolean flag, NetCallback<T> netCallback) {
        postWithToken(true,null,ctx,url,false,null,null,null,cls,netCallback);
    }

    /**
     * 2.POST带Token方式请求
     *      a.没有进度条
     *      b.有请求参数
     * @param ctx               上下文
     * @param url               请求地址
     * @param params            请求参数集合
     * @param cls               相应的实体Bean的字节码类型
     * @param flag              重载标记，无意义
     * @param netCallback       网络回调
     * @param <T>               泛型
     */
    public static <T extends Bean> void postWithToken(Context ctx, String url, Map<String,String> params, Class<T> cls, boolean flag, NetCallback<T> netCallback) {
        postWithToken(true,null,ctx,url,false,null,null,params,cls,netCallback);
    }

    /**
     * 3.POST带Token方式请求
     *      a.加载等待框使用的是从外面传过来的(上传完文件等调用)
     *      b.没有请求参数
     * @param ctx               上下文
     * @param url               请求地址
     * @param outsideDialog     外部加载等待框
     * @param cls               相应的实体Bean的字节码类型
     * @param netCallback       网络回调
     * @param <T>               泛型
     */
    public  static <T extends Bean> void postWithToken(Context ctx, String url, Dialog outsideDialog, Class<T> cls, NetCallback<T> netCallback) {
        postWithToken(true,null,ctx,url,false,null,outsideDialog,null,cls,netCallback);
    }

    /**
     * 4.POST带Token方式请求
     *      a.加载等待框使用的是从外面传过来的(上传完文件等调用)
     *      b.有请求参数
     * @param ctx               上下文
     * @param url               请求地址
     * @param outsideDialog     外部加载框
     * @param params            请求参数集合
     * @param cls               相应的实体Bean的字节码类型
     * @param netCallback       网络回调
     * @param <T>               泛型
     */
    public  static <T extends Bean> void postWithToken(Context ctx, String url, Dialog outsideDialog, Map<String,String> params, Class<T> cls, NetCallback<T> netCallback) {
        postWithToken(true,null,ctx,url,false,null,outsideDialog,params,cls,netCallback);
    }

    /**
     * 5.POST带Token方式请求
     *      a.有自定义加载框
     *      b.没有请求参数
     *      c.加载框有默认提示语
     * @param ctx               上下文
     * @param url               请求地址
     * @param cls               相应的实体Bean的字节码类型
     * @param netCallback       网络请求回调
     * @param <T>               泛型
     */
    public  static <T extends Bean> void postWithToken(Context ctx, String url, Class<T> cls, NetCallback<T> netCallback) {
        postWithToken(true,null,ctx,url,true,null,null,null,cls,netCallback);
    }

    /**
     * 6.POST带Token方式请求
     *      a.有自定义加载框
     *      b.有请求参数
     *      c.加载框有默认提示语
     * @param ctx               上下文
     * @param url               请求地址
     * @param params            请求参数集合
     * @param cls               相应的实体Bean的字节码类型
     * @param netCallback       网络请求回调
     * @param <T>
     */
    public  static <T extends Bean> void postWithToken(Context ctx, String url, Map<String,String> params, Class<T> cls, NetCallback<T> netCallback) {
        postWithToken(true,null,ctx,url,true,null,null,params,cls,netCallback);
    }

    /**
     * 7.POST带Token方式请求
     *      a.有自定义加载框
     *      b.没有请求参数
     *      c.可自定义加载框提示语
     * @param ctx               上下文
     * @param url               请求地址
     * @param cls               相应的实体Bean的字节码类型
     * @param netCallback       网络请求回调
     * @param <T>               泛型
     */
    public  static <T extends Bean> void postWithToken(Context ctx, String url, String loadingMsg, Class<T> cls, NetCallback<T> netCallback) {
        postWithToken(true,null,ctx,url,true,loadingMsg,null,null,cls,netCallback);
    }



    /**
     * 8.POST带Token方式请求
     *      a.有自定义加载框
     *      b.有请求参数
     *      c.可自定义加载框提示语
     * @param ctx               上下文
     * @param url               请求地址
     * @param params            请求参数集合
     * @param cls               相应的实体Bean的字节码类型
     * @param netCallback       网络请求回调
     * @param <T>
     */
    public  static <T extends Bean> void postWithToken(Context ctx, String url, String loadingMsg, Map<String,String> params, Class<T> cls, NetCallback<T> netCallback) {
        postWithToken(true,null,ctx,url,true,loadingMsg,null,params,cls,netCallback);
    }
    /**
     *  9.POST分页请求数据
     *      1.下拉刷新、上拉是否有进度条加载框
     *      2.有请求参数
     *      3.加载提示语默认
     * @param isRefresh                 下拉刷新 true，上拉加载false
     * @param pullToRefreshListView      ListView控件
     * @param ctx                       上下文
     * @param url                       请求地址
     * @param isShowDialog              是否显示进度条加载框
     * @param params                    请求参数
     * @param cls                       实体Bean的字节码类型
     * @param netCallback               网络回调
     * @param <T>                       泛型
     */
    public static <T extends PageBean> void postWithToken(boolean isRefresh, PullToRefreshListView pullToRefreshListView, Context ctx, String url, boolean isShowDialog, Map<String,String> params, Class<T> cls, NetCallback<T> netCallback) {
        postWithRefreshAndLoad(isRefresh,pullToRefreshListView,ctx,url,isShowDialog,null,null,params,cls,netCallback);
    }

    //=================================================================

    /**
     * 1.POST不带Token方式请求
     *      a.加载等待框使用的是从外面传过来的(上传完文件等调用)
     *      b.没有请求参数
     * @param ctx               上下文
     * @param url               请求地址
     * @param outsideDialog     外部加载等待框
     * @param cls               相应的实体Bean的字节码类型
     * @param netCallback       网络回调
     * @param <T>               泛型
     */
    public  static <T extends Bean> void postWithoutToken(Context ctx, String url, Dialog outsideDialog, Class<T> cls, NetCallback<T> netCallback) {
        postWithoutToken(ctx,url,false,null,outsideDialog,null,cls,netCallback);
    }

    /**
     * 2.POST不带Token方式请求
     *      a.加载等待框使用的是从外面传过来的(上传完文件等调用)
     *      b.有请求参数
     * @param ctx               上下文
     * @param url               请求地址
     * @param outsideDialog     外部加载框
     * @param params            请求参数集合
     * @param cls               相应的实体Bean的字节码类型
     * @param netCallback       网络回调
     * @param <T>               泛型
     */
    public  static <T extends Bean> void postWithoutToken(Context ctx, String url, Dialog outsideDialog, Map<String,String> params, Class<T> cls, NetCallback<T> netCallback) {
        postWithoutToken(ctx,url,false,null,outsideDialog,params,cls,netCallback);
    }

    /**
     * 3.POST不带Token方式请求
     *      a.有自定义加载框
     *      b.没有请求参数
     *      c.加载框有默认提示语
     * @param ctx               上下文
     * @param url               请求地址
     * @param cls               相应的实体Bean的字节码类型
     * @param netCallback       网络请求回调
     * @param <T>               泛型
     */
    public  static <T extends Bean> void postWithoutToken(Context ctx, String url, Class<T> cls, NetCallback<T> netCallback) {
        postWithoutToken(ctx,url,true,null,null,null,cls,netCallback);
    }

    /**
     * 4.POST不带Token方式请求
     *      a.有自定义加载框
     *      b.有请求参数
     *      c.加载框有默认提示语
     * @param ctx               上下文
     * @param url               请求地址
     * @param params            请求参数集合
     * @param cls               相应的实体Bean的字节码类型
     * @param netCallback       网络请求回调
     * @param <T>
     */
    public  static <T extends Bean> void postWithoutToken(Context ctx, String url, Map<String,String> params, Class<T> cls, NetCallback<T> netCallback) {
        postWithoutToken(ctx,url,true,null,null,params,cls,netCallback);
    }

    /**
     * 5.POST不带Token方式请求
     *      a.有自定义加载框
     *      b.没有请求参数
     *      c.可自定义加载框提示语
     * @param ctx               上下文
     * @param url               请求地址
     * @param cls               相应的实体Bean的字节码类型
     * @param netCallback       网络请求回调
     * @param <T>               泛型
     */
    public  static <T extends Bean> void postWithoutToken(Context ctx, String url, String loadingMsg, Class<T> cls, NetCallback<T> netCallback) {
        postWithoutToken(ctx,url,true,loadingMsg,null,null,cls,netCallback);
    }

    /**
     * 6.POST不带Token方式请求
     *      a.有自定义加载框
     *      b.有请求参数
     *      c.可自定义加载框提示语
     * @param ctx               上下文
     * @param url               请求地址
     * @param params            请求参数集合
     * @param cls               相应的实体Bean的字节码类型
     * @param netCallback       网络请求回调
     * @param <T>
     */
    public  static <T extends Bean> void postWithoutToken(Context ctx, String url, String loadingMsg, Map<String,String> params, Class<T> cls, NetCallback<T> netCallback) {
        postWithoutToken(ctx,url,true,loadingMsg,null,params,cls,netCallback);
    }

    // ====================================================================

    /**
     * POST分页请求数据内部方法
     * @param isRefresh             是否下拉刷新或者上拉加载
     * @param pullToRefreshListView ListView控件
     * @param ctx                 上下文
     * @param url                 请求地址
     * @param needCustomDialog    是否需要自定义等待框
     * @param loadingMsg          进度条提示语
     * @param outsideDialog       外部加载框
     * @param params              请求参数
     * @param cls                 相应的实体Bean的字节码类型
     * @param netCallback         网络请求回调
     * @param <T>                 泛型
     */
    private  static <T extends Bean> void postWithRefreshAndLoad(boolean isRefresh, PullToRefreshListView pullToRefreshListView, Context ctx, String url, boolean needCustomDialog, String loadingMsg, Dialog outsideDialog, Map<String,String> params, Class<T> cls, NetCallback<T> netCallback) {
        if (pullToRefreshListView != null) {

            if (isRefresh) {
                // 刷新
                CURRENT_PAGE = 1;
                TOTAL_PAGE = 1;
            } else {
                CURRENT_PAGE ++;
            }

            if (params == null) {
                params = new HashMap<String,String>();
            }

            params.put("page", String.valueOf(CURRENT_PAGE));

            postWithToken(isRefresh,pullToRefreshListView,ctx,url,needCustomDialog,loadingMsg,outsideDialog,params,cls,netCallback);

        }
    }

    /**
     * POST带Token方式请求的内部方法
     * @param isRefresh             下拉刷新true,上拉加载false
     * @param pullToRefreshListView ListView控件
     * @param ctx                 上下文
     * @param url                 请求地址
     * @param needCustomDialog    是否需要自定义等待框
     * @param loadingMsg          进度条提示语
     * @param outsideDialog       外部加载框
     * @param params              请求参数
     * @param cls                 相应的实体Bean的字节码类型
     * @param netCallback         网络请求回调
     * @param <T>                 泛型
     */
    private static <T extends Bean> void postWithToken(final boolean isRefresh , PullToRefreshListView pullToRefreshListView, Context ctx, String url, boolean needCustomDialog, String loadingMsg, Dialog outsideDialog, Map<String,String> params, Class<T> cls, final NetCallback<T> netCallback) {

        if (ctx != null && !TextUtils.isEmpty(url) && cls != null) {

            // 请求服务器等待框
            Dialog dialog = null;

            if (needCustomDialog) {
                // 进度条加载提示语
                String loadingMessage = CommonConstant.LOADING_MSG;

                if (!TextUtils.isEmpty(loadingMsg)) {
                    loadingMessage = loadingMsg;
                }
                /**
                 * 在此处可以更换进度条统一进度条样式
                 */
//                dialog = new LoadingStyleOneDialog(ctx,R.layout.dialog_tips_loading,loadingMessage);
                dialog = new LoadingStyleDefaultDialog(ctx,loadingMessage);
            }else {
                dialog = outsideDialog;
            }


            post(isRefresh,pullToRefreshListView,ctx,url,dialog,params,true,cls,netCallback);
        }




    }

    /**
     * POST不带Token方式请求的内部方法
     * @param ctx           上下文
     * @param url           请求地址
     * @param needDialog    是否需要等待框
     * @param loadingMsg    进度条提示语
     * @param outsideDialog 外部加载框
     * @param params        请求参数
     * @param cls           相应的实体Bean的字节码类型
     * @param netCallback   网络请求回调
     * @param <T>           泛型
     */
    private static <T extends Bean> void postWithoutToken(Context ctx, String url, boolean needDialog, String loadingMsg, Dialog outsideDialog, Map<String,String> params, Class<T> cls, final NetCallback<T> netCallback) {

        if (ctx != null && !TextUtils.isEmpty(url) && cls != null) {

            // 请求服务器等待框
            Dialog dialog = null;

            if (needDialog) {
                // 进度条加载提示语
                String loadingMessage = CommonConstant.LOADING_MSG;

                if (!TextUtils.isEmpty(loadingMsg)) {
                    loadingMessage = loadingMsg;
                }
                /**
                 * 在此处可以更换进度条统一进度条样式
                 */
                dialog = new LoadingStyleDefaultDialog(ctx,loadingMessage);
            }else {
                dialog = outsideDialog;
            }

            post(true,null,ctx,url,dialog,params,false,cls,netCallback);
        }
    }

    /**
     * POST基本业务逻辑处理内部方法
     *      1.基本参数校验在使用该方法的方法中进行校验
     * @param isRefresh             下拉刷新true 上拉加载false
     * @param pullToRefreshListView ListView控件
     * @param ctx                   上下文
     * @param url                   请求地址
     * @param dialog                进度条
     * @param params                请求参数集合
     * @param needToken             是否需要Token
     * @param cls                   相应的实体Bean的字节码类型
     * @param netCallback           网络请求回调类，需要子类具体实现
     * @param <T>                   泛型
     */
    private static <T extends Bean> void post(final boolean isRefresh, final PullToRefreshListView pullToRefreshListView, final Context ctx, final String url, final Dialog dialog, final Map<String,String> params, final boolean needToken, final Class<T> cls, final NetCallback<T> netCallback) {
        // 判断当前是否有网络
        if(NetWorkUtil.isNetworkConnected(ctx)) {

            // 构建Post请求创建器
            PostFormBuilder builder = OkHttpUtils.post().url(url).tag(ctx).addParams("json", "Y").params(params);
            // 判断是否需要Token
            if (needToken) {
                // 本地缓存的Token
                String tokenCode = SharedPreferencesUtil.get(ctx, CommonConstant.TOKEN_FILE_NAME,CommonConstant.TOKEN_CODE_KEY,DEFAULT_TOKEN);
                builder.addHeader("accessToken", tokenCode);
                // 输出请求参数日志(有Token)
                LogUtil.printParams(url,params,tokenCode);
            } else {
                // 输出请求参数日志(没有Token)
                LogUtil.printParams(url,params,null);
            }
            // 构造回调对象
            final RequestCall requestCall = builder.build();

            if(requestCall != null) {
                // 回调处理
                final Callback callback = new Callback<T>() {

                    @Override
                    public T parseNetworkResponse(Response response, int id) throws Exception {
                        String body = response.body().string();
                        LogUtil.d("OkHttpUtil ===== response -->",body);
                        // 解析实体Bean的引用
                        T t = null;

                        if (!TextUtils.isEmpty(body)) {

                            try {
                                JSONObject jsonObject = new JSONObject(body);
                                CURRENT_STATUS = jsonObject.optString("status");

                                if (OkHttpsUtil.REQUEST_SUCCESS.equals(CURRENT_STATUS)) {

                                    // 判断是否为请求Oss STSToken的接口,如果是，需要缓存相关权限
//                                    if (UrlConstant.GET_OSS_STS_TOKEN.equals(url)) {
//                                        // 缓存权限Json
//                                        SharedPreferencesUtil.clear(DoctorApplication.getInstance(),CommonConstant.CACHE_AUTHORITY_FILE_NAME);
//                                        // 删除之前缓存的图片文件
//                                        SDCardUtil.delFolder(CommonConstant.IMAGE_LOADER_CACHE_PATH);
//                                        // Token失效，清除本地缓存文件内容
//                                        SharedPreferencesUtil.set(DoctorApplication.getInstance(),CommonConstant.CACHE_AUTHORITY_FILE_NAME,CommonConstant.OSS_AUTHORITY_JSON,body);
//                                    }

                                    // 获取当前页的总页数
                                    if (pullToRefreshListView != null) {
                                        CURRENT_PAGE = jsonObject.optInt("page");
                                        TOTAL_PAGE = jsonObject.optInt("total");
                                    }

                                    t = GsonUtil.json2Object(body,cls);

                                } else if (OkHttpsUtil.REQUEST_NO_DATA.equals(CURRENT_STATUS)) {

                                } else if (OkHttpsUtil.REQUEST_TOKEN_INVALID.equals(CURRENT_STATUS)){
                                    if (needToken) {
                                        // Token失效
                                        OkHttpsUtil.getTokenAndResume(isRefresh,pullToRefreshListView,ctx,url,dialog,params,needToken,cls,netCallback);
                                    }
                                }
                            }catch(Exception e) {
                                CURRENT_STATUS = "";
                            }
                        }
                        return t;
                    }

                    @Override
                    public void onResponse(T t, int id) {
                        if(!TextUtils.isEmpty(CURRENT_STATUS) && netCallback != null) {

                            if (OkHttpsUtil.REQUEST_SUCCESS.equals(CURRENT_STATUS)) {

                                if (pullToRefreshListView != null) {
                                    if(CURRENT_PAGE > 0 && TOTAL_PAGE > 0) {

                                        if (CURRENT_PAGE >= TOTAL_PAGE) {
                                            pullToRefreshListView.setLoadFull(true);
                                        } else {
                                            pullToRefreshListView.setLoadFull(false);
                                        }
                                    }

                                    if (!isRefresh) {
                                        pullToRefreshListView.onLoadComplete();
                                    }else {
                                        pullToRefreshListView.onRefreshComplete();
                                    }
                                }
                                // 请求数据成功
                                netCallback.onSuccess(ctx,t,dialog);
                            }else if(OkHttpsUtil.REQUEST_NO_DATA.equals(CURRENT_STATUS)) {
                                // 请求无数据
                                netCallback.onNoData(ctx,dialog);
                            }else {
                                return;
                            }
                        }else {
                            netCallback.onError(ctx,dialog);
                        }
                    }

                    @Override
                    public void onError(Call call, Exception e, int id) {
                        if (netCallback != null) {
                            netCallback.onError(ctx,dialog);
                        }
                    }

                };
                // 请求服务器
                requestCall.execute(callback);
            }
        } else {
            // 请求网络数据时，没有网络的回调处理
            if (netCallback != null) {
                netCallback.onError(ctx,dialog);
            }
            // 上拉加载失败时，游标回滚一个单位
            if (!isRefresh && CURRENT_PAGE > 1) {
                // 没有网络的时候，上拉加载失败，当前页减一，
                CURRENT_PAGE--;
            }
        }
    }

    /**
     * 获取最新的Token，并且继续上次的网络请求的内部方法
     * @param isRefresh             下拉刷新true，上拉加载false
     * @param pullToRefreshListView ListView控件
     * @param ctx                   上下文
     * @param url                   请求地址
     * @param dialog                进度条
     * @param params                请求参数集合
     * @param needToken             是否需要传Token
     * @param cls                   相应的实体Bean的字节码类型
     * @param netCallback           网络回调
     * @param <T>                   泛型
     */
    private static <T extends Bean> void getTokenAndResume(final boolean isRefresh, final PullToRefreshListView pullToRefreshListView, final Context ctx, final String url, final Dialog dialog, final Map<String,String> params, final boolean needToken, final Class<T> cls, final NetCallback<T> netCallback){
        // 本地缓存有tokenCode
        String username="";
        String password = "";
        if (!TextUtils.isEmpty(username) && !TextUtils.isEmpty(password)) {

            Map<String,String> tokenParams = new HashMap<String, String>();
            tokenParams.put("username",username);
            tokenParams.put("password",password);

            OkHttpUtils.post().url(UrlConstant.GET_APP_TOKEN).params(tokenParams).addParams("json","Y").build().execute(new Callback<AccessTokenBean>() {
                @Override
                public AccessTokenBean parseNetworkResponse(Response response, int id) throws Exception {
                    String body = response.body().string();
                    // 带解析的对象引用
                    AccessTokenBean atBean = null;
                    // 解析实体Bean
                    if (!TextUtils.isEmpty(body)) {
                        atBean = GsonUtil.json2Object(body,AccessTokenBean.class);
                    }
                    return atBean;
                }

                @Override
                public void onError(Call call, Exception e, int id) {
                    if(dialog != null) {
                        dialog.dismiss();
                    }
                }

                @Override
                public void onResponse(AccessTokenBean atBean, int id) {
                    if(atBean != null) {
                        if(OkHttpsUtil.REQUEST_SUCCESS.equals(atBean.getStatus())) {
                            // 缓存token到本地
                            if (!TextUtils.isEmpty(atBean.getAccessToken())) {
                                SharedPreferencesUtil.set(ctx,CommonConstant.TOKEN_FILE_NAME,CommonConstant.TOKEN_CODE_KEY,atBean.getAccessToken());
                                // 接着上次的回调请求服务器
                                post(isRefresh,pullToRefreshListView,ctx,url,dialog,params,needToken,cls,netCallback);
                            }

                        }
                    }
                }
            });

        }else {
            ToastUtil.show(ctx,"这个家伙没有ID或者密码，获取不到Token",0);
        }
    }

    /**
     * 根据tag取消请求WE
     * @param ctx tag标记(在这里我们把请求所在的上下文作为tag)
     */
    public static void cancel(Context ctx) {
        OkHttpUtils.getInstance().cancelTag(ctx);
    }

}
