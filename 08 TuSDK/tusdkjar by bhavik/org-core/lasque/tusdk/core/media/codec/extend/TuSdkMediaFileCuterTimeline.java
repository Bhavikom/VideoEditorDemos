package org.lasque.tusdk.core.media.codec.extend;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.lasque.tusdk.core.media.codec.TuSdkMediaExtractor;
import org.lasque.tusdk.core.media.codec.decoder.TuSdkMediaFrameInfo;
import org.lasque.tusdk.core.utils.TLog;

public class TuSdkMediaFileCuterTimeline
  extends TuSdkMediaTimeline
{
  public static final int LINEAR = 0;
  public static final int OVERLAP = 1;
  private final List<TuSdkMediaTimeSliceEntity> a = new ArrayList();
  private long b = 0L;
  private boolean c = false;
  private int d = 0;
  private long e = 0L;
  
  public TuSdkMediaFileCuterTimeline() {}
  
  public TuSdkMediaFileCuterTimeline(int paramInt)
  {
    this.d = paramInt;
  }
  
  public TuSdkMediaFileCuterTimeline(int paramInt, TuSdkMediaTimeline paramTuSdkMediaTimeline)
  {
    this.d = paramInt;
    copy(paramTuSdkMediaTimeline);
  }
  
  public TuSdkMediaFileCuterTimeline(TuSdkMediaTimeline paramTuSdkMediaTimeline)
  {
    super(paramTuSdkMediaTimeline);
  }
  
  public TuSdkMediaFileCuterTimeline(float paramFloat1, float paramFloat2)
  {
    super(paramFloat1, paramFloat2);
  }
  
  public TuSdkMediaFileCuterTimeline(List<TuSdkMediaTimeSlice> paramList)
  {
    super(paramList);
  }
  
  public TuSdkMediaFileCuterTimeline(TuSdkMediaTimeSlice paramTuSdkMediaTimeSlice)
  {
    super(paramTuSdkMediaTimeSlice);
  }
  
  public TuSdkMediaFileCuterTimeline(long paramLong)
  {
    super(paramLong);
  }
  
  public long getOutputDurationUs()
  {
    return this.b;
  }
  
  public long getRemoveOverSliceDurationUs()
  {
    return this.b - getOverSliceDuration();
  }
  
  public long getOverSliceDuration()
  {
    long l = 0L;
    Iterator localIterator = this.mOrginSlices.iterator();
    while (localIterator.hasNext())
    {
      TuSdkMediaTimeSlice localTuSdkMediaTimeSlice = (TuSdkMediaTimeSlice)localIterator.next();
      if (localTuSdkMediaTimeSlice.overlapIndex > 0) {
        l = ((float)l + (float)Math.abs(localTuSdkMediaTimeSlice.endUs - localTuSdkMediaTimeSlice.startUs) * localTuSdkMediaTimeSlice.speed);
      }
    }
    return l;
  }
  
  public void setProgressOutputMode(int paramInt)
  {
    this.d = paramInt;
  }
  
  public int getCalcMode()
  {
    return this.d;
  }
  
  public void fresh(TuSdkMediaTimeline paramTuSdkMediaTimeline)
  {
    reset();
    super.fresh(paramTuSdkMediaTimeline);
  }
  
  public void reset()
  {
    this.c = false;
    this.a.clear();
    this.b = 0L;
    this.e = 0L;
    this.mOrginStartScaling = -1.0F;
    this.mOrginEndScaling = -1.0F;
    this.mTaskID = 0L;
  }
  
  public TuSdkMediaTimeSliceEntity firstSlice()
  {
    if (this.a.size() < 1) {
      return null;
    }
    return (TuSdkMediaTimeSliceEntity)this.a.get(0);
  }
  
  public TuSdkMediaTimeSliceEntity sliceWithOutputTimeUs(long paramLong)
  {
    List localList = this.a;
    if (localList.size() < 1)
    {
      TLog.e("sliceWithOutputTimeUs() null :%s", new Object[] { Boolean.valueOf(this.c) });
      return null;
    }
    if (paramLong < 1L) {
      return (TuSdkMediaTimeSliceEntity)localList.get(0);
    }
    if (paramLong >= this.b) {
      return (TuSdkMediaTimeSliceEntity)localList.get(localList.size() - 1);
    }
    Iterator localIterator = localList.iterator();
    while (localIterator.hasNext())
    {
      TuSdkMediaTimeSliceEntity localTuSdkMediaTimeSliceEntity = (TuSdkMediaTimeSliceEntity)localIterator.next();
      if (localTuSdkMediaTimeSliceEntity.overviewOutput(paramLong) == 0) {
        return localTuSdkMediaTimeSliceEntity;
      }
    }
    return (TuSdkMediaTimeSliceEntity)localList.get(localList.size() - 1);
  }
  
  public TuSdkMediaTimeSliceEntity sliceWithInputTimeUs(long paramLong)
  {
    if (this.a.size() < 1)
    {
      TLog.e("sliceWithOutputTimeUs() null :%s", new Object[] { Boolean.valueOf(this.c) });
      return null;
    }
    if (paramLong >= this.b) {
      return (TuSdkMediaTimeSliceEntity)this.a.get(this.a.size() - 1);
    }
    Iterator localIterator = this.a.iterator();
    while (localIterator.hasNext())
    {
      TuSdkMediaTimeSliceEntity localTuSdkMediaTimeSliceEntity = (TuSdkMediaTimeSliceEntity)localIterator.next();
      if (localTuSdkMediaTimeSliceEntity.overviewInput(paramLong) == 0) {
        return localTuSdkMediaTimeSliceEntity;
      }
    }
    return (TuSdkMediaTimeSliceEntity)this.a.get(this.a.size() - 1);
  }
  
  public TuSdkMediaTimeSliceEntity sliceAudioWithInputTimeUs(long paramLong, boolean paramBoolean)
  {
    if (this.a.size() < 1)
    {
      TLog.e("sliceWithOutputTimeUs() null :%s", new Object[] { Boolean.valueOf(this.c) });
      return null;
    }
    if (paramLong >= this.b) {
      return (TuSdkMediaTimeSliceEntity)this.a.get(this.a.size() - 1);
    }
    Iterator localIterator = this.a.iterator();
    while (localIterator.hasNext())
    {
      TuSdkMediaTimeSliceEntity localTuSdkMediaTimeSliceEntity = (TuSdkMediaTimeSliceEntity)localIterator.next();
      if (localTuSdkMediaTimeSliceEntity.overviewAudioInput(paramLong, paramBoolean) == 0) {
        return localTuSdkMediaTimeSliceEntity;
      }
    }
    return (TuSdkMediaTimeSliceEntity)this.a.get(this.a.size() - 1);
  }
  
  public TuSdkMediaTimeSliceEntity existenceWithInputTimeUs(long paramLong)
  {
    List localList = this.a;
    if (localList.size() < 1)
    {
      TLog.e("sliceWithOutputTimeUs() null :%s", new Object[] { Boolean.valueOf(this.c) });
      return null;
    }
    if (paramLong < 1L) {
      return (TuSdkMediaTimeSliceEntity)localList.get(0);
    }
    Iterator localIterator = localList.iterator();
    while (localIterator.hasNext())
    {
      TuSdkMediaTimeSliceEntity localTuSdkMediaTimeSliceEntity = (TuSdkMediaTimeSliceEntity)localIterator.next();
      if ((paramLong >= localTuSdkMediaTimeSliceEntity.startUs) && (paramLong <= localTuSdkMediaTimeSliceEntity.endUs)) {
        return localTuSdkMediaTimeSliceEntity;
      }
    }
    return (TuSdkMediaTimeSliceEntity)localList.get(localList.size() - 1);
  }
  
  public long sliceWithCalcModeOutputTimeUs(long paramLong)
  {
    if (this.a.size() < 1)
    {
      TLog.e("sliceWithOutputTimeUs() null :%s", new Object[] { Boolean.valueOf(this.c) });
      return 0L;
    }
    if (paramLong < 1L) {
      return 0L;
    }
    if (paramLong >= this.b) {
      return this.b;
    }
    long l = a(paramLong);
    return l;
  }
  
  private long a(long paramLong)
  {
    if (paramLong < 0L) {
      return 0L;
    }
    if (paramLong >= this.b) {
      return this.b;
    }
    Object localObject = null;
    Iterator localIterator = this.a.iterator();
    while (localIterator.hasNext())
    {
      TuSdkMediaTimeSliceEntity localTuSdkMediaTimeSliceEntity = (TuSdkMediaTimeSliceEntity)localIterator.next();
      if (localTuSdkMediaTimeSliceEntity.overviewRealTime(paramLong) == 0)
      {
        localObject = localTuSdkMediaTimeSliceEntity;
        break;
      }
    }
    if (localObject == null) {
      return paramLong;
    }
    return ((float)((TuSdkMediaTimeSliceEntity)localObject).outputStartUs + (float)(paramLong - ((TuSdkMediaTimeSliceEntity)localObject).realTimeStarUs) / ((TuSdkMediaTimeSliceEntity)localObject).speed);
  }
  
  public boolean isFixTimeSlices()
  {
    return this.c;
  }
  
  public void fixTimeSlices(TuSdkMediaExtractor paramTuSdkMediaExtractor, boolean paramBoolean1, boolean paramBoolean2)
  {
    a(paramTuSdkMediaExtractor, paramBoolean1, true);
  }
  
  public void fixTimeSlices(TuSdkMediaExtractor paramTuSdkMediaExtractor, boolean paramBoolean)
  {
    a(paramTuSdkMediaExtractor, paramBoolean, false);
  }
  
  private void a(TuSdkMediaExtractor paramTuSdkMediaExtractor, boolean paramBoolean1, boolean paramBoolean2)
  {
    if ((paramTuSdkMediaExtractor == null) || (paramTuSdkMediaExtractor.getFrameInfo() == null) || (this.c)) {
      return;
    }
    if ((paramBoolean1) && (!paramTuSdkMediaExtractor.getFrameInfo().supportAllKeys()))
    {
      TLog.w("%s media unsupport timeline.", new Object[] { "TuSdkMediaFileCuterTimeline" });
      this.c = true;
      return;
    }
    this.mTaskID = System.nanoTime();
    long l1 = paramTuSdkMediaExtractor.getSampleTime();
    if (this.mOrginSlices.size() < 1)
    {
      TuSdkMediaTimeSliceEntity localTuSdkMediaTimeSliceEntity1 = new TuSdkMediaTimeSliceEntity();
      localTuSdkMediaTimeSliceEntity1.startUs = (Math.ceil(this.mOrginStartScaling * (float)getInputDurationUs()));
      localTuSdkMediaTimeSliceEntity1.endUs = (Math.ceil(this.mOrginEndScaling * (float)getInputDurationUs()));
      a(localTuSdkMediaTimeSliceEntity1, paramTuSdkMediaExtractor, paramBoolean1, paramBoolean2);
      if (localTuSdkMediaTimeSliceEntity1.startUs != localTuSdkMediaTimeSliceEntity1.endUs)
      {
        this.b = Math.abs(localTuSdkMediaTimeSliceEntity1.reduce());
        localTuSdkMediaTimeSliceEntity1.outputEndUs = this.b;
        this.a.add(localTuSdkMediaTimeSliceEntity1);
        append(localTuSdkMediaTimeSliceEntity1);
      }
    }
    else
    {
      long l2 = 0L;
      Object localObject = null;
      int i = 0;
      Iterator localIterator = this.mOrginSlices.iterator();
      while (localIterator.hasNext())
      {
        TuSdkMediaTimeSlice localTuSdkMediaTimeSlice = (TuSdkMediaTimeSlice)localIterator.next();
        TuSdkMediaTimeSliceEntity localTuSdkMediaTimeSliceEntity2 = new TuSdkMediaTimeSliceEntity(localTuSdkMediaTimeSlice);
        a(localTuSdkMediaTimeSliceEntity2, paramTuSdkMediaExtractor, paramBoolean1, paramBoolean2);
        if (!paramBoolean1)
        {
          if (localTuSdkMediaTimeSliceEntity2.startUs < l2) {
            localTuSdkMediaTimeSliceEntity2.startUs = l2;
          }
          if (localTuSdkMediaTimeSliceEntity2.isReverse()) {}
        }
        else if (localTuSdkMediaTimeSliceEntity2.startUs != localTuSdkMediaTimeSliceEntity2.endUs)
        {
          l2 = localTuSdkMediaTimeSliceEntity2.endUs;
          this.b += Math.floor((float)Math.abs(localTuSdkMediaTimeSliceEntity2.reduce()) / localTuSdkMediaTimeSliceEntity2.speed);
          localTuSdkMediaTimeSliceEntity2.outputEndUs = this.b;
          this.e += Math.abs(localTuSdkMediaTimeSliceEntity2.reduce());
          localTuSdkMediaTimeSliceEntity2.realTimeEndUs = this.e;
          this.a.add(localTuSdkMediaTimeSliceEntity2);
          localTuSdkMediaTimeSliceEntity2.index = i;
          localTuSdkMediaTimeSliceEntity2.previous = ((TuSdkMediaTimeSliceEntity)localObject);
          if (localObject != null)
          {
            localTuSdkMediaTimeSliceEntity2.outputStartUs = ((TuSdkMediaTimeSliceEntity)localObject).outputEndUs;
            localTuSdkMediaTimeSliceEntity2.realTimeStarUs = ((TuSdkMediaTimeSliceEntity)localObject).realTimeEndUs;
            ((TuSdkMediaTimeSliceEntity)localObject).next = localTuSdkMediaTimeSliceEntity2;
          }
          localObject = localTuSdkMediaTimeSliceEntity2;
          i++;
        }
      }
    }
    paramTuSdkMediaExtractor.seekTo(l1);
    this.c = true;
  }
  
  private void a(TuSdkMediaTimeSliceEntity paramTuSdkMediaTimeSliceEntity, TuSdkMediaExtractor paramTuSdkMediaExtractor, boolean paramBoolean1, boolean paramBoolean2)
  {
    if (paramTuSdkMediaTimeSliceEntity.speed <= 0.0F) {
      paramTuSdkMediaTimeSliceEntity.speed = 1.0F;
    }
    if (paramTuSdkMediaTimeSliceEntity.startUs < 0L) {
      paramTuSdkMediaTimeSliceEntity.startUs = 0L;
    }
    if (paramTuSdkMediaTimeSliceEntity.startUs > getInputDurationUs()) {
      paramTuSdkMediaTimeSliceEntity.startUs = getInputDurationUs();
    }
    if ((paramTuSdkMediaTimeSliceEntity.endUs < 0L) || (paramTuSdkMediaTimeSliceEntity.endUs > getInputDurationUs())) {
      paramTuSdkMediaTimeSliceEntity.endUs = getInputDurationUs();
    }
    if (paramTuSdkMediaTimeSliceEntity.startUs == paramTuSdkMediaTimeSliceEntity.endUs) {
      return;
    }
    if (paramBoolean1)
    {
      paramTuSdkMediaTimeSliceEntity.endUs = paramTuSdkMediaExtractor.seekTo(paramTuSdkMediaTimeSliceEntity.isReverse() ? paramTuSdkMediaTimeSliceEntity.endUs : paramTuSdkMediaTimeSliceEntity.endUs - 1L);
      paramTuSdkMediaTimeSliceEntity.startUs = paramTuSdkMediaExtractor.seekTo(paramTuSdkMediaTimeSliceEntity.isReverse() ? paramTuSdkMediaTimeSliceEntity.startUs - 1L : paramTuSdkMediaTimeSliceEntity.startUs);
    }
    else if (paramTuSdkMediaTimeSliceEntity.isReverse())
    {
      long l = paramTuSdkMediaTimeSliceEntity.startUs;
      paramTuSdkMediaTimeSliceEntity.startUs = paramTuSdkMediaTimeSliceEntity.endUs;
      paramTuSdkMediaTimeSliceEntity.endUs = l;
    }
    paramTuSdkMediaTimeSliceEntity.taskID = this.mTaskID;
  }
  
  public List<TuSdkMediaTimeSliceEntity> getFinalSlices()
  {
    return this.a;
  }
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKCore-3.1.0.jar!\org\lasque\tusdk\core\media\codec\extend\TuSdkMediaFileCuterTimeline.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */