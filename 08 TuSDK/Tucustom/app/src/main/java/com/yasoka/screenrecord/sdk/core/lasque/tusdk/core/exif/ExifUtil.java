// 
// Decompiled by Procyon v0.5.36
// 

package com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.exif;

import java.text.NumberFormat;

public class ExifUtil
{
    static final NumberFormat a;
    
    public static String processLensSpecifications(final Rational[] array) {
        final Rational rational = array[0];
        final Rational rational2 = array[1];
        final Rational rational3 = array[2];
        final Rational rational4 = array[3];
        ExifUtil.a.setMaximumFractionDigits(1);
        final StringBuilder sb = new StringBuilder();
        sb.append(ExifUtil.a.format(rational.toDouble()));
        sb.append("-");
        sb.append(ExifUtil.a.format(rational2.toDouble()));
        sb.append("mm f/");
        sb.append(ExifUtil.a.format(rational3.toDouble()));
        sb.append("-");
        sb.append(ExifUtil.a.format(rational4.toDouble()));
        return sb.toString();
    }
    
    static {
        a = NumberFormat.getInstance();
    }
}
