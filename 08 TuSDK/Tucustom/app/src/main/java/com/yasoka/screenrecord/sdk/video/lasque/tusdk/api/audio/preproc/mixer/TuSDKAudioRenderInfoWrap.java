// 
// Decompiled by Procyon v0.5.36
// 

package com.yasoka.screenrecord.sdk.video.lasque.tusdk.api.audio.preproc.mixer;

//import org.lasque.tusdk.core.utils.TLog;
//import org.lasque.tusdk.core.common.TuSDKMediaUtils;
import android.media.MediaFormat;
//import org.lasque.tusdk.core.media.codec.audio.TuSdkAudioInfo;
import android.annotation.SuppressLint;

import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.media.codec.audio.TuSdkAudioInfo;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.struct.TuSdkMediaDataSource;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.TLog;
import com.yasoka.screenrecord.sdk.video.lasque.tusdk.core.common.TuSDKMediaUtils;
//import org.lasque.tusdk.core.struct.TuSdkMediaDataSource;

@SuppressLint({ "InlinedApi" })
public class TuSDKAudioRenderInfoWrap extends TuSdkMediaDataSource
{
    private TuSdkAudioInfo a;
    
    public static TuSDKAudioRenderInfoWrap createWithMediaFormat(final MediaFormat mediaFormat) {
        if (mediaFormat == null) {
            return null;
        }
        final TuSDKAudioRenderInfoWrap tuSDKAudioRenderInfoWrap = new TuSDKAudioRenderInfoWrap();
        tuSDKAudioRenderInfoWrap.a = new TuSdkAudioInfo(mediaFormat);
        return tuSDKAudioRenderInfoWrap;
    }
    
    public static TuSDKAudioRenderInfoWrap createWithMediaDataSource(final TuSdkMediaDataSource tuSdkMediaDataSource) {
        return createWithMediaFormat(TuSDKMediaUtils.getAudioFormat(tuSdkMediaDataSource));
    }
    
    public static TuSDKAudioRenderInfoWrap createWithAudioInfo(final TuSdkAudioInfo a) {
        if (a == null) {
            TLog.e("%s audioInfo is null  ! ", new Object[] { "TuSDKAudioRenderInfoWrap" });
            return null;
        }
        final TuSDKAudioRenderInfoWrap tuSDKAudioRenderInfoWrap = new TuSDKAudioRenderInfoWrap();
        tuSDKAudioRenderInfoWrap.a = a;
        return tuSDKAudioRenderInfoWrap;
    }
    
    private int a() {
        return this.a.bitWidth;
    }
    
    public int bytesCountOfTime(final int n) {
        return n * this.a() * this.a.sampleRate * this.a.channelCount / 8;
    }
    
    public int bytesCountOfTimeUs(final long n) {
        return (int)(n / 1000000L * this.a() * this.a.sampleRate * this.a.channelCount / 8L);
    }
    
    public long getAudioBytesPerSample() {
        return ((this.a.sampleRate <= 0) ? 44100 : this.a.sampleRate) * this.a() / 8;
    }
    
    public long frameTimeUsWithAudioSize(final int n) {
        return 1000000 * (n / this.a.channelCount) / this.getAudioBytesPerSample();
    }
    
    public long getFrameInterval() {
        return 1024000000 / ((this.a.sampleRate <= 0) ? 44100 : this.a.sampleRate);
    }
    
    public TuSdkAudioInfo getRealAudioInfo() {
        return this.a;
    }
    
    public String toString() {
        return this.a.toString();
    }
}
