// 
// Decompiled by Procyon v0.5.36
// 

package com.yasoka.screenrecord.sdk.video.lasque.tusdk.core.decoder;

//import org.lasque.tusdk.core.common.TuSDKMediaUtils;
import android.media.MediaFormat;
//import org.lasque.tusdk.core.encoder.audio.TuSDKAudioEncoderSetting;
import android.annotation.SuppressLint;

import com.yasoka.screenrecord.sdk.video.lasque.tusdk.core.common.TuSDKMediaDataSource;
import com.yasoka.screenrecord.sdk.video.lasque.tusdk.core.common.TuSDKMediaUtils;
import com.yasoka.screenrecord.sdk.video.lasque.tusdk.core.encoder.audio.TuSDKAudioEncoderSetting;
//import org.lasque.tusdk.core.common.TuSDKMediaDataSource;

@SuppressLint({ "InlinedApi" })
public class TuSDKAudioInfo extends TuSDKMediaDataSource
{
    public int size;
    public int sampleRate;
    public int bitrate;
    public int channel;
    public int channelConfig;
    public int audioFormat;
    public long durationTimeUs;
    public String mime;
    
    public TuSDKAudioInfo() {
        this.sampleRate = TuSDKAudioEncoderSetting.AudioQuality.MEDIUM2.getSampleRate();
        this.bitrate = TuSDKAudioEncoderSetting.AudioQuality.MEDIUM2.getBitrate();
        this.channel = 2;
        this.channelConfig = 12;
        this.audioFormat = 2;
    }
    
    public static TuSDKAudioInfo createWithMediaFormat(final MediaFormat mediaFormat) {
        if (mediaFormat == null) {
            return null;
        }
        final TuSDKAudioInfo tuSDKAudioInfo = new TuSDKAudioInfo();
        if (mediaFormat.containsKey("channel-count")) {
            tuSDKAudioInfo.channel = mediaFormat.getInteger("channel-count");
        }
        if (tuSDKAudioInfo.channel == 1) {
            tuSDKAudioInfo.channelConfig = 4;
        }
        else {
            tuSDKAudioInfo.channelConfig = 12;
        }
        if (mediaFormat.containsKey("sample-rate")) {
            tuSDKAudioInfo.sampleRate = mediaFormat.getInteger("sample-rate");
        }
        if (mediaFormat.containsKey("bitrate")) {
            tuSDKAudioInfo.bitrate = ((mediaFormat.getInteger("bitrate") == 0) ? TuSDKAudioEncoderSetting.AudioQuality.MEDIUM2.getBitrate() : mediaFormat.getInteger("bitrate"));
        }
        if (mediaFormat.containsKey("durationUs")) {
            tuSDKAudioInfo.durationTimeUs = mediaFormat.getLong("durationUs");
        }
        if (mediaFormat.containsKey("mime")) {
            tuSDKAudioInfo.mime = mediaFormat.getString("mime");
        }
        if (mediaFormat.containsKey("bit-width")) {
            if (mediaFormat.getInteger("bit-width") == 8) {
                tuSDKAudioInfo.audioFormat = 3;
            }
            else {
                tuSDKAudioInfo.audioFormat = 2;
            }
        }
        return tuSDKAudioInfo;
    }
    
    public static TuSDKAudioInfo createWithMediaDataSource(final TuSDKMediaDataSource tuSDKMediaDataSource) {
        return createWithMediaFormat(TuSDKMediaUtils.getAudioFormat(tuSDKMediaDataSource));
    }
    
    public static TuSDKAudioInfo defaultAudioInfo() {
        return new TuSDKAudioInfo();
    }
    
    private int a() {
        if (this.audioFormat == 2) {
            return 16;
        }
        if (this.audioFormat == 3) {
            return 8;
        }
        return 16;
    }
    
    public int bytesCountOfTime(final int n) {
        return n * this.a() * this.sampleRate * this.channel / 8;
    }
    
    public long getAudioBytesPerSample() {
        return ((this.sampleRate <= 0) ? 44100 : this.sampleRate) * this.a() / 8;
    }
    
    public long frameTimeUsWithAudioSize(final int n) {
        return 1000000 * (n / this.channel) / this.getAudioBytesPerSample();
    }
    
    public long getFrameInterval() {
        return 1024000000 / ((this.sampleRate <= 0) ? 44100 : this.sampleRate);
    }
    
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("\n size |" + this.size).append("\n mime |" + this.mime).append("\n sampleRate |" + this.sampleRate).append("\n channel |" + this.channel).append("\n bitrate |" + this.bitrate).append("\n audioFormat |" + this.a());
        return sb.toString();
    }
}
