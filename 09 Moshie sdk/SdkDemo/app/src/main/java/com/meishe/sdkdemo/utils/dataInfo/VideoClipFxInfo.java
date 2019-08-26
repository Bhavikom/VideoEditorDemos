package com.meishe.sdkdemo.utils.dataInfo;

import android.util.Log;

public class VideoClipFxInfo {
    public static int FXMODE_BUILTIN = 0;
    public static int FXMODE_PACKAGE = 1;

    private int m_fxMode;
    private String m_fxId;
    private float m_fxIntensity;

    // 用于特殊的滤镜
    private boolean m_isCartoon = false;
    private boolean m_isStrokenOnly = true;
    private boolean m_isGrayScale = true;

    public VideoClipFxInfo() {
        m_fxId = null;
        m_fxMode = FXMODE_BUILTIN;
        m_fxIntensity = 1.0f;
    }

    public void setFxMode(int mode) {
        if(mode != FXMODE_BUILTIN && mode != FXMODE_PACKAGE) {
            Log.e("", "invalid mode data");
            return;
        }
        m_fxMode = mode;
    }

    public int getFxMode() {
        return m_fxMode;
    }

    public void setFxId(String fxId) {
        m_fxId = fxId;
    }

    public String getFxId() {
        return m_fxId;
    }

    public void setFxIntensity(float intensity) {
        m_fxIntensity = intensity;
    }

    public float getFxIntensity() {
        return m_fxIntensity;
    }
    public void setStrokenOnly(boolean strokenOnly){
        m_isStrokenOnly = strokenOnly;
    }

    public boolean getStrokenOnly(){
        return m_isStrokenOnly;
    }

    public void setGrayScale(boolean grayScale){
        m_isGrayScale = grayScale;
    }

    public boolean getGrayScale(){
        return m_isGrayScale;
    }

    public void setIsCartoon(boolean isCartoon){
        m_isCartoon = isCartoon;
    }

    public boolean getIsCartoon(){
        return m_isCartoon;
    }
}
