package org.lasque.tusdk.core.seles.tusdk.liveSticker;

import android.graphics.PointF;
import android.opengl.GLES20;
import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import org.lasque.tusdk.core.face.FaceAligment;
import org.lasque.tusdk.core.seles.SelesContext;
import org.lasque.tusdk.core.seles.SelesFramebuffer;
import org.lasque.tusdk.core.seles.SelesFramebuffer.SelesFramebufferMode;
import org.lasque.tusdk.core.seles.SelesFramebufferCache;
import org.lasque.tusdk.core.seles.SelesParameters;
import org.lasque.tusdk.core.seles.SelesParameters.FilterArg;
import org.lasque.tusdk.core.seles.SelesParameters.FilterFacePositionInterface;
import org.lasque.tusdk.core.seles.SelesParameters.FilterParameterInterface;
import org.lasque.tusdk.core.seles.filters.SelesFilter;
import org.lasque.tusdk.core.seles.filters.SelesPointDrawFilter;
import org.lasque.tusdk.core.struct.TuSdkSize;
import org.lasque.tusdk.core.utils.TLog;

public class TuSdkPlasticFace
  extends SelesFilter
  implements SelesParameters.FilterFacePositionInterface, SelesParameters.FilterParameterInterface
{
  private final FloatBuffer d = ByteBuffer.allocateDirect(2416).order(ByteOrder.nativeOrder()).asFloatBuffer();
  private final FloatBuffer e = ByteBuffer.allocateDirect(2416).order(ByteOrder.nativeOrder()).asFloatBuffer();
  private final IntBuffer f = ByteBuffer.allocateDirect(7248).order(ByteOrder.nativeOrder()).asIntBuffer();
  private final int[] g = { 0, 1, 2, 1, 2, 3 };
  private boolean h = false;
  private float[] i;
  private float[] j;
  private int[] k;
  FaceAligment[] a;
  private final Object l = new Object();
  int[] b;
  private float m = 1.0F;
  private float n = 0.0F;
  private float o = 1.0F;
  private float p = 1.0F;
  private float q = 0.0F;
  private float r = 0.0F;
  private float s = 0.0F;
  private float t = 0.0F;
  private SelesPointDrawFilter u;
  private boolean v = false;
  List<TuSdkPlasticFaceInfo> c;
  
  public TuSdkPlasticFace()
  {
    if (this.v)
    {
      this.u = new SelesPointDrawFilter();
      addTarget(this.u, 0);
    }
    this.c = new ArrayList();
  }
  
  public void removeAllTargets()
  {
    super.removeAllTargets();
    if (this.u != null) {
      addTarget(this.u, 0);
    }
  }
  
  protected void onInitOnGLThread()
  {
    super.onInitOnGLThread();
    checkGLError("TuSdkPlasticFace onInitOnGLThread");
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
    if (this.mUsingNextFrameForImageCapture) {
      this.mOutputFramebuffer.lock();
    }
    setUniformsForProgramAtIndex(0);
    GLES20.glClearColor(this.mBackgroundColorRed, this.mBackgroundColorGreen, this.mBackgroundColorBlue, this.mBackgroundColorAlpha);
    GLES20.glClear(16384);
    inputFramebufferBindTexture();
    checkGLError("TuSdkPlasticFace inputFramebufferBindTexture");
    GLES20.glVertexAttribPointer(this.mFilterPositionAttribute, 2, 5126, false, 0, this.d);
    GLES20.glVertexAttribPointer(this.mFilterTextureCoordinateAttribute, 2, 5126, false, 0, this.e);
    GLES20.glDrawElements(4, this.f.limit(), 5125, this.f);
    captureFilterImage(getClass().getSimpleName(), this.mInputTextureSize.width, this.mInputTextureSize.height);
    inputFramebufferUnlock();
    cacaptureImageBuffer();
  }
  
  private void a(FloatBuffer paramFloatBuffer1, FloatBuffer paramFloatBuffer2)
  {
    boolean bool = this.h;
    FaceAligment[] arrayOfFaceAligment = this.a;
    if ((bool) && (arrayOfFaceAligment != null))
    {
      this.c.clear();
      for (FaceAligment localFaceAligment : arrayOfFaceAligment)
      {
        TuSdkPlasticFaceInfo localTuSdkPlasticFaceInfo2 = new TuSdkPlasticFaceInfo(localFaceAligment);
        if (localTuSdkPlasticFaceInfo2.isEmpty()) {
          TLog.w("plastic face is empty !!!", new Object[0]);
        } else {
          this.c.add(localTuSdkPlasticFaceInfo2);
        }
      }
      if (this.c.isEmpty())
      {
        this.b = null;
        this.h = false;
        TLog.w("may be not data", new Object[0]);
        return;
      }
      a(this.c);
      ??? = this.c.iterator();
      while (((Iterator)???).hasNext())
      {
        TuSdkPlasticFaceInfo localTuSdkPlasticFaceInfo1 = (TuSdkPlasticFaceInfo)((Iterator)???).next();
        localTuSdkPlasticFaceInfo1.calcChin(this.n);
        localTuSdkPlasticFaceInfo1.calcEyeEnlarge(this.m);
        localTuSdkPlasticFaceInfo1.calcEyeDis(this.r);
        localTuSdkPlasticFaceInfo1.calcEyeAngle(this.s);
        localTuSdkPlasticFaceInfo1.calcNose(this.o);
        localTuSdkPlasticFaceInfo1.calcMouth(this.p);
        localTuSdkPlasticFaceInfo1.calcArchEyebrow(this.q);
        localTuSdkPlasticFaceInfo1.calcJaw(this.t);
      }
      if (!b(this.c))
      {
        this.h = false;
        return;
      }
      a();
      return;
    }
    this.d.clear();
    paramFloatBuffer1.position(0);
    this.d.put(paramFloatBuffer1).position(0);
    this.e.clear();
    paramFloatBuffer2.position(0);
    this.e.put(paramFloatBuffer2).position(0);
    this.f.clear();
    this.f.put(this.g).position(0).limit(6);
  }
  
  private void a()
  {
    synchronized (this.l)
    {
      this.d.clear();
      this.d.put(this.i).position(0).limit(this.i.length);
      this.e.clear();
      this.e.put(this.j).position(0).limit(this.j.length);
      this.f.clear();
      this.f.put(this.k).position(0).limit(this.k.length);
    }
  }
  
  private void a(float paramFloat)
  {
    this.m = paramFloat;
  }
  
  private float b()
  {
    return this.m;
  }
  
  private void b(float paramFloat)
  {
    this.r = paramFloat;
  }
  
  private float c()
  {
    return this.r;
  }
  
  private void c(float paramFloat)
  {
    this.s = paramFloat;
  }
  
  private float d()
  {
    return this.s;
  }
  
  private void d(float paramFloat)
  {
    this.n = paramFloat;
  }
  
  private float e()
  {
    return this.n;
  }
  
  private void e(float paramFloat)
  {
    this.t = paramFloat;
  }
  
  private float f()
  {
    return this.t;
  }
  
  private float g()
  {
    return this.o;
  }
  
  private void f(float paramFloat)
  {
    this.o = paramFloat;
  }
  
  private float h()
  {
    return this.p;
  }
  
  private void g(float paramFloat)
  {
    this.p = paramFloat;
  }
  
  public float getArchEyebrow()
  {
    return this.q;
  }
  
  public void setArchEyebrow(float paramFloat)
  {
    this.q = paramFloat;
  }
  
  protected SelesParameters initParams(SelesParameters paramSelesParameters)
  {
    paramSelesParameters = super.initParams(paramSelesParameters);
    paramSelesParameters.appendFloatArg("eyeSize", b(), 1.0F, 1.3F);
    paramSelesParameters.appendFloatArg("chinSize", e(), 0.0F, 0.1F);
    paramSelesParameters.appendFloatArg("noseSize", g(), 1.0F, 0.8F);
    paramSelesParameters.appendFloatArg("mouthWidth", h(), 0.9F, 1.1F);
    paramSelesParameters.appendFloatArg("archEyebrow", getArchEyebrow(), 0.3F, -0.3F);
    paramSelesParameters.appendFloatArg("eyeDis", c(), -0.05F, 0.05F);
    paramSelesParameters.appendFloatArg("eyeAngle", d(), -5.0F, 5.0F);
    paramSelesParameters.appendFloatArg("jawSize", f(), 0.06F, -0.06F);
    return paramSelesParameters;
  }
  
  protected void submitFilterArg(SelesParameters.FilterArg paramFilterArg)
  {
    if (paramFilterArg == null) {
      return;
    }
    if (paramFilterArg.equalsKey("eyeSize")) {
      a(paramFilterArg.getValue());
    } else if (paramFilterArg.equalsKey("chinSize")) {
      d(paramFilterArg.getValue());
    } else if (paramFilterArg.equalsKey("noseSize")) {
      f(paramFilterArg.getValue());
    } else if (paramFilterArg.equalsKey("mouthWidth")) {
      g(paramFilterArg.getValue());
    } else if (paramFilterArg.equalsKey("archEyebrow")) {
      setArchEyebrow(paramFilterArg.getValue());
    } else if (paramFilterArg.equalsKey("eyeDis")) {
      b(paramFilterArg.getValue());
    } else if (paramFilterArg.equalsKey("eyeAngle")) {
      c(paramFilterArg.getValue());
    } else if (paramFilterArg.equalsKey("jawSize")) {
      e(paramFilterArg.getValue());
    }
  }
  
  public void updateFaceFeatures(FaceAligment[] paramArrayOfFaceAligment, float paramFloat)
  {
    if ((paramArrayOfFaceAligment == null) || (paramArrayOfFaceAligment.length < 1))
    {
      this.b = null;
      this.h = false;
      this.a = null;
      return;
    }
    synchronized (this.l)
    {
      this.a = paramArrayOfFaceAligment;
    }
    this.h = true;
  }
  
  private boolean a(List<TuSdkPlasticFaceInfo> paramList)
  {
    ArrayList localArrayList = new ArrayList(paramList.size() * 107 + 4);
    localArrayList.add(new PointF(0.0F, 0.0F));
    localArrayList.add(new PointF(1.0F, 0.0F));
    localArrayList.add(new PointF(1.0F, 1.0F));
    localArrayList.add(new PointF(0.0F, 1.0F));
    Object localObject1 = paramList.iterator();
    while (((Iterator)localObject1).hasNext())
    {
      TuSdkPlasticFaceInfo localTuSdkPlasticFaceInfo = (TuSdkPlasticFaceInfo)((Iterator)localObject1).next();
      localObject2 = localTuSdkPlasticFaceInfo.getPoints();
      localArrayList.addAll((Collection)localObject2);
    }
    localObject1 = new float[localArrayList.size() * 2];
    int i1 = 0;
    Object localObject2 = localArrayList.iterator();
    while (((Iterator)localObject2).hasNext())
    {
      PointF localPointF = (PointF)((Iterator)localObject2).next();
      localObject1[(i1++)] = localPointF.x;
      localObject1[(i1++)] = localPointF.y;
    }
    this.j = ((float[])localObject1);
    return true;
  }
  
  private boolean b(List<TuSdkPlasticFaceInfo> paramList)
  {
    ArrayList localArrayList1 = new ArrayList(paramList.size() * 107 + 4);
    localArrayList1.add(new PointF(0.0F, 0.0F));
    localArrayList1.add(new PointF(1.0F, 0.0F));
    localArrayList1.add(new PointF(1.0F, 1.0F));
    localArrayList1.add(new PointF(0.0F, 1.0F));
    ArrayList localArrayList2 = new ArrayList();
    int[] arrayOfInt1 = { 0, 1, 2, 0, 3, 2 };
    Object localObject1 = paramList.iterator();
    while (((Iterator)localObject1).hasNext())
    {
      TuSdkPlasticFaceInfo localTuSdkPlasticFaceInfo = (TuSdkPlasticFaceInfo)((Iterator)localObject1).next();
      localObject2 = localTuSdkPlasticFaceInfo.getPoints();
      int i3 = localArrayList1.size();
      localArrayList1.addAll((Collection)localObject2);
      int[] arrayOfInt2 = localTuSdkPlasticFaceInfo.fillFace();
      for (int i4 = 0; i4 < arrayOfInt2.length; i4++) {
        localArrayList2.add(Integer.valueOf(arrayOfInt2[i4] + i3));
      }
    }
    localObject1 = new float[localArrayList1.size() * 2];
    int i1 = 0;
    Object localObject2 = localArrayList1.iterator();
    while (((Iterator)localObject2).hasNext())
    {
      PointF localPointF = (PointF)((Iterator)localObject2).next();
      localObject1[(i1++)] = (localPointF.x * 2.0F - 1.0F);
      localObject1[(i1++)] = (localPointF.y * 2.0F - 1.0F);
    }
    this.i = ((float[])localObject1);
    this.b = new int[arrayOfInt1.length + localArrayList2.size()];
    if ((this.b == null) || (this.b.length == 0)) {
      return false;
    }
    for (int i2 = 0; i2 < arrayOfInt1.length; i2++) {
      this.b[i2] = arrayOfInt1[i2];
    }
    for (i2 = 0; i2 < localArrayList2.size(); i2++) {
      this.b[(arrayOfInt1.length + i2)] = ((Integer)localArrayList2.get(i2)).intValue();
    }
    this.k = this.b;
    if (this.v) {
      c(localArrayList1);
    }
    return true;
  }
  
  private void c(List<PointF> paramList)
  {
    if (this.u == null) {
      return;
    }
    FaceAligment localFaceAligment = new FaceAligment((PointF[])paramList.toArray(new PointF[paramList.size()]));
    FaceAligment[] arrayOfFaceAligment = { localFaceAligment };
    this.u.updateElemIndex(this.k, this.i);
    this.u.updateFaceFeatures(arrayOfFaceAligment, 0.0F);
  }
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKCore-3.1.0.jar!\org\lasque\tusdk\core\seles\tusdk\liveSticker\TuSdkPlasticFace.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */