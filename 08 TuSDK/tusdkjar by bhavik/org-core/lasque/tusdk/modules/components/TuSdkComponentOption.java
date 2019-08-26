package org.lasque.tusdk.modules.components;

import org.lasque.tusdk.core.utils.ReflectUtils;
import org.lasque.tusdk.impl.activity.TuFragment;

public abstract class TuSdkComponentOption
{
  private int a = 0;
  private Class<?> b;
  
  public Class<?> getComponentClazz()
  {
    if (this.b == null) {
      this.b = getDefaultComponentClazz();
    }
    return this.b;
  }
  
  protected abstract Class<?> getDefaultComponentClazz();
  
  public void setComponentClazz(Class<?> paramClass)
  {
    if ((paramClass != null) && (getComponentClazz() != null) && (getDefaultComponentClazz().isAssignableFrom(paramClass))) {
      this.b = paramClass;
    }
  }
  
  public void setRootViewLayoutId(int paramInt)
  {
    this.a = paramInt;
  }
  
  public int getRootViewLayoutId()
  {
    if (this.a == 0) {
      this.a = getDefaultRootViewLayoutId();
    }
    return this.a;
  }
  
  protected abstract int getDefaultRootViewLayoutId();
  
  private void a(TuFragment paramTuFragment)
  {
    if (paramTuFragment == null) {
      return;
    }
    paramTuFragment.setRootViewLayoutId(getRootViewLayoutId());
  }
  
  protected <T extends TuFragment> T fragmentInstance()
  {
    TuFragment localTuFragment = (TuFragment)ReflectUtils.classInstance(getComponentClazz());
    a(localTuFragment);
    return localTuFragment;
  }
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKCore-3.1.0.jar!\org\lasque\tusdk\modules\components\TuSdkComponentOption.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */