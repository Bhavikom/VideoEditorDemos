// 
// Decompiled by Procyon v0.5.36
// 

package com.yasoka.screenrecord.sdk.video.lasque.tusdk.core.utils.hardware;

//import org.lasque.tusdk.core.detector.FrameDetectProcessor;
//import org.lasque.tusdk.core.utils.image.AlbumHelper;
//import org.lasque.tusdk.core.utils.StringHelper;
//import org.lasque.tusdk.core.TuSdk;
//import org.lasque.tusdk.core.media.codec.extend.TuSdkMediaTimeSlice;
import java.util.ArrayList;
//import org.lasque.tusdk.core.media.codec.extend.TuSdkMediaFormat;
import android.media.MediaFormat;
//import org.lasque.tusdk.core.seles.sources.SelesWatermarkImpl;
//import org.lasque.tusdk.video.editor.TuSdkMediaParticleEffectData;
//import org.lasque.tusdk.video.editor.TuSdkMediaSceneEffectData;
//import org.lasque.tusdk.core.struct.ViewSize;
//import org.lasque.tusdk.impl.view.widget.RegionDefaultHandler;
import android.graphics.PointF;
//import org.lasque.tusdk.core.media.codec.video.TuSdkVideoQuality;
//import org.lasque.tusdk.core.secret.SdkValid;
import java.text.DecimalFormat;
import android.view.ViewGroup;
import android.view.View;
import android.opengl.GLES20;
import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;
import android.opengl.GLSurfaceView;
//import org.lasque.tusdk.core.api.extend.TuSdkSurfaceRender;
//import org.lasque.tusdk.core.media.record.TuSdkRecordSurface;
import android.graphics.SurfaceTexture;
//import org.lasque.tusdk.core.seles.tusdk.FilterOption;
//import org.lasque.tusdk.core.seles.tusdk.combo.Face2DComboFilterWrap;
//import org.lasque.tusdk.core.seles.SelesParameters;
//import org.lasque.tusdk.core.sticker.LiveStickerPlayController;
//import org.lasque.tusdk.core.seles.SelesContext;
//import org.lasque.tusdk.video.editor.TuSdkMediaStickerEffectData;
//import org.lasque.tusdk.core.seles.sources.SelesOutInput;
//import org.lasque.tusdk.core.seles.sources.SelesPicture;
//import org.lasque.tusdk.core.media.camera.TuSdkCameraSize;
//import org.lasque.tusdk.core.media.camera.TuSdkCameraFocus;
//import org.lasque.tusdk.core.media.camera.TuSdkCameraOrientation;
//import org.lasque.tusdk.core.listener.TuSdkOrientationEventListener;
//import org.lasque.tusdk.core.media.camera.TuSdkCameraParameters;
//import org.lasque.tusdk.core.media.camera.TuSdkCameraBuilderImpl;
//import org.lasque.tusdk.core.media.camera.TuSdkCamera;
import android.annotation.TargetApi;
//import org.lasque.tusdk.core.face.TuSdkFaceDetector;
//import org.lasque.tusdk.core.face.FaceAligment;
//import org.lasque.tusdk.core.TuSdkResult;
import java.util.List;
//import org.lasque.tusdk.video.editor.TuSdkMediaEffectData;
//import org.lasque.tusdk.core.utils.image.BitmapHelper;
//import org.lasque.tusdk.core.seles.tusdk.FilterWrap;
//import org.lasque.tusdk.core.struct.TuSdkSize;
import java.nio.IntBuffer;
//import org.lasque.tusdk.core.media.suit.TuSdkMediaSuit;
//import org.lasque.tusdk.core.utils.FileHelper;
//import org.lasque.tusdk.core.utils.TLog;
//import org.lasque.tusdk.core.api.extend.TuSdkMediaProgress;
import android.graphics.RectF;
//import org.lasque.tusdk.core.utils.image.ImageOrientation;
//import org.lasque.tusdk.core.media.codec.extend.TuSdkMediaFileCuterTimeline;
//import org.lasque.tusdk.core.utils.sqllite.ImageSqlHelper;
//import org.lasque.tusdk.core.video.TuSDKVideoResult;
//import org.lasque.tusdk.core.media.codec.extend.TuSdkMediaTimeline;
//import org.lasque.tusdk.core.struct.TuSdkMediaDataSource;
//import org.lasque.tusdk.core.utils.ThreadHelper;
//import org.lasque.tusdk.core.secret.StatisticsManger;
//import org.lasque.tusdk.core.media.camera.TuSdkCameraShot;
//import org.lasque.tusdk.api.video.preproc.filter.TuSDKVideoProcesser;
//import org.lasque.tusdk.core.seles.filters.SelesFilter;
//import org.lasque.tusdk.core.api.engine.TuSdkEngineProcessor;
//import org.lasque.tusdk.api.engine.TuSdkFilterEngine;
//import org.lasque.tusdk.impl.components.camera.TuSdkVideoFocusTouchView;
//import org.lasque.tusdk.impl.view.widget.RegionHandler;
//import org.lasque.tusdk.core.media.record.TuSdkMediaRecordHub;
//import org.lasque.tusdk.video.editor.TuSdkTimeRange;
import java.util.LinkedList;
//import org.lasque.tusdk.core.media.camera.TuSdkCameraShotImpl;
//import org.lasque.tusdk.api.audio.preproc.processor.TuSdkAudioPitchEngine;
import java.io.File;
//import org.lasque.tusdk.core.media.camera.TuSdkCameraSizeImpl;
//import org.lasque.tusdk.core.media.camera.TuSdkCameraOrientationImpl;
//import org.lasque.tusdk.core.media.camera.TuSdkCameraFocusImpl;
//import org.lasque.tusdk.core.media.camera.TuSdkCameraParametersImpl;
//import org.lasque.tusdk.core.media.camera.TuSdkCameraBuilder;
//import org.lasque.tusdk.core.utils.TuSdkWaterMarkOption;
import android.graphics.Bitmap;
//import org.lasque.tusdk.core.seles.sources.SelesWatermark;
//import org.lasque.tusdk.core.media.record.TuSdkCameraRecorder;
//import org.lasque.tusdk.api.engine.TuSdkFilterEngineImpl;
//import org.lasque.tusdk.core.media.camera.TuSdkCameraImpl;
//import org.lasque.tusdk.core.seles.output.SelesSmartView;
import android.widget.RelativeLayout;
import android.content.Context;

import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.TuSdk;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.TuSdkResult;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.api.engine.TuSdkEngineProcessor;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.api.extend.TuSdkMediaProgress;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.api.extend.TuSdkSurfaceRender;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.face.FaceAligment;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.listener.TuSdkOrientationEventListener;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.media.camera.TuSdkCamera;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.media.camera.TuSdkCameraBuilder;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.media.camera.TuSdkCameraBuilderImpl;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.media.camera.TuSdkCameraFocus;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.media.camera.TuSdkCameraFocusImpl;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.media.camera.TuSdkCameraImpl;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.media.camera.TuSdkCameraOrientation;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.media.camera.TuSdkCameraOrientationImpl;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.media.camera.TuSdkCameraParameters;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.media.camera.TuSdkCameraParametersImpl;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.media.camera.TuSdkCameraShot;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.media.camera.TuSdkCameraShotImpl;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.media.camera.TuSdkCameraSize;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.media.camera.TuSdkCameraSizeImpl;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.media.codec.extend.TuSdkMediaFileCuterTimeline;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.media.codec.extend.TuSdkMediaFormat;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.media.codec.extend.TuSdkMediaTimeSlice;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.media.codec.extend.TuSdkMediaTimeline;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.media.codec.video.TuSdkVideoQuality;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.media.record.TuSdkCameraRecorder;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.media.record.TuSdkMediaRecordHub;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.media.record.TuSdkRecordSurface;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.media.suit.TuSdkMediaSuit;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.secret.SdkValid;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.secret.StatisticsManger;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.seles.SelesContext;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.seles.SelesParameters;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.seles.filters.SelesFilter;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.seles.output.SelesSmartView;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.seles.sources.SelesOutInput;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.seles.sources.SelesPicture;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.seles.sources.SelesWatermark;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.seles.sources.SelesWatermarkImpl;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.seles.tusdk.FilterOption;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.seles.tusdk.FilterWrap;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.seles.tusdk.combo.Face2DComboFilterWrap;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.sticker.LiveStickerPlayController;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.struct.TuSdkMediaDataSource;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.struct.TuSdkSize;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.struct.ViewSize;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.FileHelper;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.StringHelper;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.TLog;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.ThreadHelper;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.TuSdkWaterMarkOption;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.hardware.CameraConfigs;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.hardware.InterfaceOrientation;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.hardware.TuSdkStillCameraAdapter;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.image.AlbumHelper;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.image.BitmapHelper;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.image.ImageOrientation;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.sqllite.ImageSqlHelper;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.impl.view.widget.RegionDefaultHandler;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.impl.view.widget.RegionHandler;
import com.yasoka.screenrecord.sdk.video.lasque.tusdk.api.audio.preproc.processor.TuSdkAudioPitchEngine;
import com.yasoka.screenrecord.sdk.video.lasque.tusdk.api.engine.TuSdkFilterEngine;
import com.yasoka.screenrecord.sdk.video.lasque.tusdk.api.engine.TuSdkFilterEngineImpl;
import com.yasoka.screenrecord.sdk.video.lasque.tusdk.api.video.preproc.filter.TuSDKVideoProcesser;
import com.yasoka.screenrecord.sdk.video.lasque.tusdk.core.detector.FrameDetectProcessor;
import com.yasoka.screenrecord.sdk.video.lasque.tusdk.core.video.TuSDKVideoResult;
import com.yasoka.screenrecord.sdk.video.lasque.tusdk.impl.components.camera.TuSdkVideoFocusTouchView;
import com.yasoka.screenrecord.sdk.video.lasque.tusdk.video.editor.TuSdkMediaEffectData;
import com.yasoka.screenrecord.sdk.video.lasque.tusdk.video.editor.TuSdkMediaParticleEffectData;
import com.yasoka.screenrecord.sdk.video.lasque.tusdk.video.editor.TuSdkMediaSceneEffectData;
import com.yasoka.screenrecord.sdk.video.lasque.tusdk.video.editor.TuSdkMediaStickerEffectData;
import com.yasoka.screenrecord.sdk.video.lasque.tusdk.video.editor.TuSdkTimeRange;

import org.lasque.tusdk.core.face.TuSdkFaceDetector;

public final class TuSdkRecorderVideoCameraImpl implements TuSdkRecorderVideoCamera
{
    private Context a;
    private RelativeLayout b;
    private SelesSmartView c;
    private TuSdkRecorderCameraSetting d;
    private TuSdkCameraImpl e;
    private TuSdkFilterEngineImpl f;
    private TuSdkCameraRecorder g;
    private TuSdkRecorderVideoEncoderSetting h;
    private long i;
    private SelesWatermark j;
    private Bitmap k;
    private TuSdkWaterMarkOption.WaterMarkPosition l;
    private TuSdkCameraBuilder m;
    private TuSdkCameraParametersImpl n;
    private TuSdkCameraFocusImpl o;
    private TuSdkCameraOrientationImpl p;
    private TuSdkCameraSizeImpl q;
    private InterfaceOrientation r;
    private File s;
    private boolean t;
    private boolean u;
    private boolean v;
    private TuSdkAudioPitchEngine.TuSdkSoundPitchType w;
    private TuSdkCameraShotImpl x;
    private String y;
    private int z;
    private int A;
    private Object B;
    private LinkedList<TuSdkTimeRange> C;
    private LinkedList<TuSdkTimeRange> D;
    private long E;
    private long F;
    private TuSdkMediaRecordHub.TuSdkMediaRecordHubStatus G;
    private TuSdkTimeRange H;
    private long I;
    private TuSdkRecorderVideoCameraCallback J;
    private TuSdkCameraListener K;
    private TuSdkFaceDetectionCallback L;
    private TuSdkMediaEffectChangeListener M;
    private RegionHandler N;
    private TuSdkVideoFocusTouchView O;
    private float P;
    private TuSdkStillCameraAdapter.CameraState Q;
    private RecordState R;
    private boolean S;
    private long T;
    private int U;
    private boolean V;
    private TuSdkMediaRecordHub.TuSdkMediaRecordHubListener W;
    private TuSdkFilterEngine.TuSdkFilterEngineListener X;
    private TuSdkEngineProcessor.TuSdkVideoProcesserFaceDetectionDelegate Y;
    private SelesFilter.FrameProcessingDelegate Z;
    private TuSDKVideoProcesser.TuSDKVideoProcessorMediaEffectDelegate aa;
    private TuSdkCameraShot.TuSdkCameraShotListener ab;
    private TuSdkCameraShot.TuSdkCameraShotFaceFaceAligment ac;
    
    @Override
    public int getRegionViewColor() {
        return this.U;
    }
    
    @Override
    public void setRegionViewColor(final int u) {
        this.U = u;
        if (this.c != null) {
            this.c.setBackgroundColor(this.U);
        }
    }
    
    private void a(final RecordState r) {
        this.R = r;
        if (this.J != null) {
            this.J.onMovieRecordStateChanged(r);
        }
    }
    
    private void a(final TuSdkStillCameraAdapter.CameraState q) {
        this.Q = q;
        if (this.K != null) {
            this.K.onVideoCameraStateChanged(q);
        }
        if (q == TuSdkStillCameraAdapter.CameraState.StateStarted) {
            StatisticsManger.appendComponent(9441280L);
        }
        else if (q == TuSdkStillCameraAdapter.CameraState.StateUnknow) {
            StatisticsManger.appendComponent(9441281L);
        }
    }
    
    public TuSdkRecorderVideoCameraImpl(final Context a, final RelativeLayout b, final TuSdkRecorderCameraSetting d) {
        this.i = 52428800L;
        this.t = true;
        this.u = true;
        this.v = false;
        this.z = 2;
        this.A = 15;
        this.B = new Object();
        this.C = new LinkedList<TuSdkTimeRange>();
        this.D = new LinkedList<TuSdkTimeRange>();
        this.I = 0L;
        this.U = -16777216;
        this.V = false;
        this.W = (TuSdkMediaRecordHub.TuSdkMediaRecordHubListener)new TuSdkMediaRecordHub.TuSdkMediaRecordHubListener() {
            public void onStatusChanged(final TuSdkMediaRecordHub.TuSdkMediaRecordHubStatus tuSdkMediaRecordHubStatus, final TuSdkMediaRecordHub tuSdkMediaRecordHub) {
                TuSdkRecorderVideoCameraImpl.this.G = tuSdkMediaRecordHubStatus;
                TuSdkRecorderVideoCameraImpl.this.a(TuSdkRecorderVideoCameraImpl.this.T, tuSdkMediaRecordHubStatus);
                if (TuSdkRecorderVideoCameraImpl.this.J != null) {
                    ThreadHelper.post((Runnable)new Runnable() {
                        @Override
                        public void run() {
                            if (tuSdkMediaRecordHubStatus == TuSdkMediaRecordHub.TuSdkMediaRecordHubStatus.START_RECORD) {
                                TuSdkRecorderVideoCameraImpl.this.a(RecordState.Recording);
                            }
                            else if (tuSdkMediaRecordHubStatus == TuSdkMediaRecordHub.TuSdkMediaRecordHubStatus.PAUSE_RECORD) {
                                TuSdkRecorderVideoCameraImpl.this.a(RecordState.Paused);
                                if (TuSdkRecorderVideoCameraImpl.this.I >= TuSdkRecorderVideoCameraImpl.this.A * 1000000) {
                                    TuSdkRecorderVideoCameraImpl.this.a(RecordState.RecordCompleted);
                                }
                            }
                        }
                    });
                }
            }
            
            public void onProgress(final long n, final TuSdkMediaDataSource tuSdkMediaDataSource) {
                if (TuSdkRecorderVideoCameraImpl.this.G == TuSdkMediaRecordHub.TuSdkMediaRecordHubStatus.PAUSE_RECORD) {
                    return;
                }
                TuSdkRecorderVideoCameraImpl.this.T = n;
                TuSdkRecorderVideoCameraImpl.this.a(n);
            }
            
            public void onCompleted(final Exception ex, final TuSdkMediaDataSource tuSdkMediaDataSource, final TuSdkMediaTimeline tuSdkMediaTimeline) {
                ThreadHelper.post((Runnable)new Runnable() {
                    @Override
                    public void run() {
                        if (ex == null) {
                            if (TuSdkRecorderVideoCameraImpl.this.J != null) {
                                TuSdkRecorderVideoCameraImpl.this.a(RecordState.Saving);
                            }
                        }
                        else if (TuSdkRecorderVideoCameraImpl.this.J != null) {
                            TuSdkRecorderVideoCameraImpl.this.J.onMovieRecordFailed(RecordError.SaveFailed);
                        }
                    }
                });
                this.a(tuSdkMediaDataSource);
            }
            
            private void a(final TuSdkMediaDataSource tuSdkMediaDataSource) {
                if (TuSdkRecorderVideoCameraImpl.this.D.size() == 0 || TuSdkRecorderVideoCameraImpl.this.isDirectEdit()) {
                    final TuSDKVideoResult tuSDKVideoResult = new TuSDKVideoResult();
                    tuSDKVideoResult.videoPath = new File(tuSdkMediaDataSource.getPath());
                    if (TuSdkRecorderVideoCameraImpl.this.isSaveToAlbum()) {
                        tuSDKVideoResult.videoSqlInfo = ImageSqlHelper.saveMp4ToAlbum(TuSdkRecorderVideoCameraImpl.this.a, tuSDKVideoResult.videoPath);
                        ImageSqlHelper.notifyRefreshAblum(TuSdkRecorderVideoCameraImpl.this.a, tuSDKVideoResult.videoSqlInfo);
                    }
                    ThreadHelper.post((Runnable)new Runnable() {
                        @Override
                        public void run() {
                            if (TuSdkRecorderVideoCameraImpl.this.J != null) {
                                TuSdkRecorderVideoCameraImpl.this.J.onMovieRecordComplete(tuSDKVideoResult);
                                TuSdkRecorderVideoCameraImpl.this.h();
                                TuSdkRecorderVideoCameraImpl.this.a(RecordState.SaveCompleted);
                            }
                        }
                    });
                    return;
                }
                if (TuSdkRecorderVideoCameraImpl.this.D.size() > 0) {
                    final TuSdkMediaFileCuterTimeline tuSdkMediaFileCuterTimeline = new TuSdkMediaFileCuterTimeline();
                    String string = "";
                    long n = 0L;
                    for (final TuSdkTimeRange tuSdkTimeRange : TuSdkRecorderVideoCameraImpl.this.C) {
                        ((TuSdkMediaTimeline)tuSdkMediaFileCuterTimeline).append(tuSdkTimeRange.getStartTimeUS(), tuSdkTimeRange.getEndTimeUS(), 1.0f);
                        string = string + tuSdkTimeRange.getStartTimeUS() + " --- " + tuSdkTimeRange.getEndTimeUS() + "\n";
                        n += tuSdkTimeRange.getEndTimeUS() - tuSdkTimeRange.getStartTimeUS();
                    }
                    this.a(tuSdkMediaDataSource, (TuSdkMediaTimeline)tuSdkMediaFileCuterTimeline, new File(tuSdkMediaDataSource.getPath()));
                }
            }
            
            private void a(final TuSdkMediaDataSource tuSdkMediaDataSource, final TuSdkMediaTimeline tuSdkMediaTimeline, final File file) {
                TuSdkMediaSuit.cuter(tuSdkMediaDataSource, TuSdkRecorderVideoCameraImpl.this.getMovieOutputPath() + ".temp.mp4", TuSdkRecorderVideoCameraImpl.this.e(), TuSdkRecorderVideoCameraImpl.this.f(), ImageOrientation.Up, new RectF(0.0f, 0.0f, 1.0f, 1.0f), new RectF(0.0f, 0.0f, 1.0f, 1.0f), tuSdkMediaTimeline, (TuSdkMediaProgress)new TuSdkMediaProgress() {
                    public void onProgress(final float n, final TuSdkMediaDataSource tuSdkMediaDataSource, final int n2, final int n3) {
                    }
                    
                    public void onCompleted(final Exception ex, final TuSdkMediaDataSource tuSdkMediaDataSource, final int n) {
                        if (ex != null) {
                            TLog.e((Throwable)ex);
                            TuSdkRecorderVideoCameraImpl.this.h();
                            if (TuSdkRecorderVideoCameraImpl.this.J != null) {
                                TuSdkRecorderVideoCameraImpl.this.J.onMovieRecordFailed(RecordError.SaveFailed);
                            }
                            return;
                        }
                        final String absolutePath = file.getAbsolutePath();
                        FileHelper.delete(file);
                        FileHelper.rename(new File(tuSdkMediaDataSource.getPath()), new File(absolutePath));
                        final TuSDKVideoResult tuSDKVideoResult = new TuSDKVideoResult();
                        tuSDKVideoResult.videoPath = new File(absolutePath);
                        if (TuSdkRecorderVideoCameraImpl.this.isSaveToAlbum()) {
                            tuSDKVideoResult.videoSqlInfo = ImageSqlHelper.saveMp4ToAlbum(TuSdkRecorderVideoCameraImpl.this.a, new File(absolutePath));
                            ImageSqlHelper.notifyRefreshAblum(TuSdkRecorderVideoCameraImpl.this.a, tuSDKVideoResult.videoSqlInfo);
                        }
                        ThreadHelper.post((Runnable)new Runnable() {
                            @Override
                            public void run() {
                                if (TuSdkRecorderVideoCameraImpl.this.J != null) {
                                    TuSdkRecorderVideoCameraImpl.this.h();
                                    TuSdkRecorderVideoCameraImpl.this.a(RecordState.SaveCompleted);
                                    TuSdkRecorderVideoCameraImpl.this.J.onMovieRecordComplete(tuSDKVideoResult);
                                }
                            }
                        });
                    }
                });
            }
        };
        this.X = new TuSdkFilterEngine.TuSdkFilterEngineListener() {
            @Override
            public void onPictureDataCompleted(final IntBuffer intBuffer, final TuSdkSize tuSdkSize) {
            }
            
            @Override
            public void onPreviewScreenShot(final Bitmap bitmap) {
                ThreadHelper.post((Runnable)new Runnable() {
                    @Override
                    public void run() {
                        if (TuSdkRecorderVideoCameraImpl.this.K != null) {
                            TuSdkRecorderVideoCameraImpl.this.K.onVideoCameraScreenShot(bitmap);
                        }
                    }
                });
            }
            
            public void onFilterChanged(final FilterWrap filterWrap) {
                ThreadHelper.post((Runnable)new Runnable() {
                    @Override
                    public void run() {
                        if (TuSdkRecorderVideoCameraImpl.this.K != null) {
                            TuSdkRecorderVideoCameraImpl.this.K.onFilterChanged(filterWrap);
                        }
                    }
                });
            }
        };
        this.Y = (TuSdkEngineProcessor.TuSdkVideoProcesserFaceDetectionDelegate)new TuSdkEngineProcessor.TuSdkVideoProcesserFaceDetectionDelegate() {
            public void onFaceDetectionResult(final TuSdkEngineProcessor.TuSdkVideoProcesserFaceDetectionResultType tuSdkVideoProcesserFaceDetectionResultType, final int n) {
                if (TuSdkRecorderVideoCameraImpl.this.L != null) {
                    ThreadHelper.post((Runnable)new Runnable() {
                        final /* synthetic */ FaceDetectionResultType a = (tuSdkVideoProcesserFaceDetectionResultType == TuSdkEngineProcessor.TuSdkVideoProcesserFaceDetectionResultType.TuSDKVideoProcesserFaceDetectionResultTypeFaceDetected) ? FaceDetectionResultType.FaceDetected : FaceDetectionResultType.NoFaceDetected;
                        
                        @Override
                        public void run() {
                            TuSdkRecorderVideoCameraImpl.this.L.onFaceDetectionResult(this.a, n);
                        }
                    });
                }
            }
        };
        this.Z = (SelesFilter.FrameProcessingDelegate)new SelesFilter.FrameProcessingDelegate() {
            public void onFrameCompletion(final SelesFilter selesFilter, final long n) {
                final Bitmap imageFromCurrentlyProcessedOutput = selesFilter.imageFromCurrentlyProcessedOutput();
                selesFilter.setFrameProcessingDelegate((SelesFilter.FrameProcessingDelegate)null);
                final Bitmap imageCorp = BitmapHelper.imageCorp(imageFromCurrentlyProcessedOutput, TuSdkRecorderVideoCameraImpl.this.getRegionRatio());
                TuSdkRecorderVideoCameraImpl.this.a(TuSdkStillCameraAdapter.CameraState.StateCaptured);
                ThreadHelper.post((Runnable)new Runnable() {
                    @Override
                    public void run() {
                        TuSdkRecorderVideoCameraImpl.this.X.onPreviewScreenShot(imageCorp);
                    }
                });
            }
        };
        this.aa = new TuSDKVideoProcesser.TuSDKVideoProcessorMediaEffectDelegate() {
            @Override
            public void didApplyingMediaEffect(final TuSdkMediaEffectData tuSdkMediaEffectData) {
                if (TuSdkRecorderVideoCameraImpl.this.M != null) {
                    TuSdkRecorderVideoCameraImpl.this.M.didApplyingMediaEffect(tuSdkMediaEffectData);
                }
            }
            
            @Override
            public void didRemoveMediaEffect(final List<TuSdkMediaEffectData> list) {
                if (TuSdkRecorderVideoCameraImpl.this.M != null) {
                    TuSdkRecorderVideoCameraImpl.this.M.didRemoveMediaEffect(list);
                }
            }
        };
        this.ab = (TuSdkCameraShot.TuSdkCameraShotListener)new TuSdkCameraShot.TuSdkCameraShotListener() {
            public void onCameraWillShot(final TuSdkResult tuSdkResult) {
            }
            
            public void onCameraShotFailed(final TuSdkResult tuSdkResult) {
            }
            
            public void onCameraShotData(final TuSdkResult tuSdkResult) {
            }
            
            public void onCameraShotBitmap(final TuSdkResult tuSdkResult) {
                TuSdkRecorderVideoCameraImpl.this.a(TuSdkStillCameraAdapter.CameraState.StateCaptured);
                TuSdkRecorderVideoCameraImpl.this.X.onPreviewScreenShot(tuSdkResult.image);
            }
        };
        this.ac = new TuSdkCameraShot.TuSdkCameraShotFaceFaceAligment() {
            @TargetApi(12)
            public FaceAligment[] detectionImageFace(final Bitmap bitmap, final ImageOrientation imageOrientation) {
                TuSdkFaceDetector.init();
                //todo return TuSdkFaceDetector.markFace(BitmapHelper.imageResize(bitmap, TuSdkSize.create(700, 700), true, 0.0f, false, imageOrientation));
                return null;
            }
        };
        this.a = a;
        this.b = b;
        this.d = d;
        this.initCamera();
        this.initView();
    }
    
    protected void initCamera() {
        this.a(TuSdkStillCameraAdapter.CameraState.StateUnknow);
        (this.e = new TuSdkCameraImpl()).setCameraListener((TuSdkCamera.TuSdkCameraListener)new TuSdkCamera.TuSdkCameraListener() {
            public void onStatusChanged(final TuSdkCamera.TuSdkCameraStatus tuSdkCameraStatus, final TuSdkCamera tuSdkCamera) {
                if (TuSdkRecorderVideoCameraImpl.this.getFocusTouchView() != null) {
                    TuSdkRecorderVideoCameraImpl.this.getFocusTouchView().cameraStateChanged(TuSdkRecorderVideoCameraImpl.this.o.canSupportAutoFocus(), (TuSdkCamera)TuSdkRecorderVideoCameraImpl.this.e, tuSdkCameraStatus);
                }
            }
        });
        this.m = (TuSdkCameraBuilder)new TuSdkCameraBuilderImpl();
        this.e.setCameraBuilder(this.m);
        this.n = new TuSdkCameraParametersImpl();
        this.e.setCameraParameters((TuSdkCameraParameters)this.n);
        (this.p = new TuSdkCameraOrientationImpl()).setHorizontallyMirrorFrontFacingCamera(true);
        this.p.setDeviceOrientListener((TuSdkOrientationEventListener.TuSdkOrientationDelegate)new TuSdkOrientationEventListener.TuSdkOrientationDelegate() {
            public void onOrientationChanged(final InterfaceOrientation interfaceOrientation) {
                TuSdkRecorderVideoCameraImpl.this.r = interfaceOrientation;
            }
        }, (TuSdkOrientationEventListener.TuSdkOrientationDegreeDelegate)new TuSdkOrientationEventListener.TuSdkOrientationDegreeDelegate() {
            public void onOrientationDegreeChanged(final int n) {
            }
        });
        this.e.setCameraOrientation((TuSdkCameraOrientation)this.p);
        this.o = new TuSdkCameraFocusImpl();
        this.e.setCameraFocus((TuSdkCameraFocus)this.o);
        this.q = new TuSdkCameraSizeImpl();
        if (this.d.previewEffectScale != -1.0f) {
            this.q.setPreviewEffectScale(this.d.previewEffectScale);
        }
        if (this.d.previewMaxSize != -1) {
            this.q.setPreviewMaxSize(this.d.previewMaxSize);
        }
        if (this.d.previewRatio != -1.0f) {
            this.q.setPreviewRatio(this.d.previewRatio);
        }
        this.e.setCameraSize((TuSdkCameraSize)this.q);
        (this.x = new TuSdkCameraShotImpl()).setShotListener(this.ab);
        this.x.setDetectionImageFace(this.ac);
        this.x.setDetectionShotFilter((TuSdkCameraShot.TuSdkCameraShotFilter)new TuSdkCameraShot.TuSdkCameraShotFilter() {
            public SelesOutInput getFilters(final FaceAligment[] array, final SelesPicture selesPicture) {
                FilterWrap creat = null;
                Object o = null;
                for (final TuSdkMediaEffectData tuSdkMediaEffectData : TuSdkRecorderVideoCameraImpl.this.f.getAllMediaEffectData()) {
                    final TuSdkMediaEffectData clone = tuSdkMediaEffectData.clone();
                    if (clone != null) {
                        if (!clone.isVaild()) {
                            continue;
                        }
                        final FilterWrap filterWrap = clone.getFilterWrap();
                        if (clone instanceof TuSdkMediaStickerEffectData) {
                            selesPicture.mountAtGLThread((Runnable)new Runnable() {
                                @Override
                                public void run() {
                                    final LiveStickerPlayController liveStickerPlayController = new LiveStickerPlayController(SelesContext.currentEGLContext());
                                    liveStickerPlayController.showGroupSticker(((TuSdkMediaStickerEffectData)clone).getStickerGroup());
                                    if (!clone.isApplied()) {
                                        ((SelesParameters.FilterStickerInterface)clone.getFilterWrap()).setStickerVisibility(true);
                                        clone.setIsApplied(true);
                                    }
                                    ((SelesParameters.FilterStickerInterface)clone.getFilterWrap()).updateStickers(liveStickerPlayController.getStickers());
                                    final int[] map2DCurrentStickerIndexs = ((Face2DComboFilterWrap)tuSdkMediaEffectData.getFilterWrap()).getMap2DCurrentStickerIndexs();
                                    try {
                                        Thread.sleep(500L);
                                        ((Face2DComboFilterWrap)filterWrap).setMap2DCurrentStickerIndex(map2DCurrentStickerIndexs);
                                    }
                                    catch (InterruptedException ex) {
                                        ex.printStackTrace();
                                    }
                                }
                            });
                        }
                        filterWrap.processImage();
                        if (filterWrap instanceof SelesParameters.FilterFacePositionInterface) {
                            ((SelesParameters.FilterFacePositionInterface)filterWrap).updateFaceFeatures(array, 0.0f);
                        }
                        if (o == null && creat == null) {
                            creat = (FilterWrap)(o = filterWrap);
                        }
                        else {
                            ((FilterWrap)o).getLastFilter().addTarget((SelesContext.SelesInput)filterWrap.getFilter(), 0);
                            o = filterWrap;
                        }
                    }
                }
                if (creat == null) {
                    creat = FilterWrap.creat((FilterOption)null);
                }
                return creat.getFilter();
            }
        });
        this.e.setCameraShot((TuSdkCameraShot)this.x);
        this.e.setSurfaceListener((SurfaceTexture.OnFrameAvailableListener)new SurfaceTexture.OnFrameAvailableListener() {
            public void onFrameAvailable(final SurfaceTexture surfaceTexture) {
                TuSdkRecorderVideoCameraImpl.this.c.requestRender();
            }
        });
        this.n.setFlashMode(CameraConfigs.CameraFlash.On);
        this.e.prepare();
        (this.f = new TuSdkFilterEngineImpl(false, true)).setInputImageOrientation(ImageOrientation.Up);
        this.f.setOutputImageOrientation(ImageOrientation.Up);
        this.f.setCameraFacing(this.e.getFacing());
        this.f.setFilterChangedListener(this.X);
        this.f.setEnableLiveSticker(true);
        this.f.setFaceDetectionDelegate(this.Y);
        this.f.setMediaEffectDelegate(this.aa);
    }
    
    private void a() {
        (this.g = new TuSdkCameraRecorder()).appendRecordSurface((TuSdkRecordSurface)this.e);
        this.g.setRecordListener(this.W);
        this.g.setSurfaceRender((TuSdkSurfaceRender)new TuSdkSurfaceRender() {
            public void onSurfaceCreated() {
            }
            
            public void onSurfaceChanged(final int n, final int n2) {
            }
            
            public void onSurfaceDestory() {
            }
            
            public int onDrawFrame(final int n, final int n2, final int n3, final long n4) {
                return TuSdkRecorderVideoCameraImpl.this.f.processFrame(n, n2, n3, n4);
            }
            
            public void onDrawFrameCompleted() {
            }
        });
        this.g.addTarget((SelesContext.SelesInput)this.c, 0);
    }
    
    protected void initView() {
        (this.c = new SelesSmartView(this.a)).setRenderer((GLSurfaceView.Renderer)new GLSurfaceView.Renderer() {
            public void onSurfaceCreated(final GL10 gl10, final EGLConfig eglConfig) {
                GLES20.glDisable(2929);
                TuSdkRecorderVideoCameraImpl.this.g.initInGLThread();
                TuSdkRecorderVideoCameraImpl.this.f.onSurfaceCreated();
            }
            
            public void onSurfaceChanged(final GL10 gl10, final int n, final int n2) {
                GLES20.glViewport(0, 0, n, n2);
                TuSdkRecorderVideoCameraImpl.this.f.onSurfaceChanged(n, n2);
            }
            
            public void onDrawFrame(final GL10 gl10) {
                GLES20.glClear(16640);
                TuSdkRecorderVideoCameraImpl.this.g.newFrameReadyInGLThread();
            }
        });
        this.b.addView((View)this.c, 0, (ViewGroup.LayoutParams)new RelativeLayout.LayoutParams(-1, -1));
        this.a();
        this.setFocusTouchView(new TuSdkVideoFocusTouchView(this.a));
    }
    
    private void a(final long n, final TuSdkMediaRecordHub.TuSdkMediaRecordHubStatus tuSdkMediaRecordHubStatus) {
        if (tuSdkMediaRecordHubStatus == TuSdkMediaRecordHub.TuSdkMediaRecordHubStatus.START_RECORD) {
            this.E = n;
            if (this.H != null) {
                return;
            }
            (this.H = new TuSdkTimeRange()).setStartTimeUs(this.E);
        }
        else if (tuSdkMediaRecordHubStatus == TuSdkMediaRecordHub.TuSdkMediaRecordHubStatus.PAUSE_RECORD) {
            this.F = n;
            if (this.H == null) {
                return;
            }
            this.H.setEndTimeUs(this.F);
            synchronized (this.B) {
                this.C.add(this.H);
            }
            this.H = null;
        }
    }
    
    private void a(final long n) {
        this.I = n - this.c();
        if (this.b()) {
            return;
        }
        final float a = this.a(this.I / 1000000.0f);
        final float n2 = a / this.A;
        if (this.J != null) {
            this.J.onMovieRecordProgressChanged(n2, a);
        }
    }
    
    private boolean b() {
        if (this.I >= this.A * 1000000 && this.G == TuSdkMediaRecordHub.TuSdkMediaRecordHubStatus.START_RECORD) {
            this.pauseRecording();
            return true;
        }
        return false;
    }
    
    private float a(final float n) {
        return Float.valueOf(new DecimalFormat(".00").format(n));
    }
    
    private long c() {
        long n = 0L;
        for (final TuSdkTimeRange tuSdkTimeRange : this.D) {
            n += Math.abs(tuSdkTimeRange.getEndTimeUS() - tuSdkTimeRange.getStartTimeUS());
        }
        return n;
    }
    
    @Override
    public boolean isDirectEdit() {
        return this.V;
    }
    
    @Override
    public void enableDirectEdit(final boolean v) {
        this.V = v;
    }
    
    @Override
    public void setVideoEncoderSetting(final TuSdkRecorderVideoEncoderSetting h) {
        this.h = h;
        if (!SdkValid.shared.videoCameraBitrateEnabled() && h != null) {
            TLog.e("You are not allowed to change camera bitrate, please see http://tusdk.com", new Object[0]);
            this.h.videoQuality = TuSdkVideoQuality.safeQuality();
        }
    }
    
    @Override
    public TuSdkRecorderVideoEncoderSetting getVideoEncoderSetting() {
        if (this.h == null) {
            this.h = new TuSdkRecorderVideoEncoderSetting();
        }
        if (this.h.videoSize == null || !this.h.videoSize.isSize()) {
            this.h.videoSize = this.getOutputImageSize();
        }
        return this.h;
    }
    
    @Override
    public void setRecorderVideoCameraCallback(final TuSdkRecorderVideoCameraCallback j) {
        this.J = j;
    }
    
    @Override
    public void setCameraListener(final TuSdkCameraListener k) {
        this.K = k;
    }
    
    @Override
    public TuSdkStillCameraAdapter.CameraState getCameraState() {
        return null;
    }
    
    @Override
    public RecordState getRecordState() {
        return this.R;
    }
    
    @Override
    public void setFaceDetectionCallback(final TuSdkFaceDetectionCallback l) {
        this.L = l;
    }
    
    @Override
    public void setMediaEffectChangeListener(final TuSdkMediaEffectChangeListener m) {
        this.M = m;
    }
    
    @Override
    public void setEnableLiveSticker(final boolean enableLiveSticker) {
        if (this.f == null) {
            return;
        }
        this.f.setEnableLiveSticker(enableLiveSticker);
    }
    
    @Override
    public void setEnableFaceDetection(final boolean enableFaceDetection) {
        if (this.f == null) {
            return;
        }
        this.f.setEnableFaceDetection(enableFaceDetection);
    }
    
    @Override
    public void setWaterMarkImage(final Bitmap k) {
        this.k = k;
    }
    
    @Override
    public void setWaterMarkPosition(final TuSdkWaterMarkOption.WaterMarkPosition l) {
        this.l = l;
    }
    
    @Override
    public void setFlashMode(final CameraConfigs.CameraFlash flashMode) {
        this.n.setFlashMode(flashMode);
    }
    
    @Override
    public CameraConfigs.CameraFlash getFlashMode() {
        return this.n.getFlashMode();
    }
    
    @Override
    public boolean canSupportFlash() {
        return this.n.canSupportFlash();
    }
    
    @Override
    public boolean isFrontFacingCameraPresent() {
        return this.e.getFacing() == CameraConfigs.CameraFacing.Front;
    }
    
    @Override
    public boolean isBackFacingCameraPresent() {
        return this.e.getFacing() == CameraConfigs.CameraFacing.Back;
    }
    
    @Override
    public void setFocusMode(final CameraConfigs.CameraAutoFocus cameraAutoFocus, final PointF pointF) {
        this.o.setFocusMode(cameraAutoFocus, pointF);
    }
    
    @Override
    public boolean canSupportAutoFocus() {
        return this.o.canSupportAutoFocus();
    }
    
    @Override
    public void setAntibandingMode(final CameraConfigs.CameraAntibanding antibandingMode) {
        this.n.setAntibandingMode(antibandingMode);
    }
    
    @Override
    public void setDisableContinueFocus(final boolean b) {
        this.o.setDisableContinueFoucs(b);
        if (this.O != null) {
            this.O.setDisableContinueFoucs(b);
        }
    }
    
    @Override
    public void autoFocus(final CameraConfigs.CameraAutoFocus cameraAutoFocus, final PointF pointF, final TuSdkCameraFocus.TuSdkCameraFocusListener tuSdkCameraFocusListener) {
        this.o.autoFocus(cameraAutoFocus, pointF, tuSdkCameraFocusListener);
    }
    
    @Override
    public void autoFocus(final TuSdkCameraFocus.TuSdkCameraFocusListener tuSdkCameraFocusListener) {
        this.o.autoFocus(tuSdkCameraFocusListener);
    }
    
    @Override
    public TuSdkVideoFocusTouchView getFocusTouchView() {
        return this.O;
    }
    
    protected void setFocusTouchView(final TuSDKVideoCameraFocusViewInterface tuSDKVideoCameraFocusViewInterface) {
        this.O = (TuSdkVideoFocusTouchView)tuSDKVideoCameraFocusViewInterface;
        if (tuSDKVideoCameraFocusViewInterface == null || this.c == null) {
            return;
        }
        if (this.O != null) {
            this.c.removeView((View)this.O);
            this.O.viewWillDestory();
        }
        this.O.setCamera(this);
        this.O.setDisableFocusBeep(true);
        this.O.setDisableContinueFoucs(this.v);
        this.O.setGuideLineViewState(false);
        this.O.setRegionPercent(this.getRegionHandler().getRectPercent());
        this.c.addView((View)this.O, (ViewGroup.LayoutParams)new RelativeLayout.LayoutParams(-1, -1));
    }
    
    @Override
    public RegionHandler getRegionHandler() {
        if (this.N == null) {
            this.N = (RegionHandler)new RegionDefaultHandler();
        }
        this.N.setWrapSize((TuSdkSize) ViewSize.create((View)this.b));
        return this.N;
    }
    
    @Override
    public void changeRegionRatio(final float n) {
        if (!this.canChangeRatio()) {
            return;
        }
        this.P = n;
        this.e.setShotRegionRatio(n);
        this.getRegionHandler().changeWithRatio(this.P, (RegionHandler.RegionChangerListener)new RegionHandler.RegionChangerListener() {
            public void onRegionChanged(final RectF rectF) {
                if (TuSdkRecorderVideoCameraImpl.this.c != null) {
                    TuSdkRecorderVideoCameraImpl.this.c.setDisplayRect(rectF);
                }
                if (TuSdkRecorderVideoCameraImpl.this.getFocusTouchView() != null) {
                    TuSdkRecorderVideoCameraImpl.this.getFocusTouchView().setRegionPercent(rectF);
                }
                TuSdkRecorderVideoCameraImpl.this.d();
            }
        });
    }
    
    private void d() {
        if (this.f != null) {
            return;
        }
        this.f.setDisplayRect(null, this.getRegionRatio());
    }
    
    protected float getRegionRatio() {
        if (this.P == 0.0f) {
            return this.getCameraPreviewSize().getRatioFloat();
        }
        return this.P;
    }
    
    @Override
    public void setRegionRatio(final float p) {
        if (!this.canChangeRatio()) {
            return;
        }
        this.P = p;
        this.e.setShotRegionRatio(this.P);
        if (this.N != null) {
            this.N.setRatio(this.P);
        }
    }
    
    @Override
    public boolean canChangeRatio() {
        return this.G != TuSdkMediaRecordHub.TuSdkMediaRecordHubStatus.PAUSE_RECORD;
    }
    
    @Override
    public TuSdkSize getCameraPreviewSize() {
        return this.q.getOutputSize();
    }
    
    @Override
    public void rotateCamera() {
        this.e.rotateCamera();
        StatisticsManger.appendComponent(this.isFrontFacingCameraPresent() ? 9441314L : 9441313L);
    }
    
    @Override
    public void startCameraCapture() {
        this.a(TuSdkStillCameraAdapter.CameraState.StateStarting);
        this.e.startPreview(this.d.facing);
        this.a(TuSdkStillCameraAdapter.CameraState.StateStarted);
    }
    
    @Override
    public void pauseCameraCapture() {
        this.e.pausePreview();
    }
    
    @Override
    public void resumeCameraCapture() {
        this.e.resumePreview();
        this.a(TuSdkStillCameraAdapter.CameraState.StateStarted);
    }
    
    @Override
    public void stopCameraCapture() {
        this.e.stopPreview();
        this.a(TuSdkStillCameraAdapter.CameraState.StateUnknow);
    }
    
    @Override
    public InterfaceOrientation getDeviceOrient() {
        return this.r;
    }
    
    @Override
    public TuSdkSize getOutputImageSize() {
        return this.q.getOutputSize();
    }
    
    @Override
    public void setFaceDetectionDelegate(final TuSdkCameraFocus.TuSdkCameraFocusFaceListener faceListener) {
        this.o.setFaceListener(faceListener);
    }
    
    @Override
    public boolean addMediaEffectData(final TuSdkMediaEffectData tuSdkMediaEffectData) {
        if (tuSdkMediaEffectData instanceof TuSdkMediaSceneEffectData || tuSdkMediaEffectData instanceof TuSdkMediaParticleEffectData) {
            TLog.e("Invalid filter code , please contact us via http://tusdk.com", new Object[0]);
            return false;
        }
        return this.f.addMediaEffectData(tuSdkMediaEffectData);
    }
    
    @Override
    public boolean removeMediaEffectData(final TuSdkMediaEffectData tuSdkMediaEffectData) {
        return this.f.removeMediaEffectData(tuSdkMediaEffectData);
    }
    
    @Override
    public <T extends TuSdkMediaEffectData> List<T> mediaEffectsWithType(final TuSdkMediaEffectData.TuSdkMediaEffectDataType tuSdkMediaEffectDataType) {
        return this.f.mediaEffectsWithType(tuSdkMediaEffectDataType);
    }
    
    @Override
    public List<TuSdkMediaEffectData> getAllMediaEffectData() {
        return this.f.getAllMediaEffectData();
    }
    
    @Override
    public void removeMediaEffectsWithType(final TuSdkMediaEffectData.TuSdkMediaEffectDataType tuSdkMediaEffectDataType) {
        this.f.removeMediaEffectsWithType(tuSdkMediaEffectDataType);
    }
    
    @Override
    public void removeAllMediaEffects() {
        this.f.removeAllMediaEffects();
    }
    
    @Override
    public void setEnableAudioCapture(final boolean u) {
        this.u = u;
    }
    
    @Override
    public boolean isEnableAudioCapture() {
        return this.u;
    }
    
    @Override
    public void setSoundPitchType(final TuSdkAudioPitchEngine.TuSdkSoundPitchType w) {
        this.w = w;
        this.g.changePitch(w.getPitch());
    }
    
    @Override
    public void captureImage() {
        this.a(TuSdkStillCameraAdapter.CameraState.StateCapturing);
        this.e.shotPhoto();
    }
    
    @Override
    public void startRecording() {
        if (!this.g()) {
            TLog.e("TuSdkRecorderVideoCamera | There is no space available for your device \uff08Min %dM is required\uff09", new Object[] { this.getMinAvailableSpaceBytes() / 1024L / 1024L });
            if (this.J != null) {
                this.J.onMovieRecordFailed(RecordError.NotEnoughSpace);
            }
            return;
        }
        if (this.j == null && this.k != null) {
            this.j = (SelesWatermark)new SelesWatermarkImpl(true);
        }
        if (this.j != null && this.k != null) {
            this.j.setImage(this.k, false);
            this.j.setWaterPostion((this.l == null) ? TuSdkWaterMarkOption.WaterMarkPosition.TopRight : this.l);
            this.j.setScale(0.09f);
            this.g.setWatermark(this.j);
        }
        if (this.S) {
            this.resumeRecording();
            return;
        }
        this.S = true;
        this.g.setOutputVideoFormat(this.e());
        if (this.isEnableAudioCapture()) {
            this.g.setOutputAudioFormat(this.f());
        }
        this.g.start(this.getMovieOutputPath());
    }
    
    private MediaFormat e() {
        return TuSdkMediaFormat.buildSafeVideoEncodecFormat(this.getVideoEncoderSetting().videoSize, this.getVideoEncoderSetting().videoQuality, 2130708361, (this.getVideoEncoderSetting().enableAllKeyFrame || this.isDirectEdit()) ? 0 : this.getVideoEncoderSetting().mediacodecAVCIFrameInterval);
    }
    
    private MediaFormat f() {
        return TuSdkMediaFormat.buildSafeAudioEncodecFormat();
    }
    
    @Override
    public void resumeRecording() {
        if (this.I > this.getMaxRecordingTime() * 1000000) {
            TLog.w("Record more max duration , current : %s   max : %s ", new Object[] { this.I, this.getMaxRecordingTime() });
            this.J.onMovieRecordFailed(RecordError.MoreMaxDuration);
            return;
        }
        this.g.resume();
    }
    
    @Override
    public void pauseRecording() {
        this.g.pause();
    }
    
    @Override
    public void stopRecording() {
        if (this.I < this.getMinRecordingTime() * 1000000) {
            TLog.w("Record less min recording time , current : %s   min : %s ", new Object[] { this.I, this.getMinRecordingTime() });
            this.J.onMovieRecordFailed(RecordError.LessMinRecordingTime);
            return;
        }
        this.g.stop();
    }
    
    @Override
    public boolean isRecording() {
        return this.g.getState() == TuSdkMediaRecordHub.TuSdkMediaRecordHubStatus.START || this.g.getState() == TuSdkMediaRecordHub.TuSdkMediaRecordHubStatus.START_RECORD;
    }
    
    private boolean g() {
        if (this.a == null || !FileHelper.mountedExternalStorage()) {
            return false;
        }
        final File movieOutputPath = this.getMovieOutputPath();
        if (movieOutputPath == null) {
            TLog.e("TuSDKRecordVideoCamera | Create movie output file failed", new Object[0]);
            return true;
        }
        return FileHelper.getAvailableStore(movieOutputPath.getParent()) > this.getMinAvailableSpaceBytes();
    }
    
    @Override
    public void setMinAvailableSpaceBytes(final long i) {
        this.i = i;
    }
    
    @Override
    public long getMinAvailableSpaceBytes() {
        return this.i;
    }
    
    @Override
    public void setSaveToAlbum(final boolean t) {
        this.t = t;
    }
    
    @Override
    public boolean isSaveToAlbum() {
        return this.t;
    }
    
    @Override
    public void setSaveToAlbumName(final String y) {
        this.y = y;
    }
    
    @Override
    public String getSaveToAlbumName() {
        return this.y;
    }
    
    @Override
    public void setMinRecordingTime(final int z) {
        this.z = z;
    }
    
    @Override
    public int getMinRecordingTime() {
        return this.z;
    }
    
    @Override
    public void setMaxRecordingTime(final int a) {
        this.A = a;
    }
    
    @Override
    public int getMaxRecordingTime() {
        return this.A;
    }
    
    @Override
    public void setSpeedMode(final SpeedMode speedMode) {
        this.g.changeSpeed(speedMode.getSpeedRate());
    }
    
    @Override
    public float getMovieDuration() {
        return this.a(this.I / 1000000.0f);
    }
    
    @Override
    public int getRecordingFragmentSize() {
        synchronized (this.B) {
            return this.C.size();
        }
    }
    
    public ArrayList<TuSdkMediaTimeSlice> getRecordTimeSlice() {
        final ArrayList<TuSdkMediaTimeSlice> list = new ArrayList<TuSdkMediaTimeSlice>();
        for (final TuSdkTimeRange tuSdkTimeRange : this.C) {
            list.add(new TuSdkMediaTimeSlice(tuSdkTimeRange.getStartTimeUS(), tuSdkTimeRange.getEndTimeUS()));
        }
        return list;
    }
    
    @Override
    public TuSdkTimeRange popVideoFragment() {
        synchronized (this.B) {
            if (this.C.size() == 0) {
                return null;
            }
            final TuSdkTimeRange e = this.C.removeLast();
            this.D.add(e);
            this.I -= Math.abs(e.getEndTimeUS() - e.getStartTimeUS());
            StatisticsManger.appendComponent(9441296L);
            return e;
        }
    }
    
    @Override
    public void cancelRecording() {
        synchronized (this.B) {
            this.C.clear();
            this.D.clear();
            this.h();
            this.a(RecordState.Canceled);
        }
    }
    
    @Override
    public TuSdkTimeRange lastVideoFragmentRange() {
        if (this.C.size() == 0) {
            return null;
        }
        return this.C.getLast();
    }
    
    @Override
    public File getMovieOutputPath() {
        if (this.s == null) {
            this.s = this.i();
        }
        return this.s;
    }
    
    private void h() {
        this.s = null;
        this.C.clear();
        this.D.clear();
        this.g.reset();
        this.I = 0L;
        this.S = false;
        this.T = 0L;
        this.E = 0L;
        this.F = 0L;
    }
    
    @Override
    public void destroy() {
        this.e.release();
        this.J = null;
        this.a = null;
        this.b = null;
        this.C.clear();
        this.D.clear();
        this.f.release();
    }
    
    private File i() {
        if (this.isSaveToAlbum()) {
            return this.j();
        }
        return new File(TuSdk.getAppTempPath(), String.format("lsq_%s.mp4", StringHelper.timeStampString()));
    }
    
    private File j() {
        if (StringHelper.isNotBlank(this.getSaveToAlbumName())) {
            return AlbumHelper.getAlbumVideoFile(this.getSaveToAlbumName());
        }
        return AlbumHelper.getAlbumVideoFile();
    }
    
    public void setDetectScale(final float detectScale) {
        FrameDetectProcessor.setDetectScale(detectScale);
    }
}
