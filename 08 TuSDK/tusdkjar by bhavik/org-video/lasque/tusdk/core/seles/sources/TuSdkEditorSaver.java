package org.lasque.tusdk.core.seles.sources;

import android.graphics.Bitmap;
import java.io.File;
import org.lasque.tusdk.core.encoder.video.TuSDKVideoEncoderSetting;
import org.lasque.tusdk.core.struct.TuSdkMediaDataSource;
import org.lasque.tusdk.core.utils.TLog;
import org.lasque.tusdk.core.utils.TuSdkWaterMarkOption.WaterMarkPosition;

public abstract interface TuSdkEditorSaver
{
  public static final int Init = 1;
  public static final int Saving = 2;
  public static final int Saved = 3;
  public static final int Error = 4;
  public static final int Stop = 5;
  
  public abstract void setOptions(TuSdkEditorSaverOptions paramTuSdkEditorSaverOptions);
  
  public abstract void addSaverProgressListener(TuSdkSaverProgressListener paramTuSdkSaverProgressListener);
  
  public abstract void removeProgressListener(TuSdkSaverProgressListener paramTuSdkSaverProgressListener);
  
  public abstract void removeAllProgressListener();
  
  public abstract int getStatus();
  
  public abstract void stopSave();
  
  public abstract void destroy();
  
  public static abstract interface TuSdkSaverProgressListener
  {
    public abstract void onProgress(float paramFloat);
    
    public abstract void onCompleted(TuSdkMediaDataSource paramTuSdkMediaDataSource);
    
    public abstract void onError(Exception paramException);
  }
  
  public static class TuSdkEditorSaverOptions
  {
    public TuSdkMediaDataSource mediaDataSource;
    TuSDKVideoEncoderSetting a;
    public Bitmap mWaterImageBitmap;
    public float mWaterImageScale;
    public boolean isRecycleWaterImage;
    TuSdkWaterMarkOption.WaterMarkPosition b = TuSdkWaterMarkOption.WaterMarkPosition.TopRight;
    boolean c;
    String d;
    boolean e = false;
    File f;
    
    public boolean check()
    {
      if ((this.mediaDataSource == null) || (!this.mediaDataSource.isValid()))
      {
        TLog.e("%s Media Data Source is invalid !!!  %s", new Object[] { "TuSdkEditorSaverOptions", this.mediaDataSource });
        return false;
      }
      if (this.a == null)
      {
        TLog.e("%s Encoder Setting is invalid !!!  ", new Object[] { "TuSdkEditorSaverOptions" });
        return false;
      }
      return true;
    }
  }
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKVideo-3.4.1.jar!\org\lasque\tusdk\core\seles\sources\TuSdkEditorSaver.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */