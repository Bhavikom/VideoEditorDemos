package org.lasque.tusdk.impl.components.widget.smudge;

import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import org.lasque.tusdk.core.seles.tusdk.FilterWrap;
import org.lasque.tusdk.core.utils.ThreadHelper;
import org.lasque.tusdk.core.utils.hardware.TuSdkGPU;
import org.lasque.tusdk.core.utils.image.BitmapHelper;
import org.lasque.tusdk.modules.view.widget.smudge.BrushData;
import org.lasque.tusdk.modules.view.widget.smudge.BrushData.BrushType;
import org.lasque.tusdk.modules.view.widget.smudge.BrushSize;
import org.lasque.tusdk.modules.view.widget.smudge.BrushSize.SizeType;

public final class FilterSmudgeProcessor
  extends SimpleProcessor
{
  private FilterWrap a;
  private Canvas b;
  private Bitmap c;
  
  protected void init(Bitmap paramBitmap1, Bitmap paramBitmap2, int paramInt)
  {
    super.init(this.c, paramBitmap2, paramInt);
    ThreadHelper.runThread(new Runnable()
    {
      public void run()
      {
        Bitmap localBitmap = FilterSmudgeProcessor.a(FilterSmudgeProcessor.this);
        FilterSmudgeProcessor.a(FilterSmudgeProcessor.this, localBitmap);
      }
    });
    a();
    this.b = new Canvas(this.brushSnap);
  }
  
  private void a(Bitmap paramBitmap)
  {
    final Bitmap localBitmap = paramBitmap;
    ThreadHelper.post(new Runnable()
    {
      public void run()
      {
        FilterSmudgeProcessor.b(FilterSmudgeProcessor.this, localBitmap);
      }
    });
  }
  
  private void a()
  {
    BrushData localBrushData = BrushData.create(-1L, "", "");
    localBrushData.setType(BrushData.BrushType.TypeMosaic);
    this.mCurrentBrush = localBrushData;
    this.brushSnap = BitmapHelper.createOvalImage(72, 72, -16777216);
  }
  
  protected void setBrush(BrushData paramBrushData) {}
  
  protected void setBrushSize(BrushSize.SizeType paramSizeType)
  {
    if (paramSizeType == null) {
      return;
    }
    double d = BrushSize.getBrushValue(paramSizeType);
    this.mBrushScale = ((int)((d * 20.0D - 1.0D) * 48.0D));
  }
  
  protected FilterWrap getFilterWrap()
  {
    return this.a;
  }
  
  protected final void setFilterWrap(FilterWrap paramFilterWrap)
  {
    this.a = paramFilterWrap;
  }
  
  private Bitmap b()
  {
    FilterWrap localFilterWrap = getFilterWrap();
    if (localFilterWrap == null) {
      return null;
    }
    Bitmap localBitmap = localFilterWrap.process(this.originalSnap);
    if (TuSdkGPU.lowPerformance()) {
      localBitmap = b(localBitmap);
    }
    return localBitmap;
  }
  
  private Bitmap b(Bitmap paramBitmap)
  {
    Bitmap localBitmap = Bitmap.createBitmap(paramBitmap.getWidth(), paramBitmap.getHeight(), Bitmap.Config.ARGB_8888);
    Canvas localCanvas = new Canvas(localBitmap);
    ColorMatrix localColorMatrix = new ColorMatrix();
    float[] arrayOfFloat = { 1.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 1.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 1.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 255.0F };
    localColorMatrix.set(arrayOfFloat);
    Paint localPaint = new Paint();
    localPaint.setColorFilter(new ColorMatrixColorFilter(localColorMatrix));
    localPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC));
    localCanvas.drawBitmap(paramBitmap, new Matrix(), localPaint);
    return localBitmap;
  }
  
  protected float getMaxTemplateDistance(float paramFloat)
  {
    return paramFloat * 0.1F;
  }
  
  protected void drawAtPoint(float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4, float paramFloat5)
  {
    if (this.c == null) {
      return;
    }
    if ((paramFloat1 >= 0.0F) && (paramFloat1 < getImageWidth()) && (paramFloat2 >= 0.0F) && (paramFloat2 < getImageHeight())) {
      a(paramFloat1, paramFloat2, paramFloat4);
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
      float f = this.brushSnap.getWidth() * paramFloat3;
      Matrix localMatrix = new Matrix();
      localMatrix.postScale(1.0F / paramFloat3, 1.0F / paramFloat3);
      localMatrix.postTranslate((f / 2.0F - paramFloat1) / paramFloat3, (f / 2.0F - paramFloat2) / paramFloat3);
      Paint localPaint = new Paint();
      localPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_ATOP));
      this.b.drawBitmap(this.c, localMatrix, localPaint);
      localMatrix = new Matrix();
      localMatrix.postScale(paramFloat3, paramFloat3);
      localMatrix.postTranslate(paramFloat1 - f / 2.0F, paramFloat2 - f / 2.0F);
      localPaint = new Paint();
      localPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_OVER));
      this.smudgeCanvas.drawBitmap(this.brushSnap, localMatrix, localPaint);
    }
  }
  
  protected void destroy()
  {
    super.destroy();
    BitmapHelper.recycled(this.c);
  }
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKCore-3.1.0.jar!\org\lasque\tusdk\impl\components\widget\smudge\FilterSmudgeProcessor.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */