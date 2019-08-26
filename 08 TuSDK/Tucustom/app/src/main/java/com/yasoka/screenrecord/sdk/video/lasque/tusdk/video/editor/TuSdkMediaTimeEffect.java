// 
// Decompiled by Procyon v0.5.36
// 

package com.yasoka.screenrecord.sdk.video.lasque.tusdk.video.editor;

import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.media.codec.extend.TuSdkMediaTimeEffectTimeline;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.media.codec.extend.TuSdkMediaTimeSlice;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.media.codec.extend.TuSdkMediaTimeSliceEntity;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.TLog;

import java.util.Iterator;
//import org.lasque.tusdk.core.utils.TLog;
//import org.lasque.tusdk.core.media.codec.extend.TuSdkMediaTimeSlice;
import java.util.ArrayList;
//import org.lasque.tusdk.core.media.codec.extend.TuSdkMediaTimeSliceEntity;
import java.util.List;
//import org.lasque.tusdk.core.media.codec.extend.TuSdkMediaTimeEffectTimeline;

public abstract class TuSdkMediaTimeEffect
{
    protected TuSdkMediaTimeEffectTimeline mEffectTimeLine;
    protected List<TuSdkMediaTimeSliceEntity> mFinalTimeSlices;
    protected TuSdkTimeRange mTimeRange;
    protected boolean isDropOverTime;
    protected int mOutputTimeLineType;
    protected long mInputTotalTimeUs;
    protected long mCurrentInputTimeUs;
    
    public TuSdkMediaTimeEffect() {
        this.mEffectTimeLine = new TuSdkMediaTimeEffectTimeline();
        this.mFinalTimeSlices = new ArrayList<TuSdkMediaTimeSliceEntity>();
        this.mTimeRange = new TuSdkTimeRange();
        this.mOutputTimeLineType = 0;
        this.mInputTotalTimeUs = 0L;
        this.mCurrentInputTimeUs = 0L;
    }
    
    public void setTimeRange(final long startTimeUs, final long endTimeUs) {
        this.mTimeRange.setStartTimeUs(startTimeUs);
        this.mTimeRange.setEndTimeUs(endTimeUs);
    }
    
    public void setTimeRange(final TuSdkTimeRange tuSdkTimeRange) {
        this.mTimeRange.setStartTimeUs(tuSdkTimeRange.getStartTimeUS());
        this.mTimeRange.setEndTimeUs(tuSdkTimeRange.getEndTimeUS());
    }
    
    public void setRealTimeSlices(final List<TuSdkMediaTimeSliceEntity> list) {
        this.mFinalTimeSlices.clear();
        this.mFinalTimeSlices.addAll(list);
        this.mEffectTimeLine.setInputAlignTimeSlices((List)this.mFinalTimeSlices);
    }
    
    public void setDropOverTime(final boolean b) {
        this.isDropOverTime = b;
        this.mEffectTimeLine.setKeepOriginalLength(b);
    }
    
    public boolean isDropOverTime() {
        return this.isDropOverTime;
    }
    
    public TuSdkTimeRange getTimeRange() {
        return this.mTimeRange;
    }
    
    public abstract List<TuSdkMediaTimeSlice> getTimeSlickList();
    
    public long getInputTotalTimeUs() {
        if (this.mInputTotalTimeUs == 0L) {
            this.mInputTotalTimeUs = this.a();
        }
        return this.mInputTotalTimeUs;
    }
    
    private long a() {
        if (this.mFinalTimeSlices.size() == 0) {
            TLog.w("mFinalTimeSlices size is 0 !", new Object[0]);
            return 0L;
        }
        long n = 0L;
        final Iterator<TuSdkMediaTimeSliceEntity> iterator = this.mFinalTimeSlices.iterator();
        while (iterator.hasNext()) {
            n += iterator.next().reduce();
        }
        return n;
    }
    
    public abstract long getCurrentInputTimeUs();
    
    public abstract long calcOutputTimeUs(final long p0, final TuSdkMediaTimeSliceEntity p1, final List<TuSdkMediaTimeSliceEntity> p2);
    
    public abstract long calcSeekOutputUs(final long p0);
}
