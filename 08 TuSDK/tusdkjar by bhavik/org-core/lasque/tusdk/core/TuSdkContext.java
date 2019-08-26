package org.lasque.tusdk.core;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.view.ContextThemeWrapper;
import android.view.animation.Animation;
import android.widget.Toast;
import java.io.File;
import java.io.InputStream;
import java.util.Hashtable;
import org.lasque.tusdk.core.struct.TuSdkSize;
import org.lasque.tusdk.core.type.ResourceType;
import org.lasque.tusdk.core.utils.AssetsHelper;
import org.lasque.tusdk.core.utils.ContextUtils;
import org.lasque.tusdk.core.utils.FileHelper;
import org.lasque.tusdk.core.utils.ReflectUtils;
import org.lasque.tusdk.core.utils.anim.AnimHelper;
import org.lasque.tusdk.core.utils.hardware.HardwareHelper;
import org.lasque.tusdk.core.utils.hardware.TuSdkSharedPreferences;
import org.lasque.tusdk.core.utils.image.BitmapHelper;

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
  
  public static TuSdkContext init(Context paramContext)
  {
    if ((a == null) && (paramContext != null)) {
      a = new TuSdkContext(paramContext);
    }
    return a;
  }
  
  public static TuSdkContext ins()
  {
    return a;
  }
  
  private TuSdkContext(Context paramContext)
  {
    this.b = paramContext;
    this.d = new TuSdkSharedPreferences(this.b, "TUSDK_Shared_Cache");
  }
  
  private Class<?> a()
  {
    if (this.c == null)
    {
      String str = String.format("%s.R", new Object[] { this.b.getPackageName() });
      this.c = ReflectUtils.reflectClass(str);
    }
    return this.c;
  }
  
  public void setResourceClazz(Class<?> paramClass)
  {
    this.c = paramClass;
  }
  
  public Context getContext()
  {
    return this.b;
  }
  
  public void toast(String paramString)
  {
    Toast.makeText(this.b, paramString, 0).show();
  }
  
  public void toast(int paramInt)
  {
    Toast.makeText(this.b, paramInt, 0).show();
  }
  
  private int a(ResourceType paramResourceType, String paramString)
  {
    return ReflectUtils.getResource(a(), paramResourceType, paramString);
  }
  
  public static Context context()
  {
    if (a == null) {
      return null;
    }
    return a.getContext();
  }
  
  public static int getAnimaResId(String paramString)
  {
    if (a == null) {
      return 0;
    }
    return a.a(ResourceType.anim, paramString);
  }
  
  public static Animation getAnima(int paramInt)
  {
    if (a == null) {
      return null;
    }
    return AnimHelper.getResAnima(a.getContext(), paramInt);
  }
  
  public static Animation getAnima(String paramString)
  {
    return getAnima(getAnimaResId(paramString));
  }
  
  public static int getAttrResId(String paramString)
  {
    if (a == null) {
      return 0;
    }
    return a.a(ResourceType.attr, paramString);
  }
  
  public static int getColorResId(String paramString)
  {
    if (a == null) {
      return 0;
    }
    return a.a(ResourceType.color, paramString);
  }
  
  public static int getColor(int paramInt)
  {
    if (a == null) {
      return 0;
    }
    return ContextUtils.getResColor(a.getContext(), paramInt);
  }
  
  public static int getColor(String paramString)
  {
    return getColor(getColorResId(paramString));
  }
  
  public static int getDimenResId(String paramString)
  {
    if (a == null) {
      return 0;
    }
    return a.a(ResourceType.dimen, paramString);
  }
  
  public static float getDimen(int paramInt)
  {
    if (a == null) {
      return 0.0F;
    }
    return ContextUtils.getResDimension(a.getContext(), paramInt);
  }
  
  public static float getDimen(String paramString)
  {
    return getDimen(getDimenResId(paramString));
  }
  
  public static int getDimenOffset(int paramInt)
  {
    if (a == null) {
      return 0;
    }
    return ContextUtils.getResOffset(a.getContext(), paramInt);
  }
  
  public static int getDimenOffset(String paramString)
  {
    return getDimenOffset(getDimenResId(paramString));
  }
  
  public static int getDimenSize(int paramInt)
  {
    if (a == null) {
      return 0;
    }
    return ContextUtils.getResSize(a.getContext(), paramInt);
  }
  
  public static int getDimenSize(String paramString)
  {
    return getDimenSize(getDimenResId(paramString));
  }
  
  public static int getDrawableResId(String paramString)
  {
    if (a == null) {
      return 0;
    }
    return a.a(ResourceType.drawable, paramString);
  }
  
  public static Drawable getDrawable(int paramInt)
  {
    if (a == null) {
      return null;
    }
    return BitmapHelper.getResDrawable(a.getContext(), paramInt);
  }
  
  public static Drawable getDrawable(String paramString)
  {
    return getDrawable(getDrawableResId(paramString));
  }
  
  public static int getIDResId(String paramString)
  {
    if (a == null) {
      return 0;
    }
    return a.a(ResourceType.id, paramString);
  }
  
  public static int getLayoutResId(String paramString)
  {
    if (a == null) {
      return 0;
    }
    return a.a(ResourceType.layout, paramString);
  }
  
  public static int getMenuResId(String paramString)
  {
    if (a == null) {
      return 0;
    }
    return a.a(ResourceType.menu, paramString);
  }
  
  public static int getRawResId(String paramString)
  {
    if (a == null) {
      return 0;
    }
    return a.a(ResourceType.raw, paramString);
  }
  
  public static Bitmap getRawBitmap(int paramInt)
  {
    if (a == null) {
      return null;
    }
    return BitmapHelper.getRawBitmap(a.getContext(), paramInt);
  }
  
  public static Bitmap getRawBitmap(String paramString)
  {
    return getRawBitmap(getRawResId(paramString));
  }
  
  public static int getStringResId(String paramString)
  {
    if (a == null) {
      return 0;
    }
    return a.a(ResourceType.string, paramString);
  }
  
  public static String getString(int paramInt)
  {
    if (a == null) {
      return null;
    }
    return ContextUtils.getResString(a.getContext(), paramInt);
  }
  
  public static String getString(String paramString)
  {
    String str = getString(getStringResId(paramString));
    if (str == null) {
      return paramString;
    }
    return str;
  }
  
  public static String getString(int paramInt, Object... paramVarArgs)
  {
    if (a == null) {
      return null;
    }
    return ContextUtils.getResString(a.getContext(), paramInt, paramVarArgs);
  }
  
  public static String getString(String paramString, Object... paramVarArgs)
  {
    return getString(getStringResId(paramString), paramVarArgs);
  }
  
  public static int getStyleResId(String paramString)
  {
    if (a == null) {
      return 0;
    }
    return a.a(ResourceType.style, paramString);
  }
  
  public static int getStyleableResId(String paramString)
  {
    if (a == null) {
      return 0;
    }
    return a.a(ResourceType.styleable, paramString);
  }
  
  public ContextThemeWrapper getResourceStyleContext(int paramInt)
  {
    return ContextUtils.getResStyleContext(this.b, paramInt);
  }
  
  public Typeface getTypeface(String paramString)
  {
    if (paramString == null) {
      return null;
    }
    return Typeface.createFromAsset(this.b.getAssets(), paramString);
  }
  
  public static AssetManager getAssetManager()
  {
    if (a == null) {
      return null;
    }
    return a.getContext().getAssets();
  }
  
  public static InputStream getAssetsStream(String paramString)
  {
    if (a == null) {
      return null;
    }
    return AssetsHelper.getAssetsStream(a.getContext(), paramString);
  }
  
  public static Bitmap getAssetsBitmap(String paramString)
  {
    if (a == null) {
      return null;
    }
    return BitmapHelper.getAssetsBitmap(a.getContext(), paramString);
  }
  
  public static AssetFileDescriptor getAssetFileDescriptor(String paramString)
  {
    if (a == null) {
      return null;
    }
    return AssetsHelper.getAssetFileDescriptor(a.getContext(), paramString);
  }
  
  public static MediaPlayer loadMediaAsset(String paramString)
  {
    if (a == null) {
      return null;
    }
    return HardwareHelper.loadMediaAsset(a.getContext(), paramString);
  }
  
  public static Hashtable<String, String> getAssetsFiles(String paramString)
  {
    if (a == null) {
      return null;
    }
    return AssetsHelper.getAssetsFiles(a.getContext(), paramString);
  }
  
  public static String getAssetsText(String paramString)
  {
    if (a == null) {
      return null;
    }
    return AssetsHelper.getAssetsText(a.getContext(), paramString);
  }
  
  public static boolean hasAssets(String paramString)
  {
    if (a == null) {
      return false;
    }
    return AssetsHelper.hasAssets(a.getContext(), paramString);
  }
  
  public static File getAppCacheDir(boolean paramBoolean)
  {
    return getAppCacheDir(null, paramBoolean);
  }
  
  public static File getAppCacheDir(String paramString, boolean paramBoolean)
  {
    if (a == null) {
      return null;
    }
    return FileHelper.getAppCacheDir(a.getContext(), paramString, paramBoolean);
  }
  
  public static TuSdkSize getScreenSize()
  {
    if (a == null) {
      return null;
    }
    return ContextUtils.getScreenSize(a.getContext());
  }
  
  public static TuSdkSize getDisplaySize()
  {
    if (a == null) {
      return null;
    }
    return ContextUtils.getDisplaySize(a.getContext());
  }
  
  public static boolean hasAvailableExternal()
  {
    if (a == null) {
      return false;
    }
    return FileHelper.hasAvailableExternal(a.getContext());
  }
  
  public static String getWeekdayName(int paramInt)
  {
    String[] arrayOfString = { "las_week_Sun", "las_week_Mon", "las_week_Tue", "las_week_Wed", "las_week_Thu", "las_week_Fri", "las_week_Sat" };
    return getString(arrayOfString[(paramInt - 1)]);
  }
  
  public static int dip2px(float paramFloat)
  {
    if (a == null) {
      return (int)paramFloat;
    }
    return ContextUtils.dip2px(a.getContext(), paramFloat);
  }
  
  public static int px2dip(float paramFloat)
  {
    if (a == null) {
      return (int)paramFloat;
    }
    return ContextUtils.px2dip(a.getContext(), paramFloat);
  }
  
  public static int sp2px(int paramInt)
  {
    if (a == null) {
      return paramInt;
    }
    return ContextUtils.sp2px(a.getContext(), paramInt);
  }
  
  public static float sp2pxFloat(float paramFloat)
  {
    if (a == null) {
      return paramFloat;
    }
    return ContextUtils.sp2pxFloat(a.getContext(), paramFloat);
  }
  
  public static float px2sp(float paramFloat)
  {
    if (a == null) {
      return paramFloat;
    }
    return ContextUtils.px2sp(a.getContext(), paramFloat);
  }
  
  public static TuSdkSharedPreferences sharedPreferences()
  {
    if (a == null) {
      return null;
    }
    return a.d;
  }
  
  public static boolean isNetworkAvailable()
  {
    if (a == null) {
      return false;
    }
    return HardwareHelper.isNetworkAvailable(a.getContext());
  }
  
  public static String getPackageName()
  {
    if (a == null) {
      return null;
    }
    return a.getContext().getPackageName();
  }
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKCore-3.1.0.jar!\org\lasque\tusdk\core\TuSdkContext.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */