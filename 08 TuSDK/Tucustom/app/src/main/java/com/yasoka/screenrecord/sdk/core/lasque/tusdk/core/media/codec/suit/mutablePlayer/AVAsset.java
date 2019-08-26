// 
// Decompiled by Procyon v0.5.36
// 

package com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.media.codec.suit.mutablePlayer;

import android.media.MediaFormat;
import java.util.Arrays;
//import org.lasque.tusdk.core.utils.TLog;
import java.util.ArrayList;
import android.media.MediaMetadataRetriever;
import android.media.MediaExtractor;
import java.util.List;
import android.annotation.TargetApi;

import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.TLog;

@TargetApi(16)
public abstract class AVAsset
{
    protected List<AVAssetTrack> tracks;
    
    public abstract MediaExtractor createExtractor();
    
    public abstract MediaMetadataRetriever metadataRetriever();
    
    @TargetApi(16)
    public List<AVAssetTrack> tracks() {
        if (this.tracks != null) {
            return new ArrayList<AVAssetTrack>(this.tracks);
        }
        final MediaExtractor extractor = this.createExtractor();
        if (extractor == null) {
            TLog.e("A subclass must implement the createExtractor method", new Object[0]);
            return Arrays.asList(new AVAssetTrack[0]);
        }
        final ArrayList<AVAssetTrack> list = new ArrayList<AVAssetTrack>();
        try {
            if (extractor.getTrackCount() <= 0) {
                TLog.e("%s : \u8d44\u4ea7\u9519\u8bef\u65e0\u53ef\u8bfb\u53d6\u7684\u8f68\u9053\u4fe1\u606f", this);
                extractor.release();
                return list;
            }
            for (int trackCount = extractor.getTrackCount(), i = 0; i < trackCount; ++i) {
                final MediaFormat trackFormat = extractor.getTrackFormat(i);
                if (trackFormat.getString("mime").startsWith(AVMediaType.AVMediaTypeAudio.getMime())) {
                    list.add(new AVAssetTrack(this, trackFormat, AVMediaType.AVMediaTypeAudio, i));
                }
                else if (trackFormat.getString("mime").startsWith(AVMediaType.AVMediaTypeVideo.getMime())) {
                    list.add(new AVAssetTrack(this, trackFormat, AVMediaType.AVMediaTypeVideo, i));
                }
                else {
                    TLog.e("%s \u8be5\u8f68\u9053\u6682\u4e0d\u652f\u6301 \uff1a %s", this, trackFormat);
                }
            }
        }
        catch (Exception ex) {
            TLog.e("%s \u8f68\u9053\u89e3\u6790\u9519\u8bef %s", this, ex);
        }
        return list;
    }
    
    public List<AVAssetTrack> tracksWithMediaType(final AVMediaType avMediaType) {
        final List<AVAssetTrack> tracks = this.tracks();
        final ArrayList<AVAssetTrack> list = new ArrayList<AVAssetTrack>(1);
        if (tracks == null) {
            return list;
        }
        for (final AVAssetTrack avAssetTrack : tracks) {
            if (avAssetTrack.mediaType() == avMediaType) {
                list.add(avAssetTrack);
            }
        }
        return list;
    }
}
