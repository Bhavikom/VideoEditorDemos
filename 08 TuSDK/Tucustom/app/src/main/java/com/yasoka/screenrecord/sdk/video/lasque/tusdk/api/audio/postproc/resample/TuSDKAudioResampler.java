// 
// Decompiled by Procyon v0.5.36
// 

package com.yasoka.screenrecord.sdk.video.lasque.tusdk.api.audio.postproc.resample;

import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.TLog;
import com.yasoka.screenrecord.sdk.video.lasque.tusdk.api.postpro.TuSDKPostProcess;
import com.yasoka.screenrecord.sdk.video.lasque.tusdk.core.common.TuSDKMediaDataSource;

//import org.lasque.tusdk.core.utils.TLog;
import java.util.ArrayList;
import java.io.File;
//import org.lasque.tusdk.core.common.TuSDKMediaDataSource;
//import org.lasque.tusdk.api.postpro.TuSDKPostProcess;

public class TuSDKAudioResampler extends TuSDKPostProcess
{
    public final boolean process(final TuSDKMediaDataSource tuSDKMediaDataSource, final File file, final int i, final int j) {
        final ArrayList<PostProcessArg> list = new ArrayList<PostProcessArg>(5);
        if (i > 0) {
            list.add(new PostProcessArg("-ar", String.valueOf(i)));
        }
        if (j > 0) {
            list.add(new PostProcessArg("-ac", String.valueOf(j)));
        }
        if (list.size() == 0) {
            TLog.e("%s : Invalid parameter", new Object[] { this });
            return false;
        }
        list.add(new PostProcessArg("-f", "wav"));
        return super.process(tuSDKMediaDataSource, file, list);
    }
}
