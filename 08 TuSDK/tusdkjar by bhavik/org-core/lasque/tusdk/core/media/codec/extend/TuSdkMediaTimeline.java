package org.lasque.tusdk.core.media.codec.extend;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.lasque.tusdk.core.utils.TLog;

public class TuSdkMediaTimeline
  implements Serializable
{
  protected final List<TuSdkMediaTimeSlice> mOrginSlices = new ArrayList();
  protected float mOrginStartScaling = -1.0F;
  protected float mOrginEndScaling = -1.0F;
  private long a = 0L;
  protected long mTaskID;
  
  public TuSdkMediaTimeline() {}
  
  public TuSdkMediaTimeline(TuSdkMediaTimeline paramTuSdkMediaTimeline)
  {
    copy(paramTuSdkMediaTimeline);
  }
  
  public TuSdkMediaTimeline(float paramFloat1, float paramFloat2)
  {
    this.mOrginStartScaling = paramFloat1;
    this.mOrginEndScaling = paramFloat2;
  }
  
  public TuSdkMediaTimeline(List<TuSdkMediaTimeSlice> paramList)
  {
    if (paramList == null) {
      return;
    }
    this.mOrginSlices.addAll(paramList);
  }
  
  public TuSdkMediaTimeline(TuSdkMediaTimeSlice paramTuSdkMediaTimeSlice)
  {
    if (paramTuSdkMediaTimeSlice == null) {
      return;
    }
    this.mOrginSlices.add(paramTuSdkMediaTimeSlice);
  }
  
  public TuSdkMediaTimeline(long paramLong)
  {
    append(paramLong);
  }
  
  public long getInputDurationUs()
  {
    return this.a;
  }
  
  public void setInputDurationUs(long paramLong)
  {
    if (paramLong < 1L)
    {
      TLog.w("%s setInputDurationUs need timeUs > 0.", new Object[] { "TuSdkMediaTimeline" });
      return;
    }
    this.a = paramLong;
  }
  
  public void copy(TuSdkMediaTimeline paramTuSdkMediaTimeline)
  {
    if (paramTuSdkMediaTimeline == null) {
      return;
    }
    this.mOrginSlices.addAll(paramTuSdkMediaTimeline.mOrginSlices);
    this.mOrginStartScaling = paramTuSdkMediaTimeline.mOrginStartScaling;
    this.mOrginEndScaling = paramTuSdkMediaTimeline.mOrginEndScaling;
  }
  
  public void fresh(TuSdkMediaTimeline paramTuSdkMediaTimeline)
  {
    this.mOrginSlices.clear();
    this.mOrginStartScaling = -1.0F;
    this.mOrginEndScaling = -1.0F;
    this.mTaskID = 0L;
    copy(paramTuSdkMediaTimeline);
  }
  
  public long getTaskID()
  {
    return this.mTaskID;
  }
  
  public void append(long paramLong)
  {
    if (this.mOrginSlices.size() < 1)
    {
      append(paramLong, -1L);
      return;
    }
    TuSdkMediaTimeSlice localTuSdkMediaTimeSlice = (TuSdkMediaTimeSlice)this.mOrginSlices.get(this.mOrginSlices.size() - 1);
    localTuSdkMediaTimeSlice.endUs = paramLong;
    append(paramLong, -1L);
  }
  
  public void append(long paramLong1, long paramLong2)
  {
    append(paramLong1, paramLong2, 1.0F);
  }
  
  public void append(long paramLong1, long paramLong2, float paramFloat)
  {
    append(new TuSdkMediaTimeSlice(paramLong1, paramLong2, paramFloat));
  }
  
  public void append(TuSdkMediaTimeSlice paramTuSdkMediaTimeSlice)
  {
    if (paramTuSdkMediaTimeSlice == null) {
      return;
    }
    this.mOrginSlices.add(paramTuSdkMediaTimeSlice.clone());
  }
  
  public void remove(TuSdkMediaTimeSlice paramTuSdkMediaTimeSlice)
  {
    if (paramTuSdkMediaTimeSlice == null) {
      return;
    }
    Iterator localIterator = this.mOrginSlices.iterator();
    while (localIterator.hasNext())
    {
      TuSdkMediaTimeSlice localTuSdkMediaTimeSlice = (TuSdkMediaTimeSlice)localIterator.next();
      if (localTuSdkMediaTimeSlice.equals(paramTuSdkMediaTimeSlice))
      {
        this.mOrginSlices.remove(localTuSdkMediaTimeSlice);
        break;
      }
    }
  }
  
  public void remove(List<TuSdkMediaTimeSlice> paramList)
  {
    if ((paramList == null) || (paramList.size() < 1)) {
      return;
    }
    Iterator localIterator = paramList.iterator();
    while (localIterator.hasNext())
    {
      TuSdkMediaTimeSlice localTuSdkMediaTimeSlice = (TuSdkMediaTimeSlice)localIterator.next();
      remove(localTuSdkMediaTimeSlice);
    }
  }
  
  public void remove(int paramInt)
  {
    if (paramInt < this.mOrginSlices.size()) {
      this.mOrginSlices.remove(paramInt);
    }
  }
  
  public List<TuSdkMediaTimeSlice> getOrginSlices()
  {
    return this.mOrginSlices;
  }
  
  public int indexOfOrginSlices(TuSdkMediaTimeSlice paramTuSdkMediaTimeSlice)
  {
    if ((this.mOrginSlices == null) || (this.mOrginSlices.size() == 0)) {
      return -1;
    }
    for (int i = 0; i < this.mOrginSlices.size(); i++)
    {
      TuSdkMediaTimeSlice localTuSdkMediaTimeSlice = (TuSdkMediaTimeSlice)this.mOrginSlices.get(i);
      if ((localTuSdkMediaTimeSlice.startUs == paramTuSdkMediaTimeSlice.startUs) && (localTuSdkMediaTimeSlice.endUs == paramTuSdkMediaTimeSlice.endUs)) {
        return i;
      }
    }
    return -1;
  }
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKCore-3.1.0.jar!\org\lasque\tusdk\core\media\codec\extend\TuSdkMediaTimeline.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */