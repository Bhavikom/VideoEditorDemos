package org.lasque.tusdk.video.editor;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class TuSdkMediaEffectLinkedMap
  extends ConcurrentHashMap<TuSdkMediaEffectData.TuSdkMediaEffectDataType, List<TuSdkMediaEffectData>>
{
  private LinkedList<TuSdkMediaEffectData> a = new LinkedList();
  private long b = 66666L;
  
  public TuSdkMediaEffectLinkedMap()
  {
    for (TuSdkMediaEffectData.TuSdkMediaEffectDataType localTuSdkMediaEffectDataType : TuSdkMediaEffectData.TuSdkMediaEffectDataType.values()) {
      put(localTuSdkMediaEffectDataType, new ArrayList());
    }
  }
  
  public void resetMediaEffects()
  {
    Iterator localIterator1 = values().iterator();
    while (localIterator1.hasNext())
    {
      List localList = (List)localIterator1.next();
      Iterator localIterator2 = localList.iterator();
      while (localIterator2.hasNext())
      {
        TuSdkMediaEffectData localTuSdkMediaEffectData = (TuSdkMediaEffectData)localIterator2.next();
        localTuSdkMediaEffectData.setIsApplied(false);
      }
    }
  }
  
  public void putMediaEffectData(TuSdkMediaEffectData.TuSdkMediaEffectDataType paramTuSdkMediaEffectDataType, TuSdkMediaEffectData paramTuSdkMediaEffectData)
  {
    ((List)get(paramTuSdkMediaEffectDataType)).add(paramTuSdkMediaEffectData);
    this.a.add(paramTuSdkMediaEffectData);
  }
  
  public boolean deleteMediaEffectData(TuSdkMediaEffectData.TuSdkMediaEffectDataType paramTuSdkMediaEffectDataType, TuSdkMediaEffectData paramTuSdkMediaEffectData)
  {
    boolean bool = ((List)get(paramTuSdkMediaEffectDataType)).remove(paramTuSdkMediaEffectData);
    this.a.remove(paramTuSdkMediaEffectData);
    return bool;
  }
  
  public void clearByType(TuSdkMediaEffectData.TuSdkMediaEffectDataType paramTuSdkMediaEffectDataType)
  {
    List localList = (List)get(paramTuSdkMediaEffectDataType);
    if (localList != null) {
      this.a.removeAll(localList);
    }
    ((List)get(paramTuSdkMediaEffectDataType)).clear();
  }
  
  public void clear()
  {
    super.clear();
    this.a.clear();
  }
  
  public List<TuSdkMediaEffectData> getAllMediaEffectData()
  {
    ArrayList localArrayList = new ArrayList();
    Iterator localIterator = values().iterator();
    while (localIterator.hasNext())
    {
      List localList = (List)localIterator.next();
      localArrayList.addAll(localList);
    }
    return localArrayList;
  }
  
  public void clearMediaEffects()
  {
    Iterator localIterator = values().iterator();
    while (localIterator.hasNext())
    {
      List localList = (List)localIterator.next();
      localList.clear();
    }
    this.a.clear();
  }
  
  public TuSdkMediaEffectApply seekTimeUs(long paramLong)
  {
    TuSdkMediaEffectApply localTuSdkMediaEffectApply = new TuSdkMediaEffectApply();
    Iterator localIterator = entrySet().iterator();
    while (localIterator.hasNext())
    {
      Map.Entry localEntry = (Map.Entry)localIterator.next();
      List localList = (List)localEntry.getValue();
      TuSdkMediaEffectData.TuSdkMediaEffectDataType localTuSdkMediaEffectDataType = (TuSdkMediaEffectData.TuSdkMediaEffectDataType)localEntry.getKey();
      if (localTuSdkMediaEffectDataType == TuSdkMediaEffectData.TuSdkMediaEffectDataType.TuSdkMediaEffectDataTypeScene) {
        a(paramLong, localList, localTuSdkMediaEffectApply);
      } else {
        b(paramLong, localList, localTuSdkMediaEffectApply);
      }
    }
    return localTuSdkMediaEffectApply;
  }
  
  private void a(long paramLong, List<TuSdkMediaEffectData> paramList, TuSdkMediaEffectApply paramTuSdkMediaEffectApply)
  {
    ArrayList localArrayList = new ArrayList();
    Iterator localIterator = paramList.iterator();
    while (localIterator.hasNext())
    {
      TuSdkMediaEffectData localTuSdkMediaEffectData = (TuSdkMediaEffectData)localIterator.next();
      if (a(localTuSdkMediaEffectData, paramLong)) {
        localArrayList.add(localTuSdkMediaEffectData);
      } else {
        paramTuSdkMediaEffectApply.b.add(localTuSdkMediaEffectData);
      }
    }
    for (int i = 0; i < localArrayList.size(); i++) {
      if (i == localArrayList.size() - 1) {
        paramTuSdkMediaEffectApply.a.add(localArrayList.get(i));
      } else {
        paramTuSdkMediaEffectApply.b.add(localArrayList.get(i));
      }
    }
  }
  
  private void b(long paramLong, List<TuSdkMediaEffectData> paramList, TuSdkMediaEffectApply paramTuSdkMediaEffectApply)
  {
    Iterator localIterator = paramList.iterator();
    while (localIterator.hasNext())
    {
      TuSdkMediaEffectData localTuSdkMediaEffectData = (TuSdkMediaEffectData)localIterator.next();
      if (a(localTuSdkMediaEffectData, paramLong))
      {
        if (localTuSdkMediaEffectData.getMediaEffectType() == TuSdkMediaEffectData.TuSdkMediaEffectDataType.TuSdkMediaEffectDataTypeStickerAudio)
        {
          TuSdkMediaStickerAudioEffectData localTuSdkMediaStickerAudioEffectData = (TuSdkMediaStickerAudioEffectData)localTuSdkMediaEffectData;
          paramTuSdkMediaEffectApply.a.add(localTuSdkMediaStickerAudioEffectData.getMediaStickerEffectData());
          paramTuSdkMediaEffectApply.a.add(localTuSdkMediaStickerAudioEffectData.getMediaAudioEffectData());
        }
        else
        {
          paramTuSdkMediaEffectApply.a.add(localTuSdkMediaEffectData);
        }
      }
      else
      {
        localTuSdkMediaEffectData.setIsApplied(false);
        paramTuSdkMediaEffectApply.b.add(localTuSdkMediaEffectData);
      }
    }
  }
  
  private boolean a(TuSdkMediaEffectData paramTuSdkMediaEffectData, long paramLong)
  {
    if (paramTuSdkMediaEffectData.getAtTimeRange() == null) {
      return true;
    }
    if (paramTuSdkMediaEffectData.validateTimeRange())
    {
      TuSdkTimeRange localTuSdkTimeRange = paramTuSdkMediaEffectData.getAtTimeRange();
      if (!localTuSdkTimeRange.isValid()) {
        return false;
      }
      long l = localTuSdkTimeRange.getEndTimeUS() == Long.MAX_VALUE ? localTuSdkTimeRange.getEndTimeUS() : localTuSdkTimeRange.getEndTimeUS() + this.b;
      return (localTuSdkTimeRange.getStartTimeUS() <= paramLong) && (l >= paramLong);
    }
    return false;
  }
  
  public LinkedList<TuSdkMediaEffectData> getApplyMediaEffectDataList(TuSdkMediaEffectData.TuSdkMediaEffectDataType[] paramArrayOfTuSdkMediaEffectDataType)
  {
    LinkedList localLinkedList = new LinkedList();
    for (Object localObject2 : paramArrayOfTuSdkMediaEffectDataType)
    {
      List localList = (List)get(localObject2);
      Iterator localIterator = localList.iterator();
      while (localIterator.hasNext())
      {
        TuSdkMediaEffectData localTuSdkMediaEffectData = (TuSdkMediaEffectData)localIterator.next();
        if ((localTuSdkMediaEffectData instanceof TuSdkMediaStickerAudioEffectData))
        {
          localLinkedList.add(((TuSdkMediaStickerAudioEffectData)localTuSdkMediaEffectData).getMediaStickerEffectData());
          localLinkedList.add(((TuSdkMediaStickerAudioEffectData)localTuSdkMediaEffectData).getMediaAudioEffectData());
        }
        else
        {
          localLinkedList.add(localTuSdkMediaEffectData);
        }
      }
    }
    ??? = (LinkedList)this.a.clone();
    ((LinkedList)???).removeAll(localLinkedList);
    localLinkedList.addAll((Collection)???);
    return localLinkedList;
  }
  
  public class TuSdkMediaEffectApply
  {
    List<TuSdkMediaEffectData> a = new ArrayList();
    List<TuSdkMediaEffectData> b = new ArrayList();
    
    public TuSdkMediaEffectApply() {}
  }
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKVideo-3.4.1.jar!\org\lasque\tusdk\video\editor\TuSdkMediaEffectLinkedMap.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */