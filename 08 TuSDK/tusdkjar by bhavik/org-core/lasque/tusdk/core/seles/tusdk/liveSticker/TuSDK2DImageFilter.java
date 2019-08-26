package org.lasque.tusdk.core.seles.tusdk.liveSticker;

import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.RectF;
import android.opengl.GLES20;
import android.opengl.Matrix;
import java.nio.FloatBuffer;
import java.util.List;
import org.lasque.tusdk.core.seles.SelesContext;
import org.lasque.tusdk.core.seles.SelesFramebuffer;
import org.lasque.tusdk.core.seles.SelesGLProgram;
import org.lasque.tusdk.core.seles.filters.SelesFilter;
import org.lasque.tusdk.core.seles.tusdk.textSticker.Image2DStickerData;
import org.lasque.tusdk.core.seles.tusdk.textSticker.TuSdkImage2DSticker;
import org.lasque.tusdk.core.struct.TuSdkSize;
import org.lasque.tusdk.core.struct.TuSdkSizeF;
import org.lasque.tusdk.core.utils.RectHelper;

public class TuSDK2DImageFilter
  extends SelesFilter
{
  public static final String TUSDK_MAP_2D_VERTEX_SHADER = "attribute vec4 position;attribute vec4 inputTextureCoordinate;varying vec2 textureCoordinate;uniform mat4 uMVPMatrix;uniform mat4 uTexMatrix;void main(){    gl_Position = uMVPMatrix * position;\n    textureCoordinate = (uTexMatrix * inputTextureCoordinate).xy;}";
  protected static final float[] stickerVertices = { -0.5F, -0.5F, 0.0F, 1.0F, 0.5F, -0.5F, 0.0F, 1.0F, -0.5F, 0.5F, 0.0F, 1.0F, 0.5F, 0.5F, 0.0F, 1.0F };
  private final FloatBuffer a = buildBuffer(stickerVertices);
  private final FloatBuffer b = buildBuffer(noRotationTextureCoordinates);
  private int c;
  private int d;
  private final float[] e = new float[16];
  private final float[] f = new float[16];
  private List<TuSdkImage2DSticker> g;
  private RectF h;
  private float i;
  protected float mDeviceRadian = 0.0F;
  
  public TuSDK2DImageFilter()
  {
    this("attribute vec4 position;attribute vec4 inputTextureCoordinate;varying vec2 textureCoordinate;uniform mat4 uMVPMatrix;uniform mat4 uTexMatrix;void main(){    gl_Position = uMVPMatrix * position;\n    textureCoordinate = (uTexMatrix * inputTextureCoordinate).xy;}", "varying highp vec2 textureCoordinate;uniform sampler2D inputImageTexture;void main(){     gl_FragColor = texture2D(inputImageTexture, textureCoordinate);}");
  }
  
  public TuSDK2DImageFilter(String paramString)
  {
    this("attribute vec4 position;attribute vec4 inputTextureCoordinate;varying vec2 textureCoordinate;uniform mat4 uMVPMatrix;uniform mat4 uTexMatrix;void main(){    gl_Position = uMVPMatrix * position;\n    textureCoordinate = (uTexMatrix * inputTextureCoordinate).xy;}", paramString);
  }
  
  public TuSDK2DImageFilter(String paramString1, String paramString2)
  {
    super(paramString1, paramString2);
  }
  
  public void setDisplayRect(RectF paramRectF, float paramFloat)
  {
    this.h = paramRectF;
    this.i = paramFloat;
  }
  
  protected void onDestroy()
  {
    super.onDestroy();
  }
  
  protected void onInitOnGLThread()
  {
    super.onInitOnGLThread();
    this.c = this.mFilterProgram.uniformIndex("uTexMatrix");
    this.d = this.mFilterProgram.uniformIndex("uMVPMatrix");
    checkGLError(getClass().getSimpleName() + " onInitOnGLThread");
  }
  
  public void newFrameReady(long paramLong, int paramInt)
  {
    if (this.mFirstInputFramebuffer == null) {
      return;
    }
    this.b.clear();
    this.b.put(textureCoordinates(this.mInputRotation)).position(0);
    renderToTexture(this.a, this.b);
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
    checkGLError(getClass().getSimpleName() + " activateFramebuffer");
    setUniformsForProgramAtIndex(0);
    if (getStickerCount() > 0)
    {
      GLES20.glEnable(3042);
      a(paramFloatBuffer1, paramFloatBuffer2);
      GLES20.glDisable(3042);
    }
    captureFilterImage(getClass().getSimpleName(), this.mInputTextureSize.width, this.mInputTextureSize.height);
    cacaptureImageBuffer();
  }
  
  private void a(FloatBuffer paramFloatBuffer1, FloatBuffer paramFloatBuffer2)
  {
    int j = 0;
    int k = getStickerCount();
    while ((j < k) && (j < this.g.size()))
    {
      TuSdkImage2DSticker localTuSdkImage2DSticker = (TuSdkImage2DSticker)this.g.get(j);
      if (localTuSdkImage2DSticker != null)
      {
        GLES20.glBlendFunc(1, 771);
        a(localTuSdkImage2DSticker, paramFloatBuffer1, paramFloatBuffer2, -1);
        j++;
      }
    }
  }
  
  private void a(TuSdkImage2DSticker paramTuSdkImage2DSticker, FloatBuffer paramFloatBuffer1, FloatBuffer paramFloatBuffer2, int paramInt)
  {
    if ((paramTuSdkImage2DSticker == null) || (!paramTuSdkImage2DSticker.isEnabled())) {
      return;
    }
    TuSdkSize localTuSdkSize = sizeOfFBO();
    float[] arrayOfFloat = { -0.5F, -0.5F, 0.0F, 1.0F, 0.5F, -0.5F, 0.0F, 1.0F, -0.5F, 0.5F, 0.0F, 1.0F, 0.5F, 0.5F, 0.0F, 1.0F };
    a(paramTuSdkImage2DSticker, localTuSdkSize);
    paramFloatBuffer1.clear();
    paramFloatBuffer1.put(arrayOfFloat).position(0);
    GLES20.glActiveTexture(33986);
    GLES20.glBindTexture(3553, paramTuSdkImage2DSticker.getCurrentTextureId());
    GLES20.glUniform1i(this.mFilterInputTextureUniform, 2);
    GLES20.glUniformMatrix4fv(this.d, 1, false, this.f, 0);
    GLES20.glUniformMatrix4fv(this.c, 1, false, this.e, 0);
    GLES20.glVertexAttribPointer(this.mFilterPositionAttribute, 4, 5126, false, 0, paramFloatBuffer1);
    GLES20.glVertexAttribPointer(this.mFilterTextureCoordinateAttribute, 2, 5126, false, 0, paramFloatBuffer2);
    GLES20.glDrawArrays(5, 0, 4);
  }
  
  public void updateStickers(List<TuSdkImage2DSticker> paramList)
  {
    this.g = paramList;
  }
  
  protected int getStickerCount()
  {
    if (this.g == null) {
      return 0;
    }
    return this.g.size();
  }
  
  private void a(TuSdkImage2DSticker paramTuSdkImage2DSticker, TuSdkSize paramTuSdkSize)
  {
    RectF localRectF = this.h;
    TuSdkSizeF localTuSdkSizeF1 = TuSdkSizeF.create(paramTuSdkImage2DSticker.getCurrentSticker().getWidth(), paramTuSdkImage2DSticker.getCurrentSticker().getHeight());
    PointF localPointF = new PointF(0.0F, 0.0F);
    TuSdkSizeF localTuSdkSizeF2 = TuSdkSizeF.create(paramTuSdkSize.width, paramTuSdkSize.height);
    if ((localRectF == null) || (localRectF.isEmpty())) {
      if (this.i > 0.0F)
      {
        TuSdkSize localTuSdkSize = TuSdkSize.create(paramTuSdkSize);
        localTuSdkSize.width = ((int)(paramTuSdkSize.height * this.i));
        Rect localRect = RectHelper.makeRectWithAspectRatioInsideRect(localTuSdkSize, new Rect(0, 0, paramTuSdkSize.width, paramTuSdkSize.height));
        f3 = localRect.left / paramTuSdkSize.width;
        f4 = localRect.top / paramTuSdkSize.height;
        f5 = localRect.right / paramTuSdkSize.width;
        f6 = localRect.bottom / paramTuSdkSize.height;
        localRectF = new RectF(f3, f4, f5, f6);
      }
      else
      {
        localRectF = new RectF(0.0F, 0.0F, 1.0F, 1.0F);
      }
    }
    if (this.i > 0.0F)
    {
      f1 = paramTuSdkSize.width / paramTuSdkSize.height;
      if (f1 > this.i)
      {
        localTuSdkSizeF2.width = (paramTuSdkSize.height * this.i);
        localTuSdkSizeF2.height = paramTuSdkSize.height;
      }
      else
      {
        localTuSdkSizeF2.width = paramTuSdkSize.width;
        localTuSdkSizeF2.height = (paramTuSdkSize.width / this.i);
      }
    }
    else
    {
      localTuSdkSizeF2.width = (paramTuSdkSize.width * (localRectF.left + localRectF.right));
      localTuSdkSizeF2.height = (paramTuSdkSize.height * (localRectF.top + localRectF.bottom));
    }
    float f1 = localTuSdkSizeF2.width / paramTuSdkImage2DSticker.getDesignScreenSize().width;
    float f2 = localTuSdkSizeF2.height / paramTuSdkImage2DSticker.getDesignScreenSize().height;
    if ((f1 != 1.0F) && (f2 != 1.0F))
    {
      f3 = Math.min(f1, f2);
      localTuSdkSizeF1.width *= f3;
      localTuSdkSizeF1.height *= f3;
    }
    float f3 = paramTuSdkImage2DSticker.getCurrentSticker().getOffsetX() / paramTuSdkImage2DSticker.getDesignScreenSize().width;
    float f4 = paramTuSdkImage2DSticker.getCurrentSticker().getOffsetY() / paramTuSdkImage2DSticker.getDesignScreenSize().height;
    float f5 = localTuSdkSizeF2.width * f3;
    float f6 = localTuSdkSizeF2.height * f4;
    localPointF.x = (localTuSdkSizeF1.width / 2.0F + f5);
    localPointF.y = (localTuSdkSizeF1.height / 2.0F + f6);
    a(paramTuSdkSize, localTuSdkSizeF1, localPointF, paramTuSdkImage2DSticker.getCurrentSticker().getRotation());
  }
  
  private void a(TuSdkSize paramTuSdkSize, TuSdkSizeF paramTuSdkSizeF, PointF paramPointF, float paramFloat)
  {
    float[] arrayOfFloat1 = new float[16];
    Matrix.setIdentityM(arrayOfFloat1, 0);
    float[] arrayOfFloat2 = new float[16];
    Matrix.setIdentityM(arrayOfFloat2, 0);
    Matrix.setIdentityM(this.e, 0);
    Matrix.orthoM(arrayOfFloat1, 0, 0.0F, paramTuSdkSize.width, 0.0F, paramTuSdkSize.height, -1.0F, 1.0F);
    Matrix.translateM(arrayOfFloat2, 0, paramPointF.x, paramPointF.y, 0.0F);
    if (paramFloat != 0.0F) {
      Matrix.rotateM(arrayOfFloat2, 0, paramFloat, 0.0F, 0.0F, 1.0F);
    }
    Matrix.scaleM(arrayOfFloat2, 0, paramTuSdkSizeF.width, paramTuSdkSizeF.height, 1.0F);
    Matrix.multiplyMM(this.f, 0, arrayOfFloat1, 0, arrayOfFloat2, 0);
  }
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKCore-3.1.0.jar!\org\lasque\tusdk\core\seles\tusdk\liveSticker\TuSDK2DImageFilter.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */