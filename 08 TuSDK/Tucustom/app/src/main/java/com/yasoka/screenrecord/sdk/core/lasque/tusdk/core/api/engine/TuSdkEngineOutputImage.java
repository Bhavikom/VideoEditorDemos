// 
// Decompiled by Procyon v0.5.36
// 

package com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.api.engine;

//import org.lasque.tusdk.core.seles.SelesContext;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.seles.SelesContext;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.type.ColorFormatType;

import java.util.List;
//import org.lasque.tusdk.core.type.ColorFormatType;

public interface TuSdkEngineOutputImage
{
    void release();
    
    void willProcessFrame(final long p0);
    
    void setEngineRotation(final TuSdkEngineOrientation p0);
    
    int getTerminalTexture();
    
    void setYuvOutputImageFormat(final ColorFormatType p0);
    
    List<SelesContext.SelesInput> getInputs();
    
    void setEnableOutputYUVData(final boolean p0);
    
    void snatchFrame(final byte[] p0);
}
