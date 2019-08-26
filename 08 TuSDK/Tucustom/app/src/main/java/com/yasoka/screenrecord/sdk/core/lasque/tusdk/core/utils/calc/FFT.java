// 
// Decompiled by Procyon v0.5.36
// 

package com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.calc;

//import org.lasque.tusdk.core.utils.Complex;

import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.Complex;

public class FFT
{
    public static Complex[] fft(final Complex[] array) {
        final int length = array.length;
        if (length == 1) {
            return new Complex[] { array[0] };
        }
        if (length % 2 != 0) {
            throw new IllegalArgumentException("n is not a power of 2");
        }
        final Complex[] array2 = new Complex[length / 2];
        final Complex[] array3 = new Complex[length / 2];
        for (int i = 0; i < length / 2; ++i) {
            array2[i] = array[2 * i];
            array3[i] = array[2 * i + 1];
        }
        final Complex[] fft = fft(array2);
        final Complex[] fft2 = fft(array3);
        final Complex[] array4 = new Complex[length];
        for (int j = 0; j < length / 2; ++j) {
            final double n = -2 * j * 3.141592653589793 / length;
            final Complex complex = new Complex(Math.cos(n), Math.sin(n));
            array4[j] = fft[j].plus(complex.times(fft2[j]));
            array4[j + length / 2] = fft[j].minus(complex.times(fft2[j]));
        }
        return array4;
    }
    
    public static Complex[] ifft(final Complex[] array) {
        final int length = array.length;
        final Complex[] array2 = new Complex[length];
        for (int i = 0; i < length; ++i) {
            array2[i] = array[i].conjugate();
        }
        final Complex[] fft = fft(array2);
        for (int j = 0; j < length; ++j) {
            fft[j] = fft[j].conjugateScale(1.0 / length);
        }
        return fft;
    }
    
    public static Complex[] cconvolve(final Complex[] array, final Complex[] array2) {
        if (array.length != array2.length) {
            throw new IllegalArgumentException("Dimensions don't agree");
        }
        final int length = array.length;
        final Complex[] fft = fft(array);
        final Complex[] fft2 = fft(array2);
        final Complex[] array3 = new Complex[length];
        for (int i = 0; i < length; ++i) {
            array3[i] = fft[i].times(fft2[i]);
        }
        return ifft(array3);
    }
    
    public static Complex[] convolve(final Complex[] array, final Complex[] array2) {
        final Complex complex = new Complex(0.0, 0.0);
        final Complex[] array3 = new Complex[2 * array.length];
        for (int i = 0; i < array.length; ++i) {
            array3[i] = array[i];
        }
        for (int j = array.length; j < 2 * array.length; ++j) {
            array3[j] = complex;
        }
        final Complex[] array4 = new Complex[2 * array2.length];
        for (int k = 0; k < array2.length; ++k) {
            array4[k] = array2[k];
        }
        for (int l = array2.length; l < 2 * array2.length; ++l) {
            array4[l] = complex;
        }
        return cconvolve(array3, array4);
    }
}
