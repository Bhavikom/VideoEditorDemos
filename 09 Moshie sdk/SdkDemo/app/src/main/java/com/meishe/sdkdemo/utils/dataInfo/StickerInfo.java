package com.meishe.sdkdemo.utils.dataInfo;

import android.graphics.Bitmap;
import android.graphics.PointF;

public class StickerInfo {
    private String m_id;
    private String m_imagePath;
    private String m_fileUrl;
    private Bitmap m_image;
    private String m_packagePath;
    private float m_scaleFactor;
    private float m_rotation;
    private PointF m_translation;
    private int m_animateStickerZVal;
    private boolean m_horizFlip;
    private long m_inPoint;
    private long m_outPoint;
    private float m_volumeGain;

    private boolean isCustomSticker;//自定义贴纸使用
    private String m_customImagePath;//自定义贴纸图片路径

    public String getCustomImagePath() {
        return m_customImagePath;
    }

    public void setCustomImagePath(String customImagePath) {
        this.m_customImagePath = customImagePath;
    }

    public boolean isCustomSticker() {
        return isCustomSticker;
    }
    public void setCustomSticker(boolean customSticker) {
        isCustomSticker = customSticker;
    }

    public float getVolumeGain() {
        return m_volumeGain;
    }

    public void setVolumeGain(float volumeGain) {
        this.m_volumeGain = volumeGain;
    }
    public long getInPoint() {
        return m_inPoint;
    }

    public void setInPoint(long inPoint) {
        this.m_inPoint = inPoint;
    }

    public long getOutPoint() {
        return m_outPoint;
    }

    public void setOutPoint(long outPoint) {
        this.m_outPoint = outPoint;
    }

    public StickerInfo() {
        m_scaleFactor = 1;
        m_rotation = 0;
        m_translation = null;
        m_imagePath = null;
        m_fileUrl = null;
        m_image = null;
        m_packagePath = null;
        m_animateStickerZVal = 0;
        m_horizFlip = false;
        m_volumeGain = 1.0f;
        isCustomSticker = false;
        m_customImagePath = "";
    }

    public boolean isHorizFlip() {
        return m_horizFlip;
    }

    public void setHorizFlip(boolean horizFlip) {
        this.m_horizFlip = horizFlip;
    }

    public int getAnimateStickerZVal() {
        return m_animateStickerZVal;
    }

    public void setAnimateStickerZVal(int animateStickerZVal) {
        this.m_animateStickerZVal = animateStickerZVal;
    }
    public void setId(String id) {
        m_id = id;
    }

    public String getId() {
        return m_id;
    }

    public void setPackagePath(String path) {
        m_packagePath = path;
    }

    public String getPackagePath() {
        return m_packagePath;
    }

    public void setImagePath(String filePath) {
        m_imagePath = filePath;
    }

    public String getImagePath() {
        return m_imagePath;
    }

    public void setFileUrl(String url) {
        m_fileUrl = url;
    }

    public String getFileUrl() {
        return m_fileUrl;
    }

    public void setImage(Bitmap bitmap) {
        m_image = bitmap;
    }

    public Bitmap getImage() {
        return m_image;
    }

    public void setScaleFactor(float value) {
        m_scaleFactor = value;
    }

    public float getScaleFactor() {
        return m_scaleFactor;
    }

    public void setRotation(float value) {
        m_rotation = value;
    }

    public float getRotation() {
        return m_rotation;
    }

    public void setTranslation(PointF point) {
        m_translation = point;
    }

    public PointF getTranslation() {
        return m_translation;
    }

    public StickerInfo clone(){
        StickerInfo newStickerInfo = new StickerInfo();
        newStickerInfo.setId(this.getId());
        newStickerInfo.setFileUrl(this.getFileUrl());
        newStickerInfo.setImage(this.getImage());
        newStickerInfo.setImagePath(this.getImagePath());
        newStickerInfo.setPackagePath(this.getPackagePath());
        newStickerInfo.setRotation(this.getRotation());
        newStickerInfo.setScaleFactor(this.getScaleFactor());
        newStickerInfo.setTranslation(this.getTranslation());

        //copy data
        newStickerInfo.setAnimateStickerZVal(this.getAnimateStickerZVal());
        newStickerInfo.setInPoint(this.getInPoint());
        newStickerInfo.setOutPoint(this.getOutPoint());
        newStickerInfo.setHorizFlip(this.isHorizFlip());
        newStickerInfo.setVolumeGain(this.getVolumeGain());

        //自定义贴纸数据
        newStickerInfo.setCustomSticker(this.isCustomSticker());
        if(this.isCustomSticker()){
            newStickerInfo.setCustomImagePath(this.getCustomImagePath());
        }
        return newStickerInfo;
    }
}
