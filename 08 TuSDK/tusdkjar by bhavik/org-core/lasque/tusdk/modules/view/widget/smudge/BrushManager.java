package org.lasque.tusdk.modules.view.widget.smudge;

import java.util.List;
import org.lasque.tusdk.core.TuSdkConfigs;

public class BrushManager
{
  private static BrushManager a;
  private BrushLocalPackage b;
  
  public static BrushManager shared()
  {
    return a;
  }
  
  public static BrushManager init(TuSdkConfigs paramTuSdkConfigs)
  {
    if ((a == null) && (paramTuSdkConfigs != null)) {
      a = new BrushManager(paramTuSdkConfigs);
    }
    return a;
  }
  
  public List<String> getBrushNames()
  {
    return this.b.getCodes();
  }
  
  public boolean isInited()
  {
    return this.b.isInited();
  }
  
  private BrushManager(TuSdkConfigs paramTuSdkConfigs)
  {
    this.b = BrushLocalPackage.init(paramTuSdkConfigs);
  }
  
  public BrushData getBrushWithCode(String paramString)
  {
    return this.b.getBrushWithCode(paramString);
  }
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKCore-3.1.0.jar!\org\lasque\tusdk\modules\view\widget\smudge\BrushManager.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */