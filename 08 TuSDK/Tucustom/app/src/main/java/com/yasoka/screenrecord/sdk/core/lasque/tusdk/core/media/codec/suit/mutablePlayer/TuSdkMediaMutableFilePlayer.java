// 
// Decompiled by Procyon v0.5.36
// 

package com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.media.codec.suit.mutablePlayer;

//import org.lasque.tusdk.core.struct.TuSdkMediaDataSource;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.media.codec.suit.TuSdkMediaFilePlayer;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.struct.TuSdkMediaDataSource;

import java.util.List;
//import org.lasque.tusdk.core.media.codec.suit.TuSdkMediaFilePlayer;

public interface TuSdkMediaMutableFilePlayer extends TuSdkMediaFilePlayer
{
    int maxInputSize();
    
    void setMediaDataSources(final List<TuSdkMediaDataSource> p0);
    
    void setOutputRatio(final float p0);
}
