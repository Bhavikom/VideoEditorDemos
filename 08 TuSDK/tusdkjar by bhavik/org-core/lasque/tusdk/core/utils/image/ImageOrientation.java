package org.lasque.tusdk.core.utils.image;

public enum ImageOrientation
{
  private boolean a;
  private int b;
  private int c;
  private boolean d;
  private int e;
  
  private ImageOrientation(boolean paramBoolean1, int paramInt1, int paramInt2, boolean paramBoolean2, int paramInt3)
  {
    this.a = paramBoolean1;
    this.b = paramInt1;
    this.c = paramInt2;
    this.d = paramBoolean2;
    this.e = paramInt3;
  }
  
  public boolean isMirrored()
  {
    return this.a;
  }
  
  public int getDegree()
  {
    return this.b;
  }
  
  public int getExifOrientation()
  {
    return this.c;
  }
  
  public boolean isTransposed()
  {
    return this.d;
  }
  
  public int getFlag()
  {
    return this.e;
  }
  
  public boolean isMatch(int paramInt, boolean paramBoolean)
  {
    return (this.b == paramInt) && (this.a == paramBoolean);
  }
  
  public static ImageOrientation getValue(int paramInt, boolean paramBoolean)
  {
    paramInt %= 360;
    if (paramInt < 0) {
      paramInt = 360 + paramInt;
    }
    for (ImageOrientation localImageOrientation : values()) {
      if (localImageOrientation.isMatch(paramInt, paramBoolean)) {
        return localImageOrientation;
      }
    }
    return paramBoolean ? UpMirrored : Up;
  }
  
  public static ImageOrientation getValue(int paramInt)
  {
    for (ImageOrientation localImageOrientation : ) {
      if (localImageOrientation.getExifOrientation() == paramInt) {
        return localImageOrientation;
      }
    }
    return Up;
  }
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKCore-3.1.0.jar!\org\lasque\tusdk\core\utils\image\ImageOrientation.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */