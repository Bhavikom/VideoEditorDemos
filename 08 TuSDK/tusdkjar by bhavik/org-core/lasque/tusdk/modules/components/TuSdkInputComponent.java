package org.lasque.tusdk.modules.components;

import android.app.Activity;
import android.graphics.Bitmap;
import java.io.File;
import org.lasque.tusdk.core.utils.sqllite.ImageSqlInfo;

public abstract class TuSdkInputComponent
  extends TuSdkComponent
{
  private File a;
  private ImageSqlInfo b;
  private Bitmap c;
  
  public TuSdkInputComponent(Activity paramActivity)
  {
    super(paramActivity);
  }
  
  public File getTempFilePath()
  {
    return this.a;
  }
  
  public TuSdkInputComponent setTempFilePath(File paramFile)
  {
    this.a = paramFile;
    return this;
  }
  
  public ImageSqlInfo getImageSqlInfo()
  {
    return this.b;
  }
  
  public TuSdkInputComponent setImageSqlInfo(ImageSqlInfo paramImageSqlInfo)
  {
    this.b = paramImageSqlInfo;
    return this;
  }
  
  public Bitmap getImage()
  {
    return this.c;
  }
  
  public TuSdkInputComponent setImage(Bitmap paramBitmap)
  {
    this.c = paramBitmap;
    return this;
  }
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKCore-3.1.0.jar!\org\lasque\tusdk\modules\components\TuSdkInputComponent.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */