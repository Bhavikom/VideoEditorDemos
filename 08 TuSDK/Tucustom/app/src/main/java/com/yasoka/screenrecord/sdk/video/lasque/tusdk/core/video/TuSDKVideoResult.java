// 
// Decompiled by Procyon v0.5.36
// 

package com.yasoka.screenrecord.sdk.video.lasque.tusdk.core.video;

//import org.lasque.tusdk.core.decoder.TuSDKVideoInfo;
//import org.lasque.tusdk.core.utils.sqllite.ImageSqlInfo;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.sqllite.ImageSqlInfo;
import com.yasoka.screenrecord.sdk.video.lasque.tusdk.core.decoder.TuSDKVideoInfo;

import java.io.File;

public class TuSDKVideoResult
{
    public File videoPath;
    public ImageSqlInfo videoSqlInfo;
    public TuSDKVideoInfo videoInfo;
    @Deprecated
    public int duration;
    
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("videoPath : " + this.videoPath).append("\n").append("videoInfo : " + this.videoInfo);
        return sb.toString();
    }
}
