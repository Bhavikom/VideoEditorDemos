package org.lasque.tusdk.impl.activity;

import org.lasque.tusdk.modules.components.TuSdkComponentOption;

public abstract class TuResultOption
  extends TuSdkComponentOption
{
  private boolean a;
  private boolean b;
  private String c;
  private int d = 90;
  
  public boolean isSaveToTemp()
  {
    return this.a;
  }
  
  public void setSaveToTemp(boolean paramBoolean)
  {
    this.a = paramBoolean;
  }
  
  public boolean isSaveToAlbum()
  {
    return this.b;
  }
  
  public void setSaveToAlbum(boolean paramBoolean)
  {
    this.b = paramBoolean;
  }
  
  public String getSaveToAlbumName()
  {
    return this.c;
  }
  
  public void setSaveToAlbumName(String paramString)
  {
    this.c = paramString;
  }
  
  public int getOutputCompress()
  {
    if (this.d < 0) {
      this.d = 0;
    } else if (this.d > 100) {
      this.d = 100;
    }
    return this.d;
  }
  
  public void setOutputCompress(int paramInt)
  {
    this.d = paramInt;
  }
  
  private void a(TuResultFragment paramTuResultFragment)
  {
    if (paramTuResultFragment == null) {
      return;
    }
    paramTuResultFragment.setSaveToTemp(isSaveToTemp());
    paramTuResultFragment.setSaveToAlbum(isSaveToAlbum());
    paramTuResultFragment.setSaveToAlbumName(getSaveToAlbumName());
    paramTuResultFragment.setOutputCompress(getOutputCompress());
  }
  
  protected <T extends TuFragment> T fragmentInstance()
  {
    TuFragment localTuFragment = super.fragmentInstance();
    if ((localTuFragment instanceof TuResultFragment)) {
      a((TuResultFragment)localTuFragment);
    }
    return localTuFragment;
  }
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKCore-3.1.0.jar!\org\lasque\tusdk\impl\activity\TuResultOption.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */