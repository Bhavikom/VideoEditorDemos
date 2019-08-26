package org.lasque.tusdk.api.video.retriever;

import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.media.MediaMetadataRetriever;
import android.text.TextUtils;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;
import org.lasque.tusdk.core.TuSdkContext;
import org.lasque.tusdk.core.common.TuSDKMediaDataSource;
import org.lasque.tusdk.core.secret.StatisticsManger;
import org.lasque.tusdk.core.struct.TuSdkSize;
import org.lasque.tusdk.core.utils.TLog;
import org.lasque.tusdk.core.utils.ThreadHelper;
import org.lasque.tusdk.core.utils.image.BitmapHelper;
import org.lasque.tusdk.video.editor.TuSdkTimeRange;

public class TuSDKVideoImageExtractor
{
  private MediaMetadataRetriever a;
  private TuSDKMediaDataSource b;
  private TuSdkTimeRange c;
  private int d;
  private int e;
  private TuSdkSize f = TuSdkSize.create(80, 80);
  
  public static TuSDKVideoImageExtractor createExtractor()
  {
    return new TuSDKVideoImageExtractor();
  }
  
  private MediaMetadataRetriever a()
  {
    if (this.a == null) {
      this.a = new MediaMetadataRetriever();
    }
    return this.a;
  }
  
  public TuSDKMediaDataSource getVideoDataSource()
  {
    return this.b;
  }
  
  public TuSDKVideoImageExtractor setVideoDataSource(TuSDKMediaDataSource paramTuSDKMediaDataSource)
  {
    this.b = paramTuSDKMediaDataSource;
    return this;
  }
  
  public int getExtractFrameCount()
  {
    return this.d;
  }
  
  public TuSDKVideoImageExtractor setTimeRange(TuSdkTimeRange paramTuSdkTimeRange)
  {
    this.c = paramTuSdkTimeRange;
    return this;
  }
  
  public TuSDKVideoImageExtractor setExtractFrameCount(int paramInt)
  {
    this.d = paramInt;
    return this;
  }
  
  public TuSDKVideoImageExtractor setExtractFrameInterval(int paramInt)
  {
    this.e = paramInt;
    return this;
  }
  
  public int getExtractFrameInterval()
  {
    return this.e;
  }
  
  public TuSDKVideoImageExtractor setOutputImageSize(TuSdkSize paramTuSdkSize)
  {
    this.f = paramTuSdkSize;
    return this;
  }
  
  public TuSdkSize getOutputImageSize()
  {
    if (this.f == null) {
      this.f = new TuSdkSize(80, 80);
    }
    return this.f;
  }
  
  private boolean b()
  {
    if (this.a != null) {
      return true;
    }
    if ((this.b == null) || (!this.b.isValid()))
    {
      TLog.e("please set video path", new Object[0]);
      return false;
    }
    try
    {
      if (!TextUtils.isEmpty(this.b.getFilePath())) {
        a().setDataSource(this.b.getFilePath());
      } else {
        a().setDataSource(TuSdkContext.context(), this.b.getFileUri());
      }
    }
    catch (Exception localException)
    {
      return false;
    }
    return true;
  }
  
  private void c()
  {
    if (this.a != null) {
      this.a.release();
    }
    this.a = null;
  }
  
  public Bitmap getFrameAtTime(long paramLong, int paramInt)
  {
    if (!b()) {
      return null;
    }
    Bitmap localBitmap1 = a().getFrameAtTime(paramLong);
    if (localBitmap1 == null) {
      return null;
    }
    localBitmap1 = a(localBitmap1, paramInt);
    if ((localBitmap1.getWidth() == getOutputImageSize().width) && (localBitmap1.getHeight() == getOutputImageSize().height)) {
      return localBitmap1;
    }
    Bitmap localBitmap2 = BitmapHelper.imageScale(localBitmap1, getOutputImageSize().width, getOutputImageSize().height);
    BitmapHelper.recycled(localBitmap1);
    localBitmap1 = null;
    return localBitmap2;
  }
  
  public List<Bitmap> extractImageList(final TuSDKVideoImageExtractorDelegate paramTuSDKVideoImageExtractorDelegate)
  {
    ArrayList localArrayList = new ArrayList();
    if ((getExtractFrameCount() <= 0) && (getExtractFrameInterval() <= 0))
    {
      TLog.e("mExtractFrameCount and mExtractFrameInterval is invalid", new Object[0]);
      return localArrayList;
    }
    if (!b()) {
      return localArrayList;
    }
    String str = a().extractMetadata(9);
    if (TextUtils.isEmpty(str))
    {
      TLog.e("TuSDKVideoImageExtractor | Get media duration to fail, unable to extract bitmap", new Object[0]);
      return localArrayList;
    }
    float f1 = Float.parseFloat(str);
    if ((this.c == null) || (!this.c.isValid())) {
      this.c = TuSdkTimeRange.makeRange(0.0F, f1 / 1000.0F);
    }
    float f2 = this.c.duration();
    float f3 = getExtractFrameCount() > 0 ? f2 / getExtractFrameCount() : getExtractFrameInterval();
    if (f3 <= 0.0F) {
      f3 = 1.0F;
    }
    for (float f4 = this.c.getStartTime(); f4 < this.c.getEndTime(); f4 += f3)
    {
      long l = (f4 * 1000000.0F);
      final Bitmap localBitmap = getFrameAtTime(l, 80);
      if (localBitmap != null) {
        localArrayList.add(localBitmap);
      }
      ThreadHelper.post(new Runnable()
      {
        public void run()
        {
          paramTuSDKVideoImageExtractorDelegate.onVideoNewImageLoaded(localBitmap);
        }
      });
    }
    c();
    StatisticsManger.appendComponent(9449477L);
    return localArrayList;
  }
  
  public void asyncExtractImageList(final TuSDKVideoImageExtractorDelegate paramTuSDKVideoImageExtractorDelegate)
  {
    if (paramTuSDKVideoImageExtractorDelegate == null) {
      return;
    }
    ThreadHelper.runThread(new Runnable()
    {
      public void run()
      {
        final List localList = TuSDKVideoImageExtractor.this.extractImageList(paramTuSDKVideoImageExtractorDelegate);
        ThreadHelper.post(new Runnable()
        {
          public void run()
          {
            TuSDKVideoImageExtractor.2.this.a.onVideoImageListDidLoaded(localList);
          }
        });
      }
    });
  }
  
  private Bitmap a(Bitmap paramBitmap, int paramInt)
  {
    ByteArrayOutputStream localByteArrayOutputStream = new ByteArrayOutputStream();
    paramBitmap.compress(Bitmap.CompressFormat.JPEG, paramInt, localByteArrayOutputStream);
    ByteArrayInputStream localByteArrayInputStream = new ByteArrayInputStream(localByteArrayOutputStream.toByteArray());
    Bitmap localBitmap = BitmapFactory.decodeStream(localByteArrayInputStream, null, null);
    return localBitmap;
  }
  
  public static abstract interface TuSDKVideoImageExtractorDelegate
  {
    public abstract void onVideoImageListDidLoaded(List<Bitmap> paramList);
    
    public abstract void onVideoNewImageLoaded(Bitmap paramBitmap);
  }
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKVideo-3.4.1.jar!\org\lasque\tusdk\api\video\retriever\TuSDKVideoImageExtractor.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */