package com.meishe.sdkdemo.edit.data;

/**
 * Created by CaoZhiChao on 2018/5/29 15:35
 */
public class AssetInfoDescription {
    public String mAssetName;
    public int mImageId;

    public AssetInfoDescription(String mAssetName, int mImageId) {
        this.mAssetName = mAssetName;
        this.mImageId = mImageId;
    }

    public AssetInfoDescription() {
    }

    public String getAssetName() {
        return mAssetName == null ? "" : mAssetName;
    }

    public void setAssetName(String mAssetName) {
        this.mAssetName = mAssetName == null ? "" : mAssetName;
    }

    public int getImageId() {
        return mImageId;
    }

    public void setImageId(int mImageId) {
        this.mImageId = mImageId;
    }
}

