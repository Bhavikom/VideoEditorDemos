package org.lasque.tusdk.core;

import android.content.Context;
import android.os.Build.VERSION;
import android.widget.RelativeLayout;
import java.io.File;
import java.util.List;
import org.lasque.tusdk.core.network.TuSdkHttpEngine;
import org.lasque.tusdk.core.secret.LogStashManager;
import org.lasque.tusdk.core.secret.SdkValid;
import org.lasque.tusdk.core.secret.StatisticsManger;
import org.lasque.tusdk.core.seles.SelesContext;
import org.lasque.tusdk.core.seles.tusdk.FilterManager;
import org.lasque.tusdk.core.seles.tusdk.FilterManager.FilterManagerDelegate;
import org.lasque.tusdk.core.utils.StringHelper;
import org.lasque.tusdk.core.utils.TLog;
import org.lasque.tusdk.core.utils.TuSdkLocation;
import org.lasque.tusdk.core.utils.hardware.Camera2Helper;
import org.lasque.tusdk.core.utils.hardware.CameraConfigs.CameraFacing;
import org.lasque.tusdk.core.utils.hardware.TuSdkStillCamera;
import org.lasque.tusdk.core.utils.hardware.TuSdkStillCamera2;
import org.lasque.tusdk.core.utils.hardware.TuSdkStillCameraInterface;
import org.lasque.tusdk.core.utils.image.ImageLoaderHelper;
import org.lasque.tusdk.impl.TuSpecialScreenHelper;
import org.lasque.tusdk.impl.view.widget.TuMessageHubImpl;
import org.lasque.tusdk.impl.view.widget.TuMessageHubInterface;
import org.lasque.tusdk.modules.view.widget.smudge.BrushManager;
import org.lasque.tusdk.modules.view.widget.sticker.StickerLocalPackage;

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
  
  public static TuSdk shared()
  {
    return a;
  }
  
  public static TuSdk init(Context paramContext, String paramString)
  {
    return init(paramContext, paramString, null);
  }
  
  public static TuSdk init(Context paramContext, String paramString1, String paramString2)
  {
    if ((a == null) && (paramContext != null)) {
      a = new TuSdk(paramContext, paramString1, paramString2);
    }
    return a;
  }
  
  private TuSdk(Context paramContext, String paramString1, String paramString2)
  {
    if (paramContext == null)
    {
      TLog.e("TuSdk init : The context cannot be null.", new Object[0]);
      return;
    }
    if (StringHelper.isEmpty(paramString1))
    {
      TLog.e("TuSdk init : The appKey cannot be null or empty.", new Object[0]);
      return;
    }
    if (SdkValid.shared.sdkValid(paramContext, paramString1, paramString2)) {
      a();
    } else {
      TLog.e("Incorrect app key! Please see: http://tusdk.com/docs/help/package-name-and-app-key", new Object[0]);
    }
  }
  
  private void a()
  {
    if (c != null) {
      TuSdkContext.ins().setResourceClazz(c);
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
  
  public static String userIdentify()
  {
    if (TuSdkHttpEngine.shared() == null) {
      return null;
    }
    return TuSdkHttpEngine.shared().userIdentify();
  }
  
  public static void setUserIdentify(Object paramObject)
  {
    if (TuSdkHttpEngine.shared() == null) {
      return;
    }
    TuSdkHttpEngine.shared().setUserIdentify(paramObject);
  }
  
  public static void enableDebugLog(boolean paramBoolean)
  {
    if (paramBoolean) {
      TLog.enableLogging("TuSdk");
    } else {
      TLog.disableLogging();
    }
  }
  
  public static void setUseSSL(boolean paramBoolean)
  {
    TuSdkHttpEngine.useSSL = paramBoolean;
  }
  
  public static TuSdkContext appContext()
  {
    if ((a != null) && (SdkValid.shared.isVaild())) {
      return TuSdkContext.ins();
    }
    return null;
  }
  
  public static void setResourcePackageClazz(Class<?> paramClass)
  {
    c = paramClass;
  }
  
  public static File getAppTempPath()
  {
    if (!SdkValid.shared.isVaild()) {
      return null;
    }
    return TuSdkContext.getAppCacheDir("lasFilterTemp", false);
  }
  
  public static File getAppDownloadPath()
  {
    if (!SdkValid.shared.isVaild()) {
      return null;
    }
    return TuSdkContext.getAppCacheDir("lasDownload", false);
  }
  
  public static List<String> filterNames()
  {
    if (!SdkValid.shared.isVaild()) {
      return null;
    }
    return filterManager().getFilterNames();
  }
  
  public static void checkFilterManager(FilterManager.FilterManagerDelegate paramFilterManagerDelegate)
  {
    if (!SdkValid.shared.isVaild()) {
      return;
    }
    FilterManager.shared().checkFilterManager(paramFilterManagerDelegate);
  }
  
  public static FilterManager filterManager()
  {
    if (!SdkValid.shared.isVaild()) {
      return null;
    }
    return FilterManager.shared();
  }
  
  public static StickerLocalPackage stickerManager()
  {
    if (!SdkValid.shared.isVaild()) {
      return null;
    }
    return StickerLocalPackage.shared();
  }
  
  public static void setMessageHub(TuMessageHubInterface paramTuMessageHubInterface)
  {
    if (a == null) {
      return;
    }
    a.b = paramTuMessageHubInterface;
  }
  
  public static TuMessageHubInterface messageHub()
  {
    if (a == null) {
      return null;
    }
    if (a.b == null) {
      a.b = new TuMessageHubImpl();
    }
    return a.b;
  }
  
  public static TuSdkStillCameraInterface camera(Context paramContext, CameraConfigs.CameraFacing paramCameraFacing, RelativeLayout paramRelativeLayout)
  {
    return camera(paramContext, paramCameraFacing, paramRelativeLayout, false);
  }
  
  public static TuSdkStillCameraInterface camera(Context paramContext, CameraConfigs.CameraFacing paramCameraFacing, RelativeLayout paramRelativeLayout, boolean paramBoolean)
  {
    if (!SdkValid.shared.isVaild()) {
      return null;
    }
    if ((Build.VERSION.SDK_INT < 21) || (!paramBoolean) || (Camera2Helper.hardwareOnlySupportLegacy(paramContext))) {
      return new TuSdkStillCamera(paramContext, paramCameraFacing, paramRelativeLayout);
    }
    return new TuSdkStillCamera2(paramContext, paramCameraFacing, paramRelativeLayout);
  }
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKCore-3.1.0.jar!\org\lasque\tusdk\core\TuSdk.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */