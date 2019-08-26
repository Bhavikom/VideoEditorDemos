// 
// Decompiled by Procyon v0.5.36
// 

package com.yasoka.screenrecord.sdk.video.lasque.tusdk.core.utils.hardware;

import android.os.Message;
import android.os.Looper;
import android.os.Handler;
//import org.lasque.tusdk.core.seles.tusdk.FilterManager;
//import org.lasque.tusdk.core.utils.image.AlbumHelper;
//import org.lasque.tusdk.core.utils.StringHelper;
//import org.lasque.tusdk.core.TuSdk;
//import org.lasque.tusdk.core.utils.sqllite.ImageSqlHelper;
//import org.lasque.tusdk.core.video.TuSDKVideoResult;
//import org.lasque.tusdk.core.utils.ThreadHelper;
import android.content.Intent;
import android.net.Uri;
import java.io.IOException;
//import org.lasque.tusdk.core.secret.StatisticsManger;
//import org.lasque.tusdk.core.seles.video.SelesSurfaceEncoderInterface;
//import org.lasque.tusdk.impl.components.camera.TuVideoFocusTouchView;
import java.util.Iterator;
//import org.lasque.tusdk.core.utils.FileHelper;
//import org.lasque.tusdk.core.utils.TLog;
//import org.lasque.tusdk.core.secret.SdkValid;
//import org.lasque.tusdk.core.encoder.video.TuSDKVideoEncoderSetting;
import android.media.MediaCodec;
import java.nio.ByteBuffer;
import android.media.MediaFormat;
import java.util.ArrayList;
import android.widget.RelativeLayout;
//import org.lasque.tusdk.core.video.TuSDKVideoCaptureSetting;
import android.content.Context;
//import org.lasque.tusdk.core.encoder.TuSDKVideoDataEncoderDelegate;
//import org.lasque.tusdk.core.encoder.audio.TuSDKAudioDataEncoderInterface;
//import org.lasque.tusdk.api.movie.postproc.muxer.TuSDKMovieClipper;
import android.os.HandlerThread;
import android.media.MediaMuxer;
import java.io.File;
//import org.lasque.tusdk.video.editor.TuSdkTimeRange;
import java.util.List;
import android.annotation.TargetApi;
import android.annotation.SuppressLint;

import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.TuSdk;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.secret.SdkValid;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.secret.StatisticsManger;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.seles.tusdk.FilterManager;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.FileHelper;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.StringHelper;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.TLog;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.ThreadHelper;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.hardware.TuSdkStillCameraAdapter;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.image.AlbumHelper;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.sqllite.ImageSqlHelper;
import com.yasoka.screenrecord.sdk.video.lasque.tusdk.api.movie.postproc.muxer.TuSDKMovieClipper;
import com.yasoka.screenrecord.sdk.video.lasque.tusdk.core.encoder.TuSDKVideoDataEncoderDelegate;
import com.yasoka.screenrecord.sdk.video.lasque.tusdk.core.encoder.audio.TuSDKAudioDataEncoderInterface;
import com.yasoka.screenrecord.sdk.video.lasque.tusdk.core.encoder.video.TuSDKVideoEncoderSetting;
import com.yasoka.screenrecord.sdk.video.lasque.tusdk.core.seles.sources.TuSDKMovieCreatorInterface;
import com.yasoka.screenrecord.sdk.video.lasque.tusdk.core.seles.video.SelesSurfaceEncoderInterface;
import com.yasoka.screenrecord.sdk.video.lasque.tusdk.core.video.TuSDKVideoCaptureSetting;
import com.yasoka.screenrecord.sdk.video.lasque.tusdk.core.video.TuSDKVideoResult;
import com.yasoka.screenrecord.sdk.video.lasque.tusdk.impl.components.camera.TuVideoFocusTouchView;
import com.yasoka.screenrecord.sdk.video.lasque.tusdk.video.editor.TuSdkTimeRange;
//import org.lasque.tusdk.core.seles.sources.TuSDKMovieCreatorInterface;

@SuppressLint({ "InlinedApi" })
@TargetApi(18)
public class TuSDKRecordVideoCamera extends TuSDKVideoCamera implements TuSDKMovieCreatorInterface
{
    private RecordState b;
    private RecordMode c;
    private List<TuSdkTimeRange> d;
    private List<TuSdkTimeRange> e;
    private File f;
    private boolean g;
    private String h;
    private long i;
    protected MediaMuxer mMuxer;
    private boolean j;
    private int k;
    private int l;
    private HandlerThread m;
    private RecordHandler n;
    private TuSDKMovieClipper o;
    private TuSDKRecordVideoCameraDelegate p;
    private int q;
    private int r;
    private long s;
    private long t;
    private boolean u;
    private boolean v;
    private SpeedMode w;
    private TuSDKFrameSpeedRateController x;
    private TuSDKFrameSpeedRateController y;
    TuSDKMovieClipper.TuSDKMovieClipperListener a;
    private TuSDKAudioDataEncoderInterface.TuSDKAudioDataEncoderDelegate z;
    private TuSDKVideoDataEncoderDelegate A;
    
    public TuSDKRecordVideoCamera(final Context context, final TuSDKVideoCaptureSetting tuSDKVideoCaptureSetting, final RelativeLayout relativeLayout) {
        super(context, tuSDKVideoCaptureSetting, relativeLayout);
        this.c = RecordMode.Normal;
        this.d = new ArrayList<TuSdkTimeRange>(5);
        this.e = new ArrayList<TuSdkTimeRange>(5);
        this.g = true;
        this.i = 52428800L;
        this.j = false;
        this.k = -1;
        this.l = -1;
        this.q = 2;
        this.r = 10;
        this.w = SpeedMode.NORMAL;
        this.x = new TuSDKFrameSpeedRateController();
        this.y = new TuSDKFrameSpeedRateController();
        this.a = new TuSDKMovieClipper.TuSDKMovieClipperListener() {
            @Override
            public void onStart() {
                TuSDKRecordVideoCamera.this.notifyRecordingState(RecordState.Saving);
            }
            
            @Override
            public void onCancel() {
            }
            
            @Override
            public void onDone(final String pathname) {
                TuSDKRecordVideoCamera.this.s();
                TuSDKRecordVideoCamera.this.notifyRecordingResultWithVideoFile(new File(pathname));
            }
            
            @Override
            public void onError(final Exception ex) {
                TuSDKRecordVideoCamera.this.a(RecordError.SaveFailed);
            }
        };
        this.z = new TuSDKAudioDataEncoderInterface.TuSDKAudioDataEncoderDelegate() {
            @Override
            public void onAudioEncoderStarted(final MediaFormat mediaFormat) {
                if (!TuSDKRecordVideoCamera.this.j && !TuSDKRecordVideoCamera.this.y()) {
                    final MediaMuxer mediaMuxer = TuSDKRecordVideoCamera.this.getMediaMuxer();
                    if (mediaMuxer == null) {
                        return;
                    }
                    TuSDKRecordVideoCamera.this.l = mediaMuxer.addTrack(mediaFormat);
                    if (TuSDKRecordVideoCamera.this.z()) {
                        TuSDKRecordVideoCamera.this.getMediaMuxer().start();
                        TuSDKRecordVideoCamera.this.j = true;
                    }
                }
            }
            
            @Override
            public void onAudioEncoderStoped() {
            }
            
            @Override
            public void onAudioEncoderFrameDataAvailable(final long n, final ByteBuffer buffer, final MediaCodec.BufferInfo bufferInfo) {
                if (TuSDKRecordVideoCamera.this.j && TuSDKRecordVideoCamera.this.y() && TuSDKRecordVideoCamera.this.isRecording() && !TuSDKRecordVideoCamera.this.isRecordingPaused() && TuSDKRecordVideoCamera.this.s > 0L) {
                    final ByteDataFrame byteDataFrame = new ByteDataFrame();
                    byteDataFrame.trackIndex = TuSDKRecordVideoCamera.this.l;
                    byteDataFrame.buffer = buffer;
                    byteDataFrame.bufferInfo = bufferInfo;
                    TuSDKRecordVideoCamera.this.n.sendMessage(TuSDKRecordVideoCamera.this.n.obtainMessage(6, (Object)byteDataFrame));
                }
            }
            
            @Override
            public void onAudioEncoderCodecConfig(final long n, final ByteBuffer byteBuffer, final MediaCodec.BufferInfo bufferInfo) {
            }
        };
        this.A = new TuSDKVideoDataEncoderDelegate() {
            @Override
            public void onVideoEncoderStarted(final MediaFormat mediaFormat) {
                if (!TuSDKRecordVideoCamera.this.j && !TuSDKRecordVideoCamera.this.z()) {
                    final MediaMuxer mediaMuxer = TuSDKRecordVideoCamera.this.getMediaMuxer();
                    if (mediaMuxer == null) {
                        return;
                    }
                    TuSDKRecordVideoCamera.this.k = mediaMuxer.addTrack(mediaFormat);
                    if (TuSDKRecordVideoCamera.this.x()) {
                        if (TuSDKRecordVideoCamera.this.y()) {
                            TuSDKRecordVideoCamera.this.getMediaMuxer().start();
                            TuSDKRecordVideoCamera.this.j = true;
                        }
                    }
                    else {
                        TuSDKRecordVideoCamera.this.getMediaMuxer().start();
                        TuSDKRecordVideoCamera.this.j = true;
                    }
                }
            }
            
            @Override
            public void onVideoEncoderFrameDataAvailable(final long n, final ByteBuffer buffer, final MediaCodec.BufferInfo bufferInfo) {
                if (TuSDKRecordVideoCamera.this.j && TuSDKRecordVideoCamera.this.z() && TuSDKRecordVideoCamera.this.isRecording() && !TuSDKRecordVideoCamera.this.isRecordingPaused()) {
                    final ByteDataFrame byteDataFrame = new ByteDataFrame();
                    byteDataFrame.trackIndex = TuSDKRecordVideoCamera.this.k;
                    byteDataFrame.buffer = buffer;
                    byteDataFrame.bufferInfo = bufferInfo;
                    TuSDKRecordVideoCamera.this.n.sendMessage(TuSDKRecordVideoCamera.this.n.obtainMessage(6, (Object)byteDataFrame));
                }
            }
            
            @Override
            public void onVideoEncoderCodecConfig(final long n, final ByteBuffer byteBuffer, final MediaCodec.BufferInfo bufferInfo) {
            }
        };
        (this.m = new HandlerThread("WriteSampleDataWriter")).start();
        this.n = new RecordHandler(this.m.getLooper());
    }
    
    public TuSDKRecordVideoCamera(final Context context, final RelativeLayout relativeLayout) {
        this(context, new TuSDKVideoCaptureSetting(), relativeLayout);
    }
    
    @Override
    public TuSDKVideoEncoderSetting getVideoEncoderSetting() {
        if (this.mVideoEncoderSetting == null) {
            this.mVideoEncoderSetting = TuSDKVideoEncoderSetting.getDefaultRecordSetting();
        }
        return super.getVideoEncoderSetting();
    }
    
    public TuSDKRecordVideoCameraDelegate getVideoDelegate() {
        return this.p;
    }
    
    public void setVideoDelegate(final TuSDKRecordVideoCameraDelegate p) {
        this.p = p;
    }
    
    public final void setRecordMode(final RecordMode c) {
        if (c == null) {
            return;
        }
        if (c == RecordMode.Keep && !SdkValid.shared.videoRecordContinuousEnabled()) {
            TLog.e("You are not allowed to use the record continuous mode, please see http://tusdk.com", new Object[0]);
            return;
        }
        this.c = c;
    }
    
    public RecordMode getRecordMode() {
        return this.c;
    }
    
    private boolean a() {
        if (this.getContext() == null || !FileHelper.mountedExternalStorage()) {
            return false;
        }
        final File movieOutputPath = this.getMovieOutputPath();
        if (movieOutputPath == null) {
            TLog.e("TuSDKRecordVideoCamera | Create movie output file failed", new Object[0]);
            return true;
        }
        return FileHelper.getAvailableStore(movieOutputPath.getParent()) > this.getMinAvailableSpaceBytes();
    }
    
    public void setMinAvailableSpaceBytes(final long i) {
        this.i = i;
    }
    
    public long getMinAvailableSpaceBytes() {
        return this.i;
    }
    
    @Override
    public boolean isSaveToAlbum() {
        return this.g;
    }
    
    @Override
    public void setSaveToAlbum(final boolean g) {
        if (this.isRecording()) {
            TLog.w("Could not set 'saveToAlbum' while recording.", new Object[0]);
            return;
        }
        this.g = g;
    }
    
    @Override
    public String getSaveToAlbumName() {
        return this.h;
    }
    
    @Override
    public void setSaveToAlbumName(final String h) {
        this.h = h;
    }
    
    public int getMinRecordingTime() {
        return Math.max(0, this.q);
    }
    
    public void setMinRecordingTime(final int b) {
        this.q = Math.max(0, b);
    }
    
    public int getMaxRecordingTime() {
        return Math.max(0, this.r);
    }
    
    public void setMaxRecordingTime(final int b) {
        this.r = Math.max(0, b);
    }
    
    public void setSpeedMode(final SpeedMode w) {
        if (w == null || this.w == w) {
            return;
        }
        if (this.isRecording()) {
            TLog.e("The rate of change is not allowed during recording.", new Object[0]);
            return;
        }
        this.w = w;
        this.getVideoEncoderSetting().enableAllKeyFrame = (w != SpeedMode.NORMAL);
        this.y.setSpeedRate(w.a);
        this.x.setSpeedRate(w.a);
    }
    
    public float getMovieDuration() {
        return this.c() - this.d() + this.b();
    }
    
    private float b() {
        if (this.s == 0L) {
            return 0.0f;
        }
        return (this.t - this.s) / 1000000.0f;
    }
    
    private float c() {
        float n = 0.0f;
        final Iterator<TuSdkTimeRange> iterator = this.d.iterator();
        while (iterator.hasNext()) {
            n += iterator.next().duration();
        }
        return n;
    }
    
    private float d() {
        float n = 0.0f;
        final Iterator<TuSdkTimeRange> iterator = this.e.iterator();
        while (iterator.hasNext()) {
            n += iterator.next().duration();
        }
        return n;
    }
    
    private float e() {
        return this.c() + this.b();
    }
    
    @Override
    protected void initCamera() {
        super.initCamera();
        this.setEnableFaceTrace(true);
    }
    
    @Override
    public void initOutputSettings() {
        super.initOutputSettings();
        this.setFocusTouchView(new TuVideoFocusTouchView(this.getContext()));
        this.setEnableAudioCapture(true);
        this.updateOutputFilterSettings();
        this.setVideoDataDelegate(this.A);
        this.setAudioDataDelegate(this.z);
    }
    
    @Override
    protected void updateOutputFilterSettings() {
        final boolean enableHorizontallyFlip = this.isDisableMirrorFrontFacing() && this.isFrontFacingCameraPresent() && this.isHorizontallyMirrorFrontFacingCamera();
        final SelesSurfaceEncoderInterface selesSurfaceEncoderInterface = (SelesSurfaceEncoderInterface)this.getVideoDataEncoder();
        if (selesSurfaceEncoderInterface != null) {
            selesSurfaceEncoderInterface.setEnableHorizontallyFlip(enableHorizontallyFlip);
        }
    }
    
    @Override
    protected void setState(final TuSdkStillCameraAdapter.CameraState state) {
        super.setState(state);
        if (state == TuSdkStillCameraAdapter.CameraState.StateStarted) {
            StatisticsManger.appendComponent((this.c == RecordMode.Keep) ? 9441280L : 9437186L);
        }
        else if (state == TuSdkStillCameraAdapter.CameraState.StateUnknow) {
            StatisticsManger.appendComponent((this.c == RecordMode.Keep) ? 9441281L : 9437187L);
        }
    }
    
    private Boolean f() {
        if (!SdkValid.shared.videoRecordEnabled()) {
            TLog.e("The video record feature has been expired, please contact us via http://tusdk.com", new Object[0]);
            return false;
        }
        return true;
    }
    
    private void g() {
        if (this.mMuxer == null && this.getMovieOutputPath() != null) {
            try {
                (this.mMuxer = new MediaMuxer(this.getMovieOutputPath().toString(), 0)).setOrientationHint(this.getDeviceOrient().getDegree());
            }
            catch (IOException ex) {
                TLog.e("Error on get mediaMuxer: %s", new Object[] { ex });
            }
        }
    }
    
    protected MediaMuxer getMediaMuxer() {
        return this.mMuxer;
    }
    
    private void h() {
        if (!this.validateMovieFile()) {
            this.mMuxer = null;
        }
        if (this.mMuxer == null) {
            return;
        }
        try {
            this.j = false;
            this.mMuxer.stop();
            this.mMuxer.release();
            this.l = -1;
            this.k = -1;
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
        this.mMuxer = null;
    }
    
    private void i() {
        if (this.getRecordMode() == RecordMode.Keep && this.f != null && this.s != 0L) {
            if (this.t != 0L) {
                if (this.d.isEmpty()) {
                    this.d.add(TuSdkTimeRange.makeRange(0.0f, this.e()));
                }
                else {
                    final TuSdkTimeRange range = TuSdkTimeRange.makeRange(this.d.get(this.d.size() - 1).getEndTime(), this.e());
                    if (range.duration() > 0.0f) {
                        this.d.add(range);
                    }
                }
            }
        }
        this.s = 0L;
        this.t = 0L;
    }
    
    private void j() {
        if (this.e.isEmpty()) {
            this.notifyRecordingResultWithVideoFile(this.f);
            return;
        }
        this.getContext().sendBroadcast(new Intent("android.intent.action.MEDIA_SCANNER_SCAN_FILE", Uri.fromFile(this.f)));
        final File v = this.v();
        final TuSDKMovieClipper.TuSDKMovieClipperOption tuSDKMovieClipperOption = new TuSDKMovieClipper.TuSDKMovieClipperOption();
        tuSDKMovieClipperOption.savePath = v.getPath();
        tuSDKMovieClipperOption.srcUri = Uri.parse(this.f.getPath());
        tuSDKMovieClipperOption.listener = this.a;
        tuSDKMovieClipperOption.fps = this.getVideoEncoderSetting().videoQuality.getFps();
        this.o = new TuSDKMovieClipper(tuSDKMovieClipperOption);
        final ArrayList<TuSDKMovieClipper.TuSDKMovieSegment> list = new ArrayList<TuSDKMovieClipper.TuSDKMovieSegment>(this.e.size());
        for (final TuSdkTimeRange tuSdkTimeRange : this.e) {
            list.add(this.o.createSegment(tuSdkTimeRange.getStartTimeUS(), tuSdkTimeRange.getEndTimeUS()));
        }
        this.o.removeSegments(list);
    }
    
    private void k() {
        this.i();
        this.notifyRecordingState(RecordState.Paused);
    }
    
    private void l() {
        if (!this.validateMinRecordingTime()) {
            TLog.e("Recording time is less than %d seconds", new Object[] { this.getMinRecordingTime() });
            this.a(RecordError.LessMinRecordingTime);
            return;
        }
        if (!this.validateMovieFile()) {
            TLog.e("Invalid recording time  : %f seconds", new Object[] { this.getMovieDuration() });
            this.a(RecordError.InvalidRecordingTime);
            this.u();
            return;
        }
        if (this.getRecordMode() == RecordMode.Keep) {
            StatisticsManger.appendComponent(9441282L);
            this.j();
        }
        else {
            this.notifyRecordingResultWithVideoFile(this.getMovieOutputPath());
        }
        this.u();
    }
    
    protected void notifyRecordingState(final RecordState b) {
        if (this.getVideoDelegate() == null) {
            return;
        }
        this.b = b;
        ThreadHelper.post((Runnable)new Runnable() {
            @Override
            public void run() {
                TuSDKRecordVideoCamera.this.getVideoDelegate().onMovieRecordStateChanged(b);
            }
        });
    }
    
    private void a(final RecordError recordError) {
        if (this.getVideoDelegate() == null) {
            return;
        }
        ThreadHelper.post((Runnable)new Runnable() {
            @Override
            public void run() {
                TuSDKRecordVideoCamera.this.getVideoDelegate().onMovieRecordFailed(recordError);
            }
        });
    }
    
    protected void notifyRecordingResultWithVideoFile(final File videoPath) {
        final TuSDKVideoResult tuSDKVideoResult = new TuSDKVideoResult();
        tuSDKVideoResult.videoPath = videoPath;
        if (this.isSaveToAlbum()) {
            tuSDKVideoResult.videoSqlInfo = ImageSqlHelper.saveMp4ToAlbum(this.getContext(), videoPath);
            ImageSqlHelper.notifyRefreshAblum(this.getContext(), tuSDKVideoResult.videoSqlInfo);
        }
        this.notifyResult(tuSDKVideoResult);
    }
    
    protected void notifyResult(final TuSDKVideoResult tuSDKVideoResult) {
        this.t();
        this.f = null;
        if (this.getVideoDelegate() == null) {
            return;
        }
        ThreadHelper.post((Runnable)new Runnable() {
            @Override
            public void run() {
                if (tuSDKVideoResult != null) {
                    TuSDKRecordVideoCamera.this.getVideoDelegate().onMovieRecordComplete(tuSDKVideoResult);
                }
                else {
                    TuSDKRecordVideoCamera.this.getVideoDelegate().onMovieRecordFailed(RecordError.Unknow);
                }
            }
        });
    }
    
    private void m() {
        if (this.b == RecordState.RecordCompleted) {
            return;
        }
        final float movieDuration = this.getMovieDuration();
        if (this.getVideoDelegate() == null || this.getMaxRecordingTime() == 0) {
            return;
        }
        final float min = Math.min(movieDuration, (float)this.getMaxRecordingTime());
        ThreadHelper.post((Runnable)new Runnable() {
            final /* synthetic */ float a = min / TuSDKRecordVideoCamera.this.getMaxRecordingTime();
            
            @Override
            public void run() {
                TuSDKRecordVideoCamera.this.getVideoDelegate().onMovieRecordProgressChanged(this.a, min);
            }
        });
        if (movieDuration > this.getMaxRecordingTime()) {
            final float n = (float)this.getMaxRecordingTime();
            if (this.getRecordMode() == RecordMode.Keep) {
                this.o();
                this.notifyRecordingState(RecordState.RecordCompleted);
            }
            else {
                this.p();
            }
        }
    }
    
    private boolean a(final MediaCodec.BufferInfo bufferInfo) {
        return this.s != 0L;
    }
    
    private boolean b(final MediaCodec.BufferInfo bufferInfo) {
        if (this.t > bufferInfo.presentationTimeUs) {
            return false;
        }
        if ((bufferInfo.flags & 0x1) != 0x0 && !this.u) {
            this.u = true;
        }
        if (!this.u) {
            return false;
        }
        if (this.s == 0L) {
            this.s = bufferInfo.presentationTimeUs;
        }
        this.t = bufferInfo.presentationTimeUs;
        TLog.e("mVideoFragmentLastTimeUS : %s", new Object[] { bufferInfo.presentationTimeUs });
        return true;
    }
    
    protected boolean validateMovieFile() {
        if (this.isCanCaptureAudio()) {
            return this.v && this.u && this.f != null;
        }
        return this.u && this.f != null;
    }
    
    protected boolean validateMinRecordingTime() {
        return this.getMovieDuration() >= this.getMinRecordingTime();
    }
    
    protected boolean validateMaxRecordingTime() {
        return this.getMaxRecordingTime() != 0 && this.getMovieDuration() >= this.getMaxRecordingTime();
    }
    
    @Override
    public void stopCameraCapture() {
        super.stopCameraCapture();
        if (this.isCameraFacingChangeing()) {
            return;
        }
        this.h();
        this.stopAudioRecording();
        this.stopVideoDataEncoder();
        this.u();
    }
    
    @Override
    public void startRecording() {
        if (!this.a()) {
            TLog.e("TuSDKRecordVideoCamera | There is no space available for your device \uff08Min %dM is required\uff09", new Object[] { this.getMinAvailableSpaceBytes() / 1024L / 1024L });
            this.a(RecordError.NotEnoughSpace);
            return;
        }
        this.n.removeCallbacksAndMessages((Object)null);
        this.n.sendEmptyMessage(1);
    }
    
    private void n() {
        if (this.isRecording()) {
            return;
        }
        if (this.validateMaxRecordingTime()) {
            this.a(RecordError.MoreMaxDuration);
            return;
        }
        this.y.prepare();
        this.x.prepare();
        this.s = 0L;
        this.t = 0L;
        this.g();
        super.startRecording();
        this.notifyRecordingState(RecordState.Recording);
    }
    
    @Override
    public void stopRecording() {
        this.n.removeCallbacksAndMessages((Object)null);
        this.n.sendEmptyMessage(2);
    }
    
    public void _stopRecording() {
        if (!this.isRecording() || this.isRecordingPaused()) {
            return;
        }
        this.p();
    }
    
    public void pauseRecording() {
        this.n.removeMessages(3);
        this.n.sendEmptyMessageDelayed(3, 200L);
    }
    
    private void o() {
        if (!this.isRecording() || this.isRecordingPaused()) {
            return;
        }
        if (this.getRecordMode() == RecordMode.Normal) {
            this._stopRecording();
            return;
        }
        if (this.o != null) {
            this.o.cancel();
        }
        this.pauseEncoder();
        this.k();
    }
    
    public void finishRecording() {
        this.n.removeMessages(5);
        this.n.sendEmptyMessage(5);
    }
    
    private void p() {
        if (!this.validateMinRecordingTime()) {
            this.a(RecordError.LessMinRecordingTime);
            return;
        }
        this.h();
        if (!this.isRecording() && !this.isRecordingPaused()) {
            this.l();
            return;
        }
        super.stopRecording();
        this.h();
        this.l();
    }
    
    @Override
    public void pauseCameraCapture() {
        super.stopRecording();
        super.pauseCameraCapture();
    }
    
    public void cancelRecording() {
        this.n.removeCallbacksAndMessages((Object)null);
        this.n.sendEmptyMessage(4);
    }
    
    private void q() {
        if (!this.isRecording()) {
            return;
        }
        this.r();
        this.notifyRecordingState(RecordState.Canceled);
    }
    
    public int getRecordingFragmentSize() {
        if (this.getRecordMode() == RecordMode.Normal) {
            return 0;
        }
        return this.d.size();
    }
    
    public TuSdkTimeRange popVideoFragment() {
        if (this.getRecordingFragmentSize() == 0) {
            return null;
        }
        final TuSdkTimeRange lastVideoFragmentRange = this.lastVideoFragmentRange();
        if (lastVideoFragmentRange != null && lastVideoFragmentRange.isValid()) {
            this.e.add(lastVideoFragmentRange);
        }
        StatisticsManger.appendComponent(9441296L);
        return lastVideoFragmentRange;
    }
    
    public void clearAllFragments() {
        if (this.getRecordMode() == RecordMode.Normal || this.getRecordingFragmentSize() == 0) {
            return;
        }
        this.n.removeCallbacksAndMessages((Object)null);
        this.n.sendEmptyMessage(7);
    }
    
    private void r() {
        super.stopRecording();
        this.h();
        this.u();
        this.s();
        this.t();
    }
    
    public TuSdkTimeRange lastVideoFragmentRange() {
        if (this.getRecordingFragmentSize() == 0) {
            return null;
        }
        final ArrayList<TuSdkTimeRange> list = new ArrayList<TuSdkTimeRange>(this.d);
        list.removeAll(this.e);
        if (!list.isEmpty()) {
            return (TuSdkTimeRange)list.get(list.size() - 1);
        }
        return null;
    }
    
    private void s() {
        if (this.getRecordMode() == RecordMode.Keep && this.f != null && this.f.exists()) {
            this.f.delete();
            this.getContext().sendBroadcast(new Intent("android.intent.action.MEDIA_SCANNER_SCAN_FILE", Uri.fromFile(this.f)));
        }
        this.f = null;
    }
    
    private void t() {
        this.d.clear();
        this.e.clear();
        this.s = 0L;
        this.t = 0L;
    }
    
    private void u() {
        this.j = false;
        this.l = -1;
        this.k = -1;
        this.u = false;
        this.v = false;
        this.t();
        this.y.reset();
        this.x.reset();
    }
    
    @Override
    public File getMovieOutputPath() {
        if (this.f == null) {
            this.f = this.v();
        }
        return this.f;
    }
    
    private File v() {
        if (this.isSaveToAlbum()) {
            return this.w();
        }
        return new File(TuSdk.getAppTempPath(), String.format("lsq_%s.mp4", StringHelper.timeStampString()));
    }
    
    private File w() {
        if (StringHelper.isNotBlank(this.getSaveToAlbumName())) {
            return AlbumHelper.getAlbumVideoFile(this.getSaveToAlbumName());
        }
        return AlbumHelper.getAlbumVideoFile();
    }
    
    @Override
    public void switchFilter(final String s) {
        if (s == null || this.isFilterChanging() || this.mFilterWrap.equalsCode(s)) {
            return;
        }
        if (!FilterManager.shared().isNormalFilter(s) && !this.f()) {
            return;
        }
        super.switchFilter(s);
    }
    
    private boolean x() {
        return !this.y() && this.isCanCaptureAudio();
    }
    
    private boolean y() {
        return this.l != -1;
    }
    
    private boolean z() {
        return this.k != -1;
    }
    
    @SuppressLint({ "InlinedApi" })
    private void a(final ByteDataFrame byteDataFrame) {
        if (this.isRecording() && !this.isRecordingPaused() && !this.validateMaxRecordingTime() && this.j) {
            if (byteDataFrame.trackIndex == this.l) {
                if (!this.a(byteDataFrame.bufferInfo)) {
                    TLog.e("Audio timeampUs error", new Object[0]);
                    return;
                }
                this.v = true;
            }
            if (byteDataFrame.trackIndex == this.k && !this.b(byteDataFrame.bufferInfo)) {
                TLog.e("Video timeampUs error", new Object[0]);
                return;
            }
            this.getMediaMuxer().writeSampleData(byteDataFrame.trackIndex, byteDataFrame.buffer, byteDataFrame.bufferInfo);
            this.m();
        }
    }
    
    private void b(final ByteDataFrame byteDataFrame) {
        TuSDKFrameSpeedRateController tuSDKFrameSpeedRateController = null;
        if (byteDataFrame.trackIndex == this.l) {
            tuSDKFrameSpeedRateController = this.x;
        }
        if (byteDataFrame.trackIndex == this.k) {
            tuSDKFrameSpeedRateController = this.y;
        }
        tuSDKFrameSpeedRateController.setFrameSpeedRateCallback(new TuSDKFrameSpeedRateController.FrameSpeedRateCallback() {
            @Override
            public void onAvailableFrameTimeUs(final long n) {
                TuSDKRecordVideoCamera.this.a(byteDataFrame);
            }
        });
        tuSDKFrameSpeedRateController.requestAdjustSpeed(byteDataFrame.bufferInfo);
    }
    
    private class RecordHandler extends Handler
    {
        public RecordHandler(final Looper looper) {
            super(looper);
        }
        
        public void handleMessage(final Message message) {
            switch (message.what) {
                case 1: {
                    TuSDKRecordVideoCamera.this.n();
                    break;
                }
                case 2: {
                    TuSDKRecordVideoCamera.this._stopRecording();
                    break;
                }
                case 3: {
                    TuSDKRecordVideoCamera.this.o();
                    break;
                }
                case 4: {
                    TuSDKRecordVideoCamera.this.q();
                    break;
                }
                case 5: {
                    TuSDKRecordVideoCamera.this.p();
                    break;
                }
                case 6: {
                    TuSDKRecordVideoCamera.this.b((ByteDataFrame)message.obj);
                    break;
                }
                case 7: {
                    TuSDKRecordVideoCamera.this.r();
                    break;
                }
            }
        }
    }
    
    public enum SpeedMode
    {
        NORMAL(1.0f), 
        FAST1(0.5f), 
        FAST2(0.3f), 
        Slow1(1.5f), 
        Slow2(2.0f);
        
        private float a;
        
        private SpeedMode(final float a) {
            this.a = a;
        }
        
        public float getSpeedRate() {
            return this.a;
        }
    }
    
    public enum RecordState
    {
        Recording, 
        Saving, 
        Paused, 
        RecordCompleted, 
        Canceled;
    }
    
    public enum RecordError
    {
        Unknow, 
        NotEnoughSpace, 
        InvalidRecordingTime, 
        LessMinRecordingTime, 
        MoreMaxDuration, 
        SaveFailed;
    }
    
    public enum RecordMode
    {
        Normal, 
        Keep;
    }
    
    public interface TuSDKRecordVideoCameraDelegate
    {
        void onMovieRecordComplete(final TuSDKVideoResult p0);
        
        void onMovieRecordProgressChanged(final float p0, final float p1);
        
        void onMovieRecordStateChanged(final RecordState p0);
        
        void onMovieRecordFailed(final RecordError p0);
    }
}
