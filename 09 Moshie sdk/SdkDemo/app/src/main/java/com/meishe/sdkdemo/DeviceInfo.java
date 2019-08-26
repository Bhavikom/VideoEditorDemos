package com.meishe.sdkdemo;

import android.content.Context;

import com.umeng.commonsdk.statistics.common.DeviceConfig;

/**
 * Created by CaoZhiChao on 2018/12/26 13:52
 */
public class DeviceInfo {
    public static String[] getTestDeviceInfo(Context context) {
        String[] deviceInfo = new String[2];
        try {
            if (context != null) {
                deviceInfo[0] = DeviceConfig.getDeviceIdForGeneral(context);
                deviceInfo[1] = DeviceConfig.getMac(context);
            }
        } catch (Exception e) {
        }
        return deviceInfo;
    }
}
