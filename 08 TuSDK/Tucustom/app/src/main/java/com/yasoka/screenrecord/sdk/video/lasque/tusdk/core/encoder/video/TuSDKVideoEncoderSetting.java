// 
// Decompiled by Procyon v0.5.36
// 

package com.yasoka.screenrecord.sdk.video.lasque.tusdk.core.encoder.video;

//import org.lasque.tusdk.core.utils.hardware.TuSdkGPU;
//import org.lasque.tusdk.core.struct.TuSdkSize;

import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.struct.TuSdkSize;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.hardware.TuSdkGPU;

public class TuSDKVideoEncoderSetting
{
    public TuSdkSize videoSize;
    public VideoQuality videoQuality;
    public int mediacodecAVCIFrameInterval;
    public boolean enableAllKeyFrame;
    public int mediacodecAVCColorFormat;
    public int previewColorFormat;
    public int videoBufferQueueNum;
    public int bitrateMode;
    
    public static TuSDKVideoEncoderSetting getDefaultRecordSetting() {
        final TuSDKVideoEncoderSetting tuSDKVideoEncoderSetting = new TuSDKVideoEncoderSetting();
        final int performance = TuSdkGPU.getGpuType().getPerformance();
        if (performance <= 2) {
            tuSDKVideoEncoderSetting.videoQuality = VideoQuality.RECORD_LOW2;
        }
        else if (performance == 3) {
            tuSDKVideoEncoderSetting.videoQuality = VideoQuality.RECORD_MEDIUM1;
        }
        else if (performance == 4) {
            tuSDKVideoEncoderSetting.videoQuality = VideoQuality.RECORD_MEDIUM3;
        }
        else {
            tuSDKVideoEncoderSetting.videoQuality = VideoQuality.RECORD_HIGH1;
        }
        return tuSDKVideoEncoderSetting;
    }
    
    public TuSDKVideoEncoderSetting() {
        this.bitrateMode = 2;
        this.videoSize = new TuSdkSize(320, 480);
        this.videoQuality = VideoQuality.RECORD_HIGH1;
        this.mediacodecAVCIFrameInterval = 1;
        this.enableAllKeyFrame = false;
        this.videoBufferQueueNum = 5;
        this.previewColorFormat = 17;
    }
    
    public enum VideoQuality
    {
        LIVE_LOW1(12, 150000), 
        LIVE_LOW2(15, 264000), 
        LIVE_LOW3(15, 350000), 
        LIVE_MEDIUM1(20, 512000), 
        LIVE_MEDIUM2(20, 800000), 
        LIVE_MEDIUM3(24, 1000000), 
        LIVE_HIGH1(30, 1200000), 
        LIVE_HIGH2(30, 1500000), 
        LIVE_HIGH3(30, 2000000), 
        RECORD_LOW1(30, 1200000), 
        RECORD_LOW2(30, 2400000), 
        RECORD_LOW3(30, 3600000), 
        RECORD_MEDIUM1(30, 5120000), 
        RECORD_MEDIUM2(30, 8000000), 
        RECORD_MEDIUM3(30, 10000000), 
        RECORD_HIGH1(30, 12000000), 
        RECORD_HIGH2(30, 15000000), 
        RECORD_HIGH3(30, 18000000);
        
        private int a;
        private int b;
        
        private VideoQuality(final int a, final int b) {
            this.a = a;
            this.b = b;
        }
        
        public int getFps() {
            return this.a;
        }
        
        public VideoQuality setFps(final int a) {
            this.a = a;
            return this;
        }
        
        public int getBitrate() {
            return this.b;
        }
        
        public VideoQuality setBitrate(final int b) {
            this.b = b;
            return this;
        }
        
        public VideoQuality upgrade() {
            final VideoQuality[] values = values();
            final int ordinal = this.ordinal();
            if (ordinal + 1 > VideoQuality.LIVE_HIGH3.ordinal()) {
                return VideoQuality.LIVE_HIGH3;
            }
            if (ordinal + 1 > values.length - 1) {
                return values[values.length - 1];
            }
            return values[ordinal + 1];
        }
        
        public VideoQuality degrade() {
            final VideoQuality[] values = values();
            final int ordinal = this.ordinal();
            if (ordinal - 1 < 0) {
                return values[0];
            }
            return values[ordinal - 1];
        }
        
        @Override
        public String toString() {
            return String.format("bitrate: %d | fps: %d ", this.b, this.a);
        }
    }
}
