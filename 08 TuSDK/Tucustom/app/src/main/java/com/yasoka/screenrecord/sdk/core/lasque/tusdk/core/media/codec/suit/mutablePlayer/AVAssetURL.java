// 
// Decompiled by Procyon v0.5.36
// 

package com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.media.codec.suit.mutablePlayer;

import java.io.IOException;
import java.util.Map;
import android.media.MediaExtractor;
import android.content.Context;
import android.net.Uri;
import android.media.MediaMetadataRetriever;
import android.annotation.TargetApi;

@TargetApi(16)
public class AVAssetURL extends AVAsset
{
    private MediaMetadataRetriever a;
    private Uri b;
    private Context c;
    
    public AVAssetURL(final Context c, final Uri b) {
        this.b = b;
        this.c = c;
    }
    
    public Uri fileUri() {
        return this.b;
    }
    
    @Override
    public MediaExtractor createExtractor() {
        final MediaExtractor mediaExtractor = new MediaExtractor();
        try {
            mediaExtractor.setDataSource(this.c, this.b, (Map)null);
        }
        catch (IOException ex) {
            ex.printStackTrace();
        }
        return mediaExtractor;
    }
    
    @Override
    public MediaMetadataRetriever metadataRetriever() {
        if (this.a == null) {
            (this.a = new MediaMetadataRetriever()).setDataSource(this.c, this.b);
        }
        return this.a;
    }
}
