package org.lasque.tusdk.core.gl;

import java.util.Arrays;

public class EGLConfigAttrs
{
  private int a = 8;
  private int b = 8;
  private int c = 8;
  private int d = 8;
  private int e = 8;
  private int f = 4;
  private int g = 4;
  private boolean h = false;
  
  public EGLConfigAttrs red(int paramInt)
  {
    this.a = paramInt;
    return this;
  }
  
  public EGLConfigAttrs green(int paramInt)
  {
    this.b = paramInt;
    return this;
  }
  
  public EGLConfigAttrs blue(int paramInt)
  {
    this.c = paramInt;
    return this;
  }
  
  public EGLConfigAttrs alpha(int paramInt)
  {
    this.d = paramInt;
    return this;
  }
  
  public EGLConfigAttrs depth(int paramInt)
  {
    this.e = paramInt;
    return this;
  }
  
  public EGLConfigAttrs renderType(int paramInt)
  {
    this.f = paramInt;
    return this;
  }
  
  public EGLConfigAttrs surfaceType(int paramInt)
  {
    this.g = paramInt;
    return this;
  }
  
  public EGLConfigAttrs makeDefault(boolean paramBoolean)
  {
    this.h = paramBoolean;
    return this;
  }
  
  public boolean isDefault()
  {
    return this.h;
  }
  
  int[] a()
  {
    return new int[] { 12339, this.g, 12324, this.a, 12323, this.b, 12322, this.c, 12321, this.d, 12325, this.e, 12352, this.f, 12344 };
  }
  
  public String toString()
  {
    return Arrays.toString(a());
  }
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKVideo-3.4.1.jar!\org\lasque\tusdk\core\gl\EGLConfigAttrs.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */