package org.lasque.tusdk.impl.components.widget.smudge;

import android.graphics.Bitmap;
import java.io.File;
import org.lasque.tusdk.core.TuSdk;
import org.lasque.tusdk.core.struct.TuSdkSize;
import org.lasque.tusdk.core.utils.FileHelper;
import org.lasque.tusdk.core.utils.StringHelper;
import org.lasque.tusdk.core.utils.image.BitmapHelper;

public class SmudgeLog
{
  private Bitmap a;
  private int b;
  private int c;
  private boolean d;
  private String e = StringHelper.timeStampString();
  
  public SmudgeLog(Bitmap paramBitmap)
  {
    this.a = paramBitmap;
    this.b = paramBitmap.getWidth();
    this.c = paramBitmap.getHeight();
  }
  
  public String getName()
  {
    return this.e;
  }
  
  public String getFileName()
  {
    return String.format("smudgeCache_%s.tmp", new Object[] { this.e });
  }
  
  public synchronized Bitmap getBitmap()
  {
    if (this.d)
    {
      File localFile = new File(TuSdk.getAppTempPath(), getFileName());
      return BitmapHelper.getBitmap(localFile, TuSdkSize.create(this.b, this.c), true);
    }
    return this.a;
  }
  
  public boolean hasCached()
  {
    return this.d;
  }
  
  public synchronized void markAsCached()
  {
    this.d = true;
    BitmapHelper.recycled(this.a);
    this.a = null;
  }
  
  public void destroy()
  {
    if (this.d)
    {
      File localFile = new File(TuSdk.getAppTempPath(), getFileName());
      FileHelper.delete(localFile);
    }
    else
    {
      BitmapHelper.recycled(this.a);
    }
  }
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKCore-3.1.0.jar!\org\lasque\tusdk\impl\components\widget\smudge\SmudgeLog.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */