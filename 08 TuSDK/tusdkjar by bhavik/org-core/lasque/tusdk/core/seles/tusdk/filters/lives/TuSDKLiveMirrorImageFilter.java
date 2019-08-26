package org.lasque.tusdk.core.seles.tusdk.filters.lives;

import java.nio.FloatBuffer;
import org.lasque.tusdk.core.seles.filters.SelesFilter;
import org.lasque.tusdk.core.struct.TuSdkSize;

public class TuSDKLiveMirrorImageFilter
  extends SelesFilter
{
  public TuSDKLiveMirrorImageFilter()
  {
    super("-slive12f");
  }
  
  protected void onInitOnGLThread()
  {
    super.onInitOnGLThread();
    checkGLError("TuSDKLiveMirrorImageFilter");
  }
  
  protected void renderToTexture(FloatBuffer paramFloatBuffer1, FloatBuffer paramFloatBuffer2)
  {
    super.renderToTexture(paramFloatBuffer1, paramFloatBuffer2);
    checkGLError("TuSDKLiveMirrorImageFilter");
    captureFilterImage("TuSDKLiveMirrorImageFilter", this.mInputTextureSize.width, this.mInputTextureSize.height);
  }
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKCore-3.1.0.jar!\org\lasque\tusdk\core\seles\tusdk\filters\lives\TuSDKLiveMirrorImageFilter.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */