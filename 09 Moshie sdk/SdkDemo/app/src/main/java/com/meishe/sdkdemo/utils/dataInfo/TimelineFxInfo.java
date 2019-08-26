package com.meishe.sdkdemo.utils.dataInfo;

public class TimelineFxInfo {
    public static final int TIMEMODE_NONE = 0;
    public static final int TIMEMODE_REPEAT = 1;
    public static final int TIMEMODE_SLOW = 2;
    public static final int TIMEMODE_REVERSE = 3;

    private int m_fxmode;
    private float m_fxPosition;

    public TimelineFxInfo() {
        m_fxmode = TIMEMODE_NONE;
        m_fxPosition = -1;
    }

    public void setTimeFxMode(int fxMode) {
        m_fxmode = fxMode;
    }

    public int getTimeFxMode() {
        return m_fxmode;
    }

    public float getTimelineFxPosition() {
        return m_fxPosition;
    }

    public void setTimelineFxPosition(float outPoint) {
        m_fxPosition = outPoint;
    }
}
