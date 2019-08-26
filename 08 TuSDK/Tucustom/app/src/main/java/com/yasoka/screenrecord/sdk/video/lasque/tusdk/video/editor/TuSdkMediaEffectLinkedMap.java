// 
// Decompiled by Procyon v0.5.36
// 

package com.yasoka.screenrecord.sdk.video.lasque.tusdk.video.editor;

import java.util.Iterator;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public class TuSdkMediaEffectLinkedMap extends ConcurrentHashMap<TuSdkMediaEffectData.TuSdkMediaEffectDataType, List<TuSdkMediaEffectData>>
{
    private LinkedList<TuSdkMediaEffectData> a;
    private long b;
    
    public TuSdkMediaEffectLinkedMap() {
        this.b = 66666L;
        this.a = new LinkedList<TuSdkMediaEffectData>();
        final TuSdkMediaEffectData.TuSdkMediaEffectDataType[] values = TuSdkMediaEffectData.TuSdkMediaEffectDataType.values();
        for (int length = values.length, i = 0; i < length; ++i) {
            (this).put(values[i], new ArrayList<TuSdkMediaEffectData>());
        }
    }
    
    public void resetMediaEffects() {
        final Iterator<List<TuSdkMediaEffectData>> iterator = (this).values().iterator();
        while (iterator.hasNext()) {
            final Iterator<TuSdkMediaEffectData> iterator2 = iterator.next().iterator();
            while (iterator2.hasNext()) {
                iterator2.next().setIsApplied(false);
            }
        }
    }
    
    public void putMediaEffectData(final TuSdkMediaEffectData.TuSdkMediaEffectDataType key, final TuSdkMediaEffectData e) {
        (this).get(key).add(e);
        this.a.add(e);
    }
    
    public boolean deleteMediaEffectData(final TuSdkMediaEffectData.TuSdkMediaEffectDataType key, final TuSdkMediaEffectData o) {
        final boolean remove = (this).get(key).remove(o);
        this.a.remove(o);
        return remove;
    }
    
    public void clearByType(final TuSdkMediaEffectData.TuSdkMediaEffectDataType tuSdkMediaEffectDataType) {
        final List<?> c = (this).get(tuSdkMediaEffectDataType);
        if (c != null) {
            this.a.removeAll(c);
        }
        (this).get(tuSdkMediaEffectDataType).clear();
    }
    
    @Override
    public void clear() {
        super.clear();
        this.a.clear();
    }
    
    public List<TuSdkMediaEffectData> getAllMediaEffectData() {
        final ArrayList<TuSdkMediaEffectData> list = new ArrayList<TuSdkMediaEffectData>();
        final Iterator<List<TuSdkMediaEffectData>> iterator = (this).values().iterator();
        while (iterator.hasNext()) {
            list.addAll(iterator.next());
        }
        return (List<TuSdkMediaEffectData>)list;
    }
    
    public void clearMediaEffects() {
        final Iterator<List<TuSdkMediaEffectData>> iterator = (this).values().iterator();
        while (iterator.hasNext()) {
            iterator.next().clear();
        }
        this.a.clear();
    }
    
    public TuSdkMediaEffectApply seekTimeUs(final long n) {
        final TuSdkMediaEffectApply tuSdkMediaEffectApply = new TuSdkMediaEffectApply();
        for (final Entry<TuSdkMediaEffectData.TuSdkMediaEffectDataType, List<TuSdkMediaEffectData>> entry : this.entrySet()) {
            final List<TuSdkMediaEffectData> list = entry.getValue();
            if (entry.getKey() == TuSdkMediaEffectData.TuSdkMediaEffectDataType.TuSdkMediaEffectDataTypeScene) {
                this.a(n, list, tuSdkMediaEffectApply);
            }
            else {
                this.b(n, list, tuSdkMediaEffectApply);
            }
        }
        return tuSdkMediaEffectApply;
    }
    
    private void a(final long n, final List<TuSdkMediaEffectData> list, final TuSdkMediaEffectApply tuSdkMediaEffectApply) {
        final ArrayList<TuSdkMediaEffectData> list2 = new ArrayList<TuSdkMediaEffectData>();
        for (final TuSdkMediaEffectData tuSdkMediaEffectData : list) {
            if (this.a(tuSdkMediaEffectData, n)) {
                list2.add(tuSdkMediaEffectData);
            }
            else {
                tuSdkMediaEffectApply.b.add(tuSdkMediaEffectData);
            }
        }
        for (int i = 0; i < list2.size(); ++i) {
            if (i == list2.size() - 1) {
                tuSdkMediaEffectApply.a.add((TuSdkMediaEffectData)list2.get(i));
            }
            else {
                tuSdkMediaEffectApply.b.add((TuSdkMediaEffectData)list2.get(i));
            }
        }
    }
    
    private void b(final long n, final List<TuSdkMediaEffectData> list, final TuSdkMediaEffectApply tuSdkMediaEffectApply) {
        for (final TuSdkMediaEffectData tuSdkMediaEffectData : list) {
            if (this.a(tuSdkMediaEffectData, n)) {
                if (tuSdkMediaEffectData.getMediaEffectType() == TuSdkMediaEffectData.TuSdkMediaEffectDataType.TuSdkMediaEffectDataTypeStickerAudio) {
                    final TuSdkMediaStickerAudioEffectData tuSdkMediaStickerAudioEffectData = (TuSdkMediaStickerAudioEffectData)tuSdkMediaEffectData;
                    tuSdkMediaEffectApply.a.add(tuSdkMediaStickerAudioEffectData.getMediaStickerEffectData());
                    tuSdkMediaEffectApply.a.add(tuSdkMediaStickerAudioEffectData.getMediaAudioEffectData());
                }
                else {
                    tuSdkMediaEffectApply.a.add(tuSdkMediaEffectData);
                }
            }
            else {
                tuSdkMediaEffectData.setIsApplied(false);
                tuSdkMediaEffectApply.b.add(tuSdkMediaEffectData);
            }
        }
    }
    
    private boolean a(final TuSdkMediaEffectData tuSdkMediaEffectData, final long n) {
        if (tuSdkMediaEffectData.getAtTimeRange() == null) {
            return true;
        }
        if (!tuSdkMediaEffectData.validateTimeRange()) {
            return false;
        }
        final TuSdkTimeRange atTimeRange = tuSdkMediaEffectData.getAtTimeRange();
        if (!atTimeRange.isValid()) {
            return false;
        }
        final long n2 = (atTimeRange.getEndTimeUS() == Long.MAX_VALUE) ? atTimeRange.getEndTimeUS() : (atTimeRange.getEndTimeUS() + this.b);
        return atTimeRange.getStartTimeUS() <= n && n2 >= n;
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
        final LinkedList c2 = (LinkedList)this.a.clone();
        c2.removeAll(localLinkedList);
        localLinkedList.addAll(c2);
        return localLinkedList;
    }
    
    public class TuSdkMediaEffectApply
    {
        List<TuSdkMediaEffectData> a;
        List<TuSdkMediaEffectData> b;
        
        public TuSdkMediaEffectApply() {
            this.a = new ArrayList<TuSdkMediaEffectData>();
            this.b = new ArrayList<TuSdkMediaEffectData>();
        }
    }
}
