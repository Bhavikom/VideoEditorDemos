package org.lasque.tusdk.core.utils.hardware;

import java.io.File;

public class TuSDKVideoFragment
{
  private File a;
  private float b;
  private float c;
  
  public File getVideoFile()
  {
    return this.a;
  }
  
  public void setVideoFile(File paramFile)
  {
    this.a = paramFile;
  }
  
  public float getStart()
  {
    return this.b;
  }
  
  public void setStart(float paramFloat)
  {
    this.b = paramFloat;
  }
  
  public float getDuration()
  {
    return this.c;
  }
  
  public void setDuration(float paramFloat)
  {
    this.c = paramFloat;
  }
  
  public void clearVideoFile()
  {
    if ((this.a != null) && (this.a.exists())) {
      this.a.delete();
    }
    this.a = null;
  }
  
  public static TuSDKVideoFragment makeFragment(File paramFile, float paramFloat1, float paramFloat2)
  {
    TuSDKVideoFragment localTuSDKVideoFragment = new TuSDKVideoFragment();
    localTuSDKVideoFragment.a = paramFile;
    localTuSDKVideoFragment.b = paramFloat1;
    localTuSDKVideoFragment.c = paramFloat2;
    return localTuSDKVideoFragment;
  }
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKVideo-3.4.1.jar!\org\lasque\tusdk\core\utils\hardware\TuSDKVideoFragment.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */