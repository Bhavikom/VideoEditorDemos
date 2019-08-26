package com.meishe.sdkdemo;

import android.app.Application;
import android.content.Context;
import android.os.Build;

import com.meicam.sdk.NvsStreamingContext;
import com.meishe.sdkdemo.utils.Logger;
import com.meishe.sdkdemo.utils.asset.NvAssetManager;
import com.umeng.analytics.MobclickAgent;
import com.umeng.commonsdk.UMConfigure;

import java.lang.reflect.Field;
import java.lang.reflect.Method;


/**
 * Created by ${gexinyu} on 2018/5/24.
 */

public class MSApplication extends Application {
    private static Context mContext;

    public static Context getmContext() {
        return mContext;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Logger.e("MSApplication", "onCreate");
        mContext = getApplicationContext();
        //initialization
        String licensePath = "assets:/meishesdk.lic";
        NvsStreamingContext.init(mContext, licensePath, NvsStreamingContext.STREAMING_CONTEXT_FLAG_SUPPORT_4K_EDIT);
        NvAssetManager.init(mContext);

        //Friendship initializes the secret of Push push service, no empty
        UMConfigure.init(this, UMConfigure.DEVICE_TYPE_PHONE, null);
//       Whether the componentized Log output is off by default. And integration test is a switch, release to close
//        UMConfigure.setLogEnabled(true);
        // isEnable: false-Turn off error statistics；true-Turn on error statistics (default is on)
//        public static void setCatchUncaughtExceptions(boolean isEnable)
        //Scene type setting
        MobclickAgent.setScenarioType(mContext, MobclickAgent.EScenarioType.E_UM_NORMAL);
    }
}
