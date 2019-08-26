// 
// Decompiled by Procyon v0.5.36
// 

package com.yasoka.screenrecord.sdk.video.lasque.tusdk.core.decoder;

//import org.lasque.tusdk.video.editor.TuSdkTimeRange;
//import org.lasque.tusdk.core.common.TuSDKAVPacket;
import com.yasoka.screenrecord.sdk.video.lasque.tusdk.core.common.TuSDKAVPacket;
import com.yasoka.screenrecord.sdk.video.lasque.tusdk.video.editor.TuSdkTimeRange;

import java.util.LinkedList;

public abstract class TuSDKVideoTimeEffectController implements TuSDKVideoTimeEffectControllerInterface
{
    private TimeEffectMode a;
    protected LinkedList<TuSDKAVPacket> mCachePackets;
    protected TuSdkTimeRange mTimeRange;
    protected int mTimes;
    protected int mCounter;
    protected int mCopyTimes;
    
    public TuSDKVideoTimeEffectController() {
        this.a = TimeEffectMode.NoMode;
        this.mCachePackets = new LinkedList<TuSDKAVPacket>();
        this.mTimes = 3;
        this.mCounter = 0;
        this.mCopyTimes = 1;
        if (this.mTimeRange == null) {
            this.mTimeRange = TuSdkTimeRange.makeRange(0.0f, 1.0f);
        }
    }
    
    public static TuSDKVideoTimeEffectController create(final TimeEffectMode timeEffectMode) {
        if (TimeEffectMode.RepeatMode == timeEffectMode) {
            return new TuSDKVideoTimeEffectRepeatController();
        }
        if (TimeEffectMode.SlowMode == timeEffectMode) {
            return new TuSDKVideoTimeEffectSlowController();
        }
        return new TuSDKVideoTimeEffectNoController();
    }
    
    public TuSdkTimeRange getTimeRange() {
        return this.mTimeRange;
    }
    
    public void setTimeRange(final TuSdkTimeRange mTimeRange) {
        this.mTimeRange = mTimeRange;
    }
    
    public void setTimeEffectMode(final TimeEffectMode a) {
        this.a = a;
    }
    
    public TimeEffectMode getTimeEffectMode() {
        return this.a;
    }
    
    public void setTimes(final int mTimes) {
        this.mTimes = mTimes;
    }
    
    public int getTimes() {
        return this.mTimes;
    }
    
    @Override
    public void doPacketTimeEffectExtract(final LinkedList<TuSDKAVPacket> list) {
        if (!this.mTimeRange.isValid()) {
            return;
        }
    }
    
    @Override
    public void reset() {
        this.mCounter = 0;
        this.mCopyTimes = 1;
        this.mCachePackets.clear();
    }
    
    public enum TimeEffectMode
    {
        NoMode, 
        RepeatMode, 
        SlowMode;
    }
}
