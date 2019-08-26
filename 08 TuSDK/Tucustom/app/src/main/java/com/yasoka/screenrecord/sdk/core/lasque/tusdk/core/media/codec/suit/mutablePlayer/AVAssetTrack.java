// 
// Decompiled by Procyon v0.5.36
// 

package com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.media.codec.suit.mutablePlayer;

//import org.lasque.tusdk.core.media.codec.extend.TuSdkMediaFormat;
//import org.lasque.tusdk.core.utils.image.ImageOrientation;
//import org.lasque.tusdk.core.struct.TuSdkSize;
import android.media.MediaFormat;
import android.annotation.TargetApi;

import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.media.codec.extend.TuSdkMediaFormat;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.struct.TuSdkSize;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.image.ImageOrientation;

@TargetApi(16)
public class AVAssetTrack
{
    private AVAsset a;
    private MediaFormat b;
    private AVMediaType c;
    private int d;
    private AVTimeRange e;
    
    public AVAssetTrack(final AVAsset a, final MediaFormat b, final AVMediaType c, final int d) {
        this.a = a;
        this.c = c;
        this.d = d;
        this.b = b;
    }
    
    public AVTimeRange timeRange() {
        return this.e;
    }
    
    public AVMediaType mediaType() {
        return this.c;
    }
    
    public MediaFormat mediaFormat() {
        return this.b;
    }
    
    public AVAsset getAsset() {
        return this.a;
    }
    
    public int getTrackID() {
        return this.d;
    }
    
    public void setTimeRange(final AVTimeRange e) {
        this.e = e;
    }
    
    public TuSdkSize naturalSize() {
        return TuSdkSize.create(this.b.getInteger("width"), this.b.getInteger("height"));
    }
    
    public TuSdkSize presentSize() {
        if (this.orientation() == ImageOrientation.Up || this.orientation() == ImageOrientation.Down) {
            return this.naturalSize();
        }
        final TuSdkSize naturalSize = this.naturalSize();
        return TuSdkSize.create(naturalSize.height, naturalSize.width);
    }
    
    public long minFrameDuration() {
        if (this.b.containsKey("frame-rate")) {
            return 1 / this.b.getInteger("frame-rate") * 1000L;
        }
        return 0L;
    }
    
    public ImageOrientation orientation() {
        return TuSdkMediaFormat.getVideoRotation(this.getAsset().metadataRetriever());
    }
    
    public long durationTimeUs() {
        if (this.b != null && this.b.containsKey("durationUs")) {
            return TuSdkMediaFormat.getKeyDurationUs(this.b) - this.minFrameDuration() * 10L;
        }
        return TuSdkMediaFormat.getKeyDuration(this.getAsset().metadataRetriever()) * 1000L - this.minFrameDuration();
    }
}
