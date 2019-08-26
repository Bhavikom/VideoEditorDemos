// 
// Decompiled by Procyon v0.5.36
// 

package com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.secret;

//import org.lasque.tusdk.core.utils.NativeLibraryHelper;
import java.util.Arrays;
//import org.lasque.tusdk.core.seles.sources.SelesPicture;
import java.util.List;
import android.graphics.Bitmap;
//import org.lasque.tusdk.modules.components.ComponentActType;
//import org.lasque.tusdk.core.network.TuSdkAuthInfo;
//import org.lasque.tusdk.core.network.TuSdkHttpEngine;
import android.graphics.Paint;
import android.text.TextPaint;
import android.graphics.Canvas;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
//import org.lasque.tusdk.core.utils.StringHelper;
//import org.lasque.tusdk.core.utils.TLog;
//import org.lasque.tusdk.core.utils.json.JsonHelper;
//import org.lasque.tusdk.core.TuSdkContext;
//import org.lasque.tusdk.core.TuSdkBundle;
import android.content.Context;

import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.TuSdkBundle;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.TuSdkConfigs;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.TuSdkContext;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.network.TuSdkAuthInfo;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.network.TuSdkHttpEngine;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.seles.sources.SelesPicture;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.seles.tusdk.FilterWrap;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.NativeLibraryHelper;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.StringHelper;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.TLog;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.json.JsonHelper;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.modules.components.ComponentActType;
//import org.lasque.tusdk.core.seles.tusdk.FilterWrap;
//import org.lasque.tusdk.core.TuSdkConfigs;

public class SdkValid
{
    public static final boolean isInit;
    public static final SdkValid shared;
    private TuSdkConfigs a;
    private String b;
    private boolean c;
    
    private SdkValid() {
    }
    
    private synchronized boolean b() {
        return this.c;
    }
    
    private synchronized void a(final boolean c) {
        this.c = c;
    }
    
    String a() {
        return this.b;
    }
    
    public TuSdkConfigs geTuSdkConfigs() {
        return this.a;
    }
    
    public boolean isVaild() {
        return jniHasValidWithDevType();
    }
    
    public boolean isExpired() {
        return jniHasExpired();
    }
    
    public int appType() {
        if (this.a == null) {
            jniAppType();
        }
        if (this.a.appType <= 0) {
            this.a.appType = jniAppType();
        }
        return this.a.appType;
    }
    
    public long serviceExpireSeconds() {
        return jniServiceExpireSeconds();
    }
    
    public String getDeveloperId() {
        return jniDeveloperID();
    }
    
    public int maxImageSide() {
        return jniCheckAuthor(48);
    }
    
    public int maxStickers() {
        return jniCheckAuthor(64);
    }
    
    public int localFilterCount() {
        return jniCheckAuthor(80);
    }
    
    public int localStickerCount() {
        return jniCheckAuthor(96);
    }
    
    public boolean renderFilterThumb() {
        return jniCheckAuthor(128) > 0;
    }
    
    public boolean smudgeEnabled() {
        return jniCheckAuthor(144) > 0;
    }
    
    public boolean paintEnabled() {
        return jniCheckAuthor(257) > 0;
    }
    
    public boolean wipeFilterEnabled() {
        return jniCheckAuthor(256) > 0;
    }
    
    public boolean hdrFilterEnabled() {
        return jniCheckAuthor(272) > 0;
    }
    
    public int beautyLevel() {
        return jniCheckAuthor(288);
    }
    
    public boolean videoRecordEnabled() {
        return jniCheckAuthor(4096) > 0;
    }
    
    public boolean videoDurationEnabled() {
        return jniCheckAuthor(4112) > 0;
    }
    
    public boolean videoEditEnabled() {
        return jniCheckAuthor(4128) > 0;
    }
    
    public boolean videoRecordContinuousEnabled() {
        return jniCheckAuthor(4144) > 0;
    }
    
    public boolean videoCameraShotEnabled() {
        return jniCheckAuthor(4160) > 0;
    }
    
    public boolean videoCameraStickerEnabled() {
        return jniCheckAuthor(4176) > 0;
    }
    
    public boolean videoCameraBitrateEnabled() {
        return jniCheckAuthor(4192) > 0;
    }
    
    public boolean videoCameraMonsterFaceSupport() {
        return jniCheckAuthor(4419) > 0;
    }
    
    public boolean videoEditorMusicEnabled() {
        return jniCheckAuthor(4208) > 0;
    }
    
    public boolean videoEditorStickerEnabled() {
        return jniCheckAuthor(4224) > 0;
    }
    
    public boolean videoEditorFilterEnabled() {
        return jniCheckAuthor(4240) > 0;
    }
    
    public boolean videoEditorBitrateEnabled() {
        return jniCheckAuthor(4352) > 0;
    }
    
    public boolean videoEditorResolutionEnabled() {
        return jniCheckAuthor(4368) > 0;
    }
    
    public boolean videoEditorEffectsfilterEnabled() {
        return jniCheckAuthor(4384) > 0;
    }
    
    public boolean videoEditorParticleEffectsFilterEnabled() {
        return jniCheckAuthor(4400) > 0;
    }
    
    public boolean videoEditorTextEffectsEnabled() {
        return jniCheckAuthor(4416) > 0;
    }
    
    public boolean videoEditorComicEffectsSupport() {
        return jniCheckAuthor(4417) > 0;
    }
    
    public boolean videoEditorMonsterFaceSupport() {
        return jniCheckAuthor(4418) > 0;
    }
    
    public boolean videoEditorTransitionEffectsSupport() {
        return jniCheckAuthor(4420) > 0;
    }
    
    public boolean videoImageComposeSupport() {
        return jniCheckAuthor(4421) > 0;
    }
    
    public boolean audioPitchEffectsSupport() {
        return jniCheckAuthor(4608) > 0;
    }
    
    public boolean audioResampleEffectsSupport() {
        return jniCheckAuthor(4609) > 0;
    }
    
    public boolean videoStreamEnabled() {
        return jniCheckAuthor(4432) > 0;
    }
    
    public boolean filterAPIEnabled() {
        return jniFilterAPIEnabled();
    }
    
    public boolean filterAPIValidWithID(final long n) {
        return jniFilterAPIValidWithID(n);
    }
    
    public boolean evaReplaceTxt() {
        return jniCheckAuthor(196609) > 0;
    }
    
    public boolean evaReplaceImg() {
        return jniCheckAuthor(196610) > 0;
    }
    
    public boolean evaReplaceVideo() {
        return jniCheckAuthor(196611) > 0;
    }
    
    public boolean evaReplaceAudio() {
        return jniCheckAuthor(196612) > 0;
    }
    
    public boolean evaWipeCopyright() {
        return jniCheckAuthor(196613) > 0;
    }
    
    public boolean evaExportBitratet() {
        return jniCheckAuthor(196614) > 0;
    }
    
    public boolean evaExportResolution() {
        return jniCheckAuthor(196615) > 0;
    }
    
    public boolean evaExportAddMarkimage() {
        return jniCheckAuthor(196616) > 0;
    }
    
    public FilterWrap getFilterWrapWithCode(final String s) {
        return jniGetFilterWrapWithCode(s);
    }
    
    public boolean sdkValid(final Context context, final String b, final String s) {
        if (b == null || context == null) {
            return false;
        }
        this.b = b;
        if (jniInit(context, b)) {
            this.b(s);
        }
        return this.isVaild();
    }
    
    public boolean sdkValid() {
        return jniPassDoubleValid();
    }
    
    private void b(final String key) {
        if (this.a != null) {
            return;
        }
        this.a = new TuSdkConfigs(JsonHelper.json(TuSdkContext.getAssetsText(TuSdkBundle.sdkBundleOther("lsq_tusdk_configs.json"))));
        if (this.a == null) {
            this.a = null;
            TLog.e("Configuration not found! Please see: http://tusdk.com/docs/android/get-started", new Object[0]);
            return;
        }
        String master = null;
        if (StringHelper.isNotBlank(key) && this.a.masters != null) {
            master = this.a.masters.get(key);
        }
        this.a.masters = null;
        if (master == null && StringHelper.isNotBlank(this.a.master)) {
            master = this.a.master;
        }
        if (StringHelper.isBlank(master) || master.trim().length() < 11) {
            this.a = null;
            TLog.e("Master key not found! Please see: http://tusdk.com/docs/android/get-started", new Object[0]);
            return;
        }
        final String trim = master.trim();
        this.a.master = trim;
        boolean b = jniLoadDevelopInfo(trim);
        if (!b || this.isExpired()) {
            final TuSdkAuth.LocalAuthInfo localAuthInfo = TuSdkAuth.shared().localAuthInfo();
            if (localAuthInfo != null && localAuthInfo.remoteAuthInfo != null && localAuthInfo.remoteAuthInfo.isValid()) {
                b = jniLoadDevelopInfo(localAuthInfo.remoteAuthInfo.masterKey);
            }
        }
        if (!b) {
            this.a = null;
            TLog.e("Incorrect master key! Please see: http://tusdk.com/docs/help/package-name-and-app-key", new Object[0]);
        }
    }
    
    private String a(final long n, final String s, final SdkResourceType sdkResourceType) {
        if (s == null || !StringHelper.isNotBlank(s)) {
            return null;
        }
        final JSONObject json = JsonHelper.json(SdkValid.shared.decodeMaster(s));
        if (json == null) {
            return null;
        }
        JSONArray jsonArray = null;
        try {
            switch (sdkResourceType.ordinal()) {
                case 2: {
                    jsonArray = json.getJSONArray("stickerGroups");
                }
            }
            if (jsonArray == null) {
                return null;
            }
            for (int i = 0; i < jsonArray.length(); ++i) {
                final JSONObject jsonObject = (JSONObject)jsonArray.get(i);
                if (jsonObject.getLong("id") == n) {
                    return jsonObject.getString("valid_key");
                }
            }
        }
        catch (JSONException ex) {
            ex.printStackTrace();
        }
        return null;
    }
    
    public String stickerGroupValidKey(final long n, final String s) {
        return this.a(n, s, SdkResourceType.ResourceSticker);
    }
    
    public void vaildAndDraw(final Canvas canvas) {
        if (jniCheckAuthor(16) > 0) {
            return;
        }
        final float textSize = (float)TuSdkContext.sp2px(10);
        final TextPaint textPaint = new TextPaint(1);
        textPaint.setTextSize(textSize);
        textPaint.setColor(1090519039);
        textPaint.setShadowLayer(2.0f, 1.0f, 1.0f, 1073741824);
        final Paint.FontMetricsInt fontMetricsInt = textPaint.getFontMetricsInt();
        textPaint.setTextAlign(Paint.Align.LEFT);
        canvas.drawText("Technology by TuSDK", (float)fontMetricsInt.bottom, canvas.getHeight() - textSize + fontMetricsInt.bottom, (Paint)textPaint);
    }
    
    public void checkAppAuth() {
        if (this.b() || TuSdkHttpEngine.shared() == null) {
            return;
        }
        TuSdkAuth.shared().requestRemoteAuthInfo(new TuSdkAuth.AuthInfoCallback() {
            @Override
            public void onAuthInfo(final TuSdkAuthInfo tuSdkAuthInfo) {
                SdkValid.this.a(false);
                if (tuSdkAuthInfo == null || !tuSdkAuthInfo.isValid()) {
                    return;
                }
                if (!jniLoadDevelopInfo(tuSdkAuthInfo.masterKey)) {
                    TLog.e("Error while parsing lastest master config from sesrver", new Object[0]);
                    StatisticsManger.appendComponent(ComponentActType.updateAppAuthActionFail);
                    if (SdkValid.this.a != null) {
                        jniLoadDevelopInfo(SdkValid.this.a.master);
                    }
                }
                else {
                    StatisticsManger.appendComponent(ComponentActType.updateAppAuthActionSuccess);
                }
            }
        });
    }
    
    public boolean loadFilterConfig(final String s) {
        return !StringHelper.isBlank(s) && jniLoadFilterConfig(s);
    }
    
    public String loadFilterGroup(final String s, final String s2) {
        return this.a(s, s2, SdkResourceType.ResourceFilter, SdkResourceType.ResourceVideoFilter);
    }
    
    public String loadStickerGroup(final String s, final String s2) {
        return this.a(s, s2, SdkResourceType.ResourceSticker);
    }
    
    public String loadBrushGroup(final String s, final String s2) {
        return this.a(s, s2, SdkResourceType.ResourceBrush);
    }
    
    private String a(final String s, final String s2, final SdkResourceType sdkResourceType) {
        if (StringHelper.isBlank(s) || sdkResourceType == null) {
            return null;
        }
        return jniLoadResource(s, s2, sdkResourceType.type(), 0);
    }
    
    private String a(final String s, final String s2, final SdkResourceType sdkResourceType, final SdkResourceType sdkResourceType2) {
        if (StringHelper.isBlank(s) || sdkResourceType == null) {
            return null;
        }
        return jniLoadResource(s, s2, sdkResourceType.type(), sdkResourceType2.type());
    }
    
    public void removeFilterGroup(final long n) {
        this.a(n, SdkResourceType.ResourceFilter);
    }
    
    public void removeStickerGroup(final long n) {
        this.a(n, SdkResourceType.ResourceSticker);
    }
    
    public void removeBrushGroup(final long n) {
        this.a(n, SdkResourceType.ResourceBrush);
    }
    
    private void a(final long n, final SdkResourceType sdkResourceType) {
        if (n < 1L || sdkResourceType == null) {
            return;
        }
        jniRemoveResource(n, sdkResourceType.type());
    }
    
    public Bitmap readFilterThumb(final long n, final long n2) {
        return this.a(n, n2, SdkResourceType.ResourceFilter);
    }
    
    public Bitmap readStickerThumb(final long n, final long n2) {
        return this.a(n, n2, SdkResourceType.ResourceSticker);
    }
    
    public Bitmap readBrushThumb(final long n, final long n2) {
        return this.a(n, n2, SdkResourceType.ResourceBrush);
    }
    
    private Bitmap a(final long n, final long n2, final SdkResourceType sdkResourceType) {
        if (n < 1L || sdkResourceType == null) {
            return null;
        }
        return jniReadThumb(TuSdkContext.context(), n, n2, sdkResourceType.type());
    }
    
    public List<SelesPicture> readInternalTextures(final List<String> list) {
        if (list == null || list.size() == 0) {
            return null;
        }
        final SelesPicture[] jniReadInternalTextures = jniReadInternalTextures(TuSdkContext.context(), list.toArray());
        if (jniReadInternalTextures == null || jniReadInternalTextures.length == 0) {
            return null;
        }
        return Arrays.asList(jniReadInternalTextures);
    }
    
    public List<SelesPicture> readTextures(final long n, final List<String> list) {
        if (n < 1L || list == null || list.size() == 0) {
            return null;
        }
        final SelesPicture[] jniReadTextures = jniReadTextures(TuSdkContext.context(), n, list.toArray());
        if (jniReadTextures == null || jniReadTextures.length == 0) {
            return null;
        }
        return Arrays.asList(jniReadTextures);
    }
    
    public String compileShader(final String s, final int n, final int[] array) {
        if (s == null || array == null || array.length == 0) {
            return null;
        }
        return jniCompileShader(s.trim(), n, array);
    }
    
    public Bitmap readSticker(final long n, final String s) {
        if (n < 1L || s == null) {
            return null;
        }
        return jniReadSticker(TuSdkContext.context(), n, s);
    }
    
    public Bitmap readBrush(final long n, final String s) {
        if (n < 1L || s == null) {
            return null;
        }
        return jniReadBrush(TuSdkContext.context(), n, s);
    }
    
    public void saveLogStash(final String s, final String s2) {
        jniSaveLogStashFile(s, s2);
    }
    
    public String decodeMaster(final String s) {
        return jniDecodeMaster(s);
    }
    
    private static native boolean jniInit(final Context p0, final String p1);
    
    private static native String jniDeveloperID();
    
    private static native boolean jniLoadDevelopInfo(final String p0);
    
    private static native String jniDecodeMaster(final String p0);
    
    private static native boolean jniLoadFilterConfig(final String p0);
    
    private static native String jniLoadResource(final String p0, final String p1, final int p2, final int p3);
    
    private static native void jniRemoveResource(final long p0, final int p1);
    
    private static native Bitmap jniReadThumb(final Context p0, final long p1, final long p2, final int p3);
    
    private static native SelesPicture[] jniReadInternalTextures(final Context p0, final Object[] p1);
    
    private static native SelesPicture[] jniReadTextures(final Context p0, final long p1, final Object[] p2);
    
    private static native String jniCompileShader(final String p0, final int p1, final int[] p2);
    
    private static native Bitmap jniReadSticker(final Context p0, final long p1, final String p2);
    
    private static native Bitmap jniReadBrush(final Context p0, final long p1, final String p2);
    
    private static native boolean jniHasValidWithDevType();
    
    private static native int jniAppType();
    
    private static native boolean jniHasExpired();
    
    private static native long jniServiceExpireSeconds();
    
    private static native boolean jniPassDoubleValid();
    
    private static native int jniCheckAuthor(final int p0);
    
    private static native boolean jniFilterAPIEnabled();
    
    private static native boolean jniFilterAPIValidWithID(final long p0);
    
    private static native FilterWrap jniGetFilterWrapWithCode(final String p0);
    
    private static native void jniSaveLogStashFile(final String p0, final String p1);
    
    static {
        NativeLibraryHelper.shared().loadLibrary(NativeLibraryHelper.NativeLibType.LIB_CORE);
        isInit = true;
        shared = new SdkValid();
    }
    
    public enum SdkResourceType
    {
        ResourceFilter(1), 
        ResourceSticker(2), 
        ResourceBrush(3), 
        ResourceVideoFilter(4);
        
        private int a;
        
        private SdkResourceType(final int a) {
            this.a = a;
        }
        
        public int type() {
            return this.a;
        }
    }
}
