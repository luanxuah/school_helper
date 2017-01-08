package com.luanxu.activity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.luanxu.application.SchoolHelperApplication;
import com.luanxu.base.BaseActivity;
import com.luanxu.custom.CommonDialog;
import com.luanxu.custom.TitleBar;
import com.luanxu.custom.percent.PercentLinearLayout;
import com.luanxu.schoolhelper.R;
import com.luanxu.utils.CommonConstant;
import com.luanxu.utils.CommonUtils;
import com.luanxu.utils.ResourceUtil;
import com.luanxu.utils.SDCardUtil;
import com.luanxu.utils.ToastUtil;

/**
 * Created by 栾煦 on 2016/12/22.
 */
public class SetActivity extends BaseActivity implements View.OnClickListener{
    //上下文对象
    private Context context;

    //修改密码
    private PercentLinearLayout pll_change_passward;
    //清除缓存
    private PercentLinearLayout pll_clear_cache;
    //当前缓存
    private TextView tv_cache;
    //版本更新
    private PercentLinearLayout pll_update_version;
    //当前版本
    private TextView tv_version;
    //帮助和反馈
    private PercentLinearLayout pll_help;
    //关于
    private PercentLinearLayout pll_about;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_set);
        SchoolHelperApplication.getInstance().addActivity(this);
        context = this;

        TitleBar bar = getTitleBar();
        bar.setTitle(getResources().getString(R.string.str_set), R.color.color_white);
        bar.setBack();
        init();
    }

    /**
     * 初始化数据和控件
     */
    private void init(){
        pll_change_passward = (PercentLinearLayout) findViewById(R.id.pll_change_passward);
        pll_change_passward.setOnClickListener(this);
        pll_clear_cache = (PercentLinearLayout) findViewById(R.id.pll_clear_cache);
        pll_clear_cache.setOnClickListener(this);
        pll_update_version = (PercentLinearLayout) findViewById(R.id.pll_update_version);
        pll_update_version.setOnClickListener(this);
        pll_help = (PercentLinearLayout) findViewById(R.id.pll_help);
        pll_update_version.setOnClickListener(this);
        pll_help = (PercentLinearLayout) findViewById(R.id.pll_help);
        pll_help.setOnClickListener(this);
        pll_about = (PercentLinearLayout) findViewById(R.id.pll_about);
        pll_about.setOnClickListener(this);
        tv_version = (TextView) findViewById(R.id.tv_version);
        tv_cache = (TextView) findViewById(R.id.tv_cache);
        tv_cache.setText(SDCardUtil.getAutoFileOrFilesSize(CommonConstant.CACHE_PATH));
    }

    @Override
    public void onClick(View view) {
        Intent intent = null;
        switch (view.getId()){
            case R.id.pll_change_passward:
                //修改密码
                intent = new Intent(context, ChangePasswardActivity.class);
                startActivity(intent);
                break;
            case R.id.pll_clear_cache:
                //清除缓存
                if (!tv_cache.getText().equals("0B")){
                    showDialog();
                }else{
                    ToastUtil.show(context, R.string.str_no_cache, Toast.LENGTH_SHORT);
                }
                break;
            case R.id.pll_update_version:
                //更新版本
                checkUpdates();
                break;
            case R.id.pll_help:
                //帮助和反馈
                intent = new Intent(context, HelpActivity.class);
                startActivity(intent);
                break;
            case R.id.pll_about:
                //关于
                break;
        }
    }

    /**
     * 展示清除缓存的dialog
     */
    private void showDialog(){
        CommonDialog mCommonDialog = new CommonDialog(context);
        mCommonDialog.setTitle(ResourceUtil.getString(context, R.string.str_warm_prompt));
        mCommonDialog.setMessage(ResourceUtil.getString(context, R.string.str_clear_cache_prompt) + tv_cache.getText().toString() + "。");
        mCommonDialog.setCancleButton(null, ResourceUtil.getString(context, R.string.str_cancle));
        mCommonDialog.setPositiveButton(new CommonDialog.BtnClickedListener() {

            @Override
            public void onBtnClicked() {
                boolean isClear = deleteCache();
                if (isClear) {
                    ToastUtil.show(context, R.string.str_clear_cache_success, Toast.LENGTH_SHORT);
                    tv_cache.setText(SDCardUtil.getAutoFileOrFilesSize(CommonConstant.APP_LOCAL_PATH_ROOT));
                } else {
                    ToastUtil.show(context, R.string.str_clear_cache_error, Toast.LENGTH_SHORT);
                }
            }
        }, "确定");
        mCommonDialog.showDialog();
    }

    /**
     * @return 清理缓存
     */
    private boolean deleteCache() {
        try {
            SDCardUtil.delFolder(CommonConstant.CACHE_PATH);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 点击检查新版本
     */
    public void checkUpdates() {
        // ToastUtil.show(context, "当前版本是最新版本", Toast.LENGTH_SHORT);
        final Dialog dialog = new Dialog(this, R.style.CustomProgressDialog);
        View viewDialog = LayoutInflater.from(this).inflate(R.layout.dialog_version_update, null);
        ImageView iv_cancle = (ImageView) viewDialog.findViewById(R.id.iv_dialog_update_cancle);
        Button btn_upgrade = (Button) viewDialog.findViewById(R.id.bt_dialog_update_upgrade);
        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        params.width = CommonUtils.getDeviceSize(this).x * 71 / 100;
        params.height = ViewGroup.LayoutParams.WRAP_CONTENT;

        dialog.addContentView(viewDialog, params);
        dialog.show();
        //取消
        iv_cancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.cancel();
            }
        });
        //立即升级
        btn_upgrade.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ToastUtil.show(context, "后台更新中。。。", Toast.LENGTH_SHORT);
                dialog.cancel();
            }
        });
    }
}
