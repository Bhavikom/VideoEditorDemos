// 
// Decompiled by Procyon v0.5.36
// 

package com.yasoka.screenrecord.sdk.video.lasque.tusdk.core.audio;

//import org.lasque.tusdk.core.encoder.audio.TuSDKAudioEncoderSetting;

import com.yasoka.screenrecord.sdk.video.lasque.tusdk.core.encoder.audio.TuSDKAudioEncoderSetting;

public class TuSDKAudioCaptureSetting
{
    public int audioBufferQueueNum;
    public int audioRecoderFormat;
    public int audioRecoderSampleRate;
    public int audioRecoderChannelConfig;
    public int audioRecoderSliceSize;
    public int audioRecoderSource;
    public int audioRecoderBufferSize;
    public TuSDKAudioEncoderSetting.AudioQuality audioQuality;
    public boolean shouldEnableAec;
    public boolean shouldEnableNs;
    
    public TuSDKAudioCaptureSetting() {
        this.shouldEnableAec = true;
        this.shouldEnableNs = true;
        this.audioQuality = TuSDKAudioEncoderSetting.AudioQuality.MEDIUM1;
        this.audioBufferQueueNum = 10;
        this.audioRecoderFormat = 2;
        this.audioRecoderChannelConfig = 12;
        this.audioRecoderSliceSize = this.audioQuality.getSampleRate() / 10;
        this.audioRecoderBufferSize = this.audioRecoderSliceSize * 2;
        this.audioRecoderSampleRate = this.audioQuality.getSampleRate();
        this.audioRecoderSource = 7;
    }
    
    public static TuSDKAudioCaptureSetting defaultCaptureSetting() {
        return new TuSDKAudioCaptureSetting();
    }
}
