// 
// Decompiled by Procyon v0.5.36
// 

package com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.media.codec.extend;

import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.TLog;

import java.util.Iterator;
//import org.lasque.tusdk.core.utils.TLog;
import java.util.ArrayList;
import java.util.List;
import java.io.Serializable;

public class TuSdkMediaTimeline implements Serializable
{
    protected final List<TuSdkMediaTimeSlice> mOrginSlices;
    protected float mOrginStartScaling;
    protected float mOrginEndScaling;
    private long a;
    protected long mTaskID;
    
    public TuSdkMediaTimeline() {
        this.mOrginSlices = new ArrayList<TuSdkMediaTimeSlice>();
        this.mOrginStartScaling = -1.0f;
        this.mOrginEndScaling = -1.0f;
        this.a = 0L;
    }
    
    public TuSdkMediaTimeline(final TuSdkMediaTimeline tuSdkMediaTimeline) {
        this.mOrginSlices = new ArrayList<TuSdkMediaTimeSlice>();
        this.mOrginStartScaling = -1.0f;
        this.mOrginEndScaling = -1.0f;
        this.a = 0L;
        this.copy(tuSdkMediaTimeline);
    }
    
    public TuSdkMediaTimeline(final float mOrginStartScaling, final float mOrginEndScaling) {
        this.mOrginSlices = new ArrayList<TuSdkMediaTimeSlice>();
        this.mOrginStartScaling = -1.0f;
        this.mOrginEndScaling = -1.0f;
        this.a = 0L;
        this.mOrginStartScaling = mOrginStartScaling;
        this.mOrginEndScaling = mOrginEndScaling;
    }
    
    public TuSdkMediaTimeline(final List<TuSdkMediaTimeSlice> list) {
        this.mOrginSlices = new ArrayList<TuSdkMediaTimeSlice>();
        this.mOrginStartScaling = -1.0f;
        this.mOrginEndScaling = -1.0f;
        this.a = 0L;
        if (list == null) {
            return;
        }
        this.mOrginSlices.addAll(list);
    }
    
    public TuSdkMediaTimeline(final TuSdkMediaTimeSlice tuSdkMediaTimeSlice) {
        this.mOrginSlices = new ArrayList<TuSdkMediaTimeSlice>();
        this.mOrginStartScaling = -1.0f;
        this.mOrginEndScaling = -1.0f;
        this.a = 0L;
        if (tuSdkMediaTimeSlice == null) {
            return;
        }
        this.mOrginSlices.add(tuSdkMediaTimeSlice);
    }
    
    public TuSdkMediaTimeline(final long n) {
        this.mOrginSlices = new ArrayList<TuSdkMediaTimeSlice>();
        this.mOrginStartScaling = -1.0f;
        this.mOrginEndScaling = -1.0f;
        this.a = 0L;
        this.append(n);
    }
    
    public long getInputDurationUs() {
        return this.a;
    }
    
    public void setInputDurationUs(final long a) {
        if (a < 1L) {
            TLog.w("%s setInputDurationUs need timeUs > 0.", "TuSdkMediaTimeline");
            return;
        }
        this.a = a;
    }
    
    public void copy(final TuSdkMediaTimeline tuSdkMediaTimeline) {
        if (tuSdkMediaTimeline == null) {
            return;
        }
        this.mOrginSlices.addAll(tuSdkMediaTimeline.mOrginSlices);
        this.mOrginStartScaling = tuSdkMediaTimeline.mOrginStartScaling;
        this.mOrginEndScaling = tuSdkMediaTimeline.mOrginEndScaling;
    }
    
    public void fresh(final TuSdkMediaTimeline tuSdkMediaTimeline) {
        this.mOrginSlices.clear();
        this.mOrginStartScaling = -1.0f;
        this.mOrginEndScaling = -1.0f;
        this.mTaskID = 0L;
        this.copy(tuSdkMediaTimeline);
    }
    
    public long getTaskID() {
        return this.mTaskID;
    }
    
    public void append(final long endUs) {
        if (this.mOrginSlices.size() < 1) {
            this.append(endUs, -1L);
            return;
        }
        this.append(this.mOrginSlices.get(this.mOrginSlices.size() - 1).endUs = endUs, -1L);
    }
    
    public void append(final long n, final long n2) {
        this.append(n, n2, 1.0f);
    }
    
    public void append(final long n, final long n2, final float n3) {
        this.append(new TuSdkMediaTimeSlice(n, n2, n3));
    }
    
    public void append(final TuSdkMediaTimeSlice tuSdkMediaTimeSlice) {
        if (tuSdkMediaTimeSlice == null) {
            return;
        }
        this.mOrginSlices.add(tuSdkMediaTimeSlice.clone());
    }
    
    public void remove(final TuSdkMediaTimeSlice tuSdkMediaTimeSlice) {
        if (tuSdkMediaTimeSlice == null) {
            return;
        }
        for (final TuSdkMediaTimeSlice tuSdkMediaTimeSlice2 : this.mOrginSlices) {
            if (tuSdkMediaTimeSlice2.equals(tuSdkMediaTimeSlice)) {
                this.mOrginSlices.remove(tuSdkMediaTimeSlice2);
                break;
            }
        }
    }
    
    public void remove(final List<TuSdkMediaTimeSlice> list) {
        if (list == null || list.size() < 1) {
            return;
        }
        final Iterator<TuSdkMediaTimeSlice> iterator = list.iterator();
        while (iterator.hasNext()) {
            this.remove(iterator.next());
        }
    }
    
    public void remove(final int n) {
        if (n < this.mOrginSlices.size()) {
            this.mOrginSlices.remove(n);
        }
    }
    
    public List<TuSdkMediaTimeSlice> getOrginSlices() {
        return this.mOrginSlices;
    }
    
    public int indexOfOrginSlices(final TuSdkMediaTimeSlice tuSdkMediaTimeSlice) {
        if (this.mOrginSlices == null || this.mOrginSlices.size() == 0) {
            return -1;
        }
        for (int i = 0; i < this.mOrginSlices.size(); ++i) {
            final TuSdkMediaTimeSlice tuSdkMediaTimeSlice2 = this.mOrginSlices.get(i);
            if (tuSdkMediaTimeSlice2.startUs == tuSdkMediaTimeSlice.startUs && tuSdkMediaTimeSlice2.endUs == tuSdkMediaTimeSlice.endUs) {
                return i;
            }
        }
        return -1;
    }
}
