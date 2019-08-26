package org.lasque.tusdk.video.editor;

import java.util.List;
import org.lasque.tusdk.core.media.codec.extend.TuSdkMediaTimeEffectTimeline;
import org.lasque.tusdk.core.media.codec.extend.TuSdkMediaTimeSlice;
import org.lasque.tusdk.core.media.codec.extend.TuSdkMediaTimeSliceEntity;

public class TuSdkMediaRepeatTimeEffect
  extends TuSdkMediaTimeEffect
{
  private int a = 2;
  
  public void setRepeatCount(int paramInt)
  {
    this.a = paramInt;
  }
  
  public List<TuSdkMediaTimeSlice> getTimeSlickList()
  {
    this.mEffectTimeLine.setTimeSlice(new TuSdkMediaTimeSlice(this.mTimeRange.getStartTimeUS(), this.mTimeRange.getEndTimeUS()), this.a + 1);
    return this.mEffectTimeLine.getTimeEffectList();
  }
  
  public long getCurrentInputTimeUs()
  {
    return this.mCurrentInputTimeUs;
  }
  
  public long calcOutputTimeUs(long paramLong, TuSdkMediaTimeSliceEntity paramTuSdkMediaTimeSliceEntity, List<TuSdkMediaTimeSliceEntity> paramList)
  {
    this.mCurrentInputTimeUs = calOutputNoRepetTimeUs(paramLong, paramTuSdkMediaTimeSliceEntity, paramList);
    return this.mCurrentInputTimeUs;
  }
  
  public long calcSeekOutputUs(long paramLong)
  {
    return 0L;
  }
  
  public long calOutputNoRepetTimeUs(long paramLong, TuSdkMediaTimeSliceEntity paramTuSdkMediaTimeSliceEntity, List<TuSdkMediaTimeSliceEntity> paramList)
  {
    long l2 = a(paramTuSdkMediaTimeSliceEntity, paramList);
    long l1 = paramLong - l2;
    return l1;
  }
  
  private long a(TuSdkMediaTimeSliceEntity paramTuSdkMediaTimeSliceEntity, List<TuSdkMediaTimeSliceEntity> paramList)
  {
    long l = 0L;
    if (paramList.size() == 0) {
      return 0L;
    }
    int i = paramList.indexOf(paramTuSdkMediaTimeSliceEntity);
    int j;
    TuSdkMediaTimeSliceEntity localTuSdkMediaTimeSliceEntity;
    if (paramTuSdkMediaTimeSliceEntity.overlapIndex > -1) {
      for (j = 0; j <= i; j++)
      {
        localTuSdkMediaTimeSliceEntity = (TuSdkMediaTimeSliceEntity)paramList.get(j);
        if ((localTuSdkMediaTimeSliceEntity.overlapIndex > -1) && (paramTuSdkMediaTimeSliceEntity.overlapIndex > localTuSdkMediaTimeSliceEntity.overlapIndex)) {
          l += localTuSdkMediaTimeSliceEntity.endUs - localTuSdkMediaTimeSliceEntity.startUs;
        }
      }
    } else {
      for (j = 0; j <= i; j++)
      {
        localTuSdkMediaTimeSliceEntity = (TuSdkMediaTimeSliceEntity)paramList.get(j);
        if (localTuSdkMediaTimeSliceEntity.overlapIndex > 0) {
          l += localTuSdkMediaTimeSliceEntity.endUs - localTuSdkMediaTimeSliceEntity.startUs;
        }
      }
    }
    return l;
  }
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKVideo-3.4.1.jar!\org\lasque\tusdk\video\editor\TuSdkMediaRepeatTimeEffect.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */