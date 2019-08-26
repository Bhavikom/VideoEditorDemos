// 
// Decompiled by Procyon v0.5.36
// 

package com.yasoka.screenrecord.sdk.video.lasque.tusdk.api.audio.preproc.mixer;

//import org.lasque.tusdk.video.editor.TuSdkTimeRange;
import java.io.RandomAccessFile;
import java.io.IOException;
import android.media.MediaCodec;
import java.nio.ByteBuffer;
import java.util.Iterator;
//import org.lasque.tusdk.video.editor.TuSdkMediaAudioEffectData;
import java.io.FileNotFoundException;
//import org.lasque.tusdk.core.utils.TLog;
import java.util.ArrayList;
import android.os.Environment;

import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.api.extend.TuSdkAudioRender;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.TLog;
import com.yasoka.screenrecord.sdk.video.lasque.tusdk.core.decoder.TuSDKAudioDecoderTaskManager;
import com.yasoka.screenrecord.sdk.video.lasque.tusdk.video.editor.TuSdkMediaAudioEffectData;
import com.yasoka.screenrecord.sdk.video.lasque.tusdk.video.editor.TuSdkTimeRange;

import java.util.List;
//import org.lasque.tusdk.core.decoder.TuSDKAudioDecoderTaskManager;
import java.io.FileOutputStream;
//import org.lasque.tusdk.core.api.extend.TuSdkAudioRender;

public class TuSDKAudioMixerRender implements TuSdkAudioRender
{
    private boolean a;
    private String b;
    private FileOutputStream c;
    private TuSDKAudioDecoderTaskManager d;
    private List<TuSDKAudioRenderEntry> e;
    private List<RawAudioTrack> f;
    private TuSDKAudioRenderInfoWrap g;
    private float h;
    private float i;
    private TuSdkOnMixerRenderStateListener j;
    private TuSDKAudioMixerState k;
    private TuSDKAudioDecoderTaskManager.TuSDKAudioDecoderTaskStateListener l;
    private long m;
    
    public void setWirteTempFile(final boolean a) {
        this.a = a;
    }
    
    public TuSDKAudioMixerRender() {
        this.a = true;
        this.b = Environment.getExternalStorageDirectory().getAbsolutePath() + "/lsq_lastTemp_audio_PCM";
        this.e = new ArrayList<TuSDKAudioRenderEntry>();
        this.h = 1.0f;
        this.i = 1.0f;
        this.k = TuSDKAudioMixerState.None;
        this.l = new TuSDKAudioDecoderTaskManager.TuSDKAudioDecoderTaskStateListener() {
            @Override
            public void onStateChanged(final TuSDKAudioDecoderTaskManager.State state) {
                if (state == TuSDKAudioDecoderTaskManager.State.Idle) {
                    TuSDKAudioMixerRender.this.notifyStateChanged(TuSDKAudioMixerState.None);
                }
                else if (state == TuSDKAudioDecoderTaskManager.State.Decoding) {
                    TuSDKAudioMixerRender.this.notifyStateChanged(TuSDKAudioMixerState.Loading);
                }
                else if (state == TuSDKAudioDecoderTaskManager.State.Complete) {
                    TuSDKAudioMixerRender.this.notifyStateChanged(TuSDKAudioMixerState.Loaded);
                }
                else if (state == TuSDKAudioDecoderTaskManager.State.Cancelled) {
                    TuSDKAudioMixerRender.this.notifyStateChanged(TuSDKAudioMixerState.DecodeCancel);
                }
            }
        };
        this.m = -1L;
        this.d = new TuSDKAudioDecoderTaskManager();
        try {
            this.c = new FileOutputStream(this.b);
        }
        catch (FileNotFoundException ex) {
            TLog.e((Throwable)ex);
        }
    }
    
    public void setAudioRenderEntryList(final List<TuSDKAudioRenderEntry> list) {
        if (list == null || list.size() == 0) {
            TLog.e("%s mix audio list is null ", new Object[] { "TuSDKAudioMixerRender" });
            return;
        }
        this.e.clear();
        this.e.addAll(list);
    }
    
    public void notifyAudioDataChanged(final List<TuSdkMediaAudioEffectData> list) {
        if (list != null || list.size() > 0) {
            final ArrayList<TuSDKAudioRenderEntry> list2 = new ArrayList<TuSDKAudioRenderEntry>();
            final Iterator<TuSdkMediaAudioEffectData> iterator = list.iterator();
            while (iterator.hasNext()) {
                list2.add(iterator.next().getAudioEntry());
            }
            if (this.e.containsAll(list2)) {
                return;
            }
            this.e.clear();
            this.e.addAll(list2);
            if (this.f != null) {
                this.f.clear();
            }
        }
    }
    
    public void clearAllAudioData() {
        this.e.clear();
        if (this.f != null) {
            this.f.clear();
        }
    }
    
    public List<TuSDKAudioRenderEntry> getAudioRenderEntryList() {
        return this.e;
    }
    
    public void loadAudio() {
        if (this.g == null) {
            TLog.e("%s  You have to set up TrunkAudioInfo before this.", new Object[] { "TuSDKAudioMixerRender" });
            return;
        }
        this.d.setAudioEntry(this.e);
        this.d.setDelegate(this.l);
        if (this.g != null) {
            this.d.setTrunkAudioInfo(this.g.getRealAudioInfo());
        }
        this.d.start();
    }
    
    public void cancel() {
        this.d.cancel();
    }
    
    public void setMixerRenderStateListener(final TuSdkOnMixerRenderStateListener j) {
        this.j = j;
    }
    
    public void seekTo(final long n) {
        final Iterator<RawAudioTrack> iterator = this.a().iterator();
        while (iterator.hasNext()) {
            iterator.next().seekUs(n);
        }
    }
    
    public void closeReaders() {
        final Iterator<RawAudioTrack> iterator = this.a().iterator();
        while (iterator.hasNext()) {
            iterator.next().b();
        }
    }
    
    private List<RawAudioTrack> a() {
        if (this.f == null || this.f.size() != this.e.size()) {
            this.f = new ArrayList<RawAudioTrack>();
            for (int i = 0; i < this.e.size(); ++i) {
                this.f.add(new RawAudioTrack(this.e.get(i)));
            }
        }
        return this.f;
    }
    
    public void setTrunkAudioVolume(final float i) {
        this.i = i;
    }
    
    public void setSecondAudioTrack(final float h) {
        this.h = h;
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
    
    public synchronized boolean onAudioSliceRender(final ByteBuffer byteBuffer, final MediaCodec.BufferInfo bufferInfo, final TuSdkAudioRender.TuSdkAudioRenderCallback tuSdkAudioRenderCallback) {
        if (tuSdkAudioRenderCallback == null || tuSdkAudioRenderCallback.isEncodec()) {
            return false;
        }
        if (bufferInfo == null || byteBuffer == null || this.e == null || this.k == TuSDKAudioMixerState.Loading) {
            return false;
        }
        if (bufferInfo.presentationTimeUs == this.m) {
            return false;
        }
        this.m = bufferInfo.presentationTimeUs;
        this.notifyStateChanged(TuSDKAudioMixerState.Mixing);
        final byte[][] array = new byte[this.a().size() + 1][];
        byteBuffer.get(array[0] = new byte[bufferInfo.size]);
        array[0] = this.a(array[0], this.i);
        for (int i = 0; i < this.a().size(); ++i) {
            final byte[] array2 = new byte[bufferInfo.size];
            if (this.a().get(i).a(bufferInfo.presentationTimeUs, array2) != -1) {
                array[i + 1] = this.a(array2, this.h);
            }
            else {
                array[i + 1] = array2;
            }
        }
        final byte[] a = this.a(array);
        this.a(a);
        tuSdkAudioRenderCallback.returnRenderBuffer(ByteBuffer.wrap(a), bufferInfo);
        return true;
    }
    
    private void a(final byte[] b) {
        if (this.a) {
            try {
                this.c.write(b);
            }
            catch (IOException ex) {
                ex.printStackTrace();
            }
        }
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
    
    public void notifyStateChanged(final TuSDKAudioMixerState k) {
        if (this.k == k) {
            return;
        }
        this.k = k;
        if (this.j == null) {
            return;
        }
        this.j.onMixerStateChanged(k);
    }
    
    public TuSDKAudioMixerState getState() {
        return this.k;
    }
    
    public void setTrunkAudioInfo(final TuSDKAudioRenderInfoWrap g) {
        if (g == null) {
            TLog.e("%s set trunk audioInfo is null !!!", new Object[] { "TuSDKAudioMixerRender" });
            return;
        }
        this.g = g;
    }
    
    public static class RawAudioTrack
    {
        private RandomAccessFile c;
        TuSDKAudioRenderEntry a;
        private boolean d;
        int b;
        
        public RawAudioTrack(final TuSDKAudioRenderEntry a) {
            this.d = false;
            this.b = 0;
            this.a = a;
        }
        
        private RandomAccessFile a() {
            if (this.c == null) {
                try {
                    this.c = new RandomAccessFile(this.a.getRawInfo().getPath(), "r");
                }
                catch (FileNotFoundException ex) {
                    TLog.e((Throwable)ex);
                }
            }
            return this.c;
        }
        
        int a(final long n, final byte[] array) {
            final TuSdkTimeRange timeRange = this.a.getTimeRange();
            if (timeRange != null && !timeRange.contains(n) && !this.a.isLooping()) {
                TLog.e("Not contains timeRange : %s", new Object[] { timeRange });
                return -1;
            }
            if (timeRange != null) {
                if (timeRange.contains(n) && this.b != -1) {
                    try {
                        this.b = this.a().read(array);
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }
                }
                else if (this.b == -1 && this.a.isLooping()) {
                    try {
                        this.a().seek(0L);
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }
                    try {
                        this.b = this.a().read(array);
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }
                }
            }
            else {
                try {
                    this.b = this.a().read(array);
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
                if (this.b == -1 && this.a.isLooping()) {
                    try {
                        this.a().seek(0L);
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }
                    try {
                        this.b = this.a().read(array);
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }
                }
            }
            return this.b;
        }
        
        public void seekUs(final long n) {
            this.b = 0;
            try {
                if (this.a.getTimeRange() == null) {
                    int n2;
                    if (n > this.a.getRawInfo().getRealAudioInfo().durationUs) {
                        n2 = this.a.getRawInfo().bytesCountOfTimeUs(this.a().length());
                    }
                    else {
                        n2 = this.a.getRawInfo().bytesCountOfTimeUs(n);
                    }
                    this.a().seek(n2);
                }
                else if (this.a.getTimeRange().contains(n - this.a.getTimeRange().getStartTimeUS())) {
                    this.a().seek(this.a.getRawInfo().bytesCountOfTimeUs(n - this.a.getTimeRange().getStartTimeUS()));
                }
                else if (n - this.a.getTimeRange().getStartTimeUS() <= 0L) {
                    this.a().seek(0L);
                }
                else if (n - this.a.getTimeRange().getStartTimeUS() > this.a.getTimeRange().getStartTimeUS()) {
                    this.a().seek(this.a().length());
                }
            }
            catch (IOException ex) {
                TLog.e((Throwable)ex);
            }
        }
        
        private void b() {
            if (this.c != null) {
                try {
                    this.c.close();
                }
                catch (IOException ex) {
                    ex.printStackTrace();
                }
                finally {
                    this.c = null;
                }
            }
        }
    }
    
    public interface TuSdkOnMixerRenderStateListener
    {
        void onMixerStateChanged(final TuSDKAudioMixerState p0);
    }
    
    public enum TuSDKAudioMixerState
    {
        None, 
        Loading, 
        Loaded, 
        Mixing, 
        DecodeCancel, 
        Cancel, 
        Error;
    }
}
