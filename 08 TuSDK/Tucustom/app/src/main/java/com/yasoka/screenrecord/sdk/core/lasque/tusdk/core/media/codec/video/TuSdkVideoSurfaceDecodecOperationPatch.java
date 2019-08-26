// 
// Decompiled by Procyon v0.5.36
// 

package com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.media.codec.video;

import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.media.codec.extend.TuSdkMediaCodec;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.media.codec.extend.TuSdkMediaCodecImpl;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.hardware.HardwareHelper;

import java.util.HashMap;
//import org.lasque.tusdk.core.media.codec.extend.TuSdkMediaCodecImpl;
//import org.lasque.tusdk.core.utils.hardware.HardwareHelper;
//import org.lasque.tusdk.core.media.codec.extend.TuSdkMediaCodec;
import java.util.Map;

public class TuSdkVideoSurfaceDecodecOperationPatch
{
    private static final Map<String, String> a;
    
    public TuSdkMediaCodec patchMediaCodec(final String s) {
        boolean matchDeviceModelAndManuFacturer = false;
        for (final Map.Entry<String, String> entry : TuSdkVideoSurfaceDecodecOperationPatch.a.entrySet()) {
            matchDeviceModelAndManuFacturer = HardwareHelper.isMatchDeviceModelAndManuFacturer(entry.getKey(), entry.getValue());
            if (matchDeviceModelAndManuFacturer) {
                break;
            }
        }
        if (!matchDeviceModelAndManuFacturer) {
            return TuSdkMediaCodecImpl.createDecoderByType(s);
        }
        TuSdkMediaCodec tuSdkMediaCodec = null;
        switch (s) {
            case "video/avc": {
                tuSdkMediaCodec = TuSdkMediaCodecImpl.createByCodecName("OMX.google.h264.decoder");
                break;
            }
            default: {
                tuSdkMediaCodec = TuSdkMediaCodecImpl.createDecoderByType(s);
                break;
            }
        }
        return tuSdkMediaCodec;
    }
    
    static {
        (a = new HashMap<String, String>()).put("BKL-AL00", "HUAWEI");
        TuSdkVideoSurfaceDecodecOperationPatch.a.put("MHA-AL00", "HUAWEI");
        TuSdkVideoSurfaceDecodecOperationPatch.a.put("FRD-AL10", "HUAWEI");
        TuSdkVideoSurfaceDecodecOperationPatch.a.put("NXT-AL10", "HUAWEI");
        TuSdkVideoSurfaceDecodecOperationPatch.a.put("PRO 6 Plus", "Meizu");
        TuSdkVideoSurfaceDecodecOperationPatch.a.put("MX6", "Meizu");
    }
}
