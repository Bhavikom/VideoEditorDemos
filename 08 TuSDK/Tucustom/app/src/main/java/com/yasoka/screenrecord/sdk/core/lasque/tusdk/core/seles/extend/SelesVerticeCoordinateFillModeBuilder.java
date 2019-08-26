// 
// Decompiled by Procyon v0.5.36
// 

package com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.seles.extend;

//import org.lasque.tusdk.core.struct.TuSdkSize;
//import org.lasque.tusdk.core.seles.output.SelesView;

import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.seles.output.SelesView;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.struct.TuSdkSize;

public interface SelesVerticeCoordinateFillModeBuilder extends SelesVerticeCoordinateBuilder
{
    void setFillMode(final SelesView.SelesFillModeType p0);
    
    void setOnDisplaySizeChangeListener(final OnDisplaySizeChangeListener p0);
    
    public interface OnDisplaySizeChangeListener
    {
        void onDisplaySizeChanged(final TuSdkSize p0);
    }
}
