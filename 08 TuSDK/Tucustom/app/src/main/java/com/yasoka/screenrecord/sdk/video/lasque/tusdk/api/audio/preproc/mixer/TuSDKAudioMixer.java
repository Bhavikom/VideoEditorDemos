// 
// Decompiled by Procyon v0.5.36
// 

package com.yasoka.screenrecord.sdk.video.lasque.tusdk.api.audio.preproc.mixer;

//import org.lasque.tusdk.core.decoder.TuSDKAudioInfo;
import java.io.RandomAccessFile;
import android.os.AsyncTask;

import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.secret.StatisticsManger;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.TLog;
import com.yasoka.screenrecord.sdk.video.lasque.tusdk.core.decoder.TuSDKAudioDecoderTaskManagaer;
import com.yasoka.screenrecord.sdk.video.lasque.tusdk.core.decoder.TuSDKAudioInfo;

import java.io.IOException;
import java.util.Arrays;
//import org.lasque.tusdk.core.utils.TLog;
import java.util.ArrayList;
import java.util.Iterator;
//import org.lasque.tusdk.core.secret.StatisticsManger;
//import org.lasque.tusdk.core.decoder.TuSDKAudioDecoderTaskManagaer;
import java.util.List;

public abstract class TuSDKAudioMixer implements TuSDKAudioMixerInterface
{
    public static final int ERROR_CODE_UNKNOW = -1;
    private List<TuSDKAudioEntry> a;
    private RawAudioTrack b;
    private OnAudioMixerDelegate c;
    private volatile State d;
    private TuSDKAudioDecoderTaskManagaer e;
    private AsyncMixTask f;
    
    public TuSDKAudioMixer() {
        this.d = State.Idle;
        this.e = new TuSDKAudioDecoderTaskManagaer();
        this.f = null;
    }
    
    public TuSDKAudioMixer setOnAudioMixDelegate(final OnAudioMixerDelegate c) {
        this.c = c;
        return this;
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
    
    private void a(final State d) {
        if (this.d == d) {
            return;
        }
        this.d = d;
        if (this.c != null) {
            this.c.onStateChanged(d);
        }
        if (d == State.Complete) {
            StatisticsManger.appendComponent(9449473L);
        }
    }
    
    public State getState() {
        return this.d;
    }
    
    public void clearDecodeCahceInfo() {
        final Iterator<TuSDKAudioEntry> iterator = this.a.iterator();
        while (iterator.hasNext()) {
            iterator.next().clearDecodeCahceInfo();
        }
    }
    
    protected void onMixComplete() {
    }
    
    protected void onMixed(final byte[] array) {
        if (this.c != null) {
            this.c.onMixed(array);
        }
    }
    
    protected void onMixingError(final int n) {
        if (this.c != null) {
            this.c.onMixingError(n);
        }
    }
    
    protected byte[] processPCMData(final RawAudioTrack rawAudioTrack, final byte[] array) {
        if (rawAudioTrack.c.getVolume() != 1.0f) {
            return this.a(array, rawAudioTrack.c.getVolume());
        }
        return array;
    }
    
    @Override
    public void cancel() {
        if (this.d != State.Decoding && this.d != State.Mixing) {
            return;
        }
        this.a();
    }
    
    private void a() {
        if (this.d != State.Decoding && this.d != State.Mixing) {
            return;
        }
        this.b = null;
        this.d = State.Cancelled;
        if (this.e != null) {
            this.e.cancel();
        }
        if (this.f != null) {
            this.f.cancel();
        }
    }
    
    private void b() {
        (this.f = new AsyncMixTask()).execute(new Void[0]);
    }
    
    @Override
    public void mixAudios(final List<TuSDKAudioEntry> list) {
        if (list == null || list.size() == 0) {
            return;
        }
        if (this.d == State.Mixing || this.d == State.Decoding) {
            return;
        }
        this.a();
        this.a = new ArrayList<TuSDKAudioEntry>(list);
        this.a(State.Decoding);
        this.e.setAudioEntry(list);
        this.e.setDelegate(new TuSDKAudioDecoderTaskManagaer.TuSDKAudioDecoderTaskMangaerDelegate() {
            @Override
            public void onStateChanged(final TuSDKAudioDecoderTaskManagaer.State state) {
                if (state == TuSDKAudioDecoderTaskManagaer.State.Complete) {
                    TuSDKAudioMixer.this.a(State.Decoded);
                    TuSDKAudioMixer.this.b();
                }
                else if (state == TuSDKAudioDecoderTaskManagaer.State.Cancelled) {
                    TuSDKAudioMixer.this.a(State.Cancelled);
                }
            }
        });
        this.e.start();
    }
    
    private List<RawAudioTrack> a(final List<TuSDKAudioEntry> list) {
        final ArrayList<RawAudioTrack> list2 = new ArrayList<RawAudioTrack>();
        for (final TuSDKAudioEntry tuSDKAudioEntry : list) {
            if (tuSDKAudioEntry.isValid() && tuSDKAudioEntry.getRawInfo() != null) {
                if (!tuSDKAudioEntry.getRawInfo().isValid()) {
                    continue;
                }
                final RawAudioTrack b = new RawAudioTrack(tuSDKAudioEntry);
                if (this.b == null && tuSDKAudioEntry.isTrunk()) {
                    (this.b = b).e = true;
                }
                list2.add(b);
            }
        }
        return list2;
    }
    
    private int c() {
        if (this.b == null || !this.b.c.validateTimeRange()) {
            return 0;
        }
        return this.b.c.bytesSizeOfTimeRangeStartPosition();
    }
    
    private int d() {
        if (this.b != null && this.b.c.validateTimeRange()) {
            return this.b.c.getRawInfo().bytesCountOfTime(Math.round(this.b.c.getTimeRange().getEndTime()));
        }
        return Integer.MAX_VALUE;
    }
    
    private void b(final List<RawAudioTrack> list) {
        if (this.c != null) {
            if (this.b != null) {
                this.c.onReayTrunkTrackInfo(this.b.c.getRawInfo());
            }
            else {
                if (list.size() == 0) {
                    TLog.e("Trunk audio track is null !!! ", new Object[0]);
                    return;
                }
                this.c.onReayTrunkTrackInfo(list.get(0).c.getRawInfo());
            }
        }
        this.a(State.Mixing);
        final int size = list.size();
        final byte[][] array = new byte[size][];
        byte[] processPCMData = new byte[512];
        final int d = this.d();
        int c = this.c();
        try {
            while (this.d == State.Mixing) {
                for (int i = 0; i < size; ++i) {
                    final RawAudioTrack rawAudioTrack = list.get(i);
                    int n;
                    if (rawAudioTrack.d) {
                        n = -1;
                    }
                    else if (rawAudioTrack.a()) {
                        n = rawAudioTrack.b().read(processPCMData);
                        if (n == -1 || c >= d) {
                            rawAudioTrack.d = true;
                            break;
                        }
                    }
                    else if (this.b != null && rawAudioTrack.c.validateTimeRange() && (c < rawAudioTrack.c.bytesSizeOfTimeRangeStartPosition() || c >= rawAudioTrack.c.bytesSizeOfTimeRangeEndPosition())) {
                        n = -1;
                    }
                    else {
                        n = rawAudioTrack.b().read(processPCMData);
                        if (n == -1) {
                            if (this.b != null && (rawAudioTrack.c.validateTimeRange() || rawAudioTrack.c.isLooping())) {
                                rawAudioTrack.b().seek(0L);
                                n = rawAudioTrack.b().read(processPCMData);
                            }
                            else {
                                rawAudioTrack.d = true;
                            }
                        }
                    }
                    if (n != -1) {
                        processPCMData = this.processPCMData(rawAudioTrack, processPCMData);
                    }
                    array[i] = ((n == -1) ? new byte[512] : Arrays.copyOf(processPCMData, processPCMData.length));
                    if (rawAudioTrack.a()) {
                        c += array[i].length;
                    }
                }
                boolean b = true;
                for (final RawAudioTrack rawAudioTrack2 : list) {
                    if (rawAudioTrack2.a() && rawAudioTrack2.d) {
                        b = true;
                        break;
                    }
                    if (rawAudioTrack2.d) {
                        continue;
                    }
                    b = false;
                }
                if (b) {
                    break;
                }
                this.onMixed(this.mixRawAudioBytes(array));
            }
        }
        catch (IOException ex) {
            ex.printStackTrace();
            this.onMixingError(-1);
        }
        finally {
            for (final RawAudioTrack rawAudioTrack3 : list) {
                if (rawAudioTrack3 != null) {
                    rawAudioTrack3.c();
                }
            }
        }
    }
    
    private class AsyncMixTask extends AsyncTask<Void, Double, Void>
    {
        protected Void doInBackground(final Void... array) {
            TuSDKAudioMixer.this.b(TuSDKAudioMixer.this.a(TuSDKAudioMixer.this.a));
            return null;
        }
        
        protected void onPostExecute(final Void void1) {
            super.onPostExecute(void1);
            TuSDKAudioMixer.this.b = null;
            TuSDKAudioMixer.this.a(State.Complete);
            TuSDKAudioMixer.this.onMixComplete();
        }
        
        public boolean isDone() {
            return this.getStatus() == Status.FINISHED;
        }
        
        public void cancel() {
            if (this.isCancelled() || this.isDone()) {
                return;
            }
            TuSDKAudioMixer.this.d = State.Cancelled;
            this.cancel(true);
        }
        
        protected void onCancelled(final Void void1) {
            super.onCancelled(void1);
            TuSDKAudioMixer.this.a(State.Cancelled);
        }
    }
    
    protected class RawAudioTrack
    {
        private RandomAccessFile b;
        private TuSDKAudioEntry c;
        private boolean d;
        private boolean e;
        
        private RawAudioTrack(final TuSDKAudioEntry c) {
            this.e = false;
            this.c = c;
        }
        
        private boolean a() {
            return this.e;
        }
        
        private RandomAccessFile b() {
            if (this.b == null) {
                try {
                    this.b = new RandomAccessFile(this.c.getRawInfo().getFilePath(), "r");
                    if (this.a() && this.c.validateTimeRange()) {
                        this.b.seek(this.c.bytesSizeOfTimeRangeStartPosition());
                    }
                }
                catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
            return this.b;
        }
        
        private void c() {
            if (this.b != null) {
                try {
                    this.b.close();
                }
                catch (IOException ex) {
                    ex.printStackTrace();
                }
                finally {
                    this.b = null;
                }
            }
        }
    }
    
    public enum State
    {
        Idle, 
        Decoding, 
        Decoded, 
        Mixing, 
        Complete, 
        Cancelled;
    }
    
    public interface OnAudioMixerDelegate
    {
        void onReayTrunkTrackInfo(final TuSDKAudioInfo p0);
        
        void onMixed(final byte[] p0);
        
        void onMixingError(final int p0);
        
        void onStateChanged(final State p0);
    }
}
