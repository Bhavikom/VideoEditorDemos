// 
// Decompiled by Procyon v0.5.36
// 

package com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.media.codec.audio;

//import org.lasque.tusdk.core.utils.calc.FFT;
//import org.lasque.tusdk.core.utils.TLog;
//import org.lasque.tusdk.core.utils.Complex;

import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.Complex;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.TLog;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.calc.FFT;

public class TuSdkAudioData
{
    public final Complex[] left;
    public final Complex[] right;
    public final int length;
    public final int channels;
    public final int inputLength;
    
    public TuSdkAudioData(final int channels, final int inputLength) {
        this.length = (int)Math.pow(2.0, Math.ceil(Math.log(inputLength) / Math.log(2.0)));
        this.inputLength = inputLength;
        this.channels = channels;
        this.left = new Complex[inputLength];
        for (int i = inputLength; i < this.length; ++i) {
            this.left[i] = new Complex(0.0, 0.0);
        }
        if (this.channels > 1) {
            this.right = new Complex[inputLength];
            for (int j = inputLength; j < this.length; ++j) {
                this.right[j] = new Complex(0.0, 0.0);
            }
        }
        else {
            this.right = null;
        }
    }
    
    public void gainWithArgs(final TuSdkAudioGainData tuSdkAudioGainData) {
        if (tuSdkAudioGainData == null) {
            TLog.w("%s gain need gainData", "TuSdkAudioData");
            return;
        }
        if (this.channels > 1) {
            final Complex[] fft = FFT.fft(this.left);
            final Complex[] fft2 = FFT.fft(this.right);
            tuSdkAudioGainData.gainWithData(fft, fft2);
            final Complex[] ifft = FFT.ifft(fft);
            final Complex[] ifft2 = FFT.ifft(fft2);
            System.arraycopy(ifft, 0, this.left, 0, this.length);
            System.arraycopy(ifft2, 0, this.right, 0, this.length);
            return;
        }
        final Complex[] fft3 = FFT.fft(this.left);
        tuSdkAudioGainData.gainWithData(fft3);
        System.arraycopy(FFT.ifft(fft3), 0, this.left, 0, this.length);
    }
}
