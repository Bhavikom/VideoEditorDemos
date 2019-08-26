package org.lasque.tusdk.core.gl;

public class EGLContextAttrs
{
  private int a = 2;
  private boolean b;
  
  public EGLContextAttrs version(int paramInt)
  {
    this.a = paramInt;
    return this;
  }
  
  public EGLContextAttrs makeDefault(boolean paramBoolean)
  {
    this.b = paramBoolean;
    return this;
  }
  
  public boolean isDefault()
  {
    return this.b;
  }
  
  int[] a()
  {
    return new int[] { 12440, this.a, 12344 };
  }
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKVideo-3.4.1.jar!\org\lasque\tusdk\core\gl\EGLContextAttrs.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */