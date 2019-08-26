package org.lasque.tusdk.core.media.codec.extend;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import org.lasque.tusdk.core.utils.TLog;

public class TuSdkMediaTimeEffectTimeline
  extends TuSdkMediaTimeline
{
  private final List<TuSdkMediaTimeSliceEntity> a = new ArrayList();
  private List<TuSdkMediaTimeSlice> b = new ArrayList();
  protected boolean isKeepOriginalLength = false;
  private long c = -1L;
  
  public void setInputAlignTimeSlices(List<TuSdkMediaTimeSliceEntity> paramList)
  {
    this.a.clear();
    this.a.addAll(paramList);
  }
  
  public void setKeepOriginalLength(boolean paramBoolean)
  {
    this.isKeepOriginalLength = paramBoolean;
  }
  
  public void setTimeLineMaxLengthUs(long paramLong)
  {
    this.c = paramLong;
  }
  
  public void setTimeSlice(TuSdkMediaTimeSlice paramTuSdkMediaTimeSlice, int paramInt)
  {
    this.b.clear();
    Iterator localIterator1 = this.a.iterator();
    while (localIterator1.hasNext())
    {
      TuSdkMediaTimeSliceEntity localTuSdkMediaTimeSliceEntity1 = (TuSdkMediaTimeSliceEntity)localIterator1.next();
      if (paramTuSdkMediaTimeSlice.startUs > localTuSdkMediaTimeSliceEntity1.outputEndUs)
      {
        this.b.add(localTuSdkMediaTimeSliceEntity1);
      }
      else
      {
        TuSdkMediaTimeSliceEntity localTuSdkMediaTimeSliceEntity2;
        long l1;
        Object localObject;
        if ((paramTuSdkMediaTimeSlice.startUs >= localTuSdkMediaTimeSliceEntity1.outputStartUs) && (paramTuSdkMediaTimeSlice.endUs <= localTuSdkMediaTimeSliceEntity1.outputEndUs))
        {
          TLog.d("in start : %s  end: %s", new Object[] { Long.valueOf(localTuSdkMediaTimeSliceEntity1.startUs), Long.valueOf(localTuSdkMediaTimeSliceEntity1.endUs) });
          localTuSdkMediaTimeSliceEntity2 = new TuSdkMediaTimeSliceEntity(localTuSdkMediaTimeSliceEntity1);
          if (paramTuSdkMediaTimeSlice.startUs > localTuSdkMediaTimeSliceEntity1.outputStartUs)
          {
            l1 = localTuSdkMediaTimeSliceEntity2.endUs = localTuSdkMediaTimeSliceEntity1.startUs + (paramTuSdkMediaTimeSlice.startUs - localTuSdkMediaTimeSliceEntity1.outputStartUs);
            this.b.add(localTuSdkMediaTimeSliceEntity2);
            localTuSdkMediaTimeSliceEntity2 = new TuSdkMediaTimeSliceEntity();
            localTuSdkMediaTimeSliceEntity2.startUs = l1;
          }
          if (paramTuSdkMediaTimeSlice.endUs <= localTuSdkMediaTimeSliceEntity1.outputEndUs)
          {
            l1 = localTuSdkMediaTimeSliceEntity2.endUs = localTuSdkMediaTimeSliceEntity2.startUs + (paramTuSdkMediaTimeSlice.endUs - paramTuSdkMediaTimeSlice.startUs);
            for (int i = 0; i < paramInt; i++)
            {
              TLog.d("%s count : %s  star %s  end %s", new Object[] { "TuSdkMediaTimeEffectTimeline", Integer.valueOf(i), Long.valueOf(localTuSdkMediaTimeSliceEntity2.startUs), Long.valueOf(localTuSdkMediaTimeSliceEntity2.endUs) });
              localTuSdkMediaTimeSliceEntity2.speed = paramTuSdkMediaTimeSlice.speed;
              localTuSdkMediaTimeSliceEntity2.overlapIndex = i;
              this.b.add(localTuSdkMediaTimeSliceEntity2.clone());
            }
            if (paramTuSdkMediaTimeSlice.endUs < localTuSdkMediaTimeSliceEntity1.outputEndUs)
            {
              localTuSdkMediaTimeSliceEntity2 = new TuSdkMediaTimeSliceEntity();
              localTuSdkMediaTimeSliceEntity2.startUs = l1;
              localTuSdkMediaTimeSliceEntity2.endUs = localTuSdkMediaTimeSliceEntity1.endUs;
              this.b.add(localTuSdkMediaTimeSliceEntity2);
            }
            localObject = sliceWithNextTimeUs(localTuSdkMediaTimeSliceEntity2.endUs);
            if ((localObject == null) || (((List)localObject).size() == 0))
            {
              TLog.d("%s not find next time slice!!", new Object[] { "TuSdkMediaTimeEffectTimeline" });
              a();
              return;
            }
            this.b.addAll((Collection)localObject);
          }
        }
        else if ((paramTuSdkMediaTimeSlice.startUs >= localTuSdkMediaTimeSliceEntity1.outputStartUs) && (paramTuSdkMediaTimeSlice.endUs > localTuSdkMediaTimeSliceEntity1.outputEndUs))
        {
          localTuSdkMediaTimeSliceEntity2 = new TuSdkMediaTimeSliceEntity(localTuSdkMediaTimeSliceEntity1);
          if (paramTuSdkMediaTimeSlice.startUs > localTuSdkMediaTimeSliceEntity1.outputStartUs)
          {
            l1 = localTuSdkMediaTimeSliceEntity2.endUs = localTuSdkMediaTimeSliceEntity1.startUs + (paramTuSdkMediaTimeSlice.startUs - localTuSdkMediaTimeSliceEntity1.outputStartUs);
            this.b.add(localTuSdkMediaTimeSliceEntity2);
            localTuSdkMediaTimeSliceEntity2 = new TuSdkMediaTimeSliceEntity();
            localTuSdkMediaTimeSliceEntity2.startUs = l1;
          }
          ArrayList localArrayList = new ArrayList();
          localTuSdkMediaTimeSliceEntity2.endUs = localTuSdkMediaTimeSliceEntity1.endUs;
          localArrayList.add(localTuSdkMediaTimeSliceEntity2);
          List localList1 = sliceWithTimeUs(localTuSdkMediaTimeSliceEntity2.endUs, paramTuSdkMediaTimeSlice.endUs);
          if ((localList1 == null) || (localList1.size() == 0)) {
            TLog.d("%s not find middle time slice!!", new Object[] { "TuSdkMediaTimeEffectTimeline" });
          }
          localArrayList.addAll(localList1);
          localObject = findSlice(paramTuSdkMediaTimeSlice.endUs);
          if ((((TuSdkMediaTimeSlice)localArrayList.get(localArrayList.size() - 1)).startUs <= ((TuSdkMediaTimeSliceEntity)localObject).outputStartUs) && (((TuSdkMediaTimeSlice)localArrayList.get(localArrayList.size() - 1)).endUs >= ((TuSdkMediaTimeSliceEntity)localObject).outputEndUs))
          {
            for (int j = 0; j < paramInt; j++)
            {
              Iterator localIterator2 = localArrayList.iterator();
              while (localIterator2.hasNext())
              {
                TuSdkMediaTimeSlice localTuSdkMediaTimeSlice1 = (TuSdkMediaTimeSlice)localIterator2.next();
                localTuSdkMediaTimeSlice1.speed = paramTuSdkMediaTimeSlice.speed;
                localTuSdkMediaTimeSlice1.overlapIndex = j;
                this.b.add(localTuSdkMediaTimeSlice1.clone());
              }
            }
          }
          else
          {
            localTuSdkMediaTimeSliceEntity2 = new TuSdkMediaTimeSliceEntity();
            localTuSdkMediaTimeSliceEntity2.startUs = ((TuSdkMediaTimeSliceEntity)localObject).startUs;
            long l2 = localTuSdkMediaTimeSliceEntity2.endUs = ((TuSdkMediaTimeSliceEntity)localObject).startUs + (paramTuSdkMediaTimeSlice.endUs - ((TuSdkMediaTimeSliceEntity)localObject).outputStartUs);
            localArrayList.add(localTuSdkMediaTimeSliceEntity2);
            for (int k = 0; k < paramInt; k++)
            {
              Iterator localIterator3 = localArrayList.iterator();
              while (localIterator3.hasNext())
              {
                TuSdkMediaTimeSlice localTuSdkMediaTimeSlice2 = (TuSdkMediaTimeSlice)localIterator3.next();
                localTuSdkMediaTimeSlice2.speed = paramTuSdkMediaTimeSlice.speed;
                localTuSdkMediaTimeSlice2.overlapIndex = k;
                this.b.add(localTuSdkMediaTimeSlice2.clone());
              }
              TLog.d("%s add repeat times : %s", new Object[] { "TuSdkMediaTimeEffectTimeline", Integer.valueOf(localArrayList.size()) });
            }
            localTuSdkMediaTimeSliceEntity2 = new TuSdkMediaTimeSliceEntity();
            localTuSdkMediaTimeSliceEntity2.startUs = l2;
            localTuSdkMediaTimeSliceEntity2.endUs = ((TuSdkMediaTimeSliceEntity)localObject).endUs;
            this.b.add(localTuSdkMediaTimeSliceEntity2);
            if ((((TuSdkMediaTimeSlice)localArrayList.get(localArrayList.size() - 1)).startUs <= ((TuSdkMediaTimeSliceEntity)localObject).outputStartUs) || (((TuSdkMediaTimeSlice)localArrayList.get(localArrayList.size() - 1)).startUs < ((TuSdkMediaTimeSliceEntity)localObject).outputEndUs))
            {
              List localList2 = sliceWithNextTimeUs(localTuSdkMediaTimeSliceEntity2.endUs);
              if ((localList2 == null) || (localList2.size() == 0)) {
                TLog.d("%s not find next time slice!!", new Object[] { "TuSdkMediaTimeEffectTimeline" });
              } else {
                this.b.addAll(localList2);
              }
            }
          }
        }
      }
    }
    a();
    TLog.d("%s after calculation %s", new Object[] { "TuSdkMediaTimeEffectTimeline", Integer.valueOf(this.b.size()) });
  }
  
  public void reversTimeLine()
  {
    this.b.clear();
    LinkedList localLinkedList = new LinkedList();
    Iterator localIterator = this.a.iterator();
    while (localIterator.hasNext())
    {
      TuSdkMediaTimeSliceEntity localTuSdkMediaTimeSliceEntity = (TuSdkMediaTimeSliceEntity)localIterator.next();
      localLinkedList.addFirst(new TuSdkMediaTimeSlice(localTuSdkMediaTimeSliceEntity.endUs, localTuSdkMediaTimeSliceEntity.startUs));
    }
    this.b.addAll(localLinkedList);
  }
  
  public List<TuSdkMediaTimeSliceEntity> sliceWithTimeUs(long paramLong1, long paramLong2)
  {
    ArrayList localArrayList = new ArrayList();
    if (this.a.size() < 1)
    {
      TLog.w("%s sliceWithOutputTimeUs() null ", new Object[] { "TuSdkMediaTimeEffectTimeline" });
      return null;
    }
    Iterator localIterator = this.a.iterator();
    while (localIterator.hasNext())
    {
      TuSdkMediaTimeSliceEntity localTuSdkMediaTimeSliceEntity = (TuSdkMediaTimeSliceEntity)localIterator.next();
      if ((localTuSdkMediaTimeSliceEntity.overviewOutput(paramLong2) == 1) && (localTuSdkMediaTimeSliceEntity.startUs >= paramLong1)) {
        localArrayList.add(localTuSdkMediaTimeSliceEntity);
      }
    }
    return localArrayList;
  }
  
  public List<TuSdkMediaTimeSliceEntity> sliceWithNextTimeUs(long paramLong)
  {
    ArrayList localArrayList = new ArrayList();
    if (this.a.size() < 1)
    {
      TLog.d("%s sliceWithOutputTimeUs() null ", new Object[] { "TuSdkMediaTimeEffectTimeline" });
      return null;
    }
    Iterator localIterator = this.a.iterator();
    while (localIterator.hasNext())
    {
      TuSdkMediaTimeSliceEntity localTuSdkMediaTimeSliceEntity = (TuSdkMediaTimeSliceEntity)localIterator.next();
      if (paramLong <= localTuSdkMediaTimeSliceEntity.startUs) {
        localArrayList.add(localTuSdkMediaTimeSliceEntity);
      }
    }
    return localArrayList;
  }
  
  public TuSdkMediaTimeSliceEntity findSlice(long paramLong)
  {
    if (this.a.size() < 1)
    {
      TLog.e("sliceWithOutputTimeUs() null ", new Object[0]);
      return null;
    }
    Iterator localIterator = this.a.iterator();
    while (localIterator.hasNext())
    {
      TuSdkMediaTimeSliceEntity localTuSdkMediaTimeSliceEntity = (TuSdkMediaTimeSliceEntity)localIterator.next();
      if (localTuSdkMediaTimeSliceEntity.overviewOutput(paramLong) == 0) {
        return localTuSdkMediaTimeSliceEntity;
      }
    }
    return (TuSdkMediaTimeSliceEntity)this.a.get(this.a.size() - 1);
  }
  
  public List<TuSdkMediaTimeSlice> getTimeEffectList()
  {
    return this.b;
  }
  
  private void a()
  {
    if (!this.isKeepOriginalLength) {
      return;
    }
    long l1;
    if (this.c == -1L) {
      l1 = a(this.a);
    } else {
      l1 = this.c;
    }
    ArrayList localArrayList = new ArrayList();
    TLog.d("%s total length :%s", new Object[] { "TuSdkMediaTimeEffectTimeline", Long.valueOf(l1) });
    Iterator localIterator = this.b.iterator();
    while (localIterator.hasNext())
    {
      TuSdkMediaTimeSlice localTuSdkMediaTimeSlice = (TuSdkMediaTimeSlice)localIterator.next();
      long l2 = a(localArrayList);
      if (l2 == l1) {
        return;
      }
      if (l2 < l1)
      {
        if (l2 + (localTuSdkMediaTimeSlice.endUs - localTuSdkMediaTimeSlice.startUs) > l1)
        {
          TuSdkMediaTimeSliceEntity localTuSdkMediaTimeSliceEntity = new TuSdkMediaTimeSliceEntity();
          localTuSdkMediaTimeSliceEntity.startUs = localTuSdkMediaTimeSlice.startUs;
          localTuSdkMediaTimeSliceEntity.endUs = (localTuSdkMediaTimeSlice.startUs + l1 - l2);
          localArrayList.add(new TuSdkMediaTimeSliceEntity(localTuSdkMediaTimeSliceEntity));
          TLog.d("%s  after cut length :%s", new Object[] { "TuSdkMediaTimeEffectTimeline", Long.valueOf(a(localArrayList)) });
          return;
        }
        localArrayList.add(new TuSdkMediaTimeSliceEntity(localTuSdkMediaTimeSlice));
      }
    }
    TLog.d("%s after cut length :%s", new Object[] { "TuSdkMediaTimeEffectTimeline", Long.valueOf(a(localArrayList)) });
    this.b.clear();
    this.b.addAll(localArrayList);
  }
  
  private long a(List<TuSdkMediaTimeSliceEntity> paramList)
  {
    long l = 0L;
    Iterator localIterator = paramList.iterator();
    while (localIterator.hasNext())
    {
      TuSdkMediaTimeSliceEntity localTuSdkMediaTimeSliceEntity = (TuSdkMediaTimeSliceEntity)localIterator.next();
      l += localTuSdkMediaTimeSliceEntity.endUs - localTuSdkMediaTimeSliceEntity.startUs;
    }
    return l;
  }
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKCore-3.1.0.jar!\org\lasque\tusdk\core\media\codec\extend\TuSdkMediaTimeEffectTimeline.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */