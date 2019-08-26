// 
// Decompiled by Procyon v0.5.36
// 

package com.yasoka.screenrecord.sdk.video.lasque.tusdk.api.audio.preproc.mixer;

import java.util.Iterator;
//import org.lasque.tusdk.core.utils.TLog;
import android.media.MediaCodec;
import java.nio.ByteBuffer;
import android.os.Environment;
import java.util.ArrayList;
import java.util.List;
import android.annotation.TargetApi;

import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.api.extend.TuSdkAudioRender;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.TLog;
//import org.lasque.tusdk.core.api.extend.TuSdkAudioRender;

@TargetApi(16)
public class TuSdkMixerRender implements TuSdkAudioRender
{
    private List<TuSDKAudioMixerRender.RawAudioTrack> a;
    private boolean b;
    private String c;
    private float d;
    private float e;
    private long f;
    private boolean g;
    
    public TuSdkMixerRender() {
        this.a = new ArrayList<TuSDKAudioMixerRender.RawAudioTrack>();
        this.b = true;
        this.c = Environment.getExternalStorageDirectory().getAbsolutePath() + "/lsq_lastTemp_audio_PCM";
        this.d = 1.0f;
        this.e = 1.0f;
        this.f = -1L;
        this.g = true;
    }
    
    public boolean onAudioSliceRender(final ByteBuffer byteBuffer, final MediaCodec.BufferInfo bufferInfo, final TuSdkAudioRender.TuSdkAudioRenderCallback tuSdkAudioRenderCallback) {
        if (!this.g) {
            return false;
        }
        if (tuSdkAudioRenderCallback == null || tuSdkAudioRenderCallback.isEncodec()) {
            return false;
        }
        if (bufferInfo == null || byteBuffer == null) {
            return false;
        }
        final byte[][] array = new byte[this.a.size() + 1][];
        byteBuffer.get(array[0] = new byte[bufferInfo.size]);
        array[0] = this.a(array[0], this.d);
        if (this.a.size() == 0) {
            tuSdkAudioRenderCallback.returnRenderBuffer(ByteBuffer.wrap(this.a(array)), bufferInfo);
            return true;
        }
        this.f = bufferInfo.presentationTimeUs;
        for (int i = 0; i < this.a.size(); ++i) {
            final byte[] array2 = new byte[bufferInfo.size];
            if (this.a.get(i).a(bufferInfo.presentationTimeUs, array2) != -1) {
                array[i + 1] = this.a(array2, (this.a.get(i).a.getVolume() == 1.0f) ? this.e : this.a.get(i).a.getVolume());
            }
            else {
                array[i + 1] = array2;
            }
        }
        tuSdkAudioRenderCallback.returnRenderBuffer(ByteBuffer.wrap(this.a(array)), bufferInfo);
        return true;
    }
    
    public void setRawAudioTrackList(final List<TuSDKAudioMixerRender.RawAudioTrack> list) {
        if (list == null || list.size() == 0) {
            return;
        }
        this.a.clear();
        this.a.addAll(list);
    }
    
    public void seekTo(final long n) {
        final Iterator<TuSDKAudioMixerRender.RawAudioTrack> iterator = this.a.iterator();
        while (iterator.hasNext()) {
            iterator.next().seekUs(n);
        }
    }
    
    public void setTrunkAudioVolume(final float d) {
        this.d = d;
    }
    
    public float getTrunkVolume() {
        return this.d;
    }
    
    public void setSecondAudioTrack(final float e) {
        this.e = e;
    }
    
    public float getSecondVolume() {
        return this.e;
    }
    
    private byte[] a(final byte[] array, final float n) {
        final byte[] array2 = new byte[array.length];
        for (int i = 0; i < array2.length; i += 2) {
            final short n2 = (short)((short)((short)((array[i + 1] & 0xFF) << 8) | (short)(array[i] & 0xFF)) * n);
            array2[i] = (byte)n2;
            array2[i + 1] = (byte)(n2 >> 8);
        }
        return array2;
    }
    
    private byte[] a(final byte[][] array) {
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
    
    public void clearAllAudioData() {
        this.a.clear();
    }
    
    public void setEnable(final boolean g) {
        this.g = g;
    }
}
