// 
// Decompiled by Procyon v0.5.36
// 

package com.yasoka.screenrecord.sdk.video.lasque.tusdk.core.seles.sources;

//import org.lasque.tusdk.core.utils.TLog;
import java.io.File;
//import org.lasque.tusdk.core.utils.TuSdkWaterMarkOption;
import android.graphics.Bitmap;

import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.struct.TuSdkMediaDataSource;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.TLog;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.TuSdkWaterMarkOption;
import com.yasoka.screenrecord.sdk.video.lasque.tusdk.core.encoder.video.TuSDKVideoEncoderSetting;
//import org.lasque.tusdk.core.encoder.video.TuSDKVideoEncoderSetting;
//import org.lasque.tusdk.core.struct.TuSdkMediaDataSource;

public interface TuSdkEditorSaver
{
    public static final int Init = 1;
    public static final int Saving = 2;
    public static final int Saved = 3;
    public static final int Error = 4;
    public static final int Stop = 5;
    
    void setOptions(final TuSdkEditorSaverOptions p0);
    
    void addSaverProgressListener(final TuSdkSaverProgressListener p0);
    
    void removeProgressListener(final TuSdkSaverProgressListener p0);
    
    void removeAllProgressListener();
    
    int getStatus();
    
    void stopSave();
    
    void destroy();
    
    public interface TuSdkSaverProgressListener
    {
        void onProgress(final float p0);
        
        void onCompleted(final TuSdkMediaDataSource p0);
        
        void onError(final Exception p0);
    }
    
    public static class TuSdkEditorSaverOptions
    {
        public TuSdkMediaDataSource mediaDataSource;
        TuSDKVideoEncoderSetting a;
        public Bitmap mWaterImageBitmap;
        public float mWaterImageScale;
        public boolean isRecycleWaterImage;
        TuSdkWaterMarkOption.WaterMarkPosition b;
        boolean c;
        String d;
        boolean e;
        File f;
        
        public TuSdkEditorSaverOptions() {
            this.b = TuSdkWaterMarkOption.WaterMarkPosition.TopRight;
            this.e = false;
        }
        
        public boolean check() {
            if (this.mediaDataSource == null || !this.mediaDataSource.isValid()) {
                TLog.e("%s Media Data Source is invalid !!!  %s", new Object[] { "TuSdkEditorSaverOptions", this.mediaDataSource });
                return false;
            }
            if (this.a == null) {
                TLog.e("%s Encoder Setting is invalid !!!  ", new Object[] { "TuSdkEditorSaverOptions" });
                return false;
            }
            return true;
        }
    }
}
