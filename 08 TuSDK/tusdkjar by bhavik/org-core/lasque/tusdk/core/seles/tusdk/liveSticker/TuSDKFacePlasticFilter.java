package org.lasque.tusdk.core.seles.tusdk.liveSticker;

import android.graphics.PointF;
import android.graphics.RectF;
import java.nio.FloatBuffer;
import org.lasque.tusdk.core.face.FaceAligment;
import org.lasque.tusdk.core.seles.SelesGLProgram;
import org.lasque.tusdk.core.seles.SelesParameters;
import org.lasque.tusdk.core.seles.SelesParameters.FilterArg;
import org.lasque.tusdk.core.seles.SelesParameters.FilterParameterInterface;
import org.lasque.tusdk.core.seles.filters.SelesFilter;
import org.lasque.tusdk.core.seles.filters.SelesPointDrawFilter;
import org.lasque.tusdk.core.struct.TuSdkSize;
import org.lasque.tusdk.core.utils.TLog;

public class TuSDKFacePlasticFilter
  extends SelesFilter
  implements SelesParameters.FilterParameterInterface
{
  private int a;
  private int b;
  private int c;
  private int d;
  private float e = 1.05F;
  private float f = 0.048F;
  private float g = 1.7777778F;
  protected FaceAligment[] mFaces;
  protected float mDeviceAngle = 0.0F;
  private SelesPointDrawFilter h;
  private boolean i = false;
  
  public TuSDKFacePlasticFilter()
  {
    super("-sfbf3");
    if (this.i)
    {
      this.h = new SelesPointDrawFilter();
      addTarget(this.h, 0);
    }
  }
  
  protected void onInitOnGLThread()
  {
    super.onInitOnGLThread();
    this.a = this.mFilterProgram.uniformIndex("eyePower");
    this.b = this.mFilterProgram.uniformIndex("chinPower");
    this.c = this.mFilterProgram.uniformIndex("screenRatio");
    this.d = this.mFilterProgram.uniformIndex("faceInfo");
    setEyeEnlargeSize(this.e);
    setChinSize(this.f);
    setScreenRatio(this.g);
    updateFaceFeatures(this.mFaces, this.mDeviceAngle);
    checkGLError(getClass().getSimpleName() + " onInitOnGLThread");
  }
  
  protected void renderToTexture(FloatBuffer paramFloatBuffer1, FloatBuffer paramFloatBuffer2)
  {
    super.renderToTexture(paramFloatBuffer1, paramFloatBuffer2);
    checkGLError(getClass().getSimpleName());
    captureFilterImage(getClass().getSimpleName(), this.mInputTextureSize.width, this.mInputTextureSize.height);
  }
  
  public void setInputSize(TuSdkSize paramTuSdkSize, int paramInt)
  {
    super.setInputSize(paramTuSdkSize, paramInt);
    float f1 = paramTuSdkSize.height / paramTuSdkSize.width;
    if (f1 != this.g) {
      setScreenRatio(f1);
    }
  }
  
  private RectF a(PointF paramPointF1, PointF paramPointF2, PointF paramPointF3, float paramFloat)
  {
    PointF localPointF1 = new PointF(paramPointF1.x, paramPointF1.y);
    PointF localPointF2 = new PointF(paramPointF2.x, paramPointF2.y);
    PointF localPointF3 = new PointF(paramPointF3.x, paramPointF3.y);
    localPointF1.y *= paramFloat;
    localPointF2.y *= paramFloat;
    localPointF3.y *= paramFloat;
    float f1 = localPointF1.x - localPointF2.x;
    float f3 = localPointF1.y - localPointF2.y;
    float f5 = (float)(Math.pow(localPointF1.x, 2.0D) - Math.pow(localPointF2.x, 2.0D) + Math.pow(localPointF1.y, 2.0D) - Math.pow(localPointF2.y, 2.0D)) / 2.0F;
    float f2 = localPointF3.x - localPointF2.x;
    float f4 = localPointF3.y - localPointF2.y;
    float f6 = (float)(Math.pow(localPointF3.x, 2.0D) - Math.pow(localPointF2.x, 2.0D) + Math.pow(localPointF3.y, 2.0D) - Math.pow(localPointF2.y, 2.0D)) / 2.0F;
    float f7 = f1 * f4 - f2 * f3;
    float f8 = 0.0F;
    float f9 = 0.0F;
    float f10 = 0.0F;
    if (f7 == 0.0F)
    {
      f8 = localPointF1.x;
      f9 = localPointF1.y;
    }
    else
    {
      f8 = (f5 * f4 - f6 * f3) / f7;
      f9 = (f1 * f6 - f2 * f5) / f7;
    }
    f10 = (float)Math.sqrt((f8 - localPointF1.x) * (f8 - localPointF1.x) + (f9 - localPointF1.y) * (f9 - localPointF1.y));
    f9 /= paramFloat;
    return new RectF(f8, f9, f10, f10);
  }
  
  public void updateFaceFeatures(FaceAligment[] paramArrayOfFaceAligment, float paramFloat)
  {
    this.mFaces = paramArrayOfFaceAligment;
    this.mDeviceAngle = paramFloat;
    PointF[] arrayOfPointF = null;
    if ((paramArrayOfFaceAligment != null) && (paramArrayOfFaceAligment.length > 0)) {
      arrayOfPointF = paramArrayOfFaceAligment[0].getMarks();
    }
    a(arrayOfPointF);
  }
  
  private void a(PointF[] paramArrayOfPointF)
  {
    float[] arrayOfFloat = new float[24];
    for (int j = 0; j < 24; j++) {
      arrayOfFloat[j] = 0.0F;
    }
    if ((paramArrayOfPointF == null) || (paramArrayOfPointF.length == 0))
    {
      setFloatArray(arrayOfFloat, this.d, this.mFilterProgram);
      return;
    }
    PointF localPointF1 = new PointF(paramArrayOfPointF[0].x, paramArrayOfPointF[0].y);
    PointF localPointF2 = new PointF(paramArrayOfPointF[4].x, paramArrayOfPointF[4].y);
    PointF localPointF3 = new PointF(paramArrayOfPointF[8].x, paramArrayOfPointF[8].y);
    PointF localPointF4 = new PointF(paramArrayOfPointF[12].x, paramArrayOfPointF[12].y);
    PointF localPointF5 = new PointF(paramArrayOfPointF[16].x, paramArrayOfPointF[16].y);
    RectF localRectF1 = a(localPointF1, localPointF2, localPointF3, getScreenRatio());
    RectF localRectF2 = a(localPointF3, localPointF4, localPointF5, getScreenRatio());
    PointF localPointF6 = a(new int[] { 37, 38, 40, 41 }, paramArrayOfPointF);
    PointF localPointF7 = a(new int[] { 43, 44, 46, 47 }, paramArrayOfPointF);
    arrayOfFloat[0] = localPointF6.x;
    arrayOfFloat[1] = localPointF6.y;
    arrayOfFloat[2] = localPointF7.x;
    arrayOfFloat[3] = localPointF7.y;
    arrayOfFloat[4] = localPointF1.x;
    arrayOfFloat[5] = localPointF1.y;
    arrayOfFloat[6] = localPointF3.x;
    arrayOfFloat[7] = localPointF3.y;
    arrayOfFloat[8] = localPointF5.x;
    arrayOfFloat[9] = localPointF5.y;
    arrayOfFloat[10] = localRectF1.left;
    arrayOfFloat[11] = localRectF1.top;
    arrayOfFloat[12] = localRectF1.right;
    arrayOfFloat[13] = localRectF2.left;
    arrayOfFloat[14] = localRectF2.top;
    arrayOfFloat[15] = localRectF2.right;
    PointF localPointF8 = new PointF();
    localPointF8.x = (paramArrayOfPointF[36].x - (localPointF6.x - paramArrayOfPointF[36].x) * 2.5F);
    localPointF8.y = (paramArrayOfPointF[36].y - (localPointF6.y - paramArrayOfPointF[36].y) * 2.5F);
    PointF localPointF9 = new PointF();
    localPointF9.x = ((paramArrayOfPointF[37].x + paramArrayOfPointF[38].x) * 0.5F);
    localPointF9.y = ((paramArrayOfPointF[37].y + paramArrayOfPointF[38].y) * 0.5F);
    localPointF9.x -= (localPointF6.x - localPointF9.x) * 7.0F;
    localPointF9.y -= (localPointF6.y - localPointF9.y) * 7.0F;
    arrayOfFloat[16] = localPointF8.x;
    arrayOfFloat[17] = localPointF8.y;
    arrayOfFloat[18] = localPointF9.x;
    arrayOfFloat[19] = localPointF9.y;
    PointF localPointF10 = new PointF();
    localPointF10.x = (paramArrayOfPointF[45].x - (localPointF7.x - paramArrayOfPointF[45].x) * 2.5F);
    localPointF10.y = (paramArrayOfPointF[45].y - (localPointF7.y - paramArrayOfPointF[45].y) * 2.5F);
    PointF localPointF11 = new PointF();
    localPointF11.x = ((paramArrayOfPointF[43].x + paramArrayOfPointF[44].x) * 0.5F);
    localPointF11.y = ((paramArrayOfPointF[43].y + paramArrayOfPointF[44].y) * 0.5F);
    localPointF11.x -= (localPointF7.x - localPointF11.x) * 7.0F;
    localPointF11.y -= (localPointF7.y - localPointF11.y) * 7.0F;
    arrayOfFloat[20] = localPointF10.x;
    arrayOfFloat[21] = localPointF10.y;
    arrayOfFloat[22] = localPointF11.x;
    arrayOfFloat[23] = localPointF11.y;
    setFloatArray(arrayOfFloat, this.d, this.mFilterProgram);
    a(arrayOfFloat);
  }
  
  private void a(float[] paramArrayOfFloat)
  {
    if (this.h == null) {
      return;
    }
    PointF[] arrayOfPointF = { new PointF(paramArrayOfFloat[0], paramArrayOfFloat[1]), new PointF(paramArrayOfFloat[2], paramArrayOfFloat[3]), new PointF(paramArrayOfFloat[4], paramArrayOfFloat[5]), new PointF(paramArrayOfFloat[6], paramArrayOfFloat[7]), new PointF(paramArrayOfFloat[8], paramArrayOfFloat[9]), new PointF(paramArrayOfFloat[10], paramArrayOfFloat[11]), new PointF(paramArrayOfFloat[13], paramArrayOfFloat[14]), new PointF(paramArrayOfFloat[16], paramArrayOfFloat[17]), new PointF(paramArrayOfFloat[18], paramArrayOfFloat[19]), new PointF(paramArrayOfFloat[20], paramArrayOfFloat[21]), new PointF(paramArrayOfFloat[22], paramArrayOfFloat[23]) };
    TLog.d("faceLeftCicleRadius: %f, faceRightCicleRadius: %f", new Object[] { Float.valueOf(paramArrayOfFloat[16]), Float.valueOf(paramArrayOfFloat[19]) });
    FaceAligment localFaceAligment = new FaceAligment(arrayOfPointF);
    FaceAligment[] arrayOfFaceAligment = { localFaceAligment };
    this.h.updateFaceFeatures(arrayOfFaceAligment, 0.0F);
  }
  
  private PointF a(int[] paramArrayOfInt, PointF[] paramArrayOfPointF)
  {
    PointF localPointF1 = new PointF(0.0F, 0.0F);
    int j = paramArrayOfInt.length;
    for (int m = 0; m < j; m++)
    {
      int k = paramArrayOfInt[m];
      PointF localPointF2 = paramArrayOfPointF[k];
      localPointF1.x += localPointF2.x;
      localPointF1.y += localPointF2.y;
    }
    localPointF1.x /= j;
    localPointF1.y /= j;
    return localPointF1;
  }
  
  public float getScreenRatio()
  {
    return this.g;
  }
  
  public void setScreenRatio(float paramFloat)
  {
    this.g = paramFloat;
    setFloat(paramFloat, this.c, this.mFilterProgram);
  }
  
  public void setEyeEnlargeSize(float paramFloat)
  {
    this.e = paramFloat;
    float f1 = this.e == 0.0F ? 0.0F : 1.0F - 1.0F / this.e;
    setFloat(f1, this.a, this.mFilterProgram);
  }
  
  public float getEyeEnlargeSize()
  {
    return this.e;
  }
  
  public void setChinSize(float paramFloat)
  {
    this.f = paramFloat;
    setFloat(this.f * 2.0F, this.b, this.mFilterProgram);
  }
  
  public float getChinSize()
  {
    return this.f;
  }
  
  protected SelesParameters initParams(SelesParameters paramSelesParameters)
  {
    paramSelesParameters = super.initParams(paramSelesParameters);
    paramSelesParameters.appendFloatArg("eyeSize", getEyeEnlargeSize(), 1.0F, 1.36F);
    paramSelesParameters.appendFloatArg("chinSize", getChinSize(), 0.0F, 0.2F);
    return paramSelesParameters;
  }
  
  protected void submitFilterArg(SelesParameters.FilterArg paramFilterArg)
  {
    if (paramFilterArg == null) {
      return;
    }
    if (paramFilterArg.equalsKey("eyeSize")) {
      setEyeEnlargeSize(paramFilterArg.getValue());
    } else if (paramFilterArg.equalsKey("chinSize")) {
      setChinSize(paramFilterArg.getValue());
    }
  }
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKCore-3.1.0.jar!\org\lasque\tusdk\core\seles\tusdk\liveSticker\TuSDKFacePlasticFilter.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */