package com.meishe.sdkdemo.utils;

import com.meicam.sdk.NvsStreamingContext;
import com.meishe.sdkdemo.ParameterSettingActivity;

import java.io.Serializable;

/**
 * Created by admin on 2018-5-28.
 */

public class ParameterSettingValues implements Serializable {
    private static ParameterSettingValues parameterValues;
    private int m_captureResolutionGrade;
    private int m_compileVideoRes;
    private double m_compileBitrate;
    private boolean m_disableDeviceEncorder;
    private boolean m_isUseBackgroudBlur;

    public int getCompileVideoRes() {
        return m_compileVideoRes;
    }

    public void setCompileVideoRes(int compileVideoRes) {
        this.m_compileVideoRes = compileVideoRes;
    }

    public ParameterSettingValues() {
        m_captureResolutionGrade = NvsStreamingContext.VIDEO_CAPTURE_RESOLUTION_GRADE_HIGH;
        m_compileVideoRes = ParameterSettingActivity.CompileVideoRes_720;
        m_compileBitrate = 0;
        m_disableDeviceEncorder = false;
        m_isUseBackgroudBlur = false;
    }

    public static ParameterSettingValues instance() {
        if (parameterValues == null) {
            parameterValues = init();
        }
        return getParameterValues();
    }

    public static ParameterSettingValues init() {
        if (parameterValues == null) {
            synchronized (ParameterSettingValues.class) {
                if (parameterValues == null) {
                    parameterValues = new ParameterSettingValues();
                }
            }
        }
        return parameterValues;
    }
   
    public static void setParameterValues(ParameterSettingValues values) {
        parameterValues = values;
    }

    public static ParameterSettingValues getParameterValues() {
        return parameterValues;
    }

    public int getCaptureResolutionGrade() {
        return m_captureResolutionGrade;
    }

    public void setCaptureResolutionGrade(int captureRatio) {
        this.m_captureResolutionGrade = captureRatio;
    }

    public double getCompileBitrate() {
        return m_compileBitrate;
    }

    public void setCompileBitrate(double bitrate) {
        this.m_compileBitrate = bitrate;
    }

    public boolean disableDeviceEncorder() {
        return m_disableDeviceEncorder;
    }

    public void setDisableDeviceEncorder(boolean useDeviceEncorder) {
        m_disableDeviceEncorder = useDeviceEncorder;
    }

    public boolean isUseBackgroudBlur() {
        return m_isUseBackgroudBlur;
    }

    public void setUseBackgroudBlur(boolean useBackgroudBlur) {
        m_isUseBackgroudBlur = useBackgroudBlur;
    }
}
