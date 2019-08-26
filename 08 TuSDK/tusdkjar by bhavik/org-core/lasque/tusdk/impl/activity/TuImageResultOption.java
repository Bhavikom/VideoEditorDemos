package org.lasque.tusdk.impl.activity;

public abstract class TuImageResultOption
  extends TuResultOption
{
  private boolean a;
  private boolean b;
  
  public boolean isShowResultPreview()
  {
    return this.a;
  }
  
  public void setShowResultPreview(boolean paramBoolean)
  {
    this.a = paramBoolean;
  }
  
  public boolean isAutoRemoveTemp()
  {
    return this.b;
  }
  
  public void setAutoRemoveTemp(boolean paramBoolean)
  {
    this.b = paramBoolean;
  }
  
  private void a(TuImageResultFragment paramTuImageResultFragment)
  {
    if (paramTuImageResultFragment == null) {
      return;
    }
    paramTuImageResultFragment.setShowResultPreview(isShowResultPreview());
    paramTuImageResultFragment.setAutoRemoveTemp(isAutoRemoveTemp());
  }
  
  protected <T extends TuFragment> T fragmentInstance()
  {
    TuFragment localTuFragment = super.fragmentInstance();
    if ((localTuFragment instanceof TuImageResultFragment)) {
      a((TuImageResultFragment)localTuFragment);
    }
    return localTuFragment;
  }
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKCore-3.1.0.jar!\org\lasque\tusdk\impl\activity\TuImageResultOption.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */