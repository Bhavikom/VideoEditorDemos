package org.lasque.tusdk.core.seles.tusdk.filters.flowabs;

import org.lasque.tusdk.core.seles.SelesGLProgram;
import org.lasque.tusdk.core.seles.filters.SelesThreeInputFilter;

public class TuSDKTfmMixFilter
  extends SelesThreeInputFilter
{
  private float a = 0.8F;
  private int b;
  
  public TuSDKTfmMixFilter()
  {
    super("-stfm4mix");
    disableSecondFrameCheck();
  }
  
  protected void onInitOnGLThread()
  {
    super.onInitOnGLThread();
    this.b = this.mFilterProgram.uniformIndex("uLightUp");
    setLightUp(this.a);
  }
  
  public void setLightUp(float paramFloat)
  {
    this.a = paramFloat;
    setFloat(paramFloat, this.b, this.mFilterProgram);
  }
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKCore-3.1.0.jar!\org\lasque\tusdk\core\seles\tusdk\filters\flowabs\TuSDKTfmMixFilter.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */