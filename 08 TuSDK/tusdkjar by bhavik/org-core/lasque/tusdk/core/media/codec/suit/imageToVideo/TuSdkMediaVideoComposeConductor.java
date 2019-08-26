package org.lasque.tusdk.core.media.codec.suit.imageToVideo;

import android.graphics.SurfaceTexture;
import java.util.Iterator;
import java.util.LinkedList;
import org.lasque.tusdk.core.media.codec.encoder.TuSdkMediaFileEncoder;
import org.lasque.tusdk.core.struct.TuSdkSize;

class TuSdkMediaVideoComposeConductor
  implements TuSdkMediaVideoComposProcesser.ComposProcesserListener
{
  private LinkedList<TuSdkComposeItem> a;
  private int b = 30;
  private int c = 0;
  private long d = 0L;
  private long e = 0L;
  private TuSdkMediaFileEncoder f;
  private TuSdkMediaVideoComposProcesser g;
  public TuSdkImageComposeItem mImageItem;
  private Object h = new Object();
  private boolean i;
  private boolean j = true;
  private boolean k = false;
  
  public void setItemList(LinkedList<TuSdkComposeItem> paramLinkedList)
  {
    this.a = paramLinkedList;
    Iterator localIterator = paramLinkedList.iterator();
    while (localIterator.hasNext())
    {
      TuSdkComposeItem localTuSdkComposeItem = (TuSdkComposeItem)localIterator.next();
      ((TuSdkImageComposeItem)localTuSdkComposeItem).alignTimeRange(this.e);
      this.e += ((TuSdkImageComposeItem)localTuSdkComposeItem).getDurationUs();
    }
  }
  
  public void setMediaFileEncoder(TuSdkMediaFileEncoder paramTuSdkMediaFileEncoder)
  {
    this.f = paramTuSdkMediaFileEncoder;
  }
  
  public void setComposProcesser(TuSdkMediaVideoComposProcesser paramTuSdkMediaVideoComposProcesser)
  {
    this.g = paramTuSdkMediaVideoComposProcesser;
  }
  
  public void onFrameAvailable(SurfaceTexture paramSurfaceTexture)
  {
    if (this.i) {
      return;
    }
    synchronized (this.h)
    {
      this.d = ((1000000.0F / this.b * this.c));
      this.g.setCurrentFrameUs(this.d * 1000L);
      this.f.requestVideoRender(this.d * 1000L);
      if (this.j) {
        this.f.requestVideoKeyFrame();
      }
      if (this.d > this.e)
      {
        this.i = true;
        this.f.requestVideoKeyFrame();
        if (!this.k)
        {
          this.f.requestVideoKeyFrame();
          this.f.autoFillAudioMuteData(0L, this.e, true);
          this.f.signalVideoEndOfInputStream();
          this.k = true;
        }
      }
      this.c += 1;
    }
  }
  
  public void run()
  {
    this.g.setComposProcesserListener(this);
    this.f.requestVideoRender(0L);
  }
  
  public TuSdkImageComposeItem drawItemComposeItem()
  {
    synchronized (this.h)
    {
      if (this.a.size() == 0) {
        return this.mImageItem;
      }
      if ((this.mImageItem == null) || (!this.mImageItem.isContainTimeRange(this.d)))
      {
        this.mImageItem = ((TuSdkImageComposeItem)this.a.removeFirst());
        this.g.setInputSize(TuSdkSize.create(this.mImageItem.getImageBitmap()));
      }
      return this.mImageItem;
    }
  }
  
  public float calculateProgress()
  {
    return (float)this.d / (float)this.e;
  }
  
  public void setIsAllKeyFrame(boolean paramBoolean)
  {
    this.j = paramBoolean;
  }
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKCore-3.1.0.jar!\org\lasque\tusdk\core\media\codec\suit\imageToVideo\TuSdkMediaVideoComposeConductor.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */