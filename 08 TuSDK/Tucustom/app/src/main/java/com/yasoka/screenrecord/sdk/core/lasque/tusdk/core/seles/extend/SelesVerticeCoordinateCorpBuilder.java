// 
// Decompiled by Procyon v0.5.36
// 

package com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.seles.extend;

//import org.lasque.tusdk.core.struct.TuSdkSize;
import android.graphics.RectF;

import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.struct.TuSdkSize;

public interface SelesVerticeCoordinateCorpBuilder extends SelesVerticeCoordinateBuilder
{
    void setCropRect(final RectF p0);
    
    void setPreCropRect(final RectF p0);
    
    void setEnableClip(final boolean p0);
    
    TuSdkSize setOutputRatio(final float p0);
    
    float getOutputRatio(final float p0);
}
