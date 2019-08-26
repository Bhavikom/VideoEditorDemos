package com.meishe.sdkdemo.utils.dataInfo;

import android.graphics.PointF;

import java.util.ArrayList;

/**
 * Created by chengzhonglin on 2019/06/11.
 */

public class CompoundCaptionInfo {
    private float m_scaleFactorX;//X缩放因子
    private float m_scaleFactorY;//Y缩放因子
    private PointF m_anchor;//锚点
    private PointF m_translation;//字幕偏移量
    private float m_rotation;//旋转角度

    //组合字幕样式属性
    private long m_inPoint;//字幕入点
    private long m_outPoint;//字幕出点
    private String m_captionStyleUuid;//字幕样式Uuid
    private int m_captionZVal;//字幕Z值
    private ArrayList<CompoundCaptionAttr> m_captionAttributeList;//字幕属性列表

    public int getCaptionZVal() {
        return m_captionZVal;
    }

    public void setCaptionZVal(int m_captionZVal) {
        this.m_captionZVal = m_captionZVal;
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
    public String getCaptionStyleUuid() {
        return m_captionStyleUuid;
    }

    public void setCaptionStyleUuid(String captionStyleUuid) {
        this.m_captionStyleUuid = captionStyleUuid;
    }

    public ArrayList<CompoundCaptionAttr> getCaptionAttributeList() {
        return m_captionAttributeList;
    }
    public void addCaptionAttributeList(CompoundCaptionAttr captionAttribute) {
        this.m_captionAttributeList.add(captionAttribute);
    }

    public CompoundCaptionInfo() {
        m_scaleFactorX = 1.0f;
        m_scaleFactorY = 1.0f;
        m_anchor = null;
        m_rotation = 0;
        m_translation = null;
        m_inPoint = 0;
        m_outPoint = 0;
        m_captionStyleUuid = "";
        m_captionZVal = 0;
        m_captionAttributeList = new ArrayList<>();
    }

    public void setScaleFactorX(float value) {
        m_scaleFactorX = value;
    }

    public float getScaleFactorX() {
        return m_scaleFactorX;
    }
    public void setScaleFactorY(float value) {
        m_scaleFactorY = value;
    }

    public float getScaleFactorY() {
        return m_scaleFactorY;
    }

    public void setAnchor(PointF point) {
        m_anchor = point;
    }

    public PointF getAnchor() {
        return m_anchor;
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

    public CompoundCaptionInfo clone(){
        CompoundCaptionInfo newCaptionInfo = new CompoundCaptionInfo();
        newCaptionInfo.setAnchor(this.getAnchor());
        newCaptionInfo.setRotation(this.getRotation());
        newCaptionInfo.setScaleFactorX(this.getScaleFactorX());
        newCaptionInfo.setScaleFactorY(this.getScaleFactorY());
        newCaptionInfo.setTranslation(this.getTranslation());

        //copy data
        newCaptionInfo.setInPoint(this.getInPoint());
        newCaptionInfo.setOutPoint(this.getOutPoint());
        newCaptionInfo.setCaptionZVal(this.getCaptionZVal());
        newCaptionInfo.setCaptionStyleUuid(this.getCaptionStyleUuid());

        ArrayList<CompoundCaptionAttr> captionAttrList = this.getCaptionAttributeList();
        if(captionAttrList != null){
            int compoundAttrCount = captionAttrList.size();
            for (int index = 0;index < compoundAttrCount;++index){
                newCaptionInfo.addCaptionAttributeList(captionAttrList.get(index));
            }
        }

        return newCaptionInfo;
    }

    public static class CompoundCaptionAttr{
        public String getCaptionFontName() {
            return captionFontName;
        }

        public void setCaptionFontName(String captionFontName) {
            this.captionFontName = captionFontName;
        }

        public String getCaptionColor() {
            return captionColor;
        }

        public void setCaptionColor(String captionColor) {
            this.captionColor = captionColor;
        }

        public String getCaptionText() {
            return captionText;
        }

        public void setCaptionText(String captionText) {
            this.captionText = captionText;
        }
        public CompoundCaptionAttr clone(){
            CompoundCaptionAttr newACaptionAttr = new CompoundCaptionAttr();
            newACaptionAttr.setCaptionColor(this.getCaptionColor());
            newACaptionAttr.setCaptionFontName(this.getCaptionFontName());
            newACaptionAttr.setCaptionText(this.getCaptionText());
            return newACaptionAttr;
        }
        private String captionFontName;
        private String captionColor;
        private String captionText;
    }
}
