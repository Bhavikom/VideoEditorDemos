// 
// Decompiled by Procyon v0.5.36
// 

package com.yasoka.screenrecord.sdk.video.lasque.tusdk.core.decoder;

import android.media.MediaCodec;
//import org.lasque.tusdk.core.common.TuSDKMediaDataSource;
//import org.lasque.tusdk.api.audio.postproc.resample.TuSDKAudioResampler;
import android.os.AsyncTask;

import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.TuSdk;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.StringHelper;
import com.yasoka.screenrecord.sdk.video.lasque.tusdk.api.audio.postproc.resample.TuSDKAudioResampler;
import com.yasoka.screenrecord.sdk.video.lasque.tusdk.api.audio.preproc.mixer.TuSDKAudioEntry;

import java.io.File;
//import org.lasque.tusdk.core.utils.StringHelper;
//import org.lasque.tusdk.core.TuSdk;
import java.util.Iterator;
import java.util.HashSet;
//import org.lasque.tusdk.api.audio.preproc.mixer.TuSDKAudioEntry;
import java.util.List;
import java.util.Set;

public class TuSDKAudioDecoderTaskManagaer
{
    private Set<AsyncDecoderTask> a;
    private List<TuSDKAudioEntry> b;
    private TuSDKAudioDecoderTaskMangaerDelegate c;
    
    public TuSDKAudioDecoderTaskManagaer() {
        this.a = new HashSet<AsyncDecoderTask>(3);
    }
    
    public void setAudioEntry(final List<TuSDKAudioEntry> b) {
        this.b = b;
    }
    
    public void setDelegate(final TuSDKAudioDecoderTaskMangaerDelegate c) {
        this.c = c;
    }
    
    public void start() {
        this.a.clear();
        final Iterator<TuSDKAudioEntry> iterator = this.b.iterator();
        while (iterator.hasNext()) {
            final AsyncDecoderTask asyncDecoderTask = new AsyncDecoderTask(iterator.next());
            asyncDecoderTask.execute(new Void[0]);
            this.a.add(asyncDecoderTask);
        }
    }
    
    public void cancel() {
        if (this.a == null || this.a.size() == 0) {
            return;
        }
        final Iterator<AsyncDecoderTask> iterator = this.a.iterator();
        while (iterator.hasNext()) {
            iterator.next().cancel();
        }
        this.a.clear();
    }
    
    private String a(final TuSDKAudioEntry tuSDKAudioEntry, final int i, final int j) {
        return TuSdk.getAppTempPath() + "/" + StringHelper.md5(tuSDKAudioEntry.getFingerprint("sample=" + i + "channel=" + j));
    }
    
    private boolean b(final TuSDKAudioEntry tuSDKAudioEntry, final int sampleRate, final int channel) {
        final String a = this.a(tuSDKAudioEntry, sampleRate, channel);
        if (a == null) {
            return false;
        }
        final File file = new File(a);
        if (!file.exists() || !file.canRead() || !file.isFile()) {
            return false;
        }
        if (file.length() == 0L) {
            file.delete();
            return false;
        }
        tuSDKAudioEntry.getRawInfo().setFilePath(a);
        tuSDKAudioEntry.getRawInfo().sampleRate = sampleRate;
        tuSDKAudioEntry.getRawInfo().channel = channel;
        return true;
    }
    
    private TuSDKAudioEntry a() {
        if (this.b == null || this.b.size() == 0) {
            return null;
        }
        for (final TuSDKAudioEntry tuSDKAudioEntry : this.b) {
            if (tuSDKAudioEntry != null && tuSDKAudioEntry.isTrunk()) {
                return tuSDKAudioEntry;
            }
        }
        return this.b.get(0);
    }
    
    private boolean a(final TuSDKAudioEntry tuSDKAudioEntry, final TuSDKAudioEntry tuSDKAudioEntry2) {
        if (tuSDKAudioEntry == null || tuSDKAudioEntry.getRawInfo() == null || tuSDKAudioEntry2 == null || tuSDKAudioEntry2.getRawInfo() == null) {
            return false;
        }
        final TuSDKAudioInfo rawInfo = tuSDKAudioEntry.getRawInfo();
        final TuSDKAudioInfo rawInfo2 = tuSDKAudioEntry2.getRawInfo();
        return rawInfo != null && rawInfo2 != null && (rawInfo.sampleRate != rawInfo2.sampleRate || rawInfo.channel != rawInfo2.channel);
    }
    
    private class AsyncDecoderTask extends AsyncTask<Void, Double, TuSDKAudioEntry> implements TuSDKAudioDecoder.OnAudioDecoderDelegate
    {
        private TuSDKAudioEntry b;
        private TuSDKAudioDecoder c;
        
        public AsyncDecoderTask(final TuSDKAudioEntry b) {
            this.b = b;
        }
        
        protected TuSDKAudioEntry doInBackground(final Void... array) {
            if (this.b.getRawInfo() != null && this.b.getRawInfo().isValid()) {
                return this.b;
            }
            final TuSDKAudioEntry a = TuSDKAudioDecoderTaskManagaer.this.a();
            final TuSDKAudioInfo tuSDKAudioInfo = (a.getRawInfo() == null) ? this.b.getRawInfo() : a.getRawInfo();
            if (tuSDKAudioInfo == null) {
                return this.b;
            }
            if (TuSDKAudioDecoderTaskManagaer.this.b(this.b, tuSDKAudioInfo.sampleRate, tuSDKAudioInfo.channel)) {
                return this.b;
            }
            final String b = TuSDKAudioDecoderTaskManagaer.this.a(this.b, tuSDKAudioInfo.sampleRate, tuSDKAudioInfo.channel);
            if (TuSDKAudioDecoderTaskManagaer.this.a(this.b, a)) {
                final TuSDKAudioResampler tuSDKAudioResampler = new TuSDKAudioResampler();
                final String string = TuSdk.getAppTempPath() + "/" + System.currentTimeMillis();
                if (tuSDKAudioResampler.process(this.b, new File(string), a.getRawInfo().sampleRate, a.getRawInfo().channel)) {
                    this.b.getRawInfo().sampleRate = tuSDKAudioInfo.sampleRate;
                    this.b.getRawInfo().channel = tuSDKAudioInfo.channel;
                    this.b.setFilePath(string);
                }
            }
            (this.c = new TuSDKAudioDecoder(this.b, b)).setDecodeTimeRange(this.b.getCutTimeRange());
            this.c.setDelegate(this);
            this.c.start();
            return this.b;
        }
        
        protected void onPreExecute() {
            super.onPreExecute();
            if (TuSDKAudioDecoderTaskManagaer.this.c != null) {
                TuSDKAudioDecoderTaskManagaer.this.c.onStateChanged(State.Decoding);
            }
        }
        
        public void cancel() {
            if (this.isCancelled()) {
                return;
            }
            if (this.c != null) {
                this.c.stop();
            }
            this.cancel(true);
        }
        
        protected void onPostExecute(final TuSDKAudioEntry tuSDKAudioEntry) {
            super.onPostExecute(tuSDKAudioEntry);
            TuSDKAudioDecoderTaskManagaer.this.a.remove(this);
            if (TuSDKAudioDecoderTaskManagaer.this.c != null && TuSDKAudioDecoderTaskManagaer.this.a.size() == 0) {
                TuSDKAudioDecoderTaskManagaer.this.c.onStateChanged(State.Complete);
            }
        }
        
        protected void onCancelled(final TuSDKAudioEntry tuSDKAudioEntry) {
            super.onCancelled(tuSDKAudioEntry);
            TuSDKAudioDecoderTaskManagaer.this.a.remove(this);
            if (TuSDKAudioDecoderTaskManagaer.this.c != null && TuSDKAudioDecoderTaskManagaer.this.a.size() == 0) {
                TuSDKAudioDecoderTaskManagaer.this.c.onStateChanged(State.Cancelled);
            }
        }
        
        protected void onCancelled() {
            super.onCancelled();
            TuSDKAudioDecoderTaskManagaer.this.a.remove(this);
            if (TuSDKAudioDecoderTaskManagaer.this.c != null && TuSDKAudioDecoderTaskManagaer.this.a.size() == 0) {
                TuSDKAudioDecoderTaskManagaer.this.c.onStateChanged(State.Cancelled);
            }
        }
        
        public void onDecodeRawInfo(final TuSDKAudioInfo rawInfo) {
            this.b.setRawInfo(rawInfo);
        }
        
        public void onDecode(final byte[] array, final MediaCodec.BufferInfo bufferInfo, final double n) {
        }
        
        public void onDecoderErrorCode(final TuSDKMediaDecoder.TuSDKMediaDecoderError tuSDKMediaDecoderError) {
        }
    }
    
    public interface TuSDKAudioDecoderTaskMangaerDelegate
    {
        void onStateChanged(final State p0);
    }
    
    public enum State
    {
        Idle, 
        Decoding, 
        Complete, 
        Cancelled;
    }
}
