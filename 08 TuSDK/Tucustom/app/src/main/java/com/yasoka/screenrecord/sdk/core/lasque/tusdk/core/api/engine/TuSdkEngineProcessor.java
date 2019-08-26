// 
// Decompiled by Procyon v0.5.36
// 

package com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.api.engine;

//import org.lasque.tusdk.core.seles.SelesContext;

import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.seles.SelesContext;

public interface TuSdkEngineProcessor
{
    void release();
    
    void setEngineRotation(final TuSdkEngineOrientation p0);
    
    void bindEngineOutput(final TuSdkEngineOutputImage p0);
    
    void willProcessFrame(final long p0);
    
    SelesContext.SelesInput getInput();
    
    public interface TuSdkVideoProcesserFaceDetectionDelegate
    {
        void onFaceDetectionResult(final TuSdkVideoProcesserFaceDetectionResultType p0, final int p1);
    }
    
    public enum TuSdkVideoProcesserFaceDetectionResultType
    {
        TuSDKVideoProcesserFaceDetectionResultTypeFaceDetected, 
        TuSDKVideoProcesserFaceDetectionResultTypeNoFaceDetected;
    }
}
