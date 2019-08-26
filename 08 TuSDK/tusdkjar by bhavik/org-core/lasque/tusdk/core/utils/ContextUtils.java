package org.lasque.tusdk.core.utils;

import android.annotation.TargetApi;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningTaskInfo;
import android.app.NotificationManager;
import android.content.ClipData;
import android.content.ComponentName;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Point;
import android.os.Build.VERSION;
import android.util.DisplayMetrics;
import android.view.ContextThemeWrapper;
import android.view.Display;
import android.view.WindowManager;
import java.io.InputStream;
import java.util.List;
import org.lasque.tusdk.core.struct.TuSdkSize;
import org.lasque.tusdk.core.type.PermissionType;
import org.lasque.tusdk.core.utils.hardware.InterfaceOrientation;

public class ContextUtils
{
  public static <T> T getSystemService(Context paramContext, String paramString)
  {
    if (paramContext == null) {
      return null;
    }
    return (T)paramContext.getSystemService(paramString);
  }
  
  public static boolean hasSystemFeature(Context paramContext, String paramString)
  {
    if ((paramContext == null) || (paramString == null)) {
      return false;
    }
    return paramContext.getPackageManager().hasSystemFeature(paramString);
  }
  
  public static boolean hasPermission(Context paramContext, PermissionType paramPermissionType)
  {
    if ((paramPermissionType == null) || (paramContext == null)) {
      return false;
    }
    int i = paramContext.checkCallingOrSelfPermission(paramPermissionType.getKey());
    return i == 0;
  }
  
  public static WindowManager getWindowManager(Context paramContext)
  {
    return (WindowManager)getSystemService(paramContext, "window");
  }
  
  public static ActivityManager getActivityManager(Context paramContext)
  {
    return (ActivityManager)getSystemService(paramContext, "activity");
  }
  
  public static NotificationManager getNotificationManager(Context paramContext)
  {
    return (NotificationManager)getSystemService(paramContext, "notification");
  }
  
  public static TuSdkSize getScreenSize(Context paramContext)
  {
    if (paramContext == null) {
      return null;
    }
    WindowManager localWindowManager = getWindowManager(paramContext);
    DisplayMetrics localDisplayMetrics = new DisplayMetrics();
    if (Build.VERSION.SDK_INT < 17) {
      localWindowManager.getDefaultDisplay().getMetrics(localDisplayMetrics);
    } else {
      localWindowManager.getDefaultDisplay().getRealMetrics(localDisplayMetrics);
    }
    TuSdkSize localTuSdkSize = new TuSdkSize(localDisplayMetrics.widthPixels, localDisplayMetrics.heightPixels);
    return localTuSdkSize;
  }
  
  public static TuSdkSize getDisplaySize(Context paramContext)
  {
    WindowManager localWindowManager = getWindowManager(paramContext);
    Display localDisplay = localWindowManager.getDefaultDisplay();
    Point localPoint = new Point();
    if (Build.VERSION.SDK_INT < 13) {
      a(localDisplay, localPoint);
    } else {
      b(localDisplay, localPoint);
    }
    return TuSdkSize.create(localPoint.x, localPoint.y);
  }
  
  private static void a(Display paramDisplay, Point paramPoint)
  {
    paramPoint.x = paramDisplay.getWidth();
    paramPoint.y = paramDisplay.getHeight();
  }
  
  @TargetApi(13)
  private static void b(Display paramDisplay, Point paramPoint)
  {
    paramDisplay.getSize(paramPoint);
  }
  
  public static float density(Context paramContext)
  {
    if (paramContext == null) {
      return 0.0F;
    }
    return paramContext.getResources().getDisplayMetrics().density;
  }
  
  public static int dip2px(Context paramContext, float paramFloat)
  {
    float f = density(paramContext);
    return (int)(paramFloat * f + 0.5F);
  }
  
  public static int px2dip(Context paramContext, float paramFloat)
  {
    float f = density(paramContext);
    return (int)(paramFloat / f + 0.5F);
  }
  
  public static int px2sp(Context paramContext, float paramFloat)
  {
    if (paramContext == null) {
      return (int)paramFloat;
    }
    float f = paramContext.getResources().getDisplayMetrics().scaledDensity;
    return (int)(paramFloat / f + 0.5F);
  }
  
  public static int sp2px(Context paramContext, float paramFloat)
  {
    if (paramContext == null) {
      return (int)paramFloat;
    }
    float f = paramContext.getResources().getDisplayMetrics().scaledDensity;
    return (int)(paramFloat * f + 0.5F);
  }
  
  public static float sp2pxFloat(Context paramContext, float paramFloat)
  {
    if (paramContext == null) {
      return paramFloat;
    }
    float f = paramContext.getResources().getDisplayMetrics().scaledDensity;
    return paramFloat * f;
  }
  
  public static PackageInfo getPackageInfo(Context paramContext)
  {
    if (paramContext == null) {
      return null;
    }
    PackageInfo localPackageInfo = null;
    try
    {
      localPackageInfo = paramContext.getPackageManager().getPackageInfo(paramContext.getPackageName(), 0);
    }
    catch (Exception localException)
    {
      TLog.e(localException);
    }
    return localPackageInfo;
  }
  
  public static String getVersionName(Context paramContext)
  {
    PackageInfo localPackageInfo = getPackageInfo(paramContext);
    if (localPackageInfo == null) {
      return null;
    }
    return localPackageInfo.versionName;
  }
  
  public static int getVersionCode(Context paramContext)
  {
    PackageInfo localPackageInfo = getPackageInfo(paramContext);
    if (localPackageInfo == null) {
      return 0;
    }
    return localPackageInfo.versionCode;
  }
  
  public static String getAppName(Context paramContext)
  {
    PackageInfo localPackageInfo = getPackageInfo(paramContext);
    if (localPackageInfo == null) {
      return null;
    }
    return localPackageInfo.applicationInfo.loadLabel(paramContext.getPackageManager()).toString();
  }
  
  public static String getResString(Context paramContext, int paramInt)
  {
    if ((paramContext == null) || (paramInt == 0)) {
      return null;
    }
    return paramContext.getResources().getString(paramInt);
  }
  
  public static String getResString(Context paramContext, int paramInt, Object... paramVarArgs)
  {
    if ((paramContext == null) || (paramInt == 0)) {
      return null;
    }
    return paramContext.getResources().getString(paramInt, paramVarArgs);
  }
  
  public static int getResColor(Context paramContext, int paramInt)
  {
    if ((paramContext == null) || (paramInt == 0)) {
      return 0;
    }
    return paramContext.getResources().getColor(paramInt);
  }
  
  public static ContextThemeWrapper getResStyleContext(Context paramContext, int paramInt)
  {
    if ((paramContext == null) || (paramInt == 0)) {
      return null;
    }
    ContextThemeWrapper localContextThemeWrapper = new ContextThemeWrapper(paramContext, paramInt);
    return localContextThemeWrapper;
  }
  
  public static float getResDimension(Context paramContext, int paramInt)
  {
    if ((paramContext == null) || (paramInt == 0)) {
      return 0.0F;
    }
    return paramContext.getResources().getDimension(paramInt);
  }
  
  public static int getResOffset(Context paramContext, int paramInt)
  {
    if ((paramContext == null) || (paramInt == 0)) {
      return 0;
    }
    return paramContext.getResources().getDimensionPixelOffset(paramInt);
  }
  
  public static int getResSize(Context paramContext, int paramInt)
  {
    if ((paramContext == null) || (paramInt == 0)) {
      return 0;
    }
    return paramContext.getResources().getDimensionPixelSize(paramInt);
  }
  
  public static InputStream getRawStream(Context paramContext, int paramInt)
  {
    if ((paramContext == null) || (paramInt == 0)) {
      return null;
    }
    try
    {
      return paramContext.getResources().openRawResource(paramInt);
    }
    catch (Exception localException)
    {
      TLog.e(localException, "getRawStream: %s", new Object[] { Integer.valueOf(paramInt) });
    }
    return null;
  }
  
  public static int getRotation(Context paramContext)
  {
    if (paramContext == null) {
      return 0;
    }
    WindowManager localWindowManager = getWindowManager(paramContext);
    int i = localWindowManager.getDefaultDisplay().getRotation();
    return i;
  }
  
  public static InterfaceOrientation getInterfaceRotation(Context paramContext)
  {
    return InterfaceOrientation.getWithSurfaceRotation(getRotation(paramContext));
  }
  
  public static SharedPreferences getSharedPreferences(Context paramContext, String paramString, int paramInt)
  {
    if (paramContext == null) {
      return null;
    }
    return paramContext.getSharedPreferences(paramString, paramInt);
  }
  
  public boolean isAppOnForeground(Context paramContext)
  {
    ActivityManager localActivityManager = getActivityManager(paramContext);
    if (localActivityManager == null) {
      return false;
    }
    List localList = localActivityManager.getRunningTasks(1);
    String str = paramContext.getPackageName();
    return (localList.size() > 0) && (str.equals(((ActivityManager.RunningTaskInfo)localList.get(0)).topActivity.getPackageName()));
  }
  
  public static void copyToClipboard(Context paramContext, String paramString)
  {
    if ((paramString == null) || (paramContext == null)) {
      return;
    }
    if (Build.VERSION.SDK_INT < 11) {
      a(paramContext, paramString);
    } else {
      b(paramContext, paramString);
    }
  }
  
  private static void a(Context paramContext, String paramString)
  {
    android.text.ClipboardManager localClipboardManager = (android.text.ClipboardManager)paramContext.getSystemService("clipboard");
    localClipboardManager.setText(paramString);
  }
  
  @TargetApi(11)
  private static void b(Context paramContext, String paramString)
  {
    android.content.ClipboardManager localClipboardManager = (android.content.ClipboardManager)paramContext.getSystemService("clipboard");
    ClipData localClipData = ClipData.newPlainText("", paramString);
    localClipboardManager.setPrimaryClip(localClipData);
  }
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKCore-3.1.0.jar!\org\lasque\tusdk\core\utils\ContextUtils.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */