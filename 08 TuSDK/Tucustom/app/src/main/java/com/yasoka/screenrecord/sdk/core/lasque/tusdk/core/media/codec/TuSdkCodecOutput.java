// 
// Decompiled by Procyon v0.5.36
// 

package com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.media.codec;

//import org.lasque.tusdk.core.media.codec.extend.TuSdkMediaCodec;
import java.nio.ByteBuffer;
import android.view.Surface;
import android.media.MediaCodec;
import android.media.MediaFormat;

import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.media.codec.extend.TuSdkMediaCodec;

public interface TuSdkCodecOutput
{
    void outputFormatChanged(final MediaFormat p0);
    
    void updated(final MediaCodec.BufferInfo p0);
    
    boolean updatedToEOS(final MediaCodec.BufferInfo p0);
    
    void onCatchedException(final Exception p0);
    
    public interface TuSdkDecodecVideoSurfaceOutput extends TuSdkDecodecOutput
    {
        Surface requestSurface();
    }
    
    public interface TuSdkEncodecOutput extends TuSdkCodecOutput
    {
        void processOutputBuffer(final TuSdkMediaMuxer p0, final int p1, final ByteBuffer p2, final MediaCodec.BufferInfo p3);
    }
    
    public interface TuSdkDecodecOutput extends TuSdkCodecOutput
    {
        boolean canSupportMediaInfo(final int p0, final MediaFormat p1);
        
        boolean processExtractor(final TuSdkMediaExtractor p0, final TuSdkMediaCodec p1);
        
        void processOutputBuffer(final TuSdkMediaExtractor p0, final int p1, final ByteBuffer p2, final MediaCodec.BufferInfo p3);
    }
}
