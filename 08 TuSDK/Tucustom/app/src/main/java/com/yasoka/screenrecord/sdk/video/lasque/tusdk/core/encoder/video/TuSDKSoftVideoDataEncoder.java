// 
// Decompiled by Procyon v0.5.36
// 

package com.yasoka.screenrecord.sdk.video.lasque.tusdk.core.encoder.video;

import android.os.SystemClock;
//import org.lasque.tusdk.core.secret.ColorSpaceConvert;
import android.os.Message;
import android.os.Looper;
import android.os.Handler;
import java.io.IOException;
import android.os.Bundle;
import java.nio.ByteBuffer;
import android.media.MediaCrypto;
import android.view.Surface;
//import org.lasque.tusdk.core.utils.TLog;
//import org.lasque.tusdk.core.common.TuSDKMediaUtils;
import android.os.Build;
//import org.lasque.tusdk.core.encoder.TuSDKVideoDataEncoderDelegate;
import android.os.HandlerThread;
import android.media.MediaFormat;
import android.media.MediaCodec;
import android.annotation.SuppressLint;

import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.secret.ColorSpaceConvert;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.TLog;
import com.yasoka.screenrecord.sdk.video.lasque.tusdk.core.common.TuSDKMediaUtils;
import com.yasoka.screenrecord.sdk.video.lasque.tusdk.core.encoder.TuSDKVideoDataEncoderDelegate;

@SuppressLint({ "InlinedApi" })
public class TuSDKSoftVideoDataEncoder implements TuSDKSoftVideoDataEncoderInterface
{
    private final Object b;
    private MediaCodec c;
    private MediaFormat d;
    private boolean e;
    private boolean f;
    private long g;
    private TuSDKVideoBuff[] h;
    private TuSDKVideoBuff i;
    private int j;
    private TuSDKVideoBuff k;
    private long l;
    private HandlerThread m;
    private VideoDataEncoderHandler n;
    private VideoDequeueOutputBufferThread o;
    private TuSDKVideoEncoderSetting p;
    private TuSDKVideoDataEncoderDelegate q;
    int a;
    
    public TuSDKSoftVideoDataEncoder() {
        this.b = new Object();
        this.e = true;
        this.g = 0L;
        this.a = 0;
    }
    
    @Override
    public boolean initEncoder(final TuSDKVideoEncoderSetting p) {
        synchronized (this.b) {
            this.p = p;
            if (Build.VERSION.SDK_INT >= 21 && !TuSDKMediaUtils.isVideoSizeSupported(this.p.videoSize, "video/avc")) {
                TLog.w("TuSDKSoftVideoDataEncoder | May not support video size for " + this.p.videoSize, new Object[0]);
            }
            this.d = new MediaFormat();
            this.c = this.a(this.getVideoEncoderSetting(), this.d);
            this.f = false;
            if (this.c == null) {
                return false;
            }
            final int calculator = TuSDKBuffSizeCalculator.calculator(this.getVideoEncoderSetting().videoSize.width, this.getVideoEncoderSetting().videoSize.height, this.getVideoEncoderSetting().previewColorFormat);
            final int width = this.getVideoEncoderSetting().videoSize.width;
            final int height = this.getVideoEncoderSetting().videoSize.height;
            final int videoBufferQueueNum = this.getVideoEncoderSetting().videoBufferQueueNum;
            this.h = new TuSDKVideoBuff[videoBufferQueueNum];
            for (int i = 0; i < videoBufferQueueNum; ++i) {
                this.h[i] = new TuSDKVideoBuff(this.getVideoEncoderSetting().previewColorFormat, calculator);
            }
            this.j = 0;
            this.i = new TuSDKVideoBuff(21, TuSDKBuffSizeCalculator.calculator(width, height, 21));
            this.k = new TuSDKVideoBuff(this.getVideoEncoderSetting().mediacodecAVCColorFormat, TuSDKBuffSizeCalculator.calculator(width, height, this.getVideoEncoderSetting().mediacodecAVCColorFormat));
            return true;
        }
    }
    
    @Override
    public boolean start() {
        synchronized (this.b) {
            try {
                if (this.c == null) {
                    this.c = MediaCodec.createEncoderByType(this.d.getString("mime"));
                }
                this.c.configure(this.d, (Surface)null, (MediaCrypto)null, 1);
                this.c.start();
                this.f = true;
                this.g = 0L;
                (this.o = new VideoDequeueOutputBufferThread("AudioDequeueOutputBufferThread")).start();
                (this.m = new HandlerThread("videoFilterHandlerThread")).start();
                this.n = new VideoDataEncoderHandler(this.m.getLooper());
            }
            catch (Exception ex) {
                return false;
            }
            return true;
        }
    }
    
    @Override
    public void stop() {
        synchronized (this.b) {
            if (!this.f) {
                return;
            }
            this.f = false;
            if (this.c != null) {
                this.c.stop();
                this.c.release();
                this.c = null;
            }
            this.o.a();
            try {
                this.o.join();
            }
            catch (InterruptedException ex) {
                TLog.e((Throwable)ex);
            }
            this.o = null;
            this.n.removeCallbacks((Runnable)null);
            this.n.removeMessages(1);
            this.m.quit();
            try {
                this.m.join();
            }
            catch (InterruptedException ex2) {
                TLog.e((Throwable)ex2);
            }
            this.l = 0L;
            this.a = 0;
        }
    }
    
    @Override
    public void queueVideo(final byte[] buff) {
        synchronized (this.b) {
            if (!this.f) {
                return;
            }
            final int n = (this.j + 1) % this.h.length;
            if (this.h[n].isReadyToFill) {
                this.h[n].buff = buff;
                this.h[n].isReadyToFill = false;
                this.j = n;
                this.n.sendMessage(this.n.obtainMessage(1, n, 0));
            }
            else {
                TLog.d("queueVideo,abandon,targetIndex" + n, new Object[0]);
            }
        }
    }
    
    @Override
    public void onVideoEncoderStarted(final MediaFormat mediaFormat) {
        if (this.getDelegate() != null) {
            this.getDelegate().onVideoEncoderStarted(mediaFormat);
        }
    }
    
    public void setPTSUseSystemClock(final boolean e) {
        this.e = e;
    }
    
    public MediaFormat getVideoFormat() {
        return this.d;
    }
    
    @Override
    public void onVideoEncoderFrameDataAvailable(final long n, final ByteBuffer byteBuffer, final MediaCodec.BufferInfo bufferInfo) {
        if (this.l == 0L) {
            this.l = bufferInfo.presentationTimeUs;
        }
        else {
            if (this.l > bufferInfo.presentationTimeUs) {
                bufferInfo.presentationTimeUs = this.l + 1L;
            }
            this.l = bufferInfo.presentationTimeUs;
        }
        if (this.getDelegate() != null) {
            this.getDelegate().onVideoEncoderFrameDataAvailable(n, byteBuffer, bufferInfo);
        }
    }
    
    protected void onVideoEncoderCodecConfig(final long n, final ByteBuffer byteBuffer, final MediaCodec.BufferInfo bufferInfo) {
        if (this.getDelegate() != null) {
            this.getDelegate().onVideoEncoderCodecConfig(n, byteBuffer, bufferInfo);
        }
    }
    
    @Override
    public void setDelegate(final TuSDKVideoDataEncoderDelegate q) {
        this.q = q;
    }
    
    public TuSDKVideoDataEncoderDelegate getDelegate() {
        return this.q;
    }
    
    @Override
    public TuSDKVideoEncoderSetting getVideoEncoderSetting() {
        if (this.p == null) {
            this.p = new TuSDKVideoEncoderSetting();
        }
        return this.p;
    }
    
    @Override
    public void setVideoEncoderSetting(final TuSDKVideoEncoderSetting p) {
        this.p = p;
    }
    
    public boolean requestKeyFrame() {
        if (this.c == null || Build.VERSION.SDK_INT < 19) {
            return false;
        }
        final Bundle parameters = new Bundle();
        parameters.putInt("request-sync", 0);
        this.c.setParameters(parameters);
        return true;
    }
    
    @SuppressLint({ "InlinedApi" })
    private MediaCodec a(final TuSDKVideoEncoderSetting tuSDKVideoEncoderSetting, final MediaFormat mediaFormat) {
        mediaFormat.setString("mime", "video/avc");
        mediaFormat.setInteger("width", tuSDKVideoEncoderSetting.videoSize.width);
        mediaFormat.setInteger("height", tuSDKVideoEncoderSetting.videoSize.height);
        mediaFormat.setInteger("bitrate", tuSDKVideoEncoderSetting.videoQuality.getBitrate());
        mediaFormat.setInteger("frame-rate", tuSDKVideoEncoderSetting.videoQuality.getFps());
        mediaFormat.setInteger("i-frame-interval", tuSDKVideoEncoderSetting.mediacodecAVCIFrameInterval);
        mediaFormat.setInteger("profile", 1);
        mediaFormat.setInteger("level", 512);
        mediaFormat.setInteger("bitrate-mode", tuSDKVideoEncoderSetting.bitrateMode);
        MediaCodec encoderByType;
        try {
            encoderByType = MediaCodec.createEncoderByType(mediaFormat.getString("mime"));
            final int[] colorFormats = encoderByType.getCodecInfo().getCapabilitiesForType(mediaFormat.getString("mime")).colorFormats;
            int n = -1;
            if (a(colorFormats, 21)) {
                n = 21;
                tuSDKVideoEncoderSetting.mediacodecAVCColorFormat = 21;
            }
            if (n == -1 && a(colorFormats, 19)) {
                n = 19;
                tuSDKVideoEncoderSetting.mediacodecAVCColorFormat = 19;
            }
            if (n == -1) {
                TLog.e("!!!!!!!!!!!UnSupport,mediaCodecColorFormat", new Object[0]);
                return null;
            }
            mediaFormat.setInteger("color-format", n);
        }
        catch (IOException ex) {
            return null;
        }
        return encoderByType;
    }
    
    private static boolean a(final int[] array, final int n) {
        for (int length = array.length, i = 0; i < length; ++i) {
            if (array[i] == n) {
                return true;
            }
        }
        return false;
    }
    
    private long a(final MediaCodec.BufferInfo bufferInfo) {
        if (bufferInfo == null) {
            return 0L;
        }
        if (this.g == 0L) {
            this.g = bufferInfo.presentationTimeUs / 1000L;
        }
        return bufferInfo.presentationTimeUs / 1000L - this.g;
    }
    
    private long a(final int n) {
        return n * 1000000000L / Math.max(this.p.videoQuality.getFps(), 25);
    }
    
    private class VideoDataEncoderHandler extends Handler
    {
        public static final int WHAT_INCOMING_BUFF = 1;
        
        VideoDataEncoderHandler(final Looper looper) {
            super(looper);
        }
        
        public void handleMessage(final Message message) {
            if (!TuSDKSoftVideoDataEncoder.this.f) {
                return;
            }
            switch (message.what) {
                case 1: {
                    TuSDKSoftVideoDataEncoder.this.requestKeyFrame();
                    final int arg1 = message.arg1;
                    System.arraycopy(TuSDKSoftVideoDataEncoder.this.h[arg1].buff, 0, TuSDKSoftVideoDataEncoder.this.i.buff, 0, TuSDKSoftVideoDataEncoder.this.i.buff.length);
                    TuSDKSoftVideoDataEncoder.this.h[arg1].isReadyToFill = true;
                    if (TuSDKSoftVideoDataEncoder.this.getVideoEncoderSetting().mediacodecAVCColorFormat == 21) {
                        ColorSpaceConvert.nv21ToYuv420sp(TuSDKSoftVideoDataEncoder.this.i.buff, TuSDKSoftVideoDataEncoder.this.k.buff, TuSDKSoftVideoDataEncoder.this.getVideoEncoderSetting().videoSize.width * TuSDKSoftVideoDataEncoder.this.getVideoEncoderSetting().videoSize.height);
                    }
                    else if (TuSDKSoftVideoDataEncoder.this.getVideoEncoderSetting().mediacodecAVCColorFormat == 19) {
                        ColorSpaceConvert.nv21TOYuv420p(TuSDKSoftVideoDataEncoder.this.i.buff, TuSDKSoftVideoDataEncoder.this.k.buff, TuSDKSoftVideoDataEncoder.this.getVideoEncoderSetting().videoSize.width * TuSDKSoftVideoDataEncoder.this.getVideoEncoderSetting().videoSize.height);
                    }
                    TuSDKSoftVideoDataEncoder.this.i.isReadyToFill = true;
                    final long n = TuSDKSoftVideoDataEncoder.this.e ? (SystemClock.uptimeMillis() * 1000L) : (TuSDKSoftVideoDataEncoder.this.a(TuSDKSoftVideoDataEncoder.this.a++) / 1000L);
                    synchronized (TuSDKSoftVideoDataEncoder.this.b) {
                        if (TuSDKSoftVideoDataEncoder.this.c != null && TuSDKSoftVideoDataEncoder.this.f) {
                            final int dequeueInputBuffer = TuSDKSoftVideoDataEncoder.this.c.dequeueInputBuffer(-1L);
                            if (dequeueInputBuffer >= 0) {
                                final ByteBuffer byteBuffer = TuSDKSoftVideoDataEncoder.this.c.getInputBuffers()[dequeueInputBuffer];
                                byteBuffer.position(0);
                                byteBuffer.put(TuSDKSoftVideoDataEncoder.this.k.buff, 0, TuSDKSoftVideoDataEncoder.this.k.buff.length);
                                TuSDKSoftVideoDataEncoder.this.c.queueInputBuffer(dequeueInputBuffer, 0, TuSDKSoftVideoDataEncoder.this.k.buff.length, n, 0);
                            }
                            else {
                                TLog.d("dstVideoEncoder.dequeueInputBuffer(-1)<0", new Object[0]);
                            }
                        }
                    }
                    break;
                }
            }
        }
    }
    
    private class VideoDequeueOutputBufferThread extends Thread
    {
        private boolean b;
        
        public VideoDequeueOutputBufferThread(final String name) {
            super(name);
            this.b = false;
        }
        
        void a() {
            this.b = true;
            this.interrupt();
        }
        
        @SuppressLint("WrongConstant")
        @Override
        public void run() {
            while (!this.b && TuSDKSoftVideoDataEncoder.this.f) {
                if (TuSDKSoftVideoDataEncoder.this.c == null) {
                    continue;
                }
                final MediaCodec.BufferInfo bufferInfo = new MediaCodec.BufferInfo();
                int dequeueOutputBuffer = -1;
                try {
                    dequeueOutputBuffer = TuSDKSoftVideoDataEncoder.this.c.dequeueOutputBuffer(bufferInfo, 5000L);
                }
                catch (Exception ex) {}
                switch (dequeueOutputBuffer) {
                    case -3: {
                        continue;
                    }
                    case -1: {
                        continue;
                    }
                    case -2: {
                        TuSDKSoftVideoDataEncoder.this.onVideoEncoderStarted(TuSDKSoftVideoDataEncoder.this.c.getOutputFormat());
                        continue;
                    }
                    default: {
                        if (dequeueOutputBuffer >= 0 && bufferInfo.size > 0) {
                            final ByteBuffer byteBuffer = TuSDKSoftVideoDataEncoder.this.c.getOutputBuffers()[dequeueOutputBuffer];
                            byteBuffer.position(bufferInfo.offset + 4);
                            byteBuffer.limit(bufferInfo.offset + bufferInfo.size);
                            if ((bufferInfo.flags & 0x2) != 0x0) {
                                TuSDKSoftVideoDataEncoder.this.onVideoEncoderCodecConfig(TuSDKSoftVideoDataEncoder.this.a(bufferInfo), byteBuffer, bufferInfo);
                            }
                            else {
                                TLog.i("flags= " + bufferInfo.flags, new Object[0]);
                                if (TuSDKSoftVideoDataEncoder.this.p.enableAllKeyFrame) {
                                    if ((bufferInfo.flags & 0x1) != 0x0) {
                                        TuSDKSoftVideoDataEncoder.this.onVideoEncoderFrameDataAvailable(TuSDKSoftVideoDataEncoder.this.a(bufferInfo), byteBuffer, bufferInfo);
                                    }
                                }
                                else {
                                    TuSDKSoftVideoDataEncoder.this.onVideoEncoderFrameDataAvailable(TuSDKSoftVideoDataEncoder.this.a(bufferInfo), byteBuffer, bufferInfo);
                                }
                            }
                        }
                        TuSDKSoftVideoDataEncoder.this.c.releaseOutputBuffer(dequeueOutputBuffer, false);
                        continue;
                    }
                }
            }
        }
    }
}
