// 
// Decompiled by Procyon v0.5.36
// 

package com.yasoka.screenrecord.sdk.video.lasque.tusdk.api.movie.preproc.mixer;

import android.media.MediaCodec;
import java.nio.ByteBuffer;
//import org.lasque.tusdk.video.editor.TuSDKMovieWriterInterface;
import android.media.MediaFormat;

import com.yasoka.screenrecord.sdk.video.lasque.tusdk.video.editor.TuSDKMovieWriter;
import com.yasoka.screenrecord.sdk.video.lasque.tusdk.video.editor.TuSDKMovieWriterInterface;
//import org.lasque.tusdk.video.editor.TuSDKMovieWriter;

public abstract class TuSDKMovieMixer implements TuSDKMovieMixerInterface
{
    private TuSDKMovieWriter a;
    
    public TuSDKMovieMixer setMediaWriter(final TuSDKMovieWriter a) {
        this.a = a;
        return this;
    }
    
    public TuSDKMovieWriter getMediaWriter() {
        if (this.a == null) {
            this.a = new TuSDKMovieWriter(this.getOutputFilePah(), this.getOutputFormat());
        }
        return this.a;
    }
    
    protected void startMovieWriter() {
        this.getMediaWriter().start();
    }
    
    protected void stopMovieWriter() {
        if (this.a != null) {
            this.a.stop();
        }
        this.a = null;
    }
    
    public int addAudioTrack(final MediaFormat mediaFormat) {
        return this.getMediaWriter().addAudioTrack(mediaFormat);
    }
    
    public int addVideoTrack(final MediaFormat mediaFormat) {
        return this.getMediaWriter().addVideoTrack(mediaFormat);
    }
    
    public void writeSampleData(final TuSDKMovieWriterInterface.ByteBufferData byteBufferData) {
        this.getMediaWriter().writeSampleData(byteBufferData);
    }
    
    public void writeAudioSampleData(final ByteBuffer byteBuffer, final MediaCodec.BufferInfo bufferInfo) {
        this.getMediaWriter().writeAudioSampleData(byteBuffer, bufferInfo);
    }
    
    public void writeVideoSampleData(final ByteBuffer byteBuffer, final MediaCodec.BufferInfo bufferInfo) {
        this.getMediaWriter().writeVideoSampleData(byteBuffer, bufferInfo);
    }
    
    public abstract String getOutputFilePah();
    
    protected abstract TuSDKMovieWriterInterface.MovieWriterOutputFormat getOutputFormat();
}
