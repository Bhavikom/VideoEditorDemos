// 
// Decompiled by Procyon v0.5.36
// 

package com.yasoka.screenrecord.sdk.video.lasque.tusdk.api.audio.preproc.mixer;

//import org.lasque.tusdk.core.utils.StringHelper;
import android.text.TextUtils;
import java.io.File;
import android.net.Uri;
//import org.lasque.tusdk.video.editor.TuSdkTimeRange;
//import org.lasque.tusdk.core.decoder.TuSDKAudioInfo;
import android.annotation.TargetApi;

import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.StringHelper;
import com.yasoka.screenrecord.sdk.video.lasque.tusdk.core.common.TuSDKMediaDataSource;
import com.yasoka.screenrecord.sdk.video.lasque.tusdk.core.decoder.TuSDKAudioInfo;
import com.yasoka.screenrecord.sdk.video.lasque.tusdk.video.editor.TuSdkTimeRange;
//import org.lasque.tusdk.core.common.TuSDKMediaDataSource;

@TargetApi(16)
public class TuSDKAudioEntry extends TuSDKMediaDataSource
{
    private TuSDKAudioInfo a;
    private boolean b;
    private TuSdkTimeRange c;
    private boolean d;
    private TuSdkTimeRange e;
    private float f;
    
    public TuSDKAudioEntry() {
        this.f = 1.0f;
    }
    
    public TuSDKAudioEntry(final String s) {
        super(s);
        this.f = 1.0f;
    }
    
    public TuSDKAudioEntry(final Uri uri) {
        super(uri);
        this.f = 1.0f;
    }
    
    public TuSDKAudioEntry(final TuSDKAudioInfo a) {
        this.f = 1.0f;
        this.a = a;
    }
    
    public TuSDKAudioEntry(final TuSDKMediaDataSource tuSDKMediaDataSource) {
        super(tuSDKMediaDataSource);
        this.f = 1.0f;
    }
    
    public TuSDKAudioEntry setRawInfo(final TuSDKAudioInfo a) {
        this.a = a;
        return this;
    }
    
    public TuSDKAudioInfo getRawInfo() {
        if (this.a == null) {
            this.a = TuSDKAudioInfo.createWithMediaDataSource(this);
        }
        return this.a;
    }
    
    public TuSDKAudioEntry setTrunk(final boolean b) {
        this.b = b;
        return this;
    }
    
    public boolean isTrunk() {
        return this.b;
    }
    
    public TuSDKAudioEntry setLooping(final boolean d) {
        this.d = d;
        return this;
    }
    
    public boolean isLooping() {
        return this.d;
    }
    
    public TuSDKAudioEntry setTimeRange(final TuSdkTimeRange c) {
        this.c = c;
        return this;
    }
    
    public TuSdkTimeRange getTimeRange() {
        return this.c;
    }
    
    public boolean validateTimeRange() {
        return this.c != null && this.c.isValid();
    }
    
    public TuSDKAudioEntry setCutTimeRange(final TuSdkTimeRange e) {
        this.e = e;
        return this;
    }
    
    public float getVolume() {
        return this.f;
    }
    
    public TuSDKAudioEntry setVolume(final float f) {
        if (this.f <= 1.0f && this.f >= 0.0f) {
            this.f = f;
        }
        return this;
    }
    
    public int bytesSizeOfTimeRangeStartPosition() {
        if (!this.validateTimeRange() || this.getTimeRange().getStartTime() == 0.0f || this.a == null) {
            return 0;
        }
        return this.a.bytesCountOfTime((int)Math.ceil(this.getTimeRange().getStartTime()));
    }
    
    public int bytesSizeOfTimeRangeEndPosition() {
        if (!this.validateTimeRange() || this.a == null) {
            return 0;
        }
        return this.a.bytesCountOfTime((int)Math.ceil(this.getTimeRange().getEndTime()));
    }
    
    public TuSdkTimeRange getCutTimeRange() {
        return this.e;
    }
    
    public boolean validateCutTimeRange() {
        return this.e != null && this.e.isValid();
    }
    
    public boolean clearDecodeCahceInfo() {
        if (this.a == null || !this.a.isValid()) {
            return false;
        }
        if (this.a.getFilePath() != null) {
            new File(this.a.getFilePath()).delete();
        }
        this.a = null;
        return false;
    }
    
    public String getFingerprint() {
        return this.getFingerprint(null);
    }
    
    public String getFingerprint(final String str) {
        if (!this.isValid()) {
            return null;
        }
        final StringBuilder sb = new StringBuilder();
        if (this.validateCutTimeRange()) {
            sb.append(this.getCutTimeRange().getStartTime()).append(this.getCutTimeRange().getEndTime());
        }
        if (!TextUtils.isEmpty((CharSequence)this.getFilePath())) {
            sb.append(this.getFilePath());
        }
        else {
            sb.append(this.getFileUri().getPath());
        }
        if (!TextUtils.isEmpty((CharSequence)str)) {
            sb.append(str);
        }
        return StringHelper.md5(sb.toString());
    }
}
