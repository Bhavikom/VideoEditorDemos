package org.lasque.tusdk.video.editor;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.lasque.tusdk.core.media.codec.extend.TuSdkMediaTimeEffectTimeline;
import org.lasque.tusdk.core.media.codec.extend.TuSdkMediaTimeSlice;
import org.lasque.tusdk.core.media.codec.extend.TuSdkMediaTimeSliceEntity;
import org.lasque.tusdk.core.utils.TLog;

public abstract class TuSdkMediaTimeEffect
{
  protected TuSdkMediaTimeEffectTimeline mEffectTimeLine = new TuSdkMediaTimeEffectTimeline();
  protected List<TuSdkMediaTimeSliceEntity> mFinalTimeSlices = new ArrayList();
  protected TuSdkTimeRange mTimeRange = new TuSdkTimeRange();
  protected boolean isDropOverTime;
  protected int mOutputTimeLineType = 0;
  protected long mInputTotalTimeUs = 0L;
  protected long mCurrentInputTimeUs = 0L;
  
  public void setTimeRange(long paramLong1, long paramLong2)
  {
    this.mTimeRange.setStartTimeUs(paramLong1);
    this.mTimeRange.setEndTimeUs(paramLong2);
  }
  
  public void setTimeRange(TuSdkTimeRange paramTuSdkTimeRange)
  {
    this.mTimeRange.setStartTimeUs(paramTuSdkTimeRange.getStartTimeUS());
    this.mTimeRange.setEndTimeUs(paramTuSdkTimeRange.getEndTimeUS());
  }
  
  public void setRealTimeSlices(List<TuSdkMediaTimeSliceEntity> paramList)
  {
    this.mFinalTimeSlices.clear();
    this.mFinalTimeSlices.addAll(paramList);
    this.mEffectTimeLine.setInputAlignTimeSlices(this.mFinalTimeSlices);
  }
  
  public void setDropOverTime(boolean paramBoolean)
  {
    this.isDropOverTime = paramBoolean;
    this.mEffectTimeLine.setKeepOriginalLength(paramBoolean);
  }
  
  public boolean isDropOverTime()
  {
    return this.isDropOverTime;
  }
  
  public TuSdkTimeRange getTimeRange()
  {
    return this.mTimeRange;
  }
  
  public abstract List<TuSdkMediaTimeSlice> getTimeSlickList();
  
  public long getInputTotalTimeUs()
  {
    if (this.mInputTotalTimeUs == 0L) {
      this.mInputTotalTimeUs = a();
    }
    return this.mInputTotalTimeUs;
  }
  
  private long a()
  {
    if (this.mFinalTimeSlices.size() == 0)
    {
      TLog.w("mFinalTimeSlices size is 0 !", new Object[0]);
      return 0L;
    }
    long l = 0L;
    Iterator localIterator = this.mFinalTimeSlices.iterator();
    while (localIterator.hasNext())
    {
      TuSdkMediaTimeSliceEntity localTuSdkMediaTimeSliceEntity = (TuSdkMediaTimeSliceEntity)localIterator.next();
      l += localTuSdkMediaTimeSliceEntity.reduce();
    }
    return l;
  }
  
  public abstract long getCurrentInputTimeUs();
  
  public abstract long calcOutputTimeUs(long paramLong, TuSdkMediaTimeSliceEntity paramTuSdkMediaTimeSliceEntity, List<TuSdkMediaTimeSliceEntity> paramList);
  
  public abstract long calcSeekOutputUs(long paramLong);
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKVideo-3.4.1.jar!\org\lasque\tusdk\video\editor\TuSdkMediaTimeEffect.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */