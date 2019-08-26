// 
// Decompiled by Procyon v0.5.36
// 

package com.yasoka.screenrecord.sdk.video.lasque.tusdk.core.encoder.audio;

import android.os.Message;
import android.media.MediaCrypto;
import android.view.Surface;
import java.io.IOException;
import android.os.SystemClock;
import android.os.Looper;
import android.os.Handler;
import java.nio.ByteBuffer;
//import org.lasque.tusdk.core.utils.TLog;
import android.os.HandlerThread;
import android.media.MediaFormat;
import android.media.MediaCodec;
import android.annotation.SuppressLint;

import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.TLog;

@SuppressLint({ "InlinedApi" })
public class TuSDKAudioDataEncoder implements TuSDKAudioDataEncoderInterface
{
    private MediaCodec a;
    private MediaFormat b;
    private HandlerThread c;
    private AudioEncoderHandler d;
    private AudioEncoderThread e;
    private TuSDKAudioBuff[] f;
    private TuSDKAudioBuff g;
    private int h;
    private TuSDKAudioEncoderSetting i;
    private TuSDKAudioDataEncoderDelegate j;
    private long k;
    private long l;
    private AudioEncoderState m;
    
    public TuSDKAudioDataEncoder() {
        this.k = 0L;
        this.m = AudioEncoderState.UnKnow;
        (this.c = new HandlerThread("AudioEncoderHandlerThread")).start();
        this.d = new AudioEncoderHandler(this.c.getLooper());
    }
    
    @Override
    public boolean initEncoder(final TuSDKAudioEncoderSetting i) {
        this.i = i;
        this.b = new MediaFormat();
        this.a = this.a(this.getEncoderSetting(), this.b);
        if (this.a == null) {
            TLog.e("create Audio MediaCodec failed", new Object[0]);
            return false;
        }
        if (i.enableBuffers) {
            final int audioBufferQueueNum = this.getEncoderSetting().audioBufferQueueNum;
            final int n = this.getEncoderSetting().audioQuality.getSampleRate() / 5;
            this.f = new TuSDKAudioBuff[audioBufferQueueNum];
            for (int j = 0; j < audioBufferQueueNum; ++j) {
                this.f[j] = new TuSDKAudioBuff(n);
            }
            this.g = new TuSDKAudioBuff(n);
        }
        return true;
    }
    
    private MediaCodec a(final TuSDKAudioEncoderSetting tuSDKAudioEncoderSetting, final MediaFormat mediaFormat) {
        mediaFormat.setString("mime", "audio/mp4a-latm");
        mediaFormat.setInteger("aac-profile", tuSDKAudioEncoderSetting.mediacodecAACProfile);
        mediaFormat.setInteger("channel-count", tuSDKAudioEncoderSetting.mediacodecAACChannelCount);
        mediaFormat.setInteger("sample-rate", tuSDKAudioEncoderSetting.sampleRate);
        mediaFormat.setInteger("bitrate", tuSDKAudioEncoderSetting.audioQuality.getBitrate());
        mediaFormat.setInteger("max-input-size", tuSDKAudioEncoderSetting.mediacodecAACMaxInputSize);
        MediaCodec encoderByType;
        try {
            encoderByType = MediaCodec.createEncoderByType("audio/mp4a-latm");
        }
        catch (Exception ex) {
            return null;
        }
        return encoderByType;
    }
    
    @Override
    public void start() {
        this.d.sendMessage(this.d.obtainMessage(2));
    }
    
    @Override
    public void stop() {
        this.d.sendMessage(this.d.obtainMessage(3));
    }
    
    protected void onStopeed() {
        if (this.j != null) {
            this.j.onAudioEncoderStoped();
        }
    }
    
    @Override
    public void queueAudio(final byte[] array) {
        this.d.sendMessage(this.d.obtainMessage(1, (Object)array));
    }
    
    public void setEncoderSetting(final TuSDKAudioEncoderSetting i) {
        this.i = i;
    }
    
    public TuSDKAudioEncoderSetting getEncoderSetting() {
        if (this.i == null) {
            this.i = new TuSDKAudioEncoderSetting();
        }
        return this.i;
    }
    
    public MediaFormat getAudioFormat() {
        return this.b;
    }
    
    public TuSDKAudioDataEncoderDelegate getDelegate() {
        return this.j;
    }
    
    @Override
    public void setDelegate(final TuSDKAudioDataEncoderDelegate j) {
        this.j = j;
    }
    
    private long a(final MediaCodec.BufferInfo bufferInfo) {
        if (bufferInfo == null) {
            return 0L;
        }
        if (this.k == 0L) {
            this.k = bufferInfo.presentationTimeUs / 1000L;
        }
        return bufferInfo.presentationTimeUs / 1000L - this.k;
    }
    
    @Override
    public void onAudioEncoderStarted(final MediaFormat mediaFormat) {
        if (this.getDelegate() != null) {
            this.getDelegate().onAudioEncoderStarted(mediaFormat);
        }
    }
    
    @Override
    public void onAudioEncoderFrameDataAvailable(final long n, final ByteBuffer byteBuffer, final MediaCodec.BufferInfo bufferInfo) {
        if (this.l <= 0L) {
            this.l = bufferInfo.presentationTimeUs;
        }
        else {
            if (this.l > bufferInfo.presentationTimeUs) {
                return;
            }
            this.l = bufferInfo.presentationTimeUs;
        }
        if (this.getDelegate() != null) {
            this.getDelegate().onAudioEncoderFrameDataAvailable(n, byteBuffer, bufferInfo);
        }
    }
    
    protected void onAudioEncoderCodecConfig(final long n, final ByteBuffer byteBuffer, final MediaCodec.BufferInfo bufferInfo) {
        if (this.getDelegate() != null) {
            this.getDelegate().onAudioEncoderCodecConfig(n, byteBuffer, bufferInfo);
        }
    }
    
    private class AudioEncoderHandler extends Handler
    {
        AudioEncoderHandler(final Looper looper) {
            super(looper);
        }
        
        private void a(final byte[] src) {
            if (TuSDKAudioDataEncoder.this.m != AudioEncoderState.Runing || src == null || src.length == 0) {
                return;
            }
            final long uptimeMillis = SystemClock.uptimeMillis();
            if (TuSDKAudioDataEncoder.this.i.enableBuffers) {
                final int n = (TuSDKAudioDataEncoder.this.h + 1) % TuSDKAudioDataEncoder.this.f.length;
                if (TuSDKAudioDataEncoder.this.f[n].isReadyToFill) {
                    System.arraycopy(src, 0, TuSDKAudioDataEncoder.this.f[n].buff, 0, TuSDKAudioDataEncoder.this.getEncoderSetting().bufferSize);
                    TuSDKAudioDataEncoder.this.f[n].isReadyToFill = false;
                    TuSDKAudioDataEncoder.this.h = n;
                    TuSDKAudioDataEncoder.this.f[n].isReadyToFill = true;
                }
                System.arraycopy(TuSDKAudioDataEncoder.this.f[n].buff, 0, TuSDKAudioDataEncoder.this.g.buff, 0, TuSDKAudioDataEncoder.this.g.buff.length);
                final int dequeueInputBuffer = TuSDKAudioDataEncoder.this.a.dequeueInputBuffer(5000L);
                if (dequeueInputBuffer >= 0) {
                    final ByteBuffer byteBuffer = TuSDKAudioDataEncoder.this.a.getInputBuffers()[dequeueInputBuffer];
                    byteBuffer.position(0);
                    byteBuffer.put(TuSDKAudioDataEncoder.this.g.buff, 0, TuSDKAudioDataEncoder.this.g.buff.length);
                    TuSDKAudioDataEncoder.this.a.queueInputBuffer(dequeueInputBuffer, 0, TuSDKAudioDataEncoder.this.g.buff.length, uptimeMillis * 1000L, 0);
                }
            }
            else {
                final int dequeueInputBuffer2 = TuSDKAudioDataEncoder.this.a.dequeueInputBuffer(5000L);
                if (dequeueInputBuffer2 >= 0) {
                    final ByteBuffer byteBuffer2 = TuSDKAudioDataEncoder.this.a.getInputBuffers()[dequeueInputBuffer2];
                    byteBuffer2.position(0);
                    byteBuffer2.put(src, 0, src.length);
                    TuSDKAudioDataEncoder.this.a.queueInputBuffer(dequeueInputBuffer2, 0, src.length, uptimeMillis * 1000L, 0);
                }
            }
        }
        
        private void a() {
            if (TuSDKAudioDataEncoder.this.m == AudioEncoderState.Runing) {
                return;
            }
            TuSDKAudioDataEncoder.this.d.removeMessages(2);
            TuSDKAudioDataEncoder.this.d.removeMessages(3);
            TuSDKAudioDataEncoder.this.d.removeMessages(1);
            TuSDKAudioDataEncoder.this.d.removeCallbacksAndMessages((Object)null);
            if (TuSDKAudioDataEncoder.this.f != null) {
                final TuSDKAudioBuff[] e = TuSDKAudioDataEncoder.this.f;
                for (int length = e.length, i = 0; i < length; ++i) {
                    e[i].isReadyToFill = true;
                }
            }
            if (TuSDKAudioDataEncoder.this.a == null) {
                try {
                    TuSDKAudioDataEncoder.this.a = MediaCodec.createEncoderByType(TuSDKAudioDataEncoder.this.b.getString("mime"));
                }
                catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
            TuSDKAudioDataEncoder.this.k = 0L;
            TuSDKAudioDataEncoder.this.a.configure(TuSDKAudioDataEncoder.this.b, (Surface)null, (MediaCrypto)null, 1);
            TuSDKAudioDataEncoder.this.a.start();
            TuSDKAudioDataEncoder.this.e = new AudioEncoderThread();
            TuSDKAudioDataEncoder.this.e.start();
            TuSDKAudioDataEncoder.this.m = AudioEncoderState.Runing;
        }
        
        private void b() {
            if (TuSDKAudioDataEncoder.this.m != AudioEncoderState.Runing) {
                return;
            }
            TuSDKAudioDataEncoder.this.d.removeMessages(2);
            TuSDKAudioDataEncoder.this.d.removeMessages(3);
            TuSDKAudioDataEncoder.this.d.removeMessages(1);
            TuSDKAudioDataEncoder.this.d.removeCallbacksAndMessages((Object)null);
            if (TuSDKAudioDataEncoder.this.e != null) {
                TuSDKAudioDataEncoder.this.e.quit();
                try {
                    TuSDKAudioDataEncoder.this.e.join();
                    TuSDKAudioDataEncoder.this.e = null;
                }
                catch (InterruptedException ex) {
                    ex.printStackTrace();
                }
            }
            if (TuSDKAudioDataEncoder.this.a != null) {
                TuSDKAudioDataEncoder.this.a.stop();
                TuSDKAudioDataEncoder.this.a.release();
                TuSDKAudioDataEncoder.this.a = null;
            }
            TuSDKAudioDataEncoder.this.l = 0L;
            TuSDKAudioDataEncoder.this.m = AudioEncoderState.Stopped;
            TuSDKAudioDataEncoder.this.onStopeed();
        }
        
        public void handleMessage(final Message message) {
            if (message.what == 1) {
                this.a((byte[])message.obj);
            }
            else if (message.what == 2) {
                this.a();
            }
            else if (message.what == 3) {
                this.b();
            }
        }
    }
    
    private class AudioEncoderThread extends Thread
    {
        private boolean b;
        
        private AudioEncoderThread() {
            this.b = false;
        }
        
        public void quit() {
            this.b = true;
            this.interrupt();
        }
        
        @Override
        public void run() {
            while (!this.b) {
                final MediaCodec.BufferInfo bufferInfo = new MediaCodec.BufferInfo();
                final int dequeueOutputBuffer = TuSDKAudioDataEncoder.this.a.dequeueOutputBuffer(bufferInfo, 5000L);
                switch (dequeueOutputBuffer) {
                    case -3: {
                        continue;
                    }
                    case -1: {
                        continue;
                    }
                    case -2: {
                        TuSDKAudioDataEncoder.this.onAudioEncoderStarted(TuSDKAudioDataEncoder.this.a.getOutputFormat());
                        continue;
                    }
                    default: {
                        if (bufferInfo.size > 0) {
                            final ByteBuffer byteBuffer = TuSDKAudioDataEncoder.this.a.getOutputBuffers()[dequeueOutputBuffer];
                            byteBuffer.position(bufferInfo.offset);
                            byteBuffer.limit(bufferInfo.offset + bufferInfo.size);
                            if ((bufferInfo.flags & 0x2) != 0x0) {
                                TuSDKAudioDataEncoder.this.onAudioEncoderCodecConfig(TuSDKAudioDataEncoder.this.a(bufferInfo), byteBuffer.duplicate(), bufferInfo);
                            }
                            else {
                                TuSDKAudioDataEncoder.this.onAudioEncoderFrameDataAvailable(TuSDKAudioDataEncoder.this.a(bufferInfo), byteBuffer.duplicate(), bufferInfo);
                            }
                        }
                        TuSDKAudioDataEncoder.this.a.releaseOutputBuffer(dequeueOutputBuffer, false);
                        continue;
                    }
                }
            }
        }
    }
    
    private enum AudioEncoderState
    {
        UnKnow, 
        Runing, 
        Stopped;
    }
}
