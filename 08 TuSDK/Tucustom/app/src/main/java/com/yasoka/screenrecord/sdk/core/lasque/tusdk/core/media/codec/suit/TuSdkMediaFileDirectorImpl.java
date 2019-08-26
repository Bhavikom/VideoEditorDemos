// 
// Decompiled by Procyon v0.5.36
// 

package com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.media.codec.suit;

//import org.lasque.tusdk.core.media.codec.sync.TuSdkMediaFileSync;
//import org.lasque.tusdk.core.media.codec.sync.TuSdkMediaFileDirectorSync;

import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.media.codec.sync.TuSdkMediaFileDirectorSync;

public class TuSdkMediaFileDirectorImpl extends TuSdkMediaFileCuterImpl
{
    public TuSdkMediaFileDirectorImpl() {
        super(new TuSdkMediaFileDirectorSync());
    }
}
