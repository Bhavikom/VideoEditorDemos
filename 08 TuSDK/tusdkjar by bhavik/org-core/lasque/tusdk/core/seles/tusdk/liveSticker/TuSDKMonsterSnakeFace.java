package org.lasque.tusdk.core.seles.tusdk.liveSticker;

import android.graphics.PointF;
import android.opengl.GLES20;
import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.lasque.tusdk.core.face.FaceAligment;
import org.lasque.tusdk.core.seles.SelesContext;
import org.lasque.tusdk.core.seles.SelesFramebuffer;
import org.lasque.tusdk.core.seles.SelesFramebuffer.SelesFramebufferMode;
import org.lasque.tusdk.core.seles.SelesFramebufferCache;
import org.lasque.tusdk.core.seles.SelesParameters.FilterFacePositionInterface;
import org.lasque.tusdk.core.seles.filters.SelesFilter;
import org.lasque.tusdk.core.seles.filters.SelesPointDrawFilter;
import org.lasque.tusdk.core.struct.TuSdkSize;
import org.lasque.tusdk.core.utils.calc.PointCalc;

public class TuSDKMonsterSnakeFace
  extends SelesFilter
  implements SelesParameters.FilterFacePositionInterface
{
  public static final int TuSDKMonsterSnakeFaceType = 1;
  public static final int TuSDKMonsterBigFaceType = 2;
  private static final int[] g = { 0, 1, 12, 0, 9, 12, 1, 2, 13, 1, 12, 13, 2, 3, 14, 2, 13, 14, 3, 4, 15, 3, 14, 15, 4, 5, 17, 4, 15, 16, 4, 16, 17, 5, 6, 18, 5, 17, 18, 6, 7, 19, 6, 18, 19, 7, 8, 20, 7, 19, 20, 8, 10, 20, 9, 11, 21, 9, 12, 21, 10, 11, 21, 10, 20, 21, 12, 13, 21, 13, 14, 21, 14, 15, 21, 15, 16, 21, 16, 17, 21, 17, 18, 21, 18, 19, 21, 19, 20, 21 };
  boolean a;
  FaceAligment[] b;
  List<MonsterSnakeFaceInfo> c;
  FloatBuffer d;
  FloatBuffer e;
  IntBuffer f;
  private int h = 4;
  private int i = 2;
  private float[] j = { -1.0F, -1.0F, 1.0F, -1.0F, 1.0F, 1.0F, -1.0F, 1.0F };
  private float[] k = { 0.0F, 0.0F, 1.0F, 0.0F, 1.0F, 1.0F, 0.0F, 1.0F };
  private int[] l = { 0, 1, 2, 1, 2, 3 };
  private int m = 2;
  private SelesPointDrawFilter n;
  private SelesPointDrawFilter o;
  private boolean p = false;
  
  public TuSDKMonsterSnakeFace()
  {
    a();
    this.c = new ArrayList();
    this.d = ByteBuffer.allocateDirect((this.h + 220) * 4 * 2).order(ByteOrder.nativeOrder()).asFloatBuffer();
    this.e = ByteBuffer.allocateDirect((this.h + 220) * 4 * 2).order(ByteOrder.nativeOrder()).asFloatBuffer();
    this.f = ByteBuffer.allocateDirect((this.i + 300) * 4 * 3).order(ByteOrder.nativeOrder()).asIntBuffer();
    if (this.p)
    {
      this.n = new SelesPointDrawFilter();
      this.o = new SelesPointDrawFilter();
      addTarget(this.n, 0);
      addTarget(this.o, 0);
    }
  }
  
  public void removeAllTargets()
  {
    super.removeAllTargets();
    if (this.p)
    {
      if (this.n != null) {
        addTarget(this.n, 0);
      }
      if (this.o != null) {
        addTarget(this.o, 0);
      }
    }
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
    checkGLError(getClass().getSimpleName() + " bindFramebuffer");
    GLES20.glVertexAttribPointer(this.mFilterPositionAttribute, 2, 5126, false, 0, this.d);
    GLES20.glVertexAttribPointer(this.mFilterTextureCoordinateAttribute, 2, 5126, false, 0, this.e);
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
        MonsterSnakeFaceInfo localMonsterSnakeFaceInfo = new MonsterSnakeFaceInfo(localFaceAligment, null);
        if (localMonsterSnakeFaceInfo != null) {
          this.c.add(localMonsterSnakeFaceInfo);
        }
      }
      if (this.c.size() == 0)
      {
        this.a = false;
        return;
      }
      ??? = new float[this.j.length + 44 * this.c.size()];
      float[] arrayOfFloat1 = new float[this.k.length + 44 * this.c.size()];
      int[] arrayOfInt1 = new int[this.l.length + g.length * this.c.size()];
      int i3 = 0;
      int i4 = 0;
      int i5 = 0;
      for (int i6 = 0; i6 < this.j.length; i6++) {
        ???[(i3++)] = this.j[i6];
      }
      for (i6 = 0; i6 < this.k.length; i6++) {
        arrayOfFloat1[(i4++)] = this.k[i6];
      }
      int[] arrayOfInt2 = { 0, 1, 2, 0, 3, 2 };
      for (int i7 = 0; i7 < arrayOfInt2.length; i7++) {
        arrayOfInt1[(i5++)] = arrayOfInt2[i7];
      }
      for (i7 = 0; i7 < this.c.size(); i7++)
      {
        float[] arrayOfFloat2 = MonsterSnakeFaceInfo.a((MonsterSnakeFaceInfo)this.c.get(i7));
        for (int i8 = 0; i8 < arrayOfFloat2.length; i8++) {
          arrayOfFloat1[(i4++)] = arrayOfFloat2[i8];
        }
        float[] arrayOfFloat3;
        Iterator localIterator;
        PointF localPointF;
        if (this.p)
        {
          localObject2 = MonsterSnakeFaceInfo.b((MonsterSnakeFaceInfo)this.c.get(i7));
          arrayOfFloat3 = new float[((List)localObject2).size() * 2];
          i10 = 0;
          localIterator = ((List)localObject2).iterator();
          while (localIterator.hasNext())
          {
            localPointF = (PointF)localIterator.next();
            arrayOfFloat3[(i10++)] = (localPointF.x * 2.0F - 1.0F);
            arrayOfFloat3[(i10++)] = (localPointF.y * 2.0F - 1.0F);
          }
          a((List)localObject2, g, arrayOfFloat3);
        }
        switch (this.m)
        {
        case 2: 
          MonsterSnakeFaceInfo.c((MonsterSnakeFaceInfo)this.c.get(i7));
          break;
        default: 
          MonsterSnakeFaceInfo.d((MonsterSnakeFaceInfo)this.c.get(i7));
        }
        if (this.p)
        {
          localObject2 = MonsterSnakeFaceInfo.b((MonsterSnakeFaceInfo)this.c.get(i7));
          arrayOfFloat3 = new float[((List)localObject2).size() * 2];
          i10 = 0;
          localIterator = ((List)localObject2).iterator();
          while (localIterator.hasNext())
          {
            localPointF = (PointF)localIterator.next();
            arrayOfFloat3[(i10++)] = (localPointF.x * 2.0F - 1.0F);
            arrayOfFloat3[(i10++)] = (localPointF.y * 2.0F - 1.0F);
          }
          b((List)localObject2, g, arrayOfFloat3);
        }
        Object localObject2 = MonsterSnakeFaceInfo.e((MonsterSnakeFaceInfo)this.c.get(i7));
        for (int i9 = 0; i9 < localObject2.length; i9++) {
          ???[(i3++)] = localObject2[i9];
        }
        i9 = this.h + i7 * 22;
        for (int i10 = 0; i10 < g.length; i10++) {
          arrayOfInt1[(i5++)] = (g[i10] + i9);
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
    this.f.put(this.l).position(0).limit(this.l.length);
  }
  
  private void a(List<PointF> paramList, int[] paramArrayOfInt, float[] paramArrayOfFloat)
  {
    if (this.n == null) {
      return;
    }
    FaceAligment localFaceAligment = new FaceAligment((PointF[])paramList.toArray(new PointF[paramList.size()]));
    FaceAligment[] arrayOfFaceAligment = { localFaceAligment };
    this.n.setColor(new float[] { 1.0F, 0.0F, 0.0F, 1.0F });
    this.n.updateElemIndex(paramArrayOfInt, paramArrayOfFloat);
    this.n.updateFaceFeatures(arrayOfFaceAligment, 0.0F);
  }
  
  private void b(List<PointF> paramList, int[] paramArrayOfInt, float[] paramArrayOfFloat)
  {
    if (this.o == null) {
      return;
    }
    FaceAligment localFaceAligment = new FaceAligment((PointF[])paramList.toArray(new PointF[paramList.size()]));
    FaceAligment[] arrayOfFaceAligment = { localFaceAligment };
    this.o.setColor(new float[] { 0.0F, 1.0F, 0.0F, 1.0F });
    this.o.updateElemIndex(paramArrayOfInt, paramArrayOfFloat);
    this.o.updateFaceFeatures(arrayOfFaceAligment, 0.0F);
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
  
  private class MonsterSnakeFaceInfo
  {
    PointF a;
    PointF[] b = new PointF[12];
    PointF[] c = new PointF[9];
    
    private MonsterSnakeFaceInfo(FaceAligment paramFaceAligment)
    {
      if (paramFaceAligment != null) {
        a(paramFaceAligment.getOrginMarks());
      }
    }
    
    private List<PointF> a()
    {
      ArrayList localArrayList = new ArrayList();
      for (int i = 0; i < 12; i++) {
        localArrayList.add(new PointF(this.b[i].x, this.b[i].y));
      }
      for (i = 0; i < 9; i++) {
        localArrayList.add(new PointF(this.c[i].x, this.c[i].y));
      }
      localArrayList.add(new PointF(this.a.x, this.a.y));
      localArrayList.add(new PointF(this.a.x, this.a.y));
      return localArrayList;
    }
    
    private float[] b()
    {
      float[] arrayOfFloat = new float[44];
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
      arrayOfFloat[(i++)] = (this.a.x * 2.0F - 1.0F);
      arrayOfFloat[(i++)] = (this.a.y * 2.0F - 1.0F);
      return arrayOfFloat;
    }
    
    private float[] c()
    {
      float[] arrayOfFloat = new float[44];
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
      arrayOfFloat[(i++)] = this.a.x;
      arrayOfFloat[(i++)] = this.a.y;
      return arrayOfFloat;
    }
    
    private void d()
    {
      float f = 0.2F;
      this.c[0] = PointCalc.pointOfPercentage(this.c[0], this.a, f * 0.25F);
      this.c[1] = PointCalc.pointOfPercentage(this.c[1], this.a, f * 0.75F);
      this.c[2] = PointCalc.pointOfPercentage(this.c[2], this.a, f * 1.0F);
      this.c[3] = PointCalc.pointOfPercentage(this.c[3], this.a, f * 0.75F);
      this.c[4] = PointCalc.pointOfPercentage(this.c[4], this.a, f * 0.4F);
      this.c[5] = PointCalc.pointOfPercentage(this.c[5], this.a, f * 0.75F);
      this.c[6] = PointCalc.pointOfPercentage(this.c[6], this.a, f * 1.0F);
      this.c[7] = PointCalc.pointOfPercentage(this.c[7], this.a, f * 0.75F);
      this.c[8] = PointCalc.pointOfPercentage(this.c[8], this.a, f * 0.25F);
    }
    
    private void e()
    {
      float f = -0.2F;
      this.c[0] = PointCalc.pointOfPercentage(this.c[0], this.a, f * 0.25F);
      this.c[1] = PointCalc.pointOfPercentage(this.c[1], this.a, f * 0.75F);
      this.c[2] = PointCalc.pointOfPercentage(this.c[2], this.a, f * 1.0F);
      this.c[3] = PointCalc.pointOfPercentage(this.c[3], this.a, f * 0.75F);
      this.c[4] = PointCalc.pointOfPercentage(this.c[4], this.a, f * 0.4F);
      this.c[5] = PointCalc.pointOfPercentage(this.c[5], this.a, f * 0.75F);
      this.c[6] = PointCalc.pointOfPercentage(this.c[6], this.a, f * 1.0F);
      this.c[7] = PointCalc.pointOfPercentage(this.c[7], this.a, f * 0.75F);
      this.c[8] = PointCalc.pointOfPercentage(this.c[8], this.a, f * 0.25F);
    }
    
    private void a(PointF[] paramArrayOfPointF)
    {
      if ((paramArrayOfPointF == null) || (paramArrayOfPointF.length < 3)) {
        return;
      }
      int[] arrayOfInt = { 0, 4, 8, 12, 16, 20, 24, 28, 32 };
      this.a = new PointF(paramArrayOfPointF[46].x, paramArrayOfPointF[46].y);
      for (int i = 0; i < 9; i++) {
        this.c[i] = new PointF(paramArrayOfPointF[arrayOfInt[i]].x, paramArrayOfPointF[arrayOfInt[i]].y);
      }
      for (i = 0; i < 9; i++) {
        this.b[i] = new PointF(paramArrayOfPointF[arrayOfInt[i]].x, paramArrayOfPointF[arrayOfInt[i]].y);
      }
      this.b[9] = new PointF(paramArrayOfPointF[34].x, paramArrayOfPointF[34].y);
      this.b[10] = new PointF(paramArrayOfPointF[41].x, paramArrayOfPointF[41].y);
      this.b[11] = PointCalc.center(paramArrayOfPointF[35], paramArrayOfPointF[40]);
      PointCalc.scalePoint(this.b, this.a, 0.5F);
    }
  }
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKCore-3.1.0.jar!\org\lasque\tusdk\core\seles\tusdk\liveSticker\TuSDKMonsterSnakeFace.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */