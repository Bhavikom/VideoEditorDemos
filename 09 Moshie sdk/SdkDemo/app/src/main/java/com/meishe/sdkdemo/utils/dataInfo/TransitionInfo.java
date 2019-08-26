package com.meishe.sdkdemo.utils.dataInfo;

import android.util.Log;

public class TransitionInfo {
    public static int TRANSITIONMODE_BUILTIN = 0;
    public static int TRANSITIONMODE_PACKAGE = 1;

    private int m_transitionMode;
    private String m_transitionId;

    public TransitionInfo() {
        m_transitionId = "Fade";
        m_transitionMode = TRANSITIONMODE_BUILTIN;
    }

    public void setTransitionMode(int mode) {
        if(mode != TRANSITIONMODE_BUILTIN && mode != TRANSITIONMODE_PACKAGE) {
            Log.e("", "invalid mode data");
            return;
        }
        m_transitionMode = mode;
    }

    public int getTransitionMode() {
        return m_transitionMode;
    }

    public void setTransitionId(String fxId) {
        m_transitionId = fxId;
    }

    public String getTransitionId() {
        return m_transitionId;
    }
}
