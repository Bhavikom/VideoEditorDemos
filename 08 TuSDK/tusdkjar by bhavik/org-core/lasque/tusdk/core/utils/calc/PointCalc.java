package org.lasque.tusdk.core.utils.calc;

import android.graphics.PointF;
import java.util.List;
import org.lasque.tusdk.core.utils.RectHelper;

public class PointCalc
{
  public static float distance(PointF paramPointF1, PointF paramPointF2)
  {
    return RectHelper.getDistanceOfTwoPoints(paramPointF2, paramPointF1);
  }
  
  public static PointF center(PointF paramPointF1, PointF paramPointF2)
  {
    PointF localPointF = new PointF();
    localPointF.x = ((paramPointF1.x + paramPointF2.x) / 2.0F);
    localPointF.y = ((paramPointF1.y + paramPointF2.y) / 2.0F);
    return localPointF;
  }
  
  public static PointF crossPoint(PointF paramPointF1, PointF paramPointF2, PointF paramPointF3, PointF paramPointF4)
  {
    float f1 = a(paramPointF3, paramPointF4, paramPointF1, paramPointF2);
    float f2 = a(paramPointF1, paramPointF2, paramPointF1, paramPointF3);
    float f3 = paramPointF3.x + (paramPointF4.x - paramPointF3.x) * f2 / f1;
    float f4 = paramPointF3.y + (paramPointF4.y - paramPointF3.y) * f2 / f1;
    return new PointF(f3, f4);
  }
  
  private static float a(PointF paramPointF1, PointF paramPointF2, PointF paramPointF3, PointF paramPointF4)
  {
    return (paramPointF2.x - paramPointF1.x) * (paramPointF4.y - paramPointF3.y) - (paramPointF2.y - paramPointF1.y) * (paramPointF4.x - paramPointF3.x);
  }
  
  public static PointF increase(PointF paramPointF1, PointF paramPointF2, float paramFloat)
  {
    if (paramFloat == 0.0F) {
      return new PointF(paramPointF2.x, paramPointF2.y);
    }
    float f1 = paramPointF2.x - paramPointF1.x;
    float f2 = paramPointF2.y - paramPointF1.y;
    float f3 = (float)Math.sqrt(paramFloat * paramFloat / (f2 / f1 * (f2 / f1) + 1.0F));
    if (f1 <= 0.0F) {
      f3 = -f3;
    }
    if (paramFloat < 0.0F) {
      f3 = -f3;
    }
    PointF localPointF = new PointF();
    paramPointF2.x += f3;
    paramPointF2.y += f2 / f1 * f3;
    return localPointF;
  }
  
  public static PointF increasePercentage(PointF paramPointF1, PointF paramPointF2, float paramFloat)
  {
    float f = Math.abs(distance(paramPointF1, paramPointF2));
    return increase(paramPointF1, paramPointF2, f * paramFloat);
  }
  
  public static PointF pointOf(PointF paramPointF1, PointF paramPointF2, float paramFloat)
  {
    return increase(paramPointF2, paramPointF1, -paramFloat);
  }
  
  public static PointF pointOfPercentage(PointF paramPointF1, PointF paramPointF2, float paramFloat)
  {
    float f = Math.abs(distance(paramPointF1, paramPointF2));
    return increase(paramPointF2, paramPointF1, -paramFloat * f);
  }
  
  public static PointF crossPoint(PointF paramPointF1, PointF paramPointF2, PointF paramPointF3, float paramFloat1, float paramFloat2)
  {
    float f1 = Math.abs(distance(paramPointF1, paramPointF2));
    PointF localPointF1 = pointOf(paramPointF1, paramPointF2, f1 * paramFloat1);
    float f2 = Math.abs(distance(paramPointF3, localPointF1));
    float f3 = Math.abs(distance(paramPointF3, paramPointF1));
    float f4 = f3 * paramFloat1 + paramFloat2 * (1.0F - paramFloat1);
    PointF localPointF2 = increase(paramPointF3, localPointF1, f4 - f2);
    return localPointF2;
  }
  
  public static PointF crossPoint(PointF paramPointF1, PointF paramPointF2, PointF paramPointF3)
  {
    float f1 = (paramPointF1.y - paramPointF2.y) / (paramPointF2.x - paramPointF2.x);
    float f2 = paramPointF1.y - f1 * paramPointF1.y;
    float f3 = paramPointF3.x + f1 * paramPointF3.y;
    PointF localPointF = new PointF();
    localPointF.x = ((f3 - f1 * f2) / (f1 * f1 + 1.0F));
    localPointF.y = (f1 * localPointF.x + f2);
    return localPointF;
  }
  
  public static void scalePoint(List<PointF> paramList, PointF paramPointF, float paramFloat)
  {
    if ((paramList == null) || (paramPointF == null) || (paramFloat == 0.0F)) {
      return;
    }
    int i = 0;
    int j = paramList.size();
    while (i < j)
    {
      PointF localPointF1 = (PointF)paramList.get(i);
      PointF localPointF2 = increasePercentage(paramPointF, localPointF1, paramFloat);
      paramList.set(i, localPointF2);
      i++;
    }
  }
  
  public static void scalePoint(PointF[] paramArrayOfPointF, PointF paramPointF, float paramFloat)
  {
    if ((paramArrayOfPointF == null) || (paramPointF == null) || (paramFloat == 0.0F)) {
      return;
    }
    int i = 0;
    int j = paramArrayOfPointF.length;
    while (i < j)
    {
      PointF localPointF1 = paramArrayOfPointF[i];
      PointF localPointF2 = increasePercentage(paramPointF, localPointF1, paramFloat);
      paramArrayOfPointF[i] = localPointF2;
      i++;
    }
  }
  
  public static void disPoints(List<PointF> paramList, PointF paramPointF, float paramFloat)
  {
    for (int i = 1; i < paramList.size(); i++)
    {
      PointF localPointF1 = (PointF)paramList.get(i);
      PointF localPointF2 = increase(paramPointF, localPointF1, paramFloat);
      paramList.set(i, localPointF2);
    }
  }
  
  public static void scaleChinPoint(List<PointF> paramList, int[] paramArrayOfInt, PointF paramPointF, float paramFloat)
  {
    float f1 = Math.abs(distance((PointF)paramList.get(paramArrayOfInt[0]), (PointF)paramList.get(paramArrayOfInt[2])));
    PointF localPointF1 = (PointF)paramList.get(paramArrayOfInt[1]);
    PointF localPointF2 = crossPoint(paramPointF, localPointF1, (PointF)paramList.get(paramArrayOfInt[0]), (PointF)paramList.get(paramArrayOfInt[2]));
    float f2 = Math.abs(distance(paramPointF, localPointF2));
    float f3 = Math.abs(distance(paramPointF, localPointF1));
    if (f2 >= f3) {
      return;
    }
    float f4 = f3 - f2;
    float f5 = f4 - f4 * (f4 / f1) * 1.2F * (1.0F - paramFloat);
    PointF localPointF3 = increase(paramPointF, localPointF2, f5);
    paramList.set(paramArrayOfInt[1], localPointF3);
  }
  
  public static void scaleChinPoint2(List<PointF> paramList, int[] paramArrayOfInt, PointF paramPointF, float paramFloat)
  {
    if ((paramList == null) || (paramPointF == null) || (paramFloat == 0.0F)) {
      return;
    }
    for (int k : paramArrayOfInt)
    {
      PointF localPointF1 = (PointF)paramList.get(k);
      PointF localPointF2 = pointOfPercentage(localPointF1, paramPointF, paramFloat);
      paramList.set(k, localPointF2);
    }
  }
  
  public static void scaleChinPoint3(List<PointF> paramList, int paramInt, PointF paramPointF, float paramFloat)
  {
    if ((paramList == null) || (paramPointF == null) || (paramFloat == 0.0F)) {
      return;
    }
    PointF localPointF = pointOfPercentage((PointF)paramList.get(paramInt), paramPointF, (paramFloat - 1.0F) * 0.8F);
    paramList.set(paramInt, localPointF);
  }
  
  public static void scaleEyeEnlargePoint(List<PointF> paramList, PointF paramPointF, float paramFloat)
  {
    PointF localPointF1 = increasePercentage(paramPointF, (PointF)paramList.get(0), (paramFloat - 1.0F) * 1.6F);
    PointF localPointF2 = increasePercentage(paramPointF, (PointF)paramList.get(1), (paramFloat - 1.0F) * 1.6F);
    PointF localPointF3 = increasePercentage(paramPointF, (PointF)paramList.get(2), (paramFloat - 1.0F) / 2.0F);
    PointF localPointF4 = increasePercentage(paramPointF, (PointF)paramList.get(3), (paramFloat - 1.0F) / 2.0F);
    paramList.set(0, localPointF1);
    paramList.set(1, localPointF2);
    paramList.set(2, localPointF3);
    paramList.set(3, localPointF4);
  }
  
  public static void movePoints(List<PointF> paramList, PointF paramPointF, float paramFloat)
  {
    int i = 0;
    int j = paramList.size();
    while (i < j)
    {
      PointF localPointF = increasePercentage(paramPointF, (PointF)paramList.get(i), paramFloat);
      paramList.set(i, localPointF);
      i++;
    }
  }
  
  public static void moveEyeBrow(List<PointF> paramList, PointF paramPointF, float paramFloat)
  {
    int i = 0;
    int j = paramList.size() / 2;
    while (i < j)
    {
      PointF localPointF1 = increasePercentage(paramPointF, (PointF)paramList.get(i + 3), paramFloat);
      PointF localPointF2 = increasePercentage(paramPointF, (PointF)paramList.get(i), paramFloat);
      paramList.set(i, localPointF2);
      paramList.set(i + 3, localPointF1);
      i++;
    }
  }
  
  public static void calcArchEyebrow(List<PointF> paramList, PointF paramPointF, float paramFloat)
  {
    PointF localPointF1 = increasePercentage(paramPointF, (PointF)paramList.get(1), paramFloat * 0.5F);
    PointF localPointF2 = increasePercentage(paramPointF, (PointF)paramList.get(3), paramFloat * 0.5F);
    PointF localPointF3 = increasePercentage(paramPointF, (PointF)paramList.get(5), paramFloat * 0.5F);
    paramList.set(1, localPointF1);
    paramList.set(3, localPointF2);
    paramList.set(5, localPointF3);
  }
  
  public static float smoothstep(float paramFloat1, float paramFloat2, float paramFloat3)
  {
    if (paramFloat3 < paramFloat1) {
      return 0.0F;
    }
    if (paramFloat3 >= paramFloat2) {
      return 1.0F;
    }
    float f1 = (paramFloat3 - paramFloat1) / (paramFloat2 - paramFloat1);
    float f2 = f1 * f1 * (3.0F - 2.0F * f1);
    return f2;
  }
  
  public static void rotatePoints(List<PointF> paramList, PointF paramPointF, float paramFloat)
  {
    float f1 = (float)(paramFloat / 180.0F * 3.141592653589793D);
    for (int i = 0; i < paramList.size(); i++)
    {
      float f2 = distance(paramPointF, (PointF)paramList.get(i));
      float f3 = (float)(Math.sin(f1 / 2.0F) * f2);
      float f4 = (float)(Math.cos(f1 / 2.0F) * f2);
      float f5 = (paramPointF.x * f3 / f4 + ((PointF)paramList.get(i)).x * f4 / f3 + ((PointF)paramList.get(i)).y - paramPointF.y) / (f3 / f4 + f4 / f3);
      float f6 = (paramPointF.y * f3 / f4 + ((PointF)paramList.get(i)).y * f4 / f3 + paramPointF.x - ((PointF)paramList.get(i)).x) / (f3 / f4 + f4 / f3);
      paramList.set(i, new PointF(f5, f6));
    }
  }
  
  public static void scaleJaw(List<PointF> paramList, PointF paramPointF, float paramFloat)
  {
    if ((paramList == null) || (paramPointF == null) || (paramFloat == 0.0F)) {
      return;
    }
    PointF localPointF1 = (PointF)paramList.get(3);
    PointF localPointF2 = (PointF)paramList.get(4);
    PointF localPointF3 = (PointF)paramList.get(5);
    paramList.set(3, pointOfPercentage(localPointF1, paramPointF, paramFloat * 0.5F));
    paramList.set(4, pointOfPercentage(localPointF2, paramPointF, paramFloat));
    paramList.set(5, pointOfPercentage(localPointF3, paramPointF, paramFloat * 0.5F));
  }
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKCore-3.1.0.jar!\org\lasque\tusdk\core\utils\calc\PointCalc.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */