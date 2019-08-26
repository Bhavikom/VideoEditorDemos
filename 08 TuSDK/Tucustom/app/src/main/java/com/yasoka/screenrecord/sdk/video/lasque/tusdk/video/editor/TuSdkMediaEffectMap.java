// 
// Decompiled by Procyon v0.5.36
// 

package com.yasoka.screenrecord.sdk.video.lasque.tusdk.video.editor;

import java.util.Iterator;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public class TuSdkMediaEffectMap extends ConcurrentHashMap<TuSdkMediaEffectData.TuSdkMediaEffectDataType, List<TuSdkMediaEffectData>>
{
    private long a;
    
    public TuSdkMediaEffectMap() {
        this.a = 66666L;
        final TuSdkMediaEffectData.TuSdkMediaEffectDataType[] values = TuSdkMediaEffectData.TuSdkMediaEffectDataType.values();
        for (int length = values.length, i = 0; i < length; ++i) {
            (this).put(values[i], new ArrayList<TuSdkMediaEffectData>());
        }
    }
    
    public void resetMediaEffects() {
        final Iterator<List<TuSdkMediaEffectData>> iterator = ((this).values().iterator());
        while (iterator.hasNext()) {
            final Iterator<TuSdkMediaEffectData> iterator2 = iterator.next().iterator();
            while (iterator2.hasNext()) {
                iterator2.next().setIsApplied(false);
            }
        }
    }
    
    public List<TuSdkMediaEffectData> getAllMediaEffectData() {
        final ArrayList<TuSdkMediaEffectData> list = new ArrayList<>();
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
        final long n2 = (atTimeRange.getEndTimeUS() == Long.MAX_VALUE) ? atTimeRange.getEndTimeUS() : (atTimeRange.getEndTimeUS() + this.a);
        return atTimeRange.getStartTimeUS() <= n && n2 >= n;
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
