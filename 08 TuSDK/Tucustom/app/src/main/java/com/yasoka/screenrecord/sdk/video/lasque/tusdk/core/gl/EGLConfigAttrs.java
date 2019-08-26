// 
// Decompiled by Procyon v0.5.36
// 

package com.yasoka.screenrecord.sdk.video.lasque.tusdk.core.gl;

import java.util.Arrays;

public class EGLConfigAttrs
{
    private int a;
    private int b;
    private int c;
    private int d;
    private int e;
    private int f;
    private int g;
    private boolean h;
    
    public EGLConfigAttrs() {
        this.a = 8;
        this.b = 8;
        this.c = 8;
        this.d = 8;
        this.e = 8;
        this.f = 4;
        this.g = 4;
        this.h = false;
    }
    
    public EGLConfigAttrs red(final int a) {
        this.a = a;
        return this;
    }
    
    public EGLConfigAttrs green(final int b) {
        this.b = b;
        return this;
    }
    
    public EGLConfigAttrs blue(final int c) {
        this.c = c;
        return this;
    }
    
    public EGLConfigAttrs alpha(final int d) {
        this.d = d;
        return this;
    }
    
    public EGLConfigAttrs depth(final int e) {
        this.e = e;
        return this;
    }
    
    public EGLConfigAttrs renderType(final int f) {
        this.f = f;
        return this;
    }
    
    public EGLConfigAttrs surfaceType(final int g) {
        this.g = g;
        return this;
    }
    
    public EGLConfigAttrs makeDefault(final boolean h) {
        this.h = h;
        return this;
    }
    
    public boolean isDefault() {
        return this.h;
    }
    
    int[] a() {
        return new int[] { 12339, this.g, 12324, this.a, 12323, this.b, 12322, this.c, 12321, this.d, 12325, this.e, 12352, this.f, 12344 };
    }
    
    @Override
    public String toString() {
        return Arrays.toString(this.a());
    }
}
