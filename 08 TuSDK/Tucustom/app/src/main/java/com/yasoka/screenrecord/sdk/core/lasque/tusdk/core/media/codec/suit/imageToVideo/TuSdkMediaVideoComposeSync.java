// 
// Decompiled by Procyon v0.5.36
// 

package com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.media.codec.suit.imageToVideo;

//import org.lasque.tusdk.core.media.codec.sync.TuSdkVideoEncodecSyncBase;
//import org.lasque.tusdk.core.media.codec.sync.TuSdkAudioEncodecSyncBase;
//import org.lasque.tusdk.core.media.codec.encoder.TuSdkEncodeSurface;
//import org.lasque.tusdk.core.media.record.TuSdkRecordSurface;
//import org.lasque.tusdk.core.media.codec.sync.TuSdkVideoEncodecSync;
//import org.lasque.tusdk.core.media.codec.sync.TuSdkAudioEncodecSync;
//import org.lasque.tusdk.core.media.codec.sync.TuSdkMediaEncodecSync;

import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.media.codec.encoder.TuSdkEncodeSurface;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.media.codec.sync.TuSdkAudioEncodecSync;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.media.codec.sync.TuSdkAudioEncodecSyncBase;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.media.codec.sync.TuSdkMediaEncodecSync;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.media.codec.sync.TuSdkVideoEncodecSync;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.media.codec.sync.TuSdkVideoEncodecSyncBase;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.media.record.TuSdkRecordSurface;

public class TuSdkMediaVideoComposeSync implements TuSdkMediaEncodecSync
{
    private _VideoEncodecSync a;
    private _AudioEncodecSync b;
    private boolean c;
    
    public TuSdkMediaVideoComposeSync() {
        this.c = false;
    }
    
    @Override
    public TuSdkAudioEncodecSync getAudioEncodecSync() {
        if (this.b == null) {
            this.b = new _AudioEncodecSync();
        }
        return this.b;
    }
    
    @Override
    public TuSdkVideoEncodecSync getVideoEncodecSync() {
        if (this.a == null) {
            this.a = new _VideoEncodecSync();
        }
        return this.a;
    }
    
    @Override
    public void release() {
        if (this.c) {
            return;
        }
        this.c = true;
        if (this.b != null) {
            this.b.release();
            this.b = null;
        }
        if (this.a != null) {
            this.a.release();
            this.a = null;
        }
    }
    
    public void syncVideoEncodecDrawFrame(final long n, final boolean b, final TuSdkRecordSurface tuSdkRecordSurface, final TuSdkEncodeSurface tuSdkEncodeSurface) {
        this.a.syncVideoEncodecDrawFrame(n, b, tuSdkRecordSurface, tuSdkEncodeSurface);
    }
    
    class _AudioEncodecSync extends TuSdkAudioEncodecSyncBase
    {
    }
    
    class _VideoEncodecSync extends TuSdkVideoEncodecSyncBase
    {
        @Override
        protected boolean isLastDecodeFrame(final long n) {
            return true;
        }
    }
}
