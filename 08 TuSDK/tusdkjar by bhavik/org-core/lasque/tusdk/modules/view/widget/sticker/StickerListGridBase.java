package org.lasque.tusdk.modules.view.widget.sticker;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;
import org.lasque.tusdk.core.view.listview.TuSdkCellRelativeLayout;

public abstract class StickerListGridBase
  extends TuSdkCellRelativeLayout<StickerData>
{
  public abstract ImageView getPosterView();
  
  public StickerListGridBase(Context paramContext, AttributeSet paramAttributeSet, int paramInt)
  {
    super(paramContext, paramAttributeSet, paramInt);
  }
  
  public StickerListGridBase(Context paramContext, AttributeSet paramAttributeSet)
  {
    super(paramContext, paramAttributeSet);
  }
  
  public StickerListGridBase(Context paramContext)
  {
    super(paramContext);
  }
  
  protected void bindModel()
  {
    StickerLocalPackage.shared().loadThumb((StickerData)getModel(), getPosterView());
  }
  
  public void viewNeedRest()
  {
    StickerLocalPackage.shared().cancelLoadImage(getPosterView());
    if (getPosterView() != null) {
      getPosterView().setImageBitmap(null);
    }
    super.viewNeedRest();
  }
  
  public void viewWillDestory()
  {
    viewNeedRest();
    super.viewWillDestory();
  }
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKCore-3.1.0.jar!\org\lasque\tusdk\modules\view\widget\sticker\StickerListGridBase.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */