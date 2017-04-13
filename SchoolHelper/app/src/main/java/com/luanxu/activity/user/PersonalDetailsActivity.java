package com.luanxu.activity.user;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.luanxu.base.BaseActivity;
import com.luanxu.custom.CircleImageView;
import com.luanxu.custom.CommonDialog;
import com.luanxu.custom.TitleBar;
import com.luanxu.custom.album.MediaPickerActivity;
import com.luanxu.custom.album.SelectPhotoAlbumUtils;
import com.luanxu.custom.percent.PercentLinearLayout;
import com.luanxu.schoolhelper.R;
import com.luanxu.utils.LoaderImageUtil;
import com.luanxu.utils.ResourceUtil;

import java.io.File;
import java.util.List;

/**
 * @author: LuanXu
 * @createTime:2017/3/3 11:51
 * @className:  PersonalDetailsActivity
 * @Description: 个人信息页面
 */

public class PersonalDetailsActivity extends BaseActivity implements View.OnClickListener{
    //上下文对象
    private Activity context;

    //裁剪后的图片
    private String cropHeadPath;

    //头像的外部布局
    private PercentLinearLayout pll_head;
    //头像
    private CircleImageView civ_head;
    //姓名
    private TextView tv_name;
    //性别
    private TextView tv_sex;
    //学院
    private TextView tv_academy;
    //二维码
    private PercentLinearLayout pll_qr;
    //出生年月
    private TextView tv_birth_date;
    //政治面貌
    private TextView tv_politics_status;
    //手机号码的外部布局
    private PercentLinearLayout pll_photo;
    //手机号码
    private TextView tv_photo;
    //电子邮箱的外部布局
    private PercentLinearLayout pll_email;
    //电子邮箱
    private TextView tv_email;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_personal_details);
        context = this;
        TitleBar bar = getTitleBar();
        bar.setTitle(getResources().getString(R.string.str_personal_msg), R.color.color_white);
        bar.setBack();
        init();
}

    private void init(){
        pll_head = (PercentLinearLayout) findViewById(R.id.pll_head);
        pll_head.setOnClickListener(this);
        civ_head = (CircleImageView) findViewById(R.id.civ_head);
        tv_name = (TextView) findViewById(R.id.tv_name);
        tv_sex = (TextView) findViewById(R.id.tv_sex);
        tv_academy = (TextView) findViewById(R.id.tv_academy);
        pll_qr = (PercentLinearLayout) findViewById(R.id.pll_qr);
        pll_qr.setOnClickListener(this);
        tv_birth_date = (TextView) findViewById(R.id.tv_birth_date);
        tv_politics_status = (TextView) findViewById(R.id.tv_politics_status);
        pll_photo = (PercentLinearLayout) findViewById(R.id.pll_photo);
        pll_photo.setOnClickListener(this);
        tv_photo = (TextView) findViewById(R.id.tv_photo);
        pll_email = (PercentLinearLayout) findViewById(R.id.pll_email);
        pll_email.setOnClickListener(this);
        tv_email = (TextView) findViewById(R.id.tv_email);
    }

    /**
     *  修改信息
     * @param titile 弹窗标题
     * @param hint 弹窗提示
     * @param tag 弹窗标记，1是修改手机号，2是修改邮箱
     */
    public void uploadMsg(String titile, String hint, int tag) {
        final CommonDialog dialog = new CommonDialog(context);
        final View view = LayoutInflater.from(context).inflate(R.layout.dialog_layout, (ViewGroup) null);
        final EditText et = (EditText) view.findViewById(R.id.info);
        et.setHint(hint);
        dialog.addView(view);
        dialog.setTitle(titile);
        dialog.setPositiveButtonOpen(new CommonDialog.BtnClickedListener() {
            @Override
            public void onBtnClicked() {

            }
        }, ResourceUtil.getString(context, R.string.str_ok));
        dialog.setCancleButton(null, ResourceUtil.getString(context, R.string.str_cancle));
        dialog.showDialog();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        String takePhotoPath= MediaPickerActivity.getPath(data);
        if (requestCode == SelectPhotoAlbumUtils.ACTION_SHARE_FROM_ALBUM){
            //获取选择的图片
            if (!TextUtils.isEmpty(takePhotoPath)){
                //相册中拍照后返回
                File filePath = new File(takePhotoPath);
                cropHeadPath = LoaderImageUtil.cropPhoto(Uri.fromFile(filePath),filePath.getAbsolutePath(), context);
            }else if (data!=null){
                //相册中选择图片后返回
                List<String> selectedPicture = SelectPhotoAlbumUtils.getSelectPath(context, data);
                if (selectedPicture != null && selectedPicture.size() > 0) {
                    //截图处理
                    File filePath = new File(selectedPicture.get(0));
                    cropHeadPath = LoaderImageUtil.cropPhoto(Uri.fromFile(filePath),filePath.getAbsolutePath(), context);
                }
            }
        }else if (requestCode == SelectPhotoAlbumUtils.CROP_PHOTO_REQUEST_CODE){
            // 上传裁剪后的图片
            if (!TextUtils.isEmpty(cropHeadPath)) {
                File file = new File(cropHeadPath);
                if (file.exists() && file.length() > 0) {
                    String absolutePath = file.getAbsolutePath();
                }
            }
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.pll_head:
                //点击更改头像
                SelectPhotoAlbumUtils.selectPhoto(context, SelectPhotoAlbumUtils.ACTION_SHARE_FROM_ALBUM, 1);
                break;
            case R.id.pll_qr:
                Intent intent = new Intent(context, ActMyQr.class);
                startActivity(intent);
                break;
            case R.id.pll_photo:
                //点击更改手机号
                uploadMsg(ResourceUtil.getString(context, R.string.str_update_phone), ResourceUtil.getString(context, R.string.str_write_phone), 1);
                break;
            case R.id.pll_email:
                //点击更改手机号
                uploadMsg(ResourceUtil.getString(context, R.string.str_update_email), ResourceUtil.getString(context, R.string.str_write_email), 2);
                break;
        }
    }
}
