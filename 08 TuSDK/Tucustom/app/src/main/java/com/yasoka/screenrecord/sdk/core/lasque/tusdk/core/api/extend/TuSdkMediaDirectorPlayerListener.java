// 
// Decompiled by Procyon v0.5.36
// 

package com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.api.extend;

//import org.lasque.tusdk.core.media.codec.extend.TuSdkMediaFileCuterTimeline;
//import org.lasque.tusdk.core.struct.TuSdkMediaDataSource;

import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.media.codec.extend.TuSdkMediaFileCuterTimeline;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.struct.TuSdkMediaDataSource;

public interface TuSdkMediaDirectorPlayerListener extends TuSdkMediaPlayerListener
{
    void onProgress(final long p0, final long p1, final TuSdkMediaDataSource p2, final TuSdkMediaFileCuterTimeline p3);
}
