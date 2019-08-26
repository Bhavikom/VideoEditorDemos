// 
// Decompiled by Procyon v0.5.36
// 

package com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.seles.sources;

//import org.lasque.tusdk.core.utils.image.ImageOrientation;
//import org.lasque.tusdk.core.struct.TuSdkSize;
import android.graphics.Bitmap;

import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.struct.TuSdkSize;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.TuSdkWaterMarkOption;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.image.ImageOrientation;
//import org.lasque.tusdk.core.utils.TuSdkWaterMarkOption;

public interface SelesWatermark
{
    void setWaterPostion(final TuSdkWaterMarkOption.WaterMarkPosition p0);
    
    void setScale(final float p0);
    
    void setPadding(final float p0);
    
    void setImage(final Bitmap p0, final boolean p1);
    
    void drawInGLThread(final long p0, final TuSdkSize p1, final ImageOrientation p2);
    
    void destroy();
}
