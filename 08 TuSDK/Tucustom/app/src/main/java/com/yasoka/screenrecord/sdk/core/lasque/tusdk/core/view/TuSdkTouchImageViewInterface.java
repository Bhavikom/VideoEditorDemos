// 
// Decompiled by Procyon v0.5.36
// 

package com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.view;

import android.graphics.RectF;
import android.widget.ImageView;
//import org.lasque.tusdk.core.utils.image.ImageOrientation;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.view.View;

import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.image.ImageOrientation;

public interface TuSdkTouchImageViewInterface
{
    void setInvalidateTargetView(final View p0);
    
    void changeRegionRatio(final Rect p0, final float p1);
    
    void setImageBitmap(final Bitmap p0, final ImageOrientation p1);
    
    void setImageBitmap(final Bitmap p0);
    
    void setImageBitmapWithAnim(final Bitmap p0);
    
    void setScaleType(final ImageView.ScaleType p0);
    
    void setZoom(final float p0);
    
    void setZoom(final float p0, final float p1, final float p2);
    
    float getCurrentZoom();
    
    RectF getZoomedRect();
    
    boolean isInAnimation();
    
    void changeImageAnimation(final LsqImageChangeType p0);
    
    ImageOrientation getImageOrientation();
    
    public enum LsqImageChangeType
    {
        TypeImageChangeUnknow, 
        TypeImageChangeTurnLeft, 
        TypeImageChangeTurnRight, 
        TypeImageChangeMirrorHorizontal, 
        TypeImageChangeMirrorVertical;
    }
}
