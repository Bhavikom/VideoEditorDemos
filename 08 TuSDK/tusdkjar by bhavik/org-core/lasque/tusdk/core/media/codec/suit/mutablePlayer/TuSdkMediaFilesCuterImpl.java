package org.lasque.tusdk.core.media.codec.suit.mutablePlayer;

import android.annotation.TargetApi;
import android.graphics.RectF;
import android.graphics.SurfaceTexture;
import android.graphics.SurfaceTexture.OnFrameAvailableListener;
import android.media.MediaCodec.BufferInfo;
import android.media.MediaFormat;
import android.view.Surface;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;
import org.lasque.tusdk.core.api.extend.TuSdkMediaProgress;
import org.lasque.tusdk.core.media.codec.TuSdkMediaMuxer;
import org.lasque.tusdk.core.media.codec.audio.TuSdkAudioEncodecOperation;
import org.lasque.tusdk.core.media.codec.audio.TuSdkAudioInfo;
import org.lasque.tusdk.core.media.codec.audio.TuSdkAudioPitch;
import org.lasque.tusdk.core.media.codec.audio.TuSdkAudioPitchHardImpl;
import org.lasque.tusdk.core.media.codec.audio.TuSdkAudioResample;
import org.lasque.tusdk.core.media.codec.audio.TuSdkAudioResampleHardImpl;
import org.lasque.tusdk.core.media.codec.encoder.TuSdkEncodeSurface;
import org.lasque.tusdk.core.media.codec.encoder.TuSdkEncoderListener;
import org.lasque.tusdk.core.media.codec.encoder.TuSdkMediaFileEncoder;
import org.lasque.tusdk.core.media.codec.encoder.TuSdkMediaFileSuitEncoderBase;
import org.lasque.tusdk.core.media.codec.encoder.TuSdkVideoSurfaceEncoderListener;
import org.lasque.tusdk.core.media.codec.encoder.TuSdkVideoSurfaceEncoderListenerImpl;
import org.lasque.tusdk.core.media.codec.extend.TuSdkCodecCapabilities;
import org.lasque.tusdk.core.media.codec.extend.TuSdkMediaFormat;
import org.lasque.tusdk.core.media.codec.extend.TuSdkMediaTimeSlice;
import org.lasque.tusdk.core.media.codec.sync.TuSdkAudioEncodecSync;
import org.lasque.tusdk.core.media.codec.sync.TuSdkAudioEncodecSyncBase;
import org.lasque.tusdk.core.media.codec.sync.TuSdkAudioPitchSync;
import org.lasque.tusdk.core.media.codec.sync.TuSdkAudioResampleSync;
import org.lasque.tusdk.core.media.codec.sync.TuSdkVideoEncodecSync;
import org.lasque.tusdk.core.media.codec.sync.TuSdkVideoEncodecSyncBase;
import org.lasque.tusdk.core.media.codec.video.TuSdkVideoInfo;
import org.lasque.tusdk.core.media.record.TuSdkRecordSurface;
import org.lasque.tusdk.core.seles.extend.SelesVerticeCoordinateCorpBuilder;
import org.lasque.tusdk.core.seles.extend.SelesVerticeCoordinateCropBuilderImpl;
import org.lasque.tusdk.core.seles.sources.SelesSurfaceReceiver;
import org.lasque.tusdk.core.struct.TuSdkMediaDataSource;
import org.lasque.tusdk.core.struct.TuSdkSize;
import org.lasque.tusdk.core.utils.TLog;
import org.lasque.tusdk.core.utils.ThreadHelper;

@TargetApi(18)
public class TuSdkMediaFilesCuterImpl
  extends TuSdkMediaFileSuitEncoderBase
  implements TuSdkMediaFilesCuter
{
  private final TuSdkMediaFilesCuterSync a = new TuSdkMediaFilesCuterSync();
  private final SelesVerticeCoordinateCorpBuilder b = new SelesVerticeCoordinateCropBuilderImpl(false);
  private AudioRender c = new AudioRender(null);
  private VideoRender d = new VideoRender();
  private TuSdkMediaTimeSlice e = new TuSdkMediaTimeSlice(0L, Long.MAX_VALUE);
  private List<AVAsset> f = new ArrayList(2);
  private boolean g;
  private AVAssetTrackOutputSouce h;
  private AVAssetTrackCodecDecoder i;
  private AVAssetTrackOutputSouce j;
  private AVAssetTrackCodecDecoder k;
  private Object l = new Object();
  private long m = 0L;
  private boolean n = false;
  private SelesSurfaceReceiver o;
  private TuSdkVideoSurfaceEncoderListener p = new TuSdkVideoSurfaceEncoderListenerImpl()
  {
    public void onSurfaceCreated(GL10 paramAnonymousGL10, EGLConfig paramAnonymousEGLConfig)
    {
      TuSdkMediaFilesCuterImpl.this.initInGLThread();
    }
    
    public void onEncoderDrawFrame(long paramAnonymousLong, boolean paramAnonymousBoolean)
    {
      long l = TuSdkMediaFilesCuterImpl.a(TuSdkMediaFilesCuterImpl.this, TuSdkMediaFilesCuterImpl.n(TuSdkMediaFilesCuterImpl.this).outputTimeUs());
      TuSdkMediaFilesCuterImpl.e(TuSdkMediaFilesCuterImpl.this).syncVideoEncodecDrawFrame(l * 1000L, false, TuSdkMediaFilesCuterImpl.c(TuSdkMediaFilesCuterImpl.this), TuSdkMediaFilesCuterImpl.p(TuSdkMediaFilesCuterImpl.this).getVideoEncoder());
    }
    
    public void onEncoderUpdated(MediaCodec.BufferInfo paramAnonymousBufferInfo)
    {
      if (TLog.LOG_VIDEO_ENCODEC_INFO) {
        TuSdkCodecCapabilities.logBufferInfo("VideoEncoderListener updated", paramAnonymousBufferInfo);
      }
      TuSdkMediaFilesCuterImpl.a(TuSdkMediaFilesCuterImpl.this, false);
    }
    
    public void onEncoderCompleted(Exception paramAnonymousException)
    {
      if (paramAnonymousException == null)
      {
        TLog.d("%s encodec Video updatedToEOS", new Object[] { "TuSdkMediaFileCuterImpl" });
        TuSdkMediaFilesCuterImpl.a(TuSdkMediaFilesCuterImpl.this, false);
      }
      else
      {
        TLog.e(paramAnonymousException, "%s VideoEncoderListener thread catch exception, The thread will exit.", new Object[] { "TuSdkMediaFileCuterImpl" });
      }
      TuSdkMediaFilesCuterImpl.a(TuSdkMediaFilesCuterImpl.this, paramAnonymousException);
    }
  };
  private TuSdkEncoderListener q = new TuSdkEncoderListener()
  {
    public void onEncoderUpdated(MediaCodec.BufferInfo paramAnonymousBufferInfo)
    {
      if (TLog.LOG_AUDIO_ENCODEC_INFO) {
        TuSdkCodecCapabilities.logBufferInfo("AudioEncoderListener updated", paramAnonymousBufferInfo);
      }
    }
    
    public void onEncoderCompleted(Exception paramAnonymousException)
    {
      if (paramAnonymousException == null)
      {
        TLog.d("%s encodec Audio updatedToEOS", new Object[] { "TuSdkMediaFileCuterImpl" });
        TuSdkMediaFilesCuterImpl.a(TuSdkMediaFilesCuterImpl.this, false);
      }
      else
      {
        TLog.e(paramAnonymousException, "%s AudioEncoderListener thread catch exception, The thread will exit.", new Object[] { "TuSdkMediaFileCuterImpl" });
      }
      TuSdkMediaFilesCuterImpl.a(TuSdkMediaFilesCuterImpl.this, paramAnonymousException);
    }
  };
  
  public final int maxInputSize()
  {
    return 9;
  }
  
  public final void setMediaDataSource(TuSdkMediaDataSource paramTuSdkMediaDataSource)
  {
    setMediaDataSources(Arrays.asList(new TuSdkMediaDataSource[] { paramTuSdkMediaDataSource }));
  }
  
  public final void setMediaDataSources(List<TuSdkMediaDataSource> paramList)
  {
    if ((paramList == null) || (paramList.size() == 0))
    {
      TLog.w("%s setMediaDataSource not exists: %s", new Object[] { "TuSdkMediaFileCuterImpl", paramList });
      return;
    }
    if (paramList.size() > maxInputSize()) {
      TLog.w("The maximum number of video supported is %d", new Object[] { Integer.valueOf(maxInputSize()) });
    }
    List<TuSdkMediaDataSource> localList = paramList.size() > maxInputSize() ? paramList.subList(0, 8) : paramList;
    Iterator localIterator = localList.iterator();
    while (localIterator.hasNext())
    {
      TuSdkMediaDataSource localTuSdkMediaDataSource = (TuSdkMediaDataSource)localIterator.next();
      if (!localTuSdkMediaDataSource.isValid())
      {
        TLog.e("%s :This data source is invalid", new Object[] { localTuSdkMediaDataSource });
      }
      else
      {
        AVAssetDataSource localAVAssetDataSource = new AVAssetDataSource(localTuSdkMediaDataSource);
        if (localAVAssetDataSource.tracksWithMediaType(AVMediaType.AVMediaTypeVideo).size() == localAVAssetDataSource.tracksWithMediaType(AVMediaType.AVMediaTypeAudio).size()) {
          this.f.add(localAVAssetDataSource);
        }
      }
    }
    d();
  }
  
  private boolean a()
  {
    return (getOutputAudioInfo() != null) && (this.g);
  }
  
  public void setEnableAudioCheck(boolean paramBoolean)
  {
    this.n = paramBoolean;
  }
  
  public int setOutputAudioFormat(MediaFormat paramMediaFormat)
  {
    if (this.f.size() == 0)
    {
      TLog.e("SetOutputAudioFormat must be called after entering a valid file.", new Object[0]);
      return -1;
    }
    if ((this.g) && (paramMediaFormat != null))
    {
      int i1 = TuSdkMediaFormat.getAudioChannelCount(paramMediaFormat);
      int i2 = TuSdkMediaFormat.getAudioChannelCount(this.j.inputTrack().mediaFormat());
      int i3 = TuSdkMediaFormat.getAudioSampleRate(paramMediaFormat);
      int i4 = TuSdkMediaFormat.getAudioSampleRate(this.j.inputTrack().mediaFormat());
      if ((i1 != i2) || (i3 != i4))
      {
        paramMediaFormat.setInteger("channel-count", i2);
        paramMediaFormat.setInteger("sample-rate", i4);
        TLog.e("The number of audio channels is not supported when changing speed.", new Object[0]);
      }
      return super.setOutputAudioFormat(paramMediaFormat);
    }
    TLog.i("The input video file's not find an audio track", new Object[0]);
    return -1;
  }
  
  private void b()
  {
    this.mEncoder.requestVideoKeyFrame();
    VideoRender.a(this.d, new Runnable()
    {
      public void run()
      {
        TuSdkMediaFilesCuterImpl.VideoRender.a(TuSdkMediaFilesCuterImpl.a(TuSdkMediaFilesCuterImpl.this));
      }
    });
    if ((!this.n) && (a())) {
      AudioRender.a(this.c, new Runnable()
      {
        public void run()
        {
          TuSdkMediaFilesCuterImpl.AudioRender.a(TuSdkMediaFilesCuterImpl.b(TuSdkMediaFilesCuterImpl.this));
        }
      });
    }
  }
  
  public void setCropRect(RectF paramRectF)
  {
    if (paramRectF == null) {
      return;
    }
    this.b.setCropRect(paramRectF);
  }
  
  public void setTimeSlice(TuSdkMediaTimeSlice paramTuSdkMediaTimeSlice)
  {
    if (this.mState == 1)
    {
      TLog.w("%s already stoped.", new Object[] { "TuSdkMediaFileCuterImpl" });
      return;
    }
    if ((paramTuSdkMediaTimeSlice == null) || (paramTuSdkMediaTimeSlice.startUs < 0L))
    {
      TLog.e("%s Invalid slice. %s", new Object[] { "TuSdkMediaFileCuterImpl", paramTuSdkMediaTimeSlice });
      return;
    }
    if (paramTuSdkMediaTimeSlice.isReverse())
    {
      TLog.e("%s Reverse slicing is not supported now %s", new Object[] { "TuSdkMediaFileCuterImpl", paramTuSdkMediaTimeSlice });
      return;
    }
    this.e = paramTuSdkMediaTimeSlice;
    AVTimeRange localAVTimeRange = AVTimeRange.AVTimeRangeMake(this.e.startUs, this.e.endUs - this.e.startUs);
    if (this.h != null) {
      this.h.setTimeRange(localAVTimeRange);
    }
    if (this.j != null) {
      this.j.setTimeRange(localAVTimeRange);
    }
  }
  
  public boolean run(TuSdkMediaProgress paramTuSdkMediaProgress)
  {
    if ((this.f == null) || (this.f.size() == 0))
    {
      TLog.w("%s run need a input file path.", new Object[] { "TuSdkMediaFileCuterImpl" });
      return false;
    }
    return super.run(paramTuSdkMediaProgress);
  }
  
  public void stop()
  {
    if (this.mState == 1)
    {
      TLog.w("%s already stoped.", new Object[] { "TuSdkMediaFileCuterImpl" });
      return;
    }
    VideoRender.b(this.d, new Runnable()
    {
      public void run()
      {
        TuSdkMediaFilesCuterImpl.a(TuSdkMediaFilesCuterImpl.this, 1);
        if (TuSdkMediaFilesCuterImpl.c(TuSdkMediaFilesCuterImpl.this) != null)
        {
          TuSdkMediaFilesCuterImpl.c(TuSdkMediaFilesCuterImpl.this).destroy();
          TuSdkMediaFilesCuterImpl.a(TuSdkMediaFilesCuterImpl.this, null);
        }
        TuSdkMediaFilesCuterImpl.d(TuSdkMediaFilesCuterImpl.this).release();
        TuSdkMediaFilesCuterImpl.e(TuSdkMediaFilesCuterImpl.this).release();
      }
    });
    this.d.release();
    this.c.release();
  }
  
  private void a(boolean paramBoolean)
  {
    final float f1 = paramBoolean ? 1.0F : this.a.calculateProgress();
    ThreadHelper.post(new Runnable()
    {
      public void run()
      {
        if (TuSdkMediaFilesCuterImpl.f(TuSdkMediaFilesCuterImpl.this) == null) {
          return;
        }
        if ((TuSdkMediaFilesCuterImpl.g(TuSdkMediaFilesCuterImpl.this).inputTrack().getAsset() instanceof AVAssetDataSource))
        {
          AVAssetDataSource localAVAssetDataSource = (AVAssetDataSource)TuSdkMediaFilesCuterImpl.g(TuSdkMediaFilesCuterImpl.this).inputTrack().getAsset();
          TuSdkMediaFilesCuterImpl.i(TuSdkMediaFilesCuterImpl.this).onProgress(f1, localAVAssetDataSource.dataSource(), TuSdkMediaFilesCuterImpl.h(TuSdkMediaFilesCuterImpl.this).indexOf(localAVAssetDataSource.dataSource()), TuSdkMediaFilesCuterImpl.h(TuSdkMediaFilesCuterImpl.this).size());
        }
        else
        {
          TuSdkMediaFilesCuterImpl.j(TuSdkMediaFilesCuterImpl.this).onProgress(f1, null, -1, TuSdkMediaFilesCuterImpl.h(TuSdkMediaFilesCuterImpl.this).size());
        }
      }
    });
  }
  
  private void a(final Exception paramException)
  {
    if (paramException == null)
    {
      if (!this.a.isEncodecCompleted()) {
        return;
      }
      this.mEncoder.cleanTemp();
    }
    a(true);
    this.a.setBenchmarkEnd();
    ThreadHelper.post(new Runnable()
    {
      public void run()
      {
        TuSdkMediaFilesCuterImpl.this.stop();
        if (TuSdkMediaFilesCuterImpl.k(TuSdkMediaFilesCuterImpl.this) == null) {
          return;
        }
        TuSdkMediaFilesCuterImpl.m(TuSdkMediaFilesCuterImpl.this).onCompleted(paramException, TuSdkMediaFilesCuterImpl.l(TuSdkMediaFilesCuterImpl.this).getOutputDataSource(), 1);
      }
    });
    TLog.d("%s runCompleted: %f / %f", new Object[] { "TuSdkMediaFileCuterImpl", Float.valueOf((float)this.a.benchmarkUs() / 1000000.0F), Float.valueOf((float)this.a.totalDurationUs() / 1000000.0F) });
  }
  
  protected boolean _init()
  {
    if (!c())
    {
      TLog.w("%s init Encodec Environment failed.", new Object[] { "TuSdkMediaFileCuterImpl" });
      return false;
    }
    return true;
  }
  
  private boolean c()
  {
    this.b.setOutputSize(this.mEncoder.getOutputSize());
    this.o = new SelesSurfaceReceiver();
    this.o.setTextureCoordinateBuilder(this.b);
    this.o.addTarget(this.mEncoder.getFilterBridge(), 0);
    this.mEncoder.setSurfaceRender(this.mSurfaceRender);
    this.mEncoder.setAudioRender(this.mAudioRender);
    this.mEncoder.setMediaSync(this.a);
    this.mEncoder.setListener(this.p, this.q);
    boolean bool = this.mEncoder.prepare(null);
    return bool;
  }
  
  protected void initInGLThread()
  {
    if (this.o == null) {
      return;
    }
    this.o.initInGLThread();
    this.o.setSurfaceTextureListener(new SurfaceTexture.OnFrameAvailableListener()
    {
      public void onFrameAvailable(SurfaceTexture paramAnonymousSurfaceTexture)
      {
        TuSdkMediaFilesCuterImpl.o(TuSdkMediaFilesCuterImpl.this).requestVideoRender(TuSdkMediaFilesCuterImpl.a(TuSdkMediaFilesCuterImpl.this, TuSdkMediaFilesCuterImpl.n(TuSdkMediaFilesCuterImpl.this).outputTimeUs()));
      }
    });
    SurfaceTexture localSurfaceTexture = this.o.requestSurfaceTexture();
    Surface localSurface = new Surface(localSurfaceTexture);
    if (this.i != null) {
      this.i.setOutputSurface(localSurface);
    }
    b();
  }
  
  public TuSdkSize preferredOutputSize()
  {
    List localList = a(AVMediaType.AVMediaTypeVideo);
    if (localList.size() == 0) {
      return null;
    }
    TuSdkSize localTuSdkSize = ((AVAssetTrack)localList.get(0)).presentSize();
    if (Math.min(localTuSdkSize.width, localTuSdkSize.height) >= 1080) {
      return TuSdkSize.create((int)(localTuSdkSize.width * 0.5F), (int)(localTuSdkSize.height * 0.5F));
    }
    return localTuSdkSize;
  }
  
  public void setOutputRatio(float paramFloat, boolean paramBoolean)
  {
    if (this.o != null)
    {
      this.o.setOutputRatio(paramFloat);
      this.o.setEnableClip(paramBoolean);
    }
  }
  
  public void setOutputSize(TuSdkSize paramTuSdkSize, boolean paramBoolean)
  {
    if (this.o != null)
    {
      this.o.setOutputSize(paramTuSdkSize);
      this.o.setEnableClip(paramBoolean);
    }
  }
  
  private List<AVAssetTrack> a(AVMediaType paramAVMediaType)
  {
    ArrayList localArrayList = new ArrayList();
    Iterator localIterator = this.f.iterator();
    while (localIterator.hasNext())
    {
      AVAsset localAVAsset = (AVAsset)localIterator.next();
      List localList = localAVAsset.tracksWithMediaType(paramAVMediaType);
      localArrayList.addAll(localList);
    }
    return localArrayList;
  }
  
  private void d()
  {
    ArrayList localArrayList1 = new ArrayList(1);
    ArrayList localArrayList2 = new ArrayList(1);
    Object localObject = this.f.iterator();
    while (((Iterator)localObject).hasNext())
    {
      AVAsset localAVAsset = (AVAsset)((Iterator)localObject).next();
      List localList1 = localAVAsset.tracksWithMediaType(AVMediaType.AVMediaTypeVideo);
      List localList2 = localAVAsset.tracksWithMediaType(AVMediaType.AVMediaTypeAudio);
      if ((localList1.size() > 0) && (localList2.size() > 0))
      {
        localArrayList1.addAll(localList1);
        localArrayList2.addAll(localList2);
      }
    }
    if (localArrayList1.size() == 0)
    {
      TLog.e("%s No video tracks are available in the data source.", new Object[] { this });
      return;
    }
    localObject = null;
    if ((this.e.startUs >= 0L) && (this.e.endUs > this.e.startUs)) {
      localObject = AVTimeRange.AVTimeRangeMake(this.e.startUs, this.e.endUs - this.e.startUs);
    }
    this.h = new AVAssetTrackPipeMediaExtractor(localArrayList1);
    this.h.setTimeRange((AVTimeRange)localObject);
    this.i = new AVAssetTrackCodecDecoder(this.h);
    this.i.addTarget(this.d);
    this.g = (localArrayList2.size() > 0);
    if (this.g)
    {
      this.j = new AVAssetTrackPipeMediaExtractor(localArrayList2);
      this.j.setTimeRange((AVTimeRange)localObject);
      this.k = new AVAssetTrackCodecDecoder(this.j);
      this.k.addTarget(this.c);
    }
  }
  
  private void e()
  {
    this.mEncoder.signalVideoEndOfInputStream();
    if ((this.n) && (a())) {
      AudioRender.a(this.c, new Runnable()
      {
        public void run()
        {
          TuSdkMediaFilesCuterImpl.AudioRender.a(TuSdkMediaFilesCuterImpl.b(TuSdkMediaFilesCuterImpl.this));
        }
      });
    }
  }
  
  private void f()
  {
    long l1 = a(this.k.outputTimeUs());
    this.mEncoder.signalAudioEndOfInputStream(l1);
  }
  
  private long a(long paramLong)
  {
    return (1.0F / this.e.speed * (float)paramLong);
  }
  
  public class TuSdkMediaFilesCuterSync
    implements TuSdkMediaFilesSync
  {
    private long b = System.nanoTime();
    private long c;
    private boolean d = false;
    private _AudioEncodecSync e;
    private _VideoEncodecSync f;
    
    public TuSdkMediaFilesCuterSync() {}
    
    public TuSdkAudioEncodecSync getAudioEncodecSync()
    {
      if (this.e == null) {
        this.e = new _AudioEncodecSync(null);
      }
      return this.e;
    }
    
    public TuSdkVideoEncodecSync getVideoEncodecSync()
    {
      if (this.f == null) {
        this.f = new _VideoEncodecSync(null);
      }
      return this.f;
    }
    
    public void release()
    {
      if (this.d) {
        return;
      }
      this.d = true;
      if (this.e != null)
      {
        this.e.release();
        this.e = null;
      }
      if (this.f != null)
      {
        this.f.release();
        this.f = null;
      }
    }
    
    protected void finalize()
    {
      release();
      super.finalize();
    }
    
    public long benchmarkUs()
    {
      return this.c / 1000L;
    }
    
    public void setBenchmarkEnd()
    {
      this.c = (System.nanoTime() - this.b);
    }
    
    public long totalDurationUs()
    {
      return TuSdkMediaFilesCuterImpl.g(TuSdkMediaFilesCuterImpl.this).durationTimeUs();
    }
    
    public float calculateProgress()
    {
      float f1 = 0.0F;
      if (totalDurationUs() > 0L)
      {
        float f2 = (float)TuSdkMediaFilesCuterImpl.n(TuSdkMediaFilesCuterImpl.this).outputTimeUs() / (float)TuSdkMediaFilesCuterImpl.n(TuSdkMediaFilesCuterImpl.this).durationTimeUs();
        float f3 = TuSdkMediaFilesCuterImpl.B(TuSdkMediaFilesCuterImpl.this) ? (float)TuSdkMediaFilesCuterImpl.t(TuSdkMediaFilesCuterImpl.this).outputTimeUs() / (float)TuSdkMediaFilesCuterImpl.t(TuSdkMediaFilesCuterImpl.this).durationTimeUs() : 1.0F;
        f1 = (f2 + f3) / 2.0F;
      }
      return Math.min(Math.max(f1, 0.0F), 1.0F);
    }
    
    public boolean isEncodecCompleted()
    {
      return (isVideoEncodeCompleted()) && (isAudioEncodeCompleted());
    }
    
    public boolean isAudioEncodeCompleted()
    {
      if ((this.e == null) || (!TuSdkMediaFilesCuterImpl.B(TuSdkMediaFilesCuterImpl.this))) {
        return true;
      }
      return this.e.isAudioEncodeCompleted();
    }
    
    public boolean isVideoEncodeCompleted()
    {
      if (this.f == null) {
        return true;
      }
      return this.f.isVideoEncodeCompleted();
    }
    
    public void syncVideoEncodecDrawFrame(long paramLong, boolean paramBoolean, TuSdkRecordSurface paramTuSdkRecordSurface, TuSdkEncodeSurface paramTuSdkEncodeSurface)
    {
      if (this.f == null) {
        return;
      }
      this.f.syncVideoEncodecDrawFrame(paramLong, paramBoolean, paramTuSdkRecordSurface, paramTuSdkEncodeSurface);
      synchronized (TuSdkMediaFilesCuterImpl.q(TuSdkMediaFilesCuterImpl.this))
      {
        TuSdkMediaFilesCuterImpl.q(TuSdkMediaFilesCuterImpl.this).notify();
      }
    }
    
    private class _VideoEncodecSync
      extends TuSdkVideoEncodecSyncBase
    {
      private _VideoEncodecSync() {}
      
      public void syncVideoEncodecDrawFrame(long paramLong, boolean paramBoolean, TuSdkRecordSurface paramTuSdkRecordSurface, TuSdkEncodeSurface paramTuSdkEncodeSurface)
      {
        if ((paramTuSdkRecordSurface == null) || (paramTuSdkEncodeSurface == null) || (this.mReleased)) {
          return;
        }
        paramTuSdkRecordSurface.updateSurfaceTexImage();
        if (paramBoolean)
        {
          clearLocker();
          return;
        }
        long l1 = paramLong / 1000L;
        if (needSkip(l1))
        {
          unlockVideoTimestampUs(l1);
          this.mPreviousTimeUs = -1L;
          this.mFrameIntervalUs = 0L;
          return;
        }
        if (this.mPreviousTimeUs < 0L) {
          this.mPreviousTimeUs = l1;
        }
        this.mFrameIntervalUs = (l1 - this.mPreviousTimeUs);
        TuSdkMediaFilesCuterImpl.b(TuSdkMediaFilesCuterImpl.this, this.mFrameIntervalUs);
        this.mPreviousTimeUs = l1;
        long l2 = calculateEncodeTimestampUs(this.mFrameRates, this.mFrameCounts);
        if (l2 < 1L)
        {
          renderToEncodec(l2, l1, paramTuSdkRecordSurface, paramTuSdkEncodeSurface);
          return;
        }
        for (long l3 = l2 * 1000L; l2 < l1; l3 = l2 * 1000L)
        {
          lockVideoTimestampUs(l2);
          this.mLastTimeUs = l2;
          this.mFrameCounts += 1L;
          paramTuSdkEncodeSurface.duplicateFrameReadyInGLThread(l3);
          paramTuSdkEncodeSurface.swapBuffers(l3);
          l2 = calculateEncodeTimestampUs(this.mFrameRates, this.mFrameCounts);
        }
        if (isLastDecodeFrame(l1))
        {
          renderToEncodec(l2, l1, paramTuSdkRecordSurface, paramTuSdkEncodeSurface);
          return;
        }
        if ((l2 > l1) && (getInputIntervalUs() > 0L))
        {
          long l4 = l1 + getInputIntervalUs();
          if (l2 > l4)
          {
            unlockVideoTimestampUs(l1);
            return;
          }
        }
        renderToEncodec(l1, l1, paramTuSdkRecordSurface, paramTuSdkEncodeSurface);
      }
      
      protected boolean isLastDecodeFrame(long paramLong)
      {
        return TuSdkMediaFilesCuterImpl.n(TuSdkMediaFilesCuterImpl.this).isDecodeCompleted();
      }
      
      protected boolean needSkip(long paramLong)
      {
        return false;
      }
    }
    
    private class _AudioEncodecSync
      extends TuSdkAudioEncodecSyncBase
    {
      private _AudioEncodecSync() {}
      
      public void syncAudioEncodecOutputBuffer(TuSdkMediaMuxer paramTuSdkMediaMuxer, int paramInt, ByteBuffer paramByteBuffer, MediaCodec.BufferInfo paramBufferInfo)
      {
        super.syncAudioEncodecOutputBuffer(paramTuSdkMediaMuxer, paramInt, paramByteBuffer, paramBufferInfo);
      }
      
      public void syncAudioEncodecInfo(TuSdkAudioInfo paramTuSdkAudioInfo)
      {
        super.syncAudioEncodecInfo(paramTuSdkAudioInfo);
      }
    }
  }
  
  private class AudioRender
    implements AVAssetTrackSampleBufferOutput.AVAssetTrackSampleBufferInput
  {
    private AVMediaProcessQueue b = new AVMediaProcessQueue();
    private DefaultAduioRender c = new DefaultAduioRender(null);
    
    private AudioRender() {}
    
    private void a(Runnable paramRunnable)
    {
      this.b.runAsynchronouslyOnProcessingQueue(paramRunnable);
    }
    
    private void b(Runnable paramRunnable)
    {
      this.b.runSynchronouslyOnProcessingQueue(paramRunnable);
    }
    
    private void a()
    {
      if ((TuSdkMediaFilesCuterImpl.t(TuSdkMediaFilesCuterImpl.this) == null) || (TuSdkMediaFilesCuterImpl.u(TuSdkMediaFilesCuterImpl.this) != 0))
      {
        TLog.i("%s : The export session terminated unexpectedly, probably because the user forcibly stopped the session.", new Object[] { this });
        return;
      }
      if (TuSdkMediaFilesCuterImpl.t(TuSdkMediaFilesCuterImpl.this).renderOutputBuffers()) {
        a(new Runnable()
        {
          public void run()
          {
            TuSdkMediaFilesCuterImpl.AudioRender.a(TuSdkMediaFilesCuterImpl.AudioRender.this);
          }
        });
      } else {
        TuSdkMediaFilesCuterImpl.v(TuSdkMediaFilesCuterImpl.this);
      }
    }
    
    public void newFrameReady(AVSampleBuffer paramAVSampleBuffer)
    {
      paramAVSampleBuffer.info().presentationTimeUs = paramAVSampleBuffer.renderTimeUs();
      this.c.queueInputBuffer(paramAVSampleBuffer.buffer(), paramAVSampleBuffer.info());
    }
    
    public void outputFormatChaned(MediaFormat paramMediaFormat, AVAssetTrack paramAVAssetTrack)
    {
      TuSdkAudioInfo localTuSdkAudioInfo = new TuSdkAudioInfo(paramMediaFormat);
      this.c.changeFormat(localTuSdkAudioInfo);
    }
    
    private void b()
    {
      if (TuSdkMediaFilesCuterImpl.t(TuSdkMediaFilesCuterImpl.this) == null) {
        return;
      }
      this.c.reset();
      TuSdkMediaFilesCuterImpl.t(TuSdkMediaFilesCuterImpl.this).reset();
    }
    
    public void release()
    {
      b(new Runnable()
      {
        public void run()
        {
          TuSdkMediaFilesCuterImpl.AudioRender.b(TuSdkMediaFilesCuterImpl.AudioRender.this);
        }
      });
      this.c.release();
      this.b.quit();
    }
    
    private class DefaultAduioRender
      implements TuSdkAudioPitchSync, TuSdkAudioResampleSync
    {
      private TuSdkAudioPitch b;
      private TuSdkAudioResample c;
      private long d;
      private long e;
      private TuSdkAudioInfo f;
      
      private DefaultAduioRender() {}
      
      public boolean queueInputBuffer(ByteBuffer paramByteBuffer, MediaCodec.BufferInfo paramBufferInfo)
      {
        return this.c.queueInputBuffer(paramByteBuffer, paramBufferInfo);
      }
      
      public void changeFormat(TuSdkAudioInfo paramTuSdkAudioInfo)
      {
        if (this.c == null)
        {
          this.c = new TuSdkAudioResampleHardImpl(TuSdkMediaFilesCuterImpl.this.getOutputAudioInfo());
          this.c.changeFormat(paramTuSdkAudioInfo);
          this.c.setMediaSync(this);
          this.b = new TuSdkAudioPitchHardImpl(paramTuSdkAudioInfo);
          this.f = paramTuSdkAudioInfo;
          this.b.changeSpeed(TuSdkMediaFilesCuterImpl.w(TuSdkMediaFilesCuterImpl.this).speed);
          this.b.setMediaSync(this);
        }
        else
        {
          this.c.changeFormat(paramTuSdkAudioInfo);
        }
      }
      
      public void syncAudioResampleOutputBuffer(ByteBuffer paramByteBuffer, MediaCodec.BufferInfo paramBufferInfo)
      {
        if ((paramBufferInfo.presentationTimeUs > 0L) && (this.d <= 0L) && (TuSdkMediaFilesCuterImpl.w(TuSdkMediaFilesCuterImpl.this) != null) && (TuSdkMediaFilesCuterImpl.w(TuSdkMediaFilesCuterImpl.this).speed != 1.0F) && (this.f != null))
        {
          long l1 = 1024000000 / this.f.sampleRate;
          long l2 = ((float)paramBufferInfo.presentationTimeUs / TuSdkMediaFilesCuterImpl.w(TuSdkMediaFilesCuterImpl.this).speed - (float)l1);
          if (l2 > 0L) {
            this.d = l2;
          }
        }
        this.b.queueInputBuffer(paramByteBuffer, paramBufferInfo);
      }
      
      public void syncAudioPitchOutputBuffer(ByteBuffer paramByteBuffer, MediaCodec.BufferInfo paramBufferInfo)
      {
        paramByteBuffer.position(paramBufferInfo.offset);
        paramByteBuffer.limit(paramBufferInfo.offset + paramBufferInfo.size);
        this.e += this.d;
        if ((TuSdkMediaFilesCuterImpl.x(TuSdkMediaFilesCuterImpl.this)) && (paramBufferInfo.presentationTimeUs > TuSdkMediaFilesCuterImpl.a(TuSdkMediaFilesCuterImpl.this, TuSdkMediaFilesCuterImpl.n(TuSdkMediaFilesCuterImpl.this).outputTimeUs()) - TuSdkMediaFilesCuterImpl.y(TuSdkMediaFilesCuterImpl.this))) {
          return;
        }
        while ((TuSdkMediaFilesCuterImpl.z(TuSdkMediaFilesCuterImpl.this).getAudioOperation() != null) && (!ThreadHelper.isInterrupted()) && (TuSdkMediaFilesCuterImpl.A(TuSdkMediaFilesCuterImpl.this).getAudioOperation().writeBuffer(paramByteBuffer, paramBufferInfo) == 0)) {}
      }
      
      public void release()
      {
        if ((this.b == null) || (this.c == null)) {
          return;
        }
        this.b.release();
        this.c.release();
      }
      
      public void reset()
      {
        if ((this.b == null) || (this.c == null)) {
          return;
        }
        this.b.reset();
        this.c.reset();
      }
    }
  }
  
  private class VideoRender
    implements AVAssetTrackSampleBufferOutput.AVAssetTrackSampleBufferInput
  {
    private AVMediaProcessQueue b = new AVMediaProcessQueue();
    
    VideoRender() {}
    
    public void newFrameReady(AVSampleBuffer paramAVSampleBuffer)
    {
      try
      {
        synchronized (TuSdkMediaFilesCuterImpl.q(TuSdkMediaFilesCuterImpl.this))
        {
          TuSdkMediaFilesCuterImpl.q(TuSdkMediaFilesCuterImpl.this).wait(500L);
        }
      }
      catch (InterruptedException localInterruptedException)
      {
        localInterruptedException.printStackTrace();
      }
    }
    
    public void outputFormatChaned(MediaFormat paramMediaFormat, AVAssetTrack paramAVAssetTrack)
    {
      TuSdkVideoInfo localTuSdkVideoInfo = new TuSdkVideoInfo(paramMediaFormat);
      localTuSdkVideoInfo.setCorp(paramMediaFormat);
      TuSdkMediaFilesCuterImpl.c(TuSdkMediaFilesCuterImpl.this).setInputSize(localTuSdkVideoInfo.codecSize);
      TuSdkMediaFilesCuterImpl.c(TuSdkMediaFilesCuterImpl.this).setPreCropRect(localTuSdkVideoInfo.codecCrop);
      TuSdkMediaFilesCuterImpl.c(TuSdkMediaFilesCuterImpl.this).setInputRotation(paramAVAssetTrack.orientation());
    }
    
    private void a(Runnable paramRunnable)
    {
      this.b.runAsynchronouslyOnProcessingQueue(paramRunnable);
    }
    
    private void b(Runnable paramRunnable)
    {
      this.b.runSynchronouslyOnProcessingQueue(paramRunnable);
    }
    
    private void a()
    {
      if ((TuSdkMediaFilesCuterImpl.n(TuSdkMediaFilesCuterImpl.this) == null) || (TuSdkMediaFilesCuterImpl.r(TuSdkMediaFilesCuterImpl.this) != 0))
      {
        TLog.i("%s :The export session terminated unexpectedly, probably because the user forcibly stopped the session.", new Object[] { this });
        return;
      }
      if (TuSdkMediaFilesCuterImpl.n(TuSdkMediaFilesCuterImpl.this).renderOutputBuffers())
      {
        a(new Runnable()
        {
          public void run()
          {
            TuSdkMediaFilesCuterImpl.VideoRender.a(TuSdkMediaFilesCuterImpl.VideoRender.this);
          }
        });
      }
      else
      {
        TuSdkMediaFilesCuterImpl.s(TuSdkMediaFilesCuterImpl.this);
        TLog.i("%s : play done", new Object[] { this });
      }
    }
    
    public void release()
    {
      this.b.quit();
    }
  }
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKCore-3.1.0.jar!\org\lasque\tusdk\core\media\codec\suit\mutablePlayer\TuSdkMediaFilesCuterImpl.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */