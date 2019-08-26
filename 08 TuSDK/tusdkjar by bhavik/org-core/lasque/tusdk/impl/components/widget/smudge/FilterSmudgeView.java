package org.lasque.tusdk.impl.components.widget.smudge;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.AttributeSet;
import org.lasque.tusdk.core.secret.SdkValid;
import org.lasque.tusdk.core.seles.tusdk.FilterWrap;
import org.lasque.tusdk.core.utils.TLog;

public class FilterSmudgeView
  extends SmudgeView
{
  public FilterSmudgeView(Context paramContext)
  {
    super(paramContext);
  }
  
  public FilterSmudgeView(Context paramContext, AttributeSet paramAttributeSet)
  {
    super(paramContext, paramAttributeSet);
  }
  
  public FilterSmudgeView(Context paramContext, AttributeSet paramAttributeSet, int paramInt)
  {
    super(paramContext, paramAttributeSet, paramInt);
  }
  
  protected SimpleProcessor getProcessorInstance()
  {
    if (!SdkValid.shared.wipeFilterEnabled()) {
      return null;
    }
    if (this.mSmudgeProcessor == null) {
      this.mSmudgeProcessor = new FilterSmudgeProcessor();
    }
    return this.mSmudgeProcessor;
  }
  
  public void setImageBitmap(Bitmap paramBitmap)
  {
    if (!SdkValid.shared.wipeFilterEnabled())
    {
      TLog.e("You are not allowed to use the wipe-filter feature, please see http://tusdk.com", new Object[0]);
      return;
    }
    super.setImageBitmap(paramBitmap);
  }
  
  public FilterWrap getFilterWrap()
  {
    if (getProcessorInstance() != null)
    {
      FilterSmudgeProcessor localFilterSmudgeProcessor = (FilterSmudgeProcessor)getProcessorInstance();
      return localFilterSmudgeProcessor.getFilterWrap();
    }
    return null;
  }
  
  public final void setFilterWrap(FilterWrap paramFilterWrap)
  {
    if (getProcessorInstance() != null)
    {
      FilterSmudgeProcessor localFilterSmudgeProcessor = (FilterSmudgeProcessor)getProcessorInstance();
      localFilterSmudgeProcessor.setFilterWrap(paramFilterWrap);
    }
  }
  
  protected void updateBrushSettings()
  {
    if (getProcessorInstance() == null) {
      return;
    }
    getProcessorInstance().setBrushSize(getBrushSize());
  }
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKCore-3.1.0.jar!\org\lasque\tusdk\impl\components\widget\smudge\FilterSmudgeView.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */