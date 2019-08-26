// 
// Decompiled by Procyon v0.5.36
// 

package com.yasoka.screenrecord.sdk.core.lasque.tusdk.impl.view.widget;

import android.graphics.RectF;

import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.struct.TuSdkSize;
//import org.lasque.tusdk.core.struct.TuSdkSize;

public interface RegionHandler
{
    void setRatio(final float p0);
    
    float getRatio();
    
    void setWrapSize(final TuSdkSize p0);
    
    TuSdkSize getWrapSize();
    
    RectF getRectPercent();
    
    RectF getCenterRectPercent();
    
    void setOffsetTopPercent(final float p0);
    
    float getOffsetTopPercent();
    
    RectF changeWithRatio(final float p0, final RegionChangerListener p1);
    
    public interface RegionChangerListener
    {
        void onRegionChanged(final RectF p0);
    }
}
