package org.lasque.tusdk.core.struct;

public class TuSdkSizeF
{
  public float width;
  public float height;
  
  public static TuSdkSizeF create(float paramFloat1, float paramFloat2)
  {
    return new TuSdkSizeF(paramFloat1, paramFloat2);
  }
  
  public TuSdkSizeF() {}
  
  public TuSdkSizeF(float paramFloat1, float paramFloat2)
  {
    this.width = paramFloat1;
    this.height = paramFloat2;
  }
  
  public TuSdkSize toSize()
  {
    TuSdkSize localTuSdkSize = TuSdkSize.create((int)this.width, (int)this.width);
    return localTuSdkSize;
  }
  
  public TuSdkSize toSizeFloor()
  {
    TuSdkSize localTuSdkSize = TuSdkSize.create((int)Math.floor(this.width), (int)Math.floor(this.height));
    return localTuSdkSize;
  }
  
  public TuSdkSize toSizeCeil()
  {
    TuSdkSize localTuSdkSize = TuSdkSize.create((int)Math.ceil(this.width), (int)Math.ceil(this.height));
    return localTuSdkSize;
  }
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKCore-3.1.0.jar!\org\lasque\tusdk\core\struct\TuSdkSizeF.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */