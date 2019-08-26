// 
// Decompiled by Procyon v0.5.36
// 

package com.yasoka.screenrecord.sdk.video.lasque.tusdk.video.editor;

//import org.lasque.tusdk.core.media.codec.extend.TuSdkMediaTimeSliceEntity;
//import org.lasque.tusdk.core.media.codec.extend.TuSdkMediaTimeSlice;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.media.codec.extend.TuSdkMediaTimeSlice;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.media.codec.extend.TuSdkMediaTimeSliceEntity;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.TLog;

import java.util.List;
//import org.lasque.tusdk.core.utils.TLog;

public class TuSdkMediaSlowTimeEffect extends TuSdkMediaTimeEffect
{
    private float a;
    
    public TuSdkMediaSlowTimeEffect() {
        this.a = 1.0f;
    }
    
    public void setSpeed(final float a) {
        if (a < 0.0f || a > 2.0f) {
            TLog.e("Speed Error, speed must >0 && <=2", new Object[0]);
            return;
        }
        this.a = a;
    }
    
    @Override
    public List<TuSdkMediaTimeSlice> getTimeSlickList() {
        final TuSdkMediaTimeSlice tuSdkMediaTimeSlice = new TuSdkMediaTimeSlice(this.mTimeRange.getStartTimeUS(), this.mTimeRange.getEndTimeUS());
        tuSdkMediaTimeSlice.speed = this.a;
        this.mEffectTimeLine.setTimeSlice(tuSdkMediaTimeSlice, 1);
        return (List<TuSdkMediaTimeSlice>)this.mEffectTimeLine.getTimeEffectList();
    }
    
    @Override
    public long getCurrentInputTimeUs() {
        return this.mCurrentInputTimeUs;
    }
    
    @Override
    public long calcOutputTimeUs(final long n, final TuSdkMediaTimeSliceEntity tuSdkMediaTimeSliceEntity, final List<TuSdkMediaTimeSliceEntity> list) {
        return this.mCurrentInputTimeUs = this.calcInputTimeUs(n, tuSdkMediaTimeSliceEntity, list);
    }
    
    @Override
    public long calcSeekOutputUs(final long n) {
        return 0L;
    }
    
    public long calcInputTimeUs(final long n, final TuSdkMediaTimeSliceEntity tuSdkMediaTimeSliceEntity, final List<TuSdkMediaTimeSliceEntity> list) {
        if (tuSdkMediaTimeSliceEntity == null) {
            return n;
        }
        long n2;
        if (tuSdkMediaTimeSliceEntity.speed != 1.0f) {
            n2 = tuSdkMediaTimeSliceEntity.outputStartUs + (long)((n - tuSdkMediaTimeSliceEntity.outputStartUs) * tuSdkMediaTimeSliceEntity.speed) - this.a(tuSdkMediaTimeSliceEntity, list);
        }
        else {
            n2 = n - this.a(tuSdkMediaTimeSliceEntity, list);
        }
        return n2;
    }
    
    private long a(final TuSdkMediaTimeSliceEntity tuSdkMediaTimeSliceEntity, final List<TuSdkMediaTimeSliceEntity> list) {
        final int index = list.indexOf(tuSdkMediaTimeSliceEntity);
        long n = 0L;
        for (int i = 0; i < index; ++i) {
            final TuSdkMediaTimeSliceEntity tuSdkMediaTimeSliceEntity2 = list.get(i);
            if (tuSdkMediaTimeSliceEntity2.speed != 1.0f) {
                final long n2 = tuSdkMediaTimeSliceEntity2.outputEndUs - tuSdkMediaTimeSliceEntity2.outputStartUs;
                n += (long)(n2 - n2 * tuSdkMediaTimeSliceEntity2.speed);
            }
        }
        return n;
    }
}
