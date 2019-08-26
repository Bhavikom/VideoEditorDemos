// 
// Decompiled by Procyon v0.5.36
// 

package com.yasoka.screenrecord.sdk.core.lasque.tusdk.core;

//import org.lasque.tusdk.core.utils.hardware.TuSdkStillCamera2;
//import org.lasque.tusdk.core.utils.hardware.TuSdkStillCamera;
//import org.lasque.tusdk.core.utils.hardware.Camera2Helper;
import android.os.Build;
//import org.lasque.tusdk.core.utils.hardware.TuSdkStillCameraInterface;
import android.widget.RelativeLayout;
//import org.lasque.tusdk.core.utils.hardware.CameraConfigs;
//import org.lasque.tusdk.impl.view.widget.TuMessageHubImpl;
import java.util.List;
import java.io.File;
//import org.lasque.tusdk.core.secret.LogStashManager;
//import org.lasque.tusdk.core.secret.StatisticsManger;
//import org.lasque.tusdk.modules.view.widget.smudge.BrushManager;
//import org.lasque.tusdk.modules.view.widget.sticker.StickerLocalPackage;
//import org.lasque.tusdk.core.seles.tusdk.FilterManager;
//import org.lasque.tusdk.core.utils.image.ImageLoaderHelper;
//import org.lasque.tusdk.core.network.TuSdkHttpEngine;
//import org.lasque.tusdk.core.seles.SelesContext;
//import org.lasque.tusdk.core.utils.TuSdkLocation;
//import org.lasque.tusdk.impl.TuSpecialScreenHelper;
//import org.lasque.tusdk.core.secret.SdkValid;
//import org.lasque.tusdk.core.utils.StringHelper;
//import org.lasque.tusdk.core.utils.TLog;
import android.content.Context;

import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.network.TuSdkHttpEngine;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.secret.LogStashManager;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.secret.SdkValid;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.secret.StatisticsManger;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.seles.SelesContext;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.seles.tusdk.FilterManager;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.StringHelper;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.TLog;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.TuSdkLocation;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.hardware.Camera2Helper;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.hardware.CameraConfigs;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.hardware.TuSdkStillCamera;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.hardware.TuSdkStillCamera2;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.hardware.TuSdkStillCameraInterface;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.image.ImageLoaderHelper;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.impl.TuSpecialScreenHelper;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.impl.view.widget.TuMessageHubImpl;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.impl.view.widget.TuMessageHubInterface;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.modules.view.widget.smudge.BrushManager;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.modules.view.widget.sticker.StickerLocalPackage;
//import org.lasque.tusdk.impl.view.widget.TuMessageHubInterface;

public class TuSdk
{
    public static final String SDK_VERSION = "3.1.1";
    public static final int SDK_CODE = 11;
    public static final String SDK_CONFIGS = "lsq_tusdk_configs.json";
    public static final String TEMP_DIR = "lasFilterTemp";
    public static final String SAMPLE_DIR = "lasFilterSamples";
    public static final String DOWNLOAD_DIR = "lasDownload";
    public static final String SAMPLE_EXTENSION = "lfs";
    private static TuSdk a;
    private TuMessageHubInterface b;
    private static Class<?> c;
    
    public static TuSdk shared() {
        return TuSdk.a;
    }
    
    public static TuSdk init(final Context context, final String s) {
        return init(context, s, null);
    }
    
    public static TuSdk init(final Context context, final String s, final String s2) {
        if (TuSdk.a == null && context != null) {
            TuSdk.a = new TuSdk(context, s, s2);
        }
        return TuSdk.a;
    }
    
    private TuSdk(final Context context, final String s, final String s2) {
        if (context == null) {
            TLog.e("TuSdk init : The context cannot be null.", new Object[0]);
            return;
        }
        if (StringHelper.isEmpty(s)) {
            TLog.e("TuSdk init : The appKey cannot be null or empty.", new Object[0]);
            return;
        }
        if (SdkValid.shared.sdkValid(context, s, s2)) {
            this.a();
        }
        else {
            TLog.e("Incorrect app key! Please see: http://tusdk.com/docs/help/package-name-and-app-key", new Object[0]);
        }
    }
    
    private void a() {
        if (TuSdk.c != null) {
            TuSdkContext.ins().setResourceClazz(TuSdk.c);
        }
        TuSpecialScreenHelper.dealNotchScreen();
        TuSdkLocation.init(TuSdkContext.context());
        SelesContext.init(TuSdkContext.context());
        TuSdkHttpEngine.init(SdkValid.shared.geTuSdkConfigs(), SdkValid.shared.getDeveloperId(), TuSdkContext.context());
        ImageLoaderHelper.initImageCache(TuSdkContext.context(), TuSdkContext.getScreenSize());
        FilterManager.init(SdkValid.shared.geTuSdkConfigs());
        StickerLocalPackage.init(SdkValid.shared.geTuSdkConfigs());
        BrushManager.init(SdkValid.shared.geTuSdkConfigs());
        StatisticsManger.init(TuSdkContext.context(), getAppTempPath());
        LogStashManager.init(getAppTempPath());
        SdkValid.shared.checkAppAuth();
    }
    
    public static String userIdentify() {
        if (TuSdkHttpEngine.shared() == null) {
            return null;
        }
        return TuSdkHttpEngine.shared().userIdentify();
    }
    
    public static void setUserIdentify(final Object userIdentify) {
        if (TuSdkHttpEngine.shared() == null) {
            return;
        }
        TuSdkHttpEngine.shared().setUserIdentify(userIdentify);
    }
    
    public static void enableDebugLog(final boolean b) {
        if (b) {
            TLog.enableLogging("TuSdk");
        }
        else {
            TLog.disableLogging();
        }
    }
    
    public static void setUseSSL(final boolean useSSL) {
        TuSdkHttpEngine.useSSL = useSSL;
    }
    
    public static TuSdkContext appContext() {
        if (TuSdk.a != null && SdkValid.shared.isVaild()) {
            return TuSdkContext.ins();
        }
        return null;
    }
    
    public static void setResourcePackageClazz(final Class<?> c) {
        TuSdk.c = c;
    }
    
    public static File getAppTempPath() {
        if (!SdkValid.shared.isVaild()) {
            return null;
        }
        return TuSdkContext.getAppCacheDir("lasFilterTemp", false);
    }
    
    public static File getAppDownloadPath() {
        if (!SdkValid.shared.isVaild()) {
            return null;
        }
        return TuSdkContext.getAppCacheDir("lasDownload", false);
    }
    
    public static List<String> filterNames() {
        if (!SdkValid.shared.isVaild()) {
            return null;
        }
        return filterManager().getFilterNames();
    }
    
    public static void checkFilterManager(final FilterManager.FilterManagerDelegate filterManagerDelegate) {
        if (!SdkValid.shared.isVaild()) {
            return;
        }
        FilterManager.shared().checkFilterManager(filterManagerDelegate);
    }
    
    public static FilterManager filterManager() {
        if (!SdkValid.shared.isVaild()) {
            return null;
        }
        return FilterManager.shared();
    }
    
    public static StickerLocalPackage stickerManager() {
        if (!SdkValid.shared.isVaild()) {
            return null;
        }
        return StickerLocalPackage.shared();
    }
    
    public static void setMessageHub(final TuMessageHubInterface b) {
        if (TuSdk.a == null) {
            return;
        }
        TuSdk.a.b = b;
    }
    
    public static TuMessageHubInterface messageHub() {
        if (TuSdk.a == null) {
            return null;
        }
        if (TuSdk.a.b == null) {
            TuSdk.a.b = new TuMessageHubImpl();
        }
        return TuSdk.a.b;
    }
    
    public static TuSdkStillCameraInterface camera(final Context context, final CameraConfigs.CameraFacing cameraFacing, final RelativeLayout relativeLayout) {
        return camera(context, cameraFacing, relativeLayout, false);
    }
    
    public static TuSdkStillCameraInterface camera(final Context context, final CameraConfigs.CameraFacing cameraFacing, final RelativeLayout relativeLayout, final boolean b) {
        if (!SdkValid.shared.isVaild()) {
            return null;
        }
        if (Build.VERSION.SDK_INT < 21 || !b || Camera2Helper.hardwareOnlySupportLegacy(context)) {
            return new TuSdkStillCamera(context, cameraFacing, relativeLayout);
        }
        return new TuSdkStillCamera2(context, cameraFacing, relativeLayout);
    }
}
