// 
// Decompiled by Procyon v0.5.36
// 

package com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.media.codec.audio;

import android.media.MediaCodec;
import java.nio.ByteBuffer;

public interface TuSdkAudioRecord
{
    void setAudioInfo(final TuSdkAudioInfo p0);
    
    void setListener(final TuSdkAudioRecordListener p0);
    
    void startRecording();
    
    void stop();
    
    void release();
    
    public interface TuSdkAudioRecordListener
    {
        public static final int PARAMETRTS_ERROR = 2001;
        public static final int PERMISSION_ERROR = 2002;
        
        void onAudioRecordOutputBuffer(final ByteBuffer p0, final MediaCodec.BufferInfo p1);
        
        void onAudioRecordError(final int p0);
    }
}
