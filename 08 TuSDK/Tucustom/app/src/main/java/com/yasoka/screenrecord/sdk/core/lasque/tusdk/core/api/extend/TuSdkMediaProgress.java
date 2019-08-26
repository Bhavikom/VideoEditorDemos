// 
// Decompiled by Procyon v0.5.36
// 

package com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.api.extend;

import android.support.annotation.Nullable;

import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.struct.TuSdkMediaDataSource;
//import org.lasque.tusdk.core.struct.TuSdkMediaDataSource;

public interface TuSdkMediaProgress
{
    void onProgress(final float p0, final TuSdkMediaDataSource p1, final int p2, final int p3);
    
    void onCompleted(@Nullable final Exception p0, final TuSdkMediaDataSource p1, final int p2);
}
