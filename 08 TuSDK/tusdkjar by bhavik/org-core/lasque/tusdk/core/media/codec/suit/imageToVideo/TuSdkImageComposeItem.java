package org.lasque.tusdk.core.media.codec.suit.imageToVideo;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.IdRes;
import org.lasque.tusdk.core.utils.TLog;

public class TuSdkImageComposeItem
  extends TuSdkComposeItem
{
  private Bitmap a;
  private long b = 2000000L;
  private long c;
  private long d;
  
  public TuSdkImageComposeItem()
  {
    this.mComposeType = TuSdkComposeItem.TuSdkComposeType.IMAGE;
  }
  
  public void setImageBitmap(Bitmap paramBitmap)
  {
    if ((paramBitmap == null) || (paramBitmap.isRecycled()))
    {
      TLog.w("bitmap is null or recycled !!!", new Object[] { "TuSdkImageComposeItem" });
      return;
    }
    this.a = paramBitmap;
  }
  
  public void setImageResource(Context paramContext, @IdRes int paramInt)
  {
    Bitmap localBitmap = BitmapFactory.decodeResource(paramContext.getResources(), paramInt);
    setImageBitmap(localBitmap);
  }
  
  public void setImagePath(String paramString)
  {
    Bitmap localBitmap = BitmapFactory.decodeFile(paramString);
    setImageBitmap(localBitmap);
  }
  
  public void setDurationUs(long paramLong)
  {
    if (paramLong <= 0L)
    {
      TLog.w("%s set durationUs is invalid !!!  %s", new Object[] { "TuSdkImageComposeItem", Long.valueOf(paramLong) });
      return;
    }
    this.b = paramLong;
  }
  
  public void setDuration(float paramFloat)
  {
    setDurationUs((paramFloat * 1000000.0F));
  }
  
  public long getDurationUs()
  {
    return this.b;
  }
  
  public Bitmap getImageBitmap()
  {
    return this.a;
  }
  
  public void alignTimeRange(long paramLong)
  {
    this.c = paramLong;
    this.d = (this.c + this.b);
  }
  
  public boolean isContainTimeRange(long paramLong)
  {
    return (paramLong >= this.c) && (paramLong < this.d);
  }
  
  public long getStartTimeUs()
  {
    return this.c;
  }
  
  public long getEndTimeUs()
  {
    return this.d;
  }
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKCore-3.1.0.jar!\org\lasque\tusdk\core\media\codec\suit\imageToVideo\TuSdkImageComposeItem.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */