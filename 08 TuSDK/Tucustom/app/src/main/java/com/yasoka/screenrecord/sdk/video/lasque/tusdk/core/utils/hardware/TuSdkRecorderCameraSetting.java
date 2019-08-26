// 
// Decompiled by Procyon v0.5.36
// 

package com.yasoka.screenrecord.sdk.video.lasque.tusdk.core.utils.hardware;

import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.hardware.CameraConfigs;

public class TuSdkRecorderCameraSetting
{
    public CameraConfigs.CameraFacing facing;
    public float previewEffectScale;
    public int previewMaxSize;
    public float previewRatio;
    
    public TuSdkRecorderCameraSetting() {
        this.previewEffectScale = -1.0f;
        this.previewMaxSize = -1;
        this.previewRatio = -1.0f;
        this.facing = CameraConfigs.CameraFacing.Front;
    }
}
