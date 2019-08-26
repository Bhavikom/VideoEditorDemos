package org.lasque.tusdk.core.seles.filters.image;

import android.opengl.GLES20;
import java.nio.FloatBuffer;
import org.lasque.tusdk.core.seles.SelesGLProgram;
import org.lasque.tusdk.core.seles.filters.SelesFilter;
import org.lasque.tusdk.core.struct.TuSdkSize;
import org.lasque.tusdk.core.utils.image.ImageOrientation;

public class SelesSharpenFilter
  extends SelesFilter
{
  private int a;
  private int b;
  private int c;
  private float d;
  
  public SelesSharpenFilter()
  {
    this(0.0F);
  }
  
  public SelesSharpenFilter(float paramFloat)
  {
    super("attribute vec4 position;\nattribute vec4 inputTextureCoordinate;\n\nuniform float imageWidthFactor; \nuniform float imageHeightFactor; \nuniform float sharpness;\n\nvarying vec2 textureCoordinate;\nvarying vec2 leftTextureCoordinate;\nvarying vec2 rightTextureCoordinate; \nvarying vec2 topTextureCoordinate;\nvarying vec2 bottomTextureCoordinate;\n\nvarying float centerMultiplier;\nvarying float edgeMultiplier;\n\nvoid main()\n{\n    gl_Position = position;\n    \n    mediump vec2 widthStep = vec2(imageWidthFactor, 0.0);\n    mediump vec2 heightStep = vec2(0.0, imageHeightFactor);\n    \n    textureCoordinate = inputTextureCoordinate.xy;\n    leftTextureCoordinate = inputTextureCoordinate.xy - widthStep;\n    rightTextureCoordinate = inputTextureCoordinate.xy + widthStep;\n    topTextureCoordinate = inputTextureCoordinate.xy + heightStep;     \n    bottomTextureCoordinate = inputTextureCoordinate.xy - heightStep;\n    \n    centerMultiplier = 1.0 + 4.0 * sharpness;\n    edgeMultiplier = sharpness;\n}", "precision highp float;\n\nvarying highp vec2 textureCoordinate;\nvarying highp vec2 leftTextureCoordinate;\nvarying highp vec2 rightTextureCoordinate; \nvarying highp vec2 topTextureCoordinate;\nvarying highp vec2 bottomTextureCoordinate;\n\nvarying highp float centerMultiplier;\nvarying highp float edgeMultiplier;\n\nuniform sampler2D inputImageTexture;\n\nvoid main()\n{\n    mediump vec3 textureColor = texture2D(inputImageTexture, textureCoordinate).rgb;\n    mediump vec3 leftTextureColor = texture2D(inputImageTexture, leftTextureCoordinate).rgb;\n    mediump vec3 rightTextureColor = texture2D(inputImageTexture, rightTextureCoordinate).rgb;\n    mediump vec3 topTextureColor = texture2D(inputImageTexture, topTextureCoordinate).rgb;\n    mediump vec3 bottomTextureColor = texture2D(inputImageTexture, bottomTextureCoordinate).rgb;\n\n    gl_FragColor = vec4((textureColor * centerMultiplier - (leftTextureColor * edgeMultiplier + rightTextureColor * edgeMultiplier + topTextureColor * edgeMultiplier + bottomTextureColor * edgeMultiplier)), texture2D(inputImageTexture, bottomTextureCoordinate).w);\n}");
    this.d = paramFloat;
  }
  
  protected void onInitOnGLThread()
  {
    super.onInitOnGLThread();
    this.a = this.mFilterProgram.uniformIndex("sharpness");
    this.b = this.mFilterProgram.uniformIndex("imageWidthFactor");
    this.c = this.mFilterProgram.uniformIndex("imageHeightFactor");
    setSharpness(this.d);
    checkGLError(getClass().getSimpleName() + " onInitOnGLThread");
  }
  
  public void setupFilterForSize(final TuSdkSize paramTuSdkSize)
  {
    runOnDraw(new Runnable()
    {
      public void run()
      {
        if (SelesSharpenFilter.a(SelesSharpenFilter.this).isTransposed())
        {
          GLES20.glUniform1f(SelesSharpenFilter.b(SelesSharpenFilter.this), 1.0F / paramTuSdkSize.height);
          GLES20.glUniform1f(SelesSharpenFilter.c(SelesSharpenFilter.this), 1.0F / paramTuSdkSize.width);
        }
        else
        {
          GLES20.glUniform1f(SelesSharpenFilter.b(SelesSharpenFilter.this), 1.0F / paramTuSdkSize.width);
          GLES20.glUniform1f(SelesSharpenFilter.c(SelesSharpenFilter.this), 1.0F / paramTuSdkSize.height);
        }
      }
    });
  }
  
  protected void renderToTexture(FloatBuffer paramFloatBuffer1, FloatBuffer paramFloatBuffer2)
  {
    super.renderToTexture(paramFloatBuffer1, paramFloatBuffer2);
    checkGLError(getClass().getSimpleName());
    captureFilterImage(getClass().getSimpleName(), this.mInputTextureSize.width, this.mInputTextureSize.height);
  }
  
  public void setSharpness(float paramFloat)
  {
    this.d = paramFloat;
    setFloat(this.d, this.a, this.mFilterProgram);
  }
  
  public float getSharpness()
  {
    return this.d;
  }
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKCore-3.1.0.jar!\org\lasque\tusdk\core\seles\filters\image\SelesSharpenFilter.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */