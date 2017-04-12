package com.luanxu.bean;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * @author: LuanXu
 * @createTime:2017/3/22 11:24
 * @className:  PreviewedImageInfo
 * @Description: 图片预览的实体类
 */

public class PreviewedImageInfo implements Serializable {
    // 图片路径集合
    private ArrayList<String> imgUrls;
    // 点击图片所在集合中的位置
    private int position;
    // 当没有网络图片时,显示的默认图片资源ID
    private int defaultImgRes;
    // 缩略图的类型
    private String imgType;
    // 对能够预览的图片都需要传相应的ID，转发和收藏用,传值的内容为当前登录人的ID或者图片所属人的ID
    private String coverPartyId;
    //是否是相册中的预览
    private boolean isSourceForPhoto;

    public PreviewedImageInfo() {}

    /**
     * 带参构造方法
     * @param imgUrls 图片路径集合
     * @param position 点击图片所在集合中的位置
     * @param defaultImgRes 显示的默认图片资源ID
     * @param imgType 缩略图的类型
     * @param coverPartyId 当前登录人的ID或者图片所属人的ID(必传)
     */
    public PreviewedImageInfo(ArrayList<String> imgUrls, int position, int defaultImgRes, String imgType, String coverPartyId) {
        this.imgUrls = imgUrls;
        this.position = position;
        this.defaultImgRes = defaultImgRes;
        this.imgType = imgType;
        this.coverPartyId = coverPartyId;
    }

    public boolean isSourceForPhoto() {
        return isSourceForPhoto;
    }

    public void setSourceForPhoto(boolean sourceForPhoto) {
        isSourceForPhoto = sourceForPhoto;
    }

    public ArrayList<String> getImgUrls() {
        return imgUrls;
    }

    public void setImgUrls(ArrayList<String> imgUrls) {
        this.imgUrls = imgUrls;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public int getDefaultImgRes() {
        return defaultImgRes;
    }

    public void setDefaultImgRes(int defaultImgRes) {
        this.defaultImgRes = defaultImgRes;
    }


    public String getImgType() {
        return imgType;
    }

    public void setImgType(String imgType) {
        this.imgType = imgType;
    }

    public String getCoverPartyId() {
        return coverPartyId;
    }

    public void setCoverPartyId(String coverPartyId) {
        this.coverPartyId = coverPartyId;
    }

    @Override
    public String toString() {
        return "PreviewedImageInfo{" +
                "imgUrls=" + imgUrls +
                ", position=" + position +
                ", defaultImgRes=" + defaultImgRes +
                ", imgType='" + imgType + '\'' +
                ", coverPartyId='" + coverPartyId + '\'' +
                '}';
    }
}
