// 
// Decompiled by Procyon v0.5.36
// 

package com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.hardware;

public enum InterfaceOrientation
{
    Portrait(0, 0, 0, false, 315, 45), 
    LandscapeRight(1, 90, 1, true, 45, 135), 
    PortraitUpsideDown(2, 180, 2, false, 135, 225), 
    LandscapeLeft(3, 270, 3, true, 225, 315);
    
    private int a;
    private int b;
    private int c;
    private boolean d;
    private int e;
    private int f;
    
    private InterfaceOrientation(final int a, final int c, final int b, final boolean d, final int e, final int f) {
        this.a = a;
        this.c = c;
        this.b = b;
        this.d = d;
        this.e = e;
        this.f = f;
    }
    
    public int getFlag() {
        return this.a;
    }
    
    public int getSurfaceRotation() {
        return this.b;
    }
    
    public int getDegree() {
        return this.c;
    }
    
    public boolean isTransposed() {
        return this.d;
    }
    
    public int viewDegree() {
        int n = 360 - this.c;
        if (n == 360) {
            n = 0;
        }
        return n;
    }
    
    public int[] viewFromToDegree(final int n) {
        final int[] array = { n, this.viewDegree() };
        if (array[0] == 270 && array[1] == 0) {
            array[1] = 360;
        }
        else if (array[0] == 0 && array[1] == 270) {
            array[0] = 360;
        }
        return array;
    }
    
    public boolean isMatch(final int n) {
        if (this.c > 0) {
            if (n >= this.e && n < this.f) {
                return true;
            }
        }
        else if (n >= this.e || n < this.f) {
            return true;
        }
        return false;
    }
    
    public static InterfaceOrientation getWithSurfaceRotation(final int n) {
        for (final InterfaceOrientation interfaceOrientation : values()) {
            if (interfaceOrientation.getSurfaceRotation() == n) {
                return interfaceOrientation;
            }
        }
        return InterfaceOrientation.Portrait;
    }
    
    public static InterfaceOrientation getWithDegrees(int n) {
        n %= 360;
        if (n < 0) {
            n += 360;
        }
        for (final InterfaceOrientation interfaceOrientation : values()) {
            if (interfaceOrientation.getDegree() == n) {
                return interfaceOrientation;
            }
        }
        return InterfaceOrientation.Portrait;
    }
}
