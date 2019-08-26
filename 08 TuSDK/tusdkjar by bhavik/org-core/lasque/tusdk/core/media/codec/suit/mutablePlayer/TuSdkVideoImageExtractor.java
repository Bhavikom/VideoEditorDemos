package org.lasque.tusdk.core.media.codec.suit.mutablePlayer;

import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.SurfaceTexture;
import android.graphics.SurfaceTexture.OnFrameAvailableListener;
import android.media.MediaFormat;
import android.opengl.GLES20;
import android.view.Surface;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.lasque.tusdk.core.seles.egl.SelesEGLCoreSingleSurface;
import org.lasque.tusdk.core.seles.extend.SelesVerticeCoordinateCropBuilderImpl;
import org.lasque.tusdk.core.seles.sources.SelesSurfaceReceiver;
import org.lasque.tusdk.core.struct.TuSdkMediaDataSource;
import org.lasque.tusdk.core.struct.TuSdkSize;
import org.lasque.tusdk.core.utils.TLog;
import org.lasque.tusdk.core.utils.ThreadHelper;

public class TuSdkVideoImageExtractor
{
  private SelesEGLCoreSingleSurface a;
  private List<AVAssetTrack> b;
  private AVAssetTrackOutputSouce c;
  private AVAssetTrackCodecDecoder d;
  private ImageSurfaceReceiver e;
  private TuSdkSize f;
  private float g = 0.0F;
  private int h = 21;
  private AVMediaProcessQueue i;
  private TuSdkVideoImageExtractorListener j;
  private List<VideoImage> k = new ArrayList();
  private AVAssetTrackSampleBufferOutput.AVAssetTrackSampleBufferInput l = new AVAssetTrackSampleBufferOutput.AVAssetTrackSampleBufferInput()
  {
    float a;
    
    public void newFrameReady(AVSampleBuffer paramAnonymousAVSampleBuffer)
    {
      if (paramAnonymousAVSampleBuffer.isDecodeOnly()) {
        return;
      }
      if ((TuSdkVideoImageExtractor.g(TuSdkVideoImageExtractor.this) == null) || (TuSdkVideoImageExtractor.d(TuSdkVideoImageExtractor.this).size() >= TuSdkVideoImageExtractor.h(TuSdkVideoImageExtractor.this))) {
        return;
      }
      if ((float)paramAnonymousAVSampleBuffer.renderTimeUs() - this.a < 0.0F) {
        return;
      }
      this.a = ((float)paramAnonymousAVSampleBuffer.renderTimeUs() + TuSdkVideoImageExtractor.this.getExtractFrameIntervalTimeUs());
      TuSdkVideoImageExtractor.c(TuSdkVideoImageExtractor.this).setSurfaceTexTimestampNs(paramAnonymousAVSampleBuffer.renderTimeUs());
      synchronized (TuSdkVideoImageExtractor.d(TuSdkVideoImageExtractor.this))
      {
        try
        {
          TuSdkVideoImageExtractor.d(TuSdkVideoImageExtractor.this).wait(100L);
        }
        catch (InterruptedException localInterruptedException)
        {
          localInterruptedException.printStackTrace();
        }
      }
      TuSdkVideoImageExtractor.c(TuSdkVideoImageExtractor.this).updateSurfaceTexImage();
    }
    
    public void outputFormatChaned(MediaFormat paramAnonymousMediaFormat, AVAssetTrack paramAnonymousAVAssetTrack)
    {
      TuSdkVideoImageExtractor.c(TuSdkVideoImageExtractor.this).setInputRotation(paramAnonymousAVAssetTrack.orientation());
      TuSdkVideoImageExtractor.c(TuSdkVideoImageExtractor.this).setInputSize(paramAnonymousAVAssetTrack.naturalSize());
    }
  };
  
  public TuSdkVideoImageExtractor(List<TuSdkMediaDataSource> paramList)
  {
    if ((paramList == null) || (paramList.size() == 0))
    {
      TLog.e("Please set at least one video source.", new Object[0]);
      return;
    }
    this.b = new ArrayList(paramList.size());
    Iterator localIterator = paramList.iterator();
    while (localIterator.hasNext())
    {
      TuSdkMediaDataSource localTuSdkMediaDataSource = (TuSdkMediaDataSource)localIterator.next();
      AVAssetDataSource localAVAssetDataSource = new AVAssetDataSource(localTuSdkMediaDataSource);
      List localList = localAVAssetDataSource.tracksWithMediaType(AVMediaType.AVMediaTypeVideo);
      if (localList.size() != 0) {
        this.b.addAll(localList);
      }
    }
    if (this.b.size() == 0)
    {
      TLog.e("The input video source did not find the video tracks.", new Object[0]);
      return;
    }
    this.i = new AVMediaProcessQueue();
    this.e = new ImageSurfaceReceiver(null);
    this.e.setTextureCoordinateBuilder(new SelesVerticeCoordinateCropBuilderImpl(false));
    this.c = new AVAssetTrackPipeMediaExtractor(this.b);
    this.d = new AVAssetTrackCodecDecoder(this.c);
    this.d.addTarget(this.l);
    a();
  }
  
  private void a(Runnable paramRunnable)
  {
    this.i.runAsynchronouslyOnProcessingQueue(paramRunnable);
  }
  
  private void a()
  {
    a(new Runnable()
    {
      public void run()
      {
        TuSdkVideoImageExtractor.a(TuSdkVideoImageExtractor.this, new SelesEGLCoreSingleSurface(null));
        TuSdkVideoImageExtractor.b(TuSdkVideoImageExtractor.this).attachOffscreenSurface(TuSdkVideoImageExtractor.a(TuSdkVideoImageExtractor.this).inputTrack().naturalSize().width, TuSdkVideoImageExtractor.a(TuSdkVideoImageExtractor.this).inputTrack().naturalSize().height);
        TuSdkVideoImageExtractor.c(TuSdkVideoImageExtractor.this).initInGLThread();
        TuSdkVideoImageExtractor.c(TuSdkVideoImageExtractor.this).setOutputSize(TuSdkVideoImageExtractor.this.getOutputImageSize());
        TuSdkVideoImageExtractor.c(TuSdkVideoImageExtractor.this).setInputRotation(TuSdkVideoImageExtractor.a(TuSdkVideoImageExtractor.this).inputTrack().orientation());
        TuSdkVideoImageExtractor.c(TuSdkVideoImageExtractor.this).setInputSize(TuSdkVideoImageExtractor.a(TuSdkVideoImageExtractor.this).inputTrack().naturalSize());
        SurfaceTexture localSurfaceTexture = TuSdkVideoImageExtractor.c(TuSdkVideoImageExtractor.this).requestSurfaceTexture();
        localSurfaceTexture.setOnFrameAvailableListener(new SurfaceTexture.OnFrameAvailableListener()
        {
          public void onFrameAvailable(SurfaceTexture paramAnonymous2SurfaceTexture)
          {
            synchronized (TuSdkVideoImageExtractor.d(TuSdkVideoImageExtractor.this))
            {
              TuSdkVideoImageExtractor.d(TuSdkVideoImageExtractor.this).notify();
            }
            TuSdkVideoImageExtractor.a(TuSdkVideoImageExtractor.this, new Runnable()
            {
              public void run()
              {
                TuSdkVideoImageExtractor.c(TuSdkVideoImageExtractor.this).newFrameReadyInGLThread(0L);
              }
            });
          }
        });
        Surface localSurface = new Surface(localSurfaceTexture);
        TuSdkVideoImageExtractor.e(TuSdkVideoImageExtractor.this).setOutputSurface(localSurface);
      }
    });
  }
  
  public void extractImages()
  {
    a(new Runnable()
    {
      public void run()
      {
        if (TuSdkVideoImageExtractor.e(TuSdkVideoImageExtractor.this) == null)
        {
          TLog.i("%s : image extractor paused", new Object[] { this });
          return;
        }
        if (TuSdkVideoImageExtractor.e(TuSdkVideoImageExtractor.this).renderOutputBuffers()) {
          TuSdkVideoImageExtractor.a(TuSdkVideoImageExtractor.this, new Runnable()
          {
            public void run()
            {
              TuSdkVideoImageExtractor.f(TuSdkVideoImageExtractor.this);
            }
          });
        }
      }
    });
  }
  
  private void b()
  {
    if (this.d == null)
    {
      TLog.i("%s : image extractor paused", new Object[] { this });
      return;
    }
    if (this.d.renderOutputBuffers())
    {
      a(new Runnable()
      {
        public void run()
        {
          TuSdkVideoImageExtractor.f(TuSdkVideoImageExtractor.this);
        }
      });
    }
    else
    {
      TLog.i("%s : image extractor done", new Object[] { this });
      this.d.reset();
      if (this.j != null)
      {
        if ((this.k.size() < this.h) && (this.k.size() > 0))
        {
          VideoImage localVideoImage1 = (VideoImage)this.k.get(this.k.size() - 1);
          int m = this.k.size();
          for (int n = 0; n < this.h - m; n++)
          {
            VideoImage localVideoImage2 = new VideoImage();
            localVideoImage2.bitmap = localVideoImage1.bitmap.copy(Bitmap.Config.ARGB_8888, false);
            localVideoImage2.outputTimeUs = localVideoImage1.outputTimeUs;
            this.k.add(localVideoImage2);
            this.j.onOutputFrameImage(localVideoImage2);
          }
        }
        this.j.onImageExtractorCompleted(this.k);
      }
    }
  }
  
  public TuSdkVideoImageExtractor setImageListener(TuSdkVideoImageExtractorListener paramTuSdkVideoImageExtractorListener)
  {
    this.j = paramTuSdkVideoImageExtractorListener;
    return this;
  }
  
  public TuSdkVideoImageExtractor setOutputImageSize(TuSdkSize paramTuSdkSize)
  {
    this.f = paramTuSdkSize;
    if (this.e != null) {
      this.e.setOutputSize(paramTuSdkSize);
    }
    return this;
  }
  
  public TuSdkSize getOutputImageSize()
  {
    if (this.f == null) {
      return TuSdkSize.create(40, 40);
    }
    return this.f;
  }
  
  public TuSdkVideoImageExtractor setExtractFrameInterval(float paramFloat)
  {
    this.g = (paramFloat * 1000000.0F);
    return this;
  }
  
  public TuSdkVideoImageExtractor setExtractFrameCount(int paramInt)
  {
    if ((this.c == null) || (paramInt <= 0)) {
      return this;
    }
    this.h = (paramInt + 2);
    return this;
  }
  
  public TuSdkVideoImageExtractor setOutputTimeRange(long paramLong1, long paramLong2)
  {
    if (this.c == null) {
      return this;
    }
    this.c.setTimeRange(AVTimeRange.AVTimeRangeMake(paramLong1, paramLong2 - paramLong1));
    this.g = 0.0F;
    return this;
  }
  
  public float getExtractFrameIntervalTimeUs()
  {
    if (this.c == null) {
      return 0.0F;
    }
    if (this.g == 0.0F) {
      this.g = ((float)this.c.durationTimeUs() / (this.h + 2.0F));
    }
    return this.g;
  }
  
  public void release()
  {
    this.i.runSynchronouslyOnProcessingQueue(new Runnable()
    {
      public void run()
      {
        if (TuSdkVideoImageExtractor.e(TuSdkVideoImageExtractor.this) != null) {
          TuSdkVideoImageExtractor.e(TuSdkVideoImageExtractor.this).reset();
        }
        if (TuSdkVideoImageExtractor.c(TuSdkVideoImageExtractor.this) != null) {
          TuSdkVideoImageExtractor.c(TuSdkVideoImageExtractor.this).destroy();
        }
        TuSdkVideoImageExtractor.a(TuSdkVideoImageExtractor.this, null);
        if (TuSdkVideoImageExtractor.b(TuSdkVideoImageExtractor.this) != null) {
          TuSdkVideoImageExtractor.b(TuSdkVideoImageExtractor.this).destroy();
        }
        TuSdkVideoImageExtractor.a(TuSdkVideoImageExtractor.this, null);
      }
    });
    this.i.quit();
  }
  
  public static abstract interface TuSdkVideoImageExtractorListener
  {
    public abstract void onOutputFrameImage(TuSdkVideoImageExtractor.VideoImage paramVideoImage);
    
    public abstract void onImageExtractorCompleted(List<TuSdkVideoImageExtractor.VideoImage> paramList);
  }
  
  public class VideoImage
  {
    public Bitmap bitmap;
    public long outputTimeUs;
    
    public VideoImage() {}
  }
  
  private class ImageSurfaceReceiver
    extends SelesSurfaceReceiver
  {
    private ImageSurfaceReceiver() {}
    
    protected void renderToTexture(FloatBuffer paramFloatBuffer1, FloatBuffer paramFloatBuffer2)
    {
      super.renderToTexture(paramFloatBuffer1, paramFloatBuffer2);
      int i = TuSdkVideoImageExtractor.this.getOutputImageSize().width;
      int j = TuSdkVideoImageExtractor.this.getOutputImageSize().height;
      IntBuffer localIntBuffer = IntBuffer.allocate(i * j);
      GLES20.glReadPixels(0, 0, i, j, 6408, 5121, localIntBuffer);
      Bitmap localBitmap = Bitmap.createBitmap(i, j, Bitmap.Config.ARGB_8888);
      localBitmap.copyPixelsFromBuffer(localIntBuffer);
      if ((localBitmap != null) && (TuSdkVideoImageExtractor.g(TuSdkVideoImageExtractor.this) != null))
      {
        final TuSdkVideoImageExtractor.VideoImage localVideoImage = new TuSdkVideoImageExtractor.VideoImage(TuSdkVideoImageExtractor.this);
        localVideoImage.bitmap = localBitmap;
        localVideoImage.outputTimeUs = TuSdkVideoImageExtractor.e(TuSdkVideoImageExtractor.this).outputTimeUs();
        TuSdkVideoImageExtractor.d(TuSdkVideoImageExtractor.this).add(localVideoImage);
        ThreadHelper.post(new Runnable()
        {
          public void run()
          {
            if (TuSdkVideoImageExtractor.d(TuSdkVideoImageExtractor.this).size() > 1) {
              TuSdkVideoImageExtractor.g(TuSdkVideoImageExtractor.this).onOutputFrameImage(localVideoImage);
            }
          }
        });
      }
    }
  }
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKCore-3.1.0.jar!\org\lasque\tusdk\core\media\codec\suit\mutablePlayer\TuSdkVideoImageExtractor.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */