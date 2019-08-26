// 
// Decompiled by Procyon v0.5.36
// 

package com.yasoka.screenrecord.sdk.video.lasque.tusdk.core.encoder.video;

public class TuSDKBuffSizeCalculator
{
    public static int calculator(final int n, final int n2, final int n3) {
        switch (n3) {
            case 17:
            case 19:
            case 21:
            case 842094169: {
                return n * n2 * 3 / 2;
            }
            default: {
                return -1;
            }
        }
    }
}
