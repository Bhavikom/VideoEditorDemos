// 
// Decompiled by Procyon v0.5.36
// 

package com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.media.codec.encoder;

//import org.lasque.tusdk.core.seles.egl.SelesRenderer;

import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.seles.egl.SelesRenderer;

public interface TuSdkVideoSurfaceEncoderListener extends TuSdkEncoderListener, SelesRenderer
{
    void onEncoderDrawFrame(final long p0, final boolean p1);
}
