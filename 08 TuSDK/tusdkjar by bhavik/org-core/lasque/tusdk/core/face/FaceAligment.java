package org.lasque.tusdk.core.face;

import android.graphics.PointF;
import android.graphics.RectF;

public class FaceAligment
{
  private static int[] a = { 0, 2, 4, 6, 8, 10, 12, 14, 16, 18, 20, 22, 24, 26, 28, 30, 32, 33, 34, 35, 36, 37, 38, 39, 40, 41, 42, 43, 44, 45, 46, 47, 48, 49, 50, 51, 52, 53, 54, 55, 56, 57, 58, 59, 60, 61, 62, 63, 84, 85, 86, 87, 88, 89, 90, 91, 92, 93, 94, 95, 96, 97, 98, 99, 100, 101, 102, 103 };
  public RectF rect;
  private PointF[] b;
  private PointF[] c;
  public float pitch;
  public float yaw;
  public float roll;
  
  public PointF[] getMarks()
  {
    return this.b;
  }
  
  public PointF[] getOrginMarks()
  {
    return this.c;
  }
  
  public FaceAligment() {}
  
  public FaceAligment(PointF[] paramArrayOfPointF)
  {
    setOrginMarks(paramArrayOfPointF);
  }
  
  public final void setOrginMarks(PointF[] paramArrayOfPointF)
  {
    this.c = (this.b = paramArrayOfPointF);
    if ((paramArrayOfPointF != null) && (paramArrayOfPointF.length == 106)) {
      this.b = a(paramArrayOfPointF);
    }
  }
  
  private PointF[] a(PointF[] paramArrayOfPointF)
  {
    PointF[] arrayOfPointF = new PointF[68];
    for (int i = 0; i < 68; i++)
    {
      int j = a[i];
      arrayOfPointF[i] = paramArrayOfPointF[j];
    }
    return arrayOfPointF;
  }
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKCore-3.1.0.jar!\org\lasque\tusdk\core\face\FaceAligment.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */