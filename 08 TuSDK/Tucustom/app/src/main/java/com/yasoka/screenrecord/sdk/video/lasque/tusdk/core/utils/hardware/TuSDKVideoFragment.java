// 
// Decompiled by Procyon v0.5.36
// 

package com.yasoka.screenrecord.sdk.video.lasque.tusdk.core.utils.hardware;

import java.io.File;

public class TuSDKVideoFragment
{
    private File a;
    private float b;
    private float c;
    
    public File getVideoFile() {
        return this.a;
    }
    
    public void setVideoFile(final File a) {
        this.a = a;
    }
    
    public float getStart() {
        return this.b;
    }
    
    public void setStart(final float b) {
        this.b = b;
    }
    
    public float getDuration() {
        return this.c;
    }
    
    public void setDuration(final float c) {
        this.c = c;
    }
    
    public void clearVideoFile() {
        if (this.a != null && this.a.exists()) {
            this.a.delete();
        }
        this.a = null;
    }
    
    public static TuSDKVideoFragment makeFragment(final File a, final float b, final float c) {
        final TuSDKVideoFragment tuSDKVideoFragment = new TuSDKVideoFragment();
        tuSDKVideoFragment.a = a;
        tuSDKVideoFragment.b = b;
        tuSDKVideoFragment.c = c;
        return tuSDKVideoFragment;
    }
}
