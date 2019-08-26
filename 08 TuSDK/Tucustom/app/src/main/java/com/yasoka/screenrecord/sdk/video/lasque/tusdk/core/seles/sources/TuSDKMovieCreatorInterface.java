// 
// Decompiled by Procyon v0.5.36
// 

package com.yasoka.screenrecord.sdk.video.lasque.tusdk.core.seles.sources;

import android.media.MediaCodec;
import java.nio.ByteBuffer;
import java.io.File;

public interface TuSDKMovieCreatorInterface
{
    boolean isRecording();
    
    boolean isSaveToAlbum();
    
    void setSaveToAlbum(final boolean p0);
    
    String getSaveToAlbumName();
    
    void setSaveToAlbumName(final String p0);
    
    File getMovieOutputPath();
    
    public static class ByteDataFrame
    {
        public int trackIndex;
        public ByteBuffer buffer;
        public MediaCodec.BufferInfo bufferInfo;
    }
}
