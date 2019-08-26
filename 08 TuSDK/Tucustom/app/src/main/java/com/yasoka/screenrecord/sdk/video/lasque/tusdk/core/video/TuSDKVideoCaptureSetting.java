// 
// Decompiled by Procyon v0.5.36
// 

package com.yasoka.screenrecord.sdk.video.lasque.tusdk.core.video;

//import org.lasque.tusdk.core.encoder.video.TuSDKVideoEncoderSetting;
//import org.lasque.tusdk.core.type.ColorFormatType;
//import org.lasque.tusdk.core.struct.TuSdkSize;
//import org.lasque.tusdk.core.utils.hardware.CameraConfigs;

import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.struct.TuSdkSize;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.type.ColorFormatType;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.hardware.CameraConfigs;
import com.yasoka.screenrecord.sdk.video.lasque.tusdk.core.encoder.video.TuSDKVideoEncoderSetting;

public class TuSDKVideoCaptureSetting
{
    public CameraConfigs.CameraFacing facing;
    public TuSdkSize videoSize;
    public int fps;
    public AVCodecType videoAVCodecType;
    public ColorFormatType imageFormatType;
    
    public TuSDKVideoCaptureSetting() {
        this.facing = CameraConfigs.CameraFacing.Front;
        this.videoSize = new TuSdkSize(320, 480);
        this.fps = TuSDKVideoEncoderSetting.VideoQuality.LIVE_MEDIUM3.getFps();
        this.videoAVCodecType = AVCodecType.HW_CODEC;
        this.imageFormatType = ColorFormatType.NV21;
    }
    
    public enum AVCodecType
    {
        HW_CODEC, 
        SW_CODEC, 
        CUSTOM_CODEC;
    }
}
