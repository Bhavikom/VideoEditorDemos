// 
// Decompiled by Procyon v0.5.36
// 

package com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.struct;

import android.view.ViewGroup;
import android.view.View;

public class ViewSize extends TuSdkSize
{
    public static ViewSize create(final View view) {
        return new ViewSize(view);
    }
    
    public ViewSize() {
    }
    
    public ViewSize(final int n, final int n2) {
        super(n, n2);
    }
    
    public ViewSize(final View view) {
        if (view == null) {
            return;
        }
        this.width = view.getWidth();
        this.height = view.getHeight();
        final ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
        if (layoutParams == null) {
            return;
        }
        if (this.width < 1) {
            this.width = layoutParams.width;
        }
        if (this.height < 1) {
            this.height = layoutParams.height;
        }
    }
}
