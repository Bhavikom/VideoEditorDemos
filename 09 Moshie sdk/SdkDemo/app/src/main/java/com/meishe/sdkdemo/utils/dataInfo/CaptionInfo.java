package com.meishe.sdkdemo.utils.dataInfo;

import android.graphics.PointF;

/**
 * Created by liuluwei on 2017/12/9.
 */

public class CaptionInfo {
    public static int ATTRIBUTE_UNUSED_FLAG = 0;//表示未使用过某一属性
    public static int ATTRIBUTE_USED_FLAG = 1;//表示已经使用过某一属性

    private String m_text;//字幕文本内容
    private float m_scaleFactorX;//X缩放因子
    private float m_scaleFactorY;//Y缩放因子
    private PointF m_anchor;//锚点
    private PointF m_translation;//字幕偏移量
    private float m_rotation;//旋转角度

    //字幕样式属性
    private int m_alignVal;//字幕对齐值
    private long m_inPoint;//字幕入点
    private long m_outPoint;//字幕出点
    private String m_captionStyleUuid;//字幕样式Uuid
    private String m_captionColor;//字幕颜色
    private int m_captionColorAlpha;//字幕颜色不透明度
    private boolean m_hasOutline;//是否有描边
    private String m_outlineColor;//描边颜色
    private int m_outlineColorAlpha;//描边颜色不透明度
    private float m_outlineWidth;//描边宽度
    private String m_captionFont;//字幕字体
    private boolean m_isBold;//是否加粗
    private boolean m_isItalic;//是否是斜体
    private boolean m_isShadow;//是否有阴影
    private float m_captionSize;//字体大小
    private int m_captionZVal;//字幕Z值

    //控制标识
    private int m_usedTranslationFlag = ATTRIBUTE_UNUSED_FLAG;//使用偏移标识
    private int m_usedScaleRotationFlag = ATTRIBUTE_UNUSED_FLAG;//使用缩放旋转标识
    private int m_usedIsBoldFlag = ATTRIBUTE_UNUSED_FLAG;//使用加粗标识
    private int m_usedIsItalicFlag = ATTRIBUTE_UNUSED_FLAG;//使用斜体标识
    private int m_usedShadowFlag = ATTRIBUTE_UNUSED_FLAG;//使用阴影标识
    private int m_usedColorFlag = ATTRIBUTE_UNUSED_FLAG;//使用颜色标识
    private int m_usedOutlineFlag = ATTRIBUTE_UNUSED_FLAG;//使用描边标识

    public int getUsedTranslationFlag() {
        return m_usedTranslationFlag;
    }

    public void setUsedTranslationFlag(int usedTranslationFlag) {
        this.m_usedTranslationFlag = usedTranslationFlag;
    }

    public int getUsedScaleRotationFlag() {
        return m_usedScaleRotationFlag;
    }

    public void setUsedScaleRotationFlag(int usedScaleRotationFlag) {
        this.m_usedScaleRotationFlag = usedScaleRotationFlag;
    }

    public int getUsedIsBoldFlag() {
        return m_usedIsBoldFlag;
    }

    public void setUsedIsBoldFlag(int usedIsBoldFlag) {
        this.m_usedIsBoldFlag = usedIsBoldFlag;
    }

    public int getUsedIsItalicFlag() {
        return m_usedIsItalicFlag;
    }

    public void setUsedIsItalicFlag(int usedIsItalicFlag) {
        this.m_usedIsItalicFlag = usedIsItalicFlag;
    }

    public int getUsedShadowFlag() {
        return m_usedShadowFlag;
    }

    public void setUsedShadowFlag(int usedShadowFlag) {
        this.m_usedShadowFlag = usedShadowFlag;
    }

    public int getUsedColorFlag() {
        return m_usedColorFlag;
    }

    public void setUsedColorFlag(int usedColorFlag) {
        this.m_usedColorFlag = usedColorFlag;
    }
    public int getUsedOutlineFlag() {
        return m_usedOutlineFlag;
    }

    public void setUsedOutlineFlag(int usedOutlineFlag) {
        this.m_usedOutlineFlag = usedOutlineFlag;
    }
    /////////////////////////////////////////////////////////

    public int getAlignVal() {
        return m_alignVal;
    }

    public void setAlignVal(int alignVal) {
        this.m_alignVal = alignVal;
    }
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

    public String getCaptionColor() {
        return m_captionColor;
    }

    public void setCaptionColor(String captionColor) {
        this.m_captionColor = captionColor;
    }

    public int getCaptionColorAlpha() {
        return m_captionColorAlpha;
    }

    public void setCaptionColorAlpha(int captionColorAlpha) {
        this.m_captionColorAlpha = captionColorAlpha;
    }

    public boolean isHasOutline() {
        return m_hasOutline;
    }

    public void setHasOutline(boolean hasOutline) {
        this.m_hasOutline = hasOutline;
    }

    public String getOutlineColor() {
        return m_outlineColor;
    }

    public void setOutlineColor(String outlineColor) {
        this.m_outlineColor = outlineColor;
    }

    public int getOutlineColorAlpha() {
        return m_outlineColorAlpha;
    }

    public void setOutlineColorAlpha(int outlineColorAlpha) {
        this.m_outlineColorAlpha = outlineColorAlpha;
    }

    public float getOutlineWidth() {
        return m_outlineWidth;
    }

    public void setOutlineWidth(float outlineWidth) {
        this.m_outlineWidth = outlineWidth;
    }

    public String getCaptionFont() {
        return m_captionFont;
    }

    public void setCaptionFont(String captionFont) {
        this.m_captionFont = captionFont;
    }

    public boolean isBold() {
        return m_isBold;
    }

    public void setBold(boolean isBold) {
        this.m_isBold = isBold;
    }

    public boolean isItalic() {
        return m_isItalic;
    }

    public void setItalic(boolean isItalic) {
        this.m_isItalic = isItalic;
    }

    public boolean isShadow() {
        return m_isShadow;
    }

    public void setShadow(boolean isShadow) {
        this.m_isShadow = isShadow;
    }

    public float getCaptionSize() {
        return m_captionSize;
    }

    public void setCaptionSize(float captionSize) {
        this.m_captionSize = captionSize;
    }

    public CaptionInfo() {
        m_text = null;
        m_scaleFactorX = 1.0f;
        m_scaleFactorY = 1.0f;
        m_anchor = null;
        m_rotation = 0;
        m_translation = null;
        m_alignVal = -1;
        m_inPoint = 0;
        m_outPoint = 0;
        m_captionStyleUuid = "";
        m_captionColor = "";
        m_captionColorAlpha = 100;
        m_hasOutline = false;
        m_outlineColor = "";
        m_outlineColorAlpha = 100;
        m_outlineWidth = 8.0f;
        m_captionFont = "";
        m_isBold = true;
        m_isItalic = false;
        m_isShadow = false;
        m_captionSize = -1;
        m_captionZVal = 0;
    }

    public void setText(String text) {
        m_text = text;
    }

    public String getText() {
        return m_text;
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

    public CaptionInfo clone(){
        CaptionInfo newCaptionInfo = new CaptionInfo();
        newCaptionInfo.setText(this.getText());
        newCaptionInfo.setCaptionColor(this.getCaptionColor());
        newCaptionInfo.setAnchor(this.getAnchor());
        newCaptionInfo.setRotation(this.getRotation());
        newCaptionInfo.setScaleFactorX(this.getScaleFactorX());
        newCaptionInfo.setScaleFactorY(this.getScaleFactorY());
        newCaptionInfo.setTranslation(this.getTranslation());
        //copy data
        newCaptionInfo.setAlignVal(this.getAlignVal());
        newCaptionInfo.setInPoint(this.getInPoint());
        newCaptionInfo.setOutPoint(this.getOutPoint());
        newCaptionInfo.setCaptionZVal(this.getCaptionZVal());
        newCaptionInfo.setCaptionStyleUuid(this.getCaptionStyleUuid());
        newCaptionInfo.setCaptionColor(this.getCaptionColor());
        newCaptionInfo.setCaptionColorAlpha(this.getCaptionColorAlpha());
        newCaptionInfo.setHasOutline(this.isHasOutline());
        newCaptionInfo.setOutlineColor(this.getOutlineColor());
        newCaptionInfo.setOutlineColorAlpha(this.getOutlineColorAlpha());
        newCaptionInfo.setOutlineWidth(this.getOutlineWidth());
        newCaptionInfo.setCaptionFont(this.getCaptionFont());
        newCaptionInfo.setBold(this.isBold());
        newCaptionInfo.setItalic(this.isItalic());
        newCaptionInfo.setShadow(this.isShadow());
        newCaptionInfo.setCaptionSize(this.getCaptionSize());

        //控制标识
        newCaptionInfo.setUsedTranslationFlag(this.getUsedTranslationFlag());
        newCaptionInfo.setUsedScaleRotationFlag(this.getUsedScaleRotationFlag());
        newCaptionInfo.setUsedIsBoldFlag(this.getUsedIsBoldFlag());
        newCaptionInfo.setUsedIsItalicFlag(this.getUsedIsItalicFlag());
        newCaptionInfo.setUsedShadowFlag(this.getUsedShadowFlag());
        newCaptionInfo.setUsedColorFlag(this.getUsedColorFlag());
        newCaptionInfo.setUsedOutlineFlag(this.getUsedOutlineFlag());
        return newCaptionInfo;
    }
}
