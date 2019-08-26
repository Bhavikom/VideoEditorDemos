package org.lasque.tusdk.impl.components.widget.smudge;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffColorFilter;
import android.graphics.PorterDuffXfermode;
import org.lasque.tusdk.core.utils.image.BitmapHelper;
import org.lasque.tusdk.modules.view.widget.smudge.BrushData;
import org.lasque.tusdk.modules.view.widget.smudge.BrushData.BrushType;
import org.lasque.tusdk.modules.view.widget.smudge.BrushData.PositionType;
import org.lasque.tusdk.modules.view.widget.smudge.BrushData.RotateType;
import org.lasque.tusdk.modules.view.widget.smudge.BrushData.SizeType;

public final class SmudgeProcessor
  extends SimpleProcessor
{
  private Canvas a;
  private Bitmap b;
  
  protected void setBrush(BrushData paramBrushData)
  {
    if (paramBrushData == null) {
      return;
    }
    super.setBrush(paramBrushData);
    BitmapHelper.recycled(this.b);
    if ((paramBrushData.getType() == BrushData.BrushType.TypeMosaic) && (this.originalSnap != null))
    {
      this.a = new Canvas(this.brushSnap);
      this.b = a(this.originalSnap);
    }
  }
  
  protected final void drawAtPoint(float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4, float paramFloat5)
  {
    float f = 1.0F - 0.5F / (1.0F + paramFloat3 / this.mBrushScale);
    if ((paramFloat1 >= 0.0F) && (paramFloat1 < getImageWidth()) && (paramFloat2 >= 0.0F) && (paramFloat2 < getImageHeight()))
    {
      BrushData.BrushType localBrushType = getBrush().getType();
      if (localBrushType == BrushData.BrushType.TypeEraser) {
        b(paramFloat1, paramFloat2, paramFloat4);
      } else if (localBrushType == BrushData.BrushType.TypeMosaic) {
        a(paramFloat1, paramFloat2, paramFloat4);
      } else {
        a(paramFloat1, paramFloat2, paramFloat4, f, paramFloat5);
      }
    }
  }
  
  private void a(float paramFloat1, float paramFloat2, float paramFloat3)
  {
    int i = this.originalSnap.getWidth();
    int j = this.originalSnap.getHeight();
    if ((paramFloat1 >= 0.0F) && (paramFloat1 < i) && (paramFloat2 >= 0.0F) && (paramFloat2 < j))
    {
      paramFloat1 = Math.round(paramFloat1);
      paramFloat2 = Math.round(paramFloat2);
      float f1 = this.brushSnap.getWidth() * paramFloat3;
      int k = i / 30;
      int m = j / 30;
      Matrix localMatrix = new Matrix();
      float f2 = (i + 1.0F) / k;
      float f3 = (j + 1.0F) / m;
      localMatrix.postScale(f2 / paramFloat3, f3 / paramFloat3);
      localMatrix.postTranslate((f1 / 2.0F - paramFloat1) / paramFloat3, (f1 / 2.0F - paramFloat2) / paramFloat3);
      Paint localPaint = new Paint();
      localPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
      this.a.drawBitmap(this.b, localMatrix, localPaint);
      localMatrix = new Matrix();
      localMatrix.postScale(paramFloat3, paramFloat3);
      localMatrix.postTranslate(paramFloat1 - f1 / 2.0F, paramFloat2 - f1 / 2.0F);
      localPaint = new Paint();
      localPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_OVER));
      this.smudgeCanvas.drawBitmap(this.brushSnap, localMatrix, localPaint);
    }
  }
  
  private void b(float paramFloat1, float paramFloat2, float paramFloat3)
  {
    int i = this.originalSnap.getWidth();
    int j = this.originalSnap.getHeight();
    if ((paramFloat1 >= 0.0F) && (paramFloat1 < i) && (paramFloat2 >= 0.0F) && (paramFloat2 < j))
    {
      int k = (int)(this.brushSnap.getWidth() * paramFloat3);
      Matrix localMatrix = new Matrix();
      localMatrix.postScale(paramFloat3, paramFloat3);
      localMatrix.postTranslate(paramFloat1 - k / 2, paramFloat2 - k / 2);
      Paint localPaint = new Paint();
      localPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_OUT));
      this.smudgeCanvas.drawBitmap(this.brushSnap, localMatrix, localPaint);
    }
  }
  
  private void a(double paramDouble1, double paramDouble2, double paramDouble3, double paramDouble4, double paramDouble5)
  {
    BrushData localBrushData = getBrush();
    BrushData.RotateType localRotateType = localBrushData.getRotateType();
    if (localRotateType == BrushData.RotateType.RotateRandom) {
      paramDouble5 = Math.random() * 360.0D;
    } else if (localRotateType == BrushData.RotateType.RotateLimitRandom) {
      paramDouble5 += (Math.random() - 0.5D) * 60.0D;
    } else if (localRotateType == BrushData.RotateType.RotateNone) {
      paramDouble5 = 0.0D;
    }
    double d1;
    if (localBrushData.getPositionType() == BrushData.PositionType.PositionRandom)
    {
      d1 = this.brushSnap.getWidth() * paramDouble3;
      double d2 = d1 * (Math.random() - 0.5D);
      double d3 = d1 * (Math.random() - 0.5D);
      paramDouble1 += d2;
      paramDouble2 += d3;
    }
    if (localBrushData.getSizeType() == BrushData.SizeType.SizeRandom)
    {
      d1 = Math.random();
      paramDouble3 *= d1;
      paramDouble4 *= (1.0D - 0.25D * d1);
    }
    b(paramDouble1, paramDouble2, paramDouble3, paramDouble5, paramDouble4);
  }
  
  private void b(double paramDouble1, double paramDouble2, double paramDouble3, double paramDouble4, double paramDouble5)
  {
    int i = this.originalSnap.getWidth();
    int j = this.originalSnap.getHeight();
    if ((paramDouble1 >= 0.0D) && (paramDouble1 < i) && (paramDouble2 >= 0.0D) && (paramDouble2 < j))
    {
      int k = (int)(this.brushSnap.getWidth() * paramDouble3);
      int m = this.originalSnap.getPixel((int)paramDouble1, (int)paramDouble2);
      Matrix localMatrix = new Matrix();
      localMatrix.postScale((float)paramDouble3, (float)paramDouble3);
      localMatrix.postTranslate(-k / 2, -k / 2);
      localMatrix.postRotate((float)paramDouble4);
      localMatrix.postTranslate((float)paramDouble1, (float)paramDouble2);
      Paint localPaint = new Paint();
      PorterDuffColorFilter localPorterDuffColorFilter = new PorterDuffColorFilter(m, PorterDuff.Mode.SRC_ATOP);
      localPaint.setAlpha((int)(paramDouble5 * 255.0D));
      localPaint.setColorFilter(localPorterDuffColorFilter);
      localPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_OVER));
      this.smudgeCanvas.drawBitmap(this.brushSnap, localMatrix, localPaint);
    }
  }
  
  private Bitmap a(Bitmap paramBitmap)
  {
    int i = paramBitmap.getWidth();
    int j = paramBitmap.getHeight();
    int k = i / 30;
    int m = j / 30;
    return Bitmap.createScaledBitmap(paramBitmap, k, m, true);
  }
  
  public void destroy()
  {
    super.destroy();
    BitmapHelper.recycled(this.b);
  }
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKCore-3.1.0.jar!\org\lasque\tusdk\impl\components\widget\smudge\SmudgeProcessor.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */