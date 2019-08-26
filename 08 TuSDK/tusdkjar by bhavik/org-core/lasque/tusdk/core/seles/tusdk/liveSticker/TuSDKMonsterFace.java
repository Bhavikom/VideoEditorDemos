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

public class TuSDKMonsterFace
  extends SelesFilter
  implements SelesParameters.FilterFacePositionInterface
{
  public static final int TuSDKMonsterFaceThickLipsType = 1;
  public static final int TuSDKMonsterPapayaFaceType = 2;
  public static final int TuSDKMonsterSmallEyesType = 3;
  private static int[] l = { 0, 1, 12, 0, 9, 12, 1, 2, 13, 1, 12, 13, 2, 3, 14, 2, 13, 14, 3, 4, 16, 3, 14, 15, 3, 15, 16, 4, 5, 16, 5, 6, 18, 5, 16, 17, 5, 17, 18, 6, 7, 19, 6, 18, 19, 7, 8, 20, 7, 19, 20, 8, 10, 20, 9, 11, 24, 9, 12, 21, 9, 21, 22, 9, 22, 24, 10, 11, 31, 10, 20, 28, 10, 28, 29, 10, 29, 31, 11, 24, 26, 11, 26, 35, 11, 31, 33, 11, 33, 35, 12, 13, 38, 12, 21, 36, 12, 36, 38, 13, 14, 47, 13, 37, 38, 13, 37, 45, 13, 45, 47, 14, 15, 47, 15, 16, 55, 15, 47, 55, 16, 17, 57, 16, 55, 56, 16, 56, 57, 17, 18, 48, 17, 48, 57, 18, 19, 48, 19, 46, 48, 19, 20, 43, 19, 41, 43, 19, 41, 46, 20, 28, 40, 20, 40, 43, 21, 22, 23, 21, 23, 36, 22, 23, 25, 22, 24, 25, 23, 25, 36, 24, 25, 26, 25, 26, 27, 25, 27, 36, 26, 27, 35, 27, 35, 36, 28, 29, 30, 28, 30, 40, 29, 30, 32, 29, 31, 32, 30, 32, 40, 31, 32, 33, 32, 33, 34, 32, 34, 40, 33, 34, 35, 34, 35, 40, 35, 36, 39, 35, 39, 45, 35, 40, 42, 35, 42, 46, 35, 44, 45, 35, 44, 46, 36, 37, 38, 36, 37, 39, 37, 39, 45, 40, 41, 42, 40, 41, 43, 41, 42, 46, 44, 45, 49, 44, 46, 51, 44, 49, 50, 44, 50, 51, 45, 47, 49, 46, 48, 51, 47, 49, 52, 47, 52, 58, 47, 55, 58, 48, 51, 54, 48, 54, 60, 48, 57, 60, 49, 50, 52, 50, 51, 54, 50, 52, 53, 50, 53, 54, 52, 53, 58, 53, 54, 60, 53, 58, 59, 53, 59, 60, 55, 56, 58, 56, 57, 60, 56, 58, 59, 56, 59, 60 };
  boolean a;
  FaceAligment[] b;
  List<MonsterFaceInfo> c = new ArrayList();
  FloatBuffer d = ByteBuffer.allocateDirect((this.j + 610) * 4 * 2).order(ByteOrder.nativeOrder()).asFloatBuffer();
  FloatBuffer e = ByteBuffer.allocateDirect((this.j + 610) * 4 * 2).order(ByteOrder.nativeOrder()).asFloatBuffer();
  IntBuffer f = ByteBuffer.allocateDirect((this.k + 1080) * 4 * 3).order(ByteOrder.nativeOrder()).asIntBuffer();
  private int m = 1;
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
  
  public void setMonsterFaceType(int paramInt)
  {
    this.m = paramInt;
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
        MonsterFaceInfo localMonsterFaceInfo1 = new MonsterFaceInfo(localFaceAligment, null);
        if (localMonsterFaceInfo1 != null) {
          this.c.add(localMonsterFaceInfo1);
        }
      }
      if (this.c.size() == 0)
      {
        this.a = false;
        return;
      }
      ??? = new float[this.g.length + 122 * this.c.size()];
      float[] arrayOfFloat1 = new float[this.h.length + 122 * this.c.size()];
      int[] arrayOfInt1 = new int[this.i.length + l.length * this.c.size()];
      int i2 = 0;
      int i3 = 0;
      int i4 = 0;
      for (int i5 = 0; i5 < this.g.length; i5++) {
        ???[(i2++)] = this.g[i5];
      }
      for (i5 = 0; i5 < this.h.length; i5++) {
        arrayOfFloat1[(i3++)] = this.h[i5];
      }
      int[] arrayOfInt2 = { 0, 1, 2, 0, 3, 2 };
      for (int i6 = 0; i6 < arrayOfInt2.length; i6++) {
        arrayOfInt1[(i4++)] = arrayOfInt2[i6];
      }
      for (i6 = 0; i6 < this.c.size(); i6++)
      {
        MonsterFaceInfo localMonsterFaceInfo2 = (MonsterFaceInfo)this.c.get(i6);
        float[] arrayOfFloat2 = MonsterFaceInfo.a(localMonsterFaceInfo2, false);
        for (int i7 = 0; i7 < arrayOfFloat2.length; i7++) {
          arrayOfFloat1[(i3++)] = arrayOfFloat2[i7];
        }
        switch (this.m)
        {
        case 2: 
          MonsterFaceInfo.a(localMonsterFaceInfo2);
          break;
        case 3: 
          MonsterFaceInfo.b(localMonsterFaceInfo2);
          break;
        default: 
          MonsterFaceInfo.c(localMonsterFaceInfo2);
        }
        MonsterFaceInfo.b(localMonsterFaceInfo2);
        float[] arrayOfFloat3 = MonsterFaceInfo.a(localMonsterFaceInfo2, true);
        for (int i8 = 0; i8 < arrayOfFloat3.length; i8++) {
          ???[(i2++)] = arrayOfFloat3[i8];
        }
        i8 = this.j + i6 * 61;
        for (int i9 = 0; i9 < l.length; i9++) {
          arrayOfInt1[(i4++)] = (l[i9] + i8);
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
      return;
    }
    this.b = paramArrayOfFaceAligment;
    this.a = true;
  }
  
  private class MonsterFaceInfo
  {
    PointF a;
    PointF b;
    PointF c;
    PointF d;
    PointF e;
    PointF f;
    PointF g;
    PointF h;
    PointF[] i = new PointF[12];
    PointF[] j = new PointF[9];
    PointF[] k = new PointF[7];
    PointF[] l = new PointF[7];
    PointF[] m = new PointF[4];
    PointF[] n = new PointF[4];
    PointF[] o = new PointF[3];
    PointF[] p = new PointF[14];
    
    private MonsterFaceInfo(FaceAligment paramFaceAligment)
    {
      if (paramFaceAligment != null) {
        a(paramFaceAligment.getOrginMarks());
      }
    }
    
    private float[] a(boolean paramBoolean)
    {
      float[] arrayOfFloat = new float[122];
      int i1 = 0;
      for (int i2 = 0; i2 < 12; i2++) {
        if (paramBoolean)
        {
          arrayOfFloat[(i1++)] = (this.i[i2].x * 2.0F - 1.0F);
          arrayOfFloat[(i1++)] = (this.i[i2].y * 2.0F - 1.0F);
        }
        else
        {
          arrayOfFloat[(i1++)] = this.i[i2].x;
          arrayOfFloat[(i1++)] = this.i[i2].y;
        }
      }
      for (i2 = 0; i2 < 9; i2++) {
        if (paramBoolean)
        {
          arrayOfFloat[(i1++)] = (this.j[i2].x * 2.0F - 1.0F);
          arrayOfFloat[(i1++)] = (this.j[i2].y * 2.0F - 1.0F);
        }
        else
        {
          arrayOfFloat[(i1++)] = this.j[i2].x;
          arrayOfFloat[(i1++)] = this.j[i2].y;
        }
      }
      for (i2 = 0; i2 < 7; i2++) {
        if (paramBoolean)
        {
          arrayOfFloat[(i1++)] = (this.k[i2].x * 2.0F - 1.0F);
          arrayOfFloat[(i1++)] = (this.k[i2].y * 2.0F - 1.0F);
        }
        else
        {
          arrayOfFloat[(i1++)] = this.k[i2].x;
          arrayOfFloat[(i1++)] = this.k[i2].y;
        }
      }
      for (i2 = 0; i2 < 7; i2++) {
        if (paramBoolean)
        {
          arrayOfFloat[(i1++)] = (this.l[i2].x * 2.0F - 1.0F);
          arrayOfFloat[(i1++)] = (this.l[i2].y * 2.0F - 1.0F);
        }
        else
        {
          arrayOfFloat[(i1++)] = this.l[i2].x;
          arrayOfFloat[(i1++)] = this.l[i2].y;
        }
      }
      if (paramBoolean)
      {
        arrayOfFloat[(i1++)] = (this.b.x * 2.0F - 1.0F);
        arrayOfFloat[(i1++)] = (this.b.y * 2.0F - 1.0F);
      }
      else
      {
        arrayOfFloat[(i1++)] = this.b.x;
        arrayOfFloat[(i1++)] = this.b.y;
      }
      for (i2 = 0; i2 < 4; i2++) {
        if (paramBoolean)
        {
          arrayOfFloat[(i1++)] = (this.m[i2].x * 2.0F - 1.0F);
          arrayOfFloat[(i1++)] = (this.m[i2].y * 2.0F - 1.0F);
        }
        else
        {
          arrayOfFloat[(i1++)] = this.m[i2].x;
          arrayOfFloat[(i1++)] = this.m[i2].y;
        }
      }
      for (i2 = 0; i2 < 4; i2++) {
        if (paramBoolean)
        {
          arrayOfFloat[(i1++)] = (this.n[i2].x * 2.0F - 1.0F);
          arrayOfFloat[(i1++)] = (this.n[i2].y * 2.0F - 1.0F);
        }
        else
        {
          arrayOfFloat[(i1++)] = this.n[i2].x;
          arrayOfFloat[(i1++)] = this.n[i2].y;
        }
      }
      for (i2 = 0; i2 < 3; i2++) {
        if (paramBoolean)
        {
          arrayOfFloat[(i1++)] = (this.o[i2].x * 2.0F - 1.0F);
          arrayOfFloat[(i1++)] = (this.o[i2].y * 2.0F - 1.0F);
        }
        else
        {
          arrayOfFloat[(i1++)] = this.o[i2].x;
          arrayOfFloat[(i1++)] = this.o[i2].y;
        }
      }
      for (i2 = 0; i2 < 14; i2++) {
        if (paramBoolean)
        {
          arrayOfFloat[(i1++)] = (this.p[i2].x * 2.0F - 1.0F);
          arrayOfFloat[(i1++)] = (this.p[i2].y * 2.0F - 1.0F);
        }
        else
        {
          arrayOfFloat[(i1++)] = this.p[i2].x;
          arrayOfFloat[(i1++)] = this.p[i2].y;
        }
      }
      return arrayOfFloat;
    }
    
    private void a()
    {
      float f1 = 0.55F;
      this.m[0] = PointCalc.increasePercentage(this.c, this.m[0], -f1);
      this.m[1] = PointCalc.increasePercentage(this.c, this.m[1], -f1);
      this.n[0] = PointCalc.increasePercentage(this.d, this.n[0], -f1);
      this.n[1] = PointCalc.increasePercentage(this.d, this.n[1], -f1);
    }
    
    private void b()
    {
      float f1 = 0.55F;
      this.m[0] = PointCalc.increasePercentage(this.c, this.m[0], -f1);
      this.m[1] = PointCalc.increasePercentage(this.c, this.m[1], -f1);
      this.n[0] = PointCalc.increasePercentage(this.d, this.n[0], -f1);
      this.n[1] = PointCalc.increasePercentage(this.d, this.n[1], -f1);
      this.p[0] = PointCalc.increasePercentage(this.h, this.p[0], f1);
      this.p[1] = PointCalc.increasePercentage(this.h, this.p[1], f1);
      this.p[2] = PointCalc.increasePercentage(this.h, this.p[2], f1 * 1.3F);
      this.p[3] = PointCalc.increasePercentage(this.h, this.p[3], f1 * 1.3F);
      this.p[4] = PointCalc.increasePercentage(this.h, this.p[4], f1 * 1.3F);
      this.p[5] = PointCalc.increasePercentage(this.h, this.p[5], f1 * 1.3F);
      this.p[6] = PointCalc.increasePercentage(this.h, this.p[6], f1 * 1.3F);
      this.p[7] = PointCalc.increasePercentage(this.h, this.p[7], f1 * 1.3F);
      this.p[8] = PointCalc.increasePercentage(this.h, this.p[8], f1 * 1.3F);
      this.p[9] = PointCalc.increasePercentage(this.h, this.p[9], f1 * 1.3F);
      this.p[10] = PointCalc.increasePercentage(this.h, this.p[10], f1 * 1.3F);
      this.p[11] = PointCalc.increasePercentage(this.h, this.p[11], f1 * 1.3F);
      this.p[12] = PointCalc.increasePercentage(this.h, this.p[12], f1 * 1.3F);
      this.p[13] = PointCalc.increasePercentage(this.h, this.p[13], f1 * 1.3F);
      this.o[0] = PointCalc.increasePercentage(this.b, this.o[0], -f1 * 0.2F);
      this.o[1] = PointCalc.increasePercentage(this.j[1], this.o[1], -f1 * 0.2F);
      this.o[2] = PointCalc.increasePercentage(this.j[7], this.o[2], -f1 * 0.2F);
      f1 *= 0.3F;
      this.j[0] = PointCalc.increasePercentage(this.a, this.j[0], f1 * 0.1F);
      this.j[1] = PointCalc.increasePercentage(this.a, this.j[1], f1 * 0.5F);
      this.j[2] = PointCalc.increasePercentage(this.a, this.j[2], f1 * 1.0F);
      this.j[3] = PointCalc.increasePercentage(this.a, this.j[3], f1 * 1.0F);
      this.j[4] = PointCalc.increasePercentage(this.a, this.j[4], f1 * 1.0F);
      this.j[5] = PointCalc.increasePercentage(this.a, this.j[5], f1 * 1.0F);
      this.j[6] = PointCalc.increasePercentage(this.a, this.j[6], f1 * 1.0F);
      this.j[7] = PointCalc.increasePercentage(this.a, this.j[7], f1 * 0.5F);
      this.j[8] = PointCalc.increasePercentage(this.a, this.j[8], f1 * 0.1F);
    }
    
    private void c()
    {
      float f1 = 0.29F;
      this.m[0] = PointCalc.increasePercentage(this.c, this.m[0], f1 * 1.5F);
      this.m[1] = PointCalc.increasePercentage(this.c, this.m[1], f1 * 1.5F);
      this.m[2] = PointCalc.increasePercentage(this.c, this.m[2], f1 * 0.5F);
      this.m[3] = PointCalc.increasePercentage(this.c, this.m[3], f1 * 0.5F);
      this.n[0] = PointCalc.increasePercentage(this.d, this.n[0], f1 * 1.5F);
      this.n[1] = PointCalc.increasePercentage(this.d, this.n[1], f1 * 1.5F);
      this.n[2] = PointCalc.increasePercentage(this.d, this.n[2], f1 * 0.5F);
      this.n[3] = PointCalc.increasePercentage(this.d, this.n[3], f1 * 0.5F);
      this.p[0] = PointCalc.increasePercentage(this.a, this.p[0], f1);
      this.p[1] = PointCalc.increasePercentage(this.a, this.p[1], f1);
      this.p[2] = PointCalc.increasePercentage(this.a, this.p[2], f1);
      this.p[3] = PointCalc.increasePercentage(this.a, this.p[3], f1);
      this.p[4] = PointCalc.increasePercentage(this.a, this.p[4], f1);
      this.p[5] = PointCalc.increasePercentage(this.a, this.p[5], f1);
      this.p[6] = PointCalc.increasePercentage(this.a, this.p[6], f1);
      this.p[7] = PointCalc.increasePercentage(this.a, this.p[7], f1);
      this.p[8] = PointCalc.increasePercentage(this.a, this.p[8], f1);
      this.p[9] = PointCalc.increasePercentage(this.a, this.p[9], f1);
      this.p[10] = PointCalc.increasePercentage(this.a, this.p[10], f1);
      this.p[11] = PointCalc.increasePercentage(this.a, this.p[11], f1);
      this.p[12] = PointCalc.increasePercentage(this.a, this.p[12], f1);
      this.p[13] = PointCalc.increasePercentage(this.a, this.p[13], f1);
      this.o[0] = PointCalc.increasePercentage(this.a, this.o[0], f1 * 1.5F);
      this.o[1] = PointCalc.increasePercentage(this.a, this.o[1], f1 * 1.5F);
      this.o[2] = PointCalc.increasePercentage(this.a, this.o[2], f1 * 1.5F);
    }
    
    private void a(PointF[] paramArrayOfPointF)
    {
      if ((paramArrayOfPointF == null) || (paramArrayOfPointF.length < 3)) {
        return;
      }
      this.a = new PointF(paramArrayOfPointF[46].x, paramArrayOfPointF[46].y);
      int[] arrayOfInt1 = { 72, 73, 52, 55 };
      int[] arrayOfInt2 = { 75, 76, 58, 61 };
      this.c = PointCalc.crossPoint(paramArrayOfPointF[52], paramArrayOfPointF[55], paramArrayOfPointF[72], paramArrayOfPointF[73]);
      this.d = PointCalc.crossPoint(paramArrayOfPointF[58], paramArrayOfPointF[61], paramArrayOfPointF[75], paramArrayOfPointF[76]);
      for (int i1 = 0; i1 < 4; i1++) {
        this.m[i1] = new PointF(paramArrayOfPointF[arrayOfInt1[i1]].x, paramArrayOfPointF[arrayOfInt1[i1]].y);
      }
      for (i1 = 0; i1 < 4; i1++) {
        this.n[i1] = new PointF(paramArrayOfPointF[arrayOfInt2[i1]].x, paramArrayOfPointF[arrayOfInt2[i1]].y);
      }
      int[] arrayOfInt3 = { 49, 82, 83 };
      this.g = new PointF(paramArrayOfPointF[45].x, paramArrayOfPointF[45].y);
      for (int i2 = 0; i2 < 3; i2++) {
        this.o[i2] = new PointF(paramArrayOfPointF[arrayOfInt3[i2]].x, paramArrayOfPointF[arrayOfInt3[i2]].y);
      }
      int[] arrayOfInt4 = { 84, 90, 86, 87, 88, 97, 98, 99, 94, 93, 92, 103, 102, 101 };
      this.h = PointCalc.crossPoint(paramArrayOfPointF[84], paramArrayOfPointF[90], paramArrayOfPointF[87], paramArrayOfPointF[93]);
      for (int i3 = 0; i3 < 14; i3++) {
        this.p[i3] = new PointF(paramArrayOfPointF[arrayOfInt4[i3]].x, paramArrayOfPointF[arrayOfInt4[i3]].y);
      }
      int[] arrayOfInt5 = { 33, 34, 64, 35, 65, 37, 67 };
      int[] arrayOfInt6 = { 42, 41, 71, 40, 70, 38, 68 };
      this.b = PointCalc.crossPoint(paramArrayOfPointF[37], paramArrayOfPointF[79], paramArrayOfPointF[38], paramArrayOfPointF[78]);
      this.e = PointCalc.crossPoint(paramArrayOfPointF[33], paramArrayOfPointF[37], paramArrayOfPointF[35], paramArrayOfPointF[65]);
      this.f = PointCalc.crossPoint(paramArrayOfPointF[38], paramArrayOfPointF[42], paramArrayOfPointF[40], paramArrayOfPointF[70]);
      for (int i4 = 0; i4 < 7; i4++) {
        this.k[i4] = new PointF(paramArrayOfPointF[arrayOfInt5[i4]].x, paramArrayOfPointF[arrayOfInt5[i4]].y);
      }
      for (i4 = 0; i4 < 7; i4++) {
        this.l[i4] = new PointF(paramArrayOfPointF[arrayOfInt6[i4]].x, paramArrayOfPointF[arrayOfInt6[i4]].y);
      }
      int[] arrayOfInt7 = { 0, 4, 8, 12, 16, 20, 24, 28, 32 };
      for (int i5 = 0; i5 < 9; i5++) {
        this.j[i5] = new PointF(paramArrayOfPointF[arrayOfInt7[i5]].x, paramArrayOfPointF[arrayOfInt7[i5]].y);
      }
      this.j[3] = PointCalc.increasePercentage(this.a, this.j[3], 0.05F);
      this.j[4] = PointCalc.increasePercentage(this.a, this.j[4], 0.1F);
      this.j[5] = PointCalc.increasePercentage(this.a, this.j[5], 0.05F);
      for (i5 = 0; i5 < 9; i5++) {
        this.i[i5] = new PointF(paramArrayOfPointF[arrayOfInt7[i5]].x, paramArrayOfPointF[arrayOfInt7[i5]].y);
      }
      this.i[9] = paramArrayOfPointF[34];
      this.i[10] = paramArrayOfPointF[41];
      this.i[11] = PointCalc.center(paramArrayOfPointF[35], paramArrayOfPointF[40]);
      PointCalc.scalePoint(this.i, this.a, 0.4F);
    }
  }
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKCore-3.1.0.jar!\org\lasque\tusdk\core\seles\tusdk\liveSticker\TuSDKMonsterFace.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */