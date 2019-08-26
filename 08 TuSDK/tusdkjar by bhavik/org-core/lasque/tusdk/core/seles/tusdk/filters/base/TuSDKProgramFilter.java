package org.lasque.tusdk.core.seles.tusdk.filters.base;

import java.nio.FloatBuffer;
import org.lasque.tusdk.core.seles.filters.SelesFilter;
import org.lasque.tusdk.core.struct.TuSdkSize;

public class TuSDKProgramFilter
  extends SelesFilter
{
  public TuSDKProgramFilter(String paramString)
  {
    super(paramString);
  }
  
  public TuSDKProgramFilter(String paramString1, String paramString2)
  {
    super(paramString1, paramString2);
  }
  
  protected void onInitOnGLThread()
  {
    super.onInitOnGLThread();
    checkGLError(getClass().getSimpleName() + " onInitOnGLThread");
  }
  
  protected void renderToTexture(FloatBuffer paramFloatBuffer1, FloatBuffer paramFloatBuffer2)
  {
    super.renderToTexture(paramFloatBuffer1, paramFloatBuffer2);
    checkGLError(getClass().getSimpleName());
    captureFilterImage(getClass().getSimpleName(), this.mInputTextureSize.width, this.mInputTextureSize.height);
  }
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKCore-3.1.0.jar!\org\lasque\tusdk\core\seles\tusdk\filters\base\TuSDKProgramFilter.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */