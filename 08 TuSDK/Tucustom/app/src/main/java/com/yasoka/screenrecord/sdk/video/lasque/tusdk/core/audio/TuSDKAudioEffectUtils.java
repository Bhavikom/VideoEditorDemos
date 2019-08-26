// 
// Decompiled by Procyon v0.5.36
// 

package com.yasoka.screenrecord.sdk.video.lasque.tusdk.core.audio;

import android.os.Process;
import android.content.Context;
//import org.lasque.tusdk.core.utils.TLog;
import android.os.Build;

import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.TLog;

import java.util.Arrays;
import java.util.List;

final class TuSDKAudioEffectUtils
{
    private static final String[] a;
    private static final String[] b;
    private static final String[] c;
    private static final String[] d;
    private static int e;
    private static boolean f;
    private static boolean g;
    private static boolean h;
    private static boolean i;
    
    public static synchronized void setBasedAcousticEchoCanceler(final boolean g) {
        TuSDKAudioEffectUtils.g = g;
    }
    
    public static synchronized void setBasedAutomaticGainControl(final boolean h) {
        TuSDKAudioEffectUtils.h = h;
    }
    
    public static synchronized void setBasedNoiseSuppressor(final boolean i) {
        TuSDKAudioEffectUtils.i = i;
    }
    
    public static synchronized boolean useBasedAcousticEchoCanceler() {
        return TuSDKAudioEffectUtils.g;
    }
    
    public static synchronized boolean useBasedAutomaticGainControl() {
        return TuSDKAudioEffectUtils.h;
    }
    
    public static synchronized boolean useBasedNoiseSuppressor() {
        return TuSDKAudioEffectUtils.i;
    }
    
    public static boolean isAcousticEchoCancelerSupported() {
        return TuSDKAudioEffects.canUseAcousticEchoCanceler();
    }
    
    public static boolean isAutomaticGainControlSupported() {
        return TuSDKAudioEffects.canUseAutomaticGainControl();
    }
    
    public static boolean isNoiseSuppressorSupported() {
        return TuSDKAudioEffects.canUseNoiseSuppressor();
    }
    
    public static synchronized void setDefaultSampleRateHz(final int e) {
        TuSDKAudioEffectUtils.f = true;
        TuSDKAudioEffectUtils.e = e;
    }
    
    public static synchronized boolean isDefaultSampleRateOverridden() {
        return TuSDKAudioEffectUtils.f;
    }
    
    public static synchronized int getDefaultSampleRateHz() {
        return TuSDKAudioEffectUtils.e;
    }
    
    public static List<String> getBlackListedModelsForAecUsage() {
        return Arrays.asList(TuSDKAudioEffectUtils.b);
    }
    
    public static List<String> getBlackListedModelsForAgcUsage() {
        return Arrays.asList(TuSDKAudioEffectUtils.c);
    }
    
    public static List<String> getBlackListedModelsForNsUsage() {
        return Arrays.asList(TuSDKAudioEffectUtils.d);
    }
    
    public static boolean runningOnGingerBreadOrHigher() {
        return Build.VERSION.SDK_INT >= 9;
    }
    
    public static boolean runningOnJellyBeanOrHigher() {
        return Build.VERSION.SDK_INT >= 16;
    }
    
    public static boolean runningOnJellyBeanMR1OrHigher() {
        return Build.VERSION.SDK_INT >= 17;
    }
    
    public static boolean runningOnJellyBeanMR2OrHigher() {
        return Build.VERSION.SDK_INT >= 18;
    }
    
    public static boolean runningOnLollipopOrHigher() {
        return Build.VERSION.SDK_INT >= 21;
    }
    
    public static boolean runningOnMarshmallowOrHigher() {
        return Build.VERSION.SDK_INT >= 23;
    }
    
    public static String getThreadInfo() {
        return "@[name=" + Thread.currentThread().getName() + ", id=" + Thread.currentThread().getId() + "]";
    }
    
    public static boolean runningOnEmulator() {
        return Build.HARDWARE.equals("goldfish") && Build.BRAND.startsWith("generic_");
    }
    
    public static boolean deviceIsBlacklistedForOpenSLESUsage() {
        return Arrays.asList(TuSDKAudioEffectUtils.a).contains(Build.MODEL);
    }
    
    public static void logDeviceInfo(final String s) {
        TLog.d("Android SDK: " + Build.VERSION.SDK_INT + ", Release: " + Build.VERSION.RELEASE + ", Brand: " + Build.BRAND + ", Device: " + Build.DEVICE + ", Id: " + Build.ID + ", Hardware: " + Build.HARDWARE + ", Manufacturer: " + Build.MANUFACTURER + ", Model: " + Build.MODEL + ", Product: " + Build.PRODUCT, new Object[0]);
    }
    
    public static boolean hasPermission(final Context context, final String s) {
        return context.checkPermission(s, Process.myPid(), Process.myUid()) == 0;
    }
    
    static {
        a = new String[0];
        b = new String[] { "D6503", "ONE A2005", "MotoG3", "Redmi 4A" };
        c = new String[] { "Nexus 10", "Nexus 9", "Redmi 4A" };
        d = new String[] { "Nexus 10", "Nexus 9", "ONE A2005", "Redmi 4A" };
        TuSDKAudioEffectUtils.e = 16000;
        TuSDKAudioEffectUtils.f = false;
        TuSDKAudioEffectUtils.g = false;
        TuSDKAudioEffectUtils.h = false;
        TuSDKAudioEffectUtils.i = false;
    }
}
