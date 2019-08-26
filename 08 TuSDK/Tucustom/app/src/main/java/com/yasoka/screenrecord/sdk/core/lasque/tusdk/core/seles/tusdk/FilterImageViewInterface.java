// 
// Decompiled by Procyon v0.5.36
// 

package com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.seles.tusdk;

import android.graphics.Bitmap;

public interface FilterImageViewInterface
{
    void setImage(final Bitmap p0);
    
    void setImageBackgroundColor(final int p0);
    
    void setFilterWrap(final FilterWrap p0);
    
    void enableTouchForOrigin();
    
    void disableTouchForOrigin();
    
    void requestRender();
}
