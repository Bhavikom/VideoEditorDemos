// 
// Decompiled by Procyon v0.5.36
// 

package com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.media.codec.video;

import java.util.Arrays;
import java.util.HashMap;
import android.os.Build;
//import org.lasque.tusdk.core.media.codec.extend.TuSdkMediaFormat;
import android.media.MediaFormat;
//import org.lasque.tusdk.core.media.codec.extend.TuSdkMediaCodecImpl;
//import org.lasque.tusdk.core.utils.hardware.HardwareHelper;
//import org.lasque.tusdk.core.media.codec.extend.TuSdkMediaCodec;
import java.util.List;
import java.util.Map;
import android.annotation.TargetApi;

import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.media.codec.extend.TuSdkMediaCodec;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.media.codec.extend.TuSdkMediaCodecImpl;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.media.codec.extend.TuSdkMediaFormat;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.hardware.HardwareHelper;

@TargetApi(18)
public class TuSdkVideoSurfaceEncodecOperationPatch
{
    private static final Map<String, String> a;
    private static final List<String> b;
    
    public TuSdkMediaCodec patchMediaCodec() {
        boolean matchDeviceModelAndManuFacturer = false;
        for (final Map.Entry<String, String> entry : TuSdkVideoSurfaceEncodecOperationPatch.a.entrySet()) {
            matchDeviceModelAndManuFacturer = HardwareHelper.isMatchDeviceModelAndManuFacturer(entry.getKey(), entry.getValue());
            if (matchDeviceModelAndManuFacturer) {
                break;
            }
        }
        if (!matchDeviceModelAndManuFacturer) {
            return null;
        }
        return TuSdkMediaCodecImpl.createByCodecName("OMX.google.h264.encoder");
    }
    
    public boolean patchRequestKeyFrame(final MediaFormat mediaFormat) {
        if (mediaFormat == null) {
            return false;
        }
        final int integer = TuSdkMediaFormat.getInteger(mediaFormat, "i-frame-interval", 0);
        final boolean enableKeyFrameASAP = TuSdkMediaFormat.isEnableKeyFrameASAP(mediaFormat);
        if (integer < 0) {
            mediaFormat.setInteger("i-frame-interval", 0);
        }
        final String manufacturer = Build.MANUFACTURER;
        if (manufacturer != null && TuSdkVideoSurfaceEncodecOperationPatch.b.contains(manufacturer.toUpperCase()) && integer < 1) {
            mediaFormat.setInteger("i-frame-interval", 1);
        }
        return enableKeyFrameASAP;
    }
    
    public boolean isEnableCompatibilityMode() {
        return HardwareHelper.isMatchDeviceModelAndManuFacturer("MI NOTE LTE", "Xiaomi");
    }
    
    static {
        (a = new HashMap<String, String>()).put("FRD-AL00", "HUAWEI");
        TuSdkVideoSurfaceEncodecOperationPatch.a.put("MHA-AL00", "HUAWEI");
        TuSdkVideoSurfaceEncodecOperationPatch.a.put("BKL-AL00", "HUAWEI");
        TuSdkVideoSurfaceEncodecOperationPatch.a.put("FRD-AL10", "HUAWEI");
        TuSdkVideoSurfaceEncodecOperationPatch.a.put("NXT-AL10", "HUAWEI");
        TuSdkVideoSurfaceEncodecOperationPatch.a.put("BND-AL10", "HUAWEI");
        TuSdkVideoSurfaceEncodecOperationPatch.a.put("BND-TL10", "HUAWEI");
        TuSdkVideoSurfaceEncodecOperationPatch.a.put("MP1605", "Meitu");
        TuSdkVideoSurfaceEncodecOperationPatch.a.put("Nexus 6P", "HUAWEI");
        TuSdkVideoSurfaceEncodecOperationPatch.a.put("HUAWEI NXT-AL10", "HUAWEI");
        TuSdkVideoSurfaceEncodecOperationPatch.a.put("KNT-AL10", "HUAWEI");
        TuSdkVideoSurfaceEncodecOperationPatch.a.put("KNT-AL20", "HUAWEI");
        TuSdkVideoSurfaceEncodecOperationPatch.a.put("ANE-TL00", "HUAWEI");
        TuSdkVideoSurfaceEncodecOperationPatch.a.put("MX6", "MeiZu");
        TuSdkVideoSurfaceEncodecOperationPatch.a.put("LLD-AL10", "HUAWEI");
        TuSdkVideoSurfaceEncodecOperationPatch.a.put("WAS-AL00", "HUAWEI");
        b = Arrays.asList("XIAOMI", "OPPO");
    }
}
