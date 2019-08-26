// 
// Decompiled by Procyon v0.5.36
// 

package com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.media.codec.audio;

//import org.lasque.tusdk.core.utils.TLog;
//import org.lasque.tusdk.core.utils.calc.FFT;
//import org.lasque.tusdk.core.utils.Complex;

import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.Complex;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.TLog;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.calc.FFT;

public class TuSdkAudioGainData
{
    public static final double AUDIO_GAIN_RE_WEIGHT = 0.75;
    public static final double AUDIO_GAIN_IM_WEIGHT = 0.25;
    public final Complex[] gain;
    public final int length;
    
    public TuSdkAudioGainData(final int n, final float n2) {
        this.length = (int)Math.pow(2.0, Math.ceil(Math.log(n) / Math.log(2.0)));
        this.gain = new Complex[n];
        final double pow = Math.pow(10.0, ((n2 < 1.0f) ? (-1.0f / n2 * 2.0f) : ((double)(n2 * 2.0f))) / 20.0);
        int i = 0;
        final int n3 = this.length / 2;
        final int n4 = this.length - 1;
        while (i < n3) {
            this.gain[i] = new Complex(pow * 0.75, pow * 0.25);
            this.gain[n4 - i] = new Complex(this.gain[i].re(), this.gain[i].im());
            ++i;
        }
        final Complex[] ifft;
        final Complex[] array = ifft = FFT.ifft(this.gain);
        for (int length = ifft.length, j = 0; j < length; ++j) {
            ifft[j].setImZero();
        }
        System.arraycopy(FFT.fft(array), 0, this.gain, 0, this.length);
    }
    
    public void gainWithData(final Complex[] array) {
        if (array == null) {
            TLog.w("%s gainWithData need %d datas", "TuSdkAudioGainData", this.length);
            return;
        }
        final int min = Math.min(array.length, this.length);
        int i = 0;
        final int n = min / 2;
        final int n2 = min - 1;
        while (i < n) {
            array[i] = this.gain[i].times(array[i]);
            array[n2 - i] = this.gain[n2 - i].times(array[n2 - i]);
            ++i;
        }
    }
    
    public void gainWithData(final Complex[] array, final Complex[] array2) {
        if (array == null || array2 == null) {
            TLog.w("%s gainWithData need %d datas", "TuSdkAudioGainData", this.length);
            return;
        }
        final int min = Math.min(Math.min(array.length, array2.length), this.length);
        int i = 0;
        final int n = min / 2;
        final int n2 = min - 1;
        while (i < n) {
            array[i] = this.gain[i].times(array[i]);
            array[n2 - i] = this.gain[n2 - i].times(array[n2 - i]);
            array2[i] = this.gain[i].times(array2[i]);
            array2[n2 - i] = this.gain[n2 - i].times(array2[n2 - i]);
            ++i;
        }
    }
}
