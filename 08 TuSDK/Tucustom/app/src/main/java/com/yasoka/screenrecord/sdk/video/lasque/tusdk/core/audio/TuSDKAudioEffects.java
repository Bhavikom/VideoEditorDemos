// 
// Decompiled by Procyon v0.5.36
// 

package com.yasoka.screenrecord.sdk.video.lasque.tusdk.core.audio;

import android.annotation.TargetApi;
//import org.lasque.tusdk.core.utils.TLog;
import android.os.Build;
import android.media.audiofx.NoiseSuppressor;
import android.media.audiofx.AutomaticGainControl;
import android.media.audiofx.AcousticEchoCanceler;
import android.media.audiofx.AudioEffect;

import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.TLog;

import java.util.UUID;

class TuSDKAudioEffects
{
    private static final UUID a;
    private static final UUID b;
    private static final UUID c;
    private static AudioEffect.Descriptor[] d;
    private AcousticEchoCanceler e;
    private AutomaticGainControl f;
    private NoiseSuppressor g;
    private boolean h;
    private boolean i;
    private boolean j;
    
    public static boolean isAcousticEchoCancelerSupported() {
        return TuSDKAudioEffectUtils.runningOnJellyBeanOrHigher() && e();
    }
    
    public static boolean isAutomaticGainControlSupported() {
        return TuSDKAudioEffectUtils.runningOnJellyBeanOrHigher() && f();
    }
    
    public static boolean isNoiseSuppressorSupported() {
        return TuSDKAudioEffectUtils.runningOnJellyBeanOrHigher() && g();
    }
    
    public static boolean isAcousticEchoCancelerBlacklisted() {
        final boolean contains = TuSDKAudioEffectUtils.getBlackListedModelsForAecUsage().contains(Build.MODEL);
        if (contains) {
            TLog.w(Build.MODEL + " is blacklisted for HW AEC usage!", new Object[0]);
        }
        return contains;
    }
    
    public static boolean isAutomaticGainControlBlacklisted() {
        final boolean contains = TuSDKAudioEffectUtils.getBlackListedModelsForAgcUsage().contains(Build.MODEL);
        if (contains) {
            TLog.w(Build.MODEL + " is blacklisted for HW AGC usage!", new Object[0]);
        }
        return contains;
    }
    
    public static boolean isNoiseSuppressorBlacklisted() {
        final boolean contains = TuSDKAudioEffectUtils.getBlackListedModelsForNsUsage().contains(Build.MODEL);
        if (contains) {
            TLog.w("TuSDKAudioEffects", new Object[] { Build.MODEL + " is blacklisted for HW NS usage!" });
        }
        return contains;
    }
    
    @TargetApi(18)
    private static boolean b() {
        for (final AudioEffect.Descriptor descriptor : h()) {
            if (descriptor.type.equals(AudioEffect.EFFECT_TYPE_AEC) && descriptor.uuid.equals(TuSDKAudioEffects.a)) {
                TLog.w("the platform AEC should be excluded based on its UUID", new Object[0]);
                return true;
            }
        }
        return false;
    }
    
    @TargetApi(18)
    private static boolean c() {
        for (final AudioEffect.Descriptor descriptor : h()) {
            if (descriptor.type.equals(AudioEffect.EFFECT_TYPE_AGC) && descriptor.uuid.equals(TuSDKAudioEffects.b)) {
                return true;
            }
        }
        return false;
    }
    
    @TargetApi(18)
    private static boolean d() {
        for (final AudioEffect.Descriptor descriptor : h()) {
            if (descriptor.type.equals(AudioEffect.EFFECT_TYPE_NS) && descriptor.uuid.equals(TuSDKAudioEffects.c)) {
                return true;
            }
        }
        return false;
    }
    
    @TargetApi(18)
    private static boolean e() {
        return a(AudioEffect.EFFECT_TYPE_AEC);
    }
    
    @TargetApi(18)
    private static boolean f() {
        return a(AudioEffect.EFFECT_TYPE_AGC);
    }
    
    @TargetApi(18)
    private static boolean g() {
        return a(AudioEffect.EFFECT_TYPE_NS);
    }
    
    public static boolean canUseAcousticEchoCanceler() {
        final boolean b = isAcousticEchoCancelerSupported() && !TuSDKAudioEffectUtils.useBasedAcousticEchoCanceler() && !isAcousticEchoCancelerBlacklisted() && !b();
        TLog.d("canUseAcousticEchoCanceler: " + b, new Object[0]);
        return b;
    }
    
    public static boolean canUseAutomaticGainControl() {
        final boolean b = isAutomaticGainControlSupported() && !TuSDKAudioEffectUtils.useBasedAutomaticGainControl() && !isAutomaticGainControlBlacklisted() && !c();
        TLog.d("canUseAutomaticGainControl: " + b, new Object[0]);
        return b;
    }
    
    public static boolean canUseNoiseSuppressor() {
        final boolean b = isNoiseSuppressorSupported() && !TuSDKAudioEffectUtils.useBasedNoiseSuppressor() && !isNoiseSuppressorBlacklisted() && !d();
        TLog.d("canUseNoiseSuppressor: " + b, new Object[0]);
        return b;
    }
    
    static TuSDKAudioEffects a() {
        if (!TuSDKAudioEffectUtils.runningOnJellyBeanOrHigher()) {
            TLog.w("API level 16 or higher is required!", new Object[0]);
            return null;
        }
        return new TuSDKAudioEffects();
    }
    
    private TuSDKAudioEffects() {
        this.e = null;
        this.f = null;
        this.g = null;
        this.h = false;
        this.i = false;
        this.j = false;
    }
    
    public boolean setAEC(final boolean h) {
        if (!canUseAcousticEchoCanceler()) {
            TLog.w("Platform AEC is not supported", new Object[0]);
            return this.h = false;
        }
        if (this.e != null && h != this.h) {
            TLog.e("Platform AEC state can't be modified while recording", new Object[0]);
            return false;
        }
        this.h = h;
        return true;
    }
    
    public boolean setAGC(final boolean i) {
        if (!canUseAutomaticGainControl()) {
            TLog.w("Platform AGC is not supported", new Object[0]);
            return this.i = false;
        }
        if (this.f != null && i != this.i) {
            TLog.e("Platform AGC state can't be modified while recording", new Object[0]);
            return false;
        }
        this.i = i;
        return true;
    }
    
    public boolean setNS(final boolean j) {
        if (!canUseNoiseSuppressor()) {
            TLog.w("Platform NS is not supported", new Object[0]);
            return this.j = false;
        }
        if (this.g != null && j != this.j) {
            TLog.e("Platform NS state can't be modified while recording", new Object[0]);
            return false;
        }
        this.j = j;
        return true;
    }
    
    public void enable(final int n) {
        a(this.e == null);
        a(this.f == null);
        a(this.g == null);
        if (isAcousticEchoCancelerSupported()) {
            this.e = AcousticEchoCanceler.create(n);
            if (this.e != null) {
                this.e.getEnabled();
                if (this.e.setEnabled(this.h && canUseAcousticEchoCanceler()) != 0) {
                    TLog.e("Failed to set the AcousticEchoCanceler state", new Object[0]);
                }
                TLog.d("AcousticEchoCanceler: is " + (this.e.getEnabled() ? "enabled" : "disabled"), new Object[0]);
            }
            else {
                TLog.e("Failed to create the AcousticEchoCanceler instance", new Object[0]);
            }
        }
        if (isAutomaticGainControlSupported()) {
            this.f = AutomaticGainControl.create(n);
            if (this.f != null) {
                this.f.getEnabled();
                if (this.f.setEnabled(this.i && canUseAutomaticGainControl()) != 0) {
                    TLog.e("Failed to set the AutomaticGainControl state", new Object[0]);
                }
                TLog.d("AutomaticGainControl: is " + (this.f.getEnabled() ? "enabled" : "disabled"), new Object[0]);
            }
            else {
                TLog.e("Failed to create the AutomaticGainControl instance", new Object[0]);
            }
        }
        if (isNoiseSuppressorSupported()) {
            this.g = NoiseSuppressor.create(n);
            if (this.g != null) {
                this.g.getEnabled();
                if (this.g.setEnabled(this.j && canUseNoiseSuppressor()) != 0) {
                    TLog.e("Failed to set the NoiseSuppressor state", new Object[0]);
                }
                TLog.d("NoiseSuppressor: is " + (this.g.getEnabled() ? "enabled" : "disabled"), new Object[0]);
            }
            else {
                TLog.e("Failed to create the NoiseSuppressor instance", new Object[0]);
            }
        }
    }
    
    public void release() {
        if (this.e != null) {
            this.e.release();
            this.e = null;
        }
        if (this.f != null) {
            this.f.release();
            this.f = null;
        }
        if (this.g != null) {
            this.g.release();
            this.g = null;
        }
    }
    
    private static void a(final boolean b) {
        if (!b) {
            throw new AssertionError((Object)"Expected condition to be true");
        }
    }
    
    private static AudioEffect.Descriptor[] h() {
        if (TuSDKAudioEffects.d != null) {
            return TuSDKAudioEffects.d;
        }
        return TuSDKAudioEffects.d = AudioEffect.queryEffects();
    }
    
    private static boolean a(final UUID obj) {
        final AudioEffect.Descriptor[] h = h();
        if (h == null) {
            return false;
        }
        final AudioEffect.Descriptor[] array = h;
        for (int length = array.length, i = 0; i < length; ++i) {
            if (array[i].type.equals(obj)) {
                return true;
            }
        }
        return false;
    }
    
    static {
        a = UUID.fromString("bb392ec0-8d4d-11e0-a896-0002a5d5c51b");
        b = UUID.fromString("aa8130e0-66fc-11e0-bad0-0002a5d5c51b");
        c = UUID.fromString("c06c8400-8e06-11e0-9cb6-0002a5d5c51b");
        TuSDKAudioEffects.d = null;
    }
}
