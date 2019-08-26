package org.lasque.tusdk.video.editor;

import java.util.List;
import org.lasque.tusdk.core.seles.SelesParameters.FilterArg;

public abstract interface TuSdkMediaEffectParameterInterface
{
  public abstract SelesParameters.FilterArg getFilterArg(String paramString);
  
  public abstract List<SelesParameters.FilterArg> getFilterArgs();
  
  public abstract void submitParameter(String paramString, float paramFloat);
  
  public abstract void submitParameter(int paramInt, float paramFloat);
  
  public abstract void submitParameters();
  
  public abstract void resetParameters();
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKVideo-3.4.1.jar!\org\lasque\tusdk\video\editor\TuSdkMediaEffectParameterInterface.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */