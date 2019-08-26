// 
// Decompiled by Procyon v0.5.36
// 

package com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.media.codec.extend;

import android.media.MediaCodecInfo;
import android.os.Handler;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.media.Image;
import java.nio.ByteBuffer;
import android.media.MediaCodec;
import android.media.MediaDescrambler;
import android.media.MediaCrypto;
import android.view.Surface;
import android.media.MediaFormat;

public interface TuSdkMediaCodec
{
    boolean isStarted();
    
    boolean isReleased();
    
    Exception configureError();
    
    boolean reset();
    
    boolean release();
    
    boolean configure(final MediaFormat p0, final Surface p1, final MediaCrypto p2, final int p3);
    
    boolean configure(final MediaFormat p0, final Surface p1, final int p2, final MediaDescrambler p3);
    
    boolean setOutputSurface(final Surface p0);
    
    boolean setInputSurface(final Surface p0);
    
    Surface createInputSurface();
    
    boolean start();
    
    boolean stop();
    
    boolean flush();
    
    boolean queueInputBuffer(final int p0, final int p1, final int p2, final long p3, final int p4);
    
    boolean queueSecureInputBuffer(final int p0, final int p1, final MediaCodec.CryptoInfo p2, final long p3, final int p4);
    
    int dequeueInputBuffer(final long p0);
    
    int dequeueOutputBuffer(final MediaCodec.BufferInfo p0, final long p1);
    
    boolean releaseOutputBuffer(final int p0, final boolean p1);
    
    boolean releaseOutputBuffer(final int p0, final long p1);
    
    boolean signalEndOfInputStream();
    
    MediaFormat getOutputFormat();
    
    MediaFormat getInputFormat();
    
    MediaFormat getOutputFormat(final int p0);
    
    ByteBuffer[] getInputBuffers();
    
    ByteBuffer[] getOutputBuffers();
    
    ByteBuffer getInputBuffer(final int p0);
    
    Image getInputImage(final int p0);
    
    ByteBuffer getOutputBuffer(final int p0);
    
    Image getOutputImage(final int p0);
    
    boolean setVideoScalingMode(final int p0);
    
    String getName();
    
    PersistableBundle getMetrics();
    
    boolean setParameters(final Bundle p0);
    
    boolean setCallback(final MediaCodec.Callback p0, final Handler p1);
    
    boolean setCallback(final MediaCodec.Callback p0);
    
    boolean setOnFrameRenderedListener(final MediaCodec.OnFrameRenderedListener p0, final Handler p1);
    
    MediaCodecInfo getCodecInfo();
}
