package org.lasque.tusdk.core.seles.tusdk.particle;

import android.graphics.Bitmap;
import android.graphics.PointF;
import android.opengl.GLES20;
import android.opengl.Matrix;
import java.nio.FloatBuffer;
import org.json.JSONObject;
import org.lasque.tusdk.core.TuSdkBundle;
import org.lasque.tusdk.core.TuSdkContext;
import org.lasque.tusdk.core.seles.SelesContext;
import org.lasque.tusdk.core.seles.SelesFramebuffer;
import org.lasque.tusdk.core.seles.SelesGLProgram;
import org.lasque.tusdk.core.seles.filters.SelesTwoInputFilter;
import org.lasque.tusdk.core.seles.sources.SelesPicture;
import org.lasque.tusdk.core.struct.TuSdkSize;

public class TuSDKParticleFilter
  extends SelesTwoInputFilter
  implements TuSDKParticleFilterInterface
{
  private ParticleManager a;
  private final float[] b = new float[16];
  private int c;
  private int d;
  private long e;
  private PointF f;
  
  public TuSDKParticleFilter(JSONObject paramJSONObject)
  {
    super("-sp02v", "-sp02f");
    disableSecondFrameCheck();
    this.a = new ParticleManager(paramJSONObject);
  }
  
  protected void onDestroy()
  {
    if (this.a != null)
    {
      this.a.destory();
      this.a = null;
    }
    super.onDestroy();
  }
  
  protected void onInitOnGLThread()
  {
    super.onInitOnGLThread();
    this.c = this.mFilterProgram.uniformIndex("uTexMatrix");
    this.d = this.mFilterProgram.uniformIndex("uTexTilePrams");
    GLES20.glUniform2fv(this.d, 1, this.a.getTextureTile(), 0);
    Matrix.setIdentityM(this.b, 0);
  }
  
  public void loadTexture()
  {
    Bitmap localBitmap = TuSdkContext.getAssetsBitmap(TuSdkBundle.sdkBundleTexture("a.png"));
    if (localBitmap == null) {
      return;
    }
    SelesPicture localSelesPicture = new SelesPicture(localBitmap, false, true);
    localSelesPicture.processImage();
    localSelesPicture.addTarget(this, 1);
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
    a();
    cacaptureImageBuffer();
  }
  
  private void a()
  {
    if (this.mSecondInputFramebuffer == null) {
      return;
    }
    GLES20.glActiveTexture(33986);
    GLES20.glBindTexture(3553, this.mSecondInputFramebuffer.getTexture());
    GLES20.glUniform1i(this.mFilterInputTextureUniform, 2);
    GLES20.glUniformMatrix4fv(this.c, 1, false, this.b, 0);
    this.a.freshVBO();
    GLES20.glVertexAttribPointer(this.mFilterPositionAttribute, 2, 5126, false, 40, 0);
    GLES20.glVertexAttribPointer(this.mFilterTextureCoordinateAttribute, 4, 5126, false, 40, 8);
    GLES20.glVertexAttribPointer(this.mFilterSecondTextureCoordinateAttribute, 4, 5126, false, 40, 24);
    GLES20.glEnable(3042);
    GLES20.glBlendFunc(this.a.config().blendFuncSrc, this.a.config().blendFuncDst);
    GLES20.glDrawArrays(0, 0, this.a.drawTotal());
    GLES20.glDisable(3042);
    GLES20.glBindBuffer(34962, 0);
  }
  
  public void setInputSize(TuSdkSize paramTuSdkSize, int paramInt)
  {
    super.setInputSize(paramTuSdkSize, paramInt);
    this.a.setTextureSize(this.mInputTextureSize);
  }
  
  protected void informTargetsAboutNewFrame(long paramLong)
  {
    a(paramLong / 1000L);
    super.informTargetsAboutNewFrame(paramLong);
  }
  
  private void a(long paramLong)
  {
    if (this.f == null) {
      return;
    }
    this.a.config().position = this.f;
    float f1 = (float)Math.abs(paramLong - this.e) / 1000.0F / 1000.0F;
    this.e = paramLong;
    if (f1 > 1.0F) {
      f1 = 0.1F;
    }
    this.a.update(f1);
  }
  
  public void updateParticleEmitPosition(PointF paramPointF)
  {
    if (paramPointF == null) {
      return;
    }
    this.f = paramPointF;
  }
  
  public void setParticleSize(float paramFloat)
  {
    this.a.updateParticleSize(paramFloat);
  }
  
  public void setParticleColor(int paramInt)
  {
    this.a.updateParticleColor(paramInt);
  }
  
  public void setActive(boolean paramBoolean)
  {
    this.a.setActive(paramBoolean);
  }
  
  public void reset()
  {
    this.a.reset(0);
  }
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKCore-3.1.0.jar!\org\lasque\tusdk\core\seles\tusdk\particle\TuSDKParticleFilter.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */