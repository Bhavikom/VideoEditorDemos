package org.lasque.tusdk.core.utils.hardware;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.PointF;
import android.graphics.RectF;
import android.graphics.SurfaceTexture;
import android.graphics.SurfaceTexture.OnFrameAvailableListener;
import android.media.MediaFormat;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView.Renderer;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import java.io.File;
import java.nio.IntBuffer;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;
import org.lasque.tusdk.api.audio.preproc.processor.TuSdkAudioPitchEngine.TuSdkSoundPitchType;
import org.lasque.tusdk.api.engine.TuSdkFilterEngine.TuSdkFilterEngineListener;
import org.lasque.tusdk.api.engine.TuSdkFilterEngineImpl;
import org.lasque.tusdk.api.video.preproc.filter.TuSDKVideoProcesser.TuSDKVideoProcessorMediaEffectDelegate;
import org.lasque.tusdk.core.TuSdk;
import org.lasque.tusdk.core.TuSdkResult;
import org.lasque.tusdk.core.api.engine.TuSdkEngineProcessor.TuSdkVideoProcesserFaceDetectionDelegate;
import org.lasque.tusdk.core.api.engine.TuSdkEngineProcessor.TuSdkVideoProcesserFaceDetectionResultType;
import org.lasque.tusdk.core.api.extend.TuSdkMediaProgress;
import org.lasque.tusdk.core.api.extend.TuSdkSurfaceRender;
import org.lasque.tusdk.core.detector.FrameDetectProcessor;
import org.lasque.tusdk.core.face.FaceAligment;
import org.lasque.tusdk.core.face.TuSdkFaceDetector;
import org.lasque.tusdk.core.listener.TuSdkOrientationEventListener.TuSdkOrientationDegreeDelegate;
import org.lasque.tusdk.core.listener.TuSdkOrientationEventListener.TuSdkOrientationDelegate;
import org.lasque.tusdk.core.media.camera.TuSdkCamera;
import org.lasque.tusdk.core.media.camera.TuSdkCamera.TuSdkCameraListener;
import org.lasque.tusdk.core.media.camera.TuSdkCamera.TuSdkCameraStatus;
import org.lasque.tusdk.core.media.camera.TuSdkCameraBuilder;
import org.lasque.tusdk.core.media.camera.TuSdkCameraBuilderImpl;
import org.lasque.tusdk.core.media.camera.TuSdkCameraFocus.TuSdkCameraFocusFaceListener;
import org.lasque.tusdk.core.media.camera.TuSdkCameraFocus.TuSdkCameraFocusListener;
import org.lasque.tusdk.core.media.camera.TuSdkCameraFocusImpl;
import org.lasque.tusdk.core.media.camera.TuSdkCameraImpl;
import org.lasque.tusdk.core.media.camera.TuSdkCameraOrientationImpl;
import org.lasque.tusdk.core.media.camera.TuSdkCameraParametersImpl;
import org.lasque.tusdk.core.media.camera.TuSdkCameraShot.TuSdkCameraShotFaceFaceAligment;
import org.lasque.tusdk.core.media.camera.TuSdkCameraShot.TuSdkCameraShotFilter;
import org.lasque.tusdk.core.media.camera.TuSdkCameraShot.TuSdkCameraShotListener;
import org.lasque.tusdk.core.media.camera.TuSdkCameraShotImpl;
import org.lasque.tusdk.core.media.camera.TuSdkCameraSizeImpl;
import org.lasque.tusdk.core.media.codec.extend.TuSdkMediaFileCuterTimeline;
import org.lasque.tusdk.core.media.codec.extend.TuSdkMediaFormat;
import org.lasque.tusdk.core.media.codec.extend.TuSdkMediaTimeSlice;
import org.lasque.tusdk.core.media.codec.extend.TuSdkMediaTimeline;
import org.lasque.tusdk.core.media.codec.video.TuSdkVideoQuality;
import org.lasque.tusdk.core.media.record.TuSdkCameraRecorder;
import org.lasque.tusdk.core.media.record.TuSdkMediaRecordHub;
import org.lasque.tusdk.core.media.record.TuSdkMediaRecordHub.TuSdkMediaRecordHubListener;
import org.lasque.tusdk.core.media.record.TuSdkMediaRecordHub.TuSdkMediaRecordHubStatus;
import org.lasque.tusdk.core.media.suit.TuSdkMediaSuit;
import org.lasque.tusdk.core.secret.SdkValid;
import org.lasque.tusdk.core.secret.StatisticsManger;
import org.lasque.tusdk.core.seles.SelesContext;
import org.lasque.tusdk.core.seles.SelesParameters.FilterFacePositionInterface;
import org.lasque.tusdk.core.seles.SelesParameters.FilterStickerInterface;
import org.lasque.tusdk.core.seles.filters.SelesFilter;
import org.lasque.tusdk.core.seles.filters.SelesFilter.FrameProcessingDelegate;
import org.lasque.tusdk.core.seles.output.SelesSmartView;
import org.lasque.tusdk.core.seles.sources.SelesOutInput;
import org.lasque.tusdk.core.seles.sources.SelesPicture;
import org.lasque.tusdk.core.seles.sources.SelesWatermark;
import org.lasque.tusdk.core.seles.sources.SelesWatermarkImpl;
import org.lasque.tusdk.core.seles.tusdk.FilterWrap;
import org.lasque.tusdk.core.seles.tusdk.combo.Face2DComboFilterWrap;
import org.lasque.tusdk.core.sticker.LiveStickerPlayController;
import org.lasque.tusdk.core.struct.TuSdkMediaDataSource;
import org.lasque.tusdk.core.struct.TuSdkSize;
import org.lasque.tusdk.core.struct.ViewSize;
import org.lasque.tusdk.core.utils.FileHelper;
import org.lasque.tusdk.core.utils.StringHelper;
import org.lasque.tusdk.core.utils.TLog;
import org.lasque.tusdk.core.utils.ThreadHelper;
import org.lasque.tusdk.core.utils.TuSdkWaterMarkOption.WaterMarkPosition;
import org.lasque.tusdk.core.utils.image.AlbumHelper;
import org.lasque.tusdk.core.utils.image.BitmapHelper;
import org.lasque.tusdk.core.utils.image.ImageOrientation;
import org.lasque.tusdk.core.utils.sqllite.ImageSqlHelper;
import org.lasque.tusdk.core.video.TuSDKVideoResult;
import org.lasque.tusdk.impl.components.camera.TuSdkVideoFocusTouchView;
import org.lasque.tusdk.impl.view.widget.RegionDefaultHandler;
import org.lasque.tusdk.impl.view.widget.RegionHandler;
import org.lasque.tusdk.impl.view.widget.RegionHandler.RegionChangerListener;
import org.lasque.tusdk.video.editor.TuSdkMediaEffectData;
import org.lasque.tusdk.video.editor.TuSdkMediaEffectData.TuSdkMediaEffectDataType;
import org.lasque.tusdk.video.editor.TuSdkMediaParticleEffectData;
import org.lasque.tusdk.video.editor.TuSdkMediaSceneEffectData;
import org.lasque.tusdk.video.editor.TuSdkMediaStickerEffectData;
import org.lasque.tusdk.video.editor.TuSdkTimeRange;

public final class TuSdkRecorderVideoCameraImpl
  implements TuSdkRecorderVideoCamera
{
  private Context a;
  private RelativeLayout b;
  private SelesSmartView c;
  private TuSdkRecorderCameraSetting d;
  private TuSdkCameraImpl e;
  private TuSdkFilterEngineImpl f;
  private TuSdkCameraRecorder g;
  private TuSdkRecorderVideoEncoderSetting h;
  private long i = 52428800L;
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
  private boolean t = true;
  private boolean u = true;
  private boolean v = false;
  private TuSdkAudioPitchEngine.TuSdkSoundPitchType w;
  private TuSdkCameraShotImpl x;
  private String y;
  private int z = 2;
  private int A = 15;
  private Object B = new Object();
  private LinkedList<TuSdkTimeRange> C = new LinkedList();
  private LinkedList<TuSdkTimeRange> D = new LinkedList();
  private long E;
  private long F;
  private TuSdkMediaRecordHub.TuSdkMediaRecordHubStatus G;
  private TuSdkTimeRange H;
  private long I = 0L;
  private TuSdkRecorderVideoCamera.TuSdkRecorderVideoCameraCallback J;
  private TuSdkRecorderVideoCamera.TuSdkCameraListener K;
  private TuSdkRecorderVideoCamera.TuSdkFaceDetectionCallback L;
  private TuSdkRecorderVideoCamera.TuSdkMediaEffectChangeListener M;
  private RegionHandler N;
  private TuSdkVideoFocusTouchView O;
  private float P;
  private TuSdkStillCameraAdapter.CameraState Q;
  private TuSdkRecorderVideoCamera.RecordState R;
  private boolean S;
  private long T;
  private int U = -16777216;
  private boolean V = false;
  private TuSdkMediaRecordHub.TuSdkMediaRecordHubListener W = new TuSdkMediaRecordHub.TuSdkMediaRecordHubListener()
  {
    public void onStatusChanged(final TuSdkMediaRecordHub.TuSdkMediaRecordHubStatus paramAnonymousTuSdkMediaRecordHubStatus, TuSdkMediaRecordHub paramAnonymousTuSdkMediaRecordHub)
    {
      TuSdkRecorderVideoCameraImpl.a(TuSdkRecorderVideoCameraImpl.this, paramAnonymousTuSdkMediaRecordHubStatus);
      TuSdkRecorderVideoCameraImpl.a(TuSdkRecorderVideoCameraImpl.this, TuSdkRecorderVideoCameraImpl.a(TuSdkRecorderVideoCameraImpl.this), paramAnonymousTuSdkMediaRecordHubStatus);
      if (TuSdkRecorderVideoCameraImpl.b(TuSdkRecorderVideoCameraImpl.this) != null) {
        ThreadHelper.post(new Runnable()
        {
          public void run()
          {
            if (paramAnonymousTuSdkMediaRecordHubStatus == TuSdkMediaRecordHub.TuSdkMediaRecordHubStatus.START_RECORD)
            {
              TuSdkRecorderVideoCameraImpl.a(TuSdkRecorderVideoCameraImpl.this, TuSdkRecorderVideoCamera.RecordState.Recording);
            }
            else if (paramAnonymousTuSdkMediaRecordHubStatus == TuSdkMediaRecordHub.TuSdkMediaRecordHubStatus.PAUSE_RECORD)
            {
              TuSdkRecorderVideoCameraImpl.a(TuSdkRecorderVideoCameraImpl.this, TuSdkRecorderVideoCamera.RecordState.Paused);
              if (TuSdkRecorderVideoCameraImpl.c(TuSdkRecorderVideoCameraImpl.this) >= TuSdkRecorderVideoCameraImpl.d(TuSdkRecorderVideoCameraImpl.this) * 1000000) {
                TuSdkRecorderVideoCameraImpl.a(TuSdkRecorderVideoCameraImpl.this, TuSdkRecorderVideoCamera.RecordState.RecordCompleted);
              }
            }
          }
        });
      }
    }
    
    public void onProgress(long paramAnonymousLong, TuSdkMediaDataSource paramAnonymousTuSdkMediaDataSource)
    {
      if (TuSdkRecorderVideoCameraImpl.e(TuSdkRecorderVideoCameraImpl.this) == TuSdkMediaRecordHub.TuSdkMediaRecordHubStatus.PAUSE_RECORD) {
        return;
      }
      TuSdkRecorderVideoCameraImpl.a(TuSdkRecorderVideoCameraImpl.this, paramAnonymousLong);
      TuSdkRecorderVideoCameraImpl.b(TuSdkRecorderVideoCameraImpl.this, paramAnonymousLong);
    }
    
    public void onCompleted(final Exception paramAnonymousException, TuSdkMediaDataSource paramAnonymousTuSdkMediaDataSource, TuSdkMediaTimeline paramAnonymousTuSdkMediaTimeline)
    {
      ThreadHelper.post(new Runnable()
      {
        public void run()
        {
          if (paramAnonymousException == null)
          {
            if (TuSdkRecorderVideoCameraImpl.b(TuSdkRecorderVideoCameraImpl.this) != null) {
              TuSdkRecorderVideoCameraImpl.a(TuSdkRecorderVideoCameraImpl.this, TuSdkRecorderVideoCamera.RecordState.Saving);
            }
          }
          else if (TuSdkRecorderVideoCameraImpl.b(TuSdkRecorderVideoCameraImpl.this) != null) {
            TuSdkRecorderVideoCameraImpl.b(TuSdkRecorderVideoCameraImpl.this).onMovieRecordFailed(TuSdkRecorderVideoCamera.RecordError.SaveFailed);
          }
        }
      });
      a(paramAnonymousTuSdkMediaDataSource);
    }
    
    private void a(TuSdkMediaDataSource paramAnonymousTuSdkMediaDataSource)
    {
      final Object localObject1;
      if ((TuSdkRecorderVideoCameraImpl.f(TuSdkRecorderVideoCameraImpl.this).size() == 0) || (TuSdkRecorderVideoCameraImpl.this.isDirectEdit()))
      {
        localObject1 = new TuSDKVideoResult();
        ((TuSDKVideoResult)localObject1).videoPath = new File(paramAnonymousTuSdkMediaDataSource.getPath());
        if (TuSdkRecorderVideoCameraImpl.this.isSaveToAlbum())
        {
          ((TuSDKVideoResult)localObject1).videoSqlInfo = ImageSqlHelper.saveMp4ToAlbum(TuSdkRecorderVideoCameraImpl.g(TuSdkRecorderVideoCameraImpl.this), ((TuSDKVideoResult)localObject1).videoPath);
          ImageSqlHelper.notifyRefreshAblum(TuSdkRecorderVideoCameraImpl.g(TuSdkRecorderVideoCameraImpl.this), ((TuSDKVideoResult)localObject1).videoSqlInfo);
        }
        ThreadHelper.post(new Runnable()
        {
          public void run()
          {
            if (TuSdkRecorderVideoCameraImpl.b(TuSdkRecorderVideoCameraImpl.this) != null)
            {
              TuSdkRecorderVideoCameraImpl.b(TuSdkRecorderVideoCameraImpl.this).onMovieRecordComplete(localObject1);
              TuSdkRecorderVideoCameraImpl.h(TuSdkRecorderVideoCameraImpl.this);
              TuSdkRecorderVideoCameraImpl.a(TuSdkRecorderVideoCameraImpl.this, TuSdkRecorderVideoCamera.RecordState.SaveCompleted);
            }
          }
        });
        return;
      }
      if (TuSdkRecorderVideoCameraImpl.f(TuSdkRecorderVideoCameraImpl.this).size() > 0)
      {
        localObject1 = new TuSdkMediaFileCuterTimeline();
        String str = "";
        long l = 0L;
        Object localObject2 = TuSdkRecorderVideoCameraImpl.i(TuSdkRecorderVideoCameraImpl.this).iterator();
        while (((Iterator)localObject2).hasNext())
        {
          TuSdkTimeRange localTuSdkTimeRange = (TuSdkTimeRange)((Iterator)localObject2).next();
          ((TuSdkMediaTimeline)localObject1).append(localTuSdkTimeRange.getStartTimeUS(), localTuSdkTimeRange.getEndTimeUS(), 1.0F);
          str = str + localTuSdkTimeRange.getStartTimeUS() + " --- " + localTuSdkTimeRange.getEndTimeUS() + "\n";
          l += localTuSdkTimeRange.getEndTimeUS() - localTuSdkTimeRange.getStartTimeUS();
        }
        localObject2 = new File(paramAnonymousTuSdkMediaDataSource.getPath());
        a(paramAnonymousTuSdkMediaDataSource, (TuSdkMediaTimeline)localObject1, (File)localObject2);
      }
    }
    
    private void a(TuSdkMediaDataSource paramAnonymousTuSdkMediaDataSource, TuSdkMediaTimeline paramAnonymousTuSdkMediaTimeline, final File paramAnonymousFile)
    {
      TuSdkMediaSuit.cuter(paramAnonymousTuSdkMediaDataSource, TuSdkRecorderVideoCameraImpl.this.getMovieOutputPath() + ".temp.mp4", TuSdkRecorderVideoCameraImpl.j(TuSdkRecorderVideoCameraImpl.this), TuSdkRecorderVideoCameraImpl.k(TuSdkRecorderVideoCameraImpl.this), ImageOrientation.Up, new RectF(0.0F, 0.0F, 1.0F, 1.0F), new RectF(0.0F, 0.0F, 1.0F, 1.0F), paramAnonymousTuSdkMediaTimeline, new TuSdkMediaProgress()
      {
        public void onProgress(float paramAnonymous2Float, TuSdkMediaDataSource paramAnonymous2TuSdkMediaDataSource, int paramAnonymous2Int1, int paramAnonymous2Int2) {}
        
        public void onCompleted(Exception paramAnonymous2Exception, TuSdkMediaDataSource paramAnonymous2TuSdkMediaDataSource, int paramAnonymous2Int)
        {
          if (paramAnonymous2Exception != null)
          {
            TLog.e(paramAnonymous2Exception);
            TuSdkRecorderVideoCameraImpl.h(TuSdkRecorderVideoCameraImpl.this);
            if (TuSdkRecorderVideoCameraImpl.b(TuSdkRecorderVideoCameraImpl.this) != null) {
              TuSdkRecorderVideoCameraImpl.b(TuSdkRecorderVideoCameraImpl.this).onMovieRecordFailed(TuSdkRecorderVideoCamera.RecordError.SaveFailed);
            }
            return;
          }
          String str = paramAnonymousFile.getAbsolutePath();
          FileHelper.delete(paramAnonymousFile);
          FileHelper.rename(new File(paramAnonymous2TuSdkMediaDataSource.getPath()), new File(str));
          final TuSDKVideoResult localTuSDKVideoResult = new TuSDKVideoResult();
          localTuSDKVideoResult.videoPath = new File(str);
          if (TuSdkRecorderVideoCameraImpl.this.isSaveToAlbum())
          {
            localTuSDKVideoResult.videoSqlInfo = ImageSqlHelper.saveMp4ToAlbum(TuSdkRecorderVideoCameraImpl.g(TuSdkRecorderVideoCameraImpl.this), new File(str));
            ImageSqlHelper.notifyRefreshAblum(TuSdkRecorderVideoCameraImpl.g(TuSdkRecorderVideoCameraImpl.this), localTuSDKVideoResult.videoSqlInfo);
          }
          ThreadHelper.post(new Runnable()
          {
            public void run()
            {
              if (TuSdkRecorderVideoCameraImpl.b(TuSdkRecorderVideoCameraImpl.this) != null)
              {
                TuSdkRecorderVideoCameraImpl.h(TuSdkRecorderVideoCameraImpl.this);
                TuSdkRecorderVideoCameraImpl.a(TuSdkRecorderVideoCameraImpl.this, TuSdkRecorderVideoCamera.RecordState.SaveCompleted);
                TuSdkRecorderVideoCameraImpl.b(TuSdkRecorderVideoCameraImpl.this).onMovieRecordComplete(localTuSDKVideoResult);
              }
            }
          });
        }
      });
    }
  };
  private TuSdkFilterEngine.TuSdkFilterEngineListener X = new TuSdkFilterEngine.TuSdkFilterEngineListener()
  {
    public void onPictureDataCompleted(IntBuffer paramAnonymousIntBuffer, TuSdkSize paramAnonymousTuSdkSize) {}
    
    public void onPreviewScreenShot(final Bitmap paramAnonymousBitmap)
    {
      ThreadHelper.post(new Runnable()
      {
        public void run()
        {
          if (TuSdkRecorderVideoCameraImpl.l(TuSdkRecorderVideoCameraImpl.this) != null) {
            TuSdkRecorderVideoCameraImpl.l(TuSdkRecorderVideoCameraImpl.this).onVideoCameraScreenShot(paramAnonymousBitmap);
          }
        }
      });
    }
    
    public void onFilterChanged(final FilterWrap paramAnonymousFilterWrap)
    {
      ThreadHelper.post(new Runnable()
      {
        public void run()
        {
          if (TuSdkRecorderVideoCameraImpl.l(TuSdkRecorderVideoCameraImpl.this) != null) {
            TuSdkRecorderVideoCameraImpl.l(TuSdkRecorderVideoCameraImpl.this).onFilterChanged(paramAnonymousFilterWrap);
          }
        }
      });
    }
  };
  private TuSdkEngineProcessor.TuSdkVideoProcesserFaceDetectionDelegate Y = new TuSdkEngineProcessor.TuSdkVideoProcesserFaceDetectionDelegate()
  {
    public void onFaceDetectionResult(TuSdkEngineProcessor.TuSdkVideoProcesserFaceDetectionResultType paramAnonymousTuSdkVideoProcesserFaceDetectionResultType, final int paramAnonymousInt)
    {
      if (TuSdkRecorderVideoCameraImpl.m(TuSdkRecorderVideoCameraImpl.this) != null)
      {
        final TuSdkRecorderVideoCamera.FaceDetectionResultType localFaceDetectionResultType = paramAnonymousTuSdkVideoProcesserFaceDetectionResultType == TuSdkEngineProcessor.TuSdkVideoProcesserFaceDetectionResultType.TuSDKVideoProcesserFaceDetectionResultTypeFaceDetected ? TuSdkRecorderVideoCamera.FaceDetectionResultType.FaceDetected : TuSdkRecorderVideoCamera.FaceDetectionResultType.NoFaceDetected;
        ThreadHelper.post(new Runnable()
        {
          public void run()
          {
            TuSdkRecorderVideoCameraImpl.m(TuSdkRecorderVideoCameraImpl.this).onFaceDetectionResult(localFaceDetectionResultType, paramAnonymousInt);
          }
        });
      }
    }
  };
  private SelesFilter.FrameProcessingDelegate Z = new SelesFilter.FrameProcessingDelegate()
  {
    public void onFrameCompletion(SelesFilter paramAnonymousSelesFilter, long paramAnonymousLong)
    {
      Bitmap localBitmap1 = paramAnonymousSelesFilter.imageFromCurrentlyProcessedOutput();
      paramAnonymousSelesFilter.setFrameProcessingDelegate(null);
      final Bitmap localBitmap2 = BitmapHelper.imageCorp(localBitmap1, TuSdkRecorderVideoCameraImpl.this.getRegionRatio());
      TuSdkRecorderVideoCameraImpl.a(TuSdkRecorderVideoCameraImpl.this, TuSdkStillCameraAdapter.CameraState.StateCaptured);
      ThreadHelper.post(new Runnable()
      {
        public void run()
        {
          TuSdkRecorderVideoCameraImpl.n(TuSdkRecorderVideoCameraImpl.this).onPreviewScreenShot(localBitmap2);
        }
      });
    }
  };
  private TuSDKVideoProcesser.TuSDKVideoProcessorMediaEffectDelegate aa = new TuSDKVideoProcesser.TuSDKVideoProcessorMediaEffectDelegate()
  {
    public void didApplyingMediaEffect(TuSdkMediaEffectData paramAnonymousTuSdkMediaEffectData)
    {
      if (TuSdkRecorderVideoCameraImpl.o(TuSdkRecorderVideoCameraImpl.this) != null) {
        TuSdkRecorderVideoCameraImpl.o(TuSdkRecorderVideoCameraImpl.this).didApplyingMediaEffect(paramAnonymousTuSdkMediaEffectData);
      }
    }
    
    public void didRemoveMediaEffect(List<TuSdkMediaEffectData> paramAnonymousList)
    {
      if (TuSdkRecorderVideoCameraImpl.o(TuSdkRecorderVideoCameraImpl.this) != null) {
        TuSdkRecorderVideoCameraImpl.o(TuSdkRecorderVideoCameraImpl.this).didRemoveMediaEffect(paramAnonymousList);
      }
    }
  };
  private TuSdkCameraShot.TuSdkCameraShotListener ab = new TuSdkCameraShot.TuSdkCameraShotListener()
  {
    public void onCameraWillShot(TuSdkResult paramAnonymousTuSdkResult) {}
    
    public void onCameraShotFailed(TuSdkResult paramAnonymousTuSdkResult) {}
    
    public void onCameraShotData(TuSdkResult paramAnonymousTuSdkResult) {}
    
    public void onCameraShotBitmap(TuSdkResult paramAnonymousTuSdkResult)
    {
      TuSdkRecorderVideoCameraImpl.a(TuSdkRecorderVideoCameraImpl.this, TuSdkStillCameraAdapter.CameraState.StateCaptured);
      TuSdkRecorderVideoCameraImpl.n(TuSdkRecorderVideoCameraImpl.this).onPreviewScreenShot(paramAnonymousTuSdkResult.image);
    }
  };
  private TuSdkCameraShot.TuSdkCameraShotFaceFaceAligment ac = new TuSdkCameraShot.TuSdkCameraShotFaceFaceAligment()
  {
    @TargetApi(12)
    public FaceAligment[] detectionImageFace(Bitmap paramAnonymousBitmap, ImageOrientation paramAnonymousImageOrientation)
    {
      TuSdkFaceDetector.init();
      Bitmap localBitmap = BitmapHelper.imageResize(paramAnonymousBitmap, TuSdkSize.create(700, 700), true, 0.0F, false, paramAnonymousImageOrientation);
      FaceAligment[] arrayOfFaceAligment = TuSdkFaceDetector.markFace(localBitmap);
      return arrayOfFaceAligment;
    }
  };
  
  public int getRegionViewColor()
  {
    return this.U;
  }
  
  public void setRegionViewColor(int paramInt)
  {
    this.U = paramInt;
    if (this.c != null) {
      this.c.setBackgroundColor(this.U);
    }
  }
  
  private void a(TuSdkRecorderVideoCamera.RecordState paramRecordState)
  {
    this.R = paramRecordState;
    if (this.J != null) {
      this.J.onMovieRecordStateChanged(paramRecordState);
    }
  }
  
  private void a(TuSdkStillCameraAdapter.CameraState paramCameraState)
  {
    this.Q = paramCameraState;
    if (this.K != null) {
      this.K.onVideoCameraStateChanged(paramCameraState);
    }
    if (paramCameraState == TuSdkStillCameraAdapter.CameraState.StateStarted) {
      StatisticsManger.appendComponent(9441280L);
    } else if (paramCameraState == TuSdkStillCameraAdapter.CameraState.StateUnknow) {
      StatisticsManger.appendComponent(9441281L);
    }
  }
  
  public TuSdkRecorderVideoCameraImpl(Context paramContext, RelativeLayout paramRelativeLayout, TuSdkRecorderCameraSetting paramTuSdkRecorderCameraSetting)
  {
    this.a = paramContext;
    this.b = paramRelativeLayout;
    this.d = paramTuSdkRecorderCameraSetting;
    initCamera();
    initView();
  }
  
  protected void initCamera()
  {
    a(TuSdkStillCameraAdapter.CameraState.StateUnknow);
    this.e = new TuSdkCameraImpl();
    this.e.setCameraListener(new TuSdkCamera.TuSdkCameraListener()
    {
      public void onStatusChanged(TuSdkCamera.TuSdkCameraStatus paramAnonymousTuSdkCameraStatus, TuSdkCamera paramAnonymousTuSdkCamera)
      {
        if (TuSdkRecorderVideoCameraImpl.this.getFocusTouchView() != null) {
          TuSdkRecorderVideoCameraImpl.this.getFocusTouchView().cameraStateChanged(TuSdkRecorderVideoCameraImpl.p(TuSdkRecorderVideoCameraImpl.this).canSupportAutoFocus(), TuSdkRecorderVideoCameraImpl.q(TuSdkRecorderVideoCameraImpl.this), paramAnonymousTuSdkCameraStatus);
        }
      }
    });
    this.m = new TuSdkCameraBuilderImpl();
    this.e.setCameraBuilder(this.m);
    this.n = new TuSdkCameraParametersImpl();
    this.e.setCameraParameters(this.n);
    this.p = new TuSdkCameraOrientationImpl();
    this.p.setHorizontallyMirrorFrontFacingCamera(true);
    this.p.setDeviceOrientListener(new TuSdkOrientationEventListener.TuSdkOrientationDelegate()new TuSdkOrientationEventListener.TuSdkOrientationDegreeDelegate
    {
      public void onOrientationChanged(InterfaceOrientation paramAnonymousInterfaceOrientation)
      {
        TuSdkRecorderVideoCameraImpl.a(TuSdkRecorderVideoCameraImpl.this, paramAnonymousInterfaceOrientation);
      }
    }, new TuSdkOrientationEventListener.TuSdkOrientationDegreeDelegate()
    {
      public void onOrientationDegreeChanged(int paramAnonymousInt) {}
    });
    this.e.setCameraOrientation(this.p);
    this.o = new TuSdkCameraFocusImpl();
    this.e.setCameraFocus(this.o);
    this.q = new TuSdkCameraSizeImpl();
    if (this.d.previewEffectScale != -1.0F) {
      this.q.setPreviewEffectScale(this.d.previewEffectScale);
    }
    if (this.d.previewMaxSize != -1) {
      this.q.setPreviewMaxSize(this.d.previewMaxSize);
    }
    if (this.d.previewRatio != -1.0F) {
      this.q.setPreviewRatio(this.d.previewRatio);
    }
    this.e.setCameraSize(this.q);
    this.x = new TuSdkCameraShotImpl();
    this.x.setShotListener(this.ab);
    this.x.setDetectionImageFace(this.ac);
    this.x.setDetectionShotFilter(new TuSdkCameraShot.TuSdkCameraShotFilter()
    {
      public SelesOutInput getFilters(FaceAligment[] paramAnonymousArrayOfFaceAligment, SelesPicture paramAnonymousSelesPicture)
      {
        Object localObject1 = null;
        Object localObject2 = null;
        Iterator localIterator = TuSdkRecorderVideoCameraImpl.r(TuSdkRecorderVideoCameraImpl.this).getAllMediaEffectData().iterator();
        while (localIterator.hasNext())
        {
          final TuSdkMediaEffectData localTuSdkMediaEffectData1 = (TuSdkMediaEffectData)localIterator.next();
          final TuSdkMediaEffectData localTuSdkMediaEffectData2 = localTuSdkMediaEffectData1.clone();
          if ((localTuSdkMediaEffectData2 != null) && (localTuSdkMediaEffectData2.isVaild()))
          {
            final FilterWrap localFilterWrap = localTuSdkMediaEffectData2.getFilterWrap();
            if ((localTuSdkMediaEffectData2 instanceof TuSdkMediaStickerEffectData)) {
              paramAnonymousSelesPicture.mountAtGLThread(new Runnable()
              {
                public void run()
                {
                  LiveStickerPlayController localLiveStickerPlayController = new LiveStickerPlayController(SelesContext.currentEGLContext());
                  localLiveStickerPlayController.showGroupSticker(((TuSdkMediaStickerEffectData)localTuSdkMediaEffectData2).getStickerGroup());
                  if (!localTuSdkMediaEffectData2.isApplied())
                  {
                    ((SelesParameters.FilterStickerInterface)localTuSdkMediaEffectData2.getFilterWrap()).setStickerVisibility(true);
                    localTuSdkMediaEffectData2.setIsApplied(true);
                  }
                  ((SelesParameters.FilterStickerInterface)localTuSdkMediaEffectData2.getFilterWrap()).updateStickers(localLiveStickerPlayController.getStickers());
                  int[] arrayOfInt = ((Face2DComboFilterWrap)localTuSdkMediaEffectData1.getFilterWrap()).getMap2DCurrentStickerIndexs();
                  try
                  {
                    Thread.sleep(500L);
                    ((Face2DComboFilterWrap)localFilterWrap).setMap2DCurrentStickerIndex(arrayOfInt);
                  }
                  catch (InterruptedException localInterruptedException)
                  {
                    localInterruptedException.printStackTrace();
                  }
                }
              });
            }
            localFilterWrap.processImage();
            if ((localFilterWrap instanceof SelesParameters.FilterFacePositionInterface)) {
              ((SelesParameters.FilterFacePositionInterface)localFilterWrap).updateFaceFeatures(paramAnonymousArrayOfFaceAligment, 0.0F);
            }
            if ((localObject2 == null) && (localObject1 == null))
            {
              localObject2 = localObject1 = localFilterWrap;
            }
            else
            {
              ((FilterWrap)localObject2).getLastFilter().addTarget(localFilterWrap.getFilter(), 0);
              localObject2 = localFilterWrap;
            }
          }
        }
        if (localObject1 == null) {
          localObject1 = FilterWrap.creat(null);
        }
        return ((FilterWrap)localObject1).getFilter();
      }
    });
    this.e.setCameraShot(this.x);
    this.e.setSurfaceListener(new SurfaceTexture.OnFrameAvailableListener()
    {
      public void onFrameAvailable(SurfaceTexture paramAnonymousSurfaceTexture)
      {
        TuSdkRecorderVideoCameraImpl.s(TuSdkRecorderVideoCameraImpl.this).requestRender();
      }
    });
    this.n.setFlashMode(CameraConfigs.CameraFlash.On);
    this.e.prepare();
    this.f = new TuSdkFilterEngineImpl(false, true);
    this.f.setInputImageOrientation(ImageOrientation.Up);
    this.f.setOutputImageOrientation(ImageOrientation.Up);
    this.f.setCameraFacing(this.e.getFacing());
    this.f.setFilterChangedListener(this.X);
    this.f.setEnableLiveSticker(true);
    this.f.setFaceDetectionDelegate(this.Y);
    this.f.setMediaEffectDelegate(this.aa);
  }
  
  private void a()
  {
    this.g = new TuSdkCameraRecorder();
    this.g.appendRecordSurface(this.e);
    this.g.setRecordListener(this.W);
    this.g.setSurfaceRender(new TuSdkSurfaceRender()
    {
      public void onSurfaceCreated() {}
      
      public void onSurfaceChanged(int paramAnonymousInt1, int paramAnonymousInt2) {}
      
      public void onSurfaceDestory() {}
      
      public int onDrawFrame(int paramAnonymousInt1, int paramAnonymousInt2, int paramAnonymousInt3, long paramAnonymousLong)
      {
        int i = TuSdkRecorderVideoCameraImpl.r(TuSdkRecorderVideoCameraImpl.this).processFrame(paramAnonymousInt1, paramAnonymousInt2, paramAnonymousInt3, paramAnonymousLong);
        return i;
      }
      
      public void onDrawFrameCompleted() {}
    });
    this.g.addTarget(this.c, 0);
  }
  
  protected void initView()
  {
    this.c = new SelesSmartView(this.a);
    this.c.setRenderer(new GLSurfaceView.Renderer()
    {
      public void onSurfaceCreated(GL10 paramAnonymousGL10, EGLConfig paramAnonymousEGLConfig)
      {
        GLES20.glDisable(2929);
        TuSdkRecorderVideoCameraImpl.t(TuSdkRecorderVideoCameraImpl.this).initInGLThread();
        TuSdkRecorderVideoCameraImpl.r(TuSdkRecorderVideoCameraImpl.this).onSurfaceCreated();
      }
      
      public void onSurfaceChanged(GL10 paramAnonymousGL10, int paramAnonymousInt1, int paramAnonymousInt2)
      {
        GLES20.glViewport(0, 0, paramAnonymousInt1, paramAnonymousInt2);
        TuSdkRecorderVideoCameraImpl.r(TuSdkRecorderVideoCameraImpl.this).onSurfaceChanged(paramAnonymousInt1, paramAnonymousInt2);
      }
      
      public void onDrawFrame(GL10 paramAnonymousGL10)
      {
        GLES20.glClear(16640);
        TuSdkRecorderVideoCameraImpl.t(TuSdkRecorderVideoCameraImpl.this).newFrameReadyInGLThread();
      }
    });
    RelativeLayout.LayoutParams localLayoutParams = new RelativeLayout.LayoutParams(-1, -1);
    this.b.addView(this.c, 0, localLayoutParams);
    a();
    TuSdkVideoFocusTouchView localTuSdkVideoFocusTouchView = new TuSdkVideoFocusTouchView(this.a);
    setFocusTouchView(localTuSdkVideoFocusTouchView);
  }
  
  private void a(long paramLong, TuSdkMediaRecordHub.TuSdkMediaRecordHubStatus paramTuSdkMediaRecordHubStatus)
  {
    if (paramTuSdkMediaRecordHubStatus == TuSdkMediaRecordHub.TuSdkMediaRecordHubStatus.START_RECORD)
    {
      this.E = paramLong;
      if (this.H != null) {
        return;
      }
      this.H = new TuSdkTimeRange();
      this.H.setStartTimeUs(this.E);
    }
    else if (paramTuSdkMediaRecordHubStatus == TuSdkMediaRecordHub.TuSdkMediaRecordHubStatus.PAUSE_RECORD)
    {
      this.F = paramLong;
      if (this.H == null) {
        return;
      }
      this.H.setEndTimeUs(this.F);
      synchronized (this.B)
      {
        this.C.add(this.H);
      }
      this.H = null;
    }
  }
  
  private void a(long paramLong)
  {
    long l1 = paramLong - c();
    this.I = l1;
    if (b()) {
      return;
    }
    float f1 = a((float)this.I / 1000000.0F);
    float f2 = f1 / this.A;
    if (this.J != null) {
      this.J.onMovieRecordProgressChanged(f2, f1);
    }
  }
  
  private boolean b()
  {
    if ((this.I >= this.A * 1000000) && (this.G == TuSdkMediaRecordHub.TuSdkMediaRecordHubStatus.START_RECORD))
    {
      pauseRecording();
      return true;
    }
    return false;
  }
  
  private float a(float paramFloat)
  {
    DecimalFormat localDecimalFormat = new DecimalFormat(".00");
    return Float.valueOf(localDecimalFormat.format(paramFloat)).floatValue();
  }
  
  private long c()
  {
    long l1 = 0L;
    Iterator localIterator = this.D.iterator();
    while (localIterator.hasNext())
    {
      TuSdkTimeRange localTuSdkTimeRange = (TuSdkTimeRange)localIterator.next();
      l1 += Math.abs(localTuSdkTimeRange.getEndTimeUS() - localTuSdkTimeRange.getStartTimeUS());
    }
    return l1;
  }
  
  public boolean isDirectEdit()
  {
    return this.V;
  }
  
  public void enableDirectEdit(boolean paramBoolean)
  {
    this.V = paramBoolean;
  }
  
  public void setVideoEncoderSetting(TuSdkRecorderVideoEncoderSetting paramTuSdkRecorderVideoEncoderSetting)
  {
    this.h = paramTuSdkRecorderVideoEncoderSetting;
    if ((!SdkValid.shared.videoCameraBitrateEnabled()) && (paramTuSdkRecorderVideoEncoderSetting != null))
    {
      TLog.e("You are not allowed to change camera bitrate, please see http://tusdk.com", new Object[0]);
      this.h.videoQuality = TuSdkVideoQuality.safeQuality();
      return;
    }
  }
  
  public TuSdkRecorderVideoEncoderSetting getVideoEncoderSetting()
  {
    if (this.h == null) {
      this.h = new TuSdkRecorderVideoEncoderSetting();
    }
    if ((this.h.videoSize == null) || (!this.h.videoSize.isSize())) {
      this.h.videoSize = getOutputImageSize();
    }
    return this.h;
  }
  
  public void setRecorderVideoCameraCallback(TuSdkRecorderVideoCamera.TuSdkRecorderVideoCameraCallback paramTuSdkRecorderVideoCameraCallback)
  {
    this.J = paramTuSdkRecorderVideoCameraCallback;
  }
  
  public void setCameraListener(TuSdkRecorderVideoCamera.TuSdkCameraListener paramTuSdkCameraListener)
  {
    this.K = paramTuSdkCameraListener;
  }
  
  public TuSdkStillCameraAdapter.CameraState getCameraState()
  {
    return null;
  }
  
  public TuSdkRecorderVideoCamera.RecordState getRecordState()
  {
    return this.R;
  }
  
  public void setFaceDetectionCallback(TuSdkRecorderVideoCamera.TuSdkFaceDetectionCallback paramTuSdkFaceDetectionCallback)
  {
    this.L = paramTuSdkFaceDetectionCallback;
  }
  
  public void setMediaEffectChangeListener(TuSdkRecorderVideoCamera.TuSdkMediaEffectChangeListener paramTuSdkMediaEffectChangeListener)
  {
    this.M = paramTuSdkMediaEffectChangeListener;
  }
  
  public void setEnableLiveSticker(boolean paramBoolean)
  {
    if (this.f == null) {
      return;
    }
    this.f.setEnableLiveSticker(paramBoolean);
  }
  
  public void setEnableFaceDetection(boolean paramBoolean)
  {
    if (this.f == null) {
      return;
    }
    this.f.setEnableFaceDetection(paramBoolean);
  }
  
  public void setWaterMarkImage(Bitmap paramBitmap)
  {
    this.k = paramBitmap;
  }
  
  public void setWaterMarkPosition(TuSdkWaterMarkOption.WaterMarkPosition paramWaterMarkPosition)
  {
    this.l = paramWaterMarkPosition;
  }
  
  public void setFlashMode(CameraConfigs.CameraFlash paramCameraFlash)
  {
    this.n.setFlashMode(paramCameraFlash);
  }
  
  public CameraConfigs.CameraFlash getFlashMode()
  {
    return this.n.getFlashMode();
  }
  
  public boolean canSupportFlash()
  {
    return this.n.canSupportFlash();
  }
  
  public boolean isFrontFacingCameraPresent()
  {
    return this.e.getFacing() == CameraConfigs.CameraFacing.Front;
  }
  
  public boolean isBackFacingCameraPresent()
  {
    return this.e.getFacing() == CameraConfigs.CameraFacing.Back;
  }
  
  public void setFocusMode(CameraConfigs.CameraAutoFocus paramCameraAutoFocus, PointF paramPointF)
  {
    this.o.setFocusMode(paramCameraAutoFocus, paramPointF);
  }
  
  public boolean canSupportAutoFocus()
  {
    return this.o.canSupportAutoFocus();
  }
  
  public void setAntibandingMode(CameraConfigs.CameraAntibanding paramCameraAntibanding)
  {
    this.n.setAntibandingMode(paramCameraAntibanding);
  }
  
  public void setDisableContinueFocus(boolean paramBoolean)
  {
    this.o.setDisableContinueFoucs(paramBoolean);
    if (this.O != null) {
      this.O.setDisableContinueFoucs(paramBoolean);
    }
  }
  
  public void autoFocus(CameraConfigs.CameraAutoFocus paramCameraAutoFocus, PointF paramPointF, TuSdkCameraFocus.TuSdkCameraFocusListener paramTuSdkCameraFocusListener)
  {
    this.o.autoFocus(paramCameraAutoFocus, paramPointF, paramTuSdkCameraFocusListener);
  }
  
  public void autoFocus(TuSdkCameraFocus.TuSdkCameraFocusListener paramTuSdkCameraFocusListener)
  {
    this.o.autoFocus(paramTuSdkCameraFocusListener);
  }
  
  public TuSdkVideoFocusTouchView getFocusTouchView()
  {
    return this.O;
  }
  
  protected void setFocusTouchView(TuSDKVideoCameraFocusViewInterface paramTuSDKVideoCameraFocusViewInterface)
  {
    this.O = ((TuSdkVideoFocusTouchView)paramTuSDKVideoCameraFocusViewInterface);
    if ((paramTuSDKVideoCameraFocusViewInterface == null) || (this.c == null)) {
      return;
    }
    if (this.O != null)
    {
      this.c.removeView(this.O);
      this.O.viewWillDestory();
    }
    this.O.setCamera(this);
    this.O.setDisableFocusBeep(true);
    this.O.setDisableContinueFoucs(this.v);
    this.O.setGuideLineViewState(false);
    this.O.setRegionPercent(getRegionHandler().getRectPercent());
    this.c.addView(this.O, new RelativeLayout.LayoutParams(-1, -1));
  }
  
  public RegionHandler getRegionHandler()
  {
    if (this.N == null) {
      this.N = new RegionDefaultHandler();
    }
    this.N.setWrapSize(ViewSize.create(this.b));
    return this.N;
  }
  
  public void changeRegionRatio(float paramFloat)
  {
    if (!canChangeRatio()) {
      return;
    }
    this.P = paramFloat;
    this.e.setShotRegionRatio(paramFloat);
    getRegionHandler().changeWithRatio(this.P, new RegionHandler.RegionChangerListener()
    {
      public void onRegionChanged(RectF paramAnonymousRectF)
      {
        if (TuSdkRecorderVideoCameraImpl.s(TuSdkRecorderVideoCameraImpl.this) != null) {
          TuSdkRecorderVideoCameraImpl.s(TuSdkRecorderVideoCameraImpl.this).setDisplayRect(paramAnonymousRectF);
        }
        if (TuSdkRecorderVideoCameraImpl.this.getFocusTouchView() != null) {
          TuSdkRecorderVideoCameraImpl.this.getFocusTouchView().setRegionPercent(paramAnonymousRectF);
        }
        TuSdkRecorderVideoCameraImpl.u(TuSdkRecorderVideoCameraImpl.this);
      }
    });
  }
  
  private void d()
  {
    if (this.f != null) {
      return;
    }
    this.f.setDisplayRect(null, getRegionRatio());
  }
  
  protected float getRegionRatio()
  {
    if (this.P == 0.0F) {
      return getCameraPreviewSize().getRatioFloat();
    }
    return this.P;
  }
  
  public void setRegionRatio(float paramFloat)
  {
    if (!canChangeRatio()) {
      return;
    }
    this.P = paramFloat;
    this.e.setShotRegionRatio(this.P);
    if (this.N != null) {
      this.N.setRatio(this.P);
    }
  }
  
  public boolean canChangeRatio()
  {
    return this.G != TuSdkMediaRecordHub.TuSdkMediaRecordHubStatus.PAUSE_RECORD;
  }
  
  public TuSdkSize getCameraPreviewSize()
  {
    return this.q.getOutputSize();
  }
  
  public void rotateCamera()
  {
    this.e.rotateCamera();
    StatisticsManger.appendComponent(isFrontFacingCameraPresent() ? 9441314L : 9441313L);
  }
  
  public void startCameraCapture()
  {
    a(TuSdkStillCameraAdapter.CameraState.StateStarting);
    this.e.startPreview(this.d.facing);
    a(TuSdkStillCameraAdapter.CameraState.StateStarted);
  }
  
  public void pauseCameraCapture()
  {
    this.e.pausePreview();
  }
  
  public void resumeCameraCapture()
  {
    this.e.resumePreview();
    a(TuSdkStillCameraAdapter.CameraState.StateStarted);
  }
  
  public void stopCameraCapture()
  {
    this.e.stopPreview();
    a(TuSdkStillCameraAdapter.CameraState.StateUnknow);
  }
  
  public InterfaceOrientation getDeviceOrient()
  {
    return this.r;
  }
  
  public TuSdkSize getOutputImageSize()
  {
    return this.q.getOutputSize();
  }
  
  public void setFaceDetectionDelegate(TuSdkCameraFocus.TuSdkCameraFocusFaceListener paramTuSdkCameraFocusFaceListener)
  {
    this.o.setFaceListener(paramTuSdkCameraFocusFaceListener);
  }
  
  public boolean addMediaEffectData(TuSdkMediaEffectData paramTuSdkMediaEffectData)
  {
    if (((paramTuSdkMediaEffectData instanceof TuSdkMediaSceneEffectData)) || ((paramTuSdkMediaEffectData instanceof TuSdkMediaParticleEffectData)))
    {
      TLog.e("Invalid filter code , please contact us via http://tusdk.com", new Object[0]);
      return false;
    }
    return this.f.addMediaEffectData(paramTuSdkMediaEffectData);
  }
  
  public boolean removeMediaEffectData(TuSdkMediaEffectData paramTuSdkMediaEffectData)
  {
    return this.f.removeMediaEffectData(paramTuSdkMediaEffectData);
  }
  
  public <T extends TuSdkMediaEffectData> List<T> mediaEffectsWithType(TuSdkMediaEffectData.TuSdkMediaEffectDataType paramTuSdkMediaEffectDataType)
  {
    return this.f.mediaEffectsWithType(paramTuSdkMediaEffectDataType);
  }
  
  public List<TuSdkMediaEffectData> getAllMediaEffectData()
  {
    return this.f.getAllMediaEffectData();
  }
  
  public void removeMediaEffectsWithType(TuSdkMediaEffectData.TuSdkMediaEffectDataType paramTuSdkMediaEffectDataType)
  {
    this.f.removeMediaEffectsWithType(paramTuSdkMediaEffectDataType);
  }
  
  public void removeAllMediaEffects()
  {
    this.f.removeAllMediaEffects();
  }
  
  public void setEnableAudioCapture(boolean paramBoolean)
  {
    this.u = paramBoolean;
  }
  
  public boolean isEnableAudioCapture()
  {
    return this.u;
  }
  
  public void setSoundPitchType(TuSdkAudioPitchEngine.TuSdkSoundPitchType paramTuSdkSoundPitchType)
  {
    this.w = paramTuSdkSoundPitchType;
    this.g.changePitch(paramTuSdkSoundPitchType.getPitch());
  }
  
  public void captureImage()
  {
    a(TuSdkStillCameraAdapter.CameraState.StateCapturing);
    this.e.shotPhoto();
  }
  
  public void startRecording()
  {
    if (!g())
    {
      TLog.e("TuSdkRecorderVideoCamera | There is no space available for your device （Min %dM is required）", new Object[] { Long.valueOf(getMinAvailableSpaceBytes() / 1024L / 1024L) });
      if (this.J != null) {
        this.J.onMovieRecordFailed(TuSdkRecorderVideoCamera.RecordError.NotEnoughSpace);
      }
      return;
    }
    if ((this.j == null) && (this.k != null)) {
      this.j = new SelesWatermarkImpl(true);
    }
    this.j.setImage(this.k, false);
    this.j.setWaterPostion(this.l == null ? TuSdkWaterMarkOption.WaterMarkPosition.TopRight : this.l);
    this.j.setScale(0.09F);
    this.g.setWatermark(this.j);
    if (this.S)
    {
      resumeRecording();
      return;
    }
    this.S = true;
    this.g.setOutputVideoFormat(e());
    if (isEnableAudioCapture()) {
      this.g.setOutputAudioFormat(f());
    }
    this.g.start(getMovieOutputPath());
  }
  
  private MediaFormat e()
  {
    MediaFormat localMediaFormat = TuSdkMediaFormat.buildSafeVideoEncodecFormat(getVideoEncoderSetting().videoSize, getVideoEncoderSetting().videoQuality, 2130708361, (getVideoEncoderSetting().enableAllKeyFrame) || (isDirectEdit()) ? 0 : getVideoEncoderSetting().mediacodecAVCIFrameInterval);
    return localMediaFormat;
  }
  
  private MediaFormat f()
  {
    MediaFormat localMediaFormat = TuSdkMediaFormat.buildSafeAudioEncodecFormat();
    return localMediaFormat;
  }
  
  public void resumeRecording()
  {
    if (this.I > getMaxRecordingTime() * 1000000)
    {
      TLog.w("Record more max duration , current : %s   max : %s ", new Object[] { Long.valueOf(this.I), Integer.valueOf(getMaxRecordingTime()) });
      this.J.onMovieRecordFailed(TuSdkRecorderVideoCamera.RecordError.MoreMaxDuration);
      return;
    }
    this.g.resume();
  }
  
  public void pauseRecording()
  {
    this.g.pause();
  }
  
  public void stopRecording()
  {
    if (this.I < getMinRecordingTime() * 1000000)
    {
      TLog.w("Record less min recording time , current : %s   min : %s ", new Object[] { Long.valueOf(this.I), Integer.valueOf(getMinRecordingTime()) });
      this.J.onMovieRecordFailed(TuSdkRecorderVideoCamera.RecordError.LessMinRecordingTime);
      return;
    }
    this.g.stop();
  }
  
  public boolean isRecording()
  {
    return (this.g.getState() == TuSdkMediaRecordHub.TuSdkMediaRecordHubStatus.START) || (this.g.getState() == TuSdkMediaRecordHub.TuSdkMediaRecordHubStatus.START_RECORD);
  }
  
  private boolean g()
  {
    if ((this.a == null) || (!FileHelper.mountedExternalStorage())) {
      return false;
    }
    File localFile = getMovieOutputPath();
    if (localFile == null)
    {
      TLog.e("TuSDKRecordVideoCamera | Create movie output file failed", new Object[0]);
      return true;
    }
    boolean bool = FileHelper.getAvailableStore(localFile.getParent()) > getMinAvailableSpaceBytes();
    return bool;
  }
  
  public void setMinAvailableSpaceBytes(long paramLong)
  {
    this.i = paramLong;
  }
  
  public long getMinAvailableSpaceBytes()
  {
    return this.i;
  }
  
  public void setSaveToAlbum(boolean paramBoolean)
  {
    this.t = paramBoolean;
  }
  
  public boolean isSaveToAlbum()
  {
    return this.t;
  }
  
  public void setSaveToAlbumName(String paramString)
  {
    this.y = paramString;
  }
  
  public String getSaveToAlbumName()
  {
    return this.y;
  }
  
  public void setMinRecordingTime(int paramInt)
  {
    this.z = paramInt;
  }
  
  public int getMinRecordingTime()
  {
    return this.z;
  }
  
  public void setMaxRecordingTime(int paramInt)
  {
    this.A = paramInt;
  }
  
  public int getMaxRecordingTime()
  {
    return this.A;
  }
  
  public void setSpeedMode(TuSdkRecorderVideoCamera.SpeedMode paramSpeedMode)
  {
    this.g.changeSpeed(paramSpeedMode.getSpeedRate());
  }
  
  public float getMovieDuration()
  {
    return a((float)this.I / 1000000.0F);
  }
  
  /* Error */
  public int getRecordingFragmentSize()
  {
    // Byte code:
    //   0: aload_0
    //   1: getfield 164	org/lasque/tusdk/core/utils/hardware/TuSdkRecorderVideoCameraImpl:B	Ljava/lang/Object;
    //   4: dup
    //   5: astore_1
    //   6: monitorenter
    //   7: aload_0
    //   8: getfield 165	org/lasque/tusdk/core/utils/hardware/TuSdkRecorderVideoCameraImpl:C	Ljava/util/LinkedList;
    //   11: invokevirtual 248	java/util/LinkedList:size	()I
    //   14: aload_1
    //   15: monitorexit
    //   16: ireturn
    //   17: astore_2
    //   18: aload_1
    //   19: monitorexit
    //   20: aload_2
    //   21: athrow
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	22	0	this	TuSdkRecorderVideoCameraImpl
    //   5	14	1	Ljava/lang/Object;	Object
    //   17	4	2	localObject1	Object
    // Exception table:
    //   from	to	target	type
    //   7	16	17	finally
    //   17	20	17	finally
  }
  
  public ArrayList<TuSdkMediaTimeSlice> getRecordTimeSlice()
  {
    ArrayList localArrayList = new ArrayList();
    Iterator localIterator = this.C.iterator();
    while (localIterator.hasNext())
    {
      TuSdkTimeRange localTuSdkTimeRange = (TuSdkTimeRange)localIterator.next();
      localArrayList.add(new TuSdkMediaTimeSlice(localTuSdkTimeRange.getStartTimeUS(), localTuSdkTimeRange.getEndTimeUS()));
    }
    return localArrayList;
  }
  
  public TuSdkTimeRange popVideoFragment()
  {
    synchronized (this.B)
    {
      if (this.C.size() == 0) {
        return null;
      }
      TuSdkTimeRange localTuSdkTimeRange = (TuSdkTimeRange)this.C.removeLast();
      this.D.add(localTuSdkTimeRange);
      this.I -= Math.abs(localTuSdkTimeRange.getEndTimeUS() - localTuSdkTimeRange.getStartTimeUS());
      StatisticsManger.appendComponent(9441296L);
      return localTuSdkTimeRange;
    }
  }
  
  public void cancelRecording()
  {
    synchronized (this.B)
    {
      this.C.clear();
      this.D.clear();
      h();
      a(TuSdkRecorderVideoCamera.RecordState.Canceled);
    }
  }
  
  public TuSdkTimeRange lastVideoFragmentRange()
  {
    if (this.C.size() == 0) {
      return null;
    }
    return (TuSdkTimeRange)this.C.getLast();
  }
  
  public File getMovieOutputPath()
  {
    if (this.s == null) {
      this.s = i();
    }
    return this.s;
  }
  
  private void h()
  {
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
  
  public void destroy()
  {
    this.e.release();
    this.J = null;
    this.a = null;
    this.b = null;
    this.C.clear();
    this.D.clear();
    this.f.release();
  }
  
  private File i()
  {
    if (isSaveToAlbum()) {
      return j();
    }
    return new File(TuSdk.getAppTempPath(), String.format("lsq_%s.mp4", new Object[] { StringHelper.timeStampString() }));
  }
  
  private File j()
  {
    if (StringHelper.isNotBlank(getSaveToAlbumName())) {
      return AlbumHelper.getAlbumVideoFile(getSaveToAlbumName());
    }
    return AlbumHelper.getAlbumVideoFile();
  }
  
  public void setDetectScale(float paramFloat)
  {
    FrameDetectProcessor.setDetectScale(paramFloat);
  }
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKVideo-3.4.1.jar!\org\lasque\tusdk\core\utils\hardware\TuSdkRecorderVideoCameraImpl.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */