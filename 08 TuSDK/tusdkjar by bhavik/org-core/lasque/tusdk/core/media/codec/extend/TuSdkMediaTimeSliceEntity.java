package org.lasque.tusdk.core.media.codec.extend;

import java.util.List;

public class TuSdkMediaTimeSliceEntity
  extends TuSdkMediaTimeSlice
{
  public int index;
  public TuSdkMediaTimeSliceEntity previous;
  public TuSdkMediaTimeSliceEntity next;
  public long outputStartUs = 0L;
  public long outputEndUs = 0L;
  public long taskID = 0L;
  public long audioStartUs = 0L;
  public long audioEndUs = 0L;
  private boolean a = false;
  public long realTimeStarUs = 0L;
  public long realTimeEndUs = 0L;
  
  public TuSdkMediaTimeSliceEntity() {}
  
  public TuSdkMediaTimeSliceEntity(TuSdkMediaTimeSlice paramTuSdkMediaTimeSlice)
  {
    if (paramTuSdkMediaTimeSlice == null) {
      return;
    }
    this.startUs = paramTuSdkMediaTimeSlice.startUs;
    this.endUs = paramTuSdkMediaTimeSlice.endUs;
    this.speed = paramTuSdkMediaTimeSlice.speed;
    this.overlapIndex = paramTuSdkMediaTimeSlice.overlapIndex;
  }
  
  public TuSdkMediaTimeSliceEntity clone()
  {
    TuSdkMediaTimeSliceEntity localTuSdkMediaTimeSliceEntity = new TuSdkMediaTimeSliceEntity();
    localTuSdkMediaTimeSliceEntity.startUs = this.startUs;
    localTuSdkMediaTimeSliceEntity.endUs = this.endUs;
    localTuSdkMediaTimeSliceEntity.speed = this.speed;
    localTuSdkMediaTimeSliceEntity.overlapIndex = this.overlapIndex;
    return localTuSdkMediaTimeSliceEntity;
  }
  
  public void setAudioReverse(boolean paramBoolean)
  {
    this.a = paramBoolean;
  }
  
  public boolean isAudioReverse()
  {
    return this.a;
  }
  
  public int overview(long paramLong)
  {
    int i = 0;
    if (isReverse())
    {
      if (paramLong <= this.endUs) {
        i = 1;
      } else if (paramLong > this.startUs) {
        i = -1;
      }
    }
    else if (paramLong < this.startUs) {
      i = -1;
    } else if (paramLong >= this.endUs) {
      i = 1;
    }
    return i;
  }
  
  public int overviewAudio(long paramLong)
  {
    int i = 0;
    if ((isReverse()) && (isAudioReverse()))
    {
      if (paramLong <= this.audioEndUs) {
        i = 1;
      } else if (paramLong > this.audioStartUs) {
        i = -1;
      }
    }
    else if (paramLong < this.audioStartUs) {
      i = -1;
    } else if (paramLong >= this.audioEndUs) {
      i = 1;
    }
    return i;
  }
  
  public int overviewOutput(long paramLong)
  {
    int i = 0;
    if (paramLong < this.outputStartUs) {
      i = -1;
    } else if (paramLong >= this.outputEndUs) {
      i = 1;
    }
    return i;
  }
  
  public int overviewInput(long paramLong)
  {
    int i = 0;
    if (paramLong < this.startUs) {
      i = -1;
    } else if (paramLong >= this.endUs) {
      i = 1;
    }
    return i;
  }
  
  public int overviewAudioInput(long paramLong, boolean paramBoolean)
  {
    int i = 0;
    if (paramBoolean)
    {
      if (paramLong < this.startUs) {
        i = -1;
      } else if (paramLong >= this.endUs) {
        i = 1;
      }
      return i;
    }
    if (paramLong >= this.startUs) {
      i = -1;
    } else if (paramLong < this.endUs) {
      i = 1;
    }
    return i;
  }
  
  public int overviewRealTime(long paramLong)
  {
    int i = 0;
    if (paramLong < this.realTimeStarUs) {
      i = -1;
    } else if (paramLong > this.realTimeEndUs) {
      i = 1;
    }
    return i;
  }
  
  public long calOutputTimeUs(long paramLong)
  {
    long l = 0L;
    if (isReverse()) {
      l = this.outputStartUs + ((float)(this.startUs - paramLong) / this.speed);
    } else {
      l = this.outputStartUs + ((float)(paramLong - this.startUs) / this.speed);
    }
    if (l < 0L) {
      l = 0L;
    }
    return l;
  }
  
  public long calRealTimeOutputTimeUs(long paramLong)
  {
    long l = 0L;
    if (isReverse()) {
      l = this.outputStartUs + ((float)(this.realTimeEndUs - paramLong) / this.speed);
    } else {
      l = this.outputStartUs + ((float)(paramLong - this.realTimeStarUs) / this.speed);
    }
    if (l < 0L) {
      l = 0L;
    }
    return l;
  }
  
  public long calOutputAudioTimeUs(long paramLong)
  {
    long l = 0L;
    if (isReverse())
    {
      if (isAudioReverse()) {
        l = this.outputStartUs + ((float)(this.startUs - paramLong) / this.speed);
      } else {
        l = this.outputStartUs + ((float)(paramLong - this.endUs) / this.speed);
      }
    }
    else {
      l = this.outputStartUs + ((float)(paramLong - this.startUs) / this.speed);
    }
    return l;
  }
  
  public long calMutilOutputAudioTimeUs(long paramLong, TuSdkMediaFileCuterTimeline paramTuSdkMediaFileCuterTimeline)
  {
    TuSdkMediaTimeSliceEntity localTuSdkMediaTimeSliceEntity = paramTuSdkMediaFileCuterTimeline.sliceAudioWithInputTimeUs(paramLong, isAudioReverse());
    return localTuSdkMediaTimeSliceEntity.calOutputAudioTimeUs(paramLong);
  }
  
  public long calInputTimeUs(long paramLong)
  {
    if (paramLong < 0L) {
      return this.startUs;
    }
    if (paramLong <= this.outputStartUs) {
      return this.startUs;
    }
    if (paramLong >= this.outputEndUs) {
      return this.endUs;
    }
    long l = ((float)(paramLong - this.outputStartUs) * this.speed);
    if (isReverse()) {
      l = this.startUs - l;
    } else {
      l += this.startUs;
    }
    return l;
  }
  
  public long calInputTimeUs(long paramLong, TuSdkMediaFileCuterTimeline paramTuSdkMediaFileCuterTimeline)
  {
    if (paramLong < 0L) {
      return this.startUs;
    }
    if (paramLong <= this.outputStartUs) {
      return this.startUs;
    }
    if (paramLong >= this.outputEndUs) {
      return this.endUs;
    }
    long l = ((float)(paramLong - getPreTimeSliceRepetTimeUs(paramTuSdkMediaFileCuterTimeline)) * this.speed);
    if (isReverse()) {
      l = this.startUs - l;
    }
    return l;
  }
  
  public long calOutputNoRepetTimeUs(long paramLong, TuSdkMediaFileCuterTimeline paramTuSdkMediaFileCuterTimeline)
  {
    if (isReverse()) {
      paramLong = paramTuSdkMediaFileCuterTimeline.getOutputDurationUs() - paramLong;
    }
    long l2 = getPreTimeSliceRepetTimeUs(paramTuSdkMediaFileCuterTimeline);
    long l1;
    if (this.speed != 1.0F) {
      l1 = this.outputStartUs - getPreTimeSliceRepetTimeUs(paramTuSdkMediaFileCuterTimeline) + ((float)(calInputTimeUs(paramLong) - this.outputStartUs) * this.speed);
    } else {
      l1 = paramLong - getPreTimeSliceRepetTimeUs(paramTuSdkMediaFileCuterTimeline);
    }
    return l1;
  }
  
  public long calOutputHaveRepetTimeUs(long paramLong, TuSdkMediaFileCuterTimeline paramTuSdkMediaFileCuterTimeline)
  {
    long l = ((float)(paramLong + b(paramTuSdkMediaFileCuterTimeline)) / this.speed);
    return l;
  }
  
  public long calcOutputNoSlowTimeUs(long paramLong, TuSdkMediaFileCuterTimeline paramTuSdkMediaFileCuterTimeline)
  {
    long l = 0L;
    if (this.speed != 1.0F) {
      l = paramLong + a(paramTuSdkMediaFileCuterTimeline) + ((float)(calOutputTimeUs(paramLong) - this.outputStartUs) * this.speed);
    } else {
      l = paramLong + a(paramTuSdkMediaFileCuterTimeline);
    }
    return l;
  }
  
  private long a(TuSdkMediaFileCuterTimeline paramTuSdkMediaFileCuterTimeline)
  {
    long l1 = 0L;
    if (this.previous == null) {
      return l1;
    }
    int i = paramTuSdkMediaFileCuterTimeline.getFinalSlices().indexOf(this);
    for (int j = 0; j < i; j++)
    {
      TuSdkMediaTimeSlice localTuSdkMediaTimeSlice = (TuSdkMediaTimeSlice)paramTuSdkMediaFileCuterTimeline.getFinalSlices().get(j);
      if (localTuSdkMediaTimeSlice.speed != 1.0F)
      {
        long l2 = ((float)(localTuSdkMediaTimeSlice.endUs - localTuSdkMediaTimeSlice.startUs) / localTuSdkMediaTimeSlice.speed);
        l1 += l2 - (localTuSdkMediaTimeSlice.endUs - localTuSdkMediaTimeSlice.startUs);
      }
    }
    return l1;
  }
  
  public long getPreTimeSliceRepetTimeUs(TuSdkMediaFileCuterTimeline paramTuSdkMediaFileCuterTimeline)
  {
    long l = 0L;
    if (this.previous == null) {
      return l;
    }
    int i = paramTuSdkMediaFileCuterTimeline.getFinalSlices().indexOf(this);
    int j;
    TuSdkMediaTimeSliceEntity localTuSdkMediaTimeSliceEntity;
    if (this.overlapIndex > -1) {
      for (j = 0; j <= i; j++)
      {
        localTuSdkMediaTimeSliceEntity = (TuSdkMediaTimeSliceEntity)paramTuSdkMediaFileCuterTimeline.getFinalSlices().get(j);
        if ((localTuSdkMediaTimeSliceEntity.overlapIndex > -1) && (this.overlapIndex > localTuSdkMediaTimeSliceEntity.overlapIndex)) {
          l = ((float)l + (float)(localTuSdkMediaTimeSliceEntity.endUs - localTuSdkMediaTimeSliceEntity.startUs) / this.speed);
        }
      }
    } else {
      for (j = 0; j <= i; j++)
      {
        localTuSdkMediaTimeSliceEntity = (TuSdkMediaTimeSliceEntity)paramTuSdkMediaFileCuterTimeline.getFinalSlices().get(j);
        if (localTuSdkMediaTimeSliceEntity.overlapIndex > 0) {
          l = ((float)l + (float)(localTuSdkMediaTimeSliceEntity.endUs - localTuSdkMediaTimeSliceEntity.startUs) / this.speed);
        }
      }
    }
    return l;
  }
  
  private long b(TuSdkMediaFileCuterTimeline paramTuSdkMediaFileCuterTimeline)
  {
    long l = 0L;
    if (this.previous == null) {
      return l;
    }
    int i = paramTuSdkMediaFileCuterTimeline.getFinalSlices().indexOf(this);
    int j;
    TuSdkMediaTimeSlice localTuSdkMediaTimeSlice;
    if (this.overlapIndex > -1) {
      for (j = 0; j < paramTuSdkMediaFileCuterTimeline.getFinalSlices().size(); j++)
      {
        localTuSdkMediaTimeSlice = (TuSdkMediaTimeSlice)paramTuSdkMediaFileCuterTimeline.getFinalSlices().get(j);
        if (localTuSdkMediaTimeSlice.overlapIndex > 0) {
          l = ((float)l + (float)(localTuSdkMediaTimeSlice.endUs - localTuSdkMediaTimeSlice.startUs) / this.speed);
        }
      }
    } else {
      for (j = 0; j <= i; j++)
      {
        localTuSdkMediaTimeSlice = (TuSdkMediaTimeSlice)paramTuSdkMediaFileCuterTimeline.getFinalSlices().get(j);
        if (localTuSdkMediaTimeSlice.overlapIndex > 0) {
          l = ((float)l + (float)(localTuSdkMediaTimeSlice.endUs - localTuSdkMediaTimeSlice.startUs) / this.speed);
        }
      }
    }
    return l;
  }
  
  public long calOutputOrginTimeUs(long paramLong)
  {
    long l = this.outputStartUs + (paramLong - this.audioStartUs);
    return l;
  }
  
  public boolean equals(Object paramObject)
  {
    if ((paramObject == null) || (!(paramObject instanceof TuSdkMediaTimeSliceEntity))) {
      return false;
    }
    TuSdkMediaTimeSliceEntity localTuSdkMediaTimeSliceEntity = (TuSdkMediaTimeSliceEntity)paramObject;
    return (localTuSdkMediaTimeSliceEntity.startUs == this.startUs) && (localTuSdkMediaTimeSliceEntity.endUs == this.endUs) && (localTuSdkMediaTimeSliceEntity.speed == this.speed) && (localTuSdkMediaTimeSliceEntity.index == this.index);
  }
  
  public String toString()
  {
    StringBuffer localStringBuffer = new StringBuffer("TuSdkMediaTimeSliceEntity").append("{ \n");
    localStringBuffer.append("startUs: ").append(this.startUs).append(", \n");
    localStringBuffer.append("endUs: ").append(this.endUs).append(", \n");
    localStringBuffer.append("audioStartUs: ").append(this.audioStartUs).append(", \n");
    localStringBuffer.append("audioEndUs: ").append(this.audioEndUs).append(", \n");
    localStringBuffer.append("speed: ").append(this.speed).append(", \n");
    localStringBuffer.append("index: ").append(this.index).append(", \n");
    localStringBuffer.append("outputStartUs: ").append(this.outputStartUs).append(", \n");
    localStringBuffer.append("outputEndUs: ").append(this.outputEndUs).append(", \n");
    localStringBuffer.append("next: ").append(this.next).append(", \n");
    localStringBuffer.append("taskID: ").append(this.taskID).append(", \n");
    localStringBuffer.append("}");
    return localStringBuffer.toString();
  }
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKCore-3.1.0.jar!\org\lasque\tusdk\core\media\codec\extend\TuSdkMediaTimeSliceEntity.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */