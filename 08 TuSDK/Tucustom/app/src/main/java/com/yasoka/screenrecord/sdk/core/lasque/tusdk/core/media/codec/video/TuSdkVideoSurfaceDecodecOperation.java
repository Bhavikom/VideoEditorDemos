// 
// Decompiled by Procyon v0.5.36
// 

package com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.media.codec.video;

import android.media.MediaCodec;
import android.view.Surface;
//import org.lasque.tusdk.core.utils.ThreadHelper;
//import org.lasque.tusdk.core.utils.TLog;
//import org.lasque.tusdk.core.media.codec.exception.TuSdkNoMediaTrackException;
//import org.lasque.tusdk.core.media.codec.extend.TuSdkMediaUtils;
//import org.lasque.tusdk.core.media.codec.TuSdkMediaExtractor;
import android.media.MediaFormat;
//import org.lasque.tusdk.core.media.codec.TuSdkCodecOutput;
import java.nio.ByteBuffer;
//import org.lasque.tusdk.core.media.codec.extend.TuSdkMediaCodec;
import android.annotation.TargetApi;

import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.media.codec.TuSdkCodecOutput;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.media.codec.TuSdkDecodecOperation;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.media.codec.TuSdkMediaExtractor;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.media.codec.exception.TuSdkNoMediaTrackException;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.media.codec.extend.TuSdkMediaCodec;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.media.codec.extend.TuSdkMediaUtils;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.TLog;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.ThreadHelper;
//import org.lasque.tusdk.core.media.codec.TuSdkDecodecOperation;

@TargetApi(18)
public class TuSdkVideoSurfaceDecodecOperation implements TuSdkDecodecOperation
{
    private int a;
    private TuSdkMediaCodec b;
    private boolean c;
    private ByteBuffer[] d;
    private final long e = 10000L;
    private TuSdkCodecOutput.TuSdkDecodecVideoSurfaceOutput f;
    private TuSdkVideoSurfaceDecodecOperationPatch g;
    private MediaFormat h;
    private boolean i;
    
    public TuSdkVideoSurfaceDecodecOperation(final TuSdkCodecOutput.TuSdkDecodecVideoSurfaceOutput f) {
        this.a = -1;
        this.i = false;
        if (f == null) {
            throw new NullPointerException(String.format("%s init failed, codecOutput is Empty", "TuSdkVideoSurfaceDecodecOperation"));
        }
        this.f = f;
    }
    
    @Override
    public void flush() {
        if (this.b == null || this.i) {
            return;
        }
        this.b.flush();
    }
    
    @Override
    public boolean decodecInit(final TuSdkMediaExtractor tuSdkMediaExtractor) {
        this.a = TuSdkMediaUtils.getMediaTrackIndex(tuSdkMediaExtractor, "video/");
        if (this.a < 0) {
            this.decodecException(new TuSdkNoMediaTrackException(String.format("%s decodecInit can not find media track: %s", "TuSdkVideoSurfaceDecodecOperation", "video/")));
            TLog.e("%s decodecInit mTrackIndex result false  ", "TuSdkVideoSurfaceDecodecOperation");
            return false;
        }
        this.h = tuSdkMediaExtractor.getTrackFormat(this.a);
        tuSdkMediaExtractor.selectTrack(this.a);
        if (!this.f.canSupportMediaInfo(this.a, this.h)) {
            TLog.e("%s decodecInit mDecodecOutput result false  ", "TuSdkVideoSurfaceDecodecOperation");
            TLog.w("%s decodecInit can not Support MediaInfo: %s", "TuSdkVideoSurfaceDecodecOperation", this.h);
            return false;
        }
        Surface requestSurface = null;
        while (!ThreadHelper.isInterrupted() && (requestSurface = this.f.requestSurface()) == null) {}
        this.b = this.a().patchMediaCodec(this.h.getString("mime"));
        if (this.b.configureError() != null || !this.b.configure(this.h, requestSurface, null, 0)) {
            this.decodecException(this.b.configureError());
            this.b = null;
            return false;
        }
        this.b.start();
        this.d = this.b.getOutputBuffers();
        this.c = false;
        return true;
    }
    
    private TuSdkVideoSurfaceDecodecOperationPatch a() {
        if (this.g == null) {
            this.g = new TuSdkVideoSurfaceDecodecOperationPatch();
        }
        return this.g;
    }
    
    @Override
    public boolean decodecProcessUntilEnd(final TuSdkMediaExtractor tuSdkMediaExtractor) {
        final TuSdkMediaCodec b = this.b;
        if (b == null) {
            return true;
        }
        if (!this.c) {
            this.c = this.f.processExtractor(tuSdkMediaExtractor, b);
        }
        final MediaCodec.BufferInfo bufferInfo = new MediaCodec.BufferInfo();
        final int dequeueOutputBuffer = b.dequeueOutputBuffer(bufferInfo, 10000L);
        switch (dequeueOutputBuffer) {
            case -2: {
                this.f.outputFormatChanged(b.getOutputFormat());
                break;
            }
            case -1: {
                break;
            }
            case -3: {
                this.d = b.getOutputBuffers();
                break;
            }
            default: {
                if (dequeueOutputBuffer < 0) {
                    break;
                }
                if (bufferInfo.size > 0) {
                    this.f.processOutputBuffer(tuSdkMediaExtractor, this.a, this.d[dequeueOutputBuffer], bufferInfo);
                }
                if (!this.i) {
                    b.releaseOutputBuffer(dequeueOutputBuffer, true);
                }
                this.f.updated(bufferInfo);
                break;
            }
        }
        return (bufferInfo.flags & 0x4) != 0x0 && this.f.updatedToEOS(bufferInfo);
    }
    
    @Override
    public void decodecRelease() {
        this.i = true;
        if (this.b == null) {
            return;
        }
        this.b.stop();
        this.b.release();
        this.b = null;
    }
    
    @Override
    protected void finalize() {
        this.decodecRelease();
        try {
            super.finalize();
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
    }
    
    @Override
    public void decodecException(final Exception ex) {
        this.f.onCatchedException(ex);
    }
}
