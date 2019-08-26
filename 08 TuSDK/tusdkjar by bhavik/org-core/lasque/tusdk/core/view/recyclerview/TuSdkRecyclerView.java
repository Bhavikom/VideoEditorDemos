package org.lasque.tusdk.core.view.recyclerview;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import org.lasque.tusdk.core.view.TuSdkViewInterface;

public class TuSdkRecyclerView
  extends RecyclerView
  implements TuSdkViewInterface
{
  public TuSdkRecyclerView(Context paramContext)
  {
    super(paramContext);
    initView();
  }
  
  public TuSdkRecyclerView(Context paramContext, AttributeSet paramAttributeSet)
  {
    super(paramContext, paramAttributeSet);
    initView();
  }
  
  public TuSdkRecyclerView(Context paramContext, AttributeSet paramAttributeSet, int paramInt)
  {
    super(paramContext, paramAttributeSet, paramInt);
    initView();
  }
  
  protected void initView() {}
  
  public void loadView() {}
  
  public void viewDidLoad() {}
  
  public void viewNeedRest() {}
  
  public void viewWillDestory() {}
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKCore-3.1.0.jar!\org\lasque\tusdk\core\view\recyclerview\TuSdkRecyclerView.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */