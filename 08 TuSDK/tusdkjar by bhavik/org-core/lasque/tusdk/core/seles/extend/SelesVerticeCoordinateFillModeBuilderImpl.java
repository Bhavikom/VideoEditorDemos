package org.lasque.tusdk.core.seles.extend;

import android.graphics.Rect;
import android.graphics.RectF;
import java.nio.FloatBuffer;
import org.lasque.tusdk.core.seles.output.SelesSurfacePusher;
import org.lasque.tusdk.core.seles.output.SelesView.SelesFillModeType;
import org.lasque.tusdk.core.struct.TuSdkSize;
import org.lasque.tusdk.core.utils.RectHelper;
import org.lasque.tusdk.core.utils.TLog;
import org.lasque.tusdk.core.utils.image.ImageOrientation;

public class SelesVerticeCoordinateFillModeBuilderImpl
  implements SelesVerticeCoordinateFillModeBuilder
{
  private TuSdkSize a = TuSdkSize.create(0);
  private TuSdkSize b = TuSdkSize.create(0);
  private RectF c = new RectF();
  private ImageOrientation d = ImageOrientation.Up;
  private boolean e = false;
  private boolean f = false;
  private SelesVerticeCoordinateFillModeBuilder.OnDisplaySizeChangeListener g;
  private SelesView.SelesFillModeType h = SelesView.SelesFillModeType.PreserveAspectRatio;
  
  public SelesVerticeCoordinateFillModeBuilderImpl(boolean paramBoolean)
  {
    this.f = paramBoolean;
  }
  
  public void setOutputSize(TuSdkSize paramTuSdkSize)
  {
    if ((paramTuSdkSize == null) || (!paramTuSdkSize.isSize()))
    {
      TLog.w("%s setOutputSize is Null or side < 1, size: %s", new Object[] { "_SelesFillModeVerticeCoordinateBuilder", paramTuSdkSize });
      return;
    }
    if (paramTuSdkSize.equals(this.b)) {
      return;
    }
    this.b = paramTuSdkSize.copy();
    this.e = true;
  }
  
  public void setCanvasRect(RectF paramRectF)
  {
    if (this.c.equals(paramRectF)) {
      return;
    }
    this.c = new RectF(paramRectF);
    this.e = true;
  }
  
  public void setFillMode(SelesView.SelesFillModeType paramSelesFillModeType)
  {
    if ((paramSelesFillModeType == null) || (paramSelesFillModeType == this.h)) {
      return;
    }
    this.h = paramSelesFillModeType;
    this.e = true;
  }
  
  public void setOnDisplaySizeChangeListener(SelesVerticeCoordinateFillModeBuilder.OnDisplaySizeChangeListener paramOnDisplaySizeChangeListener)
  {
    this.g = paramOnDisplaySizeChangeListener;
  }
  
  public TuSdkSize outputSize()
  {
    return this.b;
  }
  
  public boolean calculate(TuSdkSize paramTuSdkSize, ImageOrientation paramImageOrientation, FloatBuffer paramFloatBuffer1, FloatBuffer paramFloatBuffer2)
  {
    if ((paramTuSdkSize == null) || (!paramTuSdkSize.isSize()))
    {
      TLog.w("%s setOutputSize is Null or side < 1, size: %s", new Object[] { "_SelesFillModeVerticeCoordinateBuilder", paramTuSdkSize });
      return false;
    }
    if (paramFloatBuffer1 == null)
    {
      TLog.w("%s calculate need verticesBuffer", new Object[] { "_SelesFillModeVerticeCoordinateBuilder" });
      return false;
    }
    if (paramFloatBuffer2 == null)
    {
      TLog.w("%s calculate need textureBuffer", new Object[] { "_SelesFillModeVerticeCoordinateBuilder" });
      return false;
    }
    if (paramImageOrientation == null) {
      paramImageOrientation = ImageOrientation.Up;
    }
    if ((paramTuSdkSize.equals(this.a)) && (paramImageOrientation == this.d) && (!this.e)) {
      return true;
    }
    this.e = false;
    this.a = paramTuSdkSize.copy();
    this.d = paramImageOrientation;
    if (!this.b.isSize()) {
      this.b = paramTuSdkSize.copy();
    }
    paramFloatBuffer2.clear();
    paramFloatBuffer2.put(SelesSurfacePusher.textureCoordinates(paramImageOrientation)).position(0);
    if (this.c.isEmpty()) {
      a(this.b, this.a, paramFloatBuffer1, this.h);
    } else {
      a(this.b, this.a, paramFloatBuffer1, this.c);
    }
    return true;
  }
  
  private void a(TuSdkSize paramTuSdkSize1, TuSdkSize paramTuSdkSize2, FloatBuffer paramFloatBuffer, SelesView.SelesFillModeType paramSelesFillModeType)
  {
    TuSdkSize localTuSdkSize = paramTuSdkSize1.copy();
    Rect localRect = RectHelper.makeRectWithAspectRatioInsideRect(paramTuSdkSize2, new Rect(0, 0, localTuSdkSize.width, localTuSdkSize.height));
    float f2;
    float f1;
    switch (1.a[paramSelesFillModeType.ordinal()])
    {
    case 1: 
      f2 = localRect.width() / localTuSdkSize.width;
      f1 = localRect.height() / localTuSdkSize.height;
      break;
    case 2: 
      f2 = localTuSdkSize.height / localRect.height();
      f1 = localTuSdkSize.width / localRect.width();
      break;
    case 3: 
    default: 
      f2 = 1.0F;
      f1 = 1.0F;
    }
    float[] arrayOfFloat = { -f2, -f1, f2, -f1, -f2, f1, f2, f1 };
    if (this.g != null) {
      this.g.onDisplaySizeChanged(TuSdkSize.create(localRect));
    }
    paramFloatBuffer.clear();
    paramFloatBuffer.put(arrayOfFloat).position(0);
  }
  
  private void a(TuSdkSize paramTuSdkSize1, TuSdkSize paramTuSdkSize2, FloatBuffer paramFloatBuffer, RectF paramRectF)
  {
    TuSdkSize localTuSdkSize = paramTuSdkSize1.copy();
    localTuSdkSize.height = Math.max(localTuSdkSize.width, localTuSdkSize.height);
    RectF localRectF = RectHelper.makeRectWithAspectRatioOutsideRect(paramTuSdkSize2, new RectF(0.0F, 0.0F, localTuSdkSize.width, localTuSdkSize.height));
    float f2 = localRectF.width() / localTuSdkSize.width;
    float f1 = localRectF.height() / localTuSdkSize.height;
    float f3 = f2 * localTuSdkSize.width / localRectF.width() + 2.0F * f2 * paramRectF.left;
    float f4 = f1 * localTuSdkSize.height / localRectF.height() + 2.0F * f1 * paramRectF.top;
    float[] arrayOfFloat = { -f3, -f1, 2.0F * f2 - f3, -f1, -f3, f1, 2.0F * f2 - f3, f1 };
    float f5 = localRectF.width();
    float f6 = localRectF.height();
    if (f5 < f6) {
      arrayOfFloat = new float[] { -f2, -2.0F * f1 + f4, f2, -2.0F * f1 + f4, -f2, f4, f2, f4 };
    }
    paramFloatBuffer.clear();
    paramFloatBuffer.put(arrayOfFloat).position(0);
  }
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKCore-3.1.0.jar!\org\lasque\tusdk\core\seles\extend\SelesVerticeCoordinateFillModeBuilderImpl.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */