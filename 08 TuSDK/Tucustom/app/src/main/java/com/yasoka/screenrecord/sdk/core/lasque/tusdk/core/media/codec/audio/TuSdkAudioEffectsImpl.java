// 
// Decompiled by Procyon v0.5.36
// 

package com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.media.codec.audio;

import android.media.audiofx.NoiseSuppressor;
import android.media.audiofx.AutomaticGainControl;
import android.media.audiofx.AcousticEchoCanceler;
//import org.lasque.tusdk.core.utils.TLog;
import java.util.ArrayList;
import android.media.audiofx.AudioEffect;
import java.util.List;
import android.annotation.TargetApi;

import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.TLog;

@TargetApi(16)
public class TuSdkAudioEffectsImpl implements TuSdkAudioEffects
{
    private boolean a;
    private boolean b;
    private boolean c;
    private int d;
    private final List<AudioEffect> e;
    
    public TuSdkAudioEffectsImpl(final int audioSession) {
        this.d = -1;
        this.e = new ArrayList<AudioEffect>();
        this.setAudioSession(audioSession);
    }
    
    public void setAudioSession(final int d) {
        this.d = d;
    }
    
    @Override
    public void release() {
        for (final AudioEffect audioEffect : this.e) {
            try {
                audioEffect.release();
            }
            catch (Exception ex) {
                TLog.e(ex, "%s release error: %s", "TuSdkAudioEffectsImpl", audioEffect);
            }
        }
        this.e.clear();
    }
    
    @Override
    protected void finalize() {
        this.release();
        try {
            super.finalize();
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
    }
    
    @Override
    public boolean enableAcousticEchoCanceler() {
        if (this.a) {
            return true;
        }
        if (!this.a(AcousticEchoCanceler.isAvailable(), "enableAcousticEchoCanceler")) {
            return false;
        }
        final AcousticEchoCanceler create = AcousticEchoCanceler.create(this.d);
        this.a = this.a((AudioEffect)create, "AcousticEchoCanceler");
        if (this.a) {
            this.e.add((AudioEffect)create);
        }
        return this.a;
    }
    
    @Override
    public boolean enableAutomaticGainControl() {
        if (this.b) {
            return true;
        }
        if (!this.a(AutomaticGainControl.isAvailable(), "enableAutomaticGainControl")) {
            return false;
        }
        final AutomaticGainControl create = AutomaticGainControl.create(this.d);
        this.b = this.a((AudioEffect)create, "AutomaticGainControl");
        if (this.b) {
            this.e.add((AudioEffect)create);
        }
        return this.b;
    }
    
    @Override
    public boolean enableNoiseSuppressor() {
        if (this.c) {
            return true;
        }
        if (!this.a(NoiseSuppressor.isAvailable(), "enableNoiseSuppressor")) {
            return false;
        }
        final NoiseSuppressor create = NoiseSuppressor.create(this.d);
        this.c = this.a((AudioEffect)create, "NoiseSuppressor");
        if (this.c) {
            this.e.add((AudioEffect)create);
        }
        return this.c;
    }
    
    private boolean a(final boolean b, final String s) {
        if (this.d < 1) {
            TLog.w("%s %s need a AudioSession.", "TuSdkAudioEffectsImpl", s);
            return false;
        }
        if (!b) {
            TLog.w("%s can not support %s.", "TuSdkAudioEffectsImpl", s);
            return false;
        }
        return true;
    }
    
    private boolean a(final AudioEffect audioEffect, final String s) {
        if (audioEffect == null) {
            TLog.w("%s create %s failed.", "TuSdkAudioEffectsImpl", s);
            return false;
        }
        boolean enabled;
        try {
            audioEffect.setEnabled(true);
            enabled = audioEffect.getEnabled();
        }
        catch (Exception ex) {
            TLog.e(ex, "%s enable %s failed.", "TuSdkAudioEffectsImpl", s);
            return false;
        }
        return enabled;
    }
}
