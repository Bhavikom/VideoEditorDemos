// 
// Decompiled by Procyon v0.5.36
// 

package com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.task;

//import org.lasque.tusdk.core.utils.FileHelper;
//import org.lasque.tusdk.core.TuSdkContext;
//import org.lasque.tusdk.core.utils.TuSdkDate;
import java.io.File;
import android.graphics.Bitmap;

import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.TuSdkContext;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.FileHelper;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.TuSdkDate;

public class FiltersTempTask extends FiltersTaskBase
{
    public static final String TAG = "FiltersTempTask";
    private boolean a;
    
    public FiltersTempTask() {
        this.appendFilterCode("Normal");
    }
    
    public FiltersTempTask(final Bitmap inputImage) {
        this();
        this.setInputImage(inputImage);
    }
    
    @Override
    public File getSampleRootPath() {
        if (super.getSampleRootPath() == null) {
            final File appCacheDir = TuSdkContext.getAppCacheDir(String.format("%s/tempTask/%s", "lasFilterTemp", TuSdkDate.create().getTimeInSeconds()), false);
            if (appCacheDir == null) {
                return null;
            }
            appCacheDir.mkdirs();
            this.setSampleRootPath(appCacheDir);
        }
        return super.getSampleRootPath();
    }
    
    public boolean isCancelTask() {
        return this.a;
    }
    
    @Override
    public void setInputImage(final Bitmap inputImage) {
        super.setInputImage(inputImage);
        this.start();
    }
    
    @Override
    protected void asyncBuildWithFilterName(final String s) {
        if (this.a) {
            return;
        }
        super.asyncBuildWithFilterName(s);
    }
    
    @Override
    public void resetQueues() {
        this.a = true;
        super.resetQueues();
        FileHelper.delete(this.getSampleRootPath());
    }
}
