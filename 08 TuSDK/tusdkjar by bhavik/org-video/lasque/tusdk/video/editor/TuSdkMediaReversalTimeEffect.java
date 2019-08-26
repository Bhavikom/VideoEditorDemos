package org.lasque.tusdk.video.editor;

import java.util.List;
import org.lasque.tusdk.core.media.codec.extend.TuSdkMediaTimeEffectTimeline;
import org.lasque.tusdk.core.media.codec.extend.TuSdkMediaTimeSlice;
import org.lasque.tusdk.core.media.codec.extend.TuSdkMediaTimeSliceEntity;

public class TuSdkMediaReversalTimeEffect
  extends TuSdkMediaTimeEffect
{
  public List<TuSdkMediaTimeSlice> getTimeSlickList()
  {
    this.mEffectTimeLine.reversTimeLine();
    return this.mEffectTimeLine.getTimeEffectList();
  }
  
  public long getCurrentInputTimeUs()
  {
    return this.mCurrentInputTimeUs;
  }
  
  public long calcOutputTimeUs(long paramLong, TuSdkMediaTimeSliceEntity paramTuSdkMediaTimeSliceEntity, List<TuSdkMediaTimeSliceEntity> paramList)
  {
    this.mCurrentInputTimeUs = (getInputTotalTimeUs() - paramLong);
    if (this.mCurrentInputTimeUs < 0L) {
      this.mCurrentInputTimeUs = 0L;
    }
    return this.mCurrentInputTimeUs;
  }
  
  public long calcSeekOutputUs(long paramLong)
  {
    return 0L;
  }
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKVideo-3.4.1.jar!\org\lasque\tusdk\video\editor\TuSdkMediaReversalTimeEffect.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */