// 
// Decompiled by Procyon v0.5.36
// 

package com.yasoka.screenrecord.sdk.video.lasque.tusdk.core.seles.video;

//import org.lasque.tusdk.core.utils.TuSdkWaterMarkOption;
import android.graphics.Bitmap;
import android.graphics.RectF;
import android.graphics.SurfaceTexture;
import android.opengl.EGLContext;

import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.seles.SelesContext;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.TuSdkWaterMarkOption;
import com.yasoka.screenrecord.sdk.video.lasque.tusdk.core.encoder.video.TuSDKVideoDataEncoderInterface;
//import org.lasque.tusdk.core.seles.SelesContext;
//import org.lasque.tusdk.core.encoder.video.TuSDKVideoDataEncoderInterface;

public interface

SelesSurfaceEncoderInterface extends TuSDKVideoDataEncoderInterface, SelesContext.SelesInput
{
    void startRecording(final EGLContext p0, final SurfaceTexture p1);
    
    void stopRecording();
    
    void pausdRecording();
    
    boolean isRecording();
    
    boolean isPaused();
    
    void setCropRegion(final RectF p0);
    
    void setEnableHorizontallyFlip(final boolean p0);
    
    void updateWaterMark(final Bitmap p0, final int p1, final TuSdkWaterMarkOption.WaterMarkPosition p2);
    
    void destroy();
}
