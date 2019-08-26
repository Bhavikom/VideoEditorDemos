// 
// Decompiled by Procyon v0.5.36
// 

package com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.api.engine;

import android.graphics.RectF;

import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.seles.extend.SelesVerticeCoordinateCorpBuilder;
//import org.lasque.tusdk.core.seles.extend.SelesVerticeCoordinateCorpBuilder;

public interface TuSdkEngine
{
    void release();
    
    void setEngineOrientation(final TuSdkEngineOrientation p0);
    
    void setEngineInputImage(final TuSdkEngineInputImage p0);
    
    void setEngineProcessor(final TuSdkEngineProcessor p0);
    
    void setEngineOutputImage(final TuSdkEngineOutputImage p0);
    
    void setInputTextureCoordinateBuilder(final SelesVerticeCoordinateCorpBuilder p0);
    
    void setInputPreCropRect(final RectF p0);
    
    boolean prepareInGlThread();
    
    void processFrame(final byte[] p0, final int p1, final int p2, final long p3);
    
    void processFrame(final int p0, final int p1, final int p2, final long p3);
    
    void processFrame(final int p0, final int p1, final int p2, final byte[] p3, final long p4);
}
