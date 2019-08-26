// 
// Decompiled by Procyon v0.5.36
// 

package com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.media.codec.extend;

import android.media.MediaCodecInfo;
import android.os.Handler;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.media.Image;
import java.nio.ByteBuffer;
//import org.lasque.tusdk.core.utils.TLog;
import android.media.MediaDescrambler;
import android.media.MediaCrypto;
import android.media.MediaFormat;
import android.view.Surface;
import android.media.MediaCodec;
import android.annotation.TargetApi;

import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.TLog;

@TargetApi(18)
public final class TuSdkMediaCodecImpl implements TuSdkMediaCodec
{
    private final MediaCodec a;
    private Exception b;
    private boolean c;
    private boolean d;
    
    public static TuSdkMediaCodec createDecoderByType(final String s) {
        return a(s, true, false);
    }
    
    public static TuSdkMediaCodec createEncoderByType(final String s) {
        return a(s, true, true);
    }
    
    public static TuSdkMediaCodec createByCodecName(final String s) {
        return a(s, false, false);
    }
    
    @TargetApi(23)
    public static Surface createPersistentInputSurface() {
        return MediaCodec.createPersistentInputSurface();
    }
    
    private static TuSdkMediaCodec a(final String s, final boolean b, final boolean b2) {
        MediaCodec mediaCodec = null;
        Exception b3 = null;
        try {
            if (!b) {
                mediaCodec = MediaCodec.createByCodecName(s);
            }
            else if (!b2) {
                mediaCodec = MediaCodec.createDecoderByType(s);
            }
            else {
                mediaCodec = MediaCodec.createEncoderByType(s);
            }
        }
        catch (Exception ex) {
            b3 = ex;
        }
        final TuSdkMediaCodecImpl tuSdkMediaCodecImpl = new TuSdkMediaCodecImpl(mediaCodec);
        tuSdkMediaCodecImpl.b = b3;
        return tuSdkMediaCodecImpl;
    }
    
    @Override
    public boolean isStarted() {
        return this.c;
    }
    
    @Override
    public boolean isReleased() {
        return this.d;
    }
    
    @Override
    public Exception configureError() {
        return this.b;
    }
    
    private TuSdkMediaCodecImpl(final MediaCodec a) {
        this.c = false;
        this.d = false;
        this.a = a;
    }
    
    @TargetApi(21)
    @Override
    public final boolean reset() {
        try {
            this.a.reset();
        }
        catch (Exception b) {
            this.b = b;
            return false;
        }
        return true;
    }
    
    @Override
    public final boolean release() {
        try {
            this.a.release();
        }
        catch (Exception b) {
            this.b = b;
            return false;
        }
        this.c = false;
        return this.d = true;
    }
    
    @Override
    public boolean configure(final MediaFormat mediaFormat, final Surface surface, final MediaCrypto mediaCrypto, final int n) {
        try {
            this.a.configure(mediaFormat, surface, mediaCrypto, n);
        }
        catch (Exception b) {
            this.b = b;
            return false;
        }
        return true;
    }
    
    @TargetApi(26)
    @Override
    public boolean configure(final MediaFormat mediaFormat, final Surface surface, final int n, final MediaDescrambler mediaDescrambler) {
        try {
            this.a.configure(mediaFormat, surface, n, mediaDescrambler);
        }
        catch (Exception b) {
            this.b = b;
            return false;
        }
        return true;
    }
    
    @TargetApi(23)
    @Override
    public boolean setOutputSurface(final Surface outputSurface) {
        try {
            this.a.setOutputSurface(outputSurface);
        }
        catch (Exception b) {
            this.b = b;
            return false;
        }
        return true;
    }
    
    @TargetApi(23)
    @Override
    public boolean setInputSurface(final Surface inputSurface) {
        try {
            this.a.setInputSurface(inputSurface);
        }
        catch (Exception b) {
            this.b = b;
            return false;
        }
        return true;
    }
    
    @Override
    public final Surface createInputSurface() {
        try {
            return this.a.createInputSurface();
        }
        catch (Exception b) {
            this.b = b;
            return null;
        }
    }
    
    @Override
    public final boolean start() {
        try {
            this.a.start();
        }
        catch (Exception b) {
            this.b = b;
            this.d = true;
            return false;
        }
        this.d = false;
        return this.c = true;
    }
    
    @Override
    public final boolean stop() {
        try {
            this.a.stop();
        }
        catch (Exception b) {
            this.b = b;
            return false;
        }
        this.d = true;
        this.c = false;
        return true;
    }
    
    @Override
    public final boolean flush() {
        try {
            this.a.flush();
        }
        catch (Exception b) {
            this.b = b;
            return false;
        }
        return true;
    }
    
    @Override
    public final boolean queueInputBuffer(final int n, final int n2, final int n3, final long n4, final int n5) {
        try {
            this.a.queueInputBuffer(n, n2, n3, n4, n5);
        }
        catch (Exception b) {
            this.b = b;
            return false;
        }
        return true;
    }
    
    @Override
    public final boolean queueSecureInputBuffer(final int n, final int n2, final MediaCodec.CryptoInfo cryptoInfo, final long n3, final int n4) {
        try {
            this.a.queueSecureInputBuffer(n, n2, cryptoInfo, n3, n4);
        }
        catch (Exception b) {
            this.b = b;
            return false;
        }
        return true;
    }
    
    @Override
    public final int dequeueInputBuffer(final long n) {
        try {
            return this.a.dequeueInputBuffer(n);
        }
        catch (Exception b) {
            TLog.e(this.b = b, "%s dequeueInputBuffer failed, ignore then try once.", "TuSdkMediaCodecImpl");
            return -1;
        }
    }
    
    @Override
    public final int dequeueOutputBuffer(final MediaCodec.BufferInfo bufferInfo, final long n) {
        try {
            return this.a.dequeueOutputBuffer(bufferInfo, n);
        }
        catch (Exception b) {
            TLog.e(this.b = b, "%s dequeueOutputBuffer failed, ignore then try once.", "TuSdkMediaCodecImpl");
            return -1;
        }
    }
    
    @Override
    public final boolean releaseOutputBuffer(final int n, final boolean b) {
        try {
            this.a.releaseOutputBuffer(n, b);
        }
        catch (Exception b2) {
            this.b = b2;
            return false;
        }
        return true;
    }
    
    @TargetApi(21)
    @Override
    public final boolean releaseOutputBuffer(final int n, final long n2) {
        try {
            this.a.releaseOutputBuffer(n, n2);
        }
        catch (Exception b) {
            this.b = b;
            return false;
        }
        return true;
    }
    
    @Override
    public final boolean signalEndOfInputStream() {
        try {
            this.a.signalEndOfInputStream();
        }
        catch (Exception b) {
            this.b = b;
            return false;
        }
        return true;
    }
    
    @Override
    public final MediaFormat getOutputFormat() {
        try {
            return this.a.getOutputFormat();
        }
        catch (Exception b) {
            TLog.e(this.b = b, "%s getOutputFormat failed, ignore then try once.", "TuSdkMediaCodecImpl");
            return null;
        }
    }
    
    @TargetApi(21)
    @Override
    public final MediaFormat getInputFormat() {
        try {
            return this.a.getInputFormat();
        }
        catch (Exception b) {
            TLog.e(this.b = b, "%s getInputFormat failed, ignore then try once.", "TuSdkMediaCodecImpl");
            return null;
        }
    }
    
    @TargetApi(21)
    @Override
    public final MediaFormat getOutputFormat(final int n) {
        try {
            return this.a.getOutputFormat(n);
        }
        catch (Exception b) {
            TLog.e(this.b = b, "%s getOutputFormat failed, ignore then try once.", "TuSdkMediaCodecImpl");
            return null;
        }
    }
    
    @Override
    public ByteBuffer[] getInputBuffers() {
        try {
            return this.a.getInputBuffers();
        }
        catch (Exception b) {
            TLog.e(this.b = b, "%s getInputBuffers failed, ignore then try once.", "TuSdkMediaCodecImpl");
            return null;
        }
    }
    
    @Override
    public ByteBuffer[] getOutputBuffers() {
        try {
            return this.a.getOutputBuffers();
        }
        catch (Exception b) {
            TLog.e(this.b = b, "%s getOutputBuffers failed, ignore then try once.", "TuSdkMediaCodecImpl");
            return null;
        }
    }
    
    @TargetApi(21)
    @Override
    public ByteBuffer getInputBuffer(final int n) {
        try {
            return this.a.getInputBuffer(n);
        }
        catch (Exception b) {
            TLog.e(this.b = b, "%s getInputBuffer failed, ignore then try once.", "TuSdkMediaCodecImpl");
            return null;
        }
    }
    
    @TargetApi(21)
    @Override
    public Image getInputImage(final int n) {
        try {
            return this.a.getInputImage(n);
        }
        catch (Exception b) {
            TLog.e(this.b = b, "%s getInputImage failed, ignore then try once.", "TuSdkMediaCodecImpl");
            return null;
        }
    }
    
    @TargetApi(21)
    @Override
    public ByteBuffer getOutputBuffer(final int n) {
        try {
            return this.a.getOutputBuffer(n);
        }
        catch (Exception b) {
            TLog.e(this.b = b, "%s getOutputBuffer failed, ignore then try once.", "TuSdkMediaCodecImpl");
            return null;
        }
    }
    
    @TargetApi(21)
    @Override
    public Image getOutputImage(final int n) {
        try {
            return this.a.getOutputImage(n);
        }
        catch (Exception b) {
            TLog.e(this.b = b, "%s getOutputImage failed,, ignore then try once.", "TuSdkMediaCodecImpl");
            return null;
        }
    }
    
    @Override
    public final boolean setVideoScalingMode(final int videoScalingMode) {
        try {
            this.a.setVideoScalingMode(videoScalingMode);
        }
        catch (Exception b) {
            this.b = b;
            return false;
        }
        return true;
    }
    
    @Override
    public final String getName() {
        try {
            return this.a.getName();
        }
        catch (Exception b) {
            TLog.e(this.b = b, "%s getName failed, ignore then try once.", "TuSdkMediaCodecImpl");
            return null;
        }
    }
    
    @TargetApi(26)
    @Override
    public PersistableBundle getMetrics() {
        try {
            return this.a.getMetrics();
        }
        catch (Exception b) {
            TLog.e(this.b = b, "%s getMetrics failed, ignore then try once.", "TuSdkMediaCodecImpl");
            return null;
        }
    }
    
    @TargetApi(19)
    @Override
    public final boolean setParameters(final Bundle parameters) {
        try {
            this.a.setParameters(parameters);
        }
        catch (Exception b) {
            this.b = b;
            return false;
        }
        return true;
    }
    
    @TargetApi(23)
    @Override
    public boolean setCallback(final MediaCodec.Callback callback, final Handler handler) {
        try {
            this.a.setCallback(callback, handler);
        }
        catch (Exception b) {
            this.b = b;
            return false;
        }
        return true;
    }
    
    @TargetApi(21)
    @Override
    public boolean setCallback(final MediaCodec.Callback callback) {
        try {
            this.a.setCallback(callback);
        }
        catch (Exception b) {
            this.b = b;
            return false;
        }
        return true;
    }
    
    @TargetApi(23)
    @Override
    public boolean setOnFrameRenderedListener(final MediaCodec.OnFrameRenderedListener onFrameRenderedListener, final Handler handler) {
        try {
            this.a.setOnFrameRenderedListener(onFrameRenderedListener, handler);
        }
        catch (Exception b) {
            this.b = b;
            return false;
        }
        return true;
    }
    
    @Override
    public MediaCodecInfo getCodecInfo() {
        try {
            return this.a.getCodecInfo();
        }
        catch (Exception b) {
            TLog.e(this.b = b, "%s getCodecInfo failed, ignore then try once.", "TuSdkMediaCodecImpl");
            return null;
        }
    }
}
