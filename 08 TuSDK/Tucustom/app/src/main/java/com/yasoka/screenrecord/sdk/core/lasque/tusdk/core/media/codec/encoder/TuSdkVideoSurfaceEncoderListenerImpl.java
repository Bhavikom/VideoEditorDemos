// 
// Decompiled by Procyon v0.5.36
// 

package com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.media.codec.encoder;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;
import android.media.MediaCodec;

public abstract class TuSdkVideoSurfaceEncoderListenerImpl implements TuSdkVideoSurfaceEncoderListener
{
    @Override
    public void onEncoderDrawFrame(final long n, final boolean b) {
    }
    
    @Override
    public void onEncoderUpdated(final MediaCodec.BufferInfo bufferInfo) {
    }
    
    @Override
    public void onEncoderCompleted(final Exception ex) {
    }
    
    @Override
    public void onSurfaceDestory(final GL10 gl10) {
    }
    
    public void onSurfaceCreated(final GL10 gl10, final EGLConfig eglConfig) {
    }
    
    public void onSurfaceChanged(final GL10 gl10, final int n, final int n2) {
    }
    
    public void onDrawFrame(final GL10 gl10) {
    }
}
