// 
// Decompiled by Procyon v0.5.36
// 

package com.yasoka.screenrecord.sdk.video.lasque.tusdk.video.editor;

import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.seles.SelesParameters;

import java.util.List;
//import org.lasque.tusdk.core.seles.SelesParameters;

public interface TuSdkMediaEffectParameterInterface
{
    SelesParameters.FilterArg getFilterArg(final String p0);
    
    List<SelesParameters.FilterArg> getFilterArgs();
    
    void submitParameter(final String p0, final float p1);
    
    void submitParameter(final int p0, final float p1);
    
    void submitParameters();
    
    void resetParameters();
}
