// 
// Decompiled by Procyon v0.5.36
// 

package com.yasoka.screenrecord.sdk.core.lasque.tusdk.core;

//import org.lasque.tusdk.core.struct.TuSdkSize;
//import org.lasque.tusdk.core.utils.FileHelper;
import java.io.File;
import java.util.Hashtable;
//import org.lasque.tusdk.core.utils.hardware.HardwareHelper;
import android.media.MediaPlayer;
import android.content.res.AssetFileDescriptor;
//import org.lasque.tusdk.core.utils.AssetsHelper;
import java.io.InputStream;
import android.content.res.AssetManager;
import android.graphics.Typeface;
import android.view.ContextThemeWrapper;
import android.graphics.Bitmap;
//import org.lasque.tusdk.core.utils.image.BitmapHelper;
import android.graphics.drawable.Drawable;
//import org.lasque.tusdk.core.utils.ContextUtils;
//import org.lasque.tusdk.core.utils.anim.AnimHelper;
import android.view.animation.Animation;
//import org.lasque.tusdk.core.type.ResourceType;
import android.widget.Toast;
//import org.lasque.tusdk.core.utils.ReflectUtils;
//import org.lasque.tusdk.core.utils.hardware.TuSdkSharedPreferences;
import android.content.Context;

import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.struct.TuSdkSize;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.type.ResourceType;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.AssetsHelper;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.ContextUtils;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.FileHelper;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.ReflectUtils;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.anim.AnimHelper;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.hardware.HardwareHelper;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.hardware.TuSdkSharedPreferences;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.image.BitmapHelper;

public class TuSdkContext
{
    public static final String SUFFIX = "TUSDK_";
    public static final String DEVICE_UUID = "TUSDK_DeviceUUID";
    public static final String GLOBAL_UUID = "TUSDK_GlobalUUID";
    public static final String SHARED_CACHE_KEY = "TUSDK_Shared_Cache";
    private static TuSdkContext a;
    private Context b;
    private Class<?> c;
    private TuSdkSharedPreferences d;
    
    public static TuSdkContext init(final Context context) {
        if (TuSdkContext.a == null && context != null) {
            TuSdkContext.a = new TuSdkContext(context);
        }
        return TuSdkContext.a;
    }
    
    public static TuSdkContext ins() {
        return TuSdkContext.a;
    }
    
    private TuSdkContext(final Context b) {
        this.b = b;
        this.d = new TuSdkSharedPreferences(this.b, "TUSDK_Shared_Cache");
    }
    
    private Class<?> a() {
        if (this.c == null) {
            this.c = ReflectUtils.reflectClass(String.format("%s.R", this.b.getPackageName()));
        }
        return this.c;
    }
    
    public void setResourceClazz(final Class<?> c) {
        this.c = c;
    }
    
    public Context getContext() {
        return this.b;
    }
    
    public void toast(final String s) {
        Toast.makeText(this.b, (CharSequence)s, Toast.LENGTH_SHORT).show();
    }
    
    public void toast(final int n) {
        Toast.makeText(this.b, n, Toast.LENGTH_SHORT).show();
    }
    
    private int a(final ResourceType resourceType, final String s) {
        return ReflectUtils.getResource(this.a(), resourceType, s);
    }
    
    public static Context context() {
        if (TuSdkContext.a == null) {
            return null;
        }
        return TuSdkContext.a.getContext();
    }
    
    public static int getAnimaResId(final String s) {
        if (TuSdkContext.a == null) {
            return 0;
        }
        return TuSdkContext.a.a(ResourceType.anim, s);
    }
    
    public static Animation getAnima(final int n) {
        if (TuSdkContext.a == null) {
            return null;
        }
        return AnimHelper.getResAnima(TuSdkContext.a.getContext(), n);
    }
    
    public static Animation getAnima(final String s) {
        return getAnima(getAnimaResId(s));
    }
    
    public static int getAttrResId(final String s) {
        if (TuSdkContext.a == null) {
            return 0;
        }
        return TuSdkContext.a.a(ResourceType.attr, s);
    }
    
    public static int getColorResId(final String s) {
        if (TuSdkContext.a == null) {
            return 0;
        }
        return TuSdkContext.a.a(ResourceType.color, s);
    }
    
    public static int getColor(final int n) {
        if (TuSdkContext.a == null) {
            return 0;
        }
        return ContextUtils.getResColor(TuSdkContext.a.getContext(), n);
    }
    
    public static int getColor(final String s) {
        return getColor(getColorResId(s));
    }
    
    public static int getDimenResId(final String s) {
        if (TuSdkContext.a == null) {
            return 0;
        }
        return TuSdkContext.a.a(ResourceType.dimen, s);
    }
    
    public static float getDimen(final int n) {
        if (TuSdkContext.a == null) {
            return 0.0f;
        }
        return ContextUtils.getResDimension(TuSdkContext.a.getContext(), n);
    }
    
    public static float getDimen(final String s) {
        return getDimen(getDimenResId(s));
    }
    
    public static int getDimenOffset(final int n) {
        if (TuSdkContext.a == null) {
            return 0;
        }
        return ContextUtils.getResOffset(TuSdkContext.a.getContext(), n);
    }
    
    public static int getDimenOffset(final String s) {
        return getDimenOffset(getDimenResId(s));
    }
    
    public static int getDimenSize(final int n) {
        if (TuSdkContext.a == null) {
            return 0;
        }
        return ContextUtils.getResSize(TuSdkContext.a.getContext(), n);
    }
    
    public static int getDimenSize(final String s) {
        return getDimenSize(getDimenResId(s));
    }
    
    public static int getDrawableResId(final String s) {
        if (TuSdkContext.a == null) {
            return 0;
        }
        return TuSdkContext.a.a(ResourceType.drawable, s);
    }
    
    public static Drawable getDrawable(final int n) {
        if (TuSdkContext.a == null) {
            return null;
        }
        return BitmapHelper.getResDrawable(TuSdkContext.a.getContext(), n);
    }
    
    public static Drawable getDrawable(final String s) {
        return getDrawable(getDrawableResId(s));
    }
    
    public static int getIDResId(final String s) {
        if (TuSdkContext.a == null) {
            return 0;
        }
        return TuSdkContext.a.a(ResourceType.id, s);
    }
    
    public static int getLayoutResId(final String s) {
        if (TuSdkContext.a == null) {
            return 0;
        }
        return TuSdkContext.a.a(ResourceType.layout, s);
    }
    
    public static int getMenuResId(final String s) {
        if (TuSdkContext.a == null) {
            return 0;
        }
        return TuSdkContext.a.a(ResourceType.menu, s);
    }
    
    public static int getRawResId(final String s) {
        if (TuSdkContext.a == null) {
            return 0;
        }
        return TuSdkContext.a.a(ResourceType.raw, s);
    }
    
    public static Bitmap getRawBitmap(final int n) {
        if (TuSdkContext.a == null) {
            return null;
        }
        return BitmapHelper.getRawBitmap(TuSdkContext.a.getContext(), n);
    }
    
    public static Bitmap getRawBitmap(final String s) {
        return getRawBitmap(getRawResId(s));
    }
    
    public static int getStringResId(final String s) {
        if (TuSdkContext.a == null) {
            return 0;
        }
        return TuSdkContext.a.a(ResourceType.string, s);
    }
    
    public static String getString(final int n) {
        if (TuSdkContext.a == null) {
            return null;
        }
        return ContextUtils.getResString(TuSdkContext.a.getContext(), n);
    }
    
    public static String getString(final String s) {
        final String string = getString(getStringResId(s));
        if (string == null) {
            return s;
        }
        return string;
    }
    
    public static String getString(final int n, final Object... array) {
        if (TuSdkContext.a == null) {
            return null;
        }
        return ContextUtils.getResString(TuSdkContext.a.getContext(), n, array);
    }
    
    public static String getString(final String s, final Object... array) {
        return getString(getStringResId(s), array);
    }
    
    public static int getStyleResId(final String s) {
        if (TuSdkContext.a == null) {
            return 0;
        }
        return TuSdkContext.a.a(ResourceType.style, s);
    }
    
    public static int getStyleableResId(final String s) {
        if (TuSdkContext.a == null) {
            return 0;
        }
        return TuSdkContext.a.a(ResourceType.styleable, s);
    }
    
    public ContextThemeWrapper getResourceStyleContext(final int n) {
        return ContextUtils.getResStyleContext(this.b, n);
    }
    
    public Typeface getTypeface(final String s) {
        if (s == null) {
            return null;
        }
        return Typeface.createFromAsset(this.b.getAssets(), s);
    }
    
    public static AssetManager getAssetManager() {
        if (TuSdkContext.a == null) {
            return null;
        }
        return TuSdkContext.a.getContext().getAssets();
    }
    
    public static InputStream getAssetsStream(final String s) {
        if (TuSdkContext.a == null) {
            return null;
        }
        return AssetsHelper.getAssetsStream(TuSdkContext.a.getContext(), s);
    }
    
    public static Bitmap getAssetsBitmap(final String s) {
        if (TuSdkContext.a == null) {
            return null;
        }
        return BitmapHelper.getAssetsBitmap(TuSdkContext.a.getContext(), s);
    }
    
    public static AssetFileDescriptor getAssetFileDescriptor(final String s) {
        if (TuSdkContext.a == null) {
            return null;
        }
        return AssetsHelper.getAssetFileDescriptor(TuSdkContext.a.getContext(), s);
    }
    
    public static MediaPlayer loadMediaAsset(final String s) {
        if (TuSdkContext.a == null) {
            return null;
        }
        return HardwareHelper.loadMediaAsset(TuSdkContext.a.getContext(), s);
    }
    
    public static Hashtable<String, String> getAssetsFiles(final String s) {
        if (TuSdkContext.a == null) {
            return null;
        }
        return AssetsHelper.getAssetsFiles(TuSdkContext.a.getContext(), s);
    }
    
    public static String getAssetsText(final String s) {
        if (TuSdkContext.a == null) {
            return null;
        }
        return AssetsHelper.getAssetsText(TuSdkContext.a.getContext(), s);
    }
    
    public static boolean hasAssets(final String s) {
        return TuSdkContext.a != null && AssetsHelper.hasAssets(TuSdkContext.a.getContext(), s);
    }
    
    public static File getAppCacheDir(final boolean b) {
        return getAppCacheDir(null, b);
    }
    
    public static File getAppCacheDir(final String s, final boolean b) {
        if (TuSdkContext.a == null) {
            return null;
        }
        return FileHelper.getAppCacheDir(TuSdkContext.a.getContext(), s, b);
    }
    
    public static TuSdkSize getScreenSize() {
        if (TuSdkContext.a == null) {
            return null;
        }
        return ContextUtils.getScreenSize(TuSdkContext.a.getContext());
    }
    
    public static TuSdkSize getDisplaySize() {
        if (TuSdkContext.a == null) {
            return null;
        }
        return ContextUtils.getDisplaySize(TuSdkContext.a.getContext());
    }
    
    public static boolean hasAvailableExternal() {
        return TuSdkContext.a != null && FileHelper.hasAvailableExternal(TuSdkContext.a.getContext());
    }
    
    public static String getWeekdayName(final int n) {
        return getString((new String[] { "las_week_Sun", "las_week_Mon", "las_week_Tue", "las_week_Wed", "las_week_Thu", "las_week_Fri", "las_week_Sat" })[n - 1]);
    }
    
    public static int dip2px(final float n) {
        if (TuSdkContext.a == null) {
            return (int)n;
        }
        return ContextUtils.dip2px(TuSdkContext.a.getContext(), n);
    }
    
    public static int px2dip(final float n) {
        if (TuSdkContext.a == null) {
            return (int)n;
        }
        return ContextUtils.px2dip(TuSdkContext.a.getContext(), n);
    }
    
    public static int sp2px(final int n) {
        if (TuSdkContext.a == null) {
            return n;
        }
        return ContextUtils.sp2px(TuSdkContext.a.getContext(), (float)n);
    }
    
    public static float sp2pxFloat(final float n) {
        if (TuSdkContext.a == null) {
            return n;
        }
        return ContextUtils.sp2pxFloat(TuSdkContext.a.getContext(), n);
    }
    
    public static float px2sp(final float n) {
        if (TuSdkContext.a == null) {
            return n;
        }
        return (float)ContextUtils.px2sp(TuSdkContext.a.getContext(), n);
    }
    
    public static TuSdkSharedPreferences sharedPreferences() {
        if (TuSdkContext.a == null) {
            return null;
        }
        return TuSdkContext.a.d;
    }
    
    public static boolean isNetworkAvailable() {
        return TuSdkContext.a != null && HardwareHelper.isNetworkAvailable(TuSdkContext.a.getContext());
    }
    
    public static String getPackageName() {
        if (TuSdkContext.a == null) {
            return null;
        }
        return TuSdkContext.a.getContext().getPackageName();
    }
}
