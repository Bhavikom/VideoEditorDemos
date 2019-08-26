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

public class TuSDKMonsterSquareFace
  extends SelesFilter
  implements SelesParameters.FilterFacePositionInterface
{
  private static final int[] l = { 0, 2, 3, 0, 3, 4, 1, 4, 5, 1, 5, 6, 2, 3, 4, 2, 4, 6, 4, 5, 6 };
  boolean a;
  FaceAligment[] b;
  List<MonsterSquareFaceInfo> c;
  FloatBuffer d;
  FloatBuffer e;
  IntBuffer f;
  float[] g = { -1.0F, -1.0F, 1.0F, -1.0F, 1.0F, 1.0F, -1.0F, 1.0F };
  float[] h = { 0.0F, 0.0F, 1.0F, 0.0F, 1.0F, 1.0F, 0.0F, 1.0F };
  int[] i = { 0, 1, 2, 1, 2, 3 };
  int j = 4;
  int k = 2;
  
  public TuSDKMonsterSquareFace()
  {
    a();
    this.c = new ArrayList();
    this.d = ByteBuffer.allocateDirect((this.j + 70) * 4 * 2).order(ByteOrder.nativeOrder()).asFloatBuffer();
    this.e = ByteBuffer.allocateDirect((this.j + 70) * 4 * 2).order(ByteOrder.nativeOrder()).asFloatBuffer();
    this.f = ByteBuffer.allocateDirect((this.k + 70) * 4 * 3).order(ByteOrder.nativeOrder()).asIntBuffer();
  }
  
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
    GLES20.glVertexAttribPointer(this.mFilterPositionAttribute, 2, 5126, false, 0, this.d);
    GLES20.glVertexAttribPointer(this.mFilterTextureCoordinateAttribute, 2, 5126, false, 0, this.e);
    checkGLError(getClass().getSimpleName() + " bindFramebuffer");
    GLES20.glDrawElements(4, this.f.limit(), 5125, this.f);
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
      for (FaceAligment localFaceAligment : arrayOfFaceAligment)
      {
        MonsterSquareFaceInfo localMonsterSquareFaceInfo = new MonsterSquareFaceInfo(localFaceAligment, null);
        if (localMonsterSquareFaceInfo != null) {
          this.c.add(localMonsterSquareFaceInfo);
        }
      }
      if (this.c.size() == 0)
      {
        this.a = false;
        return;
      }
      ??? = new float[this.g.length + 14 * this.c.size()];
      float[] arrayOfFloat1 = new float[this.h.length + 14 * this.c.size()];
      int[] arrayOfInt1 = new int[this.i.length + l.length * this.c.size()];
      int i1 = 0;
      int i2 = 0;
      int i3 = 0;
      for (int i4 = 0; i4 < this.g.length; i4++) {
        ???[(i1++)] = this.g[i4];
      }
      for (i4 = 0; i4 < this.h.length; i4++) {
        arrayOfFloat1[(i2++)] = this.h[i4];
      }
      int[] arrayOfInt2 = { 0, 1, 2, 0, 3, 2 };
      for (int i5 = 0; i5 < arrayOfInt2.length; i5++) {
        arrayOfInt1[(i3++)] = arrayOfInt2[i5];
      }
      for (i5 = 0; i5 < this.c.size(); i5++)
      {
        float[] arrayOfFloat2 = MonsterSquareFaceInfo.a((MonsterSquareFaceInfo)this.c.get(i5));
        for (int i6 = 0; i6 < arrayOfFloat2.length; i6++) {
          arrayOfFloat1[(i2++)] = arrayOfFloat2[i6];
        }
        MonsterSquareFaceInfo.b((MonsterSquareFaceInfo)this.c.get(i5));
        float[] arrayOfFloat3 = MonsterSquareFaceInfo.c((MonsterSquareFaceInfo)this.c.get(i5));
        for (int i7 = 0; i7 < arrayOfFloat3.length; i7++) {
          ???[(i1++)] = arrayOfFloat3[i7];
        }
        i7 = this.j + i5 * 7;
        for (int i8 = 0; i8 < l.length; i8++) {
          arrayOfInt1[(i3++)] = (l[i8] + i7);
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
  
  private void a() {}
  
  public void updateFaceFeatures(FaceAligment[] paramArrayOfFaceAligment, float paramFloat)
  {
    if ((paramArrayOfFaceAligment == null) || (paramArrayOfFaceAligment.length < 1))
    {
      this.a = false;
      this.b = null;
      return;
    }
    this.b = paramArrayOfFaceAligment;
    this.a = true;
  }
  
  private class MonsterSquareFaceInfo
  {
    PointF a;
    PointF[] b = new PointF[2];
    PointF[] c = new PointF[5];
    PointF d;
    PointF e;
    
    private MonsterSquareFaceInfo(FaceAligment paramFaceAligment)
    {
      if (paramFaceAligment != null) {
        a(paramFaceAligment.getOrginMarks());
      }
    }
    
    private float[] a()
    {
      float[] arrayOfFloat = new float[14];
      int i = 0;
      for (int j = 0; j < 2; j++)
      {
        arrayOfFloat[(i++)] = (this.b[j].x * 2.0F - 1.0F);
        arrayOfFloat[(i++)] = (this.b[j].y * 2.0F - 1.0F);
      }
      for (j = 0; j < 5; j++)
      {
        arrayOfFloat[(i++)] = (this.c[j].x * 2.0F - 1.0F);
        arrayOfFloat[(i++)] = (this.c[j].y * 2.0F - 1.0F);
      }
      return arrayOfFloat;
    }
    
    private float[] b()
    {
      float[] arrayOfFloat = new float[14];
      int i = 0;
      for (int j = 0; j < 2; j++)
      {
        arrayOfFloat[(i++)] = this.b[j].x;
        arrayOfFloat[(i++)] = this.b[j].y;
      }
      for (j = 0; j < 5; j++)
      {
        arrayOfFloat[(i++)] = this.c[j].x;
        arrayOfFloat[(i++)] = this.c[j].y;
      }
      return arrayOfFloat;
    }
    
    private void c()
    {
      this.c[1].x = this.d.x;
      this.c[1].y = this.d.y;
      this.c[3].x = this.e.x;
      this.c[3].y = this.e.y;
    }
    
    private void a(PointF[] paramArrayOfPointF)
    {
      if ((paramArrayOfPointF == null) || (paramArrayOfPointF.length < 3)) {
        return;
      }
      this.a = new PointF(paramArrayOfPointF[46].x, paramArrayOfPointF[46].y);
      this.c[0] = new PointF(paramArrayOfPointF[0].x, paramArrayOfPointF[0].y);
      this.c[1] = new PointF(paramArrayOfPointF[8].x, paramArrayOfPointF[8].y);
      this.c[2] = new PointF(paramArrayOfPointF[16].x, paramArrayOfPointF[16].y);
      this.c[3] = new PointF(paramArrayOfPointF[24].x, paramArrayOfPointF[24].y);
      this.c[4] = new PointF(paramArrayOfPointF[32].x, paramArrayOfPointF[32].y);
      float f1 = Math.abs(PointCalc.distance(this.c[0], this.c[4]));
      float f2 = Math.abs(PointCalc.distance(this.c[1], this.c[3]));
      float f3 = -(f1 - f2) / f2;
      PointF localPointF = PointCalc.crossPoint(paramArrayOfPointF[16], paramArrayOfPointF[43], this.c[1], this.c[3]);
      this.d = PointCalc.pointOfPercentage(this.c[1], localPointF, f3);
      this.e = PointCalc.pointOfPercentage(this.c[3], localPointF, f3);
      this.d = PointCalc.pointOfPercentage(this.d, localPointF, 0.05F);
      this.e = PointCalc.pointOfPercentage(this.e, localPointF, 0.05F);
      this.b[0] = PointCalc.pointOfPercentage(this.d, this.a, -0.2F);
      this.b[1] = PointCalc.pointOfPercentage(this.e, this.a, -0.2F);
    }
  }
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKCore-3.1.0.jar!\org\lasque\tusdk\core\seles\tusdk\liveSticker\TuSDKMonsterSquareFace.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */