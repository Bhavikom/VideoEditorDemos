package org.lasque.tusdk.video.editor;

import java.util.List;
import org.lasque.tusdk.core.media.codec.extend.TuSdkMediaTimeEffectTimeline;
import org.lasque.tusdk.core.media.codec.extend.TuSdkMediaTimeSlice;
import org.lasque.tusdk.core.media.codec.extend.TuSdkMediaTimeSliceEntity;
import org.lasque.tusdk.core.utils.TLog;

public class TuSdkMediaSlowTimeEffect
  extends TuSdkMediaTimeEffect
{
  private float a = 1.0F;
  
  public void setSpeed(float paramFloat)
  {
    if ((paramFloat < 0.0F) || (paramFloat > 2.0F))
    {
      TLog.e("Speed Error, speed must >0 && <=2", new Object[0]);
      return;
    }
    this.a = paramFloat;
  }
  
  public List<TuSdkMediaTimeSlice> getTimeSlickList()
  {
    TuSdkMediaTimeSlice localTuSdkMediaTimeSlice = new TuSdkMediaTimeSlice(this.mTimeRange.getStartTimeUS(), this.mTimeRange.getEndTimeUS());
    localTuSdkMediaTimeSlice.speed = this.a;
    this.mEffectTimeLine.setTimeSlice(localTuSdkMediaTimeSlice, 1);
    return this.mEffectTimeLine.getTimeEffectList();
  }
  
  public long getCurrentInputTimeUs()
  {
    return this.mCurrentInputTimeUs;
  }
  
  public long calcOutputTimeUs(long paramLong, TuSdkMediaTimeSliceEntity paramTuSdkMediaTimeSliceEntity, List<TuSdkMediaTimeSliceEntity> paramList)
  {
    this.mCurrentInputTimeUs = calcInputTimeUs(paramLong, paramTuSdkMediaTimeSliceEntity, paramList);
    return this.mCurrentInputTimeUs;
  }
  
  public long calcSeekOutputUs(long paramLong)
  {
    return 0L;
  }
  
  public long calcInputTimeUs(long paramLong, TuSdkMediaTimeSliceEntity paramTuSdkMediaTimeSliceEntity, List<TuSdkMediaTimeSliceEntity> paramList)
  {
    if (paramTuSdkMediaTimeSliceEntity == null) {
      return paramLong;
    }
    long l;
    if (paramTuSdkMediaTimeSliceEntity.speed != 1.0F)
    {
      l = paramTuSdkMediaTimeSliceEntity.outputStartUs + ((float)(paramLong - paramTuSdkMediaTimeSliceEntity.outputStartUs) * paramTuSdkMediaTimeSliceEntity.speed);
      l -= a(paramTuSdkMediaTimeSliceEntity, paramList);
    }
    else
    {
      l = paramLong - a(paramTuSdkMediaTimeSliceEntity, paramList);
    }
    return l;
  }
  
  private long a(TuSdkMediaTimeSliceEntity paramTuSdkMediaTimeSliceEntity, List<TuSdkMediaTimeSliceEntity> paramList)
  {
    int i = paramList.indexOf(paramTuSdkMediaTimeSliceEntity);
    long l1 = 0L;
    for (int j = 0; j < i; j++)
    {
      TuSdkMediaTimeSliceEntity localTuSdkMediaTimeSliceEntity = (TuSdkMediaTimeSliceEntity)paramList.get(j);
      if (localTuSdkMediaTimeSliceEntity.speed != 1.0F)
      {
        long l2 = localTuSdkMediaTimeSliceEntity.outputEndUs - localTuSdkMediaTimeSliceEntity.outputStartUs;
        l1 += ((float)l2 - (float)l2 * localTuSdkMediaTimeSliceEntity.speed);
      }
    }
    return l1;
  }
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKVideo-3.4.1.jar!\org\lasque\tusdk\video\editor\TuSdkMediaSlowTimeEffect.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */