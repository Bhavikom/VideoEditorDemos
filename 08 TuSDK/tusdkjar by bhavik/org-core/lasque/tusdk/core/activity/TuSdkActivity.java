package org.lasque.tusdk.core.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import org.lasque.tusdk.core.TuSdkContext;
import org.lasque.tusdk.core.view.TuSdkViewHelper;

public class TuSdkActivity
  extends Activity
{
  public <T extends View> T getViewById(int paramInt)
  {
    View localView = findViewById(paramInt);
    return TuSdkViewHelper.loadView(localView);
  }
  
  public <T extends View> T getViewById(String paramString)
  {
    int i = TuSdkContext.getIDResId(paramString);
    if (i == 0) {
      return null;
    }
    return getViewById(i);
  }
  
  public void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    initView();
  }
  
  protected void initView() {}
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKCore-3.1.0.jar!\org\lasque\tusdk\core\activity\TuSdkActivity.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */