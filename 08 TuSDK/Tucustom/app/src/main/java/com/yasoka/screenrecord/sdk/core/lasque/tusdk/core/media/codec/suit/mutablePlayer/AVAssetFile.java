// 
// Decompiled by Procyon v0.5.36
// 

package com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.media.codec.suit.mutablePlayer;

import java.io.IOException;
import android.media.MediaExtractor;
import java.io.File;
import android.media.MediaMetadataRetriever;
import android.annotation.TargetApi;

@TargetApi(16)
public class AVAssetFile extends AVAsset
{
    private MediaMetadataRetriever a;
    private File b;
    
    public AVAssetFile(final File b) {
        this.b = b;
    }
    
    public File getFile() {
        return this.b;
    }
    
    @Override
    public String toString() {
        return "Asset : " + this.getFile().getAbsolutePath();
    }
    
    @Override
    public MediaExtractor createExtractor() {
        final MediaExtractor mediaExtractor = new MediaExtractor();
        try {
            mediaExtractor.setDataSource(this.getFile().getAbsolutePath());
        }
        catch (IOException ex) {
            ex.printStackTrace();
        }
        return mediaExtractor;
    }
    
    @Override
    public MediaMetadataRetriever metadataRetriever() {
        if (this.a == null) {
            (this.a = new MediaMetadataRetriever()).setDataSource(this.getFile().getAbsolutePath());
        }
        return this.a;
    }
}
