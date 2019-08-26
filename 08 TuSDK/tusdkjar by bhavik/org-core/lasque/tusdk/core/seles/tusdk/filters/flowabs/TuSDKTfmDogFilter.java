package org.lasque.tusdk.core.seles.tusdk.filters.flowabs;

import org.lasque.tusdk.core.seles.SelesGLProgram;
import org.lasque.tusdk.core.seles.filters.SelesThreeInputFilter;
import org.lasque.tusdk.core.struct.TuSdkSize;
import org.lasque.tusdk.core.struct.TuSdkSizeF;

public class TuSDKTfmDogFilter
  extends SelesThreeInputFilter
{
  private float a = 2.0F;
  private float b = 1.0F;
  private float c = 1.02F;
  private float d = 160.0F;
  private int e;
  private int f;
  private int g;
  private int h;
  private int i;
  
  public TuSDKTfmDogFilter()
  {
    super("-stfm2dogv", "-stfm2dogf");
  }
  
  protected void onInitOnGLThread()
  {
    super.onInitOnGLThread();
    this.e = this.mFilterProgram.uniformIndex("stepOffset");
    this.f = this.mFilterProgram.uniformIndex("stepLength");
    this.g = this.mFilterProgram.uniformIndex("uTau");
    this.h = this.mFilterProgram.uniformIndex("uSigma");
    this.i = this.mFilterProgram.uniformIndex("uPhi");
    setStepLength(this.a);
    setTau(this.b);
    setPhi(this.d);
    setupFilterForSize(sizeOfFBO());
    setSigma(this.c);
  }
  
  public void setupFilterForSize(TuSdkSize paramTuSdkSize)
  {
    super.setupFilterForSize(paramTuSdkSize);
    if ((paramTuSdkSize == null) || (!paramTuSdkSize.isSize())) {
      return;
    }
    TuSdkSizeF localTuSdkSizeF = TuSdkSizeF.create(1.0F / paramTuSdkSize.width, 1.0F / paramTuSdkSize.height);
    setSize(localTuSdkSizeF, this.e, this.mFilterProgram);
  }
  
  public void setStepLength(float paramFloat)
  {
    this.a = paramFloat;
    setFloat(paramFloat, this.f, this.mFilterProgram);
  }
  
  public void setTau(float paramFloat)
  {
    this.b = paramFloat;
    setFloat(paramFloat, this.g, this.mFilterProgram);
  }
  
  public void setSigma(float paramFloat)
  {
    this.c = paramFloat;
    setFloat(paramFloat, this.h, this.mFilterProgram);
  }
  
  public void setPhi(float paramFloat)
  {
    this.d = paramFloat;
    setFloat(paramFloat, this.i, this.mFilterProgram);
  }
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKCore-3.1.0.jar!\org\lasque\tusdk\core\seles\tusdk\filters\flowabs\TuSDKTfmDogFilter.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */