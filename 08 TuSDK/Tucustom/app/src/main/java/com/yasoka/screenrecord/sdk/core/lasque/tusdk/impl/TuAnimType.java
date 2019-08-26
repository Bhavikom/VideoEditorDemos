// 
// Decompiled by Procyon v0.5.36
// 

package com.yasoka.screenrecord.sdk.core.lasque.tusdk.impl;

//import org.lasque.tusdk.core.TuSdkContext;
//import org.lasque.tusdk.core.type.ActivityAnimType;

import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.TuSdkContext;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.type.ActivityAnimType;

public enum TuAnimType implements ActivityAnimType
{
    empty(0, 0), 
    push("lsq_push_in", "lsq_push_out"), 
    pop("lsq_pull_in", "lsq_pull_out"), 
    up("lsq_push_y_in", 17432577), 
    down(17432576, "lsq_pull_y_out"), 
    fade("lsq_fade_in", "lsq_fade_out"), 
    upDownSub("lsq_push_sub_y_in", "lsq_pull_sub_y_out"), 
    throwRight("lsq_throw_right_in", "lsq_throw_right_out"), 
    filpLeft("lsq_flip_left_in", "lsq_flip_left_out"), 
    filpRight("lsq_flip_right_in", "lsq_flip_right_out"), 
    zoomIn("lsq_scale_in", "lsq_empty"), 
    zoomOut("lsq_empty", "lsq_scale_out");
    
    private int a;
    private int b;
    
    private TuAnimType(final int a, final int b) {
        this.a = a;
        this.b = b;
    }
    
    private TuAnimType(final String s2, final String s3) {
        this(TuSdkContext.getAnimaResId(s2), TuSdkContext.getAnimaResId(s3));
    }
    
    private TuAnimType(final String s2, final int n2) {
        this(TuSdkContext.getAnimaResId(s2), n2);
    }
    
    private TuAnimType(final int n2, final String s2) {
        this(n2, TuSdkContext.getAnimaResId(s2));
    }
    
    @Override
    public int getEnterAnim() {
        return this.a;
    }
    
    @Override
    public int getExitAnim() {
        return this.b;
    }
    
    @Override
    public int getAnim(final boolean b) {
        return b ? this.b : this.a;
    }
}
