package org.lasque.tusdk.impl.view.widget.listview;

import android.content.Context;
import android.util.AttributeSet;
import org.lasque.tusdk.core.view.listview.TuSdkIndexPath;

public class TuDefaultLineListView
  extends TuArrayListView<String, TuDefaultLineListCellView>
{
  public TuDefaultLineListView(Context paramContext, AttributeSet paramAttributeSet, int paramInt)
  {
    super(paramContext, paramAttributeSet, paramInt);
  }
  
  public TuDefaultLineListView(Context paramContext, AttributeSet paramAttributeSet)
  {
    super(paramContext, paramAttributeSet);
  }
  
  public TuDefaultLineListView(Context paramContext)
  {
    super(paramContext);
  }
  
  public int getCellLayoutId()
  {
    if (super.getCellLayoutId() == 0) {
      setCellLayoutId(TuDefaultLineListCellView.getLayoutId());
    }
    return super.getCellLayoutId();
  }
  
  protected void onArrayListViewCreated(TuDefaultLineListCellView paramTuDefaultLineListCellView, TuSdkIndexPath paramTuSdkIndexPath) {}
  
  protected void onArrayListViewBinded(TuDefaultLineListCellView paramTuDefaultLineListCellView, TuSdkIndexPath paramTuSdkIndexPath) {}
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKCore-3.1.0.jar!\org\lasque\tusdk\impl\view\widget\listview\TuDefaultLineListView.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */