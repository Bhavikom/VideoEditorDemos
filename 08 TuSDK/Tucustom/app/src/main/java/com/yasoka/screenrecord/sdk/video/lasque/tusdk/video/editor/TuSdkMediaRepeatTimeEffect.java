// 
// Decompiled by Procyon v0.5.36
// 

package com.yasoka.screenrecord.sdk.video.lasque.tusdk.video.editor;

//import org.lasque.tusdk.core.media.codec.extend.TuSdkMediaTimeSliceEntity;
//import org.lasque.tusdk.core.media.codec.extend.TuSdkMediaTimeSlice;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.media.codec.extend.TuSdkMediaTimeSlice;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.media.codec.extend.TuSdkMediaTimeSliceEntity;

import java.util.List;

public class TuSdkMediaRepeatTimeEffect extends TuSdkMediaTimeEffect
{
    private int a;
    
    public TuSdkMediaRepeatTimeEffect() {
        this.a = 2;
    }
    
    public void setRepeatCount(final int a) {
        this.a = a;
    }
    
    @Override
    public List<TuSdkMediaTimeSlice> getTimeSlickList() {
        this.mEffectTimeLine.setTimeSlice(new TuSdkMediaTimeSlice(this.mTimeRange.getStartTimeUS(), this.mTimeRange.getEndTimeUS()), this.a + 1);
        return (List<TuSdkMediaTimeSlice>)this.mEffectTimeLine.getTimeEffectList();
    }
    
    @Override
    public long getCurrentInputTimeUs() {
        return this.mCurrentInputTimeUs;
    }
    
    @Override
    public long calcOutputTimeUs(final long n, final TuSdkMediaTimeSliceEntity tuSdkMediaTimeSliceEntity, final List<TuSdkMediaTimeSliceEntity> list) {
        return this.mCurrentInputTimeUs = this.calOutputNoRepetTimeUs(n, tuSdkMediaTimeSliceEntity, list);
    }
    
    @Override
    public long calcSeekOutputUs(final long n) {
        return 0L;
    }
    
    public long calOutputNoRepetTimeUs(final long n, final TuSdkMediaTimeSliceEntity tuSdkMediaTimeSliceEntity, final List<TuSdkMediaTimeSliceEntity> list) {
        return n - this.a(tuSdkMediaTimeSliceEntity, list);
    }
    
    private long a(final TuSdkMediaTimeSliceEntity tuSdkMediaTimeSliceEntity, final List<TuSdkMediaTimeSliceEntity> list) {
        long n = 0L;
        if (list.size() == 0) {
            return 0L;
        }
        final int index = list.indexOf(tuSdkMediaTimeSliceEntity);
        if (tuSdkMediaTimeSliceEntity.overlapIndex > -1) {
            for (int i = 0; i <= index; ++i) {
                final TuSdkMediaTimeSliceEntity tuSdkMediaTimeSliceEntity2 = list.get(i);
                if (tuSdkMediaTimeSliceEntity2.overlapIndex > -1 && tuSdkMediaTimeSliceEntity.overlapIndex > tuSdkMediaTimeSliceEntity2.overlapIndex) {
                    n += tuSdkMediaTimeSliceEntity2.endUs - tuSdkMediaTimeSliceEntity2.startUs;
                }
            }
        }
        else {
            for (int j = 0; j <= index; ++j) {
                final TuSdkMediaTimeSliceEntity tuSdkMediaTimeSliceEntity3 = list.get(j);
                if (tuSdkMediaTimeSliceEntity3.overlapIndex > 0) {
                    n += tuSdkMediaTimeSliceEntity3.endUs - tuSdkMediaTimeSliceEntity3.startUs;
                }
            }
        }
        return n;
    }
}
