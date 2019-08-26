package org.lasque.tusdk.core.seles.sources;

import android.graphics.RectF;
import java.nio.FloatBuffer;
import org.lasque.tusdk.core.seles.extend.SelesVerticeCoordinateCorpBuilder;
import org.lasque.tusdk.core.seles.filters.SelesFilter;
import org.lasque.tusdk.core.struct.TuSdkSize;

public class SelesTextureReceiver
  extends SelesFilter
{
  private TuSdkSize a;
  private SelesVerticeCoordinateCorpBuilder b;
  private RectF c;
  private FloatBuffer d = SelesFilter.buildBuffer(SelesFilter.imageVertices);
  private FloatBuffer e = SelesFilter.buildBuffer(SelesFilter.noRotationTextureCoordinates);
  
  protected void onInitOnGLThread()
  {
    super.onInitOnGLThread();
    checkGLError(getClass().getSimpleName() + " onInitOnGLThread");
  }
  
  public void setTextureCoordinateBuilder(SelesVerticeCoordinateCorpBuilder paramSelesVerticeCoordinateCorpBuilder)
  {
    this.b = paramSelesVerticeCoordinateCorpBuilder;
    if ((this.b != null) && (this.c != null)) {
      this.b.setPreCropRect(this.c);
    }
  }
  
  public void setPreCropRect(RectF paramRectF)
  {
    this.c = paramRectF;
    if (this.b != null) {
      this.b.setPreCropRect(this.c);
    }
  }
  
  public void newFrameReadyInGLThread(long paramLong)
  {
    if ((this.b != null) && (this.b.calculate(this.mInputTextureSize, this.mInputRotation, this.d, this.e)))
    {
      this.a = this.b.outputSize();
    }
    else
    {
      this.e.clear();
      this.e.put(SelesFilter.textureCoordinates(this.mInputRotation)).position(0);
      this.a = this.mInputTextureSize;
    }
    renderToTexture(this.d, this.e);
    informTargetsAboutNewFrame(paramLong);
  }
  
  protected void renderToTexture(FloatBuffer paramFloatBuffer1, FloatBuffer paramFloatBuffer2)
  {
    super.renderToTexture(paramFloatBuffer1, paramFloatBuffer2);
    checkGLError(getClass().getSimpleName());
    captureFilterImage(getClass().getSimpleName(), this.mInputTextureSize.width, this.mInputTextureSize.height);
  }
  
  public TuSdkSize outputFrameSize()
  {
    return this.a == null ? this.mInputTextureSize : this.a;
  }
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKCore-3.1.0.jar!\org\lasque\tusdk\core\seles\sources\SelesTextureReceiver.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */