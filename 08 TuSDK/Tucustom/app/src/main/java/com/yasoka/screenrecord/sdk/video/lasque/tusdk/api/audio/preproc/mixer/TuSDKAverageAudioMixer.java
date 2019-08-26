// 
// Decompiled by Procyon v0.5.36
// 

package com.yasoka.screenrecord.sdk.video.lasque.tusdk.api.audio.preproc.mixer;

//import org.lasque.tusdk.core.utils.TLog;

import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.TLog;

public class TuSDKAverageAudioMixer extends TuSDKAudioMixer
{
    @Override
    public byte[] mixRawAudioBytes(final byte[][] array) {
        if (array == null || array.length == 0) {
            return null;
        }
        final byte[] array2 = array[0];
        if (array.length == 1) {
            return array2;
        }
        for (int i = 0; i < array.length; ++i) {
            if (array[i].length != array2.length) {
                TLog.e("column of the road of audio + " + i + " is diffrent.", new Object[0]);
                return null;
            }
        }
        final int length = array.length;
        final int n = array2.length / 2;
        final short[][] array3 = new short[length][n];
        for (short n2 = 0; n2 < length; ++n2) {
            for (int j = 0; j < n; ++j) {
                array3[n2][j] = (short)((array[n2][j * 2] & 0xFF) | (array[n2][j * 2 + 1] & 0xFF) << 8);
            }
        }
        final short[] array4 = new short[n];
        for (int k = 0; k < n; ++k) {
            int n3 = 0;
            for (short n4 = 0; n4 < length; ++n4) {
                n3 += array3[n4][k];
            }
            array4[k] = (short)(n3 / length);
        }
        for (int l = 0; l < n; ++l) {
            array2[l * 2] = (byte)(array4[l] & 0xFF);
            array2[l * 2 + 1] = (byte)((array4[l] & 0xFF00) >> 8);
        }
        return array2;
    }
}
