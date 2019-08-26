package org.lasque.tusdk.impl;

import org.lasque.tusdk.core.TuSdkContext;
import org.lasque.tusdk.core.type.ActivityAnimType;

public enum TuAnimType
  implements ActivityAnimType
{
  private int a;
  private int b;
  
  private TuAnimType(int paramInt1, int paramInt2)
  {
    this.a = paramInt1;
    this.b = paramInt2;
  }
  
  private TuAnimType(String paramString1, String paramString2)
  {
    this(TuSdkContext.getAnimaResId(paramString1), TuSdkContext.getAnimaResId(paramString2));
  }
  
  private TuAnimType(String paramString, int paramInt)
  {
    this(TuSdkContext.getAnimaResId(paramString), paramInt);
  }
  
  private TuAnimType(int paramInt, String paramString)
  {
    this(paramInt, TuSdkContext.getAnimaResId(paramString));
  }
  
  public int getEnterAnim()
  {
    return this.a;
  }
  
  public int getExitAnim()
  {
    return this.b;
  }
  
  public int getAnim(boolean paramBoolean)
  {
    return paramBoolean ? this.b : this.a;
  }
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKCore-3.1.0.jar!\org\lasque\tusdk\impl\TuAnimType.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */