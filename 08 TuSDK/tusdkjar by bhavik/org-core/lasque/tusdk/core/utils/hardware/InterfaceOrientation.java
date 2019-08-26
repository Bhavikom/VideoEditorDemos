package org.lasque.tusdk.core.utils.hardware;

public enum InterfaceOrientation
{
  private int a;
  private int b;
  private int c;
  private boolean d;
  private int e;
  private int f;
  
  private InterfaceOrientation(int paramInt1, int paramInt2, int paramInt3, boolean paramBoolean, int paramInt4, int paramInt5)
  {
    this.a = paramInt1;
    this.c = paramInt2;
    this.b = paramInt3;
    this.d = paramBoolean;
    this.e = paramInt4;
    this.f = paramInt5;
  }
  
  public int getFlag()
  {
    return this.a;
  }
  
  public int getSurfaceRotation()
  {
    return this.b;
  }
  
  public int getDegree()
  {
    return this.c;
  }
  
  public boolean isTransposed()
  {
    return this.d;
  }
  
  public int viewDegree()
  {
    int i = 360 - this.c;
    if (i == 360) {
      i = 0;
    }
    return i;
  }
  
  public int[] viewFromToDegree(int paramInt)
  {
    int[] arrayOfInt = { paramInt, viewDegree() };
    if ((arrayOfInt[0] == 270) && (arrayOfInt[1] == 0)) {
      arrayOfInt[1] = 360;
    } else if ((arrayOfInt[0] == 0) && (arrayOfInt[1] == 270)) {
      arrayOfInt[0] = 360;
    }
    return arrayOfInt;
  }
  
  public boolean isMatch(int paramInt)
  {
    if (this.c > 0)
    {
      if ((paramInt >= this.e) && (paramInt < this.f)) {
        return true;
      }
    }
    else if ((paramInt >= this.e) || (paramInt < this.f)) {
      return true;
    }
    return false;
  }
  
  public static InterfaceOrientation getWithSurfaceRotation(int paramInt)
  {
    for (InterfaceOrientation localInterfaceOrientation : ) {
      if (localInterfaceOrientation.getSurfaceRotation() == paramInt) {
        return localInterfaceOrientation;
      }
    }
    return Portrait;
  }
  
  public static InterfaceOrientation getWithDegrees(int paramInt)
  {
    paramInt %= 360;
    if (paramInt < 0) {
      paramInt += 360;
    }
    for (InterfaceOrientation localInterfaceOrientation : values()) {
      if (localInterfaceOrientation.getDegree() == paramInt) {
        return localInterfaceOrientation;
      }
    }
    return Portrait;
  }
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKCore-3.1.0.jar!\org\lasque\tusdk\core\utils\hardware\InterfaceOrientation.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */