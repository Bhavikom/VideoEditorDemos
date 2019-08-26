// 
// Decompiled by Procyon v0.5.36
// 

package com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils;

import android.content.ClipData;
import android.content.pm.PackageManager;
import android.text.ClipboardManager;
import java.util.List;
import android.content.SharedPreferences;
//import org.lasque.tusdk.core.utils.hardware.InterfaceOrientation;
import java.io.InputStream;
import android.view.ContextThemeWrapper;
import android.content.pm.PackageInfo;
import android.annotation.TargetApi;
import android.view.Display;
import android.graphics.Point;
import android.os.Build;
import android.util.DisplayMetrics;
//import org.lasque.tusdk.core.struct.TuSdkSize;
import android.app.NotificationManager;
import android.app.ActivityManager;
import android.view.WindowManager;
//import org.lasque.tusdk.core.type.PermissionType;
import android.content.Context;

import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.struct.TuSdkSize;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.type.PermissionType;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.hardware.InterfaceOrientation;

public class ContextUtils
{
    public static <T> T getSystemService(final Context context, final String s) {
        if (context == null) {
            return null;
        }
        return (T)context.getSystemService(s);
    }
    
    public static boolean hasSystemFeature(final Context context, final String s) {
        return context != null && s != null && context.getPackageManager().hasSystemFeature(s);
    }
    
    public static boolean hasPermission(final Context context, final PermissionType permissionType) {
        return permissionType != null && context != null && context.checkCallingOrSelfPermission(permissionType.getKey()) == PackageManager.PERMISSION_GRANTED;
    }
    
    public static WindowManager getWindowManager(final Context context) {
        return getSystemService(context, "window");
    }
    
    public static ActivityManager getActivityManager(final Context context) {
        return getSystemService(context, "activity");
    }
    
    public static NotificationManager getNotificationManager(final Context context) {
        return getSystemService(context, "notification");
    }
    
    public static TuSdkSize getScreenSize(final Context context) {
        if (context == null) {
            return null;
        }
        final WindowManager windowManager = getWindowManager(context);
        final DisplayMetrics displayMetrics = new DisplayMetrics();
        if (Build.VERSION.SDK_INT < 17) {
            windowManager.getDefaultDisplay().getMetrics(displayMetrics);
        }
        else {
            windowManager.getDefaultDisplay().getRealMetrics(displayMetrics);
        }
        return new TuSdkSize(displayMetrics.widthPixels, displayMetrics.heightPixels);
    }
    
    public static TuSdkSize getDisplaySize(final Context context) {
        final Display defaultDisplay = getWindowManager(context).getDefaultDisplay();
        final Point point = new Point();
        if (Build.VERSION.SDK_INT < 13) {
            a(defaultDisplay, point);
        }
        else {
            b(defaultDisplay, point);
        }
        return TuSdkSize.create(point.x, point.y);
    }
    
    private static void a(final Display display, final Point point) {
        point.x = display.getWidth();
        point.y = display.getHeight();
    }
    
    @TargetApi(13)
    private static void b(final Display display, final Point point) {
        display.getSize(point);
    }
    
    public static float density(final Context context) {
        if (context == null) {
            return 0.0f;
        }
        return context.getResources().getDisplayMetrics().density;
    }
    
    public static int dip2px(final Context context, final float n) {
        return (int)(n * density(context) + 0.5f);
    }
    
    public static int px2dip(final Context context, final float n) {
        return (int)(n / density(context) + 0.5f);
    }
    
    public static int px2sp(final Context context, final float n) {
        if (context == null) {
            return (int)n;
        }
        return (int)(n / context.getResources().getDisplayMetrics().scaledDensity + 0.5f);
    }
    
    public static int sp2px(final Context context, final float n) {
        if (context == null) {
            return (int)n;
        }
        return (int)(n * context.getResources().getDisplayMetrics().scaledDensity + 0.5f);
    }
    
    public static float sp2pxFloat(final Context context, final float n) {
        if (context == null) {
            return n;
        }
        return n * context.getResources().getDisplayMetrics().scaledDensity;
    }
    
    public static PackageInfo getPackageInfo(final Context context) {
        if (context == null) {
            return null;
        }
        PackageInfo packageInfo = null;
        try {
            packageInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
        }
        catch (Exception ex) {
            TLog.e(ex);
        }
        return packageInfo;
    }
    
    public static String getVersionName(final Context context) {
        final PackageInfo packageInfo = getPackageInfo(context);
        if (packageInfo == null) {
            return null;
        }
        return packageInfo.versionName;
    }
    
    public static int getVersionCode(final Context context) {
        final PackageInfo packageInfo = getPackageInfo(context);
        if (packageInfo == null) {
            return 0;
        }
        return packageInfo.versionCode;
    }
    
    public static String getAppName(final Context context) {
        final PackageInfo packageInfo = getPackageInfo(context);
        if (packageInfo == null) {
            return null;
        }
        return packageInfo.applicationInfo.loadLabel(context.getPackageManager()).toString();
    }
    
    public static String getResString(final Context context, final int n) {
        if (context == null || n == 0) {
            return null;
        }
        return context.getResources().getString(n);
    }
    
    public static String getResString(final Context context, final int n, final Object... array) {
        if (context == null || n == 0) {
            return null;
        }
        return context.getResources().getString(n, array);
    }
    
    public static int getResColor(final Context context, final int n) {
        if (context == null || n == 0) {
            return 0;
        }
        return context.getResources().getColor(n);
    }
    
    public static ContextThemeWrapper getResStyleContext(final Context context, final int n) {
        if (context == null || n == 0) {
            return null;
        }
        return new ContextThemeWrapper(context, n);
    }
    
    public static float getResDimension(final Context context, final int n) {
        if (context == null || n == 0) {
            return 0.0f;
        }
        return context.getResources().getDimension(n);
    }
    
    public static int getResOffset(final Context context, final int n) {
        if (context == null || n == 0) {
            return 0;
        }
        return context.getResources().getDimensionPixelOffset(n);
    }
    
    public static int getResSize(final Context context, final int n) {
        if (context == null || n == 0) {
            return 0;
        }
        return context.getResources().getDimensionPixelSize(n);
    }
    
    public static InputStream getRawStream(final Context context, final int i) {
        if (context == null || i == 0) {
            return null;
        }
        try {
            return context.getResources().openRawResource(i);
        }
        catch (Exception ex) {
            TLog.e(ex, "getRawStream: %s", i);
            return null;
        }
    }
    
    public static int getRotation(final Context context) {
        if (context == null) {
            return 0;
        }
        return getWindowManager(context).getDefaultDisplay().getRotation();
    }
    
    public static InterfaceOrientation getInterfaceRotation(final Context context) {
        return InterfaceOrientation.getWithSurfaceRotation(getRotation(context));
    }
    
    public static SharedPreferences getSharedPreferences(final Context context, final String s, final int n) {
        if (context == null) {
            return null;
        }
        return context.getSharedPreferences(s, n);
    }

    public boolean isAppOnForeground(Context var1) {
        ActivityManager var2 = getActivityManager(var1);
        if (var2 == null) {
            return false;
        } else {
            List var3 = var2.getRunningTasks(1);
            String var4 = var1.getPackageName();
            return var3.size() > 0 && var4.equals(((ActivityManager.RunningTaskInfo)var3.get(0)).topActivity.getPackageName());
        }
    }
    
    public static void copyToClipboard(final Context context, final String s) {
        if (s == null || context == null) {
            return;
        }
        if (Build.VERSION.SDK_INT < 11) {
            a(context, s);
        }
        else {
            b(context, s);
        }
    }
    
    private static void a(final Context context, final String text) {
        ((ClipboardManager)context.getSystemService(Context.CLIPBOARD_SERVICE)).setText((CharSequence)text);
    }
    
    @TargetApi(11)
    private static void b(final Context context, final String s) {
        ((android.content.ClipboardManager)context.getSystemService(Context.CLIPBOARD_SERVICE)).setPrimaryClip(ClipData.newPlainText((CharSequence)"", (CharSequence)s));
    }
}
