package org.lasque.tusdk.core.utils.hardware;

import android.graphics.PointF;
import android.graphics.RectF;
import org.lasque.tusdk.core.utils.image.ImageOrientation;

public class TuSdkFace
{
  public RectF rect;
  public int score;
  public int id = -1;
  public PointF leftEye = null;
  public PointF rightEye = null;
  public PointF mouth = null;
  
  public String toString()
  {
    String str = String.format("Class [%s]: %s \n detail id[%s]: rect[%s], score[%s], leftEye[%s], rightEye[%s], mouth[%s]", new Object[] { Integer.valueOf(hashCode()), getClass().getName(), Integer.valueOf(this.id), this.rect, Integer.valueOf(this.score), this.leftEye, this.rightEye, this.mouth });
    return str;
  }
  
  public static void convertOrientation(TuSdkFace paramTuSdkFace, ImageOrientation paramImageOrientation)
  {
    if ((paramTuSdkFace == null) || (paramImageOrientation == null)) {
      return;
    }
    paramTuSdkFace.leftEye = a(paramTuSdkFace.leftEye, paramImageOrientation);
    paramTuSdkFace.rightEye = a(paramTuSdkFace.rightEye, paramImageOrientation);
    paramTuSdkFace.mouth = a(paramTuSdkFace.mouth, paramImageOrientation);
    PointF localPointF1 = a(new PointF(paramTuSdkFace.rect.left, paramTuSdkFace.rect.top), paramImageOrientation);
    PointF localPointF2 = a(new PointF(paramTuSdkFace.rect.right, paramTuSdkFace.rect.bottom), paramImageOrientation);
    paramTuSdkFace.rect = new RectF();
    paramTuSdkFace.rect.left = Math.min(localPointF1.x, localPointF2.x);
    paramTuSdkFace.rect.top = Math.min(localPointF1.y, localPointF2.y);
    paramTuSdkFace.rect.right = Math.max(localPointF1.x, localPointF2.x);
    paramTuSdkFace.rect.bottom = Math.max(localPointF1.y, localPointF2.y);
  }
  
  private static PointF a(PointF paramPointF, ImageOrientation paramImageOrientation)
  {
    if (paramPointF == null) {
      return paramPointF;
    }
    PointF localPointF = new PointF();
    localPointF.set(paramPointF);
    switch (1.a[paramImageOrientation.ordinal()])
    {
    case 1: 
      localPointF.x = (1.0F - paramPointF.y);
      localPointF.y = paramPointF.x;
      break;
    case 2: 
      localPointF.x = (1.0F - paramPointF.x);
      localPointF.y = (1.0F - paramPointF.y);
      break;
    case 3: 
      localPointF.x = paramPointF.y;
      localPointF.y = (1.0F - paramPointF.x);
      break;
    case 4: 
      localPointF.x = (1.0F - paramPointF.x);
      localPointF.y = paramPointF.y;
      break;
    case 5: 
      localPointF.x = paramPointF.y;
      localPointF.y = paramPointF.x;
      break;
    case 6: 
      localPointF.x = paramPointF.x;
      localPointF.y = (1.0F - paramPointF.y);
      break;
    case 7: 
      localPointF.x = (1.0F - paramPointF.y);
      localPointF.y = (1.0F - paramPointF.x);
      break;
    }
    return localPointF;
  }
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKCore-3.1.0.jar!\org\lasque\tusdk\core\utils\hardware\TuSdkFace.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */