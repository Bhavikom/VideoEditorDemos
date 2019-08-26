// 
// Decompiled by Procyon v0.5.36
// 

package com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.seles.extend;

import java.nio.FloatBuffer;
//import org.lasque.tusdk.core.utils.image.ImageOrientation;
import android.graphics.RectF;

import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.struct.TuSdkSize;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.image.ImageOrientation;
//import org.lasque.tusdk.core.struct.TuSdkSize;

public interface SelesVerticeCoordinateBuilder
{
    TuSdkSize outputSize();
    
    void setOutputSize(final TuSdkSize p0);
    
    void setCanvasRect(final RectF p0);
    
    boolean calculate(final TuSdkSize p0, final ImageOrientation p1, final FloatBuffer p2, final FloatBuffer p3);
}
