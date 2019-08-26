// 
// Decompiled by Procyon v0.5.36
// 

package com.yasoka.screenrecord.sdk.video.lasque.tusdk.core.common;

import java.io.File;
import android.text.TextUtils;
import android.net.Uri;

public class TuSDKMediaDataSource
{
    private String a;
    private Uri b;
    
    public static TuSDKMediaDataSource create(final String s) {
        return new TuSDKMediaDataSource(s);
    }
    
    public static TuSDKMediaDataSource create(final Uri uri) {
        return new TuSDKMediaDataSource(uri);
    }
    
    public TuSDKMediaDataSource() {
    }
    
    public TuSDKMediaDataSource(final String a) {
        this.a = a;
    }
    
    public TuSDKMediaDataSource(final Uri b) {
        this.b = b;
    }
    
    public TuSDKMediaDataSource(final TuSDKMediaDataSource tuSDKMediaDataSource) {
        if (tuSDKMediaDataSource != null && tuSDKMediaDataSource.isValid()) {
            if (!TextUtils.isEmpty((CharSequence)tuSDKMediaDataSource.getFilePath())) {
                this.setFilePath(tuSDKMediaDataSource.getFilePath());
            }
            else {
                this.setFileUri(tuSDKMediaDataSource.getFileUri());
            }
        }
    }
    
    public String getFilePath() {
        return this.a;
    }
    
    public void setFilePath(final String a) {
        this.a = a;
    }
    
    public Uri getFileUri() {
        return this.b;
    }
    
    public void setFileUri(final Uri b) {
        this.b = b;
    }
    
    public File getFile() {
        if (!TextUtils.isEmpty((CharSequence)this.getFilePath())) {
            return new File(this.getFilePath());
        }
        if (this.getFileUri() != null) {
            return new File(this.getFileUri().getPath());
        }
        return null;
    }
    
    public boolean isValid() {
        if (!TextUtils.isEmpty((CharSequence)this.a)) {
            final File file = new File(this.getFilePath());
            return file.exists() && file.canRead();
        }
        return this.b != null;
    }
    
    @Override
    public String toString() {
        if (this.a != null) {
            return this.a;
        }
        if (this.b != null) {
            return this.b.toString();
        }
        return this.toString();
    }
}
