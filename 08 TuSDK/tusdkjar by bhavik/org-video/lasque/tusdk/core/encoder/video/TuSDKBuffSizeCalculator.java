package org.lasque.tusdk.core.encoder.video;

public class TuSDKBuffSizeCalculator
{
  public static int calculator(int paramInt1, int paramInt2, int paramInt3)
  {
    switch (paramInt3)
    {
    case 17: 
    case 19: 
    case 21: 
    case 842094169: 
      return paramInt1 * paramInt2 * 3 / 2;
    }
    return -1;
  }
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKVideo-3.4.1.jar!\org\lasque\tusdk\core\encoder\video\TuSDKBuffSizeCalculator.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */