// 
// Decompiled by Procyon v0.5.36
// 

package com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.task;

import android.graphics.Bitmap;

public class FiltersTaskImageResult
{
    private Bitmap a;
    private String b;
    
    public Bitmap getImage() {
        return this.a;
    }
    
    public void setImage(final Bitmap a) {
        this.a = a;
    }
    
    public String getFilterName() {
        return this.b;
    }
    
    public void setFilterName(final String b) {
        this.b = b;
    }
}
