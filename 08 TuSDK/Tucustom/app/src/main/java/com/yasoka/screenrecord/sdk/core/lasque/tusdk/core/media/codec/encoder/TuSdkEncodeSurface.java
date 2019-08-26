// 
// Decompiled by Procyon v0.5.36
// 

package com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.media.codec.encoder;

//import org.lasque.tusdk.core.seles.sources.SelesWatermark;

import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.seles.sources.SelesWatermark;

public interface TuSdkEncodeSurface
{
    void newFrameReadyInGLThread(final long p0);
    
    void duplicateFrameReadyInGLThread(final long p0);
    
    boolean swapBuffers(final long p0);
    
    void requestKeyFrame();
    
    void setPause(final boolean p0);
    
    void setWatermark(final SelesWatermark p0);
    
    void flush();
}
