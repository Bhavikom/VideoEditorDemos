// 
// Decompiled by Procyon v0.5.36
// 

package com.yasoka.screenrecord.sdk.video.lasque.tusdk.api.video.preproc.filter;

//import org.lasque.tusdk.core.utils.StringHelper;
//import org.lasque.tusdk.core.TuSdk;
//import org.lasque.tusdk.video.editor.TuSDKMovieWriterInterface;
import android.annotation.TargetApi;
//import org.lasque.tusdk.core.utils.TLog;
import android.graphics.SurfaceTexture;
import android.opengl.EGLContext;
//import org.lasque.tusdk.core.seles.tusdk.FilterWrap;
//import org.lasque.tusdk.core.seles.SelesContext;
//import org.lasque.tusdk.core.seles.sources.SelesOutInput;
//import org.lasque.tusdk.core.encoder.audio.TuSDKAudioEncoderSetting;
//import org.lasque.tusdk.core.audio.TuSDKAudioCaptureSetting;
//import org.lasque.tusdk.core.encoder.video.TuSDKHardVideoDataEncoder;
//import org.lasque.tusdk.core.seles.video.SelesSurfaceTextureEncoder;
import android.media.MediaCodec;
import java.nio.ByteBuffer;
import android.media.MediaFormat;
//import org.lasque.tusdk.core.encoder.audio.TuSDKAudioDataEncoderInterface;
//import org.lasque.tusdk.core.encoder.TuSDKVideoDataEncoderDelegate;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.TuSdk;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.seles.SelesContext;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.seles.sources.SelesOutInput;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.seles.tusdk.FilterWrap;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.StringHelper;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.TLog;
import com.yasoka.screenrecord.sdk.video.lasque.tusdk.core.audio.TuSDKAudioCaptureSetting;
import com.yasoka.screenrecord.sdk.video.lasque.tusdk.core.audio.TuSDKAudioRecorderCore;
import com.yasoka.screenrecord.sdk.video.lasque.tusdk.core.encoder.TuSDKVideoDataEncoderDelegate;
import com.yasoka.screenrecord.sdk.video.lasque.tusdk.core.encoder.audio.TuSDKAudioDataEncoderInterface;
import com.yasoka.screenrecord.sdk.video.lasque.tusdk.core.encoder.audio.TuSDKAudioEncoderSetting;
import com.yasoka.screenrecord.sdk.video.lasque.tusdk.core.encoder.video.TuSDKHardVideoDataEncoder;
import com.yasoka.screenrecord.sdk.video.lasque.tusdk.core.encoder.video.TuSDKVideoEncoderSetting;
import com.yasoka.screenrecord.sdk.video.lasque.tusdk.core.seles.video.SelesSurfaceEncoderInterface;
import com.yasoka.screenrecord.sdk.video.lasque.tusdk.core.seles.video.SelesSurfaceTextureEncoder;
import com.yasoka.screenrecord.sdk.video.lasque.tusdk.video.editor.TuSDKMovieWriter;
import com.yasoka.screenrecord.sdk.video.lasque.tusdk.video.editor.TuSDKMovieWriterInterface;

import java.io.File;
//import org.lasque.tusdk.core.encoder.video.TuSDKVideoEncoderSetting;
//import org.lasque.tusdk.core.audio.TuSDKAudioRecorderCore;
//import org.lasque.tusdk.video.editor.TuSDKMovieWriter;
//import org.lasque.tusdk.core.seles.video.SelesSurfaceEncoderInterface;

public class TuSDKMediaRecoder
{
    private TuSDKMediaRecoderDelegate a;
    private TuSDKMediaRecoderVideoFirstSampleDelegate b;
    private State c;
    private SelesSurfaceEncoderInterface d;
    private TuSDKMovieWriter e;
    private int f;
    private boolean g;
    private TuSDKAudioRecorderCore h;
    private TuSDKVideoEncoderSetting i;
    private File j;
    private boolean k;
    private long l;
    private long m;
    private long n;
    private long o;
    private TuSDKMovieWriter.TuSDKMovieWriterDelegate p;
    private TuSDKVideoDataEncoderDelegate q;
    private TuSDKAudioDataEncoderInterface.TuSDKAudioDataEncoderDelegate r;
    
    public TuSDKMediaRecoder() {
        this.c = State.Idle;
        this.g = false;
        this.k = false;
        this.m = 0L;
        this.p = new TuSDKMovieWriter.TuSDKMovieWriterDelegate() {
            @Override
            public void onFirstVideoSampleDataWrited(final long n) {
                TuSDKMediaRecoder.this.n = n;
                if (TuSDKMediaRecoder.this.b != null) {
                    TuSDKMediaRecoder.this.b.onFirstVideoSampleDataAlready(n);
                }
            }
            
            @Override
            public void onProgressChanged(final float n, final long n2) {
                TuSDKMediaRecoder.this.l = n2;
                if (TuSDKMediaRecoder.this.a != null) {
                    TuSDKMediaRecoder.this.a.onMediaRecoderProgressChanged(n);
                }
            }
        };
        this.q = new TuSDKVideoDataEncoderDelegate() {
            @Override
            public void onVideoEncoderStarted(final MediaFormat mediaFormat) {
                if (!TuSDKMediaRecoder.this.g().canAddVideoTrack()) {
                    return;
                }
                TuSDKMediaRecoder.this.g().addVideoTrack(mediaFormat);
                if (TuSDKMediaRecoder.this.g().hasAudioTrack()) {
                    TuSDKMediaRecoder.this.g().start();
                }
                if (!TuSDKMediaRecoder.this.g && (TuSDKMediaRecoder.this.h() || TuSDKMediaRecoder.this.h == null)) {
                    TuSDKMediaRecoder.this.g().start();
                }
            }
            
            @Override
            public void onVideoEncoderFrameDataAvailable(final long n, final ByteBuffer byteBuffer, final MediaCodec.BufferInfo bufferInfo) {
                if (TuSDKMediaRecoder.this.g() == null || !TuSDKMediaRecoder.this.g().isStarted() || !TuSDKMediaRecoder.this.g().hasVideoTrack() || !TuSDKMediaRecoder.this.isRecording()) {
                    return;
                }
                if (!this.a(bufferInfo)) {
                    return;
                }
                TuSDKMediaRecoder.this.g().writeVideoSampleData(byteBuffer, bufferInfo);
            }
            
            private boolean a(final MediaCodec.BufferInfo bufferInfo) {
                if (TuSDKMediaRecoder.this.l > 0L && TuSDKMediaRecoder.this.o == 0L && !TuSDKMediaRecoder.this.h()) {
                    TuSDKMediaRecoder.this.m = TuSDKMediaRecoder.this.l - bufferInfo.presentationTimeUs;
                }
                bufferInfo.presentationTimeUs += TuSDKMediaRecoder.this.m;
                TuSDKMediaRecoder.this.l = bufferInfo.presentationTimeUs;
                return true;
            }
            
            @Override
            public void onVideoEncoderCodecConfig(final long n, final ByteBuffer byteBuffer, final MediaCodec.BufferInfo bufferInfo) {
            }
        };
        this.r = new TuSDKAudioDataEncoderInterface.TuSDKAudioDataEncoderDelegate() {
            @Override
            public void onAudioEncoderStarted(final MediaFormat mediaFormat) {
                TuSDKMediaRecoder.this.addAudioTrack(mediaFormat);
                if (TuSDKMediaRecoder.this.g().hasVideoTrack()) {
                    TuSDKMediaRecoder.this.g().start();
                }
            }
            
            @Override
            public void onAudioEncoderStoped() {
            }
            
            @Override
            public void onAudioEncoderFrameDataAvailable(final long n, final ByteBuffer byteBuffer, final MediaCodec.BufferInfo bufferInfo) {
                TuSDKMediaRecoder.this.a(byteBuffer, bufferInfo);
            }
            
            @Override
            public void onAudioEncoderCodecConfig(final long n, final ByteBuffer byteBuffer, final MediaCodec.BufferInfo bufferInfo) {
            }
        };
    }
    
    private final SelesSurfaceEncoderInterface a() {
        if (this.d != null) {
            return this.d;
        }
        final SelesSurfaceTextureEncoder d = new SelesSurfaceTextureEncoder() {
            @Override
            protected void prepareEncoder(final TuSDKVideoEncoderSetting tuSDKVideoEncoderSetting) {
                final TuSDKHardVideoDataEncoder mVideoEncoder = new TuSDKHardVideoDataEncoder();
                if (mVideoEncoder.initCodec(tuSDKVideoEncoderSetting)) {
                    this.mVideoEncoder = mVideoEncoder;
                }
            }
        };
        d.setVideoEncoderSetting(this.getVideoEncoderSetting());
        d.setDelegate(this.q);
        return this.d = d;
    }
    
    public void setSelesSurfaceEncoder(final SelesSurfaceEncoderInterface d) {
        this.d = d;
        if (this.d != null) {
            this.d.setDelegate(this.q);
        }
    }
    
    private TuSDKAudioRecorderCore b() {
        if (this.h == null) {
            this.h = new TuSDKAudioRecorderCore(TuSDKAudioCaptureSetting.defaultCaptureSetting(), TuSDKAudioEncoderSetting.defaultEncoderSetting());
            this.h.getAudioEncoder().setDelegate(this.r);
        }
        return this.h;
    }
    
    public TuSDKMediaRecoder setDelegate(final TuSDKMediaRecoderDelegate a) {
        this.a = a;
        return this;
    }
    
    public TuSDKMediaRecoderDelegate getDelegate() {
        return this.a;
    }
    
    public TuSDKMediaRecoder setVideoFirstSampleDelegate(final TuSDKMediaRecoderVideoFirstSampleDelegate b) {
        this.b = b;
        return this;
    }
    
    public void updateFilter(final SelesOutInput selesOutInput) {
        if (selesOutInput == null) {
            return;
        }
        selesOutInput.addTarget((SelesContext.SelesInput)this.a(), 0);
    }
    
    public void updateFilter(final FilterWrap filterWrap) {
        if (filterWrap == null) {
            return;
        }
        filterWrap.addTarget((SelesContext.SelesInput)this.a(), 0);
    }
    
    public State getState() {
        return this.c;
    }
    
    private void a(final State c) {
        if (this.c == c) {
            return;
        }
        this.c = c;
        if (this.a != null) {
            this.a.onMediaRecoderStateChanged(c);
        }
    }
    
    @TargetApi(17)
    public void startRecording(final EGLContext eglContext, final SurfaceTexture surfaceTexture) {
        if (surfaceTexture == null) {
            TLog.e("startRecording : The surfaceTexture parameters cannot be null", new Object[0]);
            return;
        }
        if (this.isRecording()) {
            return;
        }
        this.e();
        this.c();
        this.startVideoDataEncoder(eglContext, surfaceTexture);
        this.a(State.Recording);
    }
    
    public void pauseRecording() {
        if (!this.isRecording()) {
            return;
        }
        if (this.d != null) {
            this.d.pausdRecording();
        }
        this.d();
        this.m = 0L;
        this.o = 0L;
        this.a(State.Paused);
    }
    
    public void stopRecording() {
        if (this.c == State.Idle) {
            return;
        }
        this.a(State.Saving);
        this.d();
        this.stopVideoDataEncoder();
        this.f();
        this.m = 0L;
        this.o = 0L;
        this.a(State.RecordCompleted);
        this.c = State.Idle;
    }
    
    public void cancelRecording() {
        if (this.c == State.Idle) {
            return;
        }
        this.d();
        this.stopVideoDataEncoder();
        this.f();
        if (this.j != null && this.j.exists()) {
            this.j.delete();
        }
        this.m = 0L;
        this.o = 0L;
        this.a(State.Canceled);
        this.c = State.Idle;
    }
    
    public boolean isRecording() {
        return this.d != null && this.e != null && !this.isPaused() && this.d.isRecording() && this.e.isStarted();
    }
    
    public boolean isPaused() {
        return this.d != null && this.e != null && this.c == State.Paused;
    }
    
    public void startVideoDataEncoder(final EGLContext eglContext, final SurfaceTexture surfaceTexture) {
        if (surfaceTexture == null) {
            TLog.e("startVideoDataEncoder : The surfaceTexture parameters is null", new Object[0]);
            return;
        }
        this.a().startRecording(eglContext, surfaceTexture);
    }
    
    public void stopVideoDataEncoder() {
        if (this.d != null) {
            this.d.stopRecording();
        }
    }
    
    private boolean c() {
        if (this.h()) {
            return true;
        }
        final TuSDKAudioRecorderCore b = this.b();
        if (b == null || !b.isPrepared()) {
            TLog.e("Please open the audio permissions", new Object[0]);
            this.h = null;
            return false;
        }
        b.startRecording();
        return false;
    }
    
    private void d() {
        if (this.h != null) {
            this.h.stopRecording();
        }
    }
    
    private final TuSDKMediaRecoder e() {
        if (this.e == null) {
            (this.e = TuSDKMovieWriter.create(this.getOutputFilePath().toString(), TuSDKMovieWriterInterface.MovieWriterOutputFormat.MPEG_4)).setDelegate(this.p);
            this.e.setOrientationHint(this.f);
        }
        return this;
    }
    
    public final File getOutputFilePath() {
        if (this.j == null) {
            this.j = new File(TuSdk.getAppTempPath(), String.format("lsq_%s.mp4", StringHelper.timeStampString()));
        }
        return this.j;
    }
    
    public final TuSDKMediaRecoder setOutputFilePath(final File j) {
        if (this.isRecording()) {
            TLog.w("Please set the output path before starting the recording", new Object[0]);
            return this;
        }
        this.j = j;
        return this;
    }
    
    private void f() {
        if (this.e != null) {
            this.e.stop();
            this.e = null;
        }
    }
    
    private TuSDKMovieWriter g() {
        if (this.e == null) {
            this.e();
        }
        return this.e;
    }
    
    private boolean h() {
        return this.k;
    }
    
    public TuSDKMediaRecoder setMute(final boolean k) {
        this.k = k;
        return this;
    }
    
    public TuSDKMediaRecoder setEnableExternalAudio(final boolean g) {
        this.g = g;
        if (this.g) {
            this.setMute(true);
        }
        return this;
    }
    
    public TuSDKMediaRecoder setOrientationHint(final int f) {
        this.f = f;
        return this;
    }
    
    public TuSDKVideoEncoderSetting getVideoEncoderSetting() {
        if (this.i == null) {
            this.i = TuSDKVideoEncoderSetting.getDefaultRecordSetting();
        }
        return this.i;
    }
    
    public TuSDKMediaRecoder setVideoEncoderSetting(final TuSDKVideoEncoderSetting tuSDKVideoEncoderSetting) {
        this.i = tuSDKVideoEncoderSetting;
        if (this.d != null) {
            this.d.setVideoEncoderSetting(tuSDKVideoEncoderSetting);
        }
        return this;
    }
    
    public void addAudioTrack(final MediaFormat mediaFormat) {
        if (this.h() && !this.g) {
            return;
        }
        if (!this.g().canAddAudioTrack()) {
            return;
        }
        this.g().addAudioTrack(mediaFormat);
    }
    
    private boolean a(final MediaCodec.BufferInfo bufferInfo) {
        if (this.n > 0L && this.o == 0L) {
            this.o = this.n - bufferInfo.presentationTimeUs;
        }
        bufferInfo.presentationTimeUs += this.o;
        this.n = bufferInfo.presentationTimeUs;
        return true;
    }
    
    public void writeExternalAudioSampleData(final ByteBuffer byteBuffer, final MediaCodec.BufferInfo bufferInfo) {
        if (!this.g || !this.h()) {
            TLog.e("Please set enableExternalAudio for true. ", new Object[0]);
            return;
        }
        this.a(byteBuffer, bufferInfo);
    }
    
    private void a(final ByteBuffer byteBuffer, final MediaCodec.BufferInfo bufferInfo) {
        if (this.g() == null || !this.g().isStarted() || !this.g().hasAudioTrack() || !this.isRecording()) {
            return;
        }
        if (!this.a(bufferInfo)) {
            return;
        }
        this.g().writeAudioSampleData(byteBuffer, bufferInfo);
    }
    
    public TuSDKAudioDataEncoderInterface.TuSDKAudioDataEncoderDelegate getAudioDataEncoderDelegate() {
        return this.r;
    }
    
    public enum State
    {
        Idle, 
        Recording, 
        Paused, 
        Saving, 
        RecordCompleted, 
        Canceled;
    }
    
    public interface TuSDKMediaRecoderVideoFirstSampleDelegate
    {
        void onFirstVideoSampleDataAlready(final long p0);
    }
    
    public interface TuSDKMediaRecoderDelegate
    {
        void onMediaRecoderProgressChanged(final float p0);
        
        void onMediaRecoderStateChanged(final State p0);
    }
}
