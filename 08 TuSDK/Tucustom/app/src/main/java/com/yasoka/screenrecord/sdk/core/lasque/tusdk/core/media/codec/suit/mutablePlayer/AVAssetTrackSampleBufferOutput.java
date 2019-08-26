// 
// Decompiled by Procyon v0.5.36
// 

package com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.media.codec.suit.mutablePlayer;

import android.media.MediaFormat;
import java.util.List;

public interface AVAssetTrackSampleBufferOutput<Target extends AVAssetTrackSampleBufferOutput.AVAssetTrackSampleBufferInput>
{
    List<Target> targets();
    
    void addTarget(final Target p0, final int p1);
    
    void addTarget(final Target p0);
    
    void removeTarget(final Target p0);
    
    public interface AVAssetTrackSampleBufferInput
    {
        void newFrameReady(final AVSampleBuffer p0);
        
        void outputFormatChaned(final MediaFormat p0, final AVAssetTrack p1);
    }
}
