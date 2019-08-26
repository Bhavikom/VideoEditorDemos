// 
// Decompiled by Procyon v0.5.36
// 

package com.yasoka.screenrecord.sdk.video.lasque.tusdk.core.seles.sources;

//import org.lasque.tusdk.core.decoder.TuSDKVideoInfo;
//import org.lasque.tusdk.core.encoder.video.TuSDKVideoEncoderSetting;
//import org.lasque.tusdk.core.media.codec.extend.TuSdkMediaFormat;
//import org.lasque.tusdk.core.utils.image.ImageOrientation;
//import org.lasque.tusdk.core.common.TuSDKMediaUtils;
import android.media.MediaFormat;
//import org.lasque.tusdk.core.utils.image.AlbumHelper;
//import org.lasque.tusdk.core.utils.StringHelper;
//import org.lasque.tusdk.core.api.extend.TuSdkAudioRender;
//import org.lasque.tusdk.core.media.codec.sync.TuSdkMediaFileSync;
import android.graphics.Color;
//import org.lasque.tusdk.core.utils.TLog;
//import org.lasque.tusdk.core.struct.TuSdkSize;
//import org.lasque.tusdk.video.editor.TuSdkMediaAudioEffectData;
//import org.lasque.tusdk.core.secret.StatisticsManger;
//import org.lasque.tusdk.video.editor.TuSdkMediaStickerAudioEffectData;
//import org.lasque.tusdk.api.engine.TuSdkFilterEngineImpl;
//import org.lasque.tusdk.core.media.codec.extend.TuSdkMediaTimeSliceEntity;
//import org.lasque.tusdk.core.utils.sqllite.ImageSqlHelper;
//import org.lasque.tusdk.core.TuSdk;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.TuSdk;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.api.extend.TuSdkAudioRender;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.api.extend.TuSdkMediaProgress;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.api.extend.TuSdkSurfaceRender;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.media.codec.extend.TuSdkMediaFormat;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.media.codec.extend.TuSdkMediaTimeSliceEntity;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.media.codec.extend.TuSdkMediaTimeline;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.media.codec.suit.TuSdkMediaFileCuterImpl;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.media.codec.sync.TuSdkMediaFileDirectorSync;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.media.codec.sync.TuSdkMediaFileSync;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.secret.StatisticsManger;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.seles.sources.SelesWatermark;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.seles.sources.SelesWatermarkImpl;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.struct.TuSdkMediaDataSource;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.struct.TuSdkSize;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.StringHelper;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.TLog;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.image.AlbumHelper;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.image.ImageOrientation;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.sqllite.ImageSqlHelper;
import com.yasoka.screenrecord.sdk.video.lasque.tusdk.api.engine.TuSdkFilterEngine;
import com.yasoka.screenrecord.sdk.video.lasque.tusdk.api.engine.TuSdkFilterEngineImpl;
import com.yasoka.screenrecord.sdk.video.lasque.tusdk.core.common.TuSDKMediaUtils;
import com.yasoka.screenrecord.sdk.video.lasque.tusdk.core.decoder.TuSDKVideoInfo;
import com.yasoka.screenrecord.sdk.video.lasque.tusdk.core.encoder.video.TuSDKVideoEncoderSetting;
import com.yasoka.screenrecord.sdk.video.lasque.tusdk.video.editor.TuSdkMediaAudioEffectData;
import com.yasoka.screenrecord.sdk.video.lasque.tusdk.video.editor.TuSdkMediaEffectData;
import com.yasoka.screenrecord.sdk.video.lasque.tusdk.video.editor.TuSdkMediaStickerAudioEffectData;
import com.yasoka.screenrecord.sdk.video.lasque.tusdk.video.editor.TuSdkMediaTimeEffect;

import java.util.Iterator;
//import org.lasque.tusdk.core.struct.TuSdkMediaDataSource;
import java.util.ArrayList;
//import org.lasque.tusdk.core.api.extend.TuSdkSurfaceRender;
//import org.lasque.tusdk.core.api.extend.TuSdkMediaProgress;
//import org.lasque.tusdk.core.media.codec.sync.TuSdkMediaFileDirectorSync;
//import org.lasque.tusdk.video.editor.TuSdkMediaTimeEffect;
import java.io.File;
//import org.lasque.tusdk.core.media.codec.extend.TuSdkMediaTimeline;
//import org.lasque.tusdk.video.editor.TuSdkMediaEffectData;
import java.util.List;
//import org.lasque.tusdk.api.engine.TuSdkFilterEngine;
//import org.lasque.tusdk.core.media.codec.suit.TuSdkMediaFileCuterImpl;

public class TuSdkEditorSaverImpl implements TuSdkEditorSaver
{
    private TuSdkMediaFileCuterImpl a;
    private TuSdkFilterEngine b;
    private List<TuSdkMediaEffectData> c;
    private TuSdkEditorAudioMixerImpl d;
    private TuSdkMediaTimeline e;
    private SelesWatermark f;
    private File g;
    private TuSdkEditorSaverOptions h;
    private int i;
    private TuSdkMediaTimeEffect j;
    private TuSdkMediaFileDirectorSync k;
    private boolean l;
    private float m;
    private float n;
    private float o;
    private float p;
    private float q;
    private int r;
    private List<TuSdkSaverProgressListener> s;
    private TuSdkMediaProgress t;
    private TuSdkSurfaceRender u;
    
    public TuSdkEditorSaverImpl() {
        this.i = -1;
        this.l = true;
        this.m = 0.0f;
        this.n = 0.0f;
        this.o = 0.0f;
        this.p = 0.0f;
        this.q = 1.0f;
        this.r = 0;
        this.s = new ArrayList<TuSdkSaverProgressListener>();
        this.t = (TuSdkMediaProgress)new TuSdkMediaProgress() {
            public void onProgress(final float n, final TuSdkMediaDataSource tuSdkMediaDataSource, final int n2, final int n3) {
                final Iterator<TuSdkSaverProgressListener> iterator = TuSdkEditorSaverImpl.this.s.iterator();
                while (iterator.hasNext()) {
                    iterator.next().onProgress(n);
                }
            }
            
            public void onCompleted(final Exception ex, final TuSdkMediaDataSource tuSdkMediaDataSource, final int n) {
                if (TuSdkEditorSaverImpl.this.s.size() == 0) {
                    return;
                }
                TuSdkEditorSaverImpl.this.a((ex == null) ? 3 : 4);
                if (ex == null && TuSdkEditorSaverImpl.this.h.c) {
                    ImageSqlHelper.notifyRefreshAblum(TuSdk.appContext().getContext(), ImageSqlHelper.saveMp4ToAlbum(TuSdk.appContext().getContext(), new File(tuSdkMediaDataSource.getPath())));
                }
                for (final TuSdkSaverProgressListener tuSdkSaverProgressListener : TuSdkEditorSaverImpl.this.s) {
                    if (ex == null) {
                        tuSdkSaverProgressListener.onCompleted(tuSdkMediaDataSource);
                    }
                    else {
                        tuSdkSaverProgressListener.onError(ex);
                    }
                }
            }
        };
        this.u = (TuSdkSurfaceRender)new TuSdkSurfaceRender() {
            public void onSurfaceCreated() {
                TuSdkEditorSaverImpl.this.b.onSurfaceCreated();
                TuSdkEditorSaverImpl.this.d();
            }
            
            public void onSurfaceChanged(final int n, final int n2) {
                TuSdkEditorSaverImpl.this.b.onSurfaceChanged(n, n2);
            }
            
            public void onSurfaceDestory() {
                TuSdkEditorSaverImpl.this.b.release();
            }
            
            public int onDrawFrame(final int n, final int n2, final int n3, final long n4) {
                final long n5 = n4 / 1000L;
                final TuSdkMediaTimeSliceEntity sliceWithOutputTimeUs = TuSdkEditorSaverImpl.this.k.getTimeLine().sliceWithOutputTimeUs(n5);
                long n6;
                if (TuSdkEditorSaverImpl.this.j != null && sliceWithOutputTimeUs != null) {
                    n6 = TuSdkEditorSaverImpl.this.j.calcOutputTimeUs(TuSdkEditorSaverImpl.this.k.lastVideoDecodecTimestampNs() / 1000L, sliceWithOutputTimeUs, TuSdkEditorSaverImpl.this.k.getTimeLine().getFinalSlices());
                }
                else {
                    n6 = TuSdkEditorSaverImpl.this.k.getTimeLine().sliceWithCalcModeOutputTimeUs(n5);
                }
                return TuSdkEditorSaverImpl.this.b.processFrame(n, n2, n3, n6 * 1000L);
            }
            
            public void onDrawFrameCompleted() {
            }
        };
        this.b = new TuSdkFilterEngineImpl(false, true);
        this.a(1);
    }
    
    @Override
    public void setOptions(final TuSdkEditorSaverOptions h) {
        this.h = h;
    }
    
    protected void setTimeline(final TuSdkMediaTimeline e) {
        this.e = e;
    }
    
    protected void setMediaDataList(final List<TuSdkMediaEffectData> list) {
        if (list == null) {
            return;
        }
        final ArrayList<TuSdkMediaEffectData> c = new ArrayList<TuSdkMediaEffectData>();
        for (final TuSdkMediaEffectData tuSdkMediaEffectData : list) {
            c.add(tuSdkMediaEffectData.clone());
            if (tuSdkMediaEffectData instanceof TuSdkMediaStickerAudioEffectData) {
                StatisticsManger.appendComponent(9445382L);
            }
            else {
                if (!(tuSdkMediaEffectData instanceof TuSdkMediaAudioEffectData)) {
                    continue;
                }
                StatisticsManger.appendComponent(9445380L);
            }
        }
        this.c = c;
    }
    
    protected void setAudioMixerRender(final TuSdkEditorAudioMixerImpl d) {
        this.d = d;
    }
    
    @Override
    public void addSaverProgressListener(final TuSdkSaverProgressListener tuSdkSaverProgressListener) {
        if (tuSdkSaverProgressListener == null) {
            return;
        }
        this.s.add(tuSdkSaverProgressListener);
    }
    
    @Override
    public void removeProgressListener(final TuSdkSaverProgressListener tuSdkSaverProgressListener) {
        if (tuSdkSaverProgressListener == null) {
            return;
        }
        this.s.remove(tuSdkSaverProgressListener);
    }
    
    @Override
    public void removeAllProgressListener() {
        this.s.clear();
    }
    
    @Override
    public int getStatus() {
        return this.i;
    }
    
    public void setOutputRatio(final float m, final boolean l) {
        this.m = m;
        this.l = l;
    }
    
    public void setOutputSize(final TuSdkSize outputSize, final boolean l) {
        if (outputSize == null || !outputSize.isSize()) {
            TLog.w("%s output size is %s", new Object[] { outputSize, "TuSdkEditorSaver" });
            return;
        }
        if (this.a != null) {
            this.a.setOutputSize(outputSize);
        }
        this.l = l;
    }
    
    public void setCanvasColor(final int n) {
        this.setCanvasColor(Color.red(n) / 255.0f, Color.green(n) / 255.0f, Color.blue(n) / 255.0f, Color.alpha(n) / 255.0f);
    }
    
    public void setCanvasColor(final float n, final float o, final float p4, final float q) {
        this.n = n;
        this.o = o;
        this.p = p4;
        this.q = q;
    }
    
    public boolean startSave() {
        if (this.h == null || !this.h.check()) {
            TLog.e("%s Saver Options is invalid", new Object[] { "TuSdkEditorSaver" });
            return false;
        }
        this.k = new TuSdkMediaFileDirectorSync();
        this.k.getTimeLine().setProgressOutputMode(this.r);
        (this.a = new TuSdkMediaFileCuterImpl((TuSdkMediaFileSync)this.k)).setMediaDataSource(this.h.mediaDataSource);
        this.a.setEnableClip(this.l);
        this.a.setOutputRatio(this.m);
        this.a.setOutputVideoFormat(this.b());
        this.a.setOutputAudioFormat(this.c());
        if (this.h.mWaterImageBitmap != null) {
            this.e().setImage(this.h.mWaterImageBitmap, this.h.isRecycleWaterImage);
            this.e().setWaterPostion(this.h.b);
            this.e().setScale(this.h.mWaterImageScale);
            if (this.f != null) {
                this.a.setWatermark(this.f);
            }
        }
        this.a.setSurfaceRender(this.u);
        if (this.d != null && this.d.getMixerAudioRender() != null) {
            this.d.getMixerAudioRender().seekTo(0L);
            this.a.setAudioMixerRender((TuSdkAudioRender)this.d.getMixerAudioRender());
            if (this.d.getMixerAudioRender().getTrunkVolume() != 1.0f) {
                StatisticsManger.appendComponent(9445381L);
            }
        }
        if (this.e != null) {
            this.a.setTimeline(this.e);
        }
        this.a.setOutputFilePath(this.a().getAbsolutePath());
        this.a(2);
        return this.a.run(this.t);
    }
    
    protected void setTimeEffect(final TuSdkMediaTimeEffect j) {
        this.j = j;
    }
    
    protected void setCalcMode(final int r) {
        this.r = r;
    }
    
    @Override
    public void stopSave() {
        this.a.stop();
        this.a(5);
    }
    
    @Override
    public void destroy() {
        if (this.a != null) {
            this.a.stop();
        }
        this.s.clear();
        this.b.release();
        if (this.h != null && this.h.e) {
            this.h.mediaDataSource.deleted();
        }
        this.u = null;
        this.a = null;
    }
    
    private File a() {
        if (this.h.f != null) {
            return this.g = this.h.f;
        }
        if (this.h.c) {
            if (StringHelper.isNotBlank(this.h.d)) {
                this.g = AlbumHelper.getAlbumVideoFile(this.h.d);
            }
            else {
                this.g = AlbumHelper.getAlbumVideoFile();
            }
        }
        else {
            this.g = new File(TuSdk.getAppTempPath(), String.format("lsq_%s.mp4", StringHelper.timeStampString()));
        }
        return this.g;
    }
    
    private MediaFormat b() {
        final TuSDKVideoEncoderSetting a = this.h.a;
        final TuSdkSize create = TuSdkSize.create(a.videoSize);
        final TuSDKVideoInfo videoInfo = TuSDKMediaUtils.getVideoInfo(this.h.mediaDataSource);
        if (this.m != 0.0f) {
            final int n = (videoInfo.width > videoInfo.height) ? videoInfo.width : videoInfo.height;
            final TuSdkSize create2 = TuSdkSize.create(n, (int)(n / this.m));
            create.width = create2.width;
            create.height = create2.height;
            this.setOutputSize(create2, this.l);
        }
        if ((videoInfo.videoOrientation == ImageOrientation.Right || videoInfo.videoOrientation == ImageOrientation.Left) && create.width > create.height) {
            create.width = a.videoSize.height;
            create.height = a.videoSize.width;
        }
        return TuSdkMediaFormat.buildSafeVideoEncodecFormat(create.width, create.height, a.videoQuality.getFps(), a.videoQuality.getBitrate(), 2130708361, 0, a.mediacodecAVCIFrameInterval);
    }
    
    private MediaFormat c() {
        final MediaFormat audioFormat = TuSDKMediaUtils.getAudioFormat(this.h.mediaDataSource);
        if (audioFormat == null || audioFormat.getInteger("channel-count") > 1) {
            return TuSdkMediaFormat.buildSafeAudioEncodecFormat();
        }
        return TuSdkMediaFormat.buildSafeAudioEncodecFormat(audioFormat.getInteger("sample-rate"), audioFormat.getInteger("channel-count"), 96000, 2);
    }
    
    private void d() {
        if (this.c == null || this.c.size() == 0) {
            return;
        }
        final Iterator<TuSdkMediaEffectData> iterator = this.c.iterator();
        while (iterator.hasNext()) {
            this.b.addMediaEffectData(iterator.next());
        }
    }
    
    private void a(final int i) {
        this.i = i;
    }
    
    private synchronized SelesWatermark e() {
        if (this.f == null) {
            this.f = (SelesWatermark)new SelesWatermarkImpl(true);
        }
        return this.f;
    }
}
