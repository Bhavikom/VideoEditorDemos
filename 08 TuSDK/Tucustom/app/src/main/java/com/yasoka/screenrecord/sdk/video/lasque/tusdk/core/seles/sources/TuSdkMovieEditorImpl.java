// 
// Decompiled by Procyon v0.5.36
// 

package com.yasoka.screenrecord.sdk.video.lasque.tusdk.core.seles.sources;

//import org.lasque.tusdk.core.api.extend.TuSdkAudioRender;
//import org.lasque.tusdk.core.secret.StatisticsManger;
//import org.lasque.tusdk.core.struct.TuSdkSize;
//import org.lasque.tusdk.core.utils.image.ImageOrientation;
//import org.lasque.tusdk.core.utils.TLog;
//import org.lasque.tusdk.core.decoder.TuSDKVideoInfo;
//import org.lasque.tusdk.core.encoder.video.TuSDKVideoEncoderSetting;
//import org.lasque.tusdk.core.struct.TuSdkMediaDataSource;
import android.view.ViewGroup;
import android.content.Context;

import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.api.extend.TuSdkAudioRender;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.secret.StatisticsManger;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.struct.TuSdkMediaDataSource;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.struct.TuSdkSize;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.TLog;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.image.ImageOrientation;
import com.yasoka.screenrecord.sdk.video.lasque.tusdk.core.decoder.TuSDKVideoInfo;
import com.yasoka.screenrecord.sdk.video.lasque.tusdk.core.encoder.video.TuSDKVideoEncoderSetting;

public class TuSdkMovieEditorImpl implements TuSdkMovieEditor
{
    private Context a;
    private ViewGroup b;
    private TuSdkEditorTranscoderImpl c;
    private TuSdkEditorPlayerImpl d;
    private TuSdkEditorEffectorImpl e;
    private TuSdkEditorAudioMixerImpl f;
    private TuSdkEditorSaverImpl g;
    private TuSdkMediaDataSource h;
    private TuSdkMediaDataSource i;
    private TuSdkMovieEditorOptions j;
    private TuSDKVideoEncoderSetting k;
    private TuSdkEditorTranscoder.TuSdkTranscoderProgressListener l;
    
    public TuSdkMovieEditorImpl(final Context a, final ViewGroup b, final TuSdkMovieEditorOptions j) {
        this.l = new TuSdkEditorTranscoder.TuSdkTranscoderProgressListener() {
            @Override
            public void onProgressChanged(final float n) {
            }
            
            @Override
            public void onLoadComplete(final TuSDKVideoInfo tuSDKVideoInfo, final TuSdkMediaDataSource tuSdkMediaDataSource) {
                TuSdkMovieEditorImpl.this.a(tuSDKVideoInfo, tuSdkMediaDataSource);
            }
            
            @Override
            public void onError(final Exception ex) {
                TLog.e((Throwable)ex);
            }
        };
        this.a = a;
        this.b = b;
        this.j = j;
        this.setDataSource(j.videoDataSource);
    }
    
    private void a() {
        ((TuSdkEditorEffectorImpl)this.getEditorEffector()).setAudioMixer(this.getEditorMixer());
        ((TuSdkEditorPlayerImpl)this.getEditorPlayer()).setEffector((TuSdkEditorEffectorImpl)this.getEditorEffector());
        ((TuSdkEditorPlayerImpl)this.getEditorPlayer()).setAudioMixer((TuSdkEditorAudioMixerImpl)this.getEditorMixer());
    }
    
    @Override
    public Context getContext() {
        return this.a;
    }
    
    @Override
    public void setVideoPath(final String s) {
        this.setDataSource(new TuSdkMediaDataSource(s));
    }
    
    @Override
    public void setDataSource(final TuSdkMediaDataSource h) {
        this.h = h;
    }
    
    @Override
    public void setEnableTranscode(final boolean enableTranscode) {
        if (this.c == null) {
            this.getEditorTransCoder();
        }
        this.c.setEnableTranscode(enableTranscode);
    }
    
    @Override
    public void loadVideo() {
        this.a();
        if (this.h == null || !this.h.isValid()) {
            TLog.e("%s the data source is invalid. dataSource = %s ", new Object[] { "TuSdkMovieEditorImpl", this.h });
            return;
        }
        this.getEditorTransCoder().setVideoDataSource(this.h);
        this.getEditorTransCoder().setTimeRange(this.j.cutTimeRange);
        this.getEditorTransCoder().addTransCoderProgressListener(this.l);
        this.getEditorTransCoder().setCanvasRect(this.j.canvasRect);
        this.getEditorTransCoder().setCropRect(this.j.cropRect);
        this.getEditorTransCoder().startTransCoder();
    }
    
    @Override
    public void saveVideo() {
        if (!this.getEditorPlayer().isPause()) {
            this.getEditorPlayer().pausePreview();
        }
        final TuSDKVideoEncoderSetting videoEncoderSetting = this.getVideoEncoderSetting();
        final TuSDKVideoInfo inputVideoInfo = this.c.getInputVideoInfo();
        if (inputVideoInfo != null) {
            videoEncoderSetting.mediacodecAVCIFrameInterval = ((inputVideoInfo.iFrameInterval == 0) ? 1 : inputVideoInfo.iFrameInterval);
            if (videoEncoderSetting.videoSize == null) {
                TuSdkSize tuSdkSize;
                if (this.c.getInputVideoInfo().videoOrientation != ImageOrientation.Up) {
                    tuSdkSize = TuSdkSize.create(this.c.getInputVideoInfo().height, this.c.getInputVideoInfo().width);
                }
                else {
                    tuSdkSize = TuSdkSize.create(this.c.getInputVideoInfo().width, this.c.getInputVideoInfo().height);
                }
                videoEncoderSetting.videoSize = ((this.j.outputSize == null) ? tuSdkSize : this.j.outputSize);
            }
            if (videoEncoderSetting.videoQuality == null) {
                videoEncoderSetting.videoQuality = TuSDKVideoEncoderSetting.VideoQuality.RECORD_MEDIUM1;
            }
        }
        final TuSdkEditorSaver.TuSdkEditorSaverOptions options = new TuSdkEditorSaver.TuSdkEditorSaverOptions();
        options.mediaDataSource = this.i;
        options.a = videoEncoderSetting;
        options.c = this.j.saveToAlbum;
        options.d = this.j.saveToAlbumName;
        options.mWaterImageBitmap = this.j.waterImage;
        options.mWaterImageScale = this.j.waterImageScale;
        options.isRecycleWaterImage = this.j.isRecycleWaterImage;
        options.b = this.j.watermarkPosition;
        options.f = this.j.movieOutputFilePath;
        ((TuSdkEditorSaverImpl)this.getEditorSaver()).setMediaDataList(this.getEditorEffector().getAllMediaEffectData());
        ((TuSdkEditorSaverImpl)this.getEditorSaver()).setTimeline(((TuSdkEditorPlayerImpl)this.getEditorPlayer()).getTimelineEffect());
        ((TuSdkEditorSaverImpl)this.getEditorSaver()).setTimeEffect(((TuSdkEditorPlayerImpl)this.getEditorPlayer()).getTimeEffect());
        ((TuSdkEditorSaverImpl)this.getEditorSaver()).setCalcMode(((TuSdkEditorPlayerImpl)this.getEditorPlayer()).getProgressOutputMode());
        ((TuSdkEditorSaverImpl)this.getEditorSaver()).setAudioMixerRender((TuSdkEditorAudioMixerImpl)this.getEditorMixer());
        this.getEditorSaver().setOptions(options);
        ((TuSdkEditorSaverImpl)this.getEditorSaver()).setOutputRatio(((TuSdkEditorPlayerImpl)this.getEditorPlayer()).getOutputRatio(), ((TuSdkEditorPlayerImpl)this.getEditorPlayer()).isEnableClip());
        ((TuSdkEditorSaverImpl)this.getEditorSaver()).setOutputSize(((TuSdkEditorPlayerImpl)this.getEditorPlayer()).getOutputSize(), ((TuSdkEditorPlayerImpl)this.getEditorPlayer()).isEnableClip());
        ((TuSdkEditorSaverImpl)this.getEditorSaver()).setCanvasColor(((TuSdkEditorPlayerImpl)this.getEditorPlayer()).getCanvasColorRed(), ((TuSdkEditorPlayerImpl)this.getEditorPlayer()).getCanvasColorGreen(), ((TuSdkEditorPlayerImpl)this.getEditorPlayer()).getCanvasColorBlue(), ((TuSdkEditorPlayerImpl)this.getEditorPlayer()).getCanvasColorAlpha());
        ((TuSdkEditorSaverImpl)this.getEditorSaver()).startSave();
        StatisticsManger.appendComponent(9445376L);
    }
    
    @Override
    public TuSdkEditorTranscoder getEditorTransCoder() {
        if (this.c == null) {
            this.c = new TuSdkEditorTranscoderImpl();
        }
        return this.c;
    }
    
    @Override
    public TuSdkEditorPlayer getEditorPlayer() {
        if (this.d == null) {
            this.d = new TuSdkEditorPlayerImpl(this.a);
        }
        return this.d;
    }
    
    @Override
    public TuSdkEditorEffector getEditorEffector() {
        if (this.e == null) {
            this.e = new TuSdkEditorEffectorImpl();
        }
        return this.e;
    }
    
    @Override
    public TuSdkEditorAudioMixer getEditorMixer() {
        if (this.f == null) {
            this.f = new TuSdkEditorAudioMixerImpl();
        }
        return this.f;
    }
    
    @Override
    public TuSdkEditorSaver getEditorSaver() {
        if (this.g == null) {
            this.g = new TuSdkEditorSaverImpl();
        }
        return this.g;
    }
    
    @Override
    public void onDestroy() {
        this.getEditorTransCoder().destroy();
        this.getEditorPlayer().destroy();
        this.getEditorEffector().destroy();
        this.getEditorMixer().destroy();
        this.getEditorSaver().destroy();
    }
    
    private void a(final TuSDKVideoInfo tuSDKVideoInfo, final TuSdkMediaDataSource dataSource) {
        this.i = dataSource;
        this.getEditorPlayer().setPreviewContainer(this.b);
        this.getEditorPlayer().setDataSource(dataSource);
        this.getEditorPlayer().enableFirstFramePause(this.j.enableFirstFramePause);
        this.d.setProgressOutputMode(this.j.timelineType.getType());
        this.getEditorEffector().setInputImageOrientation(tuSDKVideoInfo.videoOrientation);
        this.getEditorMixer().setDataSource(dataSource);
        ((TuSdkEditorPlayerImpl)this.getEditorPlayer()).setAudioMixerRender((TuSdkAudioRender)((TuSdkEditorAudioMixerImpl)this.getEditorMixer()).getMixerAudioRender());
        ((TuSdkEditorAudioMixerImpl)this.getEditorMixer()).setIncludeMasterSound(this.j.includeAudioInVideo);
        this.d.loadVideo();
    }
    
    public TuSDKVideoEncoderSetting getVideoEncoderSetting() {
        if (this.k == null) {
            this.k = TuSDKVideoEncoderSetting.getDefaultRecordSetting();
            this.k.videoQuality = null;
            this.k.videoSize = null;
        }
        return this.k;
    }
}
