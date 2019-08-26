// 
// Decompiled by Procyon v0.5.36
// 

package com.yasoka.screenrecord.sdk.video.lasque.tusdk.core.utils.hardware;

//import org.lasque.tusdk.core.media.codec.video.TuSdkVideoQuality;
//import org.lasque.tusdk.core.struct.TuSdkSize;

import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.media.codec.video.TuSdkVideoQuality;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.struct.TuSdkSize;

public class TuSdkRecorderVideoEncoderSetting
{
    public TuSdkSize videoSize;
    public TuSdkVideoQuality videoQuality;
    public int mediacodecAVCIFrameInterval;
    public boolean enableAllKeyFrame;
    
    public TuSdkRecorderVideoEncoderSetting() {
        this.videoSize = new TuSdkSize(320, 480);
        this.videoQuality = TuSdkVideoQuality.RECORD_HIGH2;
        this.mediacodecAVCIFrameInterval = 1;
        this.enableAllKeyFrame = false;
    }
    
    public static TuSdkRecorderVideoEncoderSetting getDefaultRecordSetting() {
        final TuSdkRecorderVideoEncoderSetting tuSdkRecorderVideoEncoderSetting = new TuSdkRecorderVideoEncoderSetting();
        tuSdkRecorderVideoEncoderSetting.videoQuality = TuSdkVideoQuality.safeQuality();
        return tuSdkRecorderVideoEncoderSetting;
    }
}
