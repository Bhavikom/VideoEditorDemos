package org.lasque.tusdk.core.seles.tusdk.liveSticker;

import android.graphics.PointF;
import android.opengl.GLES20;
import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.List;
import org.lasque.tusdk.core.face.FaceAligment;
import org.lasque.tusdk.core.seles.SelesContext;
import org.lasque.tusdk.core.seles.SelesFramebuffer;
import org.lasque.tusdk.core.seles.SelesFramebuffer.SelesFramebufferMode;
import org.lasque.tusdk.core.seles.SelesFramebufferCache;
import org.lasque.tusdk.core.seles.SelesParameters.FilterFacePositionInterface;
import org.lasque.tusdk.core.seles.filters.SelesFilter;
import org.lasque.tusdk.core.struct.TuSdkSize;
import org.lasque.tusdk.core.utils.calc.PointCalc;

public class TuSDKMonsterNoseFallFace
  extends SelesFilter
  implements SelesParameters.FilterFacePositionInterface
{
  private final int l = 10;
  private final int m = 12;
  private final int n = 9;
  private final int o = 4;
  private final int p = 25;
  private final int q = 37;
  private final int[] r = { 0, 1, 12, 0, 9, 12, 1, 2, 13, 1, 12, 13, 2, 3, 14, 2, 13, 14, 3, 4, 15, 3, 14, 15, 4, 5, 17, 4, 15, 16, 4, 16, 17, 5, 6, 18, 5, 17, 18, 6, 7, 19, 6, 18, 19, 7, 8, 20, 7, 19, 20, 8, 10, 20, 9, 11, 21, 9, 12, 21, 10, 11, 21, 10, 20, 21, 12, 13, 21, 9, 11, 21, 13, 21, 23, 13, 14, 23, 14, 22, 23, 14, 15, 22, 15, 16, 22, 16, 17, 22, 17, 18, 22, 18, 19, 24, 18, 22, 24, 19, 20, 21, 19, 21, 24, 21, 23, 24, 22, 23, 24 };
  boolean a;
  FaceAligment[] b;
  List<MonsterNoseFallFaceInfo> c = new ArrayList();
  FloatBuffer d = ByteBuffer.allocateDirect((this.j + 250) * 4 * 2).order(ByteOrder.nativeOrder()).asFloatBuffer();
  FloatBuffer e = ByteBuffer.allocateDirect((this.j + 250) * 4 * 2).order(ByteOrder.nativeOrder()).asFloatBuffer();
  IntBuffer f = ByteBuffer.allocateDirect((this.k + 370) * 4 * 3).order(ByteOrder.nativeOrder()).asIntBuffer();
  float[] g = { -1.0F, -1.0F, 1.0F, -1.0F, 1.0F, 1.0F, -1.0F, 1.0F };
  float[] h = { 0.0F, 0.0F, 1.0F, 0.0F, 1.0F, 1.0F, 0.0F, 1.0F };
  int[] i = { 0, 1, 2, 1, 2, 3 };
  int j = 4;
  int k = 2;
  
  protected void onInitOnGLThread()
  {
    super.onInitOnGLThread();
    checkGLError(getClass().getSimpleName() + " onInitOnGLThread");
  }
  
  protected void renderToTexture(FloatBuffer paramFloatBuffer1, FloatBuffer paramFloatBuffer2)
  {
    runPendingOnDrawTasks();
    if (isPreventRendering())
    {
      inputFramebufferUnlock();
      return;
    }
    a(paramFloatBuffer1, paramFloatBuffer2);
    SelesContext.setActiveShaderProgram(this.mFilterProgram);
    TuSdkSize localTuSdkSize = sizeOfFBO();
    SelesFramebufferCache localSelesFramebufferCache = SelesContext.sharedFramebufferCache();
    if (localSelesFramebufferCache == null) {
      return;
    }
    this.mOutputFramebuffer = localSelesFramebufferCache.fetchFramebuffer(SelesFramebuffer.SelesFramebufferMode.FBO_AND_TEXTURE, localTuSdkSize, getOutputTextureOptions());
    this.mOutputFramebuffer.activateFramebuffer();
    checkGLError(getClass().getSimpleName() + " activateFramebuffer");
    if (this.mUsingNextFrameForImageCapture) {
      this.mOutputFramebuffer.lock();
    }
    setUniformsForProgramAtIndex(0);
    GLES20.glClearColor(this.mBackgroundColorRed, this.mBackgroundColorGreen, this.mBackgroundColorBlue, this.mBackgroundColorAlpha);
    GLES20.glClear(16384);
    inputFramebufferBindTexture();
    checkGLError(getClass().getSimpleName() + " bindFramebuffer");
    GLES20.glVertexAttribPointer(this.mFilterPositionAttribute, 2, 5126, false, 0, this.d);
    GLES20.glVertexAttribPointer(this.mFilterTextureCoordinateAttribute, 2, 5126, false, 0, this.e);
    GLES20.glDrawElements(4, this.f.limit(), 5125, this.f);
    captureFilterImage(getClass().getSimpleName(), this.mInputTextureSize.width, this.mInputTextureSize.height);
    captureFilterImage(getClass().getSimpleName(), this.mInputTextureSize.width, this.mInputTextureSize.height);
    inputFramebufferUnlock();
    cacaptureImageBuffer();
  }
  
  private void a(FloatBuffer paramFloatBuffer1, FloatBuffer paramFloatBuffer2)
  {
    boolean bool = this.a;
    FaceAligment[] arrayOfFaceAligment = this.b;
    this.d.clear();
    this.e.clear();
    this.f.clear();
    if ((bool) && (arrayOfFaceAligment != null))
    {
      this.c.clear();
      for (FaceAligment localFaceAligment : this.b)
      {
        MonsterNoseFallFaceInfo localMonsterNoseFallFaceInfo = new MonsterNoseFallFaceInfo(localFaceAligment, null);
        if (localMonsterNoseFallFaceInfo != null) {
          this.c.add(localMonsterNoseFallFaceInfo);
        }
      }
      if (this.c.size() == 0)
      {
        this.a = false;
        return;
      }
      ??? = new float[this.g.length + 50 * this.c.size()];
      float[] arrayOfFloat1 = new float[this.h.length + 50 * this.c.size()];
      int[] arrayOfInt1 = new int[this.i.length + this.r.length * this.c.size()];
      int i3 = 0;
      int i4 = 0;
      int i5 = 0;
      for (int i6 = 0; i6 < this.g.length; i6++) {
        ???[(i3++)] = this.g[i6];
      }
      for (i6 = 0; i6 < this.h.length; i6++) {
        arrayOfFloat1[(i4++)] = this.h[i6];
      }
      int[] arrayOfInt2 = { 0, 1, 2, 0, 3, 2 };
      for (int i7 = 0; i7 < arrayOfInt2.length; i7++) {
        arrayOfInt1[(i5++)] = arrayOfInt2[i7];
      }
      for (i7 = 0; i7 < this.c.size(); i7++)
      {
        float[] arrayOfFloat2 = MonsterNoseFallFaceInfo.a((MonsterNoseFallFaceInfo)this.c.get(i7));
        for (int i8 = 0; i8 < arrayOfFloat2.length; i8++) {
          arrayOfFloat1[(i4++)] = arrayOfFloat2[i8];
        }
        MonsterNoseFallFaceInfo.b((MonsterNoseFallFaceInfo)this.c.get(i7));
        float[] arrayOfFloat3 = MonsterNoseFallFaceInfo.c((MonsterNoseFallFaceInfo)this.c.get(i7));
        for (int i9 = 0; i9 < arrayOfFloat3.length; i9++) {
          ???[(i3++)] = arrayOfFloat3[i9];
        }
        i9 = this.j + i7 * 25;
        for (int i10 = 0; i10 < this.r.length; i10++) {
          arrayOfInt1[(i5++)] = (this.r[i10] + i9);
        }
      }
      this.d.put((float[])???).position(0).limit(???.length);
      this.e.put(arrayOfFloat1).position(0).limit(arrayOfFloat1.length);
      this.f.put(arrayOfInt1).position(0).limit(arrayOfInt1.length);
      return;
    }
    paramFloatBuffer1.position(0);
    this.d.put(paramFloatBuffer1).position(0);
    paramFloatBuffer2.position(0);
    this.e.put(paramFloatBuffer2).position(0);
    this.f.put(this.i).position(0).limit(this.i.length);
  }
  
  public void updateFaceFeatures(FaceAligment[] paramArrayOfFaceAligment, float paramFloat)
  {
    if ((paramArrayOfFaceAligment == null) || (paramArrayOfFaceAligment.length < 1))
    {
      this.a = false;
      this.b = null;
      this.c.clear();
      return;
    }
    this.b = paramArrayOfFaceAligment;
    this.a = true;
  }
  
  private class MonsterNoseFallFaceInfo
  {
    PointF a;
    PointF[] b = new PointF[12];
    PointF[] c = new PointF[9];
    PointF[] d = new PointF[4];
    PointF e;
    
    private MonsterNoseFallFaceInfo(FaceAligment paramFaceAligment)
    {
      if (paramFaceAligment != null) {
        a(paramFaceAligment.getOrginMarks());
      }
    }
    
    private void a(PointF[] paramArrayOfPointF)
    {
      if ((paramArrayOfPointF == null) || (paramArrayOfPointF.length < 3)) {
        return;
      }
      this.a = new PointF(paramArrayOfPointF[46].x, paramArrayOfPointF[46].y);
      int[] arrayOfInt1 = { 0, 4, 8, 12, 16, 20, 24, 28, 32 };
      for (int i = 0; i < 9; i++) {
        this.c[i] = new PointF(paramArrayOfPointF[arrayOfInt1[i]].x, paramArrayOfPointF[arrayOfInt1[i]].y);
      }
      for (i = 0; i < 9; i++) {
        this.b[i] = new PointF(paramArrayOfPointF[arrayOfInt1[i]].x, paramArrayOfPointF[arrayOfInt1[i]].y);
      }
      this.b[9] = new PointF(paramArrayOfPointF[34].x, paramArrayOfPointF[34].y);
      this.b[10] = new PointF(paramArrayOfPointF[41].x, paramArrayOfPointF[41].y);
      this.b[11] = PointCalc.center(paramArrayOfPointF[35], paramArrayOfPointF[40]);
      PointCalc.scalePoint(this.b, this.a, 0.5F);
      int[] arrayOfInt2 = { 45, 49, 82, 83 };
      for (int j = 0; j < 4; j++) {
        this.d[j] = new PointF(paramArrayOfPointF[arrayOfInt2[j]].x, paramArrayOfPointF[arrayOfInt2[j]].y);
      }
      this.e = PointCalc.crossPoint(this.d[0], this.d[1], this.d[2], this.d[3]);
    }
    
    private float[] a()
    {
      float[] arrayOfFloat = new float[50];
      int i = 0;
      for (int j = 0; j < 12; j++)
      {
        arrayOfFloat[(i++)] = (this.b[j].x * 2.0F - 1.0F);
        arrayOfFloat[(i++)] = (this.b[j].y * 2.0F - 1.0F);
      }
      for (j = 0; j < 9; j++)
      {
        arrayOfFloat[(i++)] = (this.c[j].x * 2.0F - 1.0F);
        arrayOfFloat[(i++)] = (this.c[j].y * 2.0F - 1.0F);
      }
      for (j = 0; j < 4; j++)
      {
        arrayOfFloat[(i++)] = (this.d[j].x * 2.0F - 1.0F);
        arrayOfFloat[(i++)] = (this.d[j].y * 2.0F - 1.0F);
      }
      return arrayOfFloat;
    }
    
    private float[] b()
    {
      float[] arrayOfFloat = new float[50];
      int i = 0;
      for (int j = 0; j < 12; j++)
      {
        arrayOfFloat[(i++)] = this.b[j].x;
        arrayOfFloat[(i++)] = this.b[j].y;
      }
      for (j = 0; j < 9; j++)
      {
        arrayOfFloat[(i++)] = this.c[j].x;
        arrayOfFloat[(i++)] = this.c[j].y;
      }
      for (j = 0; j < 4; j++)
      {
        arrayOfFloat[(i++)] = this.d[j].x;
        arrayOfFloat[(i++)] = this.d[j].y;
      }
      return arrayOfFloat;
    }
    
    private void c()
    {
      this.d[0] = PointCalc.pointOfPercentage(this.d[0], this.e, -0.5F);
      this.d[1] = PointCalc.pointOfPercentage(this.d[1], this.d[0], -0.5F);
      this.d[2] = PointCalc.pointOfPercentage(this.d[2], this.d[0], -0.5F);
      this.d[3] = PointCalc.pointOfPercentage(this.d[3], this.d[0], -0.5F);
      Float localFloat = Float.valueOf(-0.2F);
      this.c[0] = PointCalc.pointOfPercentage(this.c[0], this.a, localFloat.floatValue() * 0.25F);
      this.c[1] = PointCalc.pointOfPercentage(this.c[1], this.a, localFloat.floatValue() * 0.75F);
      this.c[2] = PointCalc.pointOfPercentage(this.c[2], this.a, localFloat.floatValue() * 1.0F);
      this.c[3] = PointCalc.pointOfPercentage(this.c[3], this.a, localFloat.floatValue() * 0.75F);
      this.c[4] = PointCalc.pointOfPercentage(this.c[4], this.a, localFloat.floatValue() * 0.4F);
      this.c[5] = PointCalc.pointOfPercentage(this.c[5], this.a, localFloat.floatValue() * 0.75F);
      this.c[6] = PointCalc.pointOfPercentage(this.c[6], this.a, localFloat.floatValue() * 1.0F);
      this.c[7] = PointCalc.pointOfPercentage(this.c[7], this.a, localFloat.floatValue() * 0.75F);
      this.c[8] = PointCalc.pointOfPercentage(this.c[8], this.a, localFloat.floatValue() * 0.25F);
    }
  }
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKCore-3.1.0.jar!\org\lasque\tusdk\core\seles\tusdk\liveSticker\TuSDKMonsterNoseFallFace.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */