package com.luanxu.activity.community;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import com.luanxu.adapter.community.LostAndFoundSendAdapter;
import com.luanxu.application.SchoolHelperApplication;
import com.luanxu.base.BaseActivity;
import com.luanxu.custom.MyGridView;
import com.luanxu.custom.TitleBar;
import com.luanxu.custom.album.MediaPickerActivity;
import com.luanxu.custom.album.SelectPhotoAlbumUtils;
import com.luanxu.schoolhelper.R;
import com.luanxu.utils.LogUtil;
import com.luanxu.utils.ResourceUtil;
import com.luanxu.utils.ToastUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * @author: LuanXu
 * @createTime:2017/4/12 18:31
 * @className:  LostAndFoundSendActivity
 * @Description: 失物招领的发表页面
 */

public class LostAndFoundSendActivity extends BaseActivity{
    //上下文对象
    private Activity context;

    //图片适配器
    private LostAndFoundSendAdapter adapter;

    //物品描述
    private EditText et_describe;
    //时间
    private EditText et_time;
    //地点
    private EditText et_site;
    //失物按钮
    private RadioButton rb_lost;
    //招领按钮
    private RadioButton rb_found;
    //图片
    private MyGridView gv_imgs;

    //分享文件本地和网络路径
    public ArrayList<String> mImgPathLists;
    //纠正图片方向后的图片地址（用于上传）
    public ArrayList<String> mRotatePathLists;
    // 当现实更多加号的标识
    public String mPlusFlag = "LuanXu";
    // Gridview当中最多显示的列数
    private static final int GRID_COLUMN_COUNT = 4;
    // 相片的最大数目
    private static final int MAX_IMG_COUNTS = 9;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_lost_found_send);
        SchoolHelperApplication.getInstance().addActivity(this);
        context = this;

        TitleBar bar = getTitleBar();
        bar.setTitle(R.string.str_send_lost_found, R.color.color_white);
        bar.setBack();
        bar.enableRightBtn(ResourceUtil.getString(context, R.string.str_release), -1, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        init();
    }

    //初始化数据和控件
    private void init() {
        mRotatePathLists = new ArrayList<String>();
        mImgPathLists = new ArrayList<String>();
        mImgPathLists.add(mPlusFlag);

        et_describe = (EditText) findViewById(R.id.et_describe);
        et_time = (EditText) findViewById(R.id.et_time);
        et_site = (EditText) findViewById(R.id.et_site);
        rb_lost = (RadioButton) findViewById(R.id.rb_lost);
        rb_found = (RadioButton) findViewById(R.id.rb_found);
        gv_imgs = (MyGridView) findViewById(R.id.gv_imgs);

        adapter = new LostAndFoundSendAdapter(this, mImgPathLists);
        gv_imgs.setAdapter(adapter);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // 选中的图片集合
        List<String> selectedPicture = null;
        List<String> rotatePicture = null;
        if (resultCode == Activity.RESULT_OK  && requestCode == SelectPhotoAlbumUtils.ACTION_SHARE_FROM_ALBUM) {
            String takePhotoPath= MediaPickerActivity.getPath(data);
            if (!TextUtils.isEmpty(takePhotoPath)){
                //相册中拍照后返回
                selectedPicture = new ArrayList<String>();
                rotatePicture = new ArrayList<String>();
                selectedPicture.add(takePhotoPath);
                rotatePicture.add(SelectPhotoAlbumUtils.getRotatePhotoUrl());
                LogUtil.i("doctorlog    path1="+takePhotoPath+"         path2="+SelectPhotoAlbumUtils.getRotatePhotoUrl());
            }else if (data!=null){
                selectedPicture = SelectPhotoAlbumUtils.getSelectPath(context, data);
                rotatePicture = SelectPhotoAlbumUtils.getSelectPath(context, data);
            }
            if (selectedPicture != null && selectedPicture.size() > 0) {
                if (mImgPathLists.size() == 1) {
                    mImgPathLists.clear();
                    mRotatePathLists.clear();
                    mImgPathLists.addAll(selectedPicture);
                    mRotatePathLists.addAll(rotatePicture);
                }else {
                    mImgPathLists.remove(mImgPathLists.indexOf(mPlusFlag));
                    mRotatePathLists.remove(mRotatePathLists.indexOf(mPlusFlag));
                    for (int i = 0; i < selectedPicture.size(); i++) {
                        if(!mImgPathLists.contains(selectedPicture.get(i))) {
                            mImgPathLists.add(selectedPicture.get(i));
                        }else{
                            ToastUtil.show(context, R.string.str_not_add_same_pic, Toast.LENGTH_SHORT);
                        }
                        if(!mRotatePathLists.contains(rotatePicture.get(i))) {
                            mRotatePathLists.add(rotatePicture.get(i));
                        }
                    }
                }
            }
            mImgPathLists.add(mPlusFlag);
            mRotatePathLists.add(mPlusFlag);
            updateFrame();
        }
    }

    /**
     * 刷新界面
     */
    private void updateFrame() {
        if (null == adapter) {
            adapter = new LostAndFoundSendAdapter(this, mImgPathLists);
            gv_imgs.setAdapter(adapter);
        }else {
            adapter.setDate(mImgPathLists);
        }
    }



}
