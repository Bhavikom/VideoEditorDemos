package org.lasque.tusdk.modules.view.widget.smudge;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import org.lasque.tusdk.core.view.listview.TuSdkCellRelativeLayout;
import org.lasque.tusdk.core.view.listview.TuSdkListSelectableCellViewInterface;

public abstract class BrushBarItemCellBase
  extends TuSdkCellRelativeLayout<BrushData>
  implements TuSdkListSelectableCellViewInterface
{
  private BrushTableItemCellDelegate a;
  
  public abstract ImageView getImageView();
  
  public BrushBarItemCellBase(Context paramContext, AttributeSet paramAttributeSet, int paramInt)
  {
    super(paramContext, paramAttributeSet, paramInt);
  }
  
  public BrushBarItemCellBase(Context paramContext, AttributeSet paramAttributeSet)
  {
    super(paramContext, paramAttributeSet);
  }
  
  public BrushBarItemCellBase(Context paramContext)
  {
    super(paramContext);
  }
  
  public BrushTableItemCellDelegate getDelegate()
  {
    return this.a;
  }
  
  protected void bindModel()
  {
    BrushData localBrushData = (BrushData)getModel();
    if (localBrushData == null) {
      return;
    }
    if (localBrushData.code.equals("Eraser")) {
      handleTypeEraser(localBrushData);
    } else if (localBrushData.getType() == BrushData.BrushType.TypeOnline) {
      handleTypeOnline(localBrushData);
    } else {
      handleTypeBrush(localBrushData);
    }
  }
  
  public boolean canHiddenRemoveFlag()
  {
    return true;
  }
  
  protected void handleTypeEraser(BrushData paramBrushData) {}
  
  protected void handleTypeOnline(BrushData paramBrushData) {}
  
  protected void handleTypeBrush(BrushData paramBrushData)
  {
    if ((paramBrushData == null) || (getImageView() == null)) {
      return;
    }
    getImageView().setScaleType(ImageView.ScaleType.CENTER_CROP);
    BrushLocalPackage.shared().loadThumbWithBrush(getImageView(), paramBrushData);
  }
  
  protected void handleBlockView(int paramInt1, int paramInt2)
  {
    if ((getImageView() != null) && (paramInt2 != 0))
    {
      getImageView().setImageResource(paramInt2);
      getImageView().setScaleType(ImageView.ScaleType.CENTER);
    }
  }
  
  public void viewNeedRest()
  {
    super.viewNeedRest();
    setSelected(false);
    if (getImageView() != null)
    {
      getImageView().setImageBitmap(null);
      BrushLocalPackage.shared().cancelLoadImage(getImageView());
    }
  }
  
  public void onCellSelected(int paramInt)
  {
    setSelected(true);
  }
  
  public void onCellDeselected()
  {
    setSelected(false);
  }
  
  public static abstract interface BrushTableItemCellDelegate
  {
    public abstract void onBrushCellRemove(BrushBarItemCellBase paramBrushBarItemCellBase);
  }
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKCore-3.1.0.jar!\org\lasque\tusdk\modules\view\widget\smudge\BrushBarItemCellBase.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */