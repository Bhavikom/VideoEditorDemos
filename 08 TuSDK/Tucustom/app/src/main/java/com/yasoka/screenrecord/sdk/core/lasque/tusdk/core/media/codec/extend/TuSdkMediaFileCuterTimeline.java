// 
// Decompiled by Procyon v0.5.36
// 

package com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.media.codec.extend;

//import org.lasque.tusdk.core.media.codec.TuSdkMediaExtractor;
//import org.lasque.tusdk.core.utils.TLog;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.media.codec.TuSdkMediaExtractor;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.TLog;

import java.util.Iterator;
import java.util.ArrayList;
import java.util.List;

public class TuSdkMediaFileCuterTimeline extends TuSdkMediaTimeline
{
    public static final int LINEAR = 0;
    public static final int OVERLAP = 1;
    private final List<TuSdkMediaTimeSliceEntity> a;
    private long b;
    private boolean c;
    private int d;
    private long e;
    
    public TuSdkMediaFileCuterTimeline() {
        this.a = new ArrayList<TuSdkMediaTimeSliceEntity>();
        this.b = 0L;
        this.c = false;
        this.d = 0;
        this.e = 0L;
    }
    
    public TuSdkMediaFileCuterTimeline(final int d) {
        this.a = new ArrayList<TuSdkMediaTimeSliceEntity>();
        this.b = 0L;
        this.c = false;
        this.d = 0;
        this.e = 0L;
        this.d = d;
    }
    
    public TuSdkMediaFileCuterTimeline(final int d, final TuSdkMediaTimeline tuSdkMediaTimeline) {
        this.a = new ArrayList<TuSdkMediaTimeSliceEntity>();
        this.b = 0L;
        this.c = false;
        this.d = 0;
        this.e = 0L;
        this.d = d;
        this.copy(tuSdkMediaTimeline);
    }
    
    public TuSdkMediaFileCuterTimeline(final TuSdkMediaTimeline tuSdkMediaTimeline) {
        super(tuSdkMediaTimeline);
        this.a = new ArrayList<TuSdkMediaTimeSliceEntity>();
        this.b = 0L;
        this.c = false;
        this.d = 0;
        this.e = 0L;
    }
    
    public TuSdkMediaFileCuterTimeline(final float n, final float n2) {
        super(n, n2);
        this.a = new ArrayList<TuSdkMediaTimeSliceEntity>();
        this.b = 0L;
        this.c = false;
        this.d = 0;
        this.e = 0L;
    }
    
    public TuSdkMediaFileCuterTimeline(final List<TuSdkMediaTimeSlice> list) {
        super(list);
        this.a = new ArrayList<TuSdkMediaTimeSliceEntity>();
        this.b = 0L;
        this.c = false;
        this.d = 0;
        this.e = 0L;
    }
    
    public TuSdkMediaFileCuterTimeline(final TuSdkMediaTimeSlice tuSdkMediaTimeSlice) {
        super(tuSdkMediaTimeSlice);
        this.a = new ArrayList<TuSdkMediaTimeSliceEntity>();
        this.b = 0L;
        this.c = false;
        this.d = 0;
        this.e = 0L;
    }
    
    public TuSdkMediaFileCuterTimeline(final long n) {
        super(n);
        this.a = new ArrayList<TuSdkMediaTimeSliceEntity>();
        this.b = 0L;
        this.c = false;
        this.d = 0;
        this.e = 0L;
    }
    
    public long getOutputDurationUs() {
        return this.b;
    }
    
    public long getRemoveOverSliceDurationUs() {
        return this.b - this.getOverSliceDuration();
    }
    
    public long getOverSliceDuration() {
        long n = 0L;
        for (final TuSdkMediaTimeSlice tuSdkMediaTimeSlice : this.mOrginSlices) {
            if (tuSdkMediaTimeSlice.overlapIndex > 0) {
                n += (long)(Math.abs(tuSdkMediaTimeSlice.endUs - tuSdkMediaTimeSlice.startUs) * tuSdkMediaTimeSlice.speed);
            }
        }
        return n;
    }
    
    public void setProgressOutputMode(final int d) {
        this.d = d;
    }
    
    public int getCalcMode() {
        return this.d;
    }
    
    @Override
    public void fresh(final TuSdkMediaTimeline tuSdkMediaTimeline) {
        this.reset();
        super.fresh(tuSdkMediaTimeline);
    }
    
    public void reset() {
        this.c = false;
        this.a.clear();
        this.b = 0L;
        this.e = 0L;
        this.mOrginStartScaling = -1.0f;
        this.mOrginEndScaling = -1.0f;
        this.mTaskID = 0L;
    }
    
    public TuSdkMediaTimeSliceEntity firstSlice() {
        if (this.a.size() < 1) {
            return null;
        }
        return this.a.get(0);
    }
    
    public TuSdkMediaTimeSliceEntity sliceWithOutputTimeUs(final long n) {
        final List<TuSdkMediaTimeSliceEntity> a = this.a;
        if (a.size() < 1) {
            TLog.e("sliceWithOutputTimeUs() null :%s", this.c);
            return null;
        }
        if (n < 1L) {
            return a.get(0);
        }
        if (n >= this.b) {
            return a.get(a.size() - 1);
        }
        for (final TuSdkMediaTimeSliceEntity tuSdkMediaTimeSliceEntity : a) {
            if (tuSdkMediaTimeSliceEntity.overviewOutput(n) == 0) {
                return tuSdkMediaTimeSliceEntity;
            }
        }
        return a.get(a.size() - 1);
    }
    
    public TuSdkMediaTimeSliceEntity sliceWithInputTimeUs(final long n) {
        if (this.a.size() < 1) {
            TLog.e("sliceWithOutputTimeUs() null :%s", this.c);
            return null;
        }
        if (n >= this.b) {
            return this.a.get(this.a.size() - 1);
        }
        for (final TuSdkMediaTimeSliceEntity tuSdkMediaTimeSliceEntity : this.a) {
            if (tuSdkMediaTimeSliceEntity.overviewInput(n) == 0) {
                return tuSdkMediaTimeSliceEntity;
            }
        }
        return this.a.get(this.a.size() - 1);
    }
    
    public TuSdkMediaTimeSliceEntity sliceAudioWithInputTimeUs(final long n, final boolean b) {
        if (this.a.size() < 1) {
            TLog.e("sliceWithOutputTimeUs() null :%s", this.c);
            return null;
        }
        if (n >= this.b) {
            return this.a.get(this.a.size() - 1);
        }
        for (final TuSdkMediaTimeSliceEntity tuSdkMediaTimeSliceEntity : this.a) {
            if (tuSdkMediaTimeSliceEntity.overviewAudioInput(n, b) == 0) {
                return tuSdkMediaTimeSliceEntity;
            }
        }
        return this.a.get(this.a.size() - 1);
    }
    
    public TuSdkMediaTimeSliceEntity existenceWithInputTimeUs(final long n) {
        final List<TuSdkMediaTimeSliceEntity> a = this.a;
        if (a.size() < 1) {
            TLog.e("sliceWithOutputTimeUs() null :%s", this.c);
            return null;
        }
        if (n < 1L) {
            return a.get(0);
        }
        for (final TuSdkMediaTimeSliceEntity tuSdkMediaTimeSliceEntity : a) {
            if (n >= tuSdkMediaTimeSliceEntity.startUs && n <= tuSdkMediaTimeSliceEntity.endUs) {
                return tuSdkMediaTimeSliceEntity;
            }
        }
        return a.get(a.size() - 1);
    }
    
    public long sliceWithCalcModeOutputTimeUs(final long n) {
        if (this.a.size() < 1) {
            TLog.e("sliceWithOutputTimeUs() null :%s", this.c);
            return 0L;
        }
        if (n < 1L) {
            return 0L;
        }
        if (n >= this.b) {
            return this.b;
        }
        return this.a(n);
    }
    
    private long a(final long n) {
        if (n < 0L) {
            return 0L;
        }
        if (n >= this.b) {
            return this.b;
        }
        TuSdkMediaTimeSliceEntity tuSdkMediaTimeSliceEntity = null;
        for (final TuSdkMediaTimeSliceEntity tuSdkMediaTimeSliceEntity2 : this.a) {
            if (tuSdkMediaTimeSliceEntity2.overviewRealTime(n) == 0) {
                tuSdkMediaTimeSliceEntity = tuSdkMediaTimeSliceEntity2;
                break;
            }
        }
        if (tuSdkMediaTimeSliceEntity == null) {
            return n;
        }
        return (long)(tuSdkMediaTimeSliceEntity.outputStartUs + (n - tuSdkMediaTimeSliceEntity.realTimeStarUs) / tuSdkMediaTimeSliceEntity.speed);
    }
    
    public boolean isFixTimeSlices() {
        return this.c;
    }
    
    public void fixTimeSlices(final TuSdkMediaExtractor tuSdkMediaExtractor, final boolean b, final boolean b2) {
        this.a(tuSdkMediaExtractor, b, true);
    }
    
    public void fixTimeSlices(final TuSdkMediaExtractor tuSdkMediaExtractor, final boolean b) {
        this.a(tuSdkMediaExtractor, b, false);
    }
    
    private void a(final TuSdkMediaExtractor tuSdkMediaExtractor, final boolean b, final boolean b2) {
        if (tuSdkMediaExtractor == null || tuSdkMediaExtractor.getFrameInfo() == null || this.c) {
            return;
        }
        if (b && !tuSdkMediaExtractor.getFrameInfo().supportAllKeys()) {
            TLog.w("%s media unsupport timeline.", "TuSdkMediaFileCuterTimeline");
            this.c = true;
            return;
        }
        this.mTaskID = System.nanoTime();
        final long sampleTime = tuSdkMediaExtractor.getSampleTime();
        if (this.mOrginSlices.size() < 1) {
            final TuSdkMediaTimeSliceEntity tuSdkMediaTimeSliceEntity = new TuSdkMediaTimeSliceEntity();
            tuSdkMediaTimeSliceEntity.startUs = (long)Math.ceil(this.mOrginStartScaling * this.getInputDurationUs());
            tuSdkMediaTimeSliceEntity.endUs = (long)Math.ceil(this.mOrginEndScaling * this.getInputDurationUs());
            this.a(tuSdkMediaTimeSliceEntity, tuSdkMediaExtractor, b, b2);
            if (tuSdkMediaTimeSliceEntity.startUs != tuSdkMediaTimeSliceEntity.endUs) {
                this.b = Math.abs(tuSdkMediaTimeSliceEntity.reduce());
                tuSdkMediaTimeSliceEntity.outputEndUs = this.b;
                this.a.add(tuSdkMediaTimeSliceEntity);
                this.append(tuSdkMediaTimeSliceEntity);
            }
        }
        else {
            long endUs = 0L;
            TuSdkMediaTimeSliceEntity previous = null;
            int index = 0;
            final Iterator<TuSdkMediaTimeSlice> iterator = this.mOrginSlices.iterator();
            while (iterator.hasNext()) {
                final TuSdkMediaTimeSliceEntity next = new TuSdkMediaTimeSliceEntity(iterator.next());
                this.a(next, tuSdkMediaExtractor, b, b2);
                if (!b) {
                    if (next.startUs < endUs) {
                        next.startUs = endUs;
                    }
                    if (next.isReverse()) {
                        continue;
                    }
                }
                if (next.startUs == next.endUs) {
                    continue;
                }
                endUs = next.endUs;
                this.b += (long)Math.floor(Math.abs(next.reduce()) / next.speed);
                next.outputEndUs = this.b;
                this.e += Math.abs(next.reduce());
                next.realTimeEndUs = this.e;
                this.a.add(next);
                next.index = index;
                if ((next.previous = previous) != null) {
                    next.outputStartUs = previous.outputEndUs;
                    next.realTimeStarUs = previous.realTimeEndUs;
                    previous.next = next;
                }
                previous = next;
                ++index;
            }
        }
        tuSdkMediaExtractor.seekTo(sampleTime);
        this.c = true;
    }
    
    private void a(final TuSdkMediaTimeSliceEntity tuSdkMediaTimeSliceEntity, final TuSdkMediaExtractor tuSdkMediaExtractor, final boolean b, final boolean b2) {
        if (tuSdkMediaTimeSliceEntity.speed <= 0.0f) {
            tuSdkMediaTimeSliceEntity.speed = 1.0f;
        }
        if (tuSdkMediaTimeSliceEntity.startUs < 0L) {
            tuSdkMediaTimeSliceEntity.startUs = 0L;
        }
        if (tuSdkMediaTimeSliceEntity.startUs > this.getInputDurationUs()) {
            tuSdkMediaTimeSliceEntity.startUs = this.getInputDurationUs();
        }
        if (tuSdkMediaTimeSliceEntity.endUs < 0L || tuSdkMediaTimeSliceEntity.endUs > this.getInputDurationUs()) {
            tuSdkMediaTimeSliceEntity.endUs = this.getInputDurationUs();
        }
        if (tuSdkMediaTimeSliceEntity.startUs == tuSdkMediaTimeSliceEntity.endUs) {
            return;
        }
        if (b) {
            tuSdkMediaTimeSliceEntity.endUs = tuSdkMediaExtractor.seekTo(tuSdkMediaTimeSliceEntity.isReverse() ? tuSdkMediaTimeSliceEntity.endUs : (tuSdkMediaTimeSliceEntity.endUs - 1L));
            tuSdkMediaTimeSliceEntity.startUs = tuSdkMediaExtractor.seekTo(tuSdkMediaTimeSliceEntity.isReverse() ? (tuSdkMediaTimeSliceEntity.startUs - 1L) : tuSdkMediaTimeSliceEntity.startUs);
        }
        else if (tuSdkMediaTimeSliceEntity.isReverse()) {
            final long startUs = tuSdkMediaTimeSliceEntity.startUs;
            tuSdkMediaTimeSliceEntity.startUs = tuSdkMediaTimeSliceEntity.endUs;
            tuSdkMediaTimeSliceEntity.endUs = startUs;
        }
        tuSdkMediaTimeSliceEntity.taskID = this.mTaskID;
    }
    
    public List<TuSdkMediaTimeSliceEntity> getFinalSlices() {
        return this.a;
    }
}
