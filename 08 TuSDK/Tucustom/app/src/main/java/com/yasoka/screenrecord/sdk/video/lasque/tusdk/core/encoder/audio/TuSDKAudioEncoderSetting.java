// 
// Decompiled by Procyon v0.5.36
// 

package com.yasoka.screenrecord.sdk.video.lasque.tusdk.core.encoder.audio;

import android.annotation.SuppressLint;

@SuppressLint({ "InlinedApi" })
public class TuSDKAudioEncoderSetting
{
    protected int audioBufferQueueNum;
    public int audioFormat;
    public int sampleRate;
    public int channelConfig;
    protected int sliceSize;
    protected int bufferSize;
    public AudioQuality audioQuality;
    public int mediacodecAACProfile;
    public int mediacodecAACChannelCount;
    protected int mediacodecAACMaxInputSize;
    public boolean enableBuffers;
    
    public static TuSDKAudioEncoderSetting defaultEncoderSetting() {
        return new TuSDKAudioEncoderSetting();
    }
    
    public TuSDKAudioEncoderSetting() {
        this.mediacodecAACProfile = 2;
        this.mediacodecAACChannelCount = 2;
        this.mediacodecAACMaxInputSize = 8820;
        this.audioQuality = AudioQuality.MEDIUM1;
        this.audioBufferQueueNum = 10;
        this.audioFormat = 2;
        this.channelConfig = 12;
        this.sliceSize = this.audioQuality.getSampleRate() / 10;
        this.bufferSize = this.sliceSize * 2;
        this.sampleRate = this.audioQuality.getSampleRate();
        this.enableBuffers = false;
    }
    
    public enum AudioQuality
    {
        LOW1(44100, 18432), 
        LOW2(44100, 24576), 
        MEDIUM1(44100, 32768), 
        MEDIUM2(44100, 49152), 
        HIGH1(44100, 98304), 
        HIGH2(44100, 131072);
        
        private int a;
        private int b;
        
        private AudioQuality(final int a, final int b) {
            this.a = a;
            this.b = b;
        }
        
        public int getSampleRate() {
            return this.a;
        }
        
        public int getBitrate() {
            return this.b;
        }
    }
}
