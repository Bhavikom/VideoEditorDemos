// 
// Decompiled by Procyon v0.5.36
// 

package com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.image;

public enum ImageOrientation
{
    Up(false, 0, 1, false, 0), 
    Down(false, 180, 3, false, 1), 
    Right(false, 90, 6, true, 2), 
    Left(false, 270, 8, true, 3), 
    UpMirrored(true, 0, 2, false, 4), 
    DownMirrored(true, 180, 4, false, 5), 
    RightMirrored(true, 90, 5, true, 6), 
    LeftMirrored(true, 270, 7, true, 7);
    
    private boolean a;
    private int b;
    private int c;
    private boolean d;
    private int e;
    
    private ImageOrientation(final boolean a, final int b, final int c, final boolean d, final int e) {
        this.a = a;
        this.b = b;
        this.c = c;
        this.d = d;
        this.e = e;
    }
    
    public boolean isMirrored() {
        return this.a;
    }
    
    public int getDegree() {
        return this.b;
    }
    
    public int getExifOrientation() {
        return this.c;
    }
    
    public boolean isTransposed() {
        return this.d;
    }
    
    public int getFlag() {
        return this.e;
    }
    
    public boolean isMatch(final int n, final boolean b) {
        return this.b == n && this.a == b;
    }
    
    public static ImageOrientation getValue(int n, final boolean b) {
        n %= 360;
        if (n < 0) {
            n += 360;
        }
        for (final ImageOrientation imageOrientation : values()) {
            if (imageOrientation.isMatch(n, b)) {
                return imageOrientation;
            }
        }
        return b ? ImageOrientation.UpMirrored : ImageOrientation.Up;
    }
    
    public static ImageOrientation getValue(final int n) {
        for (final ImageOrientation imageOrientation : values()) {
            if (imageOrientation.getExifOrientation() == n) {
                return imageOrientation;
            }
        }
        return ImageOrientation.Up;
    }
}
