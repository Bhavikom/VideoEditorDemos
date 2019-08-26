// 
// Decompiled by Procyon v0.5.36
// 

package com.yasoka.screenrecord.sdk.video.lasque.tusdk.video.editor;

import android.media.MediaCodec;
import java.nio.ByteBuffer;
import android.media.MediaFormat;
import android.annotation.TargetApi;

@TargetApi(18)
public interface TuSDKMovieWriterInterface
{
    boolean start();
    
    boolean stop();
    
    long getDurationTime();
    
    int addVideoTrack(final MediaFormat p0);
    
    int addAudioTrack(final MediaFormat p0);
    
    void writeSampleData(final int p0, final ByteBuffer p1, final MediaCodec.BufferInfo p2);
    
    void writeVideoSampleData(final ByteBuffer p0, final MediaCodec.BufferInfo p1);
    
    void writeAudioSampleData(final ByteBuffer p0, final MediaCodec.BufferInfo p1);
    
    void writeSampleData(final ByteBufferData p0);
    
    void setOrientationHint(final int p0);
    
    public enum MovieWriterOutputFormat
    {
        MPEG_4(0);
        
        int a;
        
        private MovieWriterOutputFormat(final int a) {
            this.a = a;
        }
        
        public int getOutputFormat() {
            return this.a;
        }
    }
    
    public static class ByteBufferData
    {
        public int trackIndex;
        public ByteBuffer buffer;
        public MediaCodec.BufferInfo bufferInfo;
    }
}
