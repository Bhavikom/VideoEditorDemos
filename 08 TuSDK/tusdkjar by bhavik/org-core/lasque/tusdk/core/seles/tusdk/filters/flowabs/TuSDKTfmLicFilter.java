package org.lasque.tusdk.core.seles.tusdk.filters.flowabs;

import org.lasque.tusdk.core.seles.SelesGLProgram;
import org.lasque.tusdk.core.seles.filters.SelesFilter;
import org.lasque.tusdk.core.struct.TuSdkSize;
import org.lasque.tusdk.core.struct.TuSdkSizeF;

public class TuSDKTfmLicFilter
  extends SelesFilter
{
  private float a = 1.5F;
  private int b;
  private int c;
  
  public TuSDKTfmLicFilter()
  {
    super("-stfm3lic");
  }
  
  protected void onInitOnGLThread()
  {
    super.onInitOnGLThread();
    this.b = this.mFilterProgram.uniformIndex("stepOffset");
    this.c = this.mFilterProgram.uniformIndex("stepLength");
    setupFilterForSize(sizeOfFBO());
    setStepLength(this.a);
  }
  
  public void setupFilterForSize(TuSdkSize paramTuSdkSize)
  {
    super.setupFilterForSize(paramTuSdkSize);
    if ((paramTuSdkSize == null) || (!paramTuSdkSize.isSize())) {
      return;
    }
    TuSdkSizeF localTuSdkSizeF = TuSdkSizeF.create(1.0F / paramTuSdkSize.width, 1.0F / paramTuSdkSize.height);
    setSize(localTuSdkSizeF, this.b, this.mFilterProgram);
  }
  
  public void setStepLength(float paramFloat)
  {
    this.a = paramFloat;
    setFloat(paramFloat, this.c, this.mFilterProgram);
  }
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKCore-3.1.0.jar!\org\lasque\tusdk\core\seles\tusdk\filters\flowabs\TuSDKTfmLicFilter.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */