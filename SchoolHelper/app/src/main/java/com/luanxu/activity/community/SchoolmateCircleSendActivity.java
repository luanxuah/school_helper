package com.luanxu.activity.community;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.Toast;

import com.luanxu.adapter.community.SchoolmateCircleSendAdapter;
import com.luanxu.base.BaseActivity;
import com.luanxu.bean.PreviewedImageInfo;
import com.luanxu.custom.TitleBar;
import com.luanxu.custom.album.MediaPickerActivity;
import com.luanxu.custom.album.SelectPhotoAlbumUtils;
import com.luanxu.schoolhelper.R;
import com.luanxu.utils.LoaderImageUtil;
import com.luanxu.utils.LogUtil;
import com.luanxu.utils.ResourceUtil;
import com.luanxu.utils.ToastUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * @author: LuanXu
 * @createTime:2017/3/16 17:52
 * @className:  SchoolmateCircleSendActivity
 * @Description: 校友圈的发送页面
 */

public class SchoolmateCircleSendActivity extends BaseActivity{
    //上下文对象
    private Activity context;

    //发送框
    private EditText et_share_content;
    //图片
    private GridView gv_imgs;

    //图片适配器
    private SchoolmateCircleSendAdapter adapter;

    //分享文件本地和网络路径
    private ArrayList<String> mImgPathLists;
    //纠正图片方向后的图片地址（用于上传）
    private ArrayList<String> mRotatePathLists;
    // 当现实更多加号的标识
    private String mPlusFlag = "LuanXu";
    // Gridview当中最多显示的列数
    private static final int GRID_COLUMN_COUNT = 4;
    // 相片的最大数目
    private static final int MAX_IMG_COUNTS = 9;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_schoolmate_circle_send);
        context = this;
        TitleBar bar = getTitleBar();
        bar.setTitle(getResources().getString(R.string.str_send_schoolmate_circle), R.color.color_white);
        bar.setBack();
        bar.enableRightBtn(ResourceUtil.getString(context, R.string.str_release), -1, new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        init();
    }

    private void init(){
        mRotatePathLists = new ArrayList<String>();
        mImgPathLists = new ArrayList<String>();
        mImgPathLists.add(mPlusFlag);

        et_share_content = (EditText) findViewById(R.id.et_share_content);
        gv_imgs = (GridView) findViewById(R.id.gv_imgs);
        adapter = new SchoolmateCircleSendAdapter(context, mImgPathLists);
        gv_imgs.setAdapter(adapter);
        gv_imgs.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                // 用来展示图片用的集合
                ArrayList<String> imgShowPaths = new ArrayList<String>();
                //进入图片预览所需的实体
                PreviewedImageInfo previewedImageInfo = new PreviewedImageInfo();
                previewedImageInfo.setImgUrls(imgShowPaths);
                previewedImageInfo.setPosition(position);
                previewedImageInfo.setDefaultImgRes(R.mipmap.empty_photo);
//                previewedImageInfo.setCoverPartyId(LoginPreference.getUserInfo().partyId);
                //不显示长按弹窗
                previewedImageInfo.setSourceForPhoto(true);
                if (mImgPathLists.size() > MAX_IMG_COUNTS) {
                    // 此时分享的图片已经有9张预览图片 去除多余的图片
                    mImgPathLists.remove(MAX_IMG_COUNTS);
                    mRotatePathLists.remove(MAX_IMG_COUNTS);
                    for(int i = 0;i < MAX_IMG_COUNTS;i ++) {
                        imgShowPaths.add(mImgPathLists.get(i));
                    }
                    LoaderImageUtil.previewLargePic(previewedImageInfo,context);
                    // 恢复原来集合的数据
                    mImgPathLists.add(mPlusFlag);
                    mRotatePathLists.add(mPlusFlag);
                }else {
                    // 含有“+”添加按钮
                    if (position != mImgPathLists.size() - 1) {
                        // 预览图片
                        for(int i = 0;i < mImgPathLists.size() - 1;i ++) {
                            imgShowPaths.add(mImgPathLists.get(i));
                        }
                        LoaderImageUtil.previewLargePic(previewedImageInfo,context);
                    }else {
//                        showCasePopWindow();
                        //选择相册
                        SelectPhotoAlbumUtils.selectPhoto(context, SelectPhotoAlbumUtils.ACTION_SHARE_FROM_ALBUM,MAX_IMG_COUNTS + 1 - mImgPathLists.size());
                    }
                }
            }
        });
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
            adapter = new SchoolmateCircleSendAdapter(context, mImgPathLists);
            gv_imgs.setAdapter(adapter);
        }else {
            adapter.setDate(mImgPathLists);
        }
    }
}
