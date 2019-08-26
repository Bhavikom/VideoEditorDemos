package org.lasque.tusdk.core.seles.filters;

import android.graphics.PointF;
import android.opengl.GLES20;
import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import org.lasque.tusdk.core.face.FaceAligment;
import org.lasque.tusdk.core.seles.SelesContext;
import org.lasque.tusdk.core.seles.SelesFramebuffer;
import org.lasque.tusdk.core.seles.SelesGLProgram;

public class SelesPointDrawFilter
  extends SelesFilter
{
  public static final String SELES_POINT_DRAW_VERTEX_SHADER = "attribute vec4 position;uniform mat4 uMVPMatrix;void main(){    gl_Position = position;    gl_PointSize = 5.0;}";
  public static final String SELES_POINT_DRAW_FRAGMENT_SHADER = "precision highp float;uniform vec4 uColor;void main(){     gl_FragColor = uColor;}";
  private FaceAligment[] b;
  private float c = 0.0F;
  private IntBuffer d;
  private FloatBuffer e;
  private FloatBuffer f;
  private int g;
  private int h;
  private final float[] i = new float[16];
  private float[] j;
  float[] a = { 1.0F, 0.0F, 0.0F, 1.0F };
  
  public SelesPointDrawFilter()
  {
    this("attribute vec4 position;uniform mat4 uMVPMatrix;void main(){    gl_Position = position;    gl_PointSize = 5.0;}", "precision highp float;uniform vec4 uColor;void main(){     gl_FragColor = uColor;}");
    this.d = ByteBuffer.allocateDirect(14448).order(ByteOrder.nativeOrder()).asIntBuffer();
    this.e = ByteBuffer.allocateDirect(4816).order(ByteOrder.nativeOrder()).asFloatBuffer();
  }
  
  public SelesPointDrawFilter(String paramString1, String paramString2)
  {
    super(paramString1, paramString2);
  }
  
  protected void onInitOnGLThread()
  {
    super.onInitOnGLThread();
    this.g = this.mFilterProgram.uniformIndex("uMVPMatrix");
    this.h = this.mFilterProgram.uniformIndex("uColor");
    setVec4(new float[] { 0.0F, 1.0F, 0.0F, 1.0F }, this.h, this.mFilterProgram);
  }
  
  public void setColor(float[] paramArrayOfFloat)
  {
    if (paramArrayOfFloat == null) {
      return;
    }
    this.a = paramArrayOfFloat;
  }
  
  public void newFrameReady(long paramLong, int paramInt)
  {
    if (this.mFirstInputFramebuffer == null) {
      return;
    }
    a();
    renderToTexture(this.f, null);
    informTargetsAboutNewFrame(paramLong);
  }
  
  protected void renderToTexture(FloatBuffer paramFloatBuffer1, FloatBuffer paramFloatBuffer2)
  {
    runPendingOnDrawTasks();
    if (isPreventRendering())
    {
      inputFramebufferUnlock();
      return;
    }
    SelesContext.setActiveShaderProgram(this.mFilterProgram);
    this.mOutputFramebuffer = this.mFirstInputFramebuffer;
    this.mOutputFramebuffer.activateFramebuffer();
    if (this.mUsingNextFrameForImageCapture) {
      this.mOutputFramebuffer.lock();
    }
    setUniformsForProgramAtIndex(0);
    if (b() > 0)
    {
      setVec4(new float[] { 0.0F, 1.0F, 0.0F, 1.0F }, this.h, this.mFilterProgram);
      GLES20.glVertexAttribPointer(this.mFilterPositionAttribute, 2, 5126, false, 0, paramFloatBuffer1);
      GLES20.glDrawArrays(0, 0, this.j.length / 2);
      setVec4(this.a, this.h, this.mFilterProgram);
      GLES20.glVertexAttribPointer(this.mFilterPositionAttribute, 2, 5126, false, 0, this.e);
      GLES20.glDrawElements(1, this.d.limit(), 5125, this.d);
    }
    cacaptureImageBuffer();
  }
  
  private void a()
  {
    if (b() < 1) {
      return;
    }
    List localList = Arrays.asList(this.b);
    int k = a(localList) * 2;
    if ((this.j == null) || (k != this.j.length))
    {
      this.j = new float[k];
      this.f = buildBuffer(this.j);
    }
    int m = 0;
    Iterator localIterator = localList.iterator();
    while (localIterator.hasNext())
    {
      FaceAligment localFaceAligment = (FaceAligment)localIterator.next();
      if (localFaceAligment.getOrginMarks() != null)
      {
        PointF[] arrayOfPointF1 = localFaceAligment.getOrginMarks();
        for (PointF localPointF : arrayOfPointF1)
        {
          this.j[(m++)] = (localPointF.x * 2.0F - 1.0F);
          this.j[(m++)] = (localPointF.y * 2.0F - 1.0F);
        }
      }
    }
    this.f.clear();
    this.f.put(this.j).position(0);
  }
  
  private int a(List<FaceAligment> paramList)
  {
    if ((paramList == null) || (paramList.size() < 1)) {
      return 0;
    }
    int k = 0;
    Iterator localIterator = paramList.iterator();
    while (localIterator.hasNext())
    {
      FaceAligment localFaceAligment = (FaceAligment)localIterator.next();
      if (localFaceAligment.getOrginMarks() != null) {
        k += localFaceAligment.getOrginMarks().length;
      }
    }
    return k;
  }
  
  public void updateFaceFeatures(FaceAligment[] paramArrayOfFaceAligment, float paramFloat)
  {
    this.b = paramArrayOfFaceAligment;
    this.c = ((float)(paramFloat * 3.141592653589793D / 180.0D));
  }
  
  public void updateElemIndex(int[] paramArrayOfInt, float[] paramArrayOfFloat)
  {
    this.d.clear();
    ArrayList localArrayList = new ArrayList();
    for (int k = 0; k < paramArrayOfInt.length; k++) {
      if (k % 3 == 0)
      {
        localArrayList.add(Integer.valueOf(paramArrayOfInt[k]));
        localArrayList.add(Integer.valueOf(paramArrayOfInt[(k + 1)]));
      }
      else if (k % 3 == 1)
      {
        localArrayList.add(Integer.valueOf(paramArrayOfInt[k]));
        localArrayList.add(Integer.valueOf(paramArrayOfInt[(k + 1)]));
      }
      else if (k % 3 == 2)
      {
        localArrayList.add(Integer.valueOf(paramArrayOfInt[k]));
        localArrayList.add(Integer.valueOf(paramArrayOfInt[(k - 2)]));
      }
    }
    int[] arrayOfInt = new int[localArrayList.size()];
    for (int m = 0; m < localArrayList.size(); m++) {
      arrayOfInt[m] = ((Integer)localArrayList.get(m)).intValue();
    }
    this.d.put(arrayOfInt).position(0).limit(arrayOfInt.length);
    this.e.clear();
    this.e.put(paramArrayOfFloat).position(0).limit(paramArrayOfFloat.length);
  }
  
  private int b()
  {
    if (this.b == null) {
      return 0;
    }
    return this.b.length;
  }
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKCore-3.1.0.jar!\org\lasque\tusdk\core\seles\filters\SelesPointDrawFilter.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */