package org.lasque.tusdk.impl.view.widget;

import java.util.List;

public abstract interface ParameterConfigViewInterface
{
  public abstract void setDelegate(ParameterConfigViewDelegate paramParameterConfigViewDelegate);
  
  public abstract void seekTo(float paramFloat);
  
  public abstract void setParams(List<String> paramList, int paramInt);
  
  public static abstract interface ParameterConfigViewDelegate
  {
    public abstract void onParameterConfigDataChanged(ParameterConfigViewInterface paramParameterConfigViewInterface, int paramInt, float paramFloat);
    
    public abstract float readParameterValue(ParameterConfigViewInterface paramParameterConfigViewInterface, int paramInt);
    
    public abstract void onParameterConfigRest(ParameterConfigViewInterface paramParameterConfigViewInterface, int paramInt);
  }
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKCore-3.1.0.jar!\org\lasque\tusdk\impl\view\widget\ParameterConfigViewInterface.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */