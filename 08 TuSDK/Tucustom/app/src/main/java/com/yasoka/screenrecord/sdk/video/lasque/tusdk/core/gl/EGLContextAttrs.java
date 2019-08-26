// 
// Decompiled by Procyon v0.5.36
// 

package com.yasoka.screenrecord.sdk.video.lasque.tusdk.core.gl;

public class EGLContextAttrs
{
    private int a;
    private boolean b;
    
    public EGLContextAttrs() {
        this.a = 2;
    }
    
    public EGLContextAttrs version(final int a) {
        this.a = a;
        return this;
    }
    
    public EGLContextAttrs makeDefault(final boolean b) {
        this.b = b;
        return this;
    }
    
    public boolean isDefault() {
        return this.b;
    }
    
    int[] a() {
        return new int[] { 12440, this.a, 12344 };
    }
}
