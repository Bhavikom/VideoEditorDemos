package org.lasque.tusdk.core.seles.filters.image;

import android.opengl.GLES20;
import java.nio.FloatBuffer;
import org.lasque.tusdk.core.seles.SelesContext;
import org.lasque.tusdk.core.seles.SelesGLProgram;
import org.lasque.tusdk.core.seles.filters.SelesTwoPassTextureSamplingFilter;
import org.lasque.tusdk.core.struct.TuSdkSize;
import org.lasque.tusdk.core.utils.TLog;

public class SelesGaussianBlurFilter
  extends SelesTwoPassTextureSamplingFilter
{
  private boolean a;
  private float b;
  private float c;
  private float d;
  private float e;
  private int f;
  
  public SelesGaussianBlurFilter()
  {
    this(vertexShaderForOptimizedBlur(4, 2.0F), fragmentShaderForOptimizedBlur(4, 2.0F));
  }
  
  public SelesGaussianBlurFilter(String paramString1, String paramString2)
  {
    this(paramString1, paramString2, paramString1, paramString2);
  }
  
  public SelesGaussianBlurFilter(String paramString1, String paramString2, String paramString3, String paramString4)
  {
    super(paramString1, paramString2, paramString3, paramString4);
    setTexelSpacingMultiplier(1.0F);
    this.b = 2.0F;
    this.a = false;
  }
  
  public void setupFilterForSize(TuSdkSize paramTuSdkSize)
  {
    super.setupFilterForSize(paramTuSdkSize);
    if (this.a) {
      if (getBlurRadiusAsFractionOfImageWidth() > 0.0F) {
        setBlurRadiusInPixels(paramTuSdkSize.width * getBlurRadiusAsFractionOfImageWidth());
      } else {
        setBlurRadiusInPixels(paramTuSdkSize.height * getBlurRadiusAsFractionOfImageHeight());
      }
    }
  }
  
  protected void renderToTexture(FloatBuffer paramFloatBuffer1, FloatBuffer paramFloatBuffer2)
  {
    super.renderToTexture(paramFloatBuffer1, paramFloatBuffer2);
    for (int i = 1; i < this.f; i++) {
      super.renderToTexture(paramFloatBuffer1, this.mNoRotationTextureBuffer);
    }
  }
  
  public void switchTo(final String paramString1, final String paramString2)
  {
    runOnDraw(new Runnable()
    {
      public void run()
      {
        SelesGaussianBlurFilter.a(SelesGaussianBlurFilter.this, SelesContext.program(paramString1, paramString2));
        if (!SelesGaussianBlurFilter.a(SelesGaussianBlurFilter.this).isInitialized())
        {
          SelesGaussianBlurFilter.b(SelesGaussianBlurFilter.this);
          if (!SelesGaussianBlurFilter.c(SelesGaussianBlurFilter.this).link())
          {
            TLog.i("Program link log: %s", new Object[] { SelesGaussianBlurFilter.d(SelesGaussianBlurFilter.this).getProgramLog() });
            TLog.i("Fragment shader compile log: %s", new Object[] { SelesGaussianBlurFilter.e(SelesGaussianBlurFilter.this).getFragmentShaderLog() });
            TLog.i("Vertex link log: %s", new Object[] { SelesGaussianBlurFilter.f(SelesGaussianBlurFilter.this).getVertexShaderLog() });
            SelesGaussianBlurFilter.b(SelesGaussianBlurFilter.this, null);
            TLog.e("Filter shader link failed: %s", new Object[] { getClass() });
            return;
          }
        }
        SelesGaussianBlurFilter.a(SelesGaussianBlurFilter.this, SelesGaussianBlurFilter.g(SelesGaussianBlurFilter.this).attributeIndex("position"));
        SelesGaussianBlurFilter.b(SelesGaussianBlurFilter.this, SelesGaussianBlurFilter.h(SelesGaussianBlurFilter.this).attributeIndex("inputTextureCoordinate"));
        SelesGaussianBlurFilter.c(SelesGaussianBlurFilter.this, SelesGaussianBlurFilter.i(SelesGaussianBlurFilter.this).uniformIndex("inputImageTexture"));
        SelesGaussianBlurFilter.d(SelesGaussianBlurFilter.this, SelesGaussianBlurFilter.j(SelesGaussianBlurFilter.this).uniformIndex("texelWidthOffset"));
        SelesGaussianBlurFilter.e(SelesGaussianBlurFilter.this, SelesGaussianBlurFilter.k(SelesGaussianBlurFilter.this).uniformIndex("texelHeightOffset"));
        SelesContext.setActiveShaderProgram(SelesGaussianBlurFilter.l(SelesGaussianBlurFilter.this));
        GLES20.glEnableVertexAttribArray(SelesGaussianBlurFilter.m(SelesGaussianBlurFilter.this));
        GLES20.glEnableVertexAttribArray(SelesGaussianBlurFilter.n(SelesGaussianBlurFilter.this));
        SelesGaussianBlurFilter.c(SelesGaussianBlurFilter.this, SelesContext.program(paramString1, paramString2));
        if (!SelesGaussianBlurFilter.o(SelesGaussianBlurFilter.this).isInitialized())
        {
          SelesGaussianBlurFilter.p(SelesGaussianBlurFilter.this);
          if (!SelesGaussianBlurFilter.q(SelesGaussianBlurFilter.this).link())
          {
            TLog.i("Program link log: %s", new Object[] { SelesGaussianBlurFilter.r(SelesGaussianBlurFilter.this).getProgramLog() });
            TLog.i("Fragment shader compile log: %s", new Object[] { SelesGaussianBlurFilter.s(SelesGaussianBlurFilter.this).getFragmentShaderLog() });
            TLog.i("Vertex link log: %s", new Object[] { SelesGaussianBlurFilter.t(SelesGaussianBlurFilter.this).getVertexShaderLog() });
            SelesGaussianBlurFilter.d(SelesGaussianBlurFilter.this, null);
            TLog.e("Filter shader link failed: %s", new Object[] { getClass() });
            return;
          }
        }
        SelesGaussianBlurFilter.f(SelesGaussianBlurFilter.this, SelesGaussianBlurFilter.u(SelesGaussianBlurFilter.this).attributeIndex("position"));
        SelesGaussianBlurFilter.g(SelesGaussianBlurFilter.this, SelesGaussianBlurFilter.v(SelesGaussianBlurFilter.this).attributeIndex("inputTextureCoordinate"));
        SelesGaussianBlurFilter.h(SelesGaussianBlurFilter.this, SelesGaussianBlurFilter.w(SelesGaussianBlurFilter.this).uniformIndex("inputImageTexture"));
        SelesGaussianBlurFilter.i(SelesGaussianBlurFilter.this, SelesGaussianBlurFilter.x(SelesGaussianBlurFilter.this).uniformIndex("inputImageTexture2"));
        SelesGaussianBlurFilter.j(SelesGaussianBlurFilter.this, SelesGaussianBlurFilter.y(SelesGaussianBlurFilter.this).uniformIndex("texelWidthOffset"));
        SelesGaussianBlurFilter.k(SelesGaussianBlurFilter.this, SelesGaussianBlurFilter.z(SelesGaussianBlurFilter.this).uniformIndex("texelHeightOffset"));
        SelesContext.setActiveShaderProgram(SelesGaussianBlurFilter.A(SelesGaussianBlurFilter.this));
        GLES20.glEnableVertexAttribArray(SelesGaussianBlurFilter.B(SelesGaussianBlurFilter.this));
        GLES20.glEnableVertexAttribArray(SelesGaussianBlurFilter.C(SelesGaussianBlurFilter.this));
        SelesGaussianBlurFilter.this.setupFilterForSize(SelesGaussianBlurFilter.this.sizeOfFBO());
        GLES20.glFlush();
      }
    });
  }
  
  public boolean isShouldResizeBlurRadiusWithImageSize()
  {
    return this.a;
  }
  
  public void setShouldResizeBlurRadiusWithImageSize(boolean paramBoolean)
  {
    this.a = paramBoolean;
  }
  
  public float getBlurRadiusInPixels()
  {
    return this.b;
  }
  
  public void setBlurRadiusInPixels(float paramFloat)
  {
    if (Math.round(paramFloat) != this.b)
    {
      this.b = Math.round(paramFloat);
      int i = 0;
      if (this.b >= 1.0F)
      {
        float f1 = 0.00390625F;
        i = (int)Math.floor(Math.sqrt(-2.0D * Math.pow(this.b, 2.0D) * Math.log(f1 * Math.sqrt(6.283185307179586D * Math.pow(this.b, 2.0D)))));
        i += i % 2;
      }
      String str1 = vertexShaderForOptimizedBlur(i, this.b);
      String str2 = fragmentShaderForOptimizedBlur(i, this.b);
      switchTo(str1, str2);
    }
    this.a = false;
  }
  
  public float getTexelSpacingMultiplier()
  {
    return this.c;
  }
  
  public void setTexelSpacingMultiplier(float paramFloat)
  {
    this.c = paramFloat;
    this.mVerticalTexelSpacing = paramFloat;
    this.mHorizontalTexelSpacing = paramFloat;
    setupFilterForSize(sizeOfFBO());
  }
  
  public float getBlurRadiusAsFractionOfImageWidth()
  {
    return this.d;
  }
  
  public void setBlurRadiusAsFractionOfImageWidth(float paramFloat)
  {
    if (paramFloat < 0.0F) {
      return;
    }
    this.a = ((this.d != paramFloat) && (paramFloat > 0.0F));
    this.d = paramFloat;
    this.e = 0.0F;
  }
  
  public float getBlurRadiusAsFractionOfImageHeight()
  {
    return this.e;
  }
  
  public void setBlurRadiusAsFractionOfImageHeight(float paramFloat)
  {
    if (paramFloat < 0.0F) {
      return;
    }
    this.a = ((this.e != paramFloat) && (paramFloat > 0.0F));
    this.e = paramFloat;
    this.d = 0.0F;
  }
  
  public int getBlurPasses()
  {
    return this.f;
  }
  
  public void setBlurPasses(int paramInt)
  {
    this.f = paramInt;
  }
  
  public static String vertexShaderForOptimizedBlur(int paramInt, float paramFloat)
  {
    if (paramInt < 1) {
      return "attribute vec4 position;attribute vec4 inputTextureCoordinate;varying vec2 textureCoordinate;void main(){    gl_Position = position;    textureCoordinate = inputTextureCoordinate.xy;}";
    }
    float[] arrayOfFloat1 = new float[paramInt + 1];
    float f1 = 0.0F;
    for (int i = 0; i < paramInt + 1; i++)
    {
      arrayOfFloat1[i] = ((float)(1.0D / Math.sqrt(6.283185307179586D * Math.pow(paramFloat, 2.0D)) * Math.exp(-Math.pow(i, 2.0D) / (2.0D * Math.pow(paramFloat, 2.0D)))));
      if (i == 0) {
        f1 += arrayOfFloat1[i];
      } else {
        f1 = (float)(f1 + 2.0D * arrayOfFloat1[i]);
      }
    }
    for (i = 0; i < paramInt + 1; i++) {
      arrayOfFloat1[i] /= f1;
    }
    i = Math.min(paramInt / 2 + paramInt % 2, 7);
    float[] arrayOfFloat2 = new float[i];
    for (int j = 0; j < i; j++)
    {
      k = j * 2 + 1;
      float f2 = 0.0F;
      if (k < arrayOfFloat1.length) {
        f2 = arrayOfFloat1[k];
      }
      k++;
      float f3 = 0.0F;
      if (k < arrayOfFloat1.length) {
        f3 = arrayOfFloat1[k];
      }
      float f4 = f2 + f3;
      arrayOfFloat2[j] = ((f2 * (j * 2 + 1) + f3 * (j * 2 + 2)) / f4);
    }
    String str = "attribute vec4 position;\nattribute vec4 inputTextureCoordinate;\n\nuniform float texelWidthOffset;\nuniform float texelHeightOffset;\n\nvarying vec2 blurCoordinates[" + (1 + i * 2) + "];\n\nvoid main()\n{\n   gl_Position = position;\n   \n   vec2 singleStepOffset = vec2(texelWidthOffset, texelHeightOffset);\n";
    str = str + "blurCoordinates[0] = inputTextureCoordinate.xy;\n";
    for (int k = 0; k < i; k++)
    {
      str = str + String.format("blurCoordinates[%d] = inputTextureCoordinate.xy + singleStepOffset * %f;\n", new Object[] { Integer.valueOf(k * 2 + 1), Float.valueOf(arrayOfFloat2[k]) });
      str = str + String.format("blurCoordinates[%d] = inputTextureCoordinate.xy - singleStepOffset * %f;\n", new Object[] { Integer.valueOf(k * 2 + 2), Float.valueOf(arrayOfFloat2[k]) });
    }
    str = str + "}\n";
    return str;
  }
  
  public static String fragmentShaderForOptimizedBlur(int paramInt, float paramFloat)
  {
    if (paramInt < 1) {
      return "varying highp vec2 textureCoordinate;uniform sampler2D inputImageTexture;void main(){     gl_FragColor = texture2D(inputImageTexture, textureCoordinate);}";
    }
    float[] arrayOfFloat = new float[paramInt + 1];
    float f1 = 0.0F;
    for (int i = 0; i < paramInt + 1; i++)
    {
      arrayOfFloat[i] = ((float)(1.0D / Math.sqrt(6.283185307179586D * Math.pow(paramFloat, 2.0D)) * Math.exp(-Math.pow(i, 2.0D) / (2.0D * Math.pow(paramFloat, 2.0D)))));
      if (i == 0) {
        f1 += arrayOfFloat[i];
      } else {
        f1 = (float)(f1 + 2.0D * arrayOfFloat[i]);
      }
    }
    for (i = 0; i < paramInt + 1; i++) {
      arrayOfFloat[i] /= f1;
    }
    i = Math.min(paramInt / 2 + paramInt % 2, 7);
    int j = paramInt / 2 + paramInt % 2;
    String str = "uniform sampler2D inputImageTexture;\nuniform highp float texelWidthOffset;\nuniform highp float texelHeightOffset;\n\nvarying highp vec2 blurCoordinates[" + (1 + i * 2) + "];\n\nvoid main()\n{\n   lowp vec4 sum = vec4(0.0);\n";
    str = str + String.format("sum += texture2D(inputImageTexture, blurCoordinates[0]) * %f;\n", new Object[] { Float.valueOf(arrayOfFloat[0]) });
    int m;
    float f2;
    float f3;
    float f4;
    for (int k = 0; k < i; k++)
    {
      m = k * 2 + 1;
      f2 = 0.0F;
      if (m < arrayOfFloat.length) {
        f2 = arrayOfFloat[m];
      }
      m++;
      f3 = 0.0F;
      if (m < arrayOfFloat.length) {
        f3 = arrayOfFloat[m];
      }
      f4 = f2 + f3;
      str = str + String.format("sum += texture2D(inputImageTexture, blurCoordinates[%d]) * %f;\n", new Object[] { Integer.valueOf(k * 2 + 1), Float.valueOf(f4) });
      str = str + String.format("sum += texture2D(inputImageTexture, blurCoordinates[%d]) * %f;\n", new Object[] { Integer.valueOf(k * 2 + 2), Float.valueOf(f4) });
    }
    if (j > i)
    {
      str = str + "highp vec2 singleStepOffset = vec2(texelWidthOffset, texelHeightOffset);\n";
      for (k = i; k < j; k++)
      {
        m = k * 2 + 1;
        f2 = 0.0F;
        if (m < arrayOfFloat.length) {
          f2 = arrayOfFloat[m];
        }
        m++;
        f3 = 0.0F;
        if (m < arrayOfFloat.length) {
          f3 = arrayOfFloat[m];
        }
        f4 = f2 + f3;
        float f5 = (f2 * (k * 2 + 1) + f3 * (k * 2 + 2)) / f4;
        str = str + String.format("sum += texture2D(inputImageTexture, blurCoordinates[0] + singleStepOffset * %f) * %f;\n", new Object[] { Float.valueOf(f5), Float.valueOf(f4) });
        str = str + String.format("sum += texture2D(inputImageTexture, blurCoordinates[0] - singleStepOffset * %f) * %f;\n", new Object[] { Float.valueOf(f5), Float.valueOf(f4) });
      }
    }
    str = str + "\tgl_FragColor = sum;\n}\n";
    return str;
  }
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKCore-3.1.0.jar!\org\lasque\tusdk\core\seles\filters\image\SelesGaussianBlurFilter.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */