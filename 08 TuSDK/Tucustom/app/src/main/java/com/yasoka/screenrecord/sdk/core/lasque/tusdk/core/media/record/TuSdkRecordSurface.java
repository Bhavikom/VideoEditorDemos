// 
// Decompiled by Procyon v0.5.36
// 

package com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.media.record;

//import org.lasque.tusdk.core.seles.SelesContext;

import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.seles.SelesContext;

public interface TuSdkRecordSurface
{
    void addTarget(final SelesContext.SelesInput p0, final int p1);
    
    void removeTarget(final SelesContext.SelesInput p0);
    
    void initInGLThread();
    
    void updateSurfaceTexImage();
    
    void newFrameReadyInGLThread(final long p0);
}
