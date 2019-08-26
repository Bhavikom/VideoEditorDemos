// 
// Decompiled by Procyon v0.5.36
// 

package com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.task;

import android.widget.ImageView;
import android.graphics.Bitmap;
import java.util.List;

public interface FilterTaskInterface
{
    void setFilerNames(final List<String> p0);
    
    void setInputImage(final Bitmap p0);
    
    void start();
    
    void resetQueues();
    
    void appendFilterCode(final String p0);
    
    boolean isRenderFilterThumb();
    
    void setRenderFilterThumb(final boolean p0);
    
    void loadImage(final ImageView p0, final String p1);
    
    void cancelLoadImage(final ImageView p0);
}
