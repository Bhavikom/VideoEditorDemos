// 
// Decompiled by Procyon v0.5.36
// 

package com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.media.codec.extend;

import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.TLog;

import java.util.LinkedList;
//import org.lasque.tusdk.core.utils.TLog;
import java.util.Collection;
import java.util.ArrayList;
import java.util.List;

public class TuSdkMediaTimeEffectTimeline extends TuSdkMediaTimeline
{
    private final List<TuSdkMediaTimeSliceEntity> a;
    private List<TuSdkMediaTimeSlice> b;
    protected boolean isKeepOriginalLength;
    private long c;
    
    public TuSdkMediaTimeEffectTimeline() {
        this.a = new ArrayList<TuSdkMediaTimeSliceEntity>();
        this.b = new ArrayList<TuSdkMediaTimeSlice>();
        this.isKeepOriginalLength = false;
        this.c = -1L;
    }
    
    public void setInputAlignTimeSlices(final List<TuSdkMediaTimeSliceEntity> list) {
        this.a.clear();
        this.a.addAll(list);
    }
    
    public void setKeepOriginalLength(final boolean isKeepOriginalLength) {
        this.isKeepOriginalLength = isKeepOriginalLength;
    }
    
    public void setTimeLineMaxLengthUs(final long c) {
        this.c = c;
    }
    
    public void setTimeSlice(final TuSdkMediaTimeSlice tuSdkMediaTimeSlice, final int n) {
        this.b.clear();
        for (final TuSdkMediaTimeSliceEntity tuSdkMediaTimeSliceEntity : this.a) {
            if (tuSdkMediaTimeSlice.startUs > tuSdkMediaTimeSliceEntity.outputEndUs) {
                this.b.add(tuSdkMediaTimeSliceEntity);
            }
            else if (tuSdkMediaTimeSlice.startUs >= tuSdkMediaTimeSliceEntity.outputStartUs && tuSdkMediaTimeSlice.endUs <= tuSdkMediaTimeSliceEntity.outputEndUs) {
                TLog.d("in start : %s  end: %s", tuSdkMediaTimeSliceEntity.startUs, tuSdkMediaTimeSliceEntity.endUs);
                TuSdkMediaTimeSliceEntity tuSdkMediaTimeSliceEntity2 = new TuSdkMediaTimeSliceEntity(tuSdkMediaTimeSliceEntity);
                if (tuSdkMediaTimeSlice.startUs > tuSdkMediaTimeSliceEntity.outputStartUs) {
                    final TuSdkMediaTimeSliceEntity tuSdkMediaTimeSliceEntity3 = tuSdkMediaTimeSliceEntity2;
                    final long endUs = tuSdkMediaTimeSliceEntity.startUs + (tuSdkMediaTimeSlice.startUs - tuSdkMediaTimeSliceEntity.outputStartUs);
                    tuSdkMediaTimeSliceEntity3.endUs = endUs;
                    final long startUs = endUs;
                    this.b.add(tuSdkMediaTimeSliceEntity2);
                    tuSdkMediaTimeSliceEntity2 = new TuSdkMediaTimeSliceEntity();
                    tuSdkMediaTimeSliceEntity2.startUs = startUs;
                }
                if (tuSdkMediaTimeSlice.endUs > tuSdkMediaTimeSliceEntity.outputEndUs) {
                    continue;
                }
                final TuSdkMediaTimeSliceEntity tuSdkMediaTimeSliceEntity4 = tuSdkMediaTimeSliceEntity2;
                final long endUs2 = tuSdkMediaTimeSliceEntity2.startUs + (tuSdkMediaTimeSlice.endUs - tuSdkMediaTimeSlice.startUs);
                tuSdkMediaTimeSliceEntity4.endUs = endUs2;
                final long startUs2 = endUs2;
                for (int i = 0; i < n; ++i) {
                    TLog.d("%s count : %s  star %s  end %s", "TuSdkMediaTimeEffectTimeline", i, tuSdkMediaTimeSliceEntity2.startUs, tuSdkMediaTimeSliceEntity2.endUs);
                    tuSdkMediaTimeSliceEntity2.speed = tuSdkMediaTimeSlice.speed;
                    tuSdkMediaTimeSliceEntity2.overlapIndex = i;
                    this.b.add(tuSdkMediaTimeSliceEntity2.clone());
                }
                if (tuSdkMediaTimeSlice.endUs < tuSdkMediaTimeSliceEntity.outputEndUs) {
                    tuSdkMediaTimeSliceEntity2 = new TuSdkMediaTimeSliceEntity();
                    tuSdkMediaTimeSliceEntity2.startUs = startUs2;
                    tuSdkMediaTimeSliceEntity2.endUs = tuSdkMediaTimeSliceEntity.endUs;
                    this.b.add(tuSdkMediaTimeSliceEntity2);
                }
                final List<TuSdkMediaTimeSliceEntity> sliceWithNextTimeUs = this.sliceWithNextTimeUs(tuSdkMediaTimeSliceEntity2.endUs);
                if (sliceWithNextTimeUs == null || sliceWithNextTimeUs.size() == 0) {
                    TLog.d("%s not find next time slice!!", "TuSdkMediaTimeEffectTimeline");
                    this.a();
                    return;
                }
                this.b.addAll(sliceWithNextTimeUs);
            }
            else {
                if (tuSdkMediaTimeSlice.startUs < tuSdkMediaTimeSliceEntity.outputStartUs || tuSdkMediaTimeSlice.endUs <= tuSdkMediaTimeSliceEntity.outputEndUs) {
                    continue;
                }
                TuSdkMediaTimeSliceEntity tuSdkMediaTimeSliceEntity5 = new TuSdkMediaTimeSliceEntity(tuSdkMediaTimeSliceEntity);
                if (tuSdkMediaTimeSlice.startUs > tuSdkMediaTimeSliceEntity.outputStartUs) {
                    final TuSdkMediaTimeSliceEntity tuSdkMediaTimeSliceEntity6 = tuSdkMediaTimeSliceEntity5;
                    final long endUs3 = tuSdkMediaTimeSliceEntity.startUs + (tuSdkMediaTimeSlice.startUs - tuSdkMediaTimeSliceEntity.outputStartUs);
                    tuSdkMediaTimeSliceEntity6.endUs = endUs3;
                    final long startUs3 = endUs3;
                    this.b.add(tuSdkMediaTimeSliceEntity5);
                    tuSdkMediaTimeSliceEntity5 = new TuSdkMediaTimeSliceEntity();
                    tuSdkMediaTimeSliceEntity5.startUs = startUs3;
                }
                final ArrayList<TuSdkMediaTimeSlice> list = new ArrayList<TuSdkMediaTimeSlice>();
                tuSdkMediaTimeSliceEntity5.endUs = tuSdkMediaTimeSliceEntity.endUs;
                list.add(tuSdkMediaTimeSliceEntity5);
                final List<TuSdkMediaTimeSliceEntity> sliceWithTimeUs = this.sliceWithTimeUs(tuSdkMediaTimeSliceEntity5.endUs, tuSdkMediaTimeSlice.endUs);
                if (sliceWithTimeUs == null || sliceWithTimeUs.size() == 0) {
                    TLog.d("%s not find middle time slice!!", "TuSdkMediaTimeEffectTimeline");
                }
                list.addAll((Collection<? extends TuSdkMediaTimeSlice>) sliceWithTimeUs);
                final TuSdkMediaTimeSliceEntity slice = this.findSlice(tuSdkMediaTimeSlice.endUs);
                if (list.get(list.size() - 1).startUs <= slice.outputStartUs && list.get(list.size() - 1).endUs >= slice.outputEndUs) {
                    for (int j = 0; j < n; ++j) {
                        for (final TuSdkMediaTimeSlice tuSdkMediaTimeSlice2 : list) {
                            tuSdkMediaTimeSlice2.speed = tuSdkMediaTimeSlice.speed;
                            tuSdkMediaTimeSlice2.overlapIndex = j;
                            this.b.add(tuSdkMediaTimeSlice2.clone());
                        }
                    }
                }
                else {
                    final TuSdkMediaTimeSliceEntity tuSdkMediaTimeSliceEntity7 = new TuSdkMediaTimeSliceEntity();
                    tuSdkMediaTimeSliceEntity7.startUs = slice.startUs;
                    final TuSdkMediaTimeSliceEntity tuSdkMediaTimeSliceEntity8 = tuSdkMediaTimeSliceEntity7;
                    final long endUs4 = slice.startUs + (tuSdkMediaTimeSlice.endUs - slice.outputStartUs);
                    tuSdkMediaTimeSliceEntity8.endUs = endUs4;
                    final long startUs4 = endUs4;
                    list.add(tuSdkMediaTimeSliceEntity7);
                    for (int k = 0; k < n; ++k) {
                        for (final TuSdkMediaTimeSlice tuSdkMediaTimeSlice3 : list) {
                            tuSdkMediaTimeSlice3.speed = tuSdkMediaTimeSlice.speed;
                            tuSdkMediaTimeSlice3.overlapIndex = k;
                            this.b.add(tuSdkMediaTimeSlice3.clone());
                        }
                        TLog.d("%s add repeat times : %s", "TuSdkMediaTimeEffectTimeline", list.size());
                    }
                    final TuSdkMediaTimeSliceEntity tuSdkMediaTimeSliceEntity9 = new TuSdkMediaTimeSliceEntity();
                    tuSdkMediaTimeSliceEntity9.startUs = startUs4;
                    tuSdkMediaTimeSliceEntity9.endUs = slice.endUs;
                    this.b.add(tuSdkMediaTimeSliceEntity9);
                    if (list.get(list.size() - 1).startUs > slice.outputStartUs && list.get(list.size() - 1).startUs >= slice.outputEndUs) {
                        continue;
                    }
                    final List<TuSdkMediaTimeSliceEntity> sliceWithNextTimeUs2 = this.sliceWithNextTimeUs(tuSdkMediaTimeSliceEntity9.endUs);
                    if (sliceWithNextTimeUs2 == null || sliceWithNextTimeUs2.size() == 0) {
                        TLog.d("%s not find next time slice!!", "TuSdkMediaTimeEffectTimeline");
                    }
                    else {
                        this.b.addAll(sliceWithNextTimeUs2);
                    }
                }
            }
        }
        this.a();
        TLog.d("%s after calculation %s", "TuSdkMediaTimeEffectTimeline", this.b.size());
    }
    
    public void reversTimeLine() {
        this.b.clear();
        final LinkedList<TuSdkMediaTimeSlice> list = new LinkedList<TuSdkMediaTimeSlice>();
        for (final TuSdkMediaTimeSliceEntity tuSdkMediaTimeSliceEntity : this.a) {
            list.addFirst(new TuSdkMediaTimeSlice(tuSdkMediaTimeSliceEntity.endUs, tuSdkMediaTimeSliceEntity.startUs));
        }
        this.b.addAll(list);
    }
    
    public List<TuSdkMediaTimeSliceEntity> sliceWithTimeUs(final long n, final long n2) {
        final ArrayList<TuSdkMediaTimeSliceEntity> list = new ArrayList<TuSdkMediaTimeSliceEntity>();
        if (this.a.size() < 1) {
            TLog.w("%s sliceWithOutputTimeUs() null ", "TuSdkMediaTimeEffectTimeline");
            return null;
        }
        for (final TuSdkMediaTimeSliceEntity tuSdkMediaTimeSliceEntity : this.a) {
            if (tuSdkMediaTimeSliceEntity.overviewOutput(n2) == 1 && tuSdkMediaTimeSliceEntity.startUs >= n) {
                list.add(tuSdkMediaTimeSliceEntity);
            }
        }
        return list;
    }
    
    public List<TuSdkMediaTimeSliceEntity> sliceWithNextTimeUs(final long n) {
        final ArrayList<TuSdkMediaTimeSliceEntity> list = new ArrayList<TuSdkMediaTimeSliceEntity>();
        if (this.a.size() < 1) {
            TLog.d("%s sliceWithOutputTimeUs() null ", "TuSdkMediaTimeEffectTimeline");
            return null;
        }
        for (final TuSdkMediaTimeSliceEntity tuSdkMediaTimeSliceEntity : this.a) {
            if (n <= tuSdkMediaTimeSliceEntity.startUs) {
                list.add(tuSdkMediaTimeSliceEntity);
            }
        }
        return list;
    }
    
    public TuSdkMediaTimeSliceEntity findSlice(final long n) {
        if (this.a.size() < 1) {
            TLog.e("sliceWithOutputTimeUs() null ", new Object[0]);
            return null;
        }
        for (final TuSdkMediaTimeSliceEntity tuSdkMediaTimeSliceEntity : this.a) {
            if (tuSdkMediaTimeSliceEntity.overviewOutput(n) == 0) {
                return tuSdkMediaTimeSliceEntity;
            }
        }
        return this.a.get(this.a.size() - 1);
    }
    
    public List<TuSdkMediaTimeSlice> getTimeEffectList() {
        return this.b;
    }
    
    private void a() {
        if (!this.isKeepOriginalLength) {
            return;
        }
        long l;
        if (this.c == -1L) {
            l = this.a(this.a);
        }
        else {
            l = this.c;
        }
        final ArrayList<TuSdkMediaTimeSliceEntity> list = new ArrayList<TuSdkMediaTimeSliceEntity>();
        TLog.d("%s total length :%s", "TuSdkMediaTimeEffectTimeline", l);
        for (final TuSdkMediaTimeSlice tuSdkMediaTimeSlice : this.b) {
            final long a = this.a(list);
            if (a == l) {
                return;
            }
            if (a >= l) {
                continue;
            }
            if (a + (tuSdkMediaTimeSlice.endUs - tuSdkMediaTimeSlice.startUs) > l) {
                final TuSdkMediaTimeSliceEntity tuSdkMediaTimeSliceEntity = new TuSdkMediaTimeSliceEntity();
                tuSdkMediaTimeSliceEntity.startUs = tuSdkMediaTimeSlice.startUs;
                tuSdkMediaTimeSliceEntity.endUs = tuSdkMediaTimeSlice.startUs + l - a;
                list.add(new TuSdkMediaTimeSliceEntity(tuSdkMediaTimeSliceEntity));
                TLog.d("%s  after cut length :%s", "TuSdkMediaTimeEffectTimeline", this.a(list));
                return;
            }
            list.add(new TuSdkMediaTimeSliceEntity(tuSdkMediaTimeSlice));
        }
        TLog.d("%s after cut length :%s", "TuSdkMediaTimeEffectTimeline", this.a(list));
        this.b.clear();
        this.b.addAll(list);
    }
    
    private long a(final List<TuSdkMediaTimeSliceEntity> list) {
        long n = 0L;
        for (final TuSdkMediaTimeSliceEntity tuSdkMediaTimeSliceEntity : list) {
            n += tuSdkMediaTimeSliceEntity.endUs - tuSdkMediaTimeSliceEntity.startUs;
        }
        return n;
    }
}
