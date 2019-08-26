package org.lasque.tusdk.impl.view.widget.listview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import org.lasque.tusdk.core.view.listview.TuSdkGroupListView;

public abstract class TuGroupListView<T, V extends View, M, N extends View>
  extends TuSdkGroupListView<T, V, M, N>
{
  public TuGroupListView(Context paramContext, AttributeSet paramAttributeSet, int paramInt)
  {
    super(paramContext, paramAttributeSet, paramInt);
  }
  
  public TuGroupListView(Context paramContext, AttributeSet paramAttributeSet)
  {
    super(paramContext, paramAttributeSet);
  }
  
  public TuGroupListView(Context paramContext)
  {
    super(paramContext);
  }
  
  protected void initView()
  {
    setTotalFooterViewId(TuListTotalFootView.getLayoutId());
    setRefreshLayoutResId(TuRefreshListHeaderView.getLayoutId(), TuRefreshListFooterView.getLayoutId());
    super.initView();
  }
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKCore-3.1.0.jar!\org\lasque\tusdk\impl\view\widget\listview\TuGroupListView.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */