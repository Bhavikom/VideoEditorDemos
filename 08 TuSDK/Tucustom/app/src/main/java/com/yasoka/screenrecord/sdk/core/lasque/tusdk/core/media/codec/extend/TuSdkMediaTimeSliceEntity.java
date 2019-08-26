// 
// Decompiled by Procyon v0.5.36
// 

package com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.media.codec.extend;

public class TuSdkMediaTimeSliceEntity extends TuSdkMediaTimeSlice
{
    public int index;
    public TuSdkMediaTimeSliceEntity previous;
    public TuSdkMediaTimeSliceEntity next;
    public long outputStartUs;
    public long outputEndUs;
    public long taskID;
    public long audioStartUs;
    public long audioEndUs;
    private boolean a;
    public long realTimeStarUs;
    public long realTimeEndUs;
    
    public TuSdkMediaTimeSliceEntity() {
        this.outputStartUs = 0L;
        this.outputEndUs = 0L;
        this.taskID = 0L;
        this.audioStartUs = 0L;
        this.audioEndUs = 0L;
        this.a = false;
        this.realTimeStarUs = 0L;
        this.realTimeEndUs = 0L;
    }
    
    public TuSdkMediaTimeSliceEntity(final TuSdkMediaTimeSlice tuSdkMediaTimeSlice) {
        this.outputStartUs = 0L;
        this.outputEndUs = 0L;
        this.taskID = 0L;
        this.audioStartUs = 0L;
        this.audioEndUs = 0L;
        this.a = false;
        this.realTimeStarUs = 0L;
        this.realTimeEndUs = 0L;
        if (tuSdkMediaTimeSlice == null) {
            return;
        }
        this.startUs = tuSdkMediaTimeSlice.startUs;
        this.endUs = tuSdkMediaTimeSlice.endUs;
        this.speed = tuSdkMediaTimeSlice.speed;
        this.overlapIndex = tuSdkMediaTimeSlice.overlapIndex;
    }
    
    @Override
    public TuSdkMediaTimeSliceEntity clone() {
        final TuSdkMediaTimeSliceEntity tuSdkMediaTimeSliceEntity = new TuSdkMediaTimeSliceEntity();
        tuSdkMediaTimeSliceEntity.startUs = this.startUs;
        tuSdkMediaTimeSliceEntity.endUs = this.endUs;
        tuSdkMediaTimeSliceEntity.speed = this.speed;
        tuSdkMediaTimeSliceEntity.overlapIndex = this.overlapIndex;
        return tuSdkMediaTimeSliceEntity;
    }
    
    public void setAudioReverse(final boolean a) {
        this.a = a;
    }
    
    public boolean isAudioReverse() {
        return this.a;
    }
    
    public int overview(final long n) {
        int n2 = 0;
        if (this.isReverse()) {
            if (n <= this.endUs) {
                n2 = 1;
            }
            else if (n > this.startUs) {
                n2 = -1;
            }
        }
        else if (n < this.startUs) {
            n2 = -1;
        }
        else if (n >= this.endUs) {
            n2 = 1;
        }
        return n2;
    }
    
    public int overviewAudio(final long n) {
        int n2 = 0;
        if (this.isReverse() && this.isAudioReverse()) {
            if (n <= this.audioEndUs) {
                n2 = 1;
            }
            else if (n > this.audioStartUs) {
                n2 = -1;
            }
        }
        else if (n < this.audioStartUs) {
            n2 = -1;
        }
        else if (n >= this.audioEndUs) {
            n2 = 1;
        }
        return n2;
    }
    
    public int overviewOutput(final long n) {
        int n2 = 0;
        if (n < this.outputStartUs) {
            n2 = -1;
        }
        else if (n >= this.outputEndUs) {
            n2 = 1;
        }
        return n2;
    }
    
    public int overviewInput(final long n) {
        int n2 = 0;
        if (n < this.startUs) {
            n2 = -1;
        }
        else if (n >= this.endUs) {
            n2 = 1;
        }
        return n2;
    }
    
    public int overviewAudioInput(final long n, final boolean b) {
        int n2 = 0;
        if (b) {
            if (n < this.startUs) {
                n2 = -1;
            }
            else if (n >= this.endUs) {
                n2 = 1;
            }
            return n2;
        }
        if (n >= this.startUs) {
            n2 = -1;
        }
        else if (n < this.endUs) {
            n2 = 1;
        }
        return n2;
    }
    
    public int overviewRealTime(final long n) {
        int n2 = 0;
        if (n < this.realTimeStarUs) {
            n2 = -1;
        }
        else if (n > this.realTimeEndUs) {
            n2 = 1;
        }
        return n2;
    }
    
    public long calOutputTimeUs(final long n) {
        long n2;
        if (this.isReverse()) {
            n2 = this.outputStartUs + (long)((this.startUs - n) / this.speed);
        }
        else {
            n2 = this.outputStartUs + (long)((n - this.startUs) / this.speed);
        }
        if (n2 < 0L) {
            n2 = 0L;
        }
        return n2;
    }
    
    public long calRealTimeOutputTimeUs(final long n) {
        long n2;
        if (this.isReverse()) {
            n2 = this.outputStartUs + (long)((this.realTimeEndUs - n) / this.speed);
        }
        else {
            n2 = this.outputStartUs + (long)((n - this.realTimeStarUs) / this.speed);
        }
        if (n2 < 0L) {
            n2 = 0L;
        }
        return n2;
    }
    
    public long calOutputAudioTimeUs(final long n) {
        long n2;
        if (this.isReverse()) {
            if (this.isAudioReverse()) {
                n2 = this.outputStartUs + (long)((this.startUs - n) / this.speed);
            }
            else {
                n2 = this.outputStartUs + (long)((n - this.endUs) / this.speed);
            }
        }
        else {
            n2 = this.outputStartUs + (long)((n - this.startUs) / this.speed);
        }
        return n2;
    }
    
    public long calMutilOutputAudioTimeUs(final long n, final TuSdkMediaFileCuterTimeline tuSdkMediaFileCuterTimeline) {
        return tuSdkMediaFileCuterTimeline.sliceAudioWithInputTimeUs(n, this.isAudioReverse()).calOutputAudioTimeUs(n);
    }
    
    public long calInputTimeUs(final long n) {
        if (n < 0L) {
            return this.startUs;
        }
        if (n <= this.outputStartUs) {
            return this.startUs;
        }
        if (n >= this.outputEndUs) {
            return this.endUs;
        }
        final long n2 = (long)((n - this.outputStartUs) * this.speed);
        long n3;
        if (this.isReverse()) {
            n3 = this.startUs - n2;
        }
        else {
            n3 = n2 + this.startUs;
        }
        return n3;
    }
    
    public long calInputTimeUs(final long n, final TuSdkMediaFileCuterTimeline tuSdkMediaFileCuterTimeline) {
        if (n < 0L) {
            return this.startUs;
        }
        if (n <= this.outputStartUs) {
            return this.startUs;
        }
        if (n >= this.outputEndUs) {
            return this.endUs;
        }
        long n2 = (long)((n - this.getPreTimeSliceRepetTimeUs(tuSdkMediaFileCuterTimeline)) * this.speed);
        if (this.isReverse()) {
            n2 = this.startUs - n2;
        }
        return n2;
    }
    
    public long calOutputNoRepetTimeUs(long n, final TuSdkMediaFileCuterTimeline tuSdkMediaFileCuterTimeline) {
        if (this.isReverse()) {
            n = tuSdkMediaFileCuterTimeline.getOutputDurationUs() - n;
        }
        this.getPreTimeSliceRepetTimeUs(tuSdkMediaFileCuterTimeline);
        long n2;
        if (this.speed != 1.0f) {
            n2 = this.outputStartUs - this.getPreTimeSliceRepetTimeUs(tuSdkMediaFileCuterTimeline) + (long)((this.calInputTimeUs(n) - this.outputStartUs) * this.speed);
        }
        else {
            n2 = n - this.getPreTimeSliceRepetTimeUs(tuSdkMediaFileCuterTimeline);
        }
        return n2;
    }
    
    public long calOutputHaveRepetTimeUs(final long n, final TuSdkMediaFileCuterTimeline tuSdkMediaFileCuterTimeline) {
        return (long)((n + this.b(tuSdkMediaFileCuterTimeline)) / this.speed);
    }
    
    public long calcOutputNoSlowTimeUs(final long n, final TuSdkMediaFileCuterTimeline tuSdkMediaFileCuterTimeline) {
        long n2;
        if (this.speed != 1.0f) {
            n2 = n + this.a(tuSdkMediaFileCuterTimeline) + (long)((this.calOutputTimeUs(n) - this.outputStartUs) * this.speed);
        }
        else {
            n2 = n + this.a(tuSdkMediaFileCuterTimeline);
        }
        return n2;
    }
    
    private long a(final TuSdkMediaFileCuterTimeline tuSdkMediaFileCuterTimeline) {
        long n = 0L;
        if (this.previous == null) {
            return n;
        }
        for (int index = tuSdkMediaFileCuterTimeline.getFinalSlices().indexOf(this), i = 0; i < index; ++i) {
            final TuSdkMediaTimeSliceEntity tuSdkMediaTimeSliceEntity = tuSdkMediaFileCuterTimeline.getFinalSlices().get(i);
            if (tuSdkMediaTimeSliceEntity.speed != 1.0f) {
                n += (long)((tuSdkMediaTimeSliceEntity.endUs - tuSdkMediaTimeSliceEntity.startUs) / tuSdkMediaTimeSliceEntity.speed) - (tuSdkMediaTimeSliceEntity.endUs - tuSdkMediaTimeSliceEntity.startUs);
            }
        }
        return n;
    }
    
    public long getPreTimeSliceRepetTimeUs(final TuSdkMediaFileCuterTimeline tuSdkMediaFileCuterTimeline) {
        long n = 0L;
        if (this.previous == null) {
            return n;
        }
        final int index = tuSdkMediaFileCuterTimeline.getFinalSlices().indexOf(this);
        if (this.overlapIndex > -1) {
            for (int i = 0; i <= index; ++i) {
                final TuSdkMediaTimeSliceEntity tuSdkMediaTimeSliceEntity = tuSdkMediaFileCuterTimeline.getFinalSlices().get(i);
                if (tuSdkMediaTimeSliceEntity.overlapIndex > -1 && this.overlapIndex > tuSdkMediaTimeSliceEntity.overlapIndex) {
                    n += (long)((tuSdkMediaTimeSliceEntity.endUs - tuSdkMediaTimeSliceEntity.startUs) / this.speed);
                }
            }
        }
        else {
            for (int j = 0; j <= index; ++j) {
                final TuSdkMediaTimeSliceEntity tuSdkMediaTimeSliceEntity2 = tuSdkMediaFileCuterTimeline.getFinalSlices().get(j);
                if (tuSdkMediaTimeSliceEntity2.overlapIndex > 0) {
                    n += (long)((tuSdkMediaTimeSliceEntity2.endUs - tuSdkMediaTimeSliceEntity2.startUs) / this.speed);
                }
            }
        }
        return n;
    }
    
    private long b(final TuSdkMediaFileCuterTimeline tuSdkMediaFileCuterTimeline) {
        long n = 0L;
        if (this.previous == null) {
            return n;
        }
        final int index = tuSdkMediaFileCuterTimeline.getFinalSlices().indexOf(this);
        if (this.overlapIndex > -1) {
            for (int i = 0; i < tuSdkMediaFileCuterTimeline.getFinalSlices().size(); ++i) {
                final TuSdkMediaTimeSliceEntity tuSdkMediaTimeSliceEntity = tuSdkMediaFileCuterTimeline.getFinalSlices().get(i);
                if (tuSdkMediaTimeSliceEntity.overlapIndex > 0) {
                    n += (long)((tuSdkMediaTimeSliceEntity.endUs - tuSdkMediaTimeSliceEntity.startUs) / this.speed);
                }
            }
        }
        else {
            for (int j = 0; j <= index; ++j) {
                final TuSdkMediaTimeSliceEntity tuSdkMediaTimeSliceEntity2 = tuSdkMediaFileCuterTimeline.getFinalSlices().get(j);
                if (tuSdkMediaTimeSliceEntity2.overlapIndex > 0) {
                    n += (long)((tuSdkMediaTimeSliceEntity2.endUs - tuSdkMediaTimeSliceEntity2.startUs) / this.speed);
                }
            }
        }
        return n;
    }
    
    public long calOutputOrginTimeUs(final long n) {
        return this.outputStartUs + (n - this.audioStartUs);
    }
    
    @Override
    public boolean equals(final Object o) {
        if (o == null || !(o instanceof TuSdkMediaTimeSliceEntity)) {
            return false;
        }
        final TuSdkMediaTimeSliceEntity tuSdkMediaTimeSliceEntity = (TuSdkMediaTimeSliceEntity)o;
        return tuSdkMediaTimeSliceEntity.startUs == this.startUs && tuSdkMediaTimeSliceEntity.endUs == this.endUs && tuSdkMediaTimeSliceEntity.speed == this.speed && tuSdkMediaTimeSliceEntity.index == this.index;
    }
    
    @Override
    public String toString() {
        final StringBuffer append = new StringBuffer("TuSdkMediaTimeSliceEntity").append("{ \n");
        append.append("startUs: ").append(this.startUs).append(", \n");
        append.append("endUs: ").append(this.endUs).append(", \n");
        append.append("audioStartUs: ").append(this.audioStartUs).append(", \n");
        append.append("audioEndUs: ").append(this.audioEndUs).append(", \n");
        append.append("speed: ").append(this.speed).append(", \n");
        append.append("index: ").append(this.index).append(", \n");
        append.append("outputStartUs: ").append(this.outputStartUs).append(", \n");
        append.append("outputEndUs: ").append(this.outputEndUs).append(", \n");
        append.append("next: ").append(this.next).append(", \n");
        append.append("taskID: ").append(this.taskID).append(", \n");
        append.append("}");
        return append.toString();
    }
}
