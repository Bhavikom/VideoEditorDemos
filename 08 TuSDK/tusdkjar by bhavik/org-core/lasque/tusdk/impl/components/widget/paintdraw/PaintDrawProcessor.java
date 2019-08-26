package org.lasque.tusdk.impl.components.widget.paintdraw;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Cap;
import android.graphics.Paint.Join;
import android.graphics.Paint.Style;
import android.graphics.Path;
import android.graphics.PointF;
import org.lasque.tusdk.impl.components.widget.smudge.SimpleProcessor;
import org.lasque.tusdk.modules.view.widget.smudge.BrushSize.SizeType;

public class PaintDrawProcessor
  extends SimpleProcessor
{
  private Paint.Cap a = Paint.Cap.ROUND;
  private Paint.Join b = Paint.Join.ROUND;
  private int c = -16777216;
  private float d = 0.0F;
  private Path e;
  private Paint f;
  private PointF g;
  private BrushSize.SizeType h = BrushSize.SizeType.MediumBrush;
  private float i = 10.0F;
  
  public void setMinDistance(float paramFloat)
  {
    this.i = (paramFloat > 20.0F ? 20.0F : paramFloat < 10.0F ? 10.0F : paramFloat);
  }
  
  protected void drawAtPoint(float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4, float paramFloat5)
  {
    throw new UnsupportedOperationException();
  }
  
  protected Bitmap getSmudgeImage(Bitmap paramBitmap, boolean paramBoolean)
  {
    return super.getSmudgeImage(paramBitmap, paramBoolean);
  }
  
  protected void destroy()
  {
    super.destroy();
  }
  
  public void setPaintCap(Paint.Cap paramCap)
  {
    this.a = paramCap;
    b();
  }
  
  public void setPaintJoin(Paint.Join paramJoin)
  {
    this.b = paramJoin;
    b();
  }
  
  public void setPaintColor(int paramInt)
  {
    this.c = paramInt;
    b();
  }
  
  public PaintDrawProcessor()
  {
    this.mBrushScale = 3.0F;
  }
  
  protected void init(Bitmap paramBitmap1, Bitmap paramBitmap2, int paramInt)
  {
    super.init(paramBitmap1, paramBitmap2, paramInt);
    b();
  }
  
  protected int getImageWidth()
  {
    return super.getImageWidth();
  }
  
  protected int getImageHeight()
  {
    return super.getImageHeight();
  }
  
  public void setBrushSize(BrushSize.SizeType paramSizeType)
  {
    setBrushSize(paramSizeType, this.mBrushScale);
  }
  
  public void setBrushScale(float paramFloat)
  {
    if ((paramFloat < 1.0F) || (paramFloat > 3.0F)) {
      return;
    }
    setBrushSize(this.h, paramFloat);
  }
  
  public void setBrushSize(BrushSize.SizeType paramSizeType, float paramFloat)
  {
    this.mBrushScale = paramFloat;
    this.h = paramSizeType;
    switch (1.a[paramSizeType.ordinal()])
    {
    case 1: 
      this.d = (1.0F * paramFloat);
      break;
    case 2: 
      this.d = (2.0F * paramFloat);
      break;
    case 3: 
      this.d = (4.0F * paramFloat);
      break;
    case 4: 
      this.d = (paramSizeType.getCustomizeBrushValue() * 4.0F * paramFloat);
    }
    b();
  }
  
  protected int getMaxUndoCount()
  {
    return super.getMaxUndoCount();
  }
  
  protected void setMaxUndoCount(int paramInt)
  {
    super.setMaxUndoCount(paramInt);
  }
  
  protected int getRedoCount()
  {
    return super.getRedoCount();
  }
  
  protected int getUndoCount()
  {
    return super.getUndoCount();
  }
  
  protected void saveCurrentAsHistory()
  {
    super.saveCurrentAsHistory();
  }
  
  protected Bitmap getRedoData()
  {
    return super.getRedoData();
  }
  
  protected Bitmap getUndoData()
  {
    return super.getUndoData();
  }
  
  protected Bitmap getCanvasImage()
  {
    return super.getCanvasImage();
  }
  
  protected Bitmap getOriginalImage()
  {
    return super.getOriginalImage();
  }
  
  protected void touchBegan(PointF paramPointF)
  {
    super.touchBegan();
    this.g = new PointF(paramPointF.x, paramPointF.y);
    this.e = new Path();
    this.e.moveTo(paramPointF.x, paramPointF.y);
  }
  
  protected void pathMove(PointF paramPointF)
  {
    int j = this.originalSnap.getWidth();
    int k = this.originalSnap.getHeight();
    if ((paramPointF.x >= 0.0F) && (paramPointF.y >= 0.0F) && (paramPointF.x < j) && (paramPointF.y < k))
    {
      if ((Math.abs(this.g.x - paramPointF.x) < this.i) && (Math.abs(this.g.y - paramPointF.y) < this.i)) {
        return;
      }
      this.e.quadTo(this.g.x, this.g.y, (this.g.x + paramPointF.x) / 2.0F, (this.g.y + paramPointF.y) / 2.0F);
      this.g.set(paramPointF.x, paramPointF.y);
      a();
    }
  }
  
  protected void touchEnd()
  {
    b();
  }
  
  private void a()
  {
    this.smudgeCanvas.drawPath(this.e, this.f);
  }
  
  private void b()
  {
    if (this.f == null) {
      this.f = new Paint();
    } else {
      this.f.reset();
    }
    this.f.setAntiAlias(true);
    this.f.setDither(true);
    this.f.setStyle(Paint.Style.STROKE);
    this.f.setStrokeWidth(this.d);
    this.f.setColor(this.c);
    this.f.setStrokeJoin(this.b);
    this.f.setStrokeCap(this.a);
  }
  
  protected float getMaxTemplateDistance(float paramFloat)
  {
    return super.getMaxTemplateDistance(paramFloat);
  }
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKCore-3.1.0.jar!\org\lasque\tusdk\impl\components\widget\paintdraw\PaintDrawProcessor.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */