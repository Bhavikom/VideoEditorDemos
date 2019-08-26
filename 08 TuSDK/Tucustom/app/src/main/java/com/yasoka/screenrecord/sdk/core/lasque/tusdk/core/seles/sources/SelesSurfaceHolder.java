// 
// Decompiled by Procyon v0.5.36
// 

package com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.seles.sources;

import android.graphics.SurfaceTexture;
import android.graphics.RectF;

import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.seles.extend.SelesVerticeCoordinateCorpBuilder;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.struct.TuSdkSize;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.image.ImageOrientation;
//import org.lasque.tusdk.core.seles.extend.SelesVerticeCoordinateCorpBuilder;
//import org.lasque.tusdk.core.utils.image.ImageOrientation;
//import org.lasque.tusdk.core.struct.TuSdkSize;

public interface SelesSurfaceHolder
{
    void setInputSize(final TuSdkSize p0);
    
    void setInputRotation(final ImageOrientation p0);
    
    void setTextureCoordinateBuilder(final SelesVerticeCoordinateCorpBuilder p0);
    
    void setPreCropRect(final RectF p0);
    
    boolean isInited();
    
    SurfaceTexture requestSurfaceTexture();
    
    long getSurfaceTexTimestampNs();
    
    void setSurfaceTexTimestampNs(final long p0);
}
