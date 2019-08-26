package org.lasque.tusdk.core.task;

import android.graphics.Bitmap;
import java.io.File;
import org.lasque.tusdk.core.TuSdkContext;
import org.lasque.tusdk.core.utils.FileHelper;
import org.lasque.tusdk.core.utils.TuSdkDate;

public class FiltersTempTask
  extends FiltersTaskBase
{
  public static final String TAG = "FiltersTempTask";
  private boolean a;
  
  public FiltersTempTask()
  {
    appendFilterCode("Normal");
  }
  
  public FiltersTempTask(Bitmap paramBitmap)
  {
    this();
    setInputImage(paramBitmap);
  }
  
  public File getSampleRootPath()
  {
    if (super.getSampleRootPath() == null)
    {
      String str = String.format("%s/tempTask/%s", new Object[] { "lasFilterTemp", Integer.valueOf(TuSdkDate.create().getTimeInSeconds()) });
      File localFile = TuSdkContext.getAppCacheDir(str, false);
      if (localFile == null) {
        return null;
      }
      localFile.mkdirs();
      setSampleRootPath(localFile);
    }
    return super.getSampleRootPath();
  }
  
  public boolean isCancelTask()
  {
    return this.a;
  }
  
  public void setInputImage(Bitmap paramBitmap)
  {
    super.setInputImage(paramBitmap);
    start();
  }
  
  protected void asyncBuildWithFilterName(String paramString)
  {
    if (this.a) {
      return;
    }
    super.asyncBuildWithFilterName(paramString);
  }
  
  public void resetQueues()
  {
    this.a = true;
    super.resetQueues();
    FileHelper.delete(getSampleRootPath());
  }
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKCore-3.1.0.jar!\org\lasque\tusdk\core\task\FiltersTempTask.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */