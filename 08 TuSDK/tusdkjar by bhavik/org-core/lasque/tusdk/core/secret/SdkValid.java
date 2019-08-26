package org.lasque.tusdk.core.secret;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint.Align;
import android.graphics.Paint.FontMetricsInt;
import android.text.TextPaint;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.lasque.tusdk.core.TuSdkBundle;
import org.lasque.tusdk.core.TuSdkConfigs;
import org.lasque.tusdk.core.TuSdkContext;
import org.lasque.tusdk.core.network.TuSdkAuthInfo;
import org.lasque.tusdk.core.network.TuSdkHttpEngine;
import org.lasque.tusdk.core.seles.sources.SelesPicture;
import org.lasque.tusdk.core.seles.tusdk.FilterWrap;
import org.lasque.tusdk.core.utils.NativeLibraryHelper;
import org.lasque.tusdk.core.utils.NativeLibraryHelper.NativeLibType;
import org.lasque.tusdk.core.utils.StringHelper;
import org.lasque.tusdk.core.utils.TLog;
import org.lasque.tusdk.core.utils.json.JsonHelper;
import org.lasque.tusdk.modules.components.ComponentActType;

public class SdkValid
{
  public static final boolean isInit = true;
  public static final SdkValid shared = new SdkValid();
  private TuSdkConfigs a;
  private String b;
  private boolean c;
  
  private synchronized boolean b()
  {
    return this.c;
  }
  
  private synchronized void a(boolean paramBoolean)
  {
    this.c = paramBoolean;
  }
  
  String a()
  {
    return this.b;
  }
  
  public TuSdkConfigs geTuSdkConfigs()
  {
    return this.a;
  }
  
  public boolean isVaild()
  {
    return jniHasValidWithDevType();
  }
  
  public boolean isExpired()
  {
    return jniHasExpired();
  }
  
  public int appType()
  {
    if (this.a == null) {
      jniAppType();
    }
    if (this.a.appType <= 0) {
      this.a.appType = jniAppType();
    }
    return this.a.appType;
  }
  
  public long serviceExpireSeconds()
  {
    return jniServiceExpireSeconds();
  }
  
  public String getDeveloperId()
  {
    return jniDeveloperID();
  }
  
  public int maxImageSide()
  {
    return jniCheckAuthor(48);
  }
  
  public int maxStickers()
  {
    return jniCheckAuthor(64);
  }
  
  public int localFilterCount()
  {
    return jniCheckAuthor(80);
  }
  
  public int localStickerCount()
  {
    return jniCheckAuthor(96);
  }
  
  public boolean renderFilterThumb()
  {
    return jniCheckAuthor(128) > 0;
  }
  
  public boolean smudgeEnabled()
  {
    return jniCheckAuthor(144) > 0;
  }
  
  public boolean paintEnabled()
  {
    return jniCheckAuthor(257) > 0;
  }
  
  public boolean wipeFilterEnabled()
  {
    return jniCheckAuthor(256) > 0;
  }
  
  public boolean hdrFilterEnabled()
  {
    return jniCheckAuthor(272) > 0;
  }
  
  public int beautyLevel()
  {
    return jniCheckAuthor(288);
  }
  
  public boolean videoRecordEnabled()
  {
    return jniCheckAuthor(4096) > 0;
  }
  
  public boolean videoDurationEnabled()
  {
    return jniCheckAuthor(4112) > 0;
  }
  
  public boolean videoEditEnabled()
  {
    return jniCheckAuthor(4128) > 0;
  }
  
  public boolean videoRecordContinuousEnabled()
  {
    return jniCheckAuthor(4144) > 0;
  }
  
  public boolean videoCameraShotEnabled()
  {
    return jniCheckAuthor(4160) > 0;
  }
  
  public boolean videoCameraStickerEnabled()
  {
    return jniCheckAuthor(4176) > 0;
  }
  
  public boolean videoCameraBitrateEnabled()
  {
    return jniCheckAuthor(4192) > 0;
  }
  
  public boolean videoCameraMonsterFaceSupport()
  {
    return jniCheckAuthor(4419) > 0;
  }
  
  public boolean videoEditorMusicEnabled()
  {
    return jniCheckAuthor(4208) > 0;
  }
  
  public boolean videoEditorStickerEnabled()
  {
    return jniCheckAuthor(4224) > 0;
  }
  
  public boolean videoEditorFilterEnabled()
  {
    return jniCheckAuthor(4240) > 0;
  }
  
  public boolean videoEditorBitrateEnabled()
  {
    return jniCheckAuthor(4352) > 0;
  }
  
  public boolean videoEditorResolutionEnabled()
  {
    return jniCheckAuthor(4368) > 0;
  }
  
  public boolean videoEditorEffectsfilterEnabled()
  {
    return jniCheckAuthor(4384) > 0;
  }
  
  public boolean videoEditorParticleEffectsFilterEnabled()
  {
    return jniCheckAuthor(4400) > 0;
  }
  
  public boolean videoEditorTextEffectsEnabled()
  {
    return jniCheckAuthor(4416) > 0;
  }
  
  public boolean videoEditorComicEffectsSupport()
  {
    return jniCheckAuthor(4417) > 0;
  }
  
  public boolean videoEditorMonsterFaceSupport()
  {
    return jniCheckAuthor(4418) > 0;
  }
  
  public boolean videoEditorTransitionEffectsSupport()
  {
    return jniCheckAuthor(4420) > 0;
  }
  
  public boolean videoImageComposeSupport()
  {
    return jniCheckAuthor(4421) > 0;
  }
  
  public boolean audioPitchEffectsSupport()
  {
    return jniCheckAuthor(4608) > 0;
  }
  
  public boolean audioResampleEffectsSupport()
  {
    return jniCheckAuthor(4609) > 0;
  }
  
  public boolean videoStreamEnabled()
  {
    return jniCheckAuthor(4432) > 0;
  }
  
  public boolean filterAPIEnabled()
  {
    return jniFilterAPIEnabled();
  }
  
  public boolean filterAPIValidWithID(long paramLong)
  {
    return jniFilterAPIValidWithID(paramLong);
  }
  
  public boolean evaReplaceTxt()
  {
    return jniCheckAuthor(196609) > 0;
  }
  
  public boolean evaReplaceImg()
  {
    return jniCheckAuthor(196610) > 0;
  }
  
  public boolean evaReplaceVideo()
  {
    return jniCheckAuthor(196611) > 0;
  }
  
  public boolean evaReplaceAudio()
  {
    return jniCheckAuthor(196612) > 0;
  }
  
  public boolean evaWipeCopyright()
  {
    return jniCheckAuthor(196613) > 0;
  }
  
  public boolean evaExportBitratet()
  {
    return jniCheckAuthor(196614) > 0;
  }
  
  public boolean evaExportResolution()
  {
    return jniCheckAuthor(196615) > 0;
  }
  
  public boolean evaExportAddMarkimage()
  {
    return jniCheckAuthor(196616) > 0;
  }
  
  public FilterWrap getFilterWrapWithCode(String paramString)
  {
    return jniGetFilterWrapWithCode(paramString);
  }
  
  public boolean sdkValid(Context paramContext, String paramString1, String paramString2)
  {
    if ((paramString1 == null) || (paramContext == null)) {
      return false;
    }
    this.b = paramString1;
    if (jniInit(paramContext, paramString1)) {
      b(paramString2);
    }
    return isVaild();
  }
  
  public boolean sdkValid()
  {
    return jniPassDoubleValid();
  }
  
  private void b(String paramString)
  {
    if (this.a != null) {
      return;
    }
    String str1 = TuSdkBundle.sdkBundleOther("lsq_tusdk_configs.json");
    String str2 = TuSdkContext.getAssetsText(str1);
    this.a = new TuSdkConfigs(JsonHelper.json(str2));
    if (this.a == null)
    {
      this.a = null;
      TLog.e("Configuration not found! Please see: http://tusdk.com/docs/android/get-started", new Object[0]);
      return;
    }
    String str3 = null;
    if ((StringHelper.isNotBlank(paramString)) && (this.a.masters != null)) {
      str3 = (String)this.a.masters.get(paramString);
    }
    this.a.masters = null;
    if ((str3 == null) && (StringHelper.isNotBlank(this.a.master))) {
      str3 = this.a.master;
    }
    if ((StringHelper.isBlank(str3)) || (str3.trim().length() < 11))
    {
      this.a = null;
      TLog.e("Master key not found! Please see: http://tusdk.com/docs/android/get-started", new Object[0]);
      return;
    }
    str3 = str3.trim();
    this.a.master = str3;
    boolean bool = jniLoadDevelopInfo(str3);
    if ((!bool) || (isExpired()))
    {
      TuSdkAuth.LocalAuthInfo localLocalAuthInfo = TuSdkAuth.shared().localAuthInfo();
      if ((localLocalAuthInfo != null) && (localLocalAuthInfo.remoteAuthInfo != null) && (localLocalAuthInfo.remoteAuthInfo.isValid())) {
        bool = jniLoadDevelopInfo(localLocalAuthInfo.remoteAuthInfo.masterKey);
      }
    }
    if (!bool)
    {
      this.a = null;
      TLog.e("Incorrect master key! Please see: http://tusdk.com/docs/help/package-name-and-app-key", new Object[0]);
    }
  }
  
  private String a(long paramLong, String paramString, SdkResourceType paramSdkResourceType)
  {
    if ((paramString == null) || (!StringHelper.isNotBlank(paramString))) {
      return null;
    }
    String str = shared.decodeMaster(paramString);
    JSONObject localJSONObject1 = JsonHelper.json(str);
    if (localJSONObject1 == null) {
      return null;
    }
    JSONArray localJSONArray = null;
    try
    {
      switch (2.a[paramSdkResourceType.ordinal()])
      {
      case 1: 
        break;
      case 2: 
        localJSONArray = localJSONObject1.getJSONArray("stickerGroups");
        break;
      case 3: 
        break;
      }
      if (localJSONArray == null) {
        return null;
      }
      for (int i = 0; i < localJSONArray.length(); i++)
      {
        JSONObject localJSONObject2 = (JSONObject)localJSONArray.get(i);
        if (localJSONObject2.getLong("id") == paramLong) {
          return localJSONObject2.getString("valid_key");
        }
      }
    }
    catch (JSONException localJSONException)
    {
      localJSONException.printStackTrace();
    }
    return null;
  }
  
  public String stickerGroupValidKey(long paramLong, String paramString)
  {
    return a(paramLong, paramString, SdkResourceType.ResourceSticker);
  }
  
  public void vaildAndDraw(Canvas paramCanvas)
  {
    if (jniCheckAuthor(16) > 0) {
      return;
    }
    float f = TuSdkContext.sp2px(10);
    TextPaint localTextPaint = new TextPaint(1);
    localTextPaint.setTextSize(f);
    localTextPaint.setColor(1090519039);
    localTextPaint.setShadowLayer(2.0F, 1.0F, 1.0F, 1073741824);
    Paint.FontMetricsInt localFontMetricsInt = localTextPaint.getFontMetricsInt();
    localTextPaint.setTextAlign(Paint.Align.LEFT);
    paramCanvas.drawText("Technology by TuSDK", localFontMetricsInt.bottom, paramCanvas.getHeight() - f + localFontMetricsInt.bottom, localTextPaint);
  }
  
  public void checkAppAuth()
  {
    if ((b()) || (TuSdkHttpEngine.shared() == null)) {
      return;
    }
    TuSdkAuth.shared().requestRemoteAuthInfo(new TuSdkAuth.AuthInfoCallback()
    {
      public void onAuthInfo(TuSdkAuthInfo paramAnonymousTuSdkAuthInfo)
      {
        SdkValid.a(SdkValid.this, false);
        if ((paramAnonymousTuSdkAuthInfo == null) || (!paramAnonymousTuSdkAuthInfo.isValid())) {
          return;
        }
        boolean bool = SdkValid.a(paramAnonymousTuSdkAuthInfo.masterKey);
        if (!bool)
        {
          TLog.e("Error while parsing lastest master config from sesrver", new Object[0]);
          StatisticsManger.appendComponent(ComponentActType.updateAppAuthActionFail);
          if (SdkValid.a(SdkValid.this) != null) {
            SdkValid.a(SdkValid.a(SdkValid.this).master);
          }
        }
        else
        {
          StatisticsManger.appendComponent(ComponentActType.updateAppAuthActionSuccess);
        }
      }
    });
  }
  
  public boolean loadFilterConfig(String paramString)
  {
    if (StringHelper.isBlank(paramString)) {
      return false;
    }
    return jniLoadFilterConfig(paramString);
  }
  
  public String loadFilterGroup(String paramString1, String paramString2)
  {
    return a(paramString1, paramString2, SdkResourceType.ResourceFilter, SdkResourceType.ResourceVideoFilter);
  }
  
  public String loadStickerGroup(String paramString1, String paramString2)
  {
    return a(paramString1, paramString2, SdkResourceType.ResourceSticker);
  }
  
  public String loadBrushGroup(String paramString1, String paramString2)
  {
    return a(paramString1, paramString2, SdkResourceType.ResourceBrush);
  }
  
  private String a(String paramString1, String paramString2, SdkResourceType paramSdkResourceType)
  {
    if ((StringHelper.isBlank(paramString1)) || (paramSdkResourceType == null)) {
      return null;
    }
    return jniLoadResource(paramString1, paramString2, paramSdkResourceType.type(), 0);
  }
  
  private String a(String paramString1, String paramString2, SdkResourceType paramSdkResourceType1, SdkResourceType paramSdkResourceType2)
  {
    if ((StringHelper.isBlank(paramString1)) || (paramSdkResourceType1 == null)) {
      return null;
    }
    return jniLoadResource(paramString1, paramString2, paramSdkResourceType1.type(), paramSdkResourceType2.type());
  }
  
  public void removeFilterGroup(long paramLong)
  {
    a(paramLong, SdkResourceType.ResourceFilter);
  }
  
  public void removeStickerGroup(long paramLong)
  {
    a(paramLong, SdkResourceType.ResourceSticker);
  }
  
  public void removeBrushGroup(long paramLong)
  {
    a(paramLong, SdkResourceType.ResourceBrush);
  }
  
  private void a(long paramLong, SdkResourceType paramSdkResourceType)
  {
    if ((paramLong < 1L) || (paramSdkResourceType == null)) {
      return;
    }
    jniRemoveResource(paramLong, paramSdkResourceType.type());
  }
  
  public Bitmap readFilterThumb(long paramLong1, long paramLong2)
  {
    return a(paramLong1, paramLong2, SdkResourceType.ResourceFilter);
  }
  
  public Bitmap readStickerThumb(long paramLong1, long paramLong2)
  {
    return a(paramLong1, paramLong2, SdkResourceType.ResourceSticker);
  }
  
  public Bitmap readBrushThumb(long paramLong1, long paramLong2)
  {
    return a(paramLong1, paramLong2, SdkResourceType.ResourceBrush);
  }
  
  private Bitmap a(long paramLong1, long paramLong2, SdkResourceType paramSdkResourceType)
  {
    if ((paramLong1 < 1L) || (paramSdkResourceType == null)) {
      return null;
    }
    return jniReadThumb(TuSdkContext.context(), paramLong1, paramLong2, paramSdkResourceType.type());
  }
  
  public List<SelesPicture> readInternalTextures(List<String> paramList)
  {
    if ((paramList == null) || (paramList.size() == 0)) {
      return null;
    }
    SelesPicture[] arrayOfSelesPicture = jniReadInternalTextures(TuSdkContext.context(), paramList.toArray());
    if ((arrayOfSelesPicture == null) || (arrayOfSelesPicture.length == 0)) {
      return null;
    }
    return Arrays.asList(arrayOfSelesPicture);
  }
  
  public List<SelesPicture> readTextures(long paramLong, List<String> paramList)
  {
    if ((paramLong < 1L) || (paramList == null) || (paramList.size() == 0)) {
      return null;
    }
    SelesPicture[] arrayOfSelesPicture = jniReadTextures(TuSdkContext.context(), paramLong, paramList.toArray());
    if ((arrayOfSelesPicture == null) || (arrayOfSelesPicture.length == 0)) {
      return null;
    }
    return Arrays.asList(arrayOfSelesPicture);
  }
  
  public String compileShader(String paramString, int paramInt, int[] paramArrayOfInt)
  {
    if ((paramString == null) || (paramArrayOfInt == null) || (paramArrayOfInt.length == 0)) {
      return null;
    }
    return jniCompileShader(paramString.trim(), paramInt, paramArrayOfInt);
  }
  
  public Bitmap readSticker(long paramLong, String paramString)
  {
    if ((paramLong < 1L) || (paramString == null)) {
      return null;
    }
    return jniReadSticker(TuSdkContext.context(), paramLong, paramString);
  }
  
  public Bitmap readBrush(long paramLong, String paramString)
  {
    if ((paramLong < 1L) || (paramString == null)) {
      return null;
    }
    return jniReadBrush(TuSdkContext.context(), paramLong, paramString);
  }
  
  public void saveLogStash(String paramString1, String paramString2)
  {
    jniSaveLogStashFile(paramString1, paramString2);
  }
  
  public String decodeMaster(String paramString)
  {
    return jniDecodeMaster(paramString);
  }
  
  private static native boolean jniInit(Context paramContext, String paramString);
  
  private static native String jniDeveloperID();
  
  private static native boolean jniLoadDevelopInfo(String paramString);
  
  private static native String jniDecodeMaster(String paramString);
  
  private static native boolean jniLoadFilterConfig(String paramString);
  
  private static native String jniLoadResource(String paramString1, String paramString2, int paramInt1, int paramInt2);
  
  private static native void jniRemoveResource(long paramLong, int paramInt);
  
  private static native Bitmap jniReadThumb(Context paramContext, long paramLong1, long paramLong2, int paramInt);
  
  private static native SelesPicture[] jniReadInternalTextures(Context paramContext, Object[] paramArrayOfObject);
  
  private static native SelesPicture[] jniReadTextures(Context paramContext, long paramLong, Object[] paramArrayOfObject);
  
  private static native String jniCompileShader(String paramString, int paramInt, int[] paramArrayOfInt);
  
  private static native Bitmap jniReadSticker(Context paramContext, long paramLong, String paramString);
  
  private static native Bitmap jniReadBrush(Context paramContext, long paramLong, String paramString);
  
  private static native boolean jniHasValidWithDevType();
  
  private static native int jniAppType();
  
  private static native boolean jniHasExpired();
  
  private static native long jniServiceExpireSeconds();
  
  private static native boolean jniPassDoubleValid();
  
  private static native int jniCheckAuthor(int paramInt);
  
  private static native boolean jniFilterAPIEnabled();
  
  private static native boolean jniFilterAPIValidWithID(long paramLong);
  
  private static native FilterWrap jniGetFilterWrapWithCode(String paramString);
  
  private static native void jniSaveLogStashFile(String paramString1, String paramString2);
  
  static
  {
    NativeLibraryHelper.shared().loadLibrary(NativeLibraryHelper.NativeLibType.LIB_CORE);
  }
  
  public static enum SdkResourceType
  {
    private int a;
    
    private SdkResourceType(int paramInt)
    {
      this.a = paramInt;
    }
    
    public int type()
    {
      return this.a;
    }
  }
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKCore-3.1.0.jar!\org\lasque\tusdk\core\secret\SdkValid.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */