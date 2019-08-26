// 
// Decompiled by Procyon v0.5.36
// 

package com.yasoka.screenrecord.sdk.video.lasque.tusdk.api.audio.preproc.mixer;

import android.os.AsyncTask;
//import org.lasque.tusdk.video.editor.TuSDKMovieWriter;
import java.nio.ByteBuffer;
import android.media.MediaCrypto;
import android.view.Surface;
import android.media.MediaFormat;
//import org.lasque.tusdk.core.utils.StringHelper;
//import org.lasque.tusdk.core.TuSdk;
import java.io.FileNotFoundException;
//import org.lasque.tusdk.core.utils.TLog;
import java.util.List;
import java.io.IOException;
//import org.lasque.tusdk.core.decoder.TuSDKAudioInfo;
import java.io.RandomAccessFile;
import java.io.FileOutputStream;
import android.media.MediaCodec;
import android.annotation.TargetApi;

import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.TuSdk;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.StringHelper;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.TLog;
import com.yasoka.screenrecord.sdk.video.lasque.tusdk.core.decoder.TuSDKAudioInfo;
import com.yasoka.screenrecord.sdk.video.lasque.tusdk.video.editor.TuSDKMovieWriter;

@TargetApi(16)
public class TuSDKAudioPacketMixer extends TuSDKAverageAudioMixer
{
    protected MediaCodec mAudioEncoder;
    private TuSDKAudioMixPacketDelegate a;
    private FileOutputStream b;
    private String c;
    private RandomAccessFile d;
    private TuSDKAudioInfo e;
    private long f;
    private byte[] g;
    private int h;
    private long i;
    private long j;
    private TuSDKAudioEntry k;
    private AsyncVideoMixTask l;
    private boolean m;
    private OnAudioMixerDelegate n;
    
    public TuSDKAudioPacketMixer() {
        this.g = new byte[4096];
        this.h = 0;
        this.i = 0L;
        this.j = 0L;
        this.m = false;
        this.n = new OnAudioMixerDelegate() {
            @Override
            public void onMixed(final byte[] b) {
                try {
                    if (TuSDKAudioPacketMixer.this.b != null) {
                        TuSDKAudioPacketMixer.this.b.write(b);
                    }
                }
                catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
            
            @Override
            public void onMixingError(final int n) {
                try {
                    if (TuSDKAudioPacketMixer.this.b != null) {
                        TuSDKAudioPacketMixer.this.b.close();
                    }
                }
                catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
            
            @Override
            public void onReayTrunkTrackInfo(final TuSDKAudioInfo tuSDKAudioInfo) {
                TuSDKAudioPacketMixer.this.e = tuSDKAudioInfo;
            }
            
            @Override
            public void onStateChanged(final State state) {
                if (state != State.Decoding) {
                    if (state != State.Decoded) {
                        if (state != State.Mixing) {
                            if (state != State.Cancelled) {
                                if (state == State.Complete) {
                                    try {
                                        if (TuSDKAudioPacketMixer.this.b != null) {
                                            TuSDKAudioPacketMixer.this.b.close();
                                        }
                                        TuSDKAudioPacketMixer.this.m();
                                    }
                                    catch (IOException ex) {
                                        ex.printStackTrace();
                                    }
                                }
                            }
                        }
                    }
                }
            }
        };
    }
    
    public void setFirstAudioSampleTimeUs(final long n) {
        this.i = n;
        this.j = n;
    }
    
    public void prepare(final List<TuSDKAudioEntry> list) {
        if (list == null || list.size() == 0) {
            return;
        }
        for (final TuSDKAudioEntry k : list) {
            if (k.isTrunk()) {
                this.k = k;
            }
        }
        if (this.k == null && list.size() == 1) {
            this.k = list.get(0);
        }
        this.m = (list.size() > 1);
        if (this.a != null) {
            this.a.onOutputAudioFormat(this.e());
        }
    }
    
    public void setAudioDataDelegate(final TuSDKAudioMixPacketDelegate a) {
        this.a = a;
    }
    
    @Override
    public void mixAudios(final List<TuSDKAudioEntry> list) {
        if (list == null || list.size() == 0) {
            return;
        }
        this.setOnAudioMixDelegate(this.n);
        for (final TuSDKAudioEntry k : list) {
            if (k.isTrunk()) {
                this.k = k;
            }
        }
        if (this.k == null && list.size() == 1) {
            this.k = list.get(0);
        }
        try {
            this.b = new FileOutputStream(this.a());
            this.d = new RandomAccessFile(this.a(), "rw");
        }
        catch (FileNotFoundException ex) {
            TLog.e("%s : Please set a valid file path", new Object[] { this });
            ex.printStackTrace();
            return;
        }
        super.mixAudios(list);
    }
    
    private String a() {
        if (this.c == null) {
            this.c = TuSdk.getAppTempPath() + "/" + String.format("lsq_%s", StringHelper.timeStampString());
        }
        return this.c;
    }
    
    private TuSDKAudioInfo b() {
        if (this.e == null && this.k != null) {
            this.e = this.k.getRawInfo();
        }
        if (this.e == null) {
            this.e = TuSDKAudioInfo.defaultAudioInfo();
        }
        return this.e;
    }
    
    private MediaCodec c() {
        if (this.mAudioEncoder == null) {
            this.mAudioEncoder = this.d();
        }
        return this.mAudioEncoder;
    }
    
    private MediaCodec d() {
         MediaCodec encoderByType = null;
        try {
            encoderByType = MediaCodec.createEncoderByType("audio/mp4a-latm");
            final MediaFormat mediaFormat = new MediaFormat();
            mediaFormat.setString("mime", "audio/mp4a-latm");
            mediaFormat.setInteger("bitrate", this.b().bitrate);
            mediaFormat.setInteger("channel-count", this.b().channel);
            mediaFormat.setInteger("sample-rate", this.b().sampleRate);
            mediaFormat.setInteger("aac-profile", 2);
            encoderByType.configure(mediaFormat, (Surface)null, (MediaCrypto)null, 1);
        } catch (IOException e1) {
            e1.printStackTrace();
        }

        return encoderByType;
    }
    
    private MediaFormat e() {
        this.c().start();
        MediaFormat outputFormat = null;
        MediaCodec.BufferInfo bufferInfo;
        int i;
        for (bufferInfo = new MediaCodec.BufferInfo(), i = 0; i != -2; i = this.c().dequeueOutputBuffer(bufferInfo, 0L)) {}
        if (i == -2) {
            outputFormat = this.c().getOutputFormat();
        }
        if (i >= 0) {
            this.c().releaseOutputBuffer(i, false);
        }
        this.mAudioEncoder = null;
        return outputFormat;
    }
    
    private long f() {
        if (this.f > 0L) {
            return this.f;
        }
        return this.e.durationTimeUs;
    }
    
    public void setMaxDurationTimeUs(final long f) {
        this.f = f;
    }
    
    private boolean g() {
        if (this.k == null) {
            return false;
        }
        if (this.k.validateTimeRange() && !this.m) {
            return this.j < (float)Math.min(this.f() - this.k.getTimeRange().getStartTimeUS(), this.k.getTimeRange().durationTimeUS());
        }
        return this.k.isLooping() && this.j < this.f();
    }
    
    private long h() {
        return this.j - this.i;
    }
    
    private void i() {
        this.c().start();
        boolean j = false;
        for (boolean k = false; !k; k = this.k()) {
            if (!j) {
                j = this.j();
            }
        }
    }
    
    private boolean j() {
        final ByteBuffer[] inputBuffers = this.mAudioEncoder.getInputBuffers();
        final int dequeueInputBuffer = this.mAudioEncoder.dequeueInputBuffer(500L);
        if (dequeueInputBuffer >= 0) {
            final ByteBuffer byteBuffer = inputBuffers[dequeueInputBuffer];
            byteBuffer.clear();
            try {
                if (this.d.read(this.g) == -1) {
                    if (!this.g()) {
                        this.mAudioEncoder.queueInputBuffer(dequeueInputBuffer, 0, 0, 0L, 4);
                        return true;
                    }
                    this.d.seek(0L);
                    this.d.read(this.g);
                }
                byteBuffer.put(this.g);
                this.h += this.g.length;
                this.mAudioEncoder.queueInputBuffer(dequeueInputBuffer, 0, this.g.length, this.b().frameTimeUsWithAudioSize(this.h), 0);
            }
            catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        return false;
    }
    
    private boolean k() {
        final ByteBuffer[] outputBuffers = this.mAudioEncoder.getOutputBuffers();
        final MediaCodec.BufferInfo bufferInfo = new MediaCodec.BufferInfo();
        final int dequeueOutputBuffer = this.mAudioEncoder.dequeueOutputBuffer(bufferInfo, 500L);
        if (dequeueOutputBuffer >= 0) {
            if ((bufferInfo.flags & 0x2) != 0x0) {
                this.mAudioEncoder.releaseOutputBuffer(dequeueOutputBuffer, false);
                return false;
            }
            if (bufferInfo.size != 0) {
                final ByteBuffer byteBuffer = outputBuffers[dequeueOutputBuffer];
                byteBuffer.position(bufferInfo.offset);
                byteBuffer.limit(bufferInfo.offset + bufferInfo.size);
                this.j += this.b().getFrameInterval();
                bufferInfo.presentationTimeUs = this.j;
                if (this.i == 0L) {
                    this.i = bufferInfo.presentationTimeUs;
                }
                this.a.onAudioPacketAvailable(bufferInfo.presentationTimeUs, byteBuffer, bufferInfo);
            }
            this.mAudioEncoder.releaseOutputBuffer(dequeueOutputBuffer, false);
            if ((bufferInfo.flags & 0x4) != 0x0) {
                return true;
            }
            if (this.h() >= this.f()) {
                return true;
            }
            if (this.k != null && ((this.k.validateTimeRange() && !this.m) || this.k.isLooping()) && !this.g()) {
                return true;
            }
        }
        else if (dequeueOutputBuffer == -3) {
            this.mAudioEncoder.getOutputBuffers();
        }
        else if (dequeueOutputBuffer == -2) {
            this.l();
        }
        return false;
    }
    
    private void l() {
        if (this.k == null || this.k.getTimeRange() == null || this.m) {
            return;
        }
        while (this.h() < this.k.getTimeRange().getStartTimeUS()) {
            final ByteBuffer wrap = ByteBuffer.wrap(TuSDKMovieWriter.AAC_MUTE_BYTES);
            final MediaCodec.BufferInfo bufferInfo = new MediaCodec.BufferInfo();
            bufferInfo.presentationTimeUs = this.j;
            bufferInfo.size = TuSDKMovieWriter.AAC_MUTE_BYTES.length;
            bufferInfo.offset = 0;
            this.a.onAudioPacketAvailable(this.j, wrap, bufferInfo);
            this.j += this.b().getFrameInterval();
            if (this.i == 0L) {
                this.i = this.j;
            }
        }
    }
    
    public boolean isPackaging() {
        return this.getState() != State.Cancelled && (this.getState() != State.Complete || (this.l != null && this.l.getStatus() == AsyncTask.Status.RUNNING));
    }
    
    private void m() {
        (this.l = new AsyncVideoMixTask()).execute(new Void[0]);
    }
    
    @Override
    public void cancel() {
        super.cancel();
        if (this.isPackaging()) {
            this.l.cancel();
        }
    }
    
    private class AsyncVideoMixTask extends AsyncTask<Void, Double, Void>
    {
        protected Void doInBackground(final Void... array) {
            TuSDKAudioPacketMixer.this.i();
            return null;
        }
        
        protected void onPreExecute() {
            super.onPreExecute();
        }
        
        protected void onPostExecute(final Void void1) {
            super.onPostExecute(void1);
            if (TuSDKAudioPacketMixer.this.mAudioEncoder != null) {
                TuSDKAudioPacketMixer.this.mAudioEncoder.stop();
                TuSDKAudioPacketMixer.this.mAudioEncoder.release();
                TuSDKAudioPacketMixer.this.mAudioEncoder = null;
            }
            if (TuSDKAudioPacketMixer.this.a != null) {
                TuSDKAudioPacketMixer.this.a.onCompleted();
            }
        }
        
        public void cancel() {
            this.cancel(true);
        }
        
        protected void onCancelled(final Void void1) {
            super.onCancelled(void1);
            if (TuSDKAudioPacketMixer.this.mAudioEncoder != null) {
                TuSDKAudioPacketMixer.this.mAudioEncoder.stop();
                TuSDKAudioPacketMixer.this.mAudioEncoder.release();
                TuSDKAudioPacketMixer.this.mAudioEncoder = null;
            }
        }
    }
    
    public interface TuSDKAudioMixPacketDelegate
    {
        void onOutputAudioFormat(final MediaFormat p0);
        
        void onAudioPacketAvailable(final long p0, final ByteBuffer p1, final MediaCodec.BufferInfo p2);
        
        void onCompleted();
    }
}
